package dburyak.logmist.model.parsers;


import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.jtools.AssertConst;
import dburyak.jtools.Validators;
import dburyak.logmist.Resources;
import dburyak.logmist.Resources.ConfigID;
import dburyak.logmist.exceptions.ParseException;
import dburyak.logmist.model.LogEntry;
import rx.Observable;
import rx.Single;


/**
 * Project : logmist.<br/>
 * <b>Designed for inheritance.</b><br/>
 * Base abstract class with default implementation for common methods.
 * <br/><b>Created on:</b> <i>2:22:25 AM Oct 2, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.2
 */
public abstract class LogFileParserBase implements ILogFileParser {

    // TODO : make it thread safe (if it is not already)

    /**
     * System logger for this class.
     * <br/><b>Created on:</b> <i>2:23:59 AM Oct 2, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(LogFileParserBase.class);


    /**
     * Validator for "lines" parameter.
     * <br/><b>PRE-conditions:</b> non-null lines
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>7:20:09 AM Dec 20, 2015</i>
     * 
     * @param lines
     *            "lines" parameter to be validated
     * @return true if "lines" is valid
     * @throws IllegalArgumentException
     *             if "lines" is invalid
     */
    private static final boolean validateLines(final Observable<String> lines) {
        return Validators.nonNull(lines);
    }


    /**
     * String representation of this parser.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-empty string
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:44:37 AM Sep 17, 2015</i>
     * 
     * @see java.lang.Object#toString()
     * @return string representation of this parser
     */
    @Override
    public final String toString() {
        return getClass().getSimpleName();
    }

    /**
     * <b>Designed for inheritance.</b><br/>
     * Template method action to be implemented by subclasses. <br/>
     * Perform parsing of given line from log file.<br/>
     * <b>WARN</b> : implement only parsing here, no parse notifications needed here.
     * <br/><b>PRE-conditions:</b> non-null line, non-negative lineNum
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:28:33 AM Oct 2, 2015</i>
     * 
     * @param line
     *            line from log file to be parsed
     * @param lineNum
     *            line number in log file
     * @return log entry parsed from given line
     * @throws ParseException
     *             if line with unexpected format given
     */
    protected abstract LogEntry doParseLine(final String line, final long lineNum) throws ParseException;


    /**
     * <b>Template method</b><br/>
     * Read and try to parse two lines from file. If parsing was successful, then return true, return false otherwise.
     * <br/><b>PRE-conditions:</b> non-null lines
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>6:03:13 AM Dec 20, 2015</i>
     * 
     * @see dburyak.logmist.model.parsers.ILogFileParser#canParse(rx.Observable)
     * @param lines
     *            source of lines for analysis
     * @return true if this parser can parse given lines, false otherwise
     */
    @SuppressWarnings({ "nls", "boxing" })
    @Override
    public final Single<Boolean> canParse(final Observable<String> lines) {
        LOG.entry(lines);
        validateLines(lines);

        final int numLines = Integer.parseInt(
            Resources.getInstance().getConfigProp(ConfigID.CORE_PARSERS_NUM_LINES_TO_TEST));
        LOG.debug("num lines for test : numLines = [%d]", numLines);

        return LOG.exit(lines.take(numLines)
            .map(line -> { // line -> isParsed by this parser
                LogEntry log = null;
                try {
                    log = doParseLine(line, 1);
                    assert (log != null) : AssertConst.ASRT_NULL_VALUE;
                } catch (final ParseException ex) {
                    LOG.debug("cannot parse line, treating format as unknown : parser = [%s] ; line = [%s]",
                        this, line);
                }
                LOG.debug("testing log line : isParsed = [%b] ; line = [%s] ; parser = [%s]", log != null, line, this);
                return (log != null);
            })
            .takeUntil(isParsed -> !isParsed)
            .defaultIfEmpty(false)
            .reduce(true, Boolean::logicalAnd)
            .toSingle()
            .doOnSuccess(canParse -> LOG.debug("can parse : canParse = [%b]", canParse)));
    }

    /**
     * <b>Template method</b><br/>
     * Parse received lines.
     * <br/><b>PRE-conditions:</b> non-null lines
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>6:27:00 AM Dec 20, 2015</i>
     * 
     * @see dburyak.logmist.model.parsers.ILogFileParser#parse(rx.Observable)
     * @param lines
     *            input lines to be parsed
     * @return source of parsed log entries
     * @emits ParseException (emitted) if line of unexpected format was received from input
     */
    @SuppressWarnings({ "boxing", "nls" })
    @Override
    public final Observable<LogEntry> parse(final Observable<String> lines) {
        LOG.entry(lines);
        validateLines(lines);

        final AtomicInteger lineNum = new AtomicInteger(0);
        return LOG.exit(lines.doOnNext(line -> lineNum.incrementAndGet())
            .map(line -> {
                LogEntry log = null;
                if (line.isEmpty()) {
                    LOG.warn("empty line read : lineNum = [%d]", lineNum.get());
                } else {
                    try {
                        log = doParseLine(line, lineNum.get());
                        LOG.debug("log line parsed : linNum = [%d] ; line = [%s]", lineNum.get(), line);
                        assert (log != null);
                    } catch (final ParseException ex) {
                        LOG.error("line with unexpected format : parser = [%s] ; line = [%s]", this, line);
                    }
                }
                return log;
            }));
    }

}
