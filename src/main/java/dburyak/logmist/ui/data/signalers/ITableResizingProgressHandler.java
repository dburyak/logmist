package dburyak.logmist.ui.data.signalers;


/**
 * Project : logmist.<br/>
 * Handler for {@link TableResizeProgressEvent} events.
 * <br/><b>Created on:</b> <i>2:50:44 AM Oct 6, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public interface ITableResizingProgressHandler {

    /**
     * Handle table resize progress event.
     * <br/><b>PRE-conditions:</b> non-null event
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:06:56 AM Oct 6, 2015</i>
     * 
     * @param event
     *            event that holds data about resize progress
     */
    public void handleResizeProgressEvent(final TableResizeProgressEvent event);

}
