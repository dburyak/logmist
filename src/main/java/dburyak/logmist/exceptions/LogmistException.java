package dburyak.logmist.exceptions;


import dburyak.jtools.Validators;


/**
 * Project : logmist.<br/>
 * Base exception for this project. All specific exceptions should extend this one.
 * <br/><b>Created on:</b> <i>1:05:40 AM Jul 31, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public class LogmistException extends Exception {

    /**
     * Serialization version ID.
     * <br/><b>Created on:</b> <i>1:08:56 AM Jul 31, 2015</i>
     */
    private static final long serialVersionUID = 1L;


    /**
     * Constructor for class : [logmist] dburyak.logmist.exceptions.LogmistException.<br/>
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:05:41 AM Jul 31, 2015</i>
     */
    public LogmistException() {
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.exceptions.LogmistException.<br/>
     * <br/><b>PRE-conditions:</b> non-empty arg
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:05:41 AM Jul 31, 2015</i>
     * 
     * @param message
     *            message for this exception
     */
    public LogmistException(final String message) {
        super(message);
        Validators.nonEmpty(message);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.exceptions.LogmistException.<br/>
     * <br/><b>PRE-conditions:</b> non-null arg
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:05:41 AM Jul 31, 2015</i>
     * 
     * @param cause
     *            exception that caused this one
     */
    public LogmistException(final Throwable cause) {
        super(cause);
        Validators.nonNull(cause);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.exceptions.LogmistException.<br/>
     * <br/><b>PRE-conditions:</b> non-empty message, non-null cause
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:05:41 AM Jul 31, 2015</i>
     * 
     * @param message
     *            message for this exception
     * @param cause
     *            exception that caused this one
     */
    public LogmistException(final String message, final Throwable cause) {
        super(message, cause);
        Validators.nonEmpty(message);
        Validators.nonNull(cause);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.exceptions.LogmistException.<br/>
     * <br/><b>PRE-conditions:</b> non-empty message, non-null cause
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:05:41 AM Jul 31, 2015</i>
     * 
     * @param message
     *            message for this exception
     * @param cause
     *            exception that caused this one
     * @param enableSuppression
     *            indicates whether suppression of exceptions should be enabled
     * @param writableStackTrace
     *            indicates whether it should contain stack trace
     */
    public LogmistException(
        final String message,
        final Throwable cause,
        final boolean enableSuppression,
        final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        Validators.nonEmpty(message);
        Validators.nonNull(cause);
    }

}
