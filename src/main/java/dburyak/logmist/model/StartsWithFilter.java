package dburyak.logmist.model;


import java.io.Serializable;
import java.util.function.Predicate;

import dburyak.jtools.AssertConst;
import dburyak.jtools.Validators;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;


/**
 * Project : logmist.<br/>
 * Filter for "Starts with ..." filtering. <br/>
 * <b>Created on:</b> <i>11:28:25 PM Jul 22, 2015</i>
 *
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
@ThreadSafe
@javax.annotation.concurrent.Immutable
@javax.annotation.concurrent.ThreadSafe
public final class StartsWithFilter extends PredicateFilter {

    /**
     * Serial version ID. <br/>
     * <b>Created on:</b> <i>11:30:31 PM Jul 22, 2015</i>
     */
    private static final long serialVersionUID = 1L;
    
    
    /**
     * Project : logmist.<br/>
     * "Starts-with" predicate implementation, case-aware. <br/>
     * <b>Created on:</b> <i>12:39:43 AM Jul 23, 2015</i>
     *
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     */
    private static final class StartsWithPredicate implements Predicate<LogEntry>, Serializable {

        /**
         * <br/>
         * <b>Created on:</b> <i>1:19:39 AM Jul 23, 2015</i>
         */
        private static final long serialVersionUID = 1L;
        
        /**
         * Prefix for this predicate. <br/>
         * <b>Created on:</b> <i>12:48:00 AM Jul 23, 2015</i>
         */
        private final String prefix;

        /**
         * Prefix in lower case for this predicate. <br/>
         * <b>Created on:</b> <i>12:48:15 AM Jul 23, 2015</i>
         */
        private final String prefixLowerCase;

        /**
         * Indicates whether case is ignored. <br/>
         * <b>Created on:</b> <i>12:48:27 AM Jul 23, 2015</i>
         */
        private final boolean ignoreCase;
        
        
        /**
         * Constructor for class : [logmist] dburyak.logmist.model.StartsWithPredicate.<br/>
         * <br/>
         * <b>PRE-conditions:</b> non-null arg <br/>
         * <b>POST-conditions:</b> NONE <br/>
         * <b>Side-effects:</b> NONE <br/>
         * <b>Created on:</b> <i>12:40:20 AM Jul 23, 2015</i>
         *
         * @param prefix
         *            prefix for this predicate
         * @param ignoreCase
         *            indicates whether case should be ignored
         */
        @SuppressWarnings("synthetic-access")
        private StartsWithPredicate(final String prefix, final boolean ignoreCase) {
            assert(StartsWithFilter.validatePrefix(prefix)) : AssertConst.ASRT_INVALID_ARG;
            
            this.prefix = prefix;
            this.ignoreCase = ignoreCase;
            this.prefixLowerCase = prefix.toLowerCase();
        }
        
        /**
         * Tests whether given log entry starts with the prfix of this predicate. <br/>
         * <b>PRE-conditions:</b> non-null arg <br/>
         * <b>POST-conditions:</b> NONE <br/>
         * <b>Side-effects:</b> NONE <br/>
         * <b>Created on:</b> <i>12:45:34 AM Jul 23, 2015</i>
         *
         * @see java.util.function.Predicate#test(java.lang.Object)
         * @param t
         *            log entry to be tested
         * @return true if log entry message starts with prefix of this predicate, false otherwise
         * @throws NullPointerException
         *             if null is passed
         */
        @Override
        public boolean test(final LogEntry t) {
            if (ignoreCase) {
                return t.getMsg().toLowerCase().startsWith(prefixLowerCase);
            } else {
                return t.getMsg().startsWith(prefix);
            }
        }
    }
    
    
    /**
     * Prefix for this "starts with" filter. <br/>
     * <b>Created on:</b> <i>11:55:47 PM Jul 22, 2015</i>
     */
    private final String prefix;
    
    /**
     * Ignore case flag for this filter. <br/>
     * <b>Created on:</b> <i>11:55:59 PM Jul 22, 2015</i>
     */
    private final boolean ignoreCase;
    
    
    /**
     * Validator for name. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>12:20:50 AM Jul 23, 2015</i>
     *
     * @param name
     *            name to be validated
     * @return true if name is valid
     * @throws IllegalArgumentException
     *             if name is valid
     */
    private static final boolean validateName(final String name) {
        return Validators.nonNull(name);
    }
    
    /**
     * Validator for prefix. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>12:28:00 AM Jul 23, 2015</i>
     *
     * @param prefix
     *            prefix to be validated
     * @return true if prefix is valid
     * @throws IllegalArgumentException
     *             if prefix is invalid
     */
    private static final boolean validatePrefix(final String prefix) {
        return Validators.nonEmpty(prefix);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.StartsWithFilter.<br/>
     * <br/>
     * <b>PRE-conditions:</b> non-null args <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> super constructor is invoked <br/>
     * <b>Created on:</b> <i>11:47:25 PM Jul 22, 2015</i>
     *
     * @param name
     *            name of the filter
     * @param prefix
     *            prefix for the filter to check starting
     * @param ignoreCase
     *            ignore case flag
     * @throws IllegalArgumentException
     *             if any of args is invalid
     */
    @SuppressWarnings("synthetic-access")
    public StartsWithFilter(final String name, final String prefix, final boolean ignoreCase) {
        super(name, new StartsWithPredicate(prefix, ignoreCase));

        validateName(name);
        validatePrefix(prefix);
        
        this.prefix = prefix;
        this.ignoreCase = ignoreCase;
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.StartsWithFilter.<br/>
     * Constructor for case sensitive starts-with filter. <br/>
     * <b>PRE-conditions:</b> non-null name, non-empty prefix <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>12:04:03 AM Jul 23, 2015</i>
     *
     * @param name
     *            name of the filter
     * @param prefix
     *            prefix for starts-with filter
     * @throws IllegalArgumentException
     *             if any of args is invalid
     */
    public StartsWithFilter(final String name, final String prefix) {
        this(name, prefix, false);
    }

    /**
     * Get "starts with" predicate string representation. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>11:28:25 PM Jul 22, 2015</i>
     *
     * @see dburyak.logmist.model.PredicateFilter#predicateToString()
     * @return string representation of "starts with" predicate
     */
    @Override
    protected String predicateToString() {
        final StringBuilder sb = (new StringBuilder("{prefix=[")).append(prefix); //$NON-NLS-1$
        sb.append("],ignoreCase=[").append(ignoreCase).append("]}"); //$NON-NLS-1$ //$NON-NLS-2$
        return sb.toString();
    }
    
}
