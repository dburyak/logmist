package dburyak.logmist.model.manipulators;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.jtools.AssertConst;
import dburyak.jtools.Validators;
import dburyak.logmist.exceptions.InaccessibleFileException;
import dburyak.logmist.exceptions.ParseException;
import dburyak.logmist.model.LogEntry;
import net.jcip.annotations.NotThreadSafe;


/**
 * Project : logmist.<br/>
 * <b>Designed for inheritance.</b><br/>
 * Base abstract class with default implementation for common methods.
 * <br/><b>Created on:</b> <i>2:22:25 AM Oct 2, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@NotThreadSafe
@javax.annotation.concurrent.NotThreadSafe
public abstract class LogFileParserBase implements ILogFileParser {

    // TODO : make it thread safe

    /**
     * System logger for this class.
     * <br/><b>Created on:</b> <i>2:23:59 AM Oct 2, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(LogFileParserBase.class);

    /**
     * Number of lines to try parsing in {@link LogFileParserBase#canParse(Path)} method.
     * <br/><b>Created on:</b> <i>2:05:40 AM Oct 3, 2015</i>
     */
    private static final int NUM_LINES_TO_TEST = 5;


    /**
     * Validator for {@link LogParseEvent} event.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:11:50 AM Oct 2, 2015</i>
     * 
     * @param event
     *            log parse event to be validated
     * @return true if event is valid
     * @throws IllegalArgumentException
     *             if given event is invalid
     */
    private static final boolean validateLogParseEvent(final LogParseEvent event) {
        final boolean isValid = event.getLinesParsed() <= event.getLinesTotal();
        if (!isValid) {
            throw new IllegalArgumentException();
        }
        return isValid;
    }

    /**
     * Validator for "filePath" parameter.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>10:22:59 PM Sep 16, 2015</i>
     * 
     * @param filePath
     *            parameter to be validated
     * @return true if filePath is non-null
     * @throws IllegalArgumentException
     *             if filePath is null
     */
    private static final boolean validateFilePath(final Path filePath) {
        return Validators.nonNull(filePath);
    }


    /**
     * Set of registered listeners for {@link LogParseEvent} events.
     * <br/><b>Created on:</b> <i>2:24:32 AM Oct 2, 2015</i>
     */
    private final Set<ILogParseEventHandler> listeners = new HashSet<>();


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
     * Register listener to be notified about {@link LogParseEvent} events from this parser.
     * <br/><b>PRE-conditions:</b> non-null arg
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> reference to given handler is stored, if not removed by
     * {@link LogFileParserBase#removeListener(ILogParseEventHandler)} it will prevent handler from being garbage
     * collected
     * <br/><b>Created on:</b> <i>2:22:25 AM Oct 2, 2015</i>
     * 
     * @see dburyak.logmist.model.manipulators.ILogFileParser#addListener(dburyak.logmist.model.manipulators.ILogParseEventHandler)
     * @param handler
     *            log parse event handler to be registered by this parser
     */
    @SuppressWarnings("nls")
    @Override
    public final void addListener(final ILogParseEventHandler handler) {
        LOG.trace("log parser registerd handler : parser = [%s] ; handler = [%s]", this, handler);
        if (!listeners.add(handler)) {
            LOG.warn("registering same handler twice : parser = [%s] ; handler = [%s]", this, handler, new Throwable());
        }
    }

    /**
     * Unregister given listener from further {@link LogParseEvent} notifications from this parser.
     * <br/><b>PRE-conditions:</b> non-null arg
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> reference to given handler is removed from internal set, thus given handler now can be
     * garbage collected
     * <br/><b>Created on:</b> <i>2:22:25 AM Oct 2, 2015</i>
     * 
     * @see dburyak.logmist.model.manipulators.ILogFileParser#removeListener(dburyak.logmist.model.manipulators.ILogParseEventHandler)
     * @param handler
     *            log parse event handler to be unregistered by this parser
     */
    @SuppressWarnings("nls")
    @Override
    public final void removeListener(final ILogParseEventHandler handler) {
        LOG.trace("log parser unregistered handler : parser = [%s] ; handler = [%s]", this, handler);
        if (!listeners.remove(handler)) {
            LOG.warn("trying to unregister non-registered handler : parser = [%s] ; handler = [%s]",
                this, handler, new Throwable());
        }
    }

    /**
     * <b>Template method</b><br/>
     * Read and try to parse two lines from file. If parsing was successful, then return true, return false otherwise.
     * <br/><b>PRE-conditions:</b> non-null and readable filePath
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> I/O operations are performed (open, read, close)
     * <br/><b>Created on:</b> <i>1:52:20 AM Oct 3, 2015</i>
     * 
     * @see dburyak.logmist.model.manipulators.ILogFileParser#canParse(java.nio.file.Path)
     * @param filePath
     *            file to be tested
     * @return true if this parser can parse given file
     * @throws InaccessibleFileException
     *             if file is not accessible (non-existent, no permissions for reading, etc.)
     */
    @SuppressWarnings({ "boxing", "nls" })
    @Override
    public final boolean canParse(final Path filePath) throws InaccessibleFileException {
        LOG.entry(filePath);

        validateFilePath(filePath);
        if (!LogFileParserUtils.isAccessibleReadable(filePath)) {
            throw LOG.throwing(Level.DEBUG, new InaccessibleFileException(filePath));
        }

        boolean canParse = true;
        try (final BufferedReader in = Files.newBufferedReader(filePath)) {
            for (int i = 0; i < NUM_LINES_TO_TEST; i++) {
                final String line = in.readLine();
                if (line == null) { // no more lines in file
                    canParse = false;
                    break;
                }
                try {
                    final LogEntry log = doParseLine(line, i + 1);
                    assert(log != null) : AssertConst.ASRT_NULL_VALUE;
                } catch (final ParseException e) {
                    LOG.catching(Level.TRACE, e);
                    LOG.debug("parser did not recognize the line : parser = [%s] ; line = [%s] ; "
                        + "filePath = [%s] ; lineNum = [%d]");
                    canParse = false;
                    break;
                }
            }
        } catch (final IOException e) {
            LOG.catching(Level.TRACE, e);
            LOG.error("IO exception when reading file : filePath = [%s]", filePath); //$NON-NLS-1$
            canParse = false;
        }

        return LOG.exit(canParse);
    }

    /**
     * <b>Template method</b><br/>
     * Parse given log file.
     * <br/><b>PRE-conditions:</b> accessible filePath
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> I/O operations are performed (open, read, close etc.).
     * <br/><b>Created on:</b> <i>4:15:47 AM Oct 3, 2015</i>
     * 
     * @see dburyak.logmist.model.manipulators.ILogFileParser#parse(java.nio.file.Path)
     * @param filePath
     *            log file to be parsed
     * @return collection of parsed log entries
     * @throws InaccessibleFileException
     *             if given log file cannot be read for some reason
     * @throws ParseException
     *             if parser encounters line of unsupported format in log file
     */
    @SuppressWarnings({ "nls", "boxing" })
    @Override
    public final Collection<LogEntry> parse(final Path filePath) throws InaccessibleFileException, ParseException {
        LOG.entry(filePath);
        validateFilePath(filePath);

        if (!LogFileParserUtils.isAccessibleReadable(filePath)) {
            LOG.warn("file is not accessible : file = [%s]", filePath); //$NON-NLS-1$
            throw LOG.throwing(Level.DEBUG, new InaccessibleFileException(filePath));
        }

        final Collection<LogEntry> resultLogs = new LinkedList<>();
        try {
            final Collection<String> allLines = Files.readAllLines(filePath);
            final long linesTotal = allLines.size();
            long linesRead = 0;
            notifyParseEvent(new LogParseEvent(linesTotal, linesRead)); // initialize parse status
            for (final String line : allLines) {
                linesRead++;
                if (line.isEmpty()) {
                    LOG.warn("empty line read : lineNum = [%s]", linesRead);
                } else {
                    final LogEntry log = doParseLine(line, linesRead); // call concrete implementation of subclass
                    resultLogs.add(log);
                }
                notifyParseEvent(new LogParseEvent(linesTotal, linesRead));
            }
        } catch (final IOException ex) {
            LOG.error("error when reading file : file = [%s]", filePath, ex); //$NON-NLS-1$
            throw new ParseException(toString(), "");
        }

        assert(resultLogs != null) : AssertConst.ASRT_NULL_RESULT;
        return LOG.exit(resultLogs);
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
     * Send {@link LogParseEvent} notification to all registered handlers.
     * <br/><b>PRE-conditions:</b> valid log parse event
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> all the registered handlers are invoked (in parallel)
     * <br/><b>Created on:</b> <i>3:08:38 AM Oct 2, 2015</i>
     * 
     * @param event
     *            log parse event to be passed to all registered listeners
     */
    private final void notifyParseEvent(final LogParseEvent event) {
        assert(validateLogParseEvent(event)) : AssertConst.ASRT_INVALID_ARG;
        listeners.parallelStream().forEach(listener -> listener.handleLogParseEvent(event));
    }


}
