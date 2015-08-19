package dburyak.logmist.model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;


/**
 * Project : logmist.<br/>
 * "Around" time filter. Represents concept : 5 seconds around Jun 1 2015 10:08:11 </br>
 * Used to check if log is around "instant" within "duration". </br>
 * Can be very useful for checking related events around the specified one.
 * <br/><b>Created on:</b> <i>2:24:55 AM Aug 15, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
@ThreadSafe
@javax.annotation.concurrent.Immutable
@javax.annotation.concurrent.ThreadSafe
public final class AroundFilter extends TimeFilter {

    /**
     * <br/><b>Created on:</b> <i>2:27:34 AM Aug 15, 2015</i>
     */
    private static final long serialVersionUID = 1L;


    /**
     * Create a new "around" predicate.
     * <br/><b>PRE-conditions:</b> valid args
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:30:39 AM Aug 15, 2015</i>
     * 
     * @param instant
     *            instant for around
     * @param duration
     *            duration of around
     * @return new around predicate
     */
    private static final Predicate<LogEntry> newAroundPredicate(final LocalDateTime instant, final Duration duration) {
        TimeFilter.validateInstant(instant);
        TimeFilter.validateDuration(duration);
        final Duration durOver = duration.plusNanos(1L);
        return (log) -> {
            return log.getTime().isAfter(instant.minus(durOver))
                && log.getTime().isBefore(instant.plus(durOver));
        };
    }

    /**
     * Create a new "around" predicate.
     * <br/><b>PRE-conditions:</b> valid args
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:59:44 AM Aug 20, 2015</i>
     * 
     * @param log
     *            log for around filter
     * @param duration
     *            duration of filter
     * @return new around predicate
     */
    private static final Predicate<LogEntry> newAroundPredicate(final LogEntry log, final Duration duration) {
        TimeFilter.validateLog(log);
        TimeFilter.validateDuration(duration);
        final Duration durOver = duration.plusNanos(1L);
        return (logEntry) -> {
            return logEntry.getTime().isAfter(log.getTime().minus(durOver))
                && logEntry.getTime().isBefore(log.getTime().plus(durOver));
        };
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.AroundFilter.<br/>
     * <br/><b>PRE-conditions:</b> non-empty name, non-null instant, non-negative and non-zero duration
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:24:55 AM Aug 15, 2015</i>
     * 
     * @param name
     *            name for the filter
     * @param instant
     *            instant for the filter
     * @param duration
     *            duration for the filter
     */
    public AroundFilter(final String name, final LocalDateTime instant, final Duration duration) {
        super(name, newAroundPredicate(instant, duration), instant, duration);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.AroundFilter.<br/>
     * <br/><b>PRE-conditions:</b> non-empty name, non-null instant, non-negative and non-zero duration
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:24:55 AM Aug 15, 2015</i>
     * 
     * @param name
     *            name for the filter
     * @param log
     *            log for the filter
     * @param duration
     *            duration for the filter
     */
    public AroundFilter(final String name, final LogEntry log, final Duration duration) {
        super(name, newAroundPredicate(log, duration), log, duration);
    }

}
