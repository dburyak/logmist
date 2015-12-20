package dburyak.logmist.exceptions;


import dburyak.jtools.Validators;


/**
 * Project : logmist.<br/>
 * Indicates that not enough data received.<br/>
 * Usual usage - for working with observables. This exception should be thrown when subscriber expects more data when
 * onCompleted is called.
 * <br/><b>Created on:</b> <i>3:38:30 AM Dec 20, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class MoreInputExpectedException extends LogmistException {

    /**
     * Serial version ID.
     * <br/><b>Created on:</b> <i>3:41:09 AM Dec 20, 2015</i>
     */
    private static final long serialVersionUID = 1L;


    /**
     * Validator for "sourceName" parameter.
     * <br/><b>PRE-conditions:</b> non-empty sourceName
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:43:36 AM Dec 20, 2015</i>
     * 
     * @param sourceName
     *            "sourceName" parameter to be validated
     * @return true if "sourceName" is valid
     * @throws IllegalArgumentException
     *             if "sourceName" is invalid
     */
    private static final boolean validateSourceName(final String sourceName) {
        return Validators.nonEmpty(sourceName);
    }

    /**
     * Validator for "cause" parameter.
     * <br/><b>PRE-conditions:</b> non-null cause
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:50:22 AM Dec 20, 2015</i>
     * 
     * @param cause
     *            "cause" parameter to be validated
     * @return true if "cause" is valid
     * @throws IllegalArgumentException
     *             if "cause" is invalid
     */
    private static final boolean validateCause(final Throwable cause) {
        return Validators.nonNull(cause);
    }


    /**
     * Constructor for class : [logmist] dburyak.logmist.exceptions.MoreInputExpectedException.<br/>
     * <br/><b>PRE-conditions:</b> non-empty sourceName
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:38:30 AM Dec 20, 2015</i>
     * 
     * @param sourceName
     *            name of the source where more data expected to be in
     */
    public MoreInputExpectedException(final String sourceName) {
        super(sourceName);
        validateSourceName(sourceName);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.exceptions.MoreInputExpectedException.<br/>
     * <br/><b>PRE-conditions:</b> non-empty sourceName, non-null cause
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:38:30 AM Dec 20, 2015</i>
     * 
     * @param sourceName
     *            name of the source where more data expected to be in
     * @param cause
     *            exception that caused this exception
     */
    public MoreInputExpectedException(final String sourceName, final Throwable cause) {
        super(sourceName, cause);
        validateSourceName(sourceName);
        validateCause(cause);
    }

}
