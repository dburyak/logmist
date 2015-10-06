package dburyak.logmist.ui.jfx;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;


public final class HeapStatusService extends ScheduledService<Void> {

    private static final Logger LOG = LogManager.getFormatterLogger(HeapStatusService.class);

    private final Label lblUsed;
    private final Label lblAll;
    private final ProgressBar heapBar;


    public HeapStatusService(final Label lblUsed, final Label lblAll, final ProgressBar heapBar) {
        super();
        this.lblUsed = lblUsed;
        this.lblAll = lblAll;
        this.heapBar = heapBar;
    }

    @Override
    protected final Task<Void> createTask() {
        return new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                updateProgress(0L, 1L);
                final Runtime rt = Runtime.getRuntime();
                final long allMem = rt.totalMemory();
                final long usedMem = allMem - rt.freeMemory();
                final double usagePercent = usedMem / (double) allMem;
                final long allMemMB = allMem / (1024 * 1024);
                final long usedMemMB = usedMem / (1024 * 1024);
                Platform.runLater(() -> { // update ui labels (on UI thread)
                    lblUsed.setText(Long.toString(usedMemMB));
                    lblAll.setText(Long.toString(allMemMB));
                    heapBar.setProgress(usagePercent);
                    LOG.debug("heap status UI update tick");
                });
                updateProgress(1L, 1L);
                return null;
            }
        };
    }

    @Override
    protected final void cancelled() {
        LOG.debug("heap status service cancelled");
        super.cancelled();
    }
}
