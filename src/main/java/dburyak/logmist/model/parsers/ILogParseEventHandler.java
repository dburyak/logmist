package dburyak.logmist.model.parsers;


/**
 * Project : logmist.<br/>
 * Event handler for {@link LogParseEvent} events.
 * <br/><b>Created on:</b> <i>4:21:31 AM Oct 3, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public interface ILogParseEventHandler {

    /**
     * Handle log parse event.
     * <br/><b>PRE-conditions:</b> non-null event
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>4:21:51 AM Oct 3, 2015</i>
     * 
     * @param event
     *            log parse event to be handled
     */
    public void handleLogParseEvent(final LogParseEvent event);

}
