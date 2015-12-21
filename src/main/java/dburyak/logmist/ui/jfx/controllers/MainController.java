package dburyak.logmist.ui.jfx.controllers;


import dburyak.logmist.ui.jfx.LogmistJFXApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;


public final class MainController {

    @FXML
    private LogsController mainLogsTableController;
    @FXML
    private StatusBarController mainStatusBarController;


    @FXML
    public final void initialize() {
        mainLogsTableController.init(this);
        mainStatusBarController.bindTo(this);
    }

    public final ProgressBar getStatusProgressBar() {
        return mainStatusBarController.getProgressBar();
    }

    @FXML
    public final void handleMenuFileOpen(final ActionEvent event) {
        mainLogsTableController.handleOpenBtn(event);
    }

    @FXML
    public final void handleMenuFileExit(final ActionEvent event) {
        LogmistJFXApp.getInstance().exit();
    }

}
