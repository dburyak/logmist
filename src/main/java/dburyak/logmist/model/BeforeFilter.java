package dburyak.logmist.model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;


/**
 * Project : logmist.<br/>
 * "Before" time filter. Represents predicate : all during 10 seconds before Jul 1 15:56:03. </br>
 * Used to check if log is before "instant" within "duration". </br>
 * Can be very useful for checking preceding events before the specified one.
 * <br/><b>Created on:</b> <i>11:45:15 PM Aug 13, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
@ThreadSafe
@javax.annotation.concurrent.Immutable
@javax.annotation.concurrent.ThreadSafe
public final class BeforeFilter extends TimeFilter {

    /**
     * <br/><b>Created on:</b> <i>11:45:35 PM Aug 13, 2015</i>
     */
    private static final long serialVersionUID = 1L;


    /**
     * Create a new "before" predicate.
     * <br/><b>PRE-conditions:</b> valid args
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:48:15 AM Aug 15, 2015</i>
     * 
     * @param instant
     *            instant before
     * @param duration
     *            duration within
     * @return new "before" predicate that represents : before instant within duration
     */
    private static final Predicate<LogEntry> newBeforePredicate(final LocalDateTime instant, final Duration duration) {
        TimeFilter.validateInstant(instant);
        TimeFilter.validateDuration(duration);
        final Duration durOver = duration.plusNanos(1L);
        return (log) -> {
            return log.getTime().isBefore(instant)
                && log.getTime().isAfter(instant.minus(durOver));
        };
    }

    /**
     * Create new "before" predicate.
     * <br/><b>PRE-conditions:</b> valid args
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:47:44 AM Aug 18, 2015</i>
     * 
     * @param log
     *            log to check before
     * @param duration
     *            duration of "before"
     * @return new "before" predicate that represents : before log within duration
     */
    private static final Predicate<LogEntry> newBeforePredicate(final LogEntry log, final Duration duration) {
        TimeFilter.validateLog(log);
        TimeFilter.validateDuration(duration);
        final Duration durOver = duration.plusNanos(1L);
        return (logEntry) -> {
            return logEntry.getTime().isBefore(log.getTime())
                && logEntry.getTime().isAfter(log.getTime().minus(durOver));
        };
    }


    /**
     * Constructor for class : [logmist] dburyak.logmist.model.BeforeFilter.<br/>
     * <br/><b>PRE-conditions:</b> non-empty name, non-null instant, non-negative and non-zero duration
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:01:18 AM Aug 15, 2015</i>
     * 
     * @param name
     *            name of the filter
     * @param instant
     *            instant before
     * @param duration
     *            duration within
     */
    public BeforeFilter(final String name, final LocalDateTime instant, final Duration duration) {
        super(name, newBeforePredicate(instant, duration), instant, duration);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.BeforeFilter.<br/>
     * Before "this log" within "duration".
     * <br/><b>PRE-conditions:</b> non-empty name, non-null log, non-negative and non-zero duration
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:52:08 AM Aug 15, 2015</i>
     * 
     * @param name
     *            name for the filter
     * @param log
     *            log that holds instant for this filter
     * @param duration
     *            duration before
     */
    public BeforeFilter(final String name, final LogEntry log, final Duration duration) {
        super(name, newBeforePredicate(log, duration), log, duration);
    }

}
