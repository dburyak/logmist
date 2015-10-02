package dburyak.logmist.ui.jfx;


import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.logmist.exceptions.UnableToLaunchException;
import dburyak.logmist.ui.Resources;
import dburyak.logmist.ui.Resources.ConfigID;
import dburyak.logmist.ui.Resources.MsgID;
import dburyak.logmist.ui.Resources.UIConfigID;
import dburyak.logmist.ui.Resources.UserFileStatus;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.ScheduledService;
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
    private final HashSet<ScheduledService<?>> services = new HashSet<>();
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

    public final boolean registerService(final ScheduledService<?> service) {
        return services.add(service);
    }

    public final boolean unregisterService(final ScheduledService<?> service) {
        return services.remove(service);
    }

    private final void initThreadPool() {
        final int numProc = Runtime.getRuntime().availableProcessors();
        final int numThreadsAdd = Integer.parseInt(Resources.getInstance().getConfigProp(
            ConfigID.CORE_THREAD_POOL_SIZE_ADD));
        threadPool = Executors.newFixedThreadPool(numProc + numThreadsAdd);
        LOG.debug("thread pool inited : numThreads = [%d]", numProc + numThreadsAdd);
    }

    @Override
    public void start(final Stage primaryStage) throws UnableToLaunchException {
        INSTANCE = this;
        stage = primaryStage;
        try {
            boolean hasResources = checkResources();
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

    private final int stopServices() {
        services.stream().forEach(srv -> {
            if (srv.cancel()) {
                LOG.debug("service cancelled successfully : srv = [%s]", srv);
            } else {
                LOG.warn("service cancel FAILED : srv = [%s]", srv);
            }
        });
        return services.size();
    }

    private final void initLogmistJFXApp(final LogmistJFXApp app) {
        app.getStage().setOnCloseRequest(event -> {
            final int numServices = stopServices();
            LOG.debug("running services cancelled : numServices = [%d]", numServices);
            threadPool.shutdown();
            LOG.debug("waiting for running thread pool to be stopped ...");
            try {
                threadPool.awaitTermination(5, TimeUnit.SECONDS);
                LOG.debug("all threads in pool stopped");
            } catch (final Exception e) {
                LOG.catching(Level.TRACE, e);
                LOG.error("error while waiting application thread pool termination", e);
            }

            try { // persist user config
                final Resources res = Resources.getInstance();
                final Stage stage = app.getStage();

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
        });

        app.getStage().maximizedProperty().addListener(
            (final ObservableValue<? extends Boolean> ov, final Boolean oldValue, final Boolean newValue) -> {
                if (oldValue == false && newValue == true) { // normal -> maximized
                    final Resources res = Resources.getInstance();
                    final double width = app.getStage().getWidth();
                    final double height = app.getStage().getHeight();
                    res.setUIProp(UIConfigID.MAIN_WINDOW_WIDTH, Double.toString(width));
                    res.setUIProp(UIConfigID.MAIN_WINDOW_HEIGHT, Double.toString(height));
                }
            });
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
        boolean isMaximized = Boolean.parseBoolean(res.getUIProp(UIConfigID.MAIN_WINDOW_MAXIMIZED));
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
