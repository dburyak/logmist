package dburyak.logmist.ui.jfx.controllers;


import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.jtools.Validators;
import dburyak.logmist.Resources;
import dburyak.logmist.Resources.UIConfigID;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;


/**
 * Project : logmist.<br/>
 * JavaFX controller class for main status bar (at the bottom of the main window).
 * <br/><b>Created on:</b> <i>5:18:46 AM Dec 21, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class StatusBarController {

    /**
     * Default system logger for this controller.
     * <br/><b>Created on:</b> <i>2:19:38 AM Dec 21, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(StatusBarController.class);


    /**
     * Project : logmist.<br/>
     * Memory usage statistics.
     * <br/><b>Created on:</b> <i>2:39:07 AM Dec 21, 2015</i>
     * 
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     */
    private static final class MemStats {

        /**
         * Default system logger for this class.
         * <br/><b>Created on:</b> <i>2:57:56 AM Dec 21, 2015</i>
         */
        @SuppressWarnings("hiding")
        private static final Logger LOG = LogManager.getFormatterLogger(MemStats.class);


        /**
         * Factory method that produces new stats from current VM memory usage.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> {@link Runtime} is accessed
         * <br/><b>Created on:</b> <i>2:39:27 AM Dec 21, 2015</i>
         * 
         * @return memory usage stats that represents current VM memory usage
         */
        static final MemStats newMemStats() {
            final Runtime rt = Runtime.getRuntime();
            final MemStats newStats = new MemStats(rt.totalMemory(), rt.freeMemory());
            LOG.debug("heap status requested : newStats = [%s]", newStats); //$NON-NLS-1$
            return newStats;
        }

        /**
         * Validator for any memory value.
         * <br/><b>PRE-conditions:</b> non-negative memory
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>2:45:59 AM Dec 21, 2015</i>
         * 
         * @param memory
         *            memory parameter to be validated
         * @return true if "memory" is valid
         * @throws IllegalArgumentException
         *             if "memory" is invalid
         */
        private static final boolean validateMem(final long memory) {
            return Validators.nonNegative(memory);
        }

        /**
         * Validator for "percent" value.
         * <br/><b>PRE-conditions:</b> (0.0D <= percent && percent <= 1.0D)
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>2:55:02 AM Dec 21, 2015</i>
         * 
         * @param percent
         *            percent to be validated
         * @return true if "percent" is valid
         * @throws IllegalArgumentException
         *             if "percent" is invalid
         */
        private static final boolean validatePercent(final double percent) {
            if (!((0.0D < percent) && (percent <= 1.0D))) {
                throw LOG.throwing(Level.TRACE, new IllegalArgumentException());
            }
            return true;
        }

        /**
         * Convert bytes to megabytes.
         * <br/><b>PRE-conditions:</b> non-negative bytes
         * <br/><b>POST-conditions:</b> non-negative result
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>2:41:21 AM Dec 21, 2015</i>
         * 
         * @param bytes
         *            bytes to be converted to megabytes
         * @return megabytes
         */
        private static final long toMB(final long bytes) {
            assert (validateMem(bytes));
            return bytes / (1024 * 1024);
        }


        /**
         * All available memory in bytes.
         * <br/><b>Created on:</b> <i>2:43:36 AM Dec 21, 2015</i>
         */
        private final long allMemBytes;

        /**
         * Free memory in bytes.
         * <br/><b>Created on:</b> <i>2:44:41 AM Dec 21, 2015</i>
         */
        private final long freeMemBytes;


        /**
         * Constructor for class : [logmist] dburyak.logmist.ui.jfx.controllers.MemStats.<br/>
         * <br/><b>PRE-conditions:</b> non-negative allMemBytes, non-negative freeMemBytes
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>2:45:01 AM Dec 21, 2015</i>
         * 
         * @param allMemBytes
         *            all available memory in bytes
         * @param freeMemBytes
         *            free memory in bytes
         */
        private MemStats(final long allMemBytes, final long freeMemBytes) {
            validateMem(allMemBytes);
            validateMem(freeMemBytes);

            this.allMemBytes = allMemBytes;
            this.freeMemBytes = freeMemBytes;
        }

        /**
         * Get memory usage percent (0.0D = 0% and 1.0D = 100%).
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> 0.0D <= result <= 1.0D
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>2:47:52 AM Dec 21, 2015</i>
         * 
         * @return memory usage percent
         */
        final double getUsagePercent() {
            final long usedMemBytes = allMemBytes - freeMemBytes;
            final double usagePercent = usedMemBytes / (double) allMemBytes;
            validatePercent(usagePercent);
            return usagePercent;
        }

        /**
         * Get all available memory in megabytes.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> non-negative result
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>2:49:57 AM Dec 21, 2015</i>
         * 
         * @return all available VM memory in megabytes
         */
        final long getAllMemMB() {
            final long allMemMB = toMB(allMemBytes);
            return allMemMB;
        }

        /**
         * Get used VM memory in megabytes.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> non-negative result
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>2:59:48 AM Dec 21, 2015</i>
         * 
         * @return used VM memory in megabytes
         */
        final long getUsedMemMB() {
            final long usedMemMB = toMB(allMemBytes - freeMemBytes);
            assert ((0L < usedMemMB) && (usedMemMB <= toMB(allMemBytes)));
            return usedMemMB;
        }

        /**
         * Get string representation of mem stats.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> non-empty result
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>5:09:47 AM Dec 21, 2015</i>
         * 
         * @return string representation of this mem stats
         */
        @SuppressWarnings("nls")
        @Override
        public String toString() {
            final StringBuilder sb = (new StringBuilder("{allMemMB=[")).append(getAllMemMB())
                .append("],usedMemMB=[").append(getUsedMemMB())
                .append("],percent=[").append(getUsagePercent()).append("]}");
            return sb.toString();
        }
    }


    /**
     * Starts job that updates heap status label in menu bar on each tick (set in configuration).
     * <br/><b>PRE-conditions:</b> non-null lblUsed, non-null lblAll, non-null heapBar
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> jobs are scheduled to Schedulers.io()
     * <br/><b>Created on:</b> <i>4:59:19 AM Dec 21, 2015</i>
     * 
     * @param lblUsed
     *            ui label that shows used memory
     * @param lblAll
     *            ui label that shows all memory
     * @param heapBar
     *            progress bar that reflects heap usage
     */
    private static final void startHeapStatusService(
        final Label lblUsed,
        final Label lblAll,
        final ProgressBar heapBar) {

        // init heap status properties
        final Resources res = Resources.getInstance();
        final long startDelayMS = Long.parseLong(res.getUIProp(
            UIConfigID.MAIN_STATUS_BAR_HEAP_BAR_START_DELAY_MS));
        final long periodMS = Long.parseLong(res.getUIProp(UIConfigID.MAIN_STATUS_BAR_HEAP_BAR_UPD_PERIOD_MS));

        final Observable<MemStats> memStatsSource = Observable
            .interval(startDelayMS, periodMS, TimeUnit.MILLISECONDS, Schedulers.computation())
            .map(tick -> MemStats.newMemStats())
            .subscribeOn(Schedulers.computation());

        memStatsSource.subscribe(new Subscriber<MemStats>() {

            @SuppressWarnings({ "nls", "synthetic-access" })
            @Override
            public void onNext(final MemStats memStats) {
                Platform.runLater(() -> { // update ui labels (on UI thread)
                    lblUsed.setText(Long.toString(memStats.getUsedMemMB()));
                    lblAll.setText(Long.toString(memStats.getAllMemMB()));
                    heapBar.setProgress(memStats.getUsagePercent());
                    LOG.debug("heap status UI update");
                });
            }

            @SuppressWarnings({ "nls", "synthetic-access" })
            @Override
            public void onCompleted() {
                LOG.error("heap status ticks are not expected to finish");
                throw LOG.throwing(Level.TRACE, new UnsupportedOperationException());
            }

            @SuppressWarnings({ "synthetic-access", "nls" })
            @Override
            public void onError(final Throwable error) {
                LOG.error("unexpected error from heap status ticker received : error = [%s]",
                    error.getClass().getSimpleName());
            }
        });
    }


    private MainController mainCtrl;
    @FXML
    private ProgressBar mainStatusBarProgressBar;
    @FXML
    private ProgressBar mainStatusBarHeapBar;
    @FXML
    private Label mainStatusBarHeapUsed;
    @FXML
    private Label mainStatusBarHeapAll;
    @FXML
    private Button mainStatusBarStopBtn;


    /**
     * Provides a way to get access to other controllers of the JavaFX application through interface of main controller.
     * <br/><b>PRE-conditions:</b> non-null mainCtrl
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> this state is changed
     * <br/><b>Created on:</b> <i>5:20:37 AM Dec 21, 2015</i>
     * 
     * @param mainCtrl
     *            main controller (Mediator for controllers)
     */
    public final void bindTo(@SuppressWarnings("hiding") final MainController mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Callback is called on FXML parsing to bind declared handlers in fxml document to real java code methods.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> state is changed, heap bar updater is launched
     * <br/><b>Created on:</b> <i>5:23:03 AM Dec 21, 2015</i>
     */
    @FXML
    public final void initialize() {
        startHeapStatusService(mainStatusBarHeapUsed, mainStatusBarHeapAll, mainStatusBarHeapBar);
    }

    public final ProgressBar getProgressBar() {
        return mainStatusBarProgressBar;
    }

    public final Button getStopBtn() {
        return mainStatusBarStopBtn;
    }

}
