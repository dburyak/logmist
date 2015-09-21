package dburyak.logmist.ui.jfx;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.logmist.exceptions.UnableToLaunchException;
import dburyak.logmist.ui.jfx.Resources.MsgID;
import javafx.application.Application;
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

    @Override
    public void start(final Stage primaryStage) throws UnableToLaunchException {
        try {
            boolean hasDefaultConfig = checkConfig();
            if (!hasDefaultConfig) {
                return; //application exit
            }
            
            boolean hasResources = checkResources();
            if (!hasResources) {
                return; // application exit
            }
            
            checkHasUserConfig();
            checkCanReadUserConfig();
            checkCanSaveUserConfig();
            
            final Parent root = buildRootPane();
            final Scene scene = buildScene(root);
            configStage(primaryStage, scene);
            primaryStage.show();
        } catch (final Exception e) {
            LOG.catching(Level.DEBUG, e);
            throw LOG.throwing(Level.DEBUG, new UnableToLaunchException(getClass().getSimpleName(), e));
        }
    }
    
    private final void showStartupAlert(final AlertType type, final String title, final String text, final boolean blocking) {
        final Alert dialog = new Alert(type);
        dialog.setTitle(title);
        dialog.setContentText(text);
        if (blocking) {
            dialog.showAndWait();
        } else {
            dialog.show();
        }
    }
    
    private final boolean checkConfig() {
        try {
            @SuppressWarnings("unused")
            final Config config = Config.getInstance();
            return true;
        } catch (final AssertionError | RuntimeException e) {
            // resources cannot be initialized, thus use hard-code (actually, locale is stored in config file)
            final String title = "Config Error";
            final String text = "Default config file not found. This happened due to incorrectly built application. "
                    + "Application cannot be started. Please, contact developers for more information.\n"
                    + "https://github.com/dburyak/logmist\n"
                    + "dmytro.buryak@gmail.com";
            showStartupAlert(AlertType.ERROR, title, text, true);
            return false;
        }
    }
    
    private final boolean checkResources() {
        try {
            @SuppressWarnings("unused")
            final Resources res = Resources.getInstance();
            return true;
        } catch (final AssertionError | RuntimeException e) {
            final String title = "Resources Error";
            final String text = "Important resource files not found. This happened due to incorrectly built "
                    + "application. Application cannot be started. Please, contact developers for more information.\n"
                    + "https://github.com/dburyak/logmist\n"
                    + "dmytro.buryak@gmail.com";
            showStartupAlert(AlertType.ERROR, title, text, true);
            return false;
        }
    }
    
    private final void checkHasUserConfig() {
        if (!Config.getInstance().hasUserConfig()) {
            final Resources res = Resources.getInstance();
            final String title = res.getMsg(MsgID.APP_RESOURCES_NO_USER_CONFIG_TITLE);
            final String text = res.getMsg(MsgID.APP_RESOURCES_NO_USER_CONFIG_TEXT);
            showStartupAlert(AlertType.INFORMATION, title, text, true);
        }
    }
    
    private void checkCanReadUserConfig() {
        if (!Config.getInstance().canReadUserConfig()) {
            final Resources res = Resources.getInstance();
            final String title = res.getMsg(MsgID.APP_RESOURCES_NOT_READABLE_USER_CONFIG_TITLE);
            final String text = res.getMsg(MsgID.APP_RESOURCES_NOT_READABLE_USER_CONFIG_TEXT);
            showStartupAlert(AlertType.WARNING, title, text, true);
        }
    }
    
    private void checkCanSaveUserConfig() {
        if (!Config.getInstance().canSaveUserConfig()) {
            final Resources res = Resources.getInstance();
            final String title = res.getMsg(MsgID.APP_RESOURCES_NOT_WRITEABLE_USER_CONFIG_TITLE);
            final String text = res.getMsg(MsgID.APP_RESOURCES_NOT_WRITEABLE_USER_CONFIG_TEXT);
            showStartupAlert(AlertType.WARNING, title, text, true);
        }
    }

    private final void configStage(final Stage primaryStage, final Scene scene) {
        primaryStage.setScene(scene);
        primaryStage.setTitle(Resources.getInstance().getMsg(Resources.MsgID.APP_TITLE));
        primaryStage.centerOnScreen();
    }

    private final Scene buildScene(final Parent root) {
        final double scrWidth = Screen.getPrimary().getVisualBounds().getWidth();
        final double scrHeight = Screen.getPrimary().getVisualBounds().getHeight();
        
        final double winWidthStartFactor = Double.parseDouble(Resources.getInstance().getUIConst(
                Resources.UIConstID.MAIN_WINDOW_START_WIDTH_FACTOR));
        final double winHeightStartFactor = Double.parseDouble(Resources.getInstance().getUIConst(
                Resources.UIConstID.MAIN_WINDOW_START_HEIGHT_FACTOR));
        
        final double winWidth = scrWidth * winWidthStartFactor;
        final double winHeight = scrHeight * winHeightStartFactor;
        final Scene scene = new Scene(root, winWidth, winHeight);
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