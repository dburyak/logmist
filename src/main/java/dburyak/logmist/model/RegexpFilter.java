package dburyak.logmist.model;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dburyak.jtools.Validators;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;


/**
 * Project : logmist.<br/>
 * Log filter based on regular expression matching.<br/>
 * <b>Created on:</b> <i>2:07:07 AM Jul 22, 2015</i>
 *
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
@ThreadSafe
@javax.annotation.concurrent.Immutable
@javax.annotation.concurrent.ThreadSafe
public final class RegexpFilter implements IFilter {

    /**
     * Serial version ID. <br/>
     * <b>Created on:</b> <i>2:28:15 AM Jul 22, 2015</i>
     */
    private static final long serialVersionUID = 1L;

    /**
     * Name of the filter. <br/>
     * <b>Created on:</b> <i>2:23:45 AM Jul 22, 2015</i>
     */
    private final String name;

    /**
     * Regexp pattern of the filter. <br/>
     * <b>Created on:</b> <i>2:23:53 AM Jul 22, 2015</i>
     */
    private final Pattern pattern;


    /**
     * Validator for name. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>2:24:07 AM Jul 22, 2015</i>
     *
     * @param name
     *            name to be validated
     * @return true if name is valid
     * @throws IllegalArgumentException
     *             if name is invalid
     */
    private static final boolean validateName(final String name) {
        return Validators.nonNull(name);
    }

    /**
     * Validator for pattern. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>2:24:19 AM Jul 22, 2015</i>
     *
     * @param pattern
     *            pattern to be validated
     * @return true if pattern is valid
     * @throws IllegalArgumentException
     *             if pattern is invalid
     */
    private static final boolean validatePattern(final Pattern pattern) {
        return Validators.nonNull(pattern);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.RegexpFilter.<br/>
     * Creates a case-sensitive regexp filter. <br/>
     * <b>PRE-conditions:</b> non-null args <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>2:25:30 AM Jul 22, 2015</i>
     *
     * @param name
     *            name for the filter
     * @param pattern
     *            regexp pattern for the filter
     */
    public RegexpFilter(final String name, final Pattern pattern) {
        this(name, pattern, false);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.RegexpFilter.<br/>
     * Provides "ignore case" option. <br/>
     * <b>PRE-conditions:</b> non-null args <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>3:16:18 AM Jul 22, 2015</i>
     *
     * @param name
     *            name for the filter
     * @param pattern
     *            regexp pattern for the filter
     * @param ignoreCase
     *            indicates whether case should be ignored
     */
    public RegexpFilter(final String name, final Pattern pattern, final boolean ignoreCase) {
        Pattern patternWithFlags = pattern;
        if (ignoreCase) {
            patternWithFlags = Pattern.compile(pattern.pattern(), Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        }

        validateName(name);
        validatePattern(patternWithFlags);

        this.name = name;
        this.pattern = patternWithFlags;
    }

    /**
     * Test if given log entry matches regexp pattern of this filter. <br/>
     * <b>PRE-conditions:</b> non-null arg <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> new Matcher is created on each call - this is a tradeoff for making this filter thread-safe
     * <br/>
     * <b>Created on:</b> <i>2:07:07 AM Jul 22, 2015</i>
     *
     * @see dburyak.logmist.model.IFilter#accept(dburyak.logmist.model.LogEntry)
     * @param log
     *            log entry to be matched
     * @return true if log entry matches regexp of this filter
     */
    @Override
    public final boolean accept(final LogEntry log) {
        final Matcher matcher = pattern.matcher(log.getMsgFull());
        return matcher.find();
    }

    /**
     * Get name of this filter. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>2:07:07 AM Jul 22, 2015</i>
     *
     * @see dburyak.logmist.model.IFilter#getName()
     * @return name of this filter
     */
    @Override
    public final String getName() {
        return name;
    }

}
