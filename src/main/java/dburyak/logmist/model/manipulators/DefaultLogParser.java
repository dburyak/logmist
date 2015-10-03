package dburyak.logmist.model.manipulators;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.jtools.Validators;
import dburyak.logmist.exceptions.ParseException;
import dburyak.logmist.model.LogEntry;
import net.jcip.annotations.NotThreadSafe;


/**
 * Project : logmist.<br/>
 * Default implementation of {@link ILogFileParser}, can parse any log format, but is not time aware (doesn't recognize
 * time stamps). This is fallback implementation, meaning that if none of registered parsers cannot recognize log
 * format, then {@link DefaultLogParser} is used.
 * <br/><b>Created on:</b> <i>4:26:11 AM Aug 20, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@NotThreadSafe
@javax.annotation.concurrent.NotThreadSafe
public final class DefaultLogParser extends LogFileParserBase {

    /**
     * Default system logger for this class.
     * <br/><b>Created on:</b> <i>12:21:28 AM Aug 21, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(DefaultLogParser.class);


    /**
     * Validator for "line" parameter. Non-empty is expected.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:55:19 PM Oct 3, 2015</i>
     * 
     * @param line
     *            line parameter to be tested
     * @return true if line is valid
     * @throws IllegalArgumentException
     *             if line is invalid
     */
    private static final boolean validateLine(final String line) {
        return Validators.nonEmpty(line);
    }

    /**
     * Validator for "lineNum" parameter. Positive is expected.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:54:03 PM Oct 3, 2015</i>
     * 
     * @param lineNum
     *            lineNum parameter to be validated
     * @return true if lineNum is valid
     * @throws IllegalArgumentException
     *             if lineNum is invalid
     */
    private static final boolean validateLineNum(final long lineNum) {
        return Validators.positive(lineNum);
    }

    /**
     * This logger is NOT time aware, meaning that id doesn't recognize time stamps in logs.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:28:21 PM Oct 3, 2015</i>
     * 
     * @see dburyak.logmist.model.manipulators.ILogFileParser#isTimeAware()
     * @return false, indicating that this logger is NOT time aware
     */
    @Override
    public final boolean isTimeAware() {
        return false;
    }

    /**
     * Perform parsing of one given line.
     * {@link LogEntry} is constructed "as is" - just from picking given line and lineNumber.
     * <br/><b>PRE-conditions:</b> non-empty line, positive lineNum
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:30:03 PM Oct 3, 2015</i>
     * 
     * @see dburyak.logmist.model.manipulators.LogFileParserBase#doParseLine(java.lang.String, long)
     * @param line
     *            line to be parsed
     * @param lineNum
     *            line number of this line in log file
     * @return new {@link LogEntry} parsed from given line and line number
     * @throws ParseException
     *             never
     */
    @SuppressWarnings("boxing")
    @Override
    protected final LogEntry doParseLine(final String line, final long lineNum) throws ParseException {
        LOG.entry(line, lineNum);

        validateLine(line);
        validateLineNum(lineNum);
        final LogEntry result = new LogEntry(line, lineNum);

        return LOG.exit(result);
    }

}
