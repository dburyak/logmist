package dburyak.logmist.model;


import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Project : logmist.<br/>
 * JUnit test for {@link EndsWithFilter} class.
 * <br/><b>Created on:</b> <i>2:04:38 AM Aug 12, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public class TestEndsWithFilter {

    /**
     * Filter 1 name.
     * <br/><b>Created on:</b> <i>2:31:53 AM Aug 12, 2015</i>
     */
    private String fName1 = null;

    /**
     * Filter 2 name.
     * <br/><b>Created on:</b> <i>2:32:41 AM Aug 12, 2015</i>
     */
    private String fName2 = null;

    /**
     * Filter 3 name.
     * <br/><b>Created on:</b> <i>2:32:46 AM Aug 12, 2015</i>
     */
    private String fName3 = null;

    /**
     * Filter 1 suffix.
     * <br/><b>Created on:</b> <i>2:32:52 AM Aug 12, 2015</i>
     */
    private String fSuffix1 = null;

    /**
     * Filter 2 suffix.
     * <br/><b>Created on:</b> <i>2:32:59 AM Aug 12, 2015</i>
     */
    private String fSuffix2 = null;

    /**
     * Filter 3 suffix.
     * <br/><b>Created on:</b> <i>2:33:04 AM Aug 12, 2015</i>
     */
    private String fSuffix3 = null;

    /**
     * Filter 1.
     * <br/><b>Created on:</b> <i>2:33:11 AM Aug 12, 2015</i>
     */
    private EndsWithFilter filter1 = null;

    /**
     * Filter 2.
     * <br/><b>Created on:</b> <i>2:33:18 AM Aug 12, 2015</i>
     */
    private EndsWithFilter filter2 = null;

    /**
     * Filter 3.
     * <br/><b>Created on:</b> <i>2:33:25 AM Aug 12, 2015</i>
     */
    private EndsWithFilter filter3 = null;


    /**
     * One time initialization for this test case.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:04:39 AM Aug 12, 2015</i>
     */
    @BeforeClass
    public static final void setUpBeforeClass() {
        // nothing to do here
    }

    /**
     * One time clean-up for this test case.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:04:39 AM Aug 12, 2015</i>
     */
    @AfterClass
    public static final void tearDownAfterClass() {
        // nothing to do here
    }

    /**
     * Initialization to be run for each test.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:04:39 AM Aug 12, 2015</i>
     */
    @SuppressWarnings("nls")
    @Before
    public final void setUp() {
        fName1 = "f1";
        fName2 = "f2";
        fName3 = "f3";

        fSuffix1 = "Test";
        fSuffix2 = "Best";
        fSuffix3 = "NULL";

        filter1 = new EndsWithFilter(fName1, fSuffix1);
        filter2 = new EndsWithFilter(fName2, fSuffix2, false);
        filter3 = new EndsWithFilter(fName3, fSuffix3, true);
    }

    /**
     * Clean-up to be run after each test.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:04:39 AM Aug 12, 2015</i>
     */
    @After
    public void tearDown() {
        fName1 = null;
        fName2 = null;
        fName3 = null;

        fSuffix1 = null;
        fSuffix2 = null;
        fSuffix3 = null;

        filter1 = null;
        filter2 = null;
        filter3 = null;

    }

    /**
     * Test method for {@link dburyak.logmist.model.EndsWithFilter#predicateToString()}.
     */
    @SuppressWarnings("nls")
    @Test
    public final void testPredicateToString() {
        final String exp1 = "{suffix=[" + fSuffix1 + "],ignoreCase=[false]}";
        final String exp2 = "{suffix=[" + fSuffix2 + "],ignoreCase=[false]}";
        final String exp3 = "{suffix=[" + fSuffix3 + "],ignoreCase=[true]}";

        Assert.assertEquals(exp1, filter1.predicateToString());
        Assert.assertEquals(exp1, filter1.predicateToString());

        Assert.assertEquals(exp2, filter2.predicateToString());
        Assert.assertEquals(exp2, filter2.predicateToString());

        Assert.assertEquals(exp3, filter3.predicateToString());
        Assert.assertEquals(exp3, filter3.predicateToString());
    }

    /**
     * Test method for
     * {@link dburyak.logmist.model.EndsWithFilter#EndsWithFilter(java.lang.String, java.lang.String, boolean)}.
     */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public final void testEndsWithFilterStringStringBoolean() {
        final EndsWithFilter f1 = new EndsWithFilter("f1", "test1", false);
        final EndsWithFilter f2 = new EndsWithFilter("f2", "test2", true);

        Assert.assertNotEquals(null, f1);
        Assert.assertNotEquals(null, f2);
    }

    /**
     * Test method for {@link EndsWithFilter#EndsWithFilter(String, String, boolean)}. Invalid arg 1 - empty.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:50:22 AM Aug 12, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testEndsWithFilterStringStringBooleanInvalid1() {
        final EndsWithFilter f1 = new EndsWithFilter("", "test", true);
        fail();
    }

    /**
     * Test method for {@link EndsWithFilter#EndsWithFilter(String, String, boolean)}. Invalild arg 1 - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:51:46 AM Aug 12, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testEndsWithFilterStringStringBooleanInvalid1_v2() {
        final EndsWithFilter f1 = new EndsWithFilter(null, "test2", true);
        fail();
    }

    /**
     * Test method for {@link EndsWithFilter#EndsWithFilter(String, String, boolean)}. Invalid arg 2 - empty.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:53:18 AM Aug 12, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testEndsWithFilterStringStringBooleanInvalid2() {
        final EndsWithFilter f1 = new EndsWithFilter("f1", "", true);
        fail();
    }

    /**
     * Test method for {@link EndsWithFilter#EndsWithFilter(String, String, boolean)}. Invalid arg2 - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:54:42 AM Aug 12, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testEndsWithFilterStringStringBooleanInvalid2_v2() {
        final EndsWithFilter f1 = new EndsWithFilter("f1", null, true);
        fail();
    }

    /**
     * Test method for {@link dburyak.logmist.model.EndsWithFilter#EndsWithFilter(java.lang.String, java.lang.String)}.
     */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public final void testEndsWithFilterStringString() {
        final EndsWithFilter f1 = new EndsWithFilter("f1", "sf1");
        Assert.assertNotEquals(null, f1);
    }

    /**
     * Test method for {@link EndsWithFilter#EndsWithFilter(String, String)}. Invalid arg 1 - empty.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:57:58 AM Aug 12, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testEndsWithFilterStringStringInvalid1() {
        final EndsWithFilter f1 = new EndsWithFilter("", "sf1");
        fail();
    }

    /**
     * Test method for {@link EndsWithFilter#EndsWithFilter(String, String)}. Invalid arg 1 - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:59:18 AM Aug 12, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testEndsWithFilterStringStringInvalid1_v2() {
        final EndsWithFilter f1 = new EndsWithFilter(null, "sf2");
        fail();
    }

    /**
     * Test method for {@link EndsWithFilter#EndsWithFilter(String, String)}. Invalid arg 2 - empty.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:11:08 AM Aug 13, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testEndsWithFilterStringStringInvalid2() {
        final EndsWithFilter f1 = new EndsWithFilter("f1", "");
        fail();
    }

    /**
     * Test method for {@link EndsWithFilter#EndsWithFilter(String, String)}. Invalid arg2 - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:12:39 AM Aug 13, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testEndsWithFilterStringStringInvalid2_v2() {
        final EndsWithFilter f1 = new EndsWithFilter("f1", null);
        fail();
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#accept(dburyak.logmist.model.LogEntry)}.
     */
    @SuppressWarnings({ "nls", "boxing" })
    @Test
    public final void testAccept() {
        final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("MMM d HH:mm:ss");

        final LogEntry[] logs = {
            new LogEntry(LocalDateTime.now(), "[INFO] - first test", timeFormat, 1),
            new LogEntry(LocalDateTime.now(), "[DEBUG] - second Test", timeFormat, 2),
            new LogEntry(LocalDateTime.now(), "[FATAL] - third best", timeFormat, 3),
            new LogEntry(LocalDateTime.now(), "[TRACE] - fourth BEST", timeFormat, 4),
            new LogEntry(LocalDateTime.now(), "[ERROR] - fifth Best", timeFormat, 5),
            new LogEntry(LocalDateTime.now(), "[INFO] - value1 = null", timeFormat, 6),
            new LogEntry(LocalDateTime.now(), "[WARN] - value2 = NULL", timeFormat, 7),
            new LogEntry(LocalDateTime.now(), "[ERROR] - no match", timeFormat, 8)
        };

        final Boolean[][] exp = {
            { FALSE, TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE },           // ("Test", false)
            { FALSE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE, FALSE },           // ("Best", false)
            { FALSE, FALSE, FALSE, FALSE, FALSE, TRUE, TRUE, FALSE }  // ("NULL", true)
        };

        final EndsWithFilter[] filters = {
            filter1,
            filter2,
            filter3
        };

        assert (filters.length == exp.length);
        for (int i = 0; i < filters.length; i++) {
            final int index = i;
            final Object[] actual = Arrays.stream(logs).map(log -> filters[index].accept(log)).toArray();
            Assert.assertArrayEquals(exp[i], actual);
        }
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#getName()}.
     */
    @Test
    public final void testGetName() {
        final String exp1 = new String(fName1);
        final String exp2 = new String(fName2);
        final String exp3 = new String(fName3);

        Assert.assertEquals(exp1, filter1.getName());
        Assert.assertEquals(exp1, filter1.getName());

        Assert.assertEquals(exp2, filter2.getName());
        Assert.assertEquals(exp2, filter2.getName());

        Assert.assertEquals(exp3, filter3.getName());
        Assert.assertEquals(exp3, filter3.getName());
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#toString()}.
     */
    @SuppressWarnings("nls")
    @Test
    public final void testToString() {
        final EndsWithFilter[] filters = { filter1, filter2, filter3 };
        final String[] exp = {
            "{name=[f1],type=[EndsWithFilter],predicate=[{suffix=[Test],ignoreCase=[false]}]}",
            "{name=[f2],type=[EndsWithFilter],predicate=[{suffix=[Best],ignoreCase=[false]}]}",
            "{name=[f3],type=[EndsWithFilter],predicate=[{suffix=[NULL],ignoreCase=[true]}]}"
        };
        assert (filters.length == exp.length);
        for (int i = 0; i < filters.length; i++) {
            Assert.assertEquals(exp[i], filters[i].toString());
            Assert.assertEquals(exp[i], filters[i].toString());
        }
    }

}
