package dburyak.logmist.model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Predicate;

import dburyak.jtools.Validators;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;


/**
 * Project : logmist.<br/>
 * Filter based on time filtering.</br>
 * Examples:
 * 
 * <pre>
 * Before instant = Jul 7, duration = 5 min
 * After instant = Jul 8, duration = 3 hours
 * Around instant = Jan 1 10:05:23, duration = 5 sec
 * </pre>
 * 
 * <br/><b>Created on:</b> <i>11:53:02 PM Aug 13, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
@ThreadSafe
@javax.annotation.concurrent.Immutable
@javax.annotation.concurrent.ThreadSafe
abstract class TimeFilter extends PredicateFilter {

    /**
     * <br/><b>Created on:</b> <i>11:44:09 PM Aug 14, 2015</i>
     */
    private static final long serialVersionUID = 1L;


    /**
     * Validator for "duration" parameter.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:45:23 AM Aug 15, 2015</i>
     * 
     * @param duration
     *            duration to be validated
     * @return true if duration is valid
     * @throws IllegalArgumentException
     *             if duration is invalid
     */
    static final boolean validateDuration(final Duration duration) {
        final boolean isValid = Validators.nonNull(duration) && (!duration.isNegative()) && (!duration.isZero());
        if (!isValid) {
            throw new IllegalArgumentException();
        }
        return isValid;
    }

    /**
     * Validator for "instant" parameter.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:48:29 AM Aug 15, 2015</i>
     * 
     * @param instant
     *            instant to be validated
     * @return true if instant is valid
     * @throws IllegalArgumentException
     *             if instant is invalid
     */
    static final boolean validateInstant(final LocalDateTime instant) {
        return Validators.nonNull(instant);
    }


    /**
     * Instant of this time filter (time point).
     * <br/><b>Created on:</b> <i>12:05:48 AM Aug 15, 2015</i>
     */
    protected final LocalDateTime instant;

    /**
     * Duration for the time filter (length of period).
     * <br/><b>Created on:</b> <i>12:06:00 AM Aug 15, 2015</i>
     */
    protected final Duration duration;


    /**
     * Validator for log parameter.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:28:01 AM Aug 15, 2015</i>
     * 
     * @param log
     *            log entry to be validated
     * @return true if log entry is valid
     * @throws IllegalArgumentException
     *             if log entry is invalid
     */
    static final boolean validateLog(final LogEntry log) {
        return Validators.nonNull(log);
    }


    /**
     * Constructor for class : [logmist] dburyak.logmist.model.TimeFilter.<br/>
     * <br/><b>PRE-conditions:</b> non-empty name, non-null predicate
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:44:17 PM Aug 14, 2015</i>
     * 
     * @param name
     *            name of the filter
     * @param predicate
     *            predicate for the filter
     * @param instant
     *            instant of the time filter
     * @param duration
     *            duration of the time filter
     */
    TimeFilter(
        final String name,
        final Predicate<LogEntry> predicate,
        final LocalDateTime instant,
        final Duration duration) {

        super(name, predicate);

        validateInstant(instant);
        validateDuration(duration);

        this.instant = instant;
        this.duration = duration;
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.TimeFilter.<br/>
     * <br/><b>PRE-conditions:</b> non-empty name, non-null predicate, non-null log, non-negative and non-zero duration
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:30:20 AM Aug 15, 2015</i>
     * 
     * @param name
     *            name for the filter
     * @param predicate
     *            predicate for the filter
     * @param log
     *            log before to filter
     * @param duration
     *            duration within to filter
     */
    TimeFilter(final String name, final Predicate<LogEntry> predicate, final LogEntry log, final Duration duration) {
        this(name, predicate, log.getTime(), duration);
        validateLog(log);
    }

    /**
     * Get string representation of time predicate of this filter. </br>
     * Example:
     * 
     * <pre>
     * {instant=[7 Jul 2013],duration=[30 sec]}
     * </pre>
     * 
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:53:03 PM Aug 13, 2015</i>
     * 
     * @see dburyak.logmist.model.PredicateFilter#predicateToString()
     * @return string representation of this time filter
     */
    @Override
    final String predicateToString() {
        final StringBuilder sb = (new StringBuilder("{instant=[")).append(instant); //$NON-NLS-1$
        sb.append("],duration=[").append(duration).append("]}");  //$NON-NLS-1$//$NON-NLS-2$
        return sb.toString();
    }

}
