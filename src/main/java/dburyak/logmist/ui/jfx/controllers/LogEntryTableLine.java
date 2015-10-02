package dburyak.logmist.ui.jfx.controllers;


import dburyak.logmist.model.LogEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public final class LogEntryTableLine {

    private StringProperty logFull;


    public LogEntryTableLine(final LogEntry log) {
        this.logFull = new SimpleStringProperty(log.getMsgFull());
    }

    public final String getLogFull() {
        return logFull.get();
    }

    public final void setLogFull(final String newValue) {
        logFull.set(newValue);
    }

    // public final StringProperty logFullProperty() {
    // if (logFull == null) {
    // logFull = new SimpleStringProperty(this, "log", "");
    // }
    // return logFull;
    // }
}
