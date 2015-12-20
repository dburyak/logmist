package dburyak.logmist.model.parsers;


import dburyak.logmist.model.LogEntry;
import rx.Observable;
import rx.Single;


/**
 * Project : logmist.<br/>
 * Log file parser. Can be used for testing parser on given file and for parsing.
 * <br/><b>Created on:</b> <i>4:12:21 AM Aug 20, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.2
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
     * Check if parser supports log format of given lines.
     * <br/><b>PRE-conditions:</b> non-null lines
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:17:46 AM Aug 20, 2015</i>
     * 
     * @param lines
     *            lines that should be tested
     * @return observable that emits result
     */
    public Single<Boolean> canParse(final Observable<String> lines);

    /**
     * Parse received lines (usually lines are retrieved from log file).
     * <br/><b>PRE-conditions:</b> non-null lines
     * <br/><b>POST-conditions:</b> non-null result; can be empty if trying to parse log file format not supported by
     * this parser
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:20:32 AM Aug 20, 2015</i>
     * 
     * @param lines
     *            observable that emits strings to be parsed
     * @return observable that emits parsed log entries; empty observable (only onCompleted will be called)
     * @emits ParseException
     *        (emitted)
     *        when line with unexpected format encountered
     */
    public Observable<LogEntry> parse(final Observable<String> lines);

}
