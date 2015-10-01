package dburyak.logmist.ui.jfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

public final class MainController {
    
    @FXML private LogsController mainLogsTableController;
    @FXML private StatusBarController mainStatusBarController;

    @FXML public void initialize() {
        mainLogsTableController.init(this);
        mainStatusBarController.init(this);
    }
    
    public final ProgressBar getStatusProgressBar() {
        return mainStatusBarController.getProgressBar();
    }
    
}
