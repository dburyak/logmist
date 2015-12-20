package dburyak.logmist.ui.jfx;


import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.logmist.Resources;
import dburyak.logmist.Resources.ConfigID;
import dburyak.logmist.Resources.MsgID;
import dburyak.logmist.Resources.UIConfigID;
import dburyak.logmist.Resources.UserFileStatus;
import dburyak.logmist.exceptions.UnableToLaunchException;
import dburyak.logmist.ui.LoggingExceptionHandlingThreadFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Screen;
import javafx.stage.Stage;


// FIXME : code style

public final class LogmistJFXApp extends Application {

    private static final Logger LOG = LogManager.getFormatterLogger(LogmistJFXApp.class);
    private static LogmistJFXApp INSTANCE = null;

    private Stage stage = null;
    private final HashSet<Service<?>> services = new HashSet<>();
    private ExecutorService threadPool;


    public static final LogmistJFXApp getInstance() {
        if (INSTANCE == null) {
            throw LOG.throwing(Level.DEBUG, new AssertionError());
        }
        return INSTANCE;
    }

    public final ExecutorService getThreadPool() {
        return threadPool;
    }

    public final boolean registerService(final Service<?> service) {
        return services.add(service);
    }

    public final boolean unregisterService(final Service<?> service) {
        return services.remove(service);
    }

    @SuppressWarnings("boxing")
    private final void initThreadPool() {
        final int numProc = Runtime.getRuntime().availableProcessors();
        final int numThreadsAdd = Integer.parseInt(Resources.getInstance().getConfigProp(
            ConfigID.CORE_THREAD_POOL_SIZE_ADD));
        final long keepAliveTimeS = Long.parseLong(Resources.getInstance().getConfigProp(
            ConfigID.CORE_THREAD_POOL_KEEP_ALIVE_TIME_S));
        final int taskQueueCapacity = Integer.parseInt(Resources.getInstance().getConfigProp(
            ConfigID.CORE_THREAD_POOL_TASK_QUEUE_CAPACITY));

        final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>(taskQueueCapacity);
        final ThreadPoolExecutor wideInterfaceThreadPool = new ThreadPoolExecutor(
            numProc, numProc + numThreadsAdd, keepAliveTimeS, TimeUnit.SECONDS, taskQueue);
        final ThreadFactory exLoggingThreadFactory = new LoggingExceptionHandlingThreadFactory(
            wideInterfaceThreadPool.getThreadFactory());
        wideInterfaceThreadPool.setThreadFactory(exLoggingThreadFactory);
        threadPool = wideInterfaceThreadPool; // upcast
        LOG.debug("thread pool inited : numThreads = [%d]", (numProc + numThreadsAdd)); //$NON-NLS-1$
    }

    @Override
    public void start(final Stage primaryStage) throws UnableToLaunchException {
        INSTANCE = this;
        stage = primaryStage;
        try {
            final boolean hasResources = checkResources();
            if (!hasResources) {
                return; // application exit
            }
            checkUserConfigFilesStatus();
            initThreadPool();

            final Parent root = buildRootPane();
            final Scene scene = buildScene(root);
            initStage(primaryStage, scene);
            initLogmistJFXApp(this);
            primaryStage.show();
        } catch (final Exception e) {
            LOG.catching(Level.DEBUG, e);
            throw LOG.throwing(Level.DEBUG, new UnableToLaunchException(getClass().getSimpleName(), e));
        }
    }

    @SuppressWarnings("nls")
    private final int stopServices() {
        services.stream().forEach(srv -> {
            if (srv.isRunning()) {
                if (srv.cancel()) {
                    LOG.debug("service cancelled successfully : srv = [%s]", srv);
                } else {
                    LOG.warn("service cancel FAILED : srv = [%s]", srv);
                }
            } else {
                LOG.debug("service is not running, no need to stop it : srv = [%s]", srv);
            }
        });
        return services.size();
    }

    private final void initLogmistJFXApp(final LogmistJFXApp app) {
        app.getStage().setOnCloseRequest(event -> {
            exit();
        });

        app.getStage().maximizedProperty().addListener(
            (final ObservableValue<? extends Boolean> ov, final Boolean oldValue, final Boolean newValue) -> {
                if ((oldValue == false) && (newValue == true)) { // normal -> maximized
                    final Resources res = Resources.getInstance();
                    final double width = app.getStage().getWidth();
                    final double height = app.getStage().getHeight();
                    res.setUIProp(UIConfigID.MAIN_WINDOW_WIDTH, Double.toString(width));
                    res.setUIProp(UIConfigID.MAIN_WINDOW_HEIGHT, Double.toString(height));
                }
            });
    }

    /**
     * Close logmist application.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> IO activity (preferences files saving), exit from Java runtime (application close) is
     * performed
     * <br/><b>Created on:</b> <i>9:57:52 PM Oct 5, 2015</i>
     */
    @SuppressWarnings({ "nls", "boxing" })
    public final void exit() {
        final Resources res = Resources.getInstance();
        final int numServices = stopServices();
        LOG.debug("running services cancelled : numServices = [%d]", numServices);
        threadPool.shutdown();
        LOG.debug("waiting for running thread pool to be stopped ...");
        try {
            final long tpShutdownTimeoutMS = Long.parseLong(res.getConfigProp(
                ConfigID.CORE_THREAD_POOL_AWAIT_TERMINATION_TIMEOUT_MS));
            threadPool.awaitTermination(tpShutdownTimeoutMS, TimeUnit.MILLISECONDS);
            LOG.debug("all threads in pool stopped");
        } catch (final Exception e) {
            LOG.catching(Level.TRACE, e);
            LOG.error("error while waiting application thread pool termination", e);
        }

        try {
            // persist user config
            // save maximized state
            final boolean isMaximized = stage.isMaximized();
            res.setUIProp(UIConfigID.MAIN_WINDOW_MAXIMIZED, Boolean.toString(isMaximized));

            if (!isMaximized) {
                // save position
                res.setUIProp(UIConfigID.MAIN_WINDOW_XPOS, Double.toString(stage.getX()));
                res.setUIProp(UIConfigID.MAIN_WINDOW_YPOS, Double.toString(stage.getY()));

                // save dimensions
                res.setUIProp(UIConfigID.MAIN_WINDOW_WIDTH, Double.toString(stage.getWidth()));
                res.setUIProp(UIConfigID.MAIN_WINDOW_HEIGHT, Double.toString(stage.getHeight()));
            }

            Resources.getInstance().persist();
        } catch (final Exception e) {
            LOG.error("user config was not saved", e);
        } finally {
            Platform.exit();
            LOG.info("Platform exited, exiting Java");
            System.exit(0);
        }
    }

    public final Stage getStage() {
        if (stage == null) {
            throw LOG.throwing(Level.DEBUG, new AssertionError());
        }
        return stage;
    }

    private final void showStartupAlert(
        final AlertType type,
        final String header,
        final String text,
        final boolean blocking) {
        final Alert dialog = new Alert(type);
        dialog.setTitle(type.name());
        dialog.setHeaderText(header);
        dialog.setContentText(text);
        if (blocking) {
            dialog.showAndWait();
        } else {
            dialog.show();
        }
    }

    private final boolean checkResources() {
        try {
            @SuppressWarnings("unused") final Resources res = Resources.getInstance();
            return true;
        } catch (final AssertionError | RuntimeException e) {
            LOG.catching(Level.DEBUG, e);
            // resources cannot be initialized, thus use hard-code (actually, locale is stored in config file)
            final String header = "Resources Error";
            final String text =
                "Important resource file not found. This happened due to incorrectly built application. "
                    + "Logmist cannot be started. Please, contact developers for more information.\n"
                    + "https://github.com/dburyak/logmist\n"
                    + "dmytro.buryak@gmail.com";
            showStartupAlert(AlertType.ERROR, header, text, true);
            return false;
        }
    }

    private final void checkUserConfigFilesStatus() {
        final UserFileStatus config = Resources.getInstance().getConfigStatus();
        final Resources res = Resources.getInstance();
        if (!config.exists()) {
            final String header = res.getMsg(MsgID.APP_RESOURCES_NO_USER_CONFIG_TITLE);
            final String text = res.getMsg(MsgID.APP_RESOURCES_NO_USER_CONFIG_TEXT);
            showStartupAlert(AlertType.INFORMATION, header, text, true);
        } else { // user config exists, check read/write permissions
            if (!config.canRead()) {
                final String header = res.getMsg(MsgID.APP_RESOURCES_NON_READABLE_USER_CONFIG_TITLE);
                final String text = res.getMsg(MsgID.APP_RESOURCES_NON_READABLE_USER_CONFIG_TEXT);
                showStartupAlert(AlertType.INFORMATION, header, text, true);
            }
            if (!config.canWrite()) {
                final String header = res.getMsg(MsgID.APP_RESOURCES_NON_WRITABLE_USER_CONFIG_TITLE);
                final String text = res.getMsg(MsgID.APP_RESOURCES_NON_WRITABLE_USER_CONFIG_TEXT);
                showStartupAlert(AlertType.INFORMATION, header, text, true);
            }
        }

        final UserFileStatus ui = Resources.getInstance().getUIConfigStatus();
        if (!ui.exists()) {
            final String header = res.getMsg(MsgID.APP_RESOURCES_NO_UI_CONFIG_TITLE);
            final String text = res.getMsg(MsgID.APP_RESOURCES_NO_UI_CONFIG_TEXT);
            showStartupAlert(AlertType.INFORMATION, header, text, true);
        } else { // ui config exists, check read/write permissions
            if (!ui.canRead()) {
                final String header = res.getMsg(MsgID.APP_RESOURCES_NON_READABLE_UI_CONFIG_TITLE);
                final String text = res.getMsg(MsgID.APP_RESOURCES_NON_READABLE_UI_CONFIG_TEXT);
                showStartupAlert(AlertType.INFORMATION, header, text, true);
            }
            if (!ui.canWrite()) {
                final String header = res.getMsg(MsgID.APP_RESOURCES_NON_WRITABLE_UI_CONFIG_TITLE);
                final String text = res.getMsg(MsgID.APP_RESOURCES_NON_WRITABLE_UI_CONFIG_TEXT);
                showStartupAlert(AlertType.INFORMATION, header, text, true);
            }
        }
    }

    private final void initStage(final Stage primaryStage, final Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.setTitle(Resources.getInstance().getMsg(Resources.MsgID.APP_TITLE));

        // determine position on screen
        final Resources res = Resources.getInstance();
        if (res.isUndefined(UIConfigID.MAIN_WINDOW_XPOS) || res.isUndefined(UIConfigID.MAIN_WINDOW_YPOS)) {
            // not configured, use default - center
            primaryStage.centerOnScreen();
        } else { // configured, use user values
            primaryStage.setX(Double.parseDouble(res.getUIProp(UIConfigID.MAIN_WINDOW_XPOS)));
            primaryStage.setY(Double.parseDouble(res.getUIProp(UIConfigID.MAIN_WINDOW_YPOS)));
        }

        // determine dimensions
        double winWidth = 100.0D;
        double winHeight = 100.0D;
        if (res.isUndefined(UIConfigID.MAIN_WINDOW_WIDTH) || res.isUndefined(UIConfigID.MAIN_WINDOW_HEIGHT)) {
            // not configured, use default values
            final double scrWidth = Screen.getPrimary().getVisualBounds().getWidth();
            final double scrHeight = Screen.getPrimary().getVisualBounds().getHeight();

            final double winWidthStartFactor = Double.parseDouble(Resources.getInstance().getUIProp(
                Resources.UIConfigID.MAIN_WINDOW_START_WIDTH_FACTOR));
            final double winHeightStartFactor = Double.parseDouble(Resources.getInstance().getUIProp(
                Resources.UIConfigID.MAIN_WINDOW_START_HEIGHT_FACTOR));

            winWidth = scrWidth * winWidthStartFactor;
            winHeight = scrHeight * winHeightStartFactor;
        } else {
            // configured, use user values
            winWidth = Double.parseDouble(res.getUIProp(UIConfigID.MAIN_WINDOW_WIDTH));
            winHeight = Double.parseDouble(res.getUIProp(UIConfigID.MAIN_WINDOW_HEIGHT));
        }
        primaryStage.setWidth(winWidth);
        primaryStage.setHeight(winHeight);

        // determine maximized state
        final boolean isMaximized = Boolean.parseBoolean(res.getUIProp(UIConfigID.MAIN_WINDOW_MAXIMIZED));
        primaryStage.setMaximized(isMaximized);
    }

    private final Scene buildScene(final Parent root) {
        final Scene scene = new Scene(root);
        return scene;
    }

    private final Parent buildRootPane() throws Exception {
        // all parts of the UI are build and composed here into one result root pane
        // FIXME : temporary, for early development, later will be split into modules (separate FXML pages)
        // or maybe main.fxml will include other fxml pages of separate modules
        return FXMLLoader.load(LogmistJFXApp.class.getResource("/dburyak/logmist/ui/jfx/view/main.fxml"),
            Resources.getInstance().getMsgBundle());
        // TODO : externalize resource
    }

}
