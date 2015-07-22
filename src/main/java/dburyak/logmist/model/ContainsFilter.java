package dburyak.logmist.model;


import dburyak.jtools.Validators;


/**
 * Project : logmist.<br/>
 * This filter filters out log entries by checking whether log message contains specific text. <br/>
 * <b>Created on:</b> <i>3:21:26 AM Jul 22, 2015</i>
 *
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class ContainsFilter implements IFilter {

    /**
     * Serial version ID.<br/>
     * <b>Created on:</b> <i>3:28:53 AM Jul 22, 2015</i>
     */
    private static final long serialVersionUID = 1L;


    /**
     * Name of this filter. <br/>
     * <b>Created on:</b> <i>12:17:52 PM Jul 22, 2015</i>
     */
    private final String name;

    /**
     * Substring to be checked for containment by this filter.<br/>
     * <b>Created on:</b> <i>3:32:23 AM Jul 22, 2015</i>
     */
    private final String subStr;

    /**
     * Substring to be checked for containment in low case. <br/>
     * <b>Created on:</b> <i>12:14:57 PM Jul 22, 2015</i>
     */
    private final String subStrLowcase;

    /**
     * Indicates whether case should be ignored. <br/>
     * <b>Created on:</b> <i>3:33:53 AM Jul 22, 2015</i>
     */
    private final boolean ignoreCase;


    /**
     * Validator for subStr argument. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>3:40:01 AM Jul 22, 2015</i>
     *
     * @param subStr
     *            substring to be validated
     * @return true if subStr is valid
     * @throws IllegalArgumentException
     *             if subStr is null
     */
    private static final boolean validateSubStr(final String subStr) {
        return Validators.nonNull(subStr);
    }

    /**
     * Validator for name argument.
     * <br/>
     * <b>PRE-conditions:</b> NONE
     * <br/>
     * <b>POST-conditions:</b> NONE
     * <br/>
     * <b>Side-effects:</b> NONE
     * <br/>
     * <b>Created on:</b> <i>12:18:31 PM Jul 22, 2015</i>
     *
     * @param name
     *            name to be validated
     * @return true if name is valid
     * @throws IllegalArgumentException
     *             if name is null
     */
    private static final boolean validateName(final String name) {
        return Validators.nonNull(name);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.ContainsFilter.<br/>
     * Creates contains filter with given case sensitiveness. <br/>
     * <b>PRE-conditions:</b> non-null args <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>3:38:15 AM Jul 22, 2015</i>
     *
     * @param name
     *            name of the filter
     * @param subStr
     *            substring to look for in given log entries
     * @param ignoreCase
     *            whether case should be ignored during comparison
     */
    public ContainsFilter(final String name, final String subStr, final boolean ignoreCase) {
        validateName(name);
        validateSubStr(subStr);

        this.name = name;
        this.subStr = subStr;
        this.subStrLowcase = subStr.toLowerCase();
        this.ignoreCase = ignoreCase;
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.ContainsFilter.<br/>
     * Creates case sensitive contains filter. <br/>
     * <b>PRE-conditions:</b> non-null arg <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>3:37:09 AM Jul 22, 2015</i>
     *
     * @param name
     *            name of the filter
     * @param subStr
     *            substring to look for in given log entries
     */
    public ContainsFilter(final String name, final String subStr) {
        this(name, subStr, false);
    }

    /**
     * Check if given log entry contains subString of this filter. <br/>
     * <b>PRE-conditions:</b> non-null arg <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>3:21:27 AM Jul 22, 2015</i>
     *
     * @see dburyak.logmist.model.IFilter#accept(dburyak.logmist.model.LogEntry)
     * @param log
     *            log entry to check containment
     * @return true if log contains subStr of this filter
     * @throws NullPointerException
     *             if log is null.
     */
    @Override
    public boolean accept(final LogEntry log) {
        if (!ignoreCase) {
            return log.getMsgFull().contains(subStr);
        } else { // case ignored
            return log.getMsgFull().toLowerCase().contains(subStrLowcase);
        }
    }

    /**
     * Get name of the filter. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>3:21:27 AM Jul 22, 2015</i>
     *
     * @see dburyak.logmist.model.IFilter#getName()
     * @return name of the filter
     */
    @Override
    public String getName() {
        return name;
    }

}
