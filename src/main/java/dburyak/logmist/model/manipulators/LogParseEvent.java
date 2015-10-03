package dburyak.logmist.model.manipulators;


import dburyak.jtools.Validators;


/**
 * Project : logmist.<br/>
 * Event that indicates some parsing activity change.
 * <br/><b>Created on:</b> <i>2:58:25 PM Oct 3, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class LogParseEvent {

    /**
     * Total lines in log file.
     * <br/><b>Created on:</b> <i>2:59:11 PM Oct 3, 2015</i>
     */
    private final long linesTotal;

    /**
     * Lines already parsed.
     * <br/><b>Created on:</b> <i>2:59:27 PM Oct 3, 2015</i>
     */
    private final long linesParsed;


    /**
     * Validator for "linesTotal" parameter. Non-negative is expected.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:13:43 PM Oct 3, 2015</i>
     * 
     * @param linesTotal
     *            parameter to be validated
     * @return true if parameter is valid
     * @throws IllegalArgumentException
     *             if parameter is invalid
     */
    private static final boolean validateLinesTotal(final long linesTotal) {
        return Validators.nonNegative(linesTotal);
    }

    /**
     * Validator for "linesParsed" parameter. Non-negative is expected.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:15:27 PM Oct 3, 2015</i>
     * 
     * @param linesParsed
     *            parameter to be validated
     * @return true if parameter is valid
     * @throws IllegalArgumentException
     *             if parameter is invalid
     */
    private static final boolean validateLinesParsed(final long linesParsed) {
        return Validators.nonNegative(linesParsed);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.manipulators.LogParseEvent.<br/>
     * <br/><b>PRE-conditions:</b> non-negative parameters
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:59:41 PM Oct 3, 2015</i>
     * 
     * @param linesTotal
     *            total lines in log file that is parsed
     * @param linesParsed
     *            number of already parsed lines
     */
    public LogParseEvent(final long linesTotal, final long linesParsed) {
        validateLinesTotal(linesTotal);
        validateLinesParsed(linesParsed);
        this.linesTotal = linesTotal;
        this.linesParsed = linesParsed;
    }


    /**
     * Getter method.<br/>
     * Get total lines in log file that is parsed.
     * <br/><b>POST-conditions:</b> non-negative result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:18:30 PM Oct 3, 2015</i>
     * 
     * @return total lines in log file that is parsed
     */
    public final long getLinesTotal() {
        return linesTotal;
    }


    /**
     * Getter method.<br/>
     * Get number of already parsed lines.
     * <br/><b>POST-conditions:</b> non-negative result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:18:30 PM Oct 3, 2015</i>
     * 
     * @return number of already parsed lines
     */
    public final long getLinesParsed() {
        return linesParsed;
    }

    /**
     * Get string representation of this event.<br/>
     * Example :
     * 
     * <pre>
     * {linesTotal=[100],linesParsed=[20]}
     * </pre>
     * 
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-empty result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:20:07 PM Oct 3, 2015</i>
     * 
     * @see java.lang.Object#toString()
     * @return string representation of this event
     */
    @SuppressWarnings("nls")
    @Override
    public final String toString() {
        final StringBuilder sb = (new StringBuilder("{linesTotal=[")).append(linesTotal);
        sb.append("],linesParsed=[").append(linesParsed).append("]}");
        return sb.toString();
    }

}
