package dburyak.logmist.ui.data.signalers;


import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.logmist.model.LogEntry;
import dburyak.logmist.ui.data.DataUpdEvent;
import dburyak.logmist.ui.data.DataUpdEventDispatcher;
import dburyak.logmist.ui.data.DataUpdEventType;
import dburyak.logmist.ui.data.LogTableData;


public final class LogTableDataUpdHandler {

    private static final Logger LOG = LogManager.getFormatterLogger(LogTableDataUpdHandler.class);

    private final LogTableData tableData;
    private final int id;
    private final String name;


    public LogTableDataUpdHandler(final LogTableData tableData, final int id, final String name) {
        this.tableData = tableData;
        this.id = id;
        this.name = name;
    }

    public final void updateLogTableData(final Collection<LogEntry> logs) {
        tableData.updateData(logs);
        final DataUpdEvent event = new DataUpdEvent(DataUpdEventType.LOG_TABLE_DATA_UPDATE, id, 0); // 0 - no sequence
        LOG.debug("log table updated, signaling event : tableName = [%s] ; id = [%s] ; event = [%s]",
            name, id, event);
        DataUpdEventDispatcher.getInstance().signal(event);
    }

}
