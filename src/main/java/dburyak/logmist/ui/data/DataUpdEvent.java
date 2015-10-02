package dburyak.logmist.ui.data;


import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;


// TODO : code style
@ThreadSafe
@Immutable
@javax.annotation.concurrent.ThreadSafe
@javax.annotation.concurrent.Immutable
public class DataUpdEvent {

    private final DataUpdEventType type;
    private final int dataID;
    private final int eventID;


    public DataUpdEvent(final DataUpdEventType type, final int eventID, final int dataID) {
        this.type = type;
        this.eventID = eventID;
        this.dataID = dataID;
    }

    public final DataUpdEventType getType() {
        return type;
    }

    public final int getDataID() {
        return dataID;
    }

    public final int getEventID() {
        return eventID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = (new StringBuilder("{type=[")).append(getType());
        sb.append("],eventID=[").append(getEventID());
        sb.append("],dataID=[").append(getDataID()).append("]}");
        return sb.toString();
    }
}
