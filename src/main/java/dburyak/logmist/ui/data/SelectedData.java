package dburyak.logmist.ui.data;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Project : logmist.<br/>
 * Holds value for "Selected : " UI element.
 * <br/><b>Created on:</b> <i>2:29:43 AM Nov 6, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class SelectedData extends UIData<Long> {

    /**
     * Default system logger for this class.
     * <br/><b>Created on:</b> <i>3:14:11 AM Nov 6, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(SelectedData.class);

    /**
     * Value of "Selected".
     * <br/><b>Created on:</b> <i>3:23:07 AM Nov 6, 2015</i>
     */
    private long value;


    /**
     * TEMPLATE METHOD<br/>
     * Update internal data with new value.
     * <br/><b>PRE-conditions:</b> non-null newValue, newValue >= 0
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> new value is set
     * <br/><b>Created on:</b> <i>3:14:23 AM Nov 6, 2015</i>
     * 
     * @see dburyak.logmist.ui.data.UIData#doUpdateData(java.lang.Object)
     * @param newValue
     *            new "Selected" value
     */
    @SuppressWarnings("boxing")
    @Override
    protected final void doUpdateData(final Long newValue) {
        LOG.trace("Selected UI data updated : prevValue = [%d] ; newValue = [%d]", value, newValue); //$NON-NLS-1$
        value = newValue;
    }

    /**
     * TEMPLATE METHOD<br/>
     * Get internal data. Usually called by UI to update its state.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> (result >= 0)
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:23:30 AM Nov 6, 2015</i>
     * 
     * @see dburyak.logmist.ui.data.UIData#doGetData()
     * @return value stored in this data model
     */
    @SuppressWarnings("boxing")
    @Override
    protected final Long doGetData() {
        LOG.trace("Selected UI data retrieved : value = [%d]", value); //$NON-NLS-1$
        return value;
    }

}
