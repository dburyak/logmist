package dburyak.logmist.model.manipulators;


import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.jtools.AssertConst;
import dburyak.logmist.model.LogEntry;


/**
 * Project : logmist.<br/>
 * Base abstract class with default implementation for common methods.
 * <br/><b>Created on:</b> <i>2:22:25 AM Oct 2, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public abstract class LogFileParserBase implements ILogFileParser {

    /**
     * System logger for this class.
     * <br/><b>Created on:</b> <i>2:23:59 AM Oct 2, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(LogFileParserBase.class);


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
     * Template method action to be implemented by subclasses. <br/>
     * Perform parsing of given line from log file.
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
     */
    protected abstract LogEntry doParseLine(final String line, final long lineNum);

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
