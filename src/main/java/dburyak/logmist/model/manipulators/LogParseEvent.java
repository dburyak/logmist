package dburyak.logmist.model.manipulators;

// TODO : code style
public final class LogParseEvent {
    
    private final long linesTotal;
    private final long linesParsed;
    
    public LogParseEvent(final long linesTotal, final long linesParsed) {
        this.linesTotal = linesTotal;
        this.linesParsed = linesParsed;
    }
    
    public long getLinesTotal() {
        return linesTotal;
    }
    
    public long getLinesParsed() {
        return linesParsed;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = (new StringBuilder("{linesTotal=[")).append(linesTotal);
        sb.append("],linesParsed=[").append(linesParsed).append("]}");
        return sb.toString();
    }

}
