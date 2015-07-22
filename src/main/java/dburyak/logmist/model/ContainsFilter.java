package dburyak.logmist.model;


import java.io.Serializable;
import java.util.function.Predicate;

import dburyak.jtools.AssertConst;
import dburyak.jtools.Validators;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;


/**
 * Project : logmist.<br/>
 * This filter filters out log entries by checking whether log message contains specific text. <br/>
 * <b>Created on:</b> <i>3:21:26 AM Jul 22, 2015</i>
 *
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
@ThreadSafe
@javax.annotation.concurrent.Immutable
@javax.annotation.concurrent.ThreadSafe
public final class ContainsFilter extends PredicateFilter {
    
    /**
     * Serial version ID.<br/>
     * <b>Created on:</b> <i>3:28:53 AM Jul 22, 2015</i>
     */
    private static final long serialVersionUID = 1L;
    
    
    /**
     * Project : logmist.<br/>
     * Predicate for "contains" testing. <br/>
     * <b>Created on:</b> <i>1:17:31 AM Jul 23, 2015</i>
     *
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     */
    private static final class ContainsPredicate implements Predicate<LogEntry>, Serializable {
        
        /**
         * <br/>
         * <b>Created on:</b> <i>1:18:55 AM Jul 23, 2015</i>
         */
        private static final long serialVersionUID = 1L;

        /**
         * Substring for searching containment with this predicate. <br/>
         * <b>Created on:</b> <i>1:26:03 AM Jul 23, 2015</i>
         */
        private final String subStr;

        /**
         * Substring in lower case for searching containment with ignore case flag. <br/>
         * <b>Created on:</b> <i>1:26:27 AM Jul 23, 2015</i>
         */
        private final String subStrLowerCase;

        /**
         * Indicates whether case should be ignored. <br/>
         * <b>Created on:</b> <i>1:26:57 AM Jul 23, 2015</i>
         */
        private final boolean ignoreCase;
        
        
        /**
         * Constructor for class : [logmist] dburyak.logmist.model.ContainsPredicate.<br/>
         * <br/>
         * <b>PRE-conditions:</b> non-null arg <br/>
         * <b>POST-conditions:</b> NONE <br/>
         * <b>Side-effects:</b> NONE <br/>
         * <b>Created on:</b> <i>1:25:48 AM Jul 23, 2015</i>
         *
         * @param subStr
         *            substring for searching containment with this predicate
         * @param ignoreCase
         *            indicates whether case should be ignored
         */
        @SuppressWarnings("synthetic-access")
        private ContainsPredicate(final String subStr, final boolean ignoreCase) {
            assert(ContainsFilter.validateSubStr(subStr)) : AssertConst.ASRT_INVALID_ARG;
            
            this.subStr = subStr;
            this.subStrLowerCase = subStr.toLowerCase();
            this.ignoreCase = ignoreCase;
        }


        /**
         * Check if log entry contains substring from this predicate. <br/>
         * <b>PRE-conditions:</b> non-null arg <br/>
         * <b>POST-conditions:</b> NONE <br/>
         * <b>Side-effects:</b> NONE <br/>
         * <b>Created on:</b> <i>1:28:14 AM Jul 23, 2015</i>
         *
         * @see java.util.function.Predicate#test(java.lang.Object)
         * @param t
         *            log entry to be tested by this predicate
         * @return true if log entry contains substring from this predicate
         */
        @Override
        public boolean test(final LogEntry t) {
            if (ignoreCase) {
                return t.getMsgFull().toLowerCase().contains(subStrLowerCase);
            } else {
                return t.getMsgFull().contains(subStr);
            }
        }

    }
    
    
    /**
     * Substring to be checked for containment by this filter.<br/>
     * <b>Created on:</b> <i>3:32:23 AM Jul 22, 2015</i>
     */
    private final String subStr;
    
    /**
     * Indicates whether case should be ignored. <br/>
     * <b>Created on:</b> <i>3:33:53 AM Jul 22, 2015</i>
     */
    private final boolean ignoreCase;
    
    
    /**
     * Validator for subStr argument. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>3:40:01 AM Jul 22, 2015</i>
     *
     * @param subStr
     *            substring to be validated
     * @return true if subStr is valid
     * @throws IllegalArgumentException
     *             if subStr is null
     */
    private static final boolean validateSubStr(final String subStr) {
        return Validators.nonEmpty(subStr);
    }
    
    /**
     * Constructor for class : [logmist] dburyak.logmist.model.ContainsFilter.<br/>
     * Creates contains filter with given case sensitiveness. <br/>
     * <b>PRE-conditions:</b> non-null args <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>3:38:15 AM Jul 22, 2015</i>
     *
     * @param name
     *            name of the filter
     * @param subStr
     *            substring to look for in given log entries
     * @param ignoreCase
     *            whether case should be ignored during comparison
     */
    @SuppressWarnings("synthetic-access")
    public ContainsFilter(final String name, final String subStr, final boolean ignoreCase) {
        super(name, new ContainsPredicate(subStr, ignoreCase));

        validateSubStr(subStr);
        
        this.subStr = subStr;
        this.ignoreCase = ignoreCase;
    }
    
    /**
     * Constructor for class : [logmist] dburyak.logmist.model.ContainsFilter.<br/>
     * Creates case sensitive contains filter. <br/>
     * <b>PRE-conditions:</b> non-null arg <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>3:37:09 AM Jul 22, 2015</i>
     *
     * @param name
     *            name of the filter
     * @param subStr
     *            substring to look for in given log entries
     */
    public ContainsFilter(final String name, final String subStr) {
        this(name, subStr, false);
    }
    
    /**
     * Get string representation of predicate for this filter. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>1:34:13 AM Jul 23, 2015</i>
     *
     * @see dburyak.logmist.model.PredicateFilter#predicateToString()
     * @return string representation of predicate for this filters
     */
    @Override
    protected String predicateToString() {
        final StringBuilder sb = (new StringBuilder("{subStr=[")).append(subStr); //$NON-NLS-1$
        sb.append("],ignoreCase=[").append(ignoreCase).append("]}"); //$NON-NLS-1$ //$NON-NLS-2$
        return sb.toString();
    }
    
}
