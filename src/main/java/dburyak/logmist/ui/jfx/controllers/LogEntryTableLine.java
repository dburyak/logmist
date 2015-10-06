package dburyak.logmist.ui.jfx.controllers;


import dburyak.logmist.model.LogEntry;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TableView;


/**
 * Project : logmist.<br/>
 * Adapter class, wraps {@link LogEntry} and provides its content in format needed for {@link TableView}.
 * <br/><b>Created on:</b> <i>3:43:44 AM Oct 5, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class LogEntryTableLine {

    /**
     * Log entry this adapter wraps.
     * <br/><b>Created on:</b> <i>3:48:59 AM Oct 5, 2015</i>
     */
    @SuppressWarnings("unused")
    private final LogEntry logEntry;

    /**
     * Line number property. Line number of log entry in log file.
     * <br/><b>Created on:</b> <i>3:49:10 AM Oct 5, 2015</i>
     */
    private final LongProperty lineNum;

    /**
     * Log property. Original log message in log file.
     * <br/><b>Created on:</b> <i>3:49:32 AM Oct 5, 2015</i>
     */
    private final StringProperty log;


    /**
     * Constructor for class : [logmist] dburyak.logmist.ui.jfx.controllers.LogEntryTableLine.<br/>
     * <br/><b>PRE-conditions:</b> non-null log
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:48:12 AM Oct 5, 2015</i>
     * 
     * @param logEntry
     *            log entry to be wrapped by this adapter
     */
    public LogEntryTableLine(final LogEntry logEntry) {
        this.logEntry = logEntry;
        lineNum = new SimpleLongProperty(logEntry.getLineNum());
        log = new SimpleStringProperty(logEntry.getMsgFull());
    }

    /**
     * Adapted getter.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:56:22 AM Oct 5, 2015</i>
     * 
     * @return line number in log file of this log
     */
    public final Long getLineNum() {
        return lineNum.getValue();
    }

    /**
     * Adapted setter.
     * <br/><b>PRE-conditions:</b> non-null value
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:56:57 AM Oct 5, 2015</i>
     * 
     * @param newValue
     *            new line number in log file for this log
     */
    public final void setLineNum(final Long newValue) {
        lineNum.setValue(newValue);
    }

    /**
     * Adapted getter.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:57:45 AM Oct 5, 2015</i>
     * 
     * @return log message of this log
     */
    public final String getLog() {
        return log.getValue();
    }

    /**
     * Adapted setter.
     * <br/><b>PRE-conditions:</b> non-null newValue
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:58:12 AM Oct 5, 2015</i>
     * 
     * @param newValue
     *            new log message for this log
     */
    public final void setLog(final String newValue) {
        log.setValue(newValue);
    }

}
