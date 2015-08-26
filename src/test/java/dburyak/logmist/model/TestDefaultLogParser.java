package dburyak.logmist.model;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dburyak.logmist.exceptions.InaccessibleFileException;
import dburyak.logmist.model.manipulators.DefaultLogParser;


/**
 * Project : logmist.<br/>
 * JUnit test for {@link DefaultLogParser} class.
 * <br/><b>Created on:</b> <i>3:37:32 AM Aug 22, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public class TestDefaultLogParser {

    /**
     * Path to test resource file that contains logs for this test.
     * <br/><b>Created on:</b> <i>1:43:45 AM Aug 27, 2015</i>
     */
    private static Path FILE_LOGS;

    /**
     * Path to the non-existent file.
     * <br/><b>Created on:</b> <i>1:50:07 AM Aug 27, 2015</i>
     */
    private static Path FILE_NONEXISTENT;

    /**
     * Path to some directory.
     * <br/><b>Created on:</b> <i>1:50:17 AM Aug 27, 2015</i>
     */
    private static Path FILE_DIR;

    /**
     * Logs expected after parsing {@link TestDefaultLogParser#FILE_LOGS} file.
     * <br/><b>Created on:</b> <i>2:05:18 AM Aug 27, 2015</i>
     */
    private static LogEntry[] LOG_EXPECTED;

    /**
     * Tick duration for parser.
     * <br/><b>Created on:</b> <i>2:24:44 AM Aug 27, 2015</i>
     */
    private static Duration tickDuration;

    /**
     * Log parser.
     * <br/><b>Created on:</b> <i>3:41:33 AM Aug 22, 2015</i>
     */
    private DefaultLogParser parser;


    /**
     * One-time initialization for the whole test case.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:37:33 AM Aug 22, 2015</i>
     */
    @SuppressWarnings("nls")
    @BeforeClass
    public static void setUpBeforeClass() {
        FILE_LOGS = Paths.get("src", "test", "resources", "TestDefaultLogParser_LOGS.log");
        FILE_NONEXISTENT = Paths.get("abracadabra");
        FILE_DIR = Paths.get("src", "test", "java");
        tickDuration = Duration.ofSeconds(1L);

        LocalDateTime timeStmp = DefaultLogParser.getTimeStart();

        LOG_EXPECTED = new LogEntry[] {
            new LogEntry(timeStmp, "message1"),
            new LogEntry((timeStmp = timeStmp.plus(tickDuration)), "message2"),
            new LogEntry((timeStmp = timeStmp.plus(tickDuration)), "message3"),
            new LogEntry((timeStmp = timeStmp.plus(tickDuration)), "message4"),
            new LogEntry((timeStmp = timeStmp.plus(tickDuration)), "message5"),
            new LogEntry((timeStmp = timeStmp.plus(tickDuration)), "log6"),
            new LogEntry((timeStmp = timeStmp.plus(tickDuration)), "log7"),
            new LogEntry((timeStmp = timeStmp.plus(tickDuration)), "log8"),
            new LogEntry((timeStmp = timeStmp.plus(tickDuration)), "log9"),
            new LogEntry((timeStmp = timeStmp.plus(tickDuration)), "log10")
        };
    }

    /**
     * Clean-up after the whole test case.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:37:33 AM Aug 22, 2015</i>
     */
    @AfterClass
    public static void tearDownAfterClass() {
        FILE_LOGS = null;
        FILE_NONEXISTENT = null;
        FILE_DIR = null;
        LOG_EXPECTED = null;
        tickDuration = null;
    }

    /**
     * Initialization for each test.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:37:33 AM Aug 22, 2015</i>
     */
    @Before
    public void setUp() {
        parser = new DefaultLogParser(tickDuration);
    }

    /**
     * Clean-up code for each test.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>3:37:33 AM Aug 22, 2015</i>
     */
    @After
    public void tearDown() {
        parser = null;
    }

    /**
     * Test method for {@link dburyak.logmist.model.manipulators.DefaultLogParser#canParse(java.nio.file.Path)}.
     */
    @Test
    public final void testCanParse() {
        Assert.assertTrue(parser.canParse(FILE_LOGS));
    }

    /**
     * Test method for {@link DefaultLogParser#canParse(Path)}. Invalid arg 1 - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:47:01 AM Aug 27, 2015</i>
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testCanParseInvalid1() {
        parser.canParse(null);
        Assert.fail();
    }

    /**
     * Test method for {@link DefaultLogParser#canParse(Path)}. Invalid arg 1 - non-existent file.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:47:01 AM Aug 27, 2015</i>
     */
    @Test()
    public final void testCanParseInvalid1_v2() {
        assert(!Files.exists(FILE_NONEXISTENT));
        final boolean canParse = parser.canParse(FILE_NONEXISTENT);
        Assert.assertFalse(canParse);
    }

    /**
     * Test method for {@link DefaultLogParser#canParse(Path)}. Invalid arg 1 - directory.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:47:01 AM Aug 27, 2015</i>
     */
    @Test()
    public final void testCanParseInvalid1_v3() {
        assert(Files.isDirectory(FILE_DIR));
        final boolean canParse = parser.canParse(FILE_DIR);
        Assert.assertFalse(canParse);
    }


    /**
     * Test method for {@link dburyak.logmist.model.manipulators.DefaultLogParser#parse(java.nio.file.Path)}.
     */
    @Test
    public final void testParse() {
        assert(parser.canParse(FILE_LOGS));
        try {
            final Collection<LogEntry> logsActual = parser.parse(FILE_LOGS);
            assert(LOG_EXPECTED != null);
            assert(logsActual.size() == LOG_EXPECTED.length);
            Assert.assertArrayEquals(LOG_EXPECTED, logsActual.toArray(new LogEntry[logsActual.size()]));
        } catch (@SuppressWarnings("unused") final InaccessibleFileException ex) {
            Assert.fail();
        }
    }

    /**
     * Test method for {@link DefaultLogParser#parse(java.nio.file.Path)}. Invalid arg 1 - null.
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
        parser.parse(null);
        Assert.fail();
    }

    /**
     * Test method for {@link DefaultLogParser#parse(java.nio.file.Path)}. Invalid arg 1 - non-existent file.
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
        assert(!Files.exists(FILE_NONEXISTENT));

        parser.parse(FILE_NONEXISTENT); // expected to throw InaccessibleFileException
        Assert.fail();
    }

    /**
     * Test method for {@link DefaultLogParser#parse(java.nio.file.Path)}. Invalid arg 1 - directory.
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
        assert(Files.isDirectory(FILE_DIR));

        parser.parse(FILE_DIR); // expected to throw InaccessibleFileException
        Assert.fail();
    }

}
