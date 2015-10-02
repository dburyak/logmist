package dburyak.logmist.ui.jfx.controllers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.logmist.ui.Resources;
import dburyak.logmist.ui.Resources.ConfigID;
import dburyak.logmist.ui.Resources.UIConfigID;
import dburyak.logmist.ui.jfx.LogmistJFXApp;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;
import javafx.scene.control.Label;


public final class StatusBarController {

    private final void startHeapStatusService(final Label lblUsed, final Label lblAll, final ProgressBar heapBar) {
        // init heap status properties
        final Resources res = Resources.getInstance();
        final double startDelayMS = Double.parseDouble(res.getUIProp(
            UIConfigID.MAIN_STATUS_BAR_HEAP_BAR_START_DELAY_MS));
        final double periodMS = Double.parseDouble(res.getUIProp(UIConfigID.MAIN_STATUS_BAR_HEAP_BAR_UPD_PERIOD_MS));

        heapStatSrv = new HeapStatusService(lblUsed, lblAll, heapBar);
        heapStatSrv.setDelay(Duration.millis(startDelayMS)); // initial hold on time
        heapStatSrv.setPeriod(Duration.millis(periodMS)); // repeat period
        heapStatSrv.setExecutor(LogmistJFXApp.getInstance().getThreadPool());
        Platform.runLater(() -> heapStatSrv.start()); // start the heap update service
        LogmistJFXApp.getInstance().registerService(heapStatSrv);
    }


    private MainController mainCtrl;
    private HeapStatusService heapStatSrv;
    @FXML
    private ProgressBar mainStatusBarProgressBar;
    @FXML
    private ProgressBar mainStatusBarHeapBar;
    @FXML
    private Label mainStatusBarHeapUsed;
    @FXML
    private Label mainStatusBarHeapAll;


    public final void init(final MainController mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public final void initialize() {
        startHeapStatusService(mainStatusBarHeapUsed, mainStatusBarHeapAll, mainStatusBarHeapBar);
    }

    public final ProgressBar getProgressBar() {
        return mainStatusBarProgressBar;
    }

}
