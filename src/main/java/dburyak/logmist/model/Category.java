package dburyak.logmist.model;


import java.io.Serializable;

import dburyak.jtools.Validators;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;


/**
 * Project : logmist.<br/>
 * Represents log category. It can be a simple category. Or it can be a composite, i.e. be an aggregate of multiple
 * sub-categories. This relationship is stored in this class. <br/>
 * <b>Created on:</b> <i>11:18:24 PM Jul 21, 2015</i>
 *
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
@ThreadSafe
@javax.annotation.concurrent.Immutable
@javax.annotation.concurrent.ThreadSafe
public final class Category implements Serializable {

    // TODO : currently implementing ....

    /**
     * Serial version ID. <br/>
     * <b>Created on:</b> <i>1:46:04 AM Jul 22, 2015</i>
     */
    private static final long serialVersionUID = 1L;

    /**
     * Name of the category. <br/>
     * <b>Created on:</b> <i>1:42:45 AM Jul 22, 2015</i>
     */
    private final String name;

    /**
     * Filter for the category. <br/>
     * <b>Created on:</b> <i>1:42:58 AM Jul 22, 2015</i>
     */
    private final IFilter filter;


    /**
     * Validator for name field. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>1:30:20 AM Jul 22, 2015</i>
     *
     * @param name
     *            name to be validated
     * @return true if name is valid, false otherwise
     * @throws IllegalArgumentException
     *             if name is invalid
     */
    private static final boolean validateName(final String name) {
        Validators.nonNull(name);
        return true;
    }

    /**
     * Validator for filter field. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>1:32:27 AM Jul 22, 2015</i>
     *
     * @param filter
     *            filter to be validated
     * @return true if filter is valid, false otherwise
     * @throws IllegalArgumentException
     *             if filter is invalid
     */
    private static final boolean validateFilter(final IFilter filter) {
        Validators.nonNull(filter);
        return true;
    }


    /**
     * Constructor for class : [logmist] dburyak.logmist.model.Category.<br/>
     * <br/>
     * <b>PRE-conditions:</b> non-null arguments <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>1:33:45 AM Jul 22, 2015</i>
     *
     * @param name
     *            name of the category
     * @param filter
     *            filter for the category
     */
    public Category(final String name, final IFilter filter) {
        validateName(name);
        validateFilter(filter);

        this.name = name;
        this.filter = filter;
    }

    /**
     * Get name of the category. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>11:59:37 PM Jul 21, 2015</i>
     *
     * @return name of category
     */
    public final String getName() {
        return name;
    }

    /**
     * Get filter for the category. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> UNDEFINED <br/>
     * <b>Created on:</b> <i>12:10:51 AM Jul 22, 2015</i>
     *
     * @return filter of category
     */
    public final IFilter getFilter() {
        return filter;
    }

    /**
     * Get string representation of this category. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>10:19:17 PM Jul 22, 2015</i>
     *
     * @see java.lang.Object#toString()
     * @return string representation of this category
     */
    @Override
    public String toString() {
        final StringBuilder sb = (new StringBuilder("{name=[")).append(name); //$NON-NLS-1$
        sb.append("],filter=[").append(filter).append("]}"); //$NON-NLS-1$ //$NON-NLS-2$
        return sb.toString();
    }

}
