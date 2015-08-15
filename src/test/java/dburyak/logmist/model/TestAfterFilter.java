package dburyak.logmist.model;


import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Project : logmist.<br/>
 * JUnit test case for {@link AfterFilter}.
 * <br/><b>Created on:</b> <i>2:43:12 AM Aug 15, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class TestAfterFilter {

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
    private final Collection<AfterFilter> filters = new ArrayList<>();


    /**
     * One-time initialization for the whole test case.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:43:12 AM Aug 15, 2015</i>
     */
    @BeforeClass
    public static final void setUpBeforeClass() {
        // nothing to do here
    }

    /**
     * One-time clean-up after the whole test case.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:43:12 AM Aug 15, 2015</i>
     */
    @AfterClass
    public static final void tearDownAfterClass() {
        // nothing to do here
    }

    /**
     * Initialization to be run before each test.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:43:12 AM Aug 15, 2015</i>
     */
    @SuppressWarnings("nls")
    @Before
    public final void setUp() {
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

        fName1 = "10sec after 10:06:45";
        fName2 = "15sec after 10:06:50";
        fName3 = "4sec after message 2";
        fName4 = "7sec after message 8";
        fName5 = "3sec after 10:07:11";

        fDuration1 = Duration.ofSeconds(10);
        fDuration2 = Duration.ofSeconds(15);
        fDuration3 = Duration.ofSeconds(4);
        fDuration4 = Duration.ofSeconds(7);
        fDuration5 = Duration.ofSeconds(3);

        filters.add(new AfterFilter(fName1, LocalDateTime.of(2015, Month.JULY, 5, 10, 6, 45), fDuration1));
        filters.add(new AfterFilter(fName2, LocalDateTime.of(2015, Month.JULY, 5, 10, 6, 50), fDuration2));
        filters.add(new AfterFilter(fName3, log2, fDuration3));
        filters.add(new AfterFilter(fName4, log8, fDuration4));
        filters.add(new AfterFilter(fName5, LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 11), fDuration5));
    }

    /**
     * Cleun-up to be run after each test.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:43:12 AM Aug 15, 2015</i>
     */
    @After
    public final void tearDown() {
        logs.clear();
        filters.clear();
    }

    /**
     * Test method for
     * {@link dburyak.logmist.model.AfterFilter#AfterFilter(java.lang.String, java.time.LocalDateTime, java.time.Duration)}
     * . Valid arguments.
     */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public final void testAfterFilterStringLocalDateTimeDuration() {
        final LocalDateTime t1 = LocalDateTime.of(2015, Month.JULY, 5, 10, 05, 07);
        final Duration d1 = Duration.ofSeconds(10);
        final AfterFilter f1 = new AfterFilter("f1", t1, d1);
        Assert.assertNotEquals(null, f1);
    }

    /**
     * Test method for {@link AfterFilter#AfterFilter(String, LocalDateTime, Duration)}. Invalid arg 1 - empty.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:46:27 AM Aug 15, 2015</i>
     */
    @SuppressWarnings({ "nls", "static-method", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAfterFilterStringLocalDateTimeDurationInvalid1() {
        final LocalDateTime t1 = LocalDateTime.of(2015, Month.JULY, 5, 10, 05, 07);
        final Duration d1 = Duration.ofSeconds(10);
        final AfterFilter f1 = new AfterFilter("", t1, d1);
        Assert.fail();
    }

    /**
     * Test method for {@link AfterFilter#AfterFilter(String, LocalDateTime, Duration)}. Invalid arg 1 - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:46:27 AM Aug 15, 2015</i>
     */
    @SuppressWarnings({ "static-method", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAfterFilterStringLocalDateTimeDurationInvalid1_v2() {
        final LocalDateTime t1 = LocalDateTime.of(2015, Month.JULY, 5, 10, 05, 07);
        final Duration d1 = Duration.ofSeconds(10);
        final AfterFilter f1 = new AfterFilter(null, t1, d1);
        Assert.fail();
    }

    /**
     * Test method for {@link AfterFilter#AfterFilter(String, LocalDateTime, Duration)}. Invalid arg 2 - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:46:27 AM Aug 15, 2015</i>
     */
    @SuppressWarnings({ "static-method", "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAfterFilterStringLocalDateTimeDurationInvalid2() {
        final LocalDateTime t1 = null;
        final Duration d1 = Duration.ofSeconds(10);
        final AfterFilter f1 = new AfterFilter("f1", t1, d1);
        Assert.fail();
    }

    /**
     * Test method for {@link AfterFilter#AfterFilter(String, LocalDateTime, Duration)}. Invalid arg 3 - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:46:27 AM Aug 15, 2015</i>
     */
    @SuppressWarnings({ "static-method", "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAfterFilterStringLocalDateTimeDurationInvalid3() {
        final LocalDateTime t1 = LocalDateTime.of(2015, Month.JULY, 5, 10, 05, 07);
        final Duration d1 = null;
        final AfterFilter f1 = new AfterFilter("f1", t1, d1);
        Assert.fail();
    }

    /**
     * Test method for {@link AfterFilter#AfterFilter(String, LocalDateTime, Duration)}. Invalid arg 3 - negative.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:46:27 AM Aug 15, 2015</i>
     */
    @SuppressWarnings({ "static-method", "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAfterFilterStringLocalDateTimeDurationInvalid3_v2() {
        final LocalDateTime t1 = LocalDateTime.of(2015, Month.JULY, 5, 10, 05, 07);
        final Duration d1 = Duration.ofSeconds(-120);
        final AfterFilter f1 = new AfterFilter("f1", t1, d1);
        Assert.fail();
    }

    /**
     * Test method for {@link AfterFilter#AfterFilter(String, LocalDateTime, Duration)}. Invalid arg 3 - zero.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:46:27 AM Aug 15, 2015</i>
     */
    @SuppressWarnings({ "static-method", "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAfterFilterStringLocalDateTimeDurationInvalid3_v3() {
        final LocalDateTime t1 = LocalDateTime.of(2015, Month.JULY, 5, 10, 05, 07);
        final Duration d1 = Duration.ofSeconds(0);
        final AfterFilter f1 = new AfterFilter("f1", t1, d1);
        Assert.fail();
    }

    /**
     * Test method for
     * {@link dburyak.logmist.model.AfterFilter#AfterFilter(java.lang.String, dburyak.logmist.model.LogEntry, java.time.Duration)}
     * .
     */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public final void testAfterFilterStringLogEntryDuration() {
        final String name1 = "f1";
        final LogEntry l1 = new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 05, 07), "message 1");
        final Duration d1 = Duration.ofSeconds(5);
        final AfterFilter f1 = new AfterFilter(name1, l1, d1);
        Assert.assertNotNull(f1);
    }

    /**
     * Test method for {@link AfterFilter#AfterFilter(String, LogEntry, Duration)}. Invalid 1 arg - empty.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:47:55 PM Aug 15, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAfterFilterStringLogEntryDurationInvalid1() {
        final LogEntry l1 = logs.iterator().next();
        assert(l1 != null);
        final AfterFilter f1 = new AfterFilter("", l1, Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for {@link AfterFilter#AfterFilter(String, LogEntry, Duration)}. Invalid 1 arg - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:47:55 PM Aug 15, 2015</i>
     */
    @SuppressWarnings({ "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAfterFilterStringLogEntryDurationInvalid1_v2() {
        final LogEntry l1 = logs.iterator().next();
        assert(l1 != null);
        final AfterFilter f1 = new AfterFilter(null, l1, Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for {@link AfterFilter#AfterFilter(String, LogEntry, Duration)}. Invalid 2 arg - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:47:55 PM Aug 15, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAfterFilterStringLogEntryDurationInvalid2() {
        final LogEntry l1 = null;
        final AfterFilter f1 = new AfterFilter("f1", l1, Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for {@link AfterFilter#AfterFilter(String, LogEntry, Duration)}. Invalid 3 arg - null.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:47:55 PM Aug 15, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAfterFilterStringLogEntryDurationInvalid3() {
        final LogEntry l1 = logs.iterator().next();
        assert(l1 != null);
        final AfterFilter f1 = new AfterFilter("f1", l1, null);
        Assert.fail();
    }

    /**
     * Test method for {@link AfterFilter#AfterFilter(String, LogEntry, Duration)}. Invalid 3 arg - negative.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:47:55 PM Aug 15, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAfterFilterStringLogEntryDurationInvalid3_v2() {
        final LogEntry l1 = logs.iterator().next();
        assert(l1 != null);
        final AfterFilter f1 = new AfterFilter("f1", l1, Duration.ofSeconds(-5));
        Assert.fail();
    }

    /**
     * Test method for {@link AfterFilter#AfterFilter(String, LogEntry, Duration)}. Invalid 3 arg - zero.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:47:55 PM Aug 15, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAfterFilterStringLogEntryDurationInvalid3_v3() {
        final LogEntry l1 = logs.iterator().next();
        assert(l1 != null);
        final AfterFilter f1 = new AfterFilter("f1", l1, Duration.ofSeconds(0));
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
            "{instant=[2015-07-05T10:06:50],duration=[PT15S]}",
            "{instant=[2015-07-05T10:07:02],duration=[PT4S]}",
            "{instant=[2015-07-05T10:07:08],duration=[PT7S]}",
            "{instant=[2015-07-05T10:07:11],duration=[PT3S]}"
        };
        final Object[] actual = filters.stream().map(AfterFilter::predicateToString).toArray();
        Assert.assertArrayEquals(exp, actual);
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#accept(dburyak.logmist.model.LogEntry)}.
     */
    @SuppressWarnings("boxing")
    @Test
    public final void testAccept() {
        final Boolean[][] exp = {
            { FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE },  // 1
            { TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, FALSE, FALSE, FALSE, FALSE },  // 2
            { FALSE, FALSE, TRUE, TRUE, TRUE, TRUE, FALSE, FALSE, FALSE, FALSE },  // 3
            { FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, TRUE, TRUE },  // 4
            { FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE } // 5
        };
        assert(exp.length == filters.size());

        final Iterator<AfterFilter> itFilters = filters.iterator();
        for (int i = 0; i < filters.size(); i++) {
            final AfterFilter filter = itFilters.next();
            final Object[] actual = logs.stream().map(log -> filter.accept(log)).toArray();
            Assert.assertArrayEquals(exp[i], actual);
        }
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#getName()}.
     */
    @Test
    public final void testGetName() {
        final String[] exp = {
            new String(fName1),
            new String(fName2),
            new String(fName3),
            new String(fName4),
            new String(fName5)
        };
        final Object[] actual = filters.stream().map(AfterFilter::getName).toArray();
        Assert.assertArrayEquals(exp, actual);
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#toString()}.
     */
    @SuppressWarnings("nls")
    @Test
    public final void testToString() {
        final String[] exp = {
            "{name=[10sec after 10:06:45],type=[AfterFilter],predicate=[{instant=[2015-07-05T10:06:45],duration=[PT10S]}]}",
            "{name=[15sec after 10:06:50],type=[AfterFilter],predicate=[{instant=[2015-07-05T10:06:50],duration=[PT15S]}]}",
            "{name=[4sec after message 2],type=[AfterFilter],predicate=[{instant=[2015-07-05T10:07:02],duration=[PT4S]}]}",
            "{name=[7sec after message 8],type=[AfterFilter],predicate=[{instant=[2015-07-05T10:07:08],duration=[PT7S]}]}",
            "{name=[3sec after 10:07:11],type=[AfterFilter],predicate=[{instant=[2015-07-05T10:07:11],duration=[PT3S]}]}"
        };
        final Object[] actual = filters.stream().map(AfterFilter::toString).toArray();
        Assert.assertArrayEquals(exp, actual);
    }

}
