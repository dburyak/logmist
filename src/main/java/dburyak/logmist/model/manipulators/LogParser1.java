package dburyak.logmist.model.manipulators;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.jtools.Validators;
import dburyak.logmist.exceptions.InaccessibleFileException;
import dburyak.logmist.model.LogEntry;


// TODO : code style
/**
 * Project : logmist.<br/>
 * <br/><b>Created on:</b> <i>10:04:08 AM Sep 4, 2015</i>
 * Log file parser for format:
 * 
 * <pre>
 * Nov 12 23:22:47   message1
 * Nov 12 23:22:48   message2
 * </pre>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class LogParser1 implements ILogFileParser {

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
         * Corresponding Java standard lib month.
         * <br/><b>Created on:</b> <i>12:38:26 AM Sep 13, 2015</i>
         */
        private final java.time.Month monthJava;


        /**
         * Constructor for class : [logmist] dburyak.logmist.model.manipulators.Month.<br/>
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
     * Mapping of month's names to month enum entries.
     * <br/><b>Created on:</b> <i>12:41:39 AM Sep 13, 2015</i>
     */
    private static final Map<String, Month> MONTHS = new HashMap<>();


    // initialize MONTHS map
    static {
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

        return LOG.exit(Pattern.compile(patternStr));
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
        final StringBuilder sb = (new StringBuilder("^(?<")).append(PATTERN_GROUP_NAME_TIME_HOUR).append(">\\d{2}):(?<")
            .append(PATTERN_GROUP_NAME_TIME_MIN).append(">\\d{2}):(?<")
            .append(PATTERN_GROUP_NAME_TIME_SEC).append(">\\d{2})");

        final String patternStr = sb.toString();
        LOG.debug("regexp pattern for time string : parser = [%s] ; pattern = [%s]",
            LogParser1.class.getSimpleName(), patternStr);

        return LOG.exit(Pattern.compile(patternStr));
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

    private final Set<ILogParseEventHandler> listeners = new HashSet<>();


    /**
     * Constructor for class : [logmist] dburyak.logmist.model.manipulators.LogParser1.<br/>
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
     * Constructor for class : [logmist] dburyak.logmist.model.manipulators.LogParser1.<br/>
     * Use implicit default year.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>9:46:08 PM Sep 16, 2015</i>
     */
    public LogParser1() {
        defaultYear = getDefaultYear();
        assert(validateDefaultYear(defaultYear));
    }

    /**
     * Test if given file can be parsed by this parser (checks if format is acceptable).
     * <br/><b>PRE-conditions:</b> non-null arg
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> I/O operations are performed (check permissions, open, read, close)
     * <br/><b>Created on:</b> <i>10:04:09 AM Sep 4, 2015</i>
     * 
     * @see dburyak.logmist.model.manipulators.ILogFileParser#canParse(java.nio.file.Path)
     * @param filePath
     *            log file to be tested
     * @return true if given log file can be parsed by this parser
     */
    @SuppressWarnings({ "nls", "boxing" })
    @Override
    public final boolean canParse(final Path filePath) throws InaccessibleFileException {
        LOG.entry(filePath);
        validateFilePath(filePath);

        boolean canParse = LogFileParserUtils.isAccessibleReadable(filePath);
        if (!canParse) {
            throw LOG.throwing(Level.DEBUG, new InaccessibleFileException(filePath));
        }

        // test line of log file against pattern
        // if there's second line, test it too (just to be sure)
        try (final BufferedReader in = Files.newBufferedReader(filePath)) {
            final String line1 = in.readLine();
            if ((line1 == null) || line1.isEmpty()) {
                LOG.warn("empty file : file = [%s]", filePath);
                canParse = false;
            } else { // test first line
                final Pattern pattern = getRegexp();
                canParse = pattern.matcher(line1).matches();
                final String line2 = in.readLine();
                if (canParse && (line2 != null) && !line2.isEmpty()) { // test second line
                    canParse = pattern.matcher(line2).matches();
                }
            }
        } catch (@SuppressWarnings("unused") final IOException e) {
            LOG.error("error when reading file : file = [%s]", filePath);
        }

        return LOG.exit(canParse);
    }

    /**
     * Validator for "filePath" parameter.
     * <br/><b>PRE-conditions:</b> non-null filePath
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>10:34:27 PM Sep 16, 2015</i>
     * 
     * @param filePath
     *            parameter to be validated
     * @return true if filePath is non-null
     * @throws IllegalArgumentException
     *             if filePath is null
     */
    private static final boolean validateFilePath(final Path filePath) {
        return Validators.nonNull(filePath);
    }

    /**
     * Parse given log file into collection of {@link LogEntry} objects.
     * Syntax example:
     * 
     * <pre>
     * Nov 12 23:22:47   message1
     * Nov 12 23:22:48   message2
     * Now 12 23:25:03   message3
     * Jan 1 00:02:11    message4
     * Feb 5 18:09:09 syslog: message5
     * Mar 25 22:51:21   message6
     * Apr 1 14:49:51  message7
     * May 2 13:41:06    message8
     * Jun 21 14:46:34  FATAL message9
     * Jul 11 15:01:17   [component1] DEBUG - message10
     * Aug 21 19:09:59  INFO message11
     * Sep 4 17:07:52    comp2[2906]: [MyClass] - message12
     * Oct 13 13:09:45   message13
     * Dec 30 13:16:04   comp3[1802]: message14
     * </pre>
     * 
     * <br/><b>PRE-conditions:</b> non-null arg
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> I/O operations are performed (check permissions, open, read, close)
     * <br/><b>Created on:</b> <i>10:04:09 AM Sep 4, 2015</i>
     * 
     * @see dburyak.logmist.model.manipulators.ILogFileParser#parse(java.nio.file.Path)
     * @param filePath
     *            log file to be parsed
     * @return collection of parsed log entries
     * @throws InaccessibleFileException
     *             if file cannot be accessed
     */
    @SuppressWarnings({ "boxing", "nls" })
    @Override
    public final Collection<LogEntry> parse(final Path filePath) throws InaccessibleFileException {
        LOG.entry(filePath);
        validateFilePath(filePath);

        if (!LogFileParserUtils.isAccessibleReadable(filePath)) {
            LOG.warn("file is not accessible : file = [%s]", filePath); //$NON-NLS-1$
            throw LOG.throwing(Level.DEBUG, new InaccessibleFileException(filePath));
        }

        final Collection<LogEntry> resultLogs = new LinkedList<>();
        try {
            final Collection<String> allLines = Files.readAllLines(filePath);
            final long linesTotal = allLines.size();
            long linesRead = 0;
            notifyParseEvent(new LogParseEvent(linesTotal, linesRead)); // initialize parse status
            final Matcher matcher = getRegexp().matcher(""); //$NON-NLS-1$
            final Matcher matcherTime = getRegexpTime().matcher(""); //$NON-NLS-1$
            for (final String line : allLines) {
                linesRead++;
                matcher.reset(line);
                if (!matcher.matches()) { // line is of wrong format
                    LOG.warn("line is not recognized : filePath = [%s] ; parser = [%s] ; lineNum = [%d] ; line = [%s]",                                                  //$NON-NLS-1$
                        filePath, getClass().getSimpleName(), linesRead, line);
                    continue;
                }
                // line is valid and was matched, let's extract elements
                // month
                final String monthStr = matcher.group(PATTERN_GROUP_NAME_MONTH);

                final Month monthParsed = MONTHS.get(monthStr);
                if (monthParsed == null) {
                    LOG.warn("cannot recognize month, line ignored : filePath = [%s] ; line = [%s]",
                        filePath, line);
                    continue;
                }
                final java.time.Month monthJava = monthParsed.getMonthJava();

                // day of month
                final String dayOfMonthStr = matcher.group(PATTERN_GROUP_NAME_DAY_OF_MONTH);
                final int dayOfMonth = Integer.parseInt(dayOfMonthStr);

                // time
                final String timeStr = matcher.group(PATTERN_GROUP_NAME_TIME);
                matcherTime.reset(timeStr);
                if (!matcherTime.matches()) {
                    LOG.warn("time format not recognized, line ignored : filePath = [%s] ; line = [%s]",
                        filePath, line);
                    continue;
                }
                final int timeHour = Integer.parseInt(matcherTime.group(PATTERN_GROUP_NAME_TIME_HOUR));
                final int timeMin = Integer.parseInt(matcherTime.group(PATTERN_GROUP_NAME_TIME_MIN));
                final int timeSec = Integer.parseInt(matcherTime.group(PATTERN_GROUP_NAME_TIME_SEC));
                final LocalDateTime timeStamp = LocalDateTime.of(defaultYear, monthJava, dayOfMonth,
                    timeHour, timeMin, timeSec);

                // msg
                final String msgStr = matcher.group(PATTERN_GROUP_NAME_MSG);

                final LogEntry log = new LogEntry(timeStamp, msgStr, line);
                LOG.debug("line parsed : line = [%s] ; log = [%s]", line, log);
                resultLogs.add(log);

                // notify listeners
                notifyParseEvent(new LogParseEvent(linesTotal, linesRead));
            }
        } catch (@SuppressWarnings("unused") final IOException e) {
            LOG.error("error when reading file : file = [%s]", filePath); //$NON-NLS-1$
            throw LOG.throwing(new InaccessibleFileException(filePath));
        }

        return LOG.exit(resultLogs);
    }

    /**
     * Get string representation of this log parser.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-empty result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:57:32 AM Sep 13, 2015</i>
     * 
     * @return name of this log parser
     */
    @Override
    public final String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public final boolean isTimeAware() {
        return true;
    }

    private void notifyParseEvent(final LogParseEvent event) {
        listeners.stream().forEach(listener -> listener.handleLogParseEvent(event));
    }

    @Override
    public void addListener(final ILogParseEventHandler handler) {
        listeners.add(handler);
    }

    @Override
    public void removeListener(final ILogParseEventHandler handler) {
        assert(listeners.remove(handler));
    }
}
