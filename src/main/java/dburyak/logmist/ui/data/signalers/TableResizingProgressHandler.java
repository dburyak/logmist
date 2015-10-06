package dburyak.logmist.ui.data.signalers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.logmist.ui.data.DataUpdEvent;
import dburyak.logmist.ui.data.DataUpdEventDispatcher;
import dburyak.logmist.ui.data.DataUpdEventType;
import dburyak.logmist.ui.data.ProgressData;


/**
 * Project : logmist.<br/>
 * Handler for {@link TableResizeProgressEvent} events. Updates progress data and signals events to
 * {@link DataUpdEventDispatcher} (which lead to UI updates).
 * <br/><b>Created on:</b> <i>4:24:01 AM Oct 6, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class TableResizingProgressHandler implements ITableResizingProgressHandler {

    // TODO : move common logic of this handler and ParseProgressHandler to abstract class

    /**
     * Default system logger for this class.
     * <br/><b>Created on:</b> <i>4:29:03 AM Oct 6, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(TableResizingProgressHandler.class);

    /**
     * Precision of how often should UI update events be fired.
     * <br/><b>Created on:</b> <i>4:34:37 AM Oct 6, 2015</i>
     */
    private static final double PRECISION = 0.01D; // 0.0 - 0% ; 1.0 - 100%

    /**
     * Previous progress percent. Keeping this value helps to discard outdated events.
     * <br/><b>Created on:</b> <i>4:35:15 AM Oct 6, 2015</i>
     */
    private double prevUpdPercent = -1.0D;

    /**
     * ID of the resizing process.
     * <br/><b>Created on:</b> <i>4:36:11 AM Oct 6, 2015</i>
     */
    private final int id;

    /**
     * UI updates sent counter.
     * <br/><b>Created on:</b> <i>4:36:41 AM Oct 6, 2015</i>
     */
    private int tick = 0;


    /**
     * Constructor for class : [logmist] dburyak.logmist.ui.data.signalers.TableResizingProgressHandler.<br/>
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:37:41 AM Oct 6, 2015</i>
     * 
     * @param id
     *            identifier of resizing process
     */
    public TableResizingProgressHandler(final int id) {
        this.id = id;
    }


    /**
     * Update {@link ProgressData}. Then signals {@link DataUpdEventType#TABLE_RESIZE_PROGRESS_UPDATE} or
     * {@link DataUpdEventType#TABLE_RESIZE_FINISHED} events to {@link DataUpdEventDispatcher}.
     * <br/><b>PRE-conditions:</b> non-null event
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:24:02 AM Oct 6, 2015</i>
     * 
     * @see dburyak.logmist.ui.data.signalers.ITableResizingProgressHandler#handleResizeProgressEvent(dburyak.logmist.ui.data.signalers.TableResizeProgressEvent)
     * @param event
     *            resize progress event to be processed
     */
    @Override
    public final void handleResizeProgressEvent(final TableResizeProgressEvent event) {
        final double percent = event.getItemsProcessed() / (double) event.getItemsTotal();
        final double diff = percent - prevUpdPercent;

        if (event.getItemsProcessed() >= event.getItemsTotal()) {
            updateProgress(DataUpdEventType.TABLE_RESIZE_FINISHED, percent);
        } else if (diff > PRECISION) {
            updateProgress(DataUpdEventType.TABLE_RESIZE_PROGRESS_UPDATE, percent);
        }
    }

    /**
     * Update {@link ProgressData} and send table resizing update event to event dispatcher.
     * <br/><b>PRE-conditions:</b> non-null eventType, non-negative percent
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> progress data updated and UI update event fired (leads to update of progress bar state)
     * <br/><b>Created on:</b> <i>4:44:54 AM Oct 6, 2015</i>
     * 
     * @param eventType
     *            either {@link DataUpdEventType#TABLE_RESIZE_PROGRESS_UPDATE} or
     *            {@link DataUpdEventType#TABLE_RESIZE_FINISHED}
     * @param percent
     *            progress between 0.0D to 1.0D
     */
    @SuppressWarnings({ "boxing", "nls" })
    private final void updateProgress(final DataUpdEventType eventType, final double percent) {
        // 1. update progress model
        prevUpdPercent = percent;
        ProgressData.getInstance().updateData(percent);

        // 2. send PROGRESS UPDATE notification
        DataUpdEventDispatcher.getInstance().signal(new DataUpdEvent(eventType, id, tick));
        LOG.debug("table resize progress data update : type = [%s] ; percent = [%f] ; eventID = [%d] ; tick = [%d]",
            eventType, percent, id, tick);
        tick++;
    }

}
