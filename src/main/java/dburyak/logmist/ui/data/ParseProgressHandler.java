package dburyak.logmist.ui.data;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.logmist.model.manipulators.ILogParseEventHandler;
import dburyak.logmist.model.manipulators.LogParseEvent;
import javafx.beans.property.DoubleProperty;


public final class ParseProgressHandler implements ILogParseEventHandler {

    private static final Logger LOG = LogManager.getFormatterLogger(ParseProgressHandler.class);
    private static final double PRECISION = 0.01D; // 0.0 - 0% ; 1.0 - 100%
    private double prevUpdPercent = -1.0D;
    private final int id;
    private int tick = 0;


    public ParseProgressHandler(final int id) {
        this.id = id;
    }

    @Override
    public final void handleLogParseEvent(final LogParseEvent event) {
        final double percent = event.getLinesParsed() / (double) event.getLinesTotal();
        final double diff = percent - prevUpdPercent;

        if (event.getLinesParsed() >= event.getLinesTotal()) { // all lines read, send FINISH update
            updateProgress(DataUpdEventType.PARSE_FINISHED, percent);
        } else if (diff > PRECISION) { // we need to send progress update
            updateProgress(DataUpdEventType.PARSE_PROGRESS_UPDATE, percent);
        }
    }

    private final void updateProgress(final DataUpdEventType eventType, final double percent) {
        // 1. update progress model
        prevUpdPercent = percent;
        ProgressData.getInstance().updateData(percent);

        // 2. send PROGRESS UPDATE notification
        DataUpdEventDispatcher.getInstance().signal(
            new DataUpdEvent(eventType, id, tick));
        LOG.debug("parse data update : type = [%s] ; percent = [%f] ; eventID = [%d] ; tick = [%d]",
            eventType, percent, id, tick);
        tick++;
    }

}
