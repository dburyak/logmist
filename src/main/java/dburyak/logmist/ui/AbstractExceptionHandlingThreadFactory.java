package dburyak.logmist.ui;


import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.ThreadFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.jtools.AssertConst;


/**
 * Project : logmist.<br/>
 * Designed for inheritance.<br/>
 * This thread factory implementation is just a decorator, which delegates all the thread creation task to the delegate
 * thread factory which is decorated by this one. Additional functionality of this class is all about setting the
 * uncaught exception handler for created threads. All the specific case error handling should be implemented in
 * subclasses.
 * <br/><b>Created on:</b> <i>1:49:13 PM Oct 31, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public abstract class AbstractExceptionHandlingThreadFactory implements ThreadFactory {

    /**
     * Default system logger for this class.
     * <br/><b>Created on:</b> <i>1:51:44 PM Oct 31, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(AbstractExceptionHandlingThreadFactory.class);

    /**
     * Suffix to be appended to thread name.
     * <br/><b>Created on:</b> <i>2:18:40 PM Oct 31, 2015</i>
     */
    private static final String THREAD_NAME_SUFFIX = "-EX"; //$NON-NLS-1$

    /**
     * Thread factory which should create real threads
     * <br/><b>Created on:</b> <i>1:52:44 PM Oct 31, 2015</i>
     */
    private final ThreadFactory delegateFactory;

    /**
     * Indicates whether exception should be also handled by previous non-null exception handler.
     * <br/><b>Created on:</b> <i>2:32:45 PM Oct 31, 2015</i>
     */
    private final boolean passToPrevHandler;


    /**
     * Project : logmist.<br/>
     * Uncaught exception handler that calls one of the three implementation-specific methods :
     * 
     * <pre>
     * {@link AbstractExceptionHandlingThreadFactory#doHandleError(Error)},
     * {@link AbstractExceptionHandlingThreadFactory#doHandleRuntimeException(RuntimeException)},
     * {@link AbstractExceptionHandlingThreadFactory#doHandleException(Exception)}.
     * </pre>
     * 
     * Also if provided, calls chained exception handler. This is very useful when existing thread already has some
     * exception handler, so instead of replacing that handler, this implementation just appends (chains) it's existing
     * functionality.
     * <br/><b>Created on:</b> <i>8:22:40 PM Oct 31, 2015</i>
     * 
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     */
    private static final class TemplateExceptionChainHandler implements UncaughtExceptionHandler {

        /**
         * Default system logger for this class.
         * <br/><b>Created on:</b> <i>8:21:40 PM Oct 31, 2015</i>
         */
        @SuppressWarnings("hiding")
        private static final Logger LOG = LogManager.getFormatterLogger(TemplateExceptionChainHandler.class);

        /**
         * Implementation that defines handlers for each of three types of exceptions.
         * <br/><b>Created on:</b> <i>8:38:25 PM Oct 31, 2015</i>
         */
        private final AbstractExceptionHandlingThreadFactory implementation;

        /**
         * {@link UncaughtExceptionHandler} to be called after specific handler of the
         * {@link TemplateExceptionChainHandler#implementation}. Gets called ONLY if
         * {@link AbstractExceptionHandlingThreadFactory#passToPrevHandler} flag of the implementation is set to true.
         * <br/><b>Created on:</b> <i>8:52:03 PM Oct 31, 2015</i>
         */
        private final UncaughtExceptionHandler chainedHandler;


        /**
         * Constructor for class : [logmist] dburyak.logmist.ui.TemplateExceptionChainHandler.<br/>
         * <br/><b>PRE-conditions:</b> non-null implementation, non-null chainedHandler is NOT instanceof
         * {@link AbstractExceptionHandlingThreadFactory}, null chainedHandler can be only used with
         * (implementation.passToPrevHandler == false)
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>8:39:04 PM Oct 31, 2015</i>
         * 
         * @param implementation
         *            concrete implementation of the three handlers for each type of exception
         * @param chainedHandler
         *            handler to be called after the specific handler method of the implementation
         */
        @SuppressWarnings({ "synthetic-access", "nls" })
        public TemplateExceptionChainHandler(
            final AbstractExceptionHandlingThreadFactory implementation,
            final UncaughtExceptionHandler chainedHandler) {

            // FIXME : use explicit checks instead of assertions
            assert (implementation != null) : AssertConst.ASRT_NULL_ARG;
            // if chaining is needed, then chainedHandler cannot be instanceof AbstractExceptionHandlingThreadFactory
            // otherwise infinite recursion is possible
            // assert ((implementation.passToPrevHandler
            // && !(chainedHandler instanceof AbstractExceptionHandlingThreadFactory))
            // ||
            // !implementation.passToPrevHandler) : AssertConst.ASRT_INVALID_ARG;

            if (implementation.passToPrevHandler) {
                LOG.debug("thread exception handler with chaining created : implementationClass = [%s] ; "
                    + "chainedHandler = [%s]", implementation.getClass().getSimpleName(), chainedHandler);
            } else {
                LOG.debug("thread exception handler without chaining created : implementationClass = [%s]",
                    implementation.getClass().getSimpleName());
            }

            this.implementation = implementation;
            this.chainedHandler = chainedHandler;
        }

        /**
         * TEMPLATE METHOD<br/>
         * Handle uncaught exception. Exception gets categorized into three types : {@link Error},
         * {@link RuntimeException} and checked {@link Exception}. Specific handler is called for corresponding type of
         * exception.
         * <br/><b>PRE-conditions:</b> non-null thread and ex
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> abstract methods are called (implementation-specific), also alien method of
         * previous uncaught exception handler is called.
         * <br/><b>Created on:</b> <i>8:28:09 PM Oct 31, 2015</i>
         * 
         * @see java.lang.Thread.UncaughtExceptionHandler#uncaughtException(java.lang.Thread, java.lang.Throwable)
         * @param thread
         *            thread that threw the exception
         * @param ex
         *            exception that has been thrown by the thread
         */
        @SuppressWarnings("synthetic-access")
        @Override
        public final void uncaughtException(final Thread thread, final Throwable ex) {
            assert (thread != null) : AssertConst.ASRT_NULL_ARG;
            assert (ex != null) : AssertConst.ASRT_NULL_ARG;

            if (ex instanceof Error) {
                implementation.doHandleError((Error) ex); // safe cast
            } else if (ex instanceof RuntimeException) {
                implementation.doHandleRuntimeException((RuntimeException) ex); // safe cast
            } else if (ex instanceof Exception) {
                implementation.doHandleException((Exception) ex);
            } else { // should never happen
                // TODO : maybe there is a better way to handle this situation
                // assertion violation, no sense to throw assertion error (it will be swallowed anyway)
                LOG.error("unexpected type of exception caught (this is impossible)", ex); //$NON-NLS-1$
                System.out.println("Assertion error!!!"); //$NON-NLS-1$
                ex.printStackTrace();
            }

            if (implementation.passToPrevHandler && !(chainedHandler instanceof TemplateExceptionChainHandler)) {
                // call previous handler (uncaught handler chain call)
                chainedHandler.uncaughtException(thread, ex);
            }
        }

    }


    /**
     * Constructor for class : [logmist] dburyak.logmist.ui.AbstractExceptionHandlingThreadFactory.<br/>
     * <br/><b>PRE-conditions:</b> non-null delegateFactory
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:59:23 PM Oct 31, 2015</i>
     * 
     * @param delegateFactory
     *            thread factory that should be decorated by this exception handling factory
     * @param passToPrevHandler
     *            whether exception should be also handled by previous non-null exception handler
     */
    public AbstractExceptionHandlingThreadFactory(
        final ThreadFactory delegateFactory,
        final boolean passToPrevHandler) {

        this.delegateFactory = delegateFactory;
        this.passToPrevHandler = passToPrevHandler;
    }

    /**
     * Get a new thread. Actual thread creation is delegated to underlying thread factory.
     * <br/><b>PRE-conditions:</b> non-null r
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> exception handling code for the thread is changed
     * <br/><b>Created on:</b> <i>1:49:14 PM Oct 31, 2015</i>
     * 
     * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
     * @param r
     *            task to be executed
     * @return thread that has specific uncaught exception handler
     */
    @SuppressWarnings("nls")
    @Override
    public final Thread newThread(final Runnable r) {
        assert (r != null) : AssertConst.ASRT_NULL_ARG;

        final Thread newThread = delegateFactory.newThread(r);
        LOG.debug("original thread to be decorated : newThread = [%s]", newThread);

        // set exception handler
        final UncaughtExceptionHandler prevHandler = newThread.getUncaughtExceptionHandler();
        newThread.setUncaughtExceptionHandler(new TemplateExceptionChainHandler(this, prevHandler));

        // append suffix to thread name
        final String prevName = newThread.getName();
        if (!prevName.endsWith(THREAD_NAME_SUFFIX)) {
            newThread.setName(prevName + THREAD_NAME_SUFFIX);
        } else {
            // very suspicious situation
            LOG.warn("thread already has -EX suffix in its name");
        }

        assert (newThread != null) : AssertConst.ASRT_NULL_RESULT;
        return newThread;
    }

    /**
     * TEMPLATE METHOD<br/>
     * Handle {@link Error}.
     * <br/><b>PRE-conditions:</b> non-null ex
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>2:16:18 PM Oct 31, 2015</i>
     * 
     * @param ex
     *            exception to be handled
     */
    public abstract void doHandleError(final Error ex);

    /**
     * TEMPLATE METHOD<br/>
     * Handle {@link RuntimeException}.
     * <br/><b>PRE-conditions:</b> non-null ex
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>9:41:04 PM Oct 31, 2015</i>
     * 
     * @param ex
     *            exception to be handled
     */
    public abstract void doHandleRuntimeException(final RuntimeException ex);

    /**
     * TEMPLATE METHOD<br/
     * Handle checked {@link Exception}. Technically, <b>this is impossible to happen in the code that was compiled in a
     * "legal" way.
     * <br/><b>PRE-conditions:</b> non-null ex
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> UNKNOWN
     * <br/><b>Created on:</b> <i>9:41:27 PM Oct 31, 2015</i>
     * 
     * @param ex
     *            exception to be handled
     */
    public abstract void doHandleException(final Exception ex);

}
