package dburyak.logmist.model;


import java.io.Serializable;
import java.util.function.Predicate;

import dburyak.jtools.AssertConst;
import dburyak.jtools.Validators;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;


/**
 * Project : logmist.<br/>
 * Ends-with filter. <br/>
 * <b>Created on:</b> <i>1:37:27 AM Jul 23, 2015</i>
 *
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
@ThreadSafe
@javax.annotation.concurrent.Immutable
@javax.annotation.concurrent.ThreadSafe
public final class EndsWithFilter extends PredicateFilter {

    /**
     * Serialization version ID. <br/>
     * <b>Created on:</b> <i>1:38:05 AM Jul 23, 2015</i>
     */
    private static final long serialVersionUID = 1L;


    /**
     * Project : logmist.<br/>
     * Ends-with predicate to test ending of log message to given string. <br/>
     * <b>Created on:</b> <i>1:38:57 AM Jul 23, 2015</i>
     *
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     */
    @Immutable
    @ThreadSafe
    @javax.annotation.concurrent.Immutable
    @javax.annotation.concurrent.ThreadSafe
    private static final class EndsWithPredicate implements Predicate<LogEntry>, Serializable {

        /**
         * Serial version ID. <br/>
         * <b>Created on:</b> <i>1:38:54 AM Jul 23, 2015</i>
         */
        private static final long serialVersionUID = 1L;


        /**
         * Suffix for this predicate. <br/>
         * <b>Created on:</b> <i>1:45:40 AM Jul 23, 2015</i>
         */
        private final String suffix;

        /**
         * Suffix in lower case for this predicate. <br/>
         * <b>Created on:</b> <i>1:45:47 AM Jul 23, 2015</i>
         */
        private final String suffixLowerCase;

        /**
         * Indicates whether case is ignored. <br/>
         * <b>Created on:</b> <i>1:46:06 AM Jul 23, 2015</i>
         */
        private final boolean ignoreCase;


        /**
         * Constructor for class : [logmist] dburyak.logmist.model.EndsWithPredicate.<br/>
         * <br/>
         * <b>PRE-conditions:</b> non-null argument <br/>
         * <b>POST-conditions:</b> NONE <br/>
         * <b>Side-effects:</b> NONE <br/>
         * <b>Created on:</b> <i>1:46:25 AM Jul 23, 2015</i>
         *
         * @param suffix
         *            suffix for this predicate
         * @param ignoreCase
         *            indicates whether case should be ignored
         */
        @SuppressWarnings("synthetic-access")
        private EndsWithPredicate(final String suffix, final boolean ignoreCase) {
            assert (EndsWithFilter.validateSuffix(suffix)) : AssertConst.ASRT_INVALID_ARG;

            this.suffix = suffix;
            this.suffixLowerCase = suffix.toLowerCase();
            this.ignoreCase = ignoreCase;
        }

        /**
         * Tests whether given log entry ends with suffix from this predicate. <br/>
         * <b>PRE-conditions:</b> non-null arg <br/>
         * <b>POST-conditions:</b> NONE <br/>
         * <b>Side-effects:</b> NONE <br/>
         * <b>Created on:</b> <i>1:47:24 AM Jul 23, 2015</i>
         *
         * @see java.util.function.Predicate#test(java.lang.Object)
         * @param t
         *            log entry to be tested by this predicate
         * @return true if log entry message ends with suffix from this predicate
         */
        @Override
        public final boolean test(final LogEntry t) {
            if (ignoreCase) {
                return t.getMsg().toLowerCase().endsWith(suffixLowerCase);
            } else {
                return t.getMsg().endsWith(suffix);
            }
        }
    }


    /**
     * Suffix for this filter. <br/>
     * <b>Created on:</b> <i>1:58:56 AM Jul 23, 2015</i>
     */
    private final String suffix;

    /**
     * Indicates whether case is ignored. <br/>
     * <b>Created on:</b> <i>1:59:08 AM Jul 23, 2015</i>
     */
    private final boolean ignoreCase;


    /**
     * Validator for suffix. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>1:43:10 AM Jul 23, 2015</i>
     *
     * @param suffix
     *            suffix to be validated
     * @return true if suffix is valid
     * @throws IllegalArgumentException
     *             if suffix is invalid
     */
    private static final boolean validateSuffix(final String suffix) {
        return Validators.nonEmpty(suffix);
    }


    /**
     * Constructor for class : [logmist] dburyak.logmist.model.EndsWithFilter.<br/>
     * <br/>
     * <b>PRE-conditions:</b> non-null name and non-empty suffix <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>1:52:20 AM Jul 23, 2015</i>
     *
     * @param name
     *            name of this filter
     * @param suffix
     *            suffix for this ends-with filter
     * @param ignoreCase
     *            indicates whether case should be ignored
     */
    @SuppressWarnings("synthetic-access")
    public EndsWithFilter(final String name, final String suffix, final boolean ignoreCase) {
        super(name, new EndsWithPredicate(suffix, ignoreCase));

        validateSuffix(suffix);

        this.suffix = suffix;
        this.ignoreCase = ignoreCase;
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.EndsWithFilter.<br/>
     * Create case-sensitive "ends-with" filter. <br/>
     * <b>PRE-conditions:</b> non-empty arguments <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>1:55:00 AM Jul 23, 2015</i>
     *
     * @param name
     *            name of this filter
     * @param suffix
     *            suffix for this filter
     */
    public EndsWithFilter(final String name, final String suffix) {
        this(name, suffix, false);
    }

    /**
     * Get string representaion of predicate for this filter. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>1:37:28 AM Jul 23, 2015</i>
     *
     * @see dburyak.logmist.model.PredicateFilter#predicateToString()
     * @return string representation of predicate of this filter
     */
    @Override
    final String predicateToString() {
        final StringBuilder sb = (new StringBuilder("{suffix=[")).append(suffix); //$NON-NLS-1$
        sb.append("],ignoreCase=[").append(ignoreCase).append("]}"); //$NON-NLS-1$ //$NON-NLS-2$
        return sb.toString();
    }

}
