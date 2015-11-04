package dburyak.logmist.model.manipulators;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        LOG_EXPECTED = new LogEntry[] {
            new LogEntry("message1", 1),
            new LogEntry("message2", 2),
            new LogEntry("message3", 3),
            new LogEntry("message4", 4),
            new LogEntry("message5", 5),
            new LogEntry("log6", 6),
            new LogEntry("log7", 7),
            new LogEntry("log8", 8),
            new LogEntry("log9", 9),
            new LogEntry("log10", 10)
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
        parser = new DefaultLogParser();
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
     * 
     * @throws InaccessibleFileException
     *             not expected to be thrown for this test
     */
    @Test
    public final void testCanParse() throws InaccessibleFileException {
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
        try {
            parser.canParse(null);
            Assert.fail();
        } catch (@SuppressWarnings("unused") final InaccessibleFileException ex) {
            Assert.fail();
        }
    }

    /**
     * Test method for {@link DefaultLogParser#canParse(Path)}. Invalid arg 1 - non-existent file.
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
     * Test method for {@link DefaultLogParser#canParse(Path)}. Invalid arg 1 - directory.
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
     * Test method for {@link dburyak.logmist.model.manipulators.DefaultLogParser#parse(java.nio.file.Path)}.
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
     * Test method for {@link DefaultLogParser#parse(java.nio.file.Path)}. Invalid arg 1 - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:37:32 AM Aug 22, 2015</i>
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testParseInvalid1() {
        try {
            parser.parse(null);
        } catch (@SuppressWarnings("unused") final ParseException e) {
            Assert.fail();
        } catch (@SuppressWarnings("unused") final InaccessibleFileException e) {
            Assert.fail();
        }
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
        assert (!Files.exists(FILE_NONEXISTENT));

        try {
            parser.parse(FILE_NONEXISTENT); // expected to throw InaccessibleFileException
        } catch (@SuppressWarnings("unused") final ParseException e) {
            Assert.fail();
        }
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
        assert (Files.isDirectory(FILE_DIR));

        try {
            parser.parse(FILE_DIR); // expected to throw InaccessibleFileException
        } catch (@SuppressWarnings("unused") final ParseException e) {
            Assert.fail();
        }
        Assert.fail();
    }

    /**
     * Test method for {@link DefaultLogParser#toString()}.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:31:55 AM Sep 17, 2015</i>
     */
    @Test
    public final void testToString() {
        Assert.assertEquals(DefaultLogParser.class.getSimpleName(), parser.toString());
    }

}
