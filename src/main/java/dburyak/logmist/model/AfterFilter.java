package dburyak.logmist.model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;


/**
 * Project : logmist.<br/>
 * "After" filter. Represents predicate : all during 10 seconds after Jul 1 15:56:03. </br>
 * Used to check if log is after "instant" within "duration". </br>
 * Can be very useful for checking consequence events before the specified one.
 * <br/><b>Created on:</b> <i>2:12:32 AM Aug 15, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
@ThreadSafe
@javax.annotation.concurrent.Immutable
@javax.annotation.concurrent.ThreadSafe
public final class AfterFilter extends TimeFilter {

    /**
     * <br/><b>Created on:</b> <i>2:12:42 AM Aug 15, 2015</i>
     */
    private static final long serialVersionUID = 1L;


    /**
     * Crates a new "after" predicate.
     * <br/><b>PRE-conditions:</b> valid args
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:17:59 AM Aug 15, 2015</i>
     * 
     * @param instant
     *            instant for "after"
     * @param duration
     *            duration of "after"
     * @return new "after" predicate
     */
    private static final Predicate<LogEntry> newAfterPredicate(final LocalDateTime instant, final Duration duration) {
        TimeFilter.validateInstant(instant);
        TimeFilter.validateDuration(duration);
        final Duration durOver = duration.plusNanos(1L);
        return (log) -> {
            return log.getTime().isAfter(instant)
                && log.getTime().isBefore(instant.plus(durOver));
        };
    }

    /**
     * Create a new "after" predicate.
     * <br/><b>PRE-conditions:</b> valid args
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:12:39 AM Aug 17, 2015</i>
     * 
     * @param log
     *            log entry for "after"
     * @param duration
     *            duration of "after" period
     * @return new "after" predicate
     */
    private static final Predicate<LogEntry> newAfterPredicate(final LogEntry log, final Duration duration) {
        TimeFilter.validateLog(log);
        TimeFilter.validateDuration(duration);
        final Duration durOver = duration.plusNanos(1L);
        return (logEntry) -> {
            return logEntry.getTime().isAfter(log.getTime())
                && logEntry.getTime().isBefore(log.getTime().plus(durOver));
        };
    }


    /**
     * Constructor for class : [logmist] dburyak.logmist.model.AfterFilter.<br/>
     * <br/><b>PRE-conditions:</b> non-empty name, non null instant, non-negative and non-zero duration
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:12:33 AM Aug 15, 2015</i>
     * 
     * @param name
     *            name for the filter
     * @param instant
     *            instant for the "after"
     * @param duration
     *            duration of the "after"
     */
    public AfterFilter(final String name, final LocalDateTime instant, final Duration duration) {
        super(name, newAfterPredicate(instant, duration), instant, duration);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.AfterFilter.<br/>
     * <br/><b>PRE-conditions:</b> non-empty name, non null log, non-negative and non-zero duration
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:12:33 AM Aug 15, 2015</i>
     * 
     * @param name
     *            name for the filter
     * @param log
     *            log for the "after"
     * @param duration
     *            duration of the "after"
     */
    public AfterFilter(final String name, final LogEntry log, final Duration duration) {
        super(name, newAfterPredicate(log, duration), log, duration);
    }

}
