package dburyak.logmist.model;


import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Project : logmist.<br/>
 * JUnit test case for {@link StartsWithFilter} class.
 * <br/><b>Created on:</b> <i>12:01:20 AM Aug 12, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public class TestStartsWithFilter {

    /**
     * Filter 1 name.
     * <br/><b>Created on:</b> <i>1:09:32 AM Aug 12, 2015</i>
     */
    private String fName1 = null;

    /**
     * Filter 2 name.
     * <br/><b>Created on:</b> <i>1:09:40 AM Aug 12, 2015</i>
     */
    private String fName2 = null;

    /**
     * Filter 3 name.
     * <br/><b>Created on:</b> <i>1:09:47 AM Aug 12, 2015</i>
     */
    private String fName3 = null;

    /**
     * Filter 1 prefix.
     * <br/><b>Created on:</b> <i>1:09:52 AM Aug 12, 2015</i>
     */
    private String fPrefix1 = null;

    /**
     * Filter 2 prefix.
     * <br/><b>Created on:</b> <i>1:09:58 AM Aug 12, 2015</i>
     */
    private String fPrefix2 = null;

    /**
     * Filter 3 prefix.
     * <br/><b>Created on:</b> <i>1:10:06 AM Aug 12, 2015</i>
     */
    private String fPrefix3 = null;

    /**
     * Filter 1.
     * <br/><b>Created on:</b> <i>1:10:16 AM Aug 12, 2015</i>
     */
    private StartsWithFilter filter1 = null;

    /**
     * Filter 2.
     * <br/><b>Created on:</b> <i>1:10:20 AM Aug 12, 2015</i>
     */
    private StartsWithFilter filter2 = null;

    /**
     * Filter 3.
     * <br/><b>Created on:</b> <i>1:10:26 AM Aug 12, 2015</i>
     */
    private StartsWithFilter filter3 = null;


    /**
     * One-time initialization for this test.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:01:20 AM Aug 12, 2015</i>
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        // nothing to do here
    }

    /**
     * One time clean-up after this test.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:01:21 AM Aug 12, 2015</i>
     */
    @AfterClass
    public static void tearDownAfterClass() {
        // nothing to do here
    }

    /**
     * Initialization before each test.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:01:21 AM Aug 12, 2015</i>
     */
    @SuppressWarnings("nls")
    @Before
    public void setUp() {
        fName1 = "f1";
        fName2 = "f2";
        fName3 = "f3";

        fPrefix1 = "[COMP1] - ";
        fPrefix2 = "[COMP2] - ";
        fPrefix3 = "TEST - ";

        filter1 = new StartsWithFilter(fName1, fPrefix1);
        filter2 = new StartsWithFilter(fName2, fPrefix2, true);
        filter3 = new StartsWithFilter(fName3, fPrefix3, false);

    }

    /**
     * Clean-up after each test.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:01:21 AM Aug 12, 2015</i>
     */
    @After
    public void tearDown() {
        fName1 = null;
        fName2 = null;
        fName3 = null;

        fPrefix1 = null;
        fPrefix2 = null;
        fPrefix3 = null;

        filter1 = null;
        filter2 = null;
        filter3 = null;
    }

    /**
     * Test method for {@link dburyak.logmist.model.StartsWithFilter#predicateToString()}.
     */
    @SuppressWarnings("nls")
    @Test
    public final void testPredicateToString() {
        final String exp1 = "{prefix=[[COMP1] - ],ignoreCase=[false]}";
        final String exp2 = "{prefix=[[COMP2] - ],ignoreCase=[true]}";
        final String exp3 = "{prefix=[TEST - ],ignoreCase=[false]}";

        assertEquals(exp1, filter1.predicateToString());
        assertEquals(exp1, filter1.predicateToString());

        assertEquals(exp2, filter2.predicateToString());
        assertEquals(exp2, filter2.predicateToString());

        assertEquals(exp3, filter3.predicateToString());
        assertEquals(exp3, filter3.predicateToString());
    }

    /**
     * Test method for
     * {@link dburyak.logmist.model.StartsWithFilter#StartsWithFilter(java.lang.String, java.lang.String, boolean)}.
     * Valid arguments.
     */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public final void testStartsWithFilterStringStringBoolean() {
        final StartsWithFilter f1 = new StartsWithFilter("f1", "abC", false);
        final StartsWithFilter f2 = new StartsWithFilter("f2", "ccdWosQ", true);
        Assert.assertNotEquals(null, f1);
        Assert.assertNotEquals(null, f2);
    }

    /**
     * Test method for {@link StartsWithFilter#StartsWithFilter(String, String, boolean)}. Invalid 1 argument - empty.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:21:14 AM Aug 12, 2015</i>
     */
    @SuppressWarnings({ "unused", "nls", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testStartsWithFilterStringStringBooleanInvalid1() {
        final StartsWithFilter f1 = new StartsWithFilter("", "bade", true);
        fail();
    }

    /**
     * Test method for {@link StartsWithFilter#StartsWithFilter(String, String, boolean)}. Invalid 1 argument - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:23:30 AM Aug 12, 2015</i>
     */
    @SuppressWarnings({ "unused", "nls", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testStartsWithFilterStringStringBooleanInvalid1_v2() {
        final StartsWithFilter f1 = new StartsWithFilter(null, "bsodif", true);
        fail();
    }

    /**
     * Test method for {@link StartsWithFilter#StartsWithFilter(String, String, boolean)}. Invalid 2 arg - empty.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:25:39 AM Aug 12, 2015</i>
     */
    @SuppressWarnings({ "static-method", "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testStartsWithFilterStringStringBooleanInvalid2() {
        final StartsWithFilter f1 = new StartsWithFilter("f1", "", true);
        fail();
    }

    /**
     * Test method for {@link StartsWithFilter#StartsWithFilter(String, String, boolean)}. Invalid 2 arg - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:27:44 AM Aug 12, 2015</i>
     */
    @SuppressWarnings({ "static-method", "nls", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testStartsWithFilterStringStringBooleanInvalid2_v2() {
        final StartsWithFilter f2 = new StartsWithFilter("f1", null, true);
        fail();
    }

    /**
     * Test method for
     * {@link dburyak.logmist.model.StartsWithFilter#StartsWithFilter(java.lang.String, java.lang.String)}.
     */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public final void testStartsWithFilterStringString() {
        final StartsWithFilter f1 = new StartsWithFilter("f1", "aste");
        Assert.assertNotEquals(null, f1);
    }

    /**
     * Test method for {@link StartsWithFilter#StartsWithFilter(String, String)}. Invalid 1 arg - empty.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> MONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:31:03 AM Aug 12, 2015</i>
     */
    @SuppressWarnings({ "unused", "nls", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testStartswithFilterStringStringInvalid1() {
        final StartsWithFilter f1 = new StartsWithFilter("", "test");
        fail();
    }

    /**
     * Test method for {@link StartsWithFilter#StartsWithFilter(String, String)}. Invalid 1 arg - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:32:52 AM Aug 12, 2015</i>
     */
    @SuppressWarnings({ "static-method", "nls", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testStartsWithFilterStringStringInvalid1_v2() {
        final StartsWithFilter f1 = new StartsWithFilter(null, "test");
        fail();
    }

    /**
     * Test method for {@link StartsWithFilter#StartsWithFilter(String, String)}. Invalid 2 arg - empty.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:34:44 AM Aug 12, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testStartsWithFilterStringStringInvalid2() {
        final StartsWithFilter f1 = new StartsWithFilter("f1", "");
        fail();
    }

    /**
     * Test method for {@link StartsWithFilter#StartsWithFilter(String, String)}. Invalid 2 arg - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>12:36:19 AM Aug 12, 2015</i>
     */
    @SuppressWarnings({ "static-method", "nls", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testStartsWithFilterStringStringInvalid2_v2() {
        final StartsWithFilter f1 = new StartsWithFilter("f1", null);
        fail();
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#accept(dburyak.logmist.model.LogEntry)}.
     */
    @SuppressWarnings({ "nls", "boxing" })
    @Test
    public final void testAccept() {
        final LogEntry[] logs = {
            new LogEntry(LocalDateTime.now(), fPrefix1 + "first msg from COMP1"),
            new LogEntry(LocalDateTime.now(), fPrefix1.toLowerCase() + "second msg from COMP2"),
            new LogEntry(LocalDateTime.now(), fPrefix2 + "msg 1 from COMP2"),
            new LogEntry(LocalDateTime.now(), fPrefix2.toLowerCase() + "msg 2 from COMP2"),
            new LogEntry(LocalDateTime.now(), fPrefix3 + "first message from TEST"),
            new LogEntry(LocalDateTime.now(), fPrefix3.toLowerCase() + "second message from TEST"),
            new LogEntry(LocalDateTime.now(), "some common msg")
        };

        final Boolean[][] exp = {
            { TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE },           // filter1 (case sensitive)
            { FALSE, FALSE, TRUE, TRUE, FALSE, FALSE, FALSE },            // filter2 (case insensitive)
            { FALSE, FALSE, FALSE, FALSE, TRUE, FALSE, FALSE },  // filter3 (case sensitive)
        };

        final Object[] act1 = Arrays.stream(logs).map(log -> filter1.accept(log)).toArray();
        final Object[] act2 = Arrays.stream(logs).map(log -> filter2.accept(log)).toArray();
        final Object[] act3 = Arrays.stream(logs).map(log -> filter3.accept(log)).toArray();

        Assert.assertArrayEquals(exp[0], act1);
        Assert.assertArrayEquals(exp[1], act2);
        Assert.assertArrayEquals(exp[2], act3);
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#getName()}.
     */
    @Test
    public final void testGetName() {
        final String exp1 = new String(fName1);
        final String exp2 = new String(fName2);
        final String exp3 = new String(fName3);

        assertEquals(exp1, filter1.getName());
        assertEquals(exp1, filter1.getName());

        assertEquals(exp2, filter2.getName());
        assertEquals(exp2, filter2.getName());

        assertEquals(exp3, filter3.getName());
        assertEquals(exp3, filter3.getName());
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#toString()}.
     */
    @SuppressWarnings("nls")
    @Test
    public final void testToString() {
        final String exp1 = "{name=[f1],type=[StartsWithFilter],predicate=[{prefix=[[COMP1] - ],ignoreCase=[false]}]}";
        final String exp2 = "{name=[f2],type=[StartsWithFilter],predicate=[{prefix=[[COMP2] - ],ignoreCase=[true]}]}";
        final String exp3 = "{name=[f3],type=[StartsWithFilter],predicate=[{prefix=[TEST - ],ignoreCase=[false]}]}";

        assertEquals(exp1, filter1.toString());
        assertEquals(exp1, filter1.toString());

        assertEquals(exp2, filter2.toString());
        assertEquals(exp2, filter2.toString());

        assertEquals(exp3, filter3.toString());
        assertEquals(exp3, filter3.toString());
    }

}
