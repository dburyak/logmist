/**
 * 
 */
package dburyak.logmist.exceptions;


import dburyak.jtools.Validators;


// FIXME : code style

/**
 * @author Андрей
 */
public final class UnableToLaunchException extends LogmistException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    /**
     * @param message
     */
    public UnableToLaunchException(String message) {
        super(message);
        Validators.nonNull(message);
    }

    /**
     * @param cause
     */
    public UnableToLaunchException(Throwable cause) {
        super(cause);
        Validators.nonNull(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public UnableToLaunchException(String message, Throwable cause) {
        super(message, cause);
        Validators.nonEmpty(message);
        Validators.nonNull(cause);
    }

}
