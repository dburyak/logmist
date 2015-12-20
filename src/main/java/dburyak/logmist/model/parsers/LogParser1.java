package dburyak.logmist.model.parsers;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.jtools.Validators;
import dburyak.logmist.exceptions.ParseException;
import dburyak.logmist.model.LogEntry;
import net.jcip.annotations.NotThreadSafe;


// TODO : this should be a separate class that supports configuring (configuration describes log syntax)

/**
 * Project : logmist.<br/>
 * Log file parser for format:
 * 
 * <pre>
 * Nov 12 23:22:47   message1
 * Nov 12 23:22:48   message2
 * </pre>
 * 
 * <br/><b>Created on:</b> <i>10:04:08 AM Sep 4, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@NotThreadSafe
@javax.annotation.concurrent.NotThreadSafe
public final class LogParser1 extends LogFileParserBase {

    /**
     * Default system logger for this class.
     * <br/><b>Created on:</b> <i>10:17:37 AM Sep 4, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(LogParser1.class);


    /**
     * Project : logmist.<br/>
     * Months for this log format.
     * <br/><b>Created on:</b> <i>12:35:35 AM Sep 13, 2015</i>
     * 
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     */
    @SuppressWarnings("nls")
    private static enum Month {
            /**
             * January.
             * <br/><b>Created on:</b> <i>12:35:58 AM Sep 13, 2015</i>
             */
        JAN("Jan", java.time.Month.JANUARY),

            /**
             * February.
             * <br/><b>Created on:</b> <i>12:36:10 AM Sep 13, 2015</i>
             */
        FEB("Feb", java.time.Month.FEBRUARY),

            /**
             * March.
             * <br/><b>Created on:</b> <i>12:36:21 AM Sep 13, 2015</i>
             */
        MAR("Mar", java.time.Month.MARCH),

            /**
             * April.
             * <br/><b>Created on:</b> <i>12:36:31 AM Sep 13, 2015</i>
             */
        APR("Apr", java.time.Month.APRIL),

            /**
             * May.
             * <br/><b>Created on:</b> <i>12:36:40 AM Sep 13, 2015</i>
             */
        MAY("May", java.time.Month.MAY),

            /**
             * June.
             * <br/><b>Created on:</b> <i>12:36:48 AM Sep 13, 2015</i>
             */
        JUN("Jun", java.time.Month.JUNE),

            /**
             * July.
             * <br/><b>Created on:</b> <i>12:36:55 AM Sep 13, 2015</i>
             */
        JUL("Jul", java.time.Month.JULY),

            /**
             * August.
             * <br/><b>Created on:</b> <i>12:37:03 AM Sep 13, 2015</i>
             */
        AUG("Aug", java.time.Month.AUGUST),

            /**
             * September.
             * <br/><b>Created on:</b> <i>12:37:11 AM Sep 13, 2015</i>
             */
        SEP("Sep", java.time.Month.SEPTEMBER),

            /**
             * October.
             * <br/><b>Created on:</b> <i>12:37:20 AM Sep 13, 2015</i>
             */
        OCT("Oct", java.time.Month.OCTOBER),

            /**
             * November.
             * <br/><b>Created on:</b> <i>12:37:28 AM Sep 13, 2015</i>
             */
        NOV("Nov", java.time.Month.NOVEMBER),

            /**
             * December.
             * <br/><b>Created on:</b> <i>12:37:36 AM Sep 13, 2015</i>
             */
        DEC("Dec", java.time.Month.DECEMBER);

        /**
         * Name of the month that is used in log.
         * <br/><b>Created on:</b> <i>12:37:54 AM Sep 13, 2015</i>
         */
        private final String nameStr;

        /**
         * Corresponding Java month from java.time package.
         * <br/><b>Created on:</b> <i>12:38:26 AM Sep 13, 2015</i>
         */
        private final java.time.Month monthJava;


        /**
         * Constructor for class : [logmist] dburyak.logmist.model.parsers.Month.<br/>
         * <br/><b>PRE-conditions:</b> non-null args
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>12:38:49 AM Sep 13, 2015</i>
         * 
         * @param nameStr
         *            name of the month
         * @param monthJava
         *            corresponding Java month
         */
        Month(final String nameStr, final java.time.Month monthJava) {
            this.nameStr = nameStr;
            this.monthJava = monthJava;
        }

        /**
         * Get string representation.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> non-empty result
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>12:39:32 AM Sep 13, 2015</i>
         * 
         * @see java.lang.Enum#toString()
         * @return string representation of the month
         */
        @Override
        public final String toString() {
            return nameStr;
        }

        /**
         * Get name of the month.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> non-empty result
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>12:40:05 AM Sep 13, 2015</i>
         * 
         * @return name of the month
         */
        public final String getName() {
            return nameStr;
        }

        /**
         * Get corresponding Java month {@link java.time.Month}.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>12:40:48 AM Sep 13, 2015</i>
         * 
         * @return corresponding Java month for this month
         */
        public final java.time.Month getMonthJava() {
            return monthJava;
        }
    }


    /**
     * Mapping of month's names to month enum entries. Used for parsing.
     * <br/><b>Created on:</b> <i>12:41:39 AM Sep 13, 2015</i>
     */
    private static final Map<String, Month> MONTHS = new HashMap<>();


    // initialize MONTHS map
    static {
        // cannot use parallel() here, cause MONTHS is not thread safe (can cause deadlocks)
        Arrays.stream(Month.values()).forEach(month -> MONTHS.put(month.getName(), month));
    }


    /**
     * Log format doesn't have year, but Java LocalDateTime does. So, we need to use some default value.
     * <br/><b>Created on:</b> <i>12:42:31 AM Sep 13, 2015</i>
     */
    private static final int DEFAULT_YEAR = 2000;


    /**
     * Pattern for white spaces.
     * <br/><b>Created on:</b> <i>12:51:42 AM Sep 13, 2015</i>
     */
    private static final String PATTERN_SPACE = "\\s+"; //$NON-NLS-1$

    /**
     * Group name for month.
     * <br/><b>Created on:</b> <i>12:51:54 AM Sep 13, 2015</i>
     */
    private static final String PATTERN_GROUP_NAME_MONTH = "month"; //$NON-NLS-1$

    /**
     * Group name for day of month.
     * <br/><b>Created on:</b> <i>12:52:17 AM Sep 13, 2015</i>
     */
    private static final String PATTERN_GROUP_NAME_DAY_OF_MONTH = "day"; //$NON-NLS-1$

    /**
     * Group name for time.
     * <br/><b>Created on:</b> <i>12:52:28 AM Sep 13, 2015</i>
     */
    private static final String PATTERN_GROUP_NAME_TIME = "time"; //$NON-NLS-1$

    /**
     * Group name for message.
     * <br/><b>Created on:</b> <i>12:52:42 AM Sep 13, 2015</i>
     */
    private static final String PATTERN_GROUP_NAME_MSG = "message"; //$NON-NLS-1$


    /**
     * Instance of regexp pattern for this parser.
     * <br/><b>Created on:</b> <i>3:33:56 PM Oct 3, 2015</i>
     */
    private static Pattern PATTERN = null;


    /**
     * Compose regular expression for matching the whole log line.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:53:12 AM Sep 13, 2015</i>
     * 
     * @return pattern for matching logs of this format
     */
    @SuppressWarnings("nls")
    private static final Pattern getRegexp() {
        LOG.entry();
        if (PATTERN != null) {
            return PATTERN;
        }

        final StringBuilder sb = (new StringBuilder("^(?<")).append(PATTERN_GROUP_NAME_MONTH).append(">"); //

        for (final Month month : Month.values()) {
            sb.append(month.getName()).append("|");
        }
        sb.deleteCharAt(sb.length() - 1); // delete last unnecessary "|" symbol
        sb.append(")");

        sb.append(PATTERN_SPACE);
        sb.append("(?<").append(PATTERN_GROUP_NAME_DAY_OF_MONTH).append(">\\d{1,2})"); // day of month

        sb.append(PATTERN_SPACE);
        sb.append("(?<").append(PATTERN_GROUP_NAME_TIME).append(">\\d{2}:\\d{2}:\\d{2})"); // time

        sb.append(PATTERN_SPACE);
        sb.append("(?<").append(PATTERN_GROUP_NAME_MSG).append(">.*)$");

        final String patternStr = sb.toString();
        LOG.debug("regexp pattern for parser : parser = [%s] ; pattern = [%s]",
            LogParser1.class.getSimpleName(), patternStr);
        PATTERN = Pattern.compile(patternStr);

        return LOG.exit(PATTERN);
    }


    /**
     * Group name for hour.
     * <br/><b>Created on:</b> <i>12:55:05 AM Sep 13, 2015</i>
     */
    private static final String PATTERN_GROUP_NAME_TIME_HOUR = "hour"; //$NON-NLS-1$

    /**
     * Group name for minutes.
     * <br/><b>Created on:</b> <i>12:55:30 AM Sep 13, 2015</i>
     */
    private static final String PATTERN_GROUP_NAME_TIME_MIN = "minute"; //$NON-NLS-1$

    /**
     * Group name for seconds.
     * <br/><b>Created on:</b> <i>12:55:41 AM Sep 13, 2015</i>
     */
    private static final String PATTERN_GROUP_NAME_TIME_SEC = "second"; //$NON-NLS-1$

    /**
     * Regexp pattern instance for parsing time stamps of this log format.
     * <br/><b>Created on:</b> <i>3:35:49 PM Oct 3, 2015</i>
     */
    private static Pattern PATTERN_TIME = null;


    /**
     * Compose pattern for matching time stamps.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:56:00 AM Sep 13, 2015</i>
     * 
     * @return pattern for matching time stamps from logs of this format
     */
    @SuppressWarnings("nls")
    private static final Pattern getRegexpTime() {
        LOG.entry();
        if (PATTERN_TIME != null) {
            return PATTERN_TIME;
        }

        final StringBuilder sb = (new StringBuilder("^(?<")).append(PATTERN_GROUP_NAME_TIME_HOUR).append(">\\d{2}):(?<")
            .append(PATTERN_GROUP_NAME_TIME_MIN).append(">\\d{2}):(?<")
            .append(PATTERN_GROUP_NAME_TIME_SEC).append(">\\d{2})");

        final String patternStr = sb.toString();
        LOG.debug("regexp pattern for time string : parser = [%s] ; pattern = [%s]",
            LogParser1.class.getSimpleName(), patternStr);
        PATTERN_TIME = Pattern.compile(patternStr);

        return LOG.exit(PATTERN_TIME);
    }


    /**
     * {@link DateTimeFormatter} to be used when printing out log entries.
     * <br/><b>Created on:</b> <i>5:10:25 PM Oct 3, 2015</i>
     */
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM d HH:mm:ss"); //$NON-NLS-1$


    /**
     * Get {@link DateTimeFormatter} that should be used when printing out log entries.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>5:11:14 PM Oct 3, 2015</i>
     * 
     * @return date-time formatter that should be used for printing out log entries
     */
    private static final DateTimeFormatter getTimeFormatter() {
        return TIME_FORMATTER;
    }


    /**
     * Log format of this parser does not use years. So this parser uses some default value this method returns.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:55:58 PM Sep 13, 2015</i>
     * 
     * @return year value this parser uses as default
     */
    static final int getDefaultYear() {
        return DEFAULT_YEAR;
    }


    /**
     * Validator for "defaultYear" parameter.
     * <br/><b>PRE-conditions:</b> positive defaultYear
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>10:11:23 PM Sep 16, 2015</i>
     * 
     * @param defaultYear
     *            parameter to be validated
     * @return true if year is valid
     * @throws IllegalArgumentException
     *             if default year is invalid
     */
    private static final boolean validateDefaultYear(final int defaultYear) {
        return Validators.positive(defaultYear);
    }


    /**
     * Year to be used for logs this parser produces (log format for this parser does not supports years).
     * <br/><b>Created on:</b> <i>9:21:00 PM Sep 16, 2015</i>
     */
    private final int defaultYear;


    /**
     * Constructor for class : [logmist] dburyak.logmist.model.parsers.LogParser1.<br/>
     * Use explicit default year.
     * <br/><b>PRE-conditions:</b> valid year
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>9:19:46 PM Sep 16, 2015</i>
     * 
     * @param defaultYear
     *            year to be used for logs this parser produces (log format for this parser does not supports years)
     */
    public LogParser1(final int defaultYear) {
        validateDefaultYear(defaultYear);
        this.defaultYear = defaultYear;
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.parsers.LogParser1.<br/>
     * Use implicit default year.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>9:46:08 PM Sep 16, 2015</i>
     */
    public LogParser1() {
        defaultYear = getDefaultYear();
        assert (validateDefaultYear(defaultYear));
    }

    /**
     * This log parser is time aware.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:38:48 PM Oct 3, 2015</i>
     * 
     * @see dburyak.logmist.model.parsers.ILogFileParser#isTimeAware()
     * @return true, indicating that this log parser can recognize time stamps
     */
    @Override
    public final boolean isTimeAware() {
        return true;
    }

    /**
     * Parse one line from log file.
     * <br/><b>PRE-conditions:</b> non-empty line, positive lineNum
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:30:43 PM Oct 3, 2015</i>
     * 
     * @see dburyak.logmist.model.parsers.LogFileParserBase#doParseLine(java.lang.String, long)
     * @param line
     *            line from log file to be parsed
     * @param lineNum
     *            line number of given line in log file
     * @return parsed log entry
     * @throws ParseException
     *             if given line has unexpected format
     */
    @SuppressWarnings({ "boxing", "nls" })
    @Override
    protected final LogEntry doParseLine(final String line, final long lineNum) throws ParseException {
        LOG.entry(line, lineNum);

        // validators

        final Matcher matcher = getRegexp().matcher(line);
        if (!matcher.matches()) { // line is of wrong format
            LOG.warn("line is not recognized : parser = [%s] ; lineNum = [%d] ; line = [%s]",
                toString(), lineNum, line);
            throw LOG.throwing(Level.TRACE, new ParseException(toString(), line));
        }

        // line is valid and was matched, let's extract elements
        // 1. month
        final String monthStr = matcher.group(PATTERN_GROUP_NAME_MONTH);

        final Month monthParsed = MONTHS.get(monthStr);
        if (monthParsed == null) {
            LOG.warn("cannot recognize month : line = [%s]", line);
            throw LOG.throwing(Level.TRACE, new ParseException(toString(), line));
        }
        final java.time.Month monthJava = monthParsed.getMonthJava();

        // 2. day of month
        final String dayOfMonthStr = matcher.group(PATTERN_GROUP_NAME_DAY_OF_MONTH);
        final int dayOfMonth = Integer.parseInt(dayOfMonthStr);

        // 3. time
        final String timeStr = matcher.group(PATTERN_GROUP_NAME_TIME);
        final Matcher matcherTime = getRegexpTime().matcher(timeStr);
        if (!matcherTime.matches()) {
            LOG.warn("time format not recognized : line = [%s]", line);
            throw LOG.throwing(Level.TRACE, new ParseException(toString(), line));
        }
        final int timeHour = Integer.parseInt(matcherTime.group(PATTERN_GROUP_NAME_TIME_HOUR));
        final int timeMin = Integer.parseInt(matcherTime.group(PATTERN_GROUP_NAME_TIME_MIN));
        final int timeSec = Integer.parseInt(matcherTime.group(PATTERN_GROUP_NAME_TIME_SEC));
        final LocalDateTime timeStamp = LocalDateTime.of(defaultYear, monthJava, dayOfMonth, timeHour, timeMin,
            timeSec);

        // 4. msg
        final String msgStr = matcher.group(PATTERN_GROUP_NAME_MSG);

        // construct result log entry
        final LogEntry log = new LogEntry(timeStamp, msgStr, getTimeFormatter(), lineNum);
        LOG.trace("line parsed : line = [%s] ; log = [%s]", line, log);

        return LOG.exit(log);
    }

}
