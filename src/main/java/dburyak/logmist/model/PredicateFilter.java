package dburyak.logmist.model;


import java.util.function.Predicate;

import dburyak.jtools.Validators;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;


/**
 * Project : logmist.<br/>
 * Designed for inheritance. <br/>
 * Abstract filter which uses provided predicate for filtering. <br/>
 * <b>Created on:</b> <i>10:45:42 PM Jul 22, 2015</i>
 *
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
@ThreadSafe
@javax.annotation.concurrent.Immutable
@javax.annotation.concurrent.ThreadSafe
abstract class PredicateFilter implements IFilter {

    /**
     * Serial version ID.
     * <br/>
     * <b>Created on:</b> <i>10:53:05 PM Jul 22, 2015</i>
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Name of this filter. <br/>
     * <b>Created on:</b> <i>10:53:18 PM Jul 22, 2015</i>
     */
    private final String name;
    
    /**
     * Predicate for this filter. <br/>
     * <b>Created on:</b> <i>10:53:31 PM Jul 22, 2015</i>
     */
    private final Predicate<LogEntry> predicate;


    /**
     * Validator for name. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>10:55:37 PM Jul 22, 2015</i>
     *
     * @param name
     *            name to be validated
     * @return true if name is valid
     * @throws IllegalArgumentException
     *             if name is invalid
     */
    private static final boolean validateName(final String name) {
        Validators.nonNull(name);
        return true;
    }

    /**
     * Validator for predicate. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>10:56:20 PM Jul 22, 2015</i>
     *
     * @param predicate
     *            predicate to be validated
     * @return true if predicate is valid
     * @throws IllegalArgumentException
     *             if predicate is invalid
     */
    private static final boolean validatePredicate(final Predicate<LogEntry> predicate) {
        Validators.nonNull(predicate);
        return true;
    }

    /**
     * Validator for log arument of {@link PredicateFilter#accept(LogEntry)} method. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>11:02:16 PM Jul 22, 2015</i>
     *
     * @param log
     *            log entry to be validated
     * @return true if log entry is valid
     * @throws NullPointerException
     *             if log entry is invalid
     */
    private static final boolean validateLog(final LogEntry log) {
        if (log == null) {
            throw new NullPointerException();
        }
        return true;
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.PredicateFilter.<br/>
     * <br/>
     * <b>PRE-conditions:</b> non-null args <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>10:53:47 PM Jul 22, 2015</i>
     *
     * @param name
     *            name of the filter
     * @param predicate
     *            predicate for the filter
     */
    protected PredicateFilter(final String name, final Predicate<LogEntry> predicate) {
        validateName(name);
        validatePredicate(predicate);
        
        this.name = name;
        this.predicate = predicate;
    }
    
    /**
     * Test given log entry with predicate of this filter. <br/>
     * <b>PRE-conditions:</b> non-null arg <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>10:45:42 PM Jul 22, 2015</i>
     *
     * @see dburyak.logmist.model.IFilter#accept(dburyak.logmist.model.LogEntry)
     * @param log
     *            log entry to be tested
     * @return true if log is accepted by predicate of this filter, false otherwise
     * @throws NullPointerException
     *             if log is null
     */
    @Override
    public final boolean accept(final LogEntry log) {
        validateLog(log);
        return predicate.test(log);
    }

    /**
     * Get name of this filter. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>10:45:42 PM Jul 22, 2015</i>
     *
     * @see dburyak.logmist.model.IFilter#getName()
     * @return name of this filter
     */
    @Override
    public final String getName() {
        return name;
    }
    
    /**
     * Template method. <br/>
     * Get string representation of this predicate filter. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> alien non-final method is called - {@link PredicateFilter#predicateToString()} <br/>
     * <b>Created on:</b> <i>11:17:28 PM Jul 22, 2015</i>
     *
     * @see java.lang.Object#toString()
     * @return string representation of this filter
     */
    @Override
    public final String toString() {
        final StringBuilder sb = (new StringBuilder("{name=[")).append(name); //$NON-NLS-1$
        // TODO : check if getClass() behaves correctly for subclasses
        sb.append("],type=[").append(getClass().getSimpleName()); //$NON-NLS-1$
        sb.append("],predicate=[").append(predicateToString()).append("]}"); //$NON-NLS-1$ //$NON-NLS-2$
        return sb.toString();
    }
    
    /**
     * Designed to be implemented in subclasses. <br/>
     * Get string representation of the predicate of this filter. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> UNDEFINED <br/>
     * <b>Created on:</b> <i>11:17:15 PM Jul 22, 2015</i>
     *
     * @return string representation of the predicate of this filter
     */
    protected abstract String predicateToString();

}
