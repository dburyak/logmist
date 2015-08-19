package dburyak.logmist.model;


import java.io.Serializable;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import dburyak.jtools.AssertConst;
import dburyak.jtools.Validators;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;


/**
 * Project : logmist.<br/>
 * Log filter based on regular expression matching.<br/>
 * <b>Created on:</b> <i>2:07:07 AM Jul 22, 2015</i>
 *
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.2
 */
@Immutable
@ThreadSafe
@javax.annotation.concurrent.Immutable
@javax.annotation.concurrent.ThreadSafe
public final class RegexpFilter extends PredicateFilter {

    /**
     * Serial version ID. <br/>
     * <b>Created on:</b> <i>2:28:15 AM Jul 22, 2015</i>
     */
    private static final long serialVersionUID = 1L;


    /**
     * Project : logmist.<br/>
     * Predicate which test given log entry against matching to regular expression. <br/>
     * <b>Created on:</b> <i>1:01:45 AM Jul 23, 2015</i>
     *
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     */
    @Immutable
    @ThreadSafe
    @javax.annotation.concurrent.Immutable
    @javax.annotation.concurrent.ThreadSafe
    private static final class RegexpPredicate implements Predicate<LogEntry>, Serializable {

        /**
         * <br/>
         * <b>Created on:</b> <i>1:19:20 AM Jul 23, 2015</i>
         */
        private static final long serialVersionUID = 1L;

        /**
         * Regexp pattern for this predicate. <br/>
         * <b>Created on:</b> <i>1:02:21 AM Jul 23, 2015</i>
         */
        private final Pattern pattern;

        /**
         * Indicates whether full line match is performed. <br/>
         * <b>Created on:</b> <i>12:31:55 AM Jul 29, 2015</i>
         */
        private final boolean fullMatch;


        /**
         * Constructor for class : [logmist] dburyak.logmist.model.RegexpPredicate.<br/>
         * <br/>
         * <b>PRE-conditions:</b> non-null argument <br/>
         * <b>POST-conditions:</b> NONE <br/>
         * <b>Side-effects:</b> NONE <br/>
         * <b>Created on:</b> <i>1:05:22 AM Jul 23, 2015</i>
         *
         * @param pattern
         *            pattern to be used for this predicate
         * @param ignoreCase
         *            indicates whether case should be ignored
         * @param fullMatch
         *            indicates whether full line matching is performed
         */
        @SuppressWarnings("synthetic-access")
        private RegexpPredicate(final Pattern pattern, final boolean ignoreCase, final boolean fullMatch) {
            assert(RegexpFilter.validatePattern(pattern)) : AssertConst.ASRT_INVALID_ARG;

            if (ignoreCase) {
                this.pattern = Pattern.compile(pattern.pattern(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            } else {
                this.pattern = pattern;
            }
            this.fullMatch = fullMatch;
        }

        /**
         * Test given log entry by matching against regexp pattern of this predicate.
         * <br/>
         * <b>PRE-conditions:</b> NONE <br/>
         * <b>POST-conditions:</b> NONE <br/>
         * <b>Side-effects:</b> NONE <br/>
         * <b>Created on:</b> <i>1:06:50 AM Jul 23, 2015</i>
         *
         * @see java.util.function.Predicate#test(java.lang.Object)
         * @param t
         *            log entry to be tested
         * @return true if given log entry matches against pattern of this predicate
         * @throws NullPointerException
         *             if null is passed
         */
        @Override
        public boolean test(final LogEntry t) {
            if (!fullMatch) {
                return pattern.matcher(t.getMsg()).find();
            } else {
                return pattern.matcher(t.getMsg()).matches();
            }
        }

    }


    /**
     * Regexp pattern of the filter. <br/>
     * <b>Created on:</b> <i>2:23:53 AM Jul 22, 2015</i>
     */
    private final Pattern pattern;

    /**
     * Indicates whether case should be ignored. <br/>
     * <b>Created on:</b> <i>1:09:12 AM Jul 23, 2015</i>
     */
    private final boolean ignoreCase;

    /**
     * Indicates whether full line mathing is performed. <br/>
     * <b>Created on:</b> <i>12:27:45 AM Jul 29, 2015</i>
     */
    private final boolean fullMatch;


    /**
     * Validator for pattern. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>2:24:19 AM Jul 22, 2015</i>
     *
     * @param pattern
     *            pattern to be validated
     * @return true if pattern is valid
     * @throws IllegalArgumentException
     *             if pattern is invalid
     */
    private static final boolean validatePattern(final Pattern pattern) {
        return Validators.nonNull(pattern);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.RegexpFilter.<br/>
     * Creates a case-sensitive regexp filter. <br/>
     * <b>PRE-conditions:</b> non-null args <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>2:25:30 AM Jul 22, 2015</i>
     *
     * @param name
     *            name for the filter
     * @param pattern
     *            regexp pattern for the filter
     */
    public RegexpFilter(final String name, final Pattern pattern) {
        this(name, pattern, false);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.RegexpFilter.<br/>
     * Provides "ignore case" option. <br/>
     * <b>PRE-conditions:</b> non-null args <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>3:16:18 AM Jul 22, 2015</i>
     *
     * @param name
     *            name for the filter
     * @param pattern
     *            regexp pattern for the filter
     * @param ignoreCase
     *            indicates whether case should be ignored
     */
    public RegexpFilter(final String name, final Pattern pattern, final boolean ignoreCase) {
        this(name, pattern, ignoreCase, false);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.RegexpFilter.<br/>
     * Most specific constructor, provides most advanced tuning. <br/>
     * <b>PRE-conditions:</b> <br/>
     * <b>POST-conditions:</b> <br/>
     * <b>Side-effects:</b> <br/>
     * <b>Created on:</b> <i>12:35:13 AM Jul 29, 2015</i>
     * 
     * @param name
     *            name for the filter
     * @param pattern
     *            regexp pattern for the filter
     * @param ignoreCase
     *            indicates whether case should be ignored
     * @param fullMatch
     *            indicates whether full string matching should be perfomred
     */
    @SuppressWarnings("synthetic-access")
    public RegexpFilter(final String name, final Pattern pattern, final boolean ignoreCase, final boolean fullMatch) {
        super(name, new RegexpPredicate(pattern, ignoreCase, fullMatch));

        validatePattern(pattern);

        this.pattern = pattern;
        this.ignoreCase = ignoreCase;
        this.fullMatch = fullMatch;
    }

    /**
     * Get string representation of predicate for this regexp filter. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>1:11:50 AM Jul 23, 2015</i>
     *
     * @see dburyak.logmist.model.PredicateFilter#predicateToString()
     * @return string representation of predicate for this regexp filter
     */
    @Override
    protected String predicateToString() {
        final StringBuilder sb = (new StringBuilder("{pattern=[")).append(pattern.pattern()); //$NON-NLS-1$
        sb.append("],ignoreCase=[").append(ignoreCase); //$NON-NLS-1$
        sb.append("],fullMatch=[").append(fullMatch).append("]}"); //$NON-NLS-1$ //$NON-NLS-2$
        return sb.toString();
    }

}
