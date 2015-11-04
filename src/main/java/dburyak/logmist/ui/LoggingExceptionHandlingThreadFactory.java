package dburyak.logmist.ui;


import java.util.concurrent.ThreadFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Project : logmist.<br/>
 * Designed for inheritance.<br/>
 * Implementation that just prints to log stacktrace for each type of exception. Can be used as a base class for
 * overriding only handlers for specific types of exceptions.
 * <br/><b>Created on:</b> <i>9:44:42 PM Oct 31, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public class LoggingExceptionHandlingThreadFactory extends AbstractExceptionHandlingThreadFactory {

    /**
     * Default system logger.
     * <br/><b>Created on:</b> <i>10:27:56 PM Oct 31, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(LoggingExceptionHandlingThreadFactory.class);


    /**
     * Constructor for class : [logmist] dburyak.logmist.ui.LoggingExceptionHandlingThreadFactory.<br/>
     * <br/><b>PRE-conditions:</b> non-null delegateFactory
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>9:46:18 PM Oct 31, 2015</i>
     * 
     * @param delegateFactory
     *            thread factory that should be decorated by this logging decorator
     */
    public LoggingExceptionHandlingThreadFactory(final ThreadFactory delegateFactory) {
        super(delegateFactory, true);
        LOG.debug("created with delegate factory : delegateFactory = [%s]", delegateFactory); //$NON-NLS-1$
    }

    /**
     * Print stacktrace to system log.
     * <br/><b>PRE-conditions:</b> non-null ex
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> message is printed to log
     * <br/><b>Created on:</b> <i>9:44:43 PM Oct 31, 2015</i>
     * 
     * @see dburyak.logmist.ui.AbstractExceptionHandlingThreadFactory#doHandleError(java.lang.Error)
     * @param ex
     *            caught error
     */
    @Override
    public void doHandleError(final Error ex) {
        LOG.error("unhandled error caught : ex = ...", ex); //$NON-NLS-1$
    }

    /**
     * Print stacktrace to system log.
     * <br/><b>PRE-conditions:</b> non-null ex
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>9:44:43 PM Oct 31, 2015</i>
     * 
     * @see dburyak.logmist.ui.AbstractExceptionHandlingThreadFactory#doHandleRuntimeException(java.lang.RuntimeException)
     * @param ex
     *            caught runtime exception
     */
    @Override
    public void doHandleRuntimeException(final RuntimeException ex) {
        LOG.error("unhandled runtime exception caught : ex = ...", ex); //$NON-NLS-1$
    }

    /**
     * Print stacktrace to system log.
     * <br/><b>PRE-conditions:</b> non-null ex
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>9:44:43 PM Oct 31, 2015</i>
     * 
     * @see dburyak.logmist.ui.AbstractExceptionHandlingThreadFactory#doHandleException(java.lang.Exception)
     * @param ex
     *            caught checked exception
     */
    @Override
    public void doHandleException(final Exception ex) {
        // technically, this should never happen on correctly compiled code...
        LOG.error("unhandled checked exception caught : ex = ...", ex); //$NON-NLS-1$
    }

}
