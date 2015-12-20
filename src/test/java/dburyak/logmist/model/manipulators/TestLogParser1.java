package dburyak.logmist.model.manipulators;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dburyak.logmist.exceptions.InaccessibleFileException;
import dburyak.logmist.exceptions.ParseException;
import dburyak.logmist.model.LogEntry;
import dburyak.logmist.model.parsers.LogParser1;


/**
 * Project : logmist.<br/>
 * <br/><b>Created on:</b> <i>11:51:52 AM Sep 10, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public class TestLogParser1 {

    /**
     * Path to log file for this test.
     * <br/><b>Created on:</b> <i>9:16:25 PM Sep 16, 2015</i>
     */
    private static Path FILE_LOGS;

    /**
     * Path to non-existent file.
     * <br/><b>Created on:</b> <i>9:16:42 PM Sep 16, 2015</i>
     */
    private static Path FILE_NONEXISTENT;

    /**
     * Path to directory.
     * <br/><b>Created on:</b> <i>9:16:51 PM Sep 16, 2015</i>
     */
    private static Path FILE_DIR;

    /**
     * Expected logs this parser parses from test log file.
     * <br/><b>Created on:</b> <i>9:17:02 PM Sep 16, 2015</i>
     */
    private static LogEntry[] LOG_EXPECTED;

    /**
     * Parser to be tested.
     * <br/><b>Created on:</b> <i>9:17:25 PM Sep 16, 2015</i>
     */
    private LogParser1 parser;


    /**
     * One time initialization of this test case.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:51:52 AM Sep 10, 2015</i>
     */
    @SuppressWarnings("nls")
    @BeforeClass
    public static void setUpBeforeClass() {
        FILE_LOGS = Paths.get("src", "test", "resources", "TestLogParser1_LOGS.log");
        FILE_NONEXISTENT = Paths.get("abracadabra");
        FILE_DIR = Paths.get("src", "test", "java");

        final int year = LogParser1.getDefaultYear();

        LOG_EXPECTED = new LogEntry[] {
            // Nov 12 23:22:47 message1
            new LogEntry(LocalDateTime.of(year, Month.NOVEMBER, 12, 23, 22, 47),
                "message1",
                "Nov 12 23:22:47   message1",
                1),

            // Nov 12 23:22:48 message2
            new LogEntry(LocalDateTime.of(year, Month.NOVEMBER, 12, 23, 22, 48),
                "message2",
                "Nov 12 23:22:48   message2",
                2),

            // Nov 12 23:25:03 message3
            new LogEntry(LocalDateTime.of(year, Month.NOVEMBER, 12, 23, 25, 3),
                "message3",
                "Nov 12 23:25:03   message3",
                3),

            // Jan 1 00:02:11 message4
            new LogEntry(LocalDateTime.of(year, Month.JANUARY, 1, 0, 2, 11),
                "message4",
                "Jan 1 00:02:11    message4",
                4),

            // Feb 5 18:09:09 syslog: message5
            new LogEntry(LocalDateTime.of(year, Month.FEBRUARY, 5, 18, 9, 9),
                "syslog: message5",
                "Feb 5 18:09:09 syslog: message5",
                5),

            // Mar 25 22:51:21 message6
            new LogEntry(LocalDateTime.of(year, Month.MARCH, 25, 22, 51, 21),
                "message6",
                "Mar 25 22:51:21   message6",
                6),

            // Apr 1 14:49:51 message7
            new LogEntry(LocalDateTime.of(year, Month.APRIL, 1, 14, 49, 51), "message7", "Apr 1 14:49:51  message7", 7),

            // May 2 13:41:06 message8
            new LogEntry(LocalDateTime.of(year, Month.MAY, 2, 13, 41, 6), "message8", "May 2 13:41:06    message8", 8),

            // Jun 21 14:46:34 FATAL message9
            new LogEntry(LocalDateTime.of(year, Month.JUNE, 21, 14, 46, 34),
                "FATAL message9",
                "Jun 21 14:46:34  FATAL message9",
                9),

            // Jul 11 15:01:17 [component1] DEBUG - message10
            new LogEntry(LocalDateTime.of(year, Month.JULY, 11, 15, 1, 17),
                "[component1] DEBUG - message10",
                "Jul 11 15:01:17   [component1] DEBUG - message10",
                10),

            // Aug 21 19:09:59 INFO message11
            new LogEntry(LocalDateTime.of(year, Month.AUGUST, 21, 19, 9, 59),
                "INFO message11",
                "Aug 21 19:09:59  INFO message11",
                11),

            // Sep 4 17:07:52 comp2[2906]: [MyClass] - message12
            new LogEntry(LocalDateTime.of(year, Month.SEPTEMBER, 4, 17, 7, 52),
                "comp2[2906]: [MyClass] - message12",
                "Sep 4 17:07:52    comp2[2906]: [MyClass] - message12",
                12),

            // Oct 13 13:09:45 message13
            new LogEntry(LocalDateTime.of(year, Month.OCTOBER, 13, 13, 9, 45),
                "message13",
                "Oct 13 13:09:45   message13",
                13),

            // Dec 30 13:16:04 comp3[1802]: message14
            new LogEntry(LocalDateTime.of(year, Month.DECEMBER, 30, 13, 16, 4),
                "comp3[1802]: message14",
                "Dec 30 13:16:04   comp3[1802]: message14",
                14),
        };
    }

    /**
     * One time clean-up after this test case.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:51:52 AM Sep 10, 2015</i>
     */
    @AfterClass
    public static void tearDownAfterClass() {
        FILE_LOGS = null;
        FILE_NONEXISTENT = null;
        FILE_DIR = null;
        LOG_EXPECTED = null;
    }

    /**
     * Initialization to be executed before each test of this test case.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:51:52 AM Sep 10, 2015</i>
     */
    @Before
    public void setUp() {
        parser = new LogParser1();
    }

    /**
     * Clean-up to be executed after each test of this test case.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:51:53 AM Sep 10, 2015</i>
     */
    @After
    public void tearDown() {
        parser = null;
    }

    /**
     * Test method for {@link LogParser1#LogParser1()}. Valid parameters.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>9:51:06 PM Sep 16, 2015</i>
     */
    @Test
    public final void testLogParser1() {
        parser = new LogParser1();
        Assert.assertNotNull(parser);
    }

    /**
     * Test method for {@link LogParser1#LogParser1(int)}. Valid params.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>9:53:05 PM Sep 16, 2015</i>
     */
    @Test
    public final void testLogParser1Int() {
        parser = new LogParser1(2007);
        Assert.assertNotNull(parser);
    }

    /**
     * Test method for {@link LogParser1#LogParser1(int)}. Invalid arg 1 - negative.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>10:15:11 PM Sep 16, 2015</i>
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testLogParser1IntInvalid() {
        parser = new LogParser1(-200);
        Assert.fail();
    }

    /**
     * Test method for {@link dburyak.logmist.model.parsers.LogParser1#canParse(java.nio.file.Path)}.
     * 
     * @throws InaccessibleFileException
     *             is not expected to be thrown for this test
     */
    @Test
    public final void testCanParse() throws InaccessibleFileException {
        Assert.assertTrue(parser.canParse(FILE_LOGS));
    }

    /**
     * Test method for {@link LogParser1#canParse(Path)}. Invalid arg 1 - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:47:01 AM Aug 27, 2015</i>
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testCanParseInvalid1() {
        try {
            parser.canParse(null);
            Assert.fail();
        } catch (@SuppressWarnings("unused") final InaccessibleFileException ex) {
            Assert.fail();
        }
    }

    /**
     * Test method for {@link LogParser1#canParse(Path)}. Invalid arg 1 - non-existent file.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:47:01 AM Aug 27, 2015</i>
     * 
     * @throws InaccessibleFileException
     *             should be thrown for non-existent file
     */
    @Test(expected = InaccessibleFileException.class)
    public final void testCanParseInvalid1_v2() throws InaccessibleFileException {
        assert (!Files.exists(FILE_NONEXISTENT));
        @SuppressWarnings("unused") final boolean canParse = parser.canParse(FILE_NONEXISTENT);
        Assert.fail();
    }

    /**
     * Test method for {@link LogParser1#canParse(Path)}. Invalid arg 1 - directory.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:47:01 AM Aug 27, 2015</i>
     * 
     * @throws InaccessibleFileException
     *             should be thrown for directory
     */
    @Test(expected = InaccessibleFileException.class)
    public final void testCanParseInvalid1_v3() throws InaccessibleFileException {
        assert (Files.isDirectory(FILE_DIR));
        @SuppressWarnings("unused") final boolean canParse = parser.canParse(FILE_DIR);
        Assert.fail();
    }

    /**
     * Test method for {@link dburyak.logmist.model.parsers.LogParser1#parse(java.nio.file.Path)}.
     */
    @Test
    public final void testParse() {
        try {
            assert (parser.canParse(FILE_LOGS));
        } catch (@SuppressWarnings("unused") final InaccessibleFileException e) {
            Assert.fail();
        }

        try {
            final Collection<LogEntry> logsActual = parser.parse(FILE_LOGS);
            assert (LOG_EXPECTED != null);
            assert (logsActual.size() == LOG_EXPECTED.length);
            Assert.assertArrayEquals(LOG_EXPECTED, logsActual.toArray(new LogEntry[logsActual.size()]));
        } catch (@SuppressWarnings("unused") final InaccessibleFileException ex) {
            Assert.fail();
        } catch (@SuppressWarnings("unused") final ParseException ex) {
            Assert.fail();
        }
    }

    /**
     * Test method for {@link LogParser1#parse(java.nio.file.Path)}. Invalid arg 1 - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:37:32 AM Aug 22, 2015</i>
     * 
     * @throws InaccessibleFileException
     *             if file cannot be accessed
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testParseInvalid1() throws InaccessibleFileException {
        try {
            parser.parse(null);
        } catch (@SuppressWarnings("unused") final ParseException ex) {
            Assert.fail();
        }
        Assert.fail();
    }

    /**
     * Test method for {@link LogParser1#parse(java.nio.file.Path)}. Invalid arg 1 - non-existent file.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:37:32 AM Aug 22, 2015</i>
     * 
     * @throws InaccessibleFileException
     *             if file cannot be accessed
     */
    @Test(expected = InaccessibleFileException.class)
    public final void testParseInvalid1_v2() throws InaccessibleFileException {
        assert (!Files.exists(FILE_NONEXISTENT));

        try {
            parser.parse(FILE_NONEXISTENT); // expected to throw InaccessibleFileException
        } catch (@SuppressWarnings("unused") final ParseException ex) {
            Assert.fail();
        }
        Assert.fail();
    }

    /**
     * Test method for {@link LogParser1#parse(java.nio.file.Path)}. Invalid arg 1 - directory.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:37:32 AM Aug 22, 2015</i>
     * 
     * @throws InaccessibleFileException
     *             if file cannot be accessed
     */
    @Test(expected = InaccessibleFileException.class)
    public final void testParseInvalid1_v3() throws InaccessibleFileException {
        assert (Files.isDirectory(FILE_DIR));

        try {
            parser.parse(FILE_DIR); // expected to throw InaccessibleFileException
        } catch (@SuppressWarnings("unused") final ParseException ex) {
            Assert.fail();
        }
        Assert.fail();
    }

    /**
     * Test method for {@link java.lang.Object#toString()}.
     */
    @Test
    public final void testToString() {
        Assert.assertEquals(LogParser1.class.getSimpleName(), parser.toString());
    }

}
