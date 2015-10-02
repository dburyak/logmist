package dburyak.logmist.model.manipulators;


import java.nio.file.Path;
import java.util.Collection;

import dburyak.logmist.exceptions.InaccessibleFileException;
import dburyak.logmist.model.LogEntry;


// TODO : code style
/**
 * Project : logmist.<br/>
 * Log file parser. Can be used for testing parser on given file and for parsing.
 * <br/><b>Created on:</b> <i>4:12:21 AM Aug 20, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public interface ILogFileParser {

    /**
     * Indicates whether file format of this parser has time stamps.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:14:58 AM Oct 2, 2015</i>
     * 
     * @return true if this log parser format deals with time stamps
     */
    public boolean isTimeAware();

    /**
     * Check if parser supports log format of given log file.
     * <br/><b>PRE-conditions:</b> <code>filePath</code> non-null arg
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> I/O operations are performed (open file, read, close file)
     * <br/><b>Created on:</b> <i>4:17:46 AM Aug 20, 2015</i>
     * 
     * @param filePath
     *            path to the log file to be tested
     * @return true if this parser supports log format of given log file, false otherwise
     * @throws InaccessibleFileException
     *             if file cannot be opened or read
     */
    public boolean canParse(final Path filePath) throws InaccessibleFileException;

    /**
     * Parse the whole file.
     * <br/><b>PRE-conditions:</b> <code>filePath</code> is a file and can be opened for read
     * <br/><b>POST-conditions:</b> non-null result; can be empty if trying to parse log file format not supported by
     * this parser
     * <br/><b>Side-effects:</b> I/O operations are performed (open file, read, close file)
     * <br/><b>Created on:</b> <i>4:20:32 AM Aug 20, 2015</i>
     * 
     * @param filePath
     *            path to the log file to be parsed
     * @return collection of parsed log entries; empty collection
     * @throws InaccessibleFileException
     *             if file cannot be opened for read
     */
    public Collection<LogEntry> parse(final Path filePath) throws InaccessibleFileException;

    /**
     * Register a handler to be notified about {@link LogParseEvent} events.
     * <br/><b>PRE-conditions:</b> non-null arg
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> IMPLEMENTATION DEPENDENT
     * <br/><b>Created on:</b> <i>3:15:46 AM Oct 2, 2015</i>
     * 
     * @param handler
     *            log parse event handler to be registered
     */
    public void addListener(final ILogParseEventHandler handler);

    /**
     * Unregister a handler from receiving notifications about {@link LogParseEvent} events from this parser.
     * <br/><b>PRE-conditions:</b> non-null arg
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> IMPLEMENTATION DEPENDENT
     * <br/><b>Created on:</b> <i>3:17:21 AM Oct 2, 2015</i>
     * 
     * @param handler
     *            log parse event handler to be unregistered
     */
    public void removeListener(final ILogParseEventHandler handler);

}
