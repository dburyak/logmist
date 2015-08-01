package dburyak.logmist.exceptions;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import dburyak.jtools.Validators;
import dburyak.logmist.model.FilterChain;
import dburyak.logmist.model.IFilter;


/**
 * Project : logmist.<br/>
 * Indicates that there is a circular reference in the filter chain {@link FilterChain}. Usually this happens when
 * FilterChain f1 contains a filter joint with complex filter (FilterChain) f2, but f2 contains a filter joint with f1.
 * <br/><b>Created on:</b> <i>1:14:06 AM Jul 31, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class CircularFilterException extends LogmistException {

    /**
     * Serialization version ID.
     * <br/><b>Created on:</b> <i>1:24:36 AM Jul 31, 2015</i>
     */
    private static final long serialVersionUID = 1L;

    /**
     * Contains the circle that caused this exception.
     * <br/><b>Created on:</b> <i>1:39:05 AM Jul 31, 2015</i>
     */
    private final Collection<IFilter> circle;


    /**
     * Constructor for class : [logmist] dburyak.logmist.exceptions.CircularFilterException.<br/>
     * <br/><b>PRE-conditions:</b> non-null arg
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> MONE
     * <br/><b>Created on:</b> <i>1:39:23 AM Jul 31, 2015</i>
     * 
     * @param circle
     *            circle of filters that caused this exception
     */
    public CircularFilterException(final Collection<IFilter> circle) {
        Validators.nonNull(circle);
        this.circle = new ArrayList<>(circle);
    }

    /**
     * Get circle of filters that caused this exception.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:38:50 AM Jul 31, 2015</i>
     * 
     * @return circle of filters that caused this exception
     */
    public final Collection<IFilter> getCircle() {
        return Collections.unmodifiableCollection(circle);
    }


}
