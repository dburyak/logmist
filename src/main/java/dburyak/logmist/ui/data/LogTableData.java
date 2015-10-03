package dburyak.logmist.ui.data;


import java.util.Collection;

import dburyak.logmist.model.LogEntry;


/**
 * Project : logmist.<br/>
 * <br/><b>Created on:</b> <i>1:20:42 AM Oct 4, 2015</i>
 * Log table data. Contains collection of logs to be displayed on log table.
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class LogTableData extends UIData<Collection<LogEntry>> {

    /**
     * <br/><b>PRE-conditions:</b>
     * <br/><b>POST-conditions:</b>
     * <br/><b>Side-effects:</b>
     * <br/><b>Created on:</b> <i>1:20:43 AM Oct 4, 2015</i>
     * 
     * @see dburyak.logmist.ui.data.UIData#doUpdateData(java.lang.Object)
     * @param newValue
     */
    @Override
    protected final void doUpdateData(final Collection<LogEntry> logs) {
    }

    /**
     * <br/><b>PRE-conditions:</b>
     * <br/><b>POST-conditions:</b>
     * <br/><b>Side-effects:</b>
     * <br/><b>Created on:</b> <i>1:20:43 AM Oct 4, 2015</i>
     * 
     * @see dburyak.logmist.ui.data.UIData#doGetData()
     * @return
     */
    @Override
    protected final Collection<LogEntry> doGetData() {
        return null;
    }

}
