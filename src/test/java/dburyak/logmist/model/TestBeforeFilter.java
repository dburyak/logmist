package dburyak.logmist.model;


import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Project : logmist.<br/>
 * JUnit test for {@link BeforeFilter} class.
 * <br/><b>Created on:</b> <i>11:13:25 PM Aug 17, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public class TestBeforeFilter {

    /**
     * Logs for test.
     * <br/><b>Created on:</b> <i>2:56:00 AM Aug 15, 2015</i>
     */
    private final Collection<LogEntry> logs = new ArrayList<>();

    /**
     * Filter1 name.
     * <br/><b>Created on:</b> <i>2:56:27 AM Aug 15, 2015</i>
     */
    private String fName1 = null;
    /**
     * Filter2 name.
     * <br/><b>Created on:</b> <i>2:56:35 AM Aug 15, 2015</i>
     */
    private String fName2 = null;
    /**
     * Filter3 name.
     * <br/><b>Created on:</b> <i>2:56:41 AM Aug 15, 2015</i>
     */
    private String fName3 = null;
    /**
     * Filter4 name.
     * <br/><b>Created on:</b> <i>2:56:46 AM Aug 15, 2015</i>
     */
    private String fName4 = null;
    /**
     * Filter5 name.
     * <br/><b>Created on:</b> <i>3:13:22 AM Aug 15, 2015</i>
     */
    private String fName5 = null;

    /**
     * Filter1 duration.
     * <br/><b>Created on:</b> <i>2:56:52 AM Aug 15, 2015</i>
     */
    private Duration fDuration1 = null;
    /**
     * Filter2 duration.
     * <br/><b>Created on:</b> <i>2:56:58 AM Aug 15, 2015</i>
     */
    private Duration fDuration2 = null;
    /**
     * Filter3 duration.
     * <br/><b>Created on:</b> <i>2:57:05 AM Aug 15, 2015</i>
     */
    private Duration fDuration3 = null;
    /**
     * Filter4 duration.
     * <br/><b>Created on:</b> <i>2:57:10 AM Aug 15, 2015</i>
     */
    private Duration fDuration4 = null;
    /**
     * Filter5 duration.
     * <br/><b>Created on:</b> <i>3:17:20 AM Aug 15, 2015</i>
     */
    private Duration fDuration5 = null;

    /**
     * Filters.
     * <br/><b>Created on:</b> <i>3:14:31 AM Aug 15, 2015</i>
     */
    private final Collection<BeforeFilter> filters = new ArrayList<>();


    /**
     * One-time initialization for the whole test case.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:13:25 PM Aug 17, 2015</i>
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        // nothing to do here
    }

    /**
     * One-time clean-up for the whole test case.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:13:26 PM Aug 17, 2015</i>
     */
    @AfterClass
    public static void tearDownAfterClass() {
        // nothing to do here
    }

    /**
     * Initialization for each test.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:13:26 PM Aug 17, 2015</i>
     */
    @SuppressWarnings("nls")
    @Before
    public void setUp() {
        final LogEntry log2 = new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 2), "message 2");
        final LogEntry log8 = new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 8), "message 8");

        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 1), "message 1"));
        logs.add(log2);
        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 3), "message 3"));
        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 4), "message 4"));
        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 5), "message 5"));
        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 6), "message 6"));
        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 7), "message 7"));
        logs.add(log8);
        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 9), "message 9"));
        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 10), "message 10"));

        fName1 = "10sec before 10:07:45";
        fName2 = "15sec before 10:07:20";
        fName3 = " 4sec before message 8";
        fName4 = " 7sec before message 2";
        fName5 = " 3sec before 10:06:55";

        fDuration1 = Duration.ofSeconds(10);
        fDuration2 = Duration.ofSeconds(15);
        fDuration3 = Duration.ofSeconds(4);
        fDuration4 = Duration.ofSeconds(7);
        fDuration5 = Duration.ofSeconds(3);

        filters.add(new BeforeFilter(fName1, LocalDateTime.of(2015, Month.JULY, 5, 10, 6, 45), fDuration1));
        filters.add(new BeforeFilter(fName2, LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 20), fDuration2));
        filters.add(new BeforeFilter(fName3, log8, fDuration3));
        filters.add(new BeforeFilter(fName4, log2, fDuration4));
        filters.add(new BeforeFilter(fName5, LocalDateTime.of(2015, Month.JULY, 5, 10, 6, 55), fDuration5));
    }

    /**
     * Clean-up method for each test.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:13:26 PM Aug 17, 2015</i>
     */
    @After
    public void tearDown() {
        logs.clear();
        filters.clear();

        fName1 = null;
        fName2 = null;
        fName3 = null;
        fName4 = null;
        fName5 = null;

        fDuration1 = null;
        fDuration2 = null;
        fDuration3 = null;
        fDuration4 = null;
        fDuration5 = null;
    }

    /**
     * Test method for
     * {@link dburyak.logmist.model.BeforeFilter#BeforeFilter(java.lang.String, java.time.LocalDateTime, java.time.Duration)}
     * .
     */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public final void testBeforeFilterStringLocalDateTimeDuration() {
        final BeforeFilter f1 = new BeforeFilter(
            "f1",
            LocalDateTime.of(2015, Month.JULY, 07, 1, 2, 3),
            Duration.ofSeconds(10));
        Assert.assertNotNull(f1);
    }

    /**
     * Test method for {@link BeforeFilter#BeforeFilter(String, LocalDateTime, Duration)}. Invalid 1 arg - empty.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:34:01 AM Aug 18, 2015</i>
     */
    @SuppressWarnings({ "nls", "static-method", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testBeforeFilterStringLocalDateTimeDurationInvalid1() {
        final LocalDateTime t1 = LocalDateTime.of(2015, Month.JULY, 10, 1, 2, 3);
        final BeforeFilter f1 = new BeforeFilter("", t1, Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for {@link BeforeFilter#BeforeFilter(String, LocalDateTime, Duration)}. Invalid 1 arg - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:34:01 AM Aug 18, 2015</i>
     */
    @SuppressWarnings({ "static-method", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testBeforeFilterStringLocalDateTimeDurationInvalid1_v2() {
        final LocalDateTime t1 = LocalDateTime.of(2015, Month.JULY, 10, 1, 2, 3);
        final BeforeFilter f1 = new BeforeFilter(null, t1, Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for {@link BeforeFilter#BeforeFilter(String, LocalDateTime, Duration)}. Invalid 2 arg - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:34:01 AM Aug 18, 2015</i>
     */
    @SuppressWarnings({ "static-method", "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testBeforeFilterStringLocalDateTimeDurationInvalid2() {
        final LocalDateTime t1 = null;
        final BeforeFilter f1 = new BeforeFilter("f1", t1, Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for {@link BeforeFilter#BeforeFilter(String, LocalDateTime, Duration)}. Invalid 3 arg - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:34:01 AM Aug 18, 2015</i>
     */
    @SuppressWarnings({ "static-method", "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testBeforeFilterStringLocalDateTimeDurationInvalid3() {
        final LocalDateTime t1 = LocalDateTime.of(2015, Month.JULY, 10, 1, 2, 3);
        final BeforeFilter f1 = new BeforeFilter("f1", t1, null);
        Assert.fail();
    }

    /**
     * Test method for {@link BeforeFilter#BeforeFilter(String, LocalDateTime, Duration)}. Invalid 3 arg - negative.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:34:01 AM Aug 18, 2015</i>
     */
    @SuppressWarnings({ "static-method", "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testBeforeFilterStringLocalDateTimeDurationInvalid3_v1() {
        final LocalDateTime t1 = LocalDateTime.of(2015, Month.JULY, 10, 1, 2, 3);
        final BeforeFilter f1 = new BeforeFilter("f1", t1, Duration.ofSeconds(-5));
        Assert.fail();
    }

    /**
     * Test method for {@link BeforeFilter#BeforeFilter(String, LocalDateTime, Duration)}. Invalid 3 arg - zero.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:34:01 AM Aug 18, 2015</i>
     */
    @SuppressWarnings({ "static-method", "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testBeforeFilterStringLocalDateTimeDurationInvalid3_v2() {
        final LocalDateTime t1 = LocalDateTime.of(2015, Month.JULY, 10, 1, 2, 3);
        final BeforeFilter f1 = new BeforeFilter("f1", t1, Duration.ofSeconds(0));
        Assert.fail();
    }

    /**
     * Test method for
     * {@link dburyak.logmist.model.BeforeFilter#BeforeFilter(java.lang.String, dburyak.logmist.model.LogEntry, java.time.Duration)}
     * . Valid args.
     */
    @SuppressWarnings({ "nls" })
    @Test
    public final void testBeforeFilterStringLogEntryDuration() {
        assert(logs.size() > 0);
        final BeforeFilter f1 = new BeforeFilter("f1", logs.iterator().next(), Duration.ofSeconds(15));
        Assert.assertNotNull(f1);
    }

    /**
     * Test method for {@link BeforeFilter#BeforeFilter(String, LogEntry, Duration)}. Invalid 1 arg - empty.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:46:10 AM Aug 18, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testBeforeFilterStringLogEntryDurationInvalid1() {
        assert(logs.size() > 0);
        final BeforeFilter f1 = new BeforeFilter("", logs.iterator().next(), Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for {@link BeforeFilter#BeforeFilter(String, LogEntry, Duration)}. Invalid 1 arg - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:46:10 AM Aug 18, 2015</i>
     */
    @SuppressWarnings({ "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testBeforeFilterStringLogEntryDurationInvalid1_v2() {
        assert(logs.size() > 0);
        final BeforeFilter f1 = new BeforeFilter(null, logs.iterator().next(), Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for {@link BeforeFilter#BeforeFilter(String, LogEntry, Duration)}. Invalid 2 arg - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:46:10 AM Aug 18, 2015</i>
     */
    @SuppressWarnings({ "unused", "nls", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testBeforeFilterStringLogEntryDurationInvalid2() {
        final LogEntry log1 = null;
        final BeforeFilter f1 = new BeforeFilter("f1", log1, Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for {@link BeforeFilter#BeforeFilter(String, LogEntry, Duration)}. Invalid 3 arg - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:46:10 AM Aug 18, 2015</i>
     */
    @SuppressWarnings({ "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testBeforeFilterStringLogEntryDurationInvalid3() {
        assert(logs.size() > 0);
        final BeforeFilter f1 = new BeforeFilter("f1", logs.iterator().next(), null);
        Assert.fail();
    }

    /**
     * Test method for {@link BeforeFilter#BeforeFilter(String, LogEntry, Duration)}. Invalid 3 arg - negative.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:46:10 AM Aug 18, 2015</i>
     */
    @SuppressWarnings({ "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testBeforeFilterStringLogEntryDurationInvalid3_v2() {
        assert(logs.size() > 0);
        final BeforeFilter f1 = new BeforeFilter("f1", logs.iterator().next(), Duration.ofSeconds(-5));
        Assert.fail();
    }

    /**
     * Test method for {@link BeforeFilter#BeforeFilter(String, LogEntry, Duration)}. Invalid 3 arg - zero.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:46:10 AM Aug 18, 2015</i>
     */
    @SuppressWarnings({ "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testBeforeFilterStringLogEntryDurationInvalid3_v3() {
        assert(logs.size() > 0);
        final BeforeFilter f1 = new BeforeFilter("f1", logs.iterator().next(), Duration.ofSeconds(0));
        Assert.fail();
    }

    /**
     * Test method for {@link dburyak.logmist.model.TimeFilter#predicateToString()}.
     */
    @SuppressWarnings("nls")
    @Test
    public final void testPredicateToString() {
        final String[] exp = {
            "{instant=[2015-07-05T10:06:45],duration=[PT10S]}",
            "{instant=[2015-07-05T10:07:20],duration=[PT15S]}",
            "{instant=[2015-07-05T10:07:08],duration=[PT4S]}",
            "{instant=[2015-07-05T10:07:02],duration=[PT7S]}",
            "{instant=[2015-07-05T10:06:55],duration=[PT3S]}",
        };
        assert(filters != null);
        assert(filters.size() == exp.length);
        final Object[] actuals = filters.stream().map(BeforeFilter::predicateToString).toArray();
        Assert.assertArrayEquals(exp, actuals);
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#accept(dburyak.logmist.model.LogEntry)}.
     */
    @SuppressWarnings("boxing")
    @Test
    public final void testAccept() {
        final Collection<Boolean[]> exp = new LinkedList<>();
        exp.add(new Boolean[] { FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE });
        exp.add(new Boolean[] { FALSE, FALSE, FALSE, FALSE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE });
        exp.add(new Boolean[] { FALSE, FALSE, FALSE, TRUE, TRUE, TRUE, TRUE, FALSE, FALSE, FALSE });
        exp.add(new Boolean[] { TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE });
        exp.add(new Boolean[] { FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE });

        assert(filters != null);
        assert(exp.size() == filters.size());

        final Iterator<Boolean[]> itExp = exp.iterator();
        for (final BeforeFilter filter : filters) {
            final Boolean[] expecteds = itExp.next();
            final Object[] actuals = logs.stream().map(log -> filter.accept(log)).toArray();
            Assert.assertArrayEquals(expecteds, actuals);
        }
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#getName()}.
     */
    @SuppressWarnings("nls")
    @Test
    public final void testGetName() {
        final String[] exp = {
            "10sec before 10:07:45",
            "15sec before 10:07:20",
            " 4sec before message 8",
            " 7sec before message 2",
            " 3sec before 10:06:55",
        };
        assert(filters != null);
        assert(filters.size() == exp.length);
        final Object[] actuals = filters.stream().map(BeforeFilter::getName).toArray();
        Assert.assertArrayEquals(exp, actuals);
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#toString()}.
     */
    @SuppressWarnings("nls")
    @Test
    public final void testToString() {
        final String[] exp = {
            "{name=[10sec before 10:07:45],type=[BeforeFilter],predicate=[{instant=[2015-07-05T10:06:45],duration=[PT10S]}]}",
            "{name=[15sec before 10:07:20],type=[BeforeFilter],predicate=[{instant=[2015-07-05T10:07:20],duration=[PT15S]}]}",
            "{name=[ 4sec before message 8],type=[BeforeFilter],predicate=[{instant=[2015-07-05T10:07:08],duration=[PT4S]}]}",
            "{name=[ 7sec before message 2],type=[BeforeFilter],predicate=[{instant=[2015-07-05T10:07:02],duration=[PT7S]}]}",
            "{name=[ 3sec before 10:06:55],type=[BeforeFilter],predicate=[{instant=[2015-07-05T10:06:55],duration=[PT3S]}]}"
        };
        assert(filters != null);
        assert(filters.size() == exp.length);
        final Object[] actuals = filters.stream().map(BeforeFilter::toString).toArray();
        Assert.assertArrayEquals(exp, actuals);
    }

}
