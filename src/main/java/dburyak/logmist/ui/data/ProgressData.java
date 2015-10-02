package dburyak.logmist.ui.data;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.jcip.annotations.ThreadSafe;


// TODO : code style
@ThreadSafe
@javax.annotation.concurrent.ThreadSafe
public final class ProgressData extends UIData<Double> {

    private static final Logger LOG = LogManager.getFormatterLogger(ProgressData.class);
    private static final double DEFAULT_DATA_VALUE = 0.0D;
    private static final ProgressData INSTANCE = new ProgressData();

    private double data;


    public static final ProgressData getInstance() {
        return INSTANCE;
    }

    private ProgressData() {
        data = DEFAULT_DATA_VALUE;
    }

    @Override
    protected final void doUpdateData(final Double newValue) {
        LOG.trace("UI data model updated : old = [%f] ; new = [%f]", data, newValue);
        data = newValue;
    }

    @Override
    protected Double doGetData() {
        LOG.trace("UI data model retreived : value = [%f]", data);
        return data;
    }

}
