package dburyak.logmist.ui.data.signalers;


import dburyak.jtools.Validators;


/**
 * Project : logmist.<br/>
 * Table resizing progress event that provides information about current status of table resizing process.
 * <br/><b>Created on:</b> <i>2:51:37 AM Oct 6, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class TableResizeProgressEvent {

    /**
     * Total items in table.
     * <br/><b>Created on:</b> <i>2:54:55 AM Oct 6, 2015</i>
     */
    private final long itemsTotal;

    /**
     * Itmes arleady processed.
     * <br/><b>Created on:</b> <i>2:55:03 AM Oct 6, 2015</i>
     */
    private final long itemsProcessed;


    /**
     * Validator for "itemsTotal" parameter. Non-negative is valid.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:55:55 AM Oct 6, 2015</i>
     * 
     * @param itemsTotal
     *            totalItems parameter to be validated
     * @return ture if itemsTotal is valid
     * @throws IllegalArgumentException
     *             if itemsTotal is invalid
     */
    private static final boolean validateItemsTotal(final long itemsTotal) {
        return Validators.nonNegative(itemsTotal);
    }

    /**
     * Validator for "itemsProcessed" parameter. Non-negative is valid.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:58:35 AM Oct 6, 2015</i>
     * 
     * @param itemsProcessed
     *            itemsProcessed parameter to be validated
     * @return true if itemsProcessed is valid
     * @throws IllegalArgumentException
     *             if itemsTotal is invalid
     */
    private static final boolean validateItemsProcessed(final long itemsProcessed) {
        return Validators.nonNegative(itemsProcessed);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.ui.jfx.TableResizeProgressEvent.<br/>
     * <br/><b>PRE-conditions:</b> non-negative itemsTotal and itemsProcessed
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:00:18 AM Oct 6, 2015</i>
     * 
     * @param itemsTotal
     *            total items in table
     * @param itemsProcessed
     *            number of already processed items in table
     */
    public TableResizeProgressEvent(final long itemsTotal, final long itemsProcessed) {
        assert(validateItemsTotal(itemsTotal));
        assert(validateItemsProcessed(itemsProcessed));
        this.itemsTotal = itemsTotal;
        this.itemsProcessed = itemsProcessed;
    }

    /**
     * Getter method.<br/>
     * <br/><b>POST-conditions:</b> non-negative result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:01:46 AM Oct 6, 2015</i>
     * 
     * @return the itemsTotal
     */
    public final long getItemsTotal() {
        return itemsTotal;
    }

    /**
     * Getter method.<br/>
     * <br/><b>POST-conditions:</b> non-negative result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:01:46 AM Oct 6, 2015</i>
     * 
     * @return the itemsProcessed
     */
    public final long getItemsProcessed() {
        return itemsProcessed;
    }

    /**
     * Get string representation of this event.<br/>
     * Example:
     * 
     * <pre>
     * {itemsTotal=[1000],itemsProcessed=[35]}
     * </pre>
     * 
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-empty result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:02:33 AM Oct 6, 2015</i>
     * 
     * @see java.lang.Object#toString()
     * @return string representation of this event
     */
    @SuppressWarnings("nls")
    @Override
    public final String toString() {
        final StringBuilder sb = (new StringBuilder("{itemsTotal=[")).append(itemsTotal);
        sb.append("],itemsProcessed=[").append(itemsProcessed).append("]}");
        return sb.toString();
    }

}
