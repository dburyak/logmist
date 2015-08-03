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
     * Collection of sub-categories of this category.
     * <br/><b>Created on:</b> <i>4:31:25 AM Aug 3, 2015</i>
     */
    private final Category[] subCategories;

    /**
     * Indicates whether this category has sub-categories.
     * <br/><b>Created on:</b> <i>4:40:08 AM Aug 3, 2015</i>
     */
    private final boolean hasSubCategories;


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
        this.subCategories = new Category[0];
        this.hasSubCategories = false;
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
     * Indicates whether this category is composite category (contains sub-categories).
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:24:35 AM Aug 3, 2015</i>
     * 
     * @return true if this category has sub-categories
     */
    public final boolean hasSubCategories() {
        return hasSubCategories;
    }

    /**
     * Factory method. Creates a new category which is effectively current with adding given sub-category.
     * <br/><b>PRE-conditions:</b> non-null arg
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> state of this object is not changed, a brand new object is created instead
     * <br/><b>Created on:</b> <i>4:50:37 AM Aug 3, 2015</i>
     * 
     * @param category
     *            category which should be included to this
     * @return new category which contains provided category
     */
    public final Category withSubCategory(final Category category) {
        // TODO : implement
        // class is immutable, thus this method returns a brand new category
    }

    /**
     * Factory method. Crates a new category which is effectively current with removing given sub-category.
     * <br/><b>PRE-conditions:</b> non-null arg, this category already contains given one
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> state of this object is not changed, a brand new object is created instead
     * <br/><b>Created on:</b> <i>4:56:42 AM Aug 3, 2015</i>
     * 
     * @param category
     *            category which should be removed from this one
     * @return a new category which doesn't contain given category
     */
    public final Category withoutSubCategory(final Category category) {
        // TODO : implement
        // class is immutable, thus this method returns a brand new category
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
