package dburyak.logmist.ui.jfx.controllers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;

import dburyak.jtools.Validators;
import dburyak.logmist.ui.jfx.LogmistJFXApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;


public final class MainController {

    /**
     * Default logger for this class.
     * <br/><b>Created on:</b> <i>8:42:05 PM Dec 25, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(MainController.class);


    /**
     * Validator for "fileOpenClicks" field.
     * <br/><b>PRE-conditions:</b> non-null fileOpenClicks
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>6:33:40 PM Dec 26, 2015</i>
     * 
     * @param fileOpenClicks
     *            "fileOpenClicks" field to be validated
     * @return true if "fileOpenClicks" is valid
     * @throws IllegalArgumentException
     *             if "fileOpenClicks" is invalid
     */
    private static final boolean validateFileOpenClicks(final EventStream<ActionEvent> fileOpenClicks) {
        return Validators.nonNull(fileOpenClicks);
    }


    /**
     * Controller for "mainLogsTable.fxml". Injected by JavaFX fxml loader.
     * <br/><b>Created on:</b> <i>6:35:02 PM Dec 26, 2015</i>
     */
    @FXML
    private LogsController mainLogsTableController;

    /**
     * Controller for "mainStatusBar.fxml". Injected by JavaFX fxml loader.
     * <br/><b>Created on:</b> <i>6:36:03 PM Dec 26, 2015</i>
     */
    @FXML
    private StatusBarController mainStatusBarController;

    /**
     * Main menu "File->Open" menu item.
     * <br/><b>Created on:</b> <i>6:36:33 PM Dec 26, 2015</i>
     */
    @FXML
    private MenuItem mainMenuFileOpen;

    // event streams
    /**
     * Event stream that emits mouse clicks on the main menu "File->Open" menu item.
     * <br/><b>Created on:</b> <i>6:37:13 PM Dec 26, 2015</i>
     */
    private EventStream<ActionEvent> fileOpenClicks = null;


    @FXML
    public final void initialize() {
        mainLogsTableController.bindTo(this);
        mainStatusBarController.bindTo(this);
        LOG.debug("controllers binding done");

        initEventStreams();
        mainLogsTableController.initActions();
        LOG.debug("actions initialized");
    }

    private final void initEventStreams() {
        fileOpenClicks = EventStreams.eventsOf(mainMenuFileOpen, ActionEvent.ACTION);
    }

    public final ProgressBar getStatusProgressBar() {
        return mainStatusBarController.getProgressBar();
    }

    public final Button getStatusStopBtn() {
        return mainStatusBarController.getStopBtn();
    }

    @FXML
    public final void handleMenuFileExit(final ActionEvent event) {
        LogmistJFXApp.getInstance().exit();
    }

    /**
     * Get event stream of mouse clicks on "MainMenu->File->Open" main menu item.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>8:44:43 PM Dec 25, 2015</i>
     * 
     * @return event stream that emits mouse click events when "Open" main menu item is clicked
     */
    public final EventStream<ActionEvent> mainMenuFileOpenClicks() {
        validateFileOpenClicks(fileOpenClicks);
        return fileOpenClicks;
    }

    final LogsController getLogsController() {
        return mainLogsTableController;
    }

    final StatusBarController getStatusBarController() {
        return mainStatusBarController;
    }

    final MenuItem getMenuFileOpen() {
        return mainMenuFileOpen;
    }

}
