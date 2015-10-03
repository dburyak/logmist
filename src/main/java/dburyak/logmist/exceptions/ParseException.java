package dburyak.logmist.exceptions;


import dburyak.jtools.Validators;


/**
 * Project : logmist.<br/>
 * <br/><b>Created on:</b> <i>1:35:34 AM Oct 3, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class ParseException extends LogmistException {

    /**
     * Default serialization version ID.
     * <br/><b>Created on:</b> <i>1:35:46 AM Oct 3, 2015</i>
     */
    private static final long serialVersionUID = 1L;

    /**
     * Parser that threw this exception.
     * <br/><b>Created on:</b> <i>1:37:17 AM Oct 3, 2015</i>
     */
    private final String parser;

    /**
     * Log line where this exception occurred.
     * <br/><b>Created on:</b> <i>1:37:39 AM Oct 3, 2015</i>
     */
    private final String line;


    /**
     * Constructor for class : [logmist] dburyak.logmist.exceptions.ParseException.<br/>
     * <br/><b>PRE-conditions:</b> non-empty parser, non-null line
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:37:53 AM Oct 3, 2015</i>
     * 
     * @param parser
     *            parser that threw this exception
     * @param line
     *            line where this exception occurred
     */
    @SuppressWarnings("nls")
    public ParseException(final String parser, final String line) {
        super("parser = [" + parser + "] ; line = [" + line + "]");
        Validators.nonEmpty(parser);
        Validators.nonNull(line);
        this.parser = parser;
        this.line = line;
    }

    /**
     * Get parser that threw this exception.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-empty result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:40:57 AM Oct 3, 2015</i>
     * 
     * @return parser that threw this exception
     */
    public final String getParser() {
        return this.parser;
    }

    /**
     * Get line where this exception occurred.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:41:42 AM Oct 3, 2015</i>
     * 
     * @return line where this exception occurred
     */
    public final String getLine() {
        return this.line;
    }

}
