package dburyak.logmist.model;


import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
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
 * JUnit test case for {@link AroundFilter} class.
 * <br/><b>Created on:</b> <i>11:50:06 AM Aug 18, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class TestAroundFilter {

    /**
     * Logs for test.
     * <br/><b>Created on:</b> <i>12:14:50 AM Aug 20, 2015</i>
     */
    private final Collection<LogEntry> logs = new ArrayList<>();

    /**
     * Filter names.
     * <br/><b>Created on:</b> <i>12:14:58 AM Aug 20, 2015</i>
     */
    private final Collection<String> names = new ArrayList<>();

    /**
     * Filter "around" durations.
     * <br/><b>Created on:</b> <i>12:15:09 AM Aug 20, 2015</i>
     */
    private final Collection<Duration> durations = new ArrayList<>();

    /**
     * Around filters.
     * <br/><b>Created on:</b> <i>12:15:18 AM Aug 20, 2015</i>
     */
    private final Collection<AroundFilter> filters = new ArrayList<>();


    /**
     * One-time initialization for test case.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:50:06 AM Aug 18, 2015</i>
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
     * <br/><b>Created on:</b> <i>11:50:07 AM Aug 18, 2015</i>
     */
    @AfterClass
    public static final void tearDownAfterClass() {
        // nothing to do here
    }

    /**
     * Initialization before each test.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:50:07 AM Aug 18, 2015</i>
     */
    @SuppressWarnings("nls")
    @Before
    public final void setUp() {
        final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("MMM d HH:mm:ss");

        final LogEntry log2 = new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 2), "message 2", timeFormat, 2);
        final LogEntry log8 = new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 8), "message 8", timeFormat, 8);

        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 1), "message 1", timeFormat, 1));
        logs.add(log2);
        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 3), "message 3", timeFormat, 3));
        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 4), "message 4", timeFormat, 4));
        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 5), "message 5", timeFormat, 5));
        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 6), "message 6", timeFormat, 6));
        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 7), "message 7", timeFormat, 7));
        logs.add(log8);
        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 9), "message 9", timeFormat, 9));
        logs.add(new LogEntry(LocalDateTime.of(2015, Month.JULY, 5, 10, 7, 10), "message 10", timeFormat, 10));

        names.add("10sec around 10:06:45"); // none
        names.add("15sec around 10:06:50"); // 1,2,3,4,5
        names.add(" 4sec around message 2"); // 1,2,3,4,5,6
        names.add(" 3sec around message 8"); // 10,9,8,7,6,5
        names.add(" 2sec around 10:07:13"); // none

        durations.add(Duration.ofSeconds(10));
        durations.add(Duration.ofSeconds(15));
        durations.add(Duration.ofSeconds(4));
        durations.add(Duration.ofSeconds(3));
        durations.add(Duration.ofSeconds(2));

        final Collection<LocalDateTime> times = new ArrayList<>();
        times.add(LocalDateTime.of(2015, Month.JULY, 5, 10, 6, 45));
        times.add(LocalDateTime.of(2015, Month.JULY, 5, 10, 6, 50));
        times.add(LocalDateTime.of(2015, Month.JULY, 5, 10, 07, 13));

        final Iterator<String> itNames = names.iterator();
        final Iterator<Duration> itDurations = durations.iterator();
        final Iterator<LocalDateTime> itTimes = times.iterator();
        filters.add(new AroundFilter(itNames.next(), itTimes.next(), itDurations.next()));
        filters.add(new AroundFilter(itNames.next(), itTimes.next(), itDurations.next()));
        filters.add(new AroundFilter(itNames.next(), log2, itDurations.next()));
        filters.add(new AroundFilter(itNames.next(), log8, itDurations.next()));
        filters.add(new AroundFilter(itNames.next(), itTimes.next(), itDurations.next()));
    }

    /**
     * Clean-up after each test.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:50:07 AM Aug 18, 2015</i>
     */
    @After
    public final void tearDown() {
        logs.clear();
        names.clear();
        durations.clear();
        filters.clear();
    }

    /**
     * Test method for
     * {@link dburyak.logmist.model.AroundFilter#AroundFilter(java.lang.String, java.time.LocalDateTime, java.time.Duration)}
     * . Valid args.
     */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public final void testAroundFilterStringLocalDateTimeDuration() {
        final LocalDateTime time = LocalDateTime.of(2015, Month.JULY, 5, 10, 07, 05);
        final AroundFilter filter = new AroundFilter("f1", time, Duration.ofSeconds(10));
        Assert.assertNotNull(filter);
    }

    /**
     * Test method for {@link AroundFilter#AroundFilter(String, LocalDateTime, Duration)}. Invalid arg 1 - empty.
     */
    @SuppressWarnings({ "nls", "static-method", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAroundFilterStringLocalDateTimeDurationInvalid1() {
        final LocalDateTime time = LocalDateTime.of(2015, Month.JULY, 5, 10, 07, 05);
        final AroundFilter filter = new AroundFilter("", time, Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for {@link AroundFilter#AroundFilter(String, LocalDateTime, Duration)}. Invalid arg 1 - null.
     */
    @SuppressWarnings({ "static-method", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAroundFilterStringLocalDateTimeDurationInvalid1_v2() {
        final LocalDateTime time = LocalDateTime.of(2015, Month.JULY, 5, 10, 07, 05);
        final AroundFilter filter = new AroundFilter(null, time, Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for {@link AroundFilter#AroundFilter(String, LocalDateTime, Duration)}. Invalid arg 2 - null.
     */
    @SuppressWarnings({ "static-method", "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAroundFilterStringLocalDateTimeDurationInvalid2() {
        final LocalDateTime time = null;
        final AroundFilter filter = new AroundFilter("f1", time, Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for {@link AroundFilter#AroundFilter(String, LocalDateTime, Duration)}. Invalid arg 3 - null.
     */
    @SuppressWarnings({ "static-method", "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAroundFilterStringLocalDateTimeDurationInvalid3() {
        final LocalDateTime time = LocalDateTime.of(2015, Month.JULY, 5, 10, 07, 05);
        final AroundFilter filter = new AroundFilter("f1", time, null);
        Assert.fail();
    }

    /**
     * Test method for {@link AroundFilter#AroundFilter(String, LocalDateTime, Duration)}. Invalid arg 3 - negative.
     */
    @SuppressWarnings({ "static-method", "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAroundFilterStringLocalDateTimeDurationInvalid3_v2() {
        final LocalDateTime time = LocalDateTime.of(2015, Month.JULY, 5, 10, 07, 05);
        final AroundFilter filter = new AroundFilter("f1", time, Duration.ofSeconds(-5));
        Assert.fail();
    }

    /**
     * Test method for {@link AroundFilter#AroundFilter(String, LocalDateTime, Duration)}. Invalid arg 3 - zero.
     */
    @SuppressWarnings({ "static-method", "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAroundFilterStringLocalDateTimeDurationInvalid3_v3() {
        final LocalDateTime time = LocalDateTime.of(2015, Month.JULY, 5, 10, 07, 05);
        final AroundFilter filter = new AroundFilter("f1", time, Duration.ofSeconds(0));
        Assert.fail();
    }

    /**
     * Test method for
     * {@link dburyak.logmist.model.AroundFilter#AroundFilter(java.lang.String, dburyak.logmist.model.LogEntry, java.time.Duration)}
     * .
     */
    @SuppressWarnings("nls")
    @Test
    public final void testAroundFilterStringLogEntryDuration() {
        final LogEntry log = logs.iterator().next();
        final AroundFilter filter = new AroundFilter("f1", log, Duration.ofSeconds(10));
        Assert.assertNotNull(filter);
    }

    /**
     * Test method for
     * {@link AroundFilter#AroundFilter(String, LogEntry, Duration)}. Invalid arg 1 - empty.
     */
    @SuppressWarnings({ "nls", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAroundFilterStringLogEntryDurationInvalid1() {
        final LogEntry log = logs.iterator().next();
        final AroundFilter filter = new AroundFilter("", log, Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for
     * {@link AroundFilter#AroundFilter(String, LogEntry, Duration)}. Invalid arg 1 - null.
     */
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public final void testAroundFilterStringLogEntryDurationInvalid1_v2() {
        final LogEntry log = logs.iterator().next();
        final AroundFilter filter = new AroundFilter(null, log, Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for
     * {@link AroundFilter#AroundFilter(String, LogEntry, Duration)}. Invalid arg 2 - null.
     */
    @SuppressWarnings({ "unused", "nls", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAroundFilterStringLogEntryDurationInvalid2() {
        final LogEntry log = null;
        final AroundFilter filter = new AroundFilter("f1", log, Duration.ofSeconds(10));
        Assert.fail();
    }

    /**
     * Test method for
     * {@link AroundFilter#AroundFilter(String, LogEntry, Duration)}. Invalid arg 3 - null.
     */
    @SuppressWarnings({ "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAroundFilterStringLogEntryDurationInvalid3() {
        final LogEntry log = logs.iterator().next();
        final AroundFilter filter = new AroundFilter("f1", log, null);
        Assert.fail();
    }

    /**
     * Test method for
     * {@link AroundFilter#AroundFilter(String, LogEntry, Duration)}. Invalid arg 3 - negative.
     */
    @SuppressWarnings({ "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAroundFilterStringLogEntryDurationInvalid3_v2() {
        final LogEntry log = logs.iterator().next();
        final AroundFilter filter = new AroundFilter("f1", log, Duration.ofSeconds(-5));
        Assert.fail();
    }

    /**
     * Test method for
     * {@link AroundFilter#AroundFilter(String, LogEntry, Duration)}. Invalid arg 3 - zero.
     */
    @SuppressWarnings({ "unused", "nls" })
    @Test(expected = IllegalArgumentException.class)
    public final void testAroundFilterStringLogEntryDurationInvalid3_v3() {
        final LogEntry log = logs.iterator().next();
        final AroundFilter filter = new AroundFilter("f1", log, Duration.ofSeconds(0));
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
            "{instant=[2015-07-05T10:07:08],duration=[PT3S]}",
            "{instant=[2015-07-05T10:07:13],duration=[PT2S]}"
        };
        final Object[] actuals = filters.stream().map(AroundFilter::predicateToString).toArray();
        Assert.assertArrayEquals(exp, actuals);
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#accept(dburyak.logmist.model.LogEntry)}.
     */
    @SuppressWarnings("boxing")
    @Test
    public final void testAccept() {
        final Collection<Boolean[]> exp = new ArrayList<>();
        exp.add(new Boolean[] { FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE }); // 1
        exp.add(new Boolean[] { TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, FALSE, FALSE, FALSE, FALSE }); // 2
        exp.add(new Boolean[] { TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, FALSE, FALSE, FALSE, FALSE }); // 3
        exp.add(new Boolean[] { FALSE, FALSE, FALSE, FALSE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE }); // 4
        exp.add(new Boolean[] { FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE }); // 5

        assert (filters != null);
        assert (exp.size() == filters.size());

        final Iterator<Boolean[]> itExp = exp.iterator();
        for (final AroundFilter filter : filters) {
            final Boolean[] expecteds = itExp.next();
            final Object[] actuals = logs.stream().map(log -> filter.accept(log)).toArray();
            Assert.assertArrayEquals(expecteds, actuals);
        }
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#getName()}.
     */
    @Test
    public final void testGetName() {
        final String[] exp = names.toArray(new String[names.size()]);
        final Object[] actuals = filters.stream().map(AroundFilter::getName).toArray();
        Assert.assertArrayEquals(exp, actuals);
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#toString()}.
     */
    @SuppressWarnings("nls")
    @Test
    public final void testToString() {
        final String[] exp = {
            "{name=[10sec around 10:06:45],type=[AroundFilter],predicate=[{instant=[2015-07-05T10:06:45],duration=[PT10S]}]}",
            "{name=[15sec around 10:06:50],type=[AroundFilter],predicate=[{instant=[2015-07-05T10:06:50],duration=[PT15S]}]}",
            "{name=[ 4sec around message 2],type=[AroundFilter],predicate=[{instant=[2015-07-05T10:07:02],duration=[PT4S]}]}",
            "{name=[ 3sec around message 8],type=[AroundFilter],predicate=[{instant=[2015-07-05T10:07:08],duration=[PT3S]}]}",
            "{name=[ 2sec around 10:07:13],type=[AroundFilter],predicate=[{instant=[2015-07-05T10:07:13],duration=[PT2S]}]}"
        };
        final Object[] actuals = filters.stream().map(AroundFilter::toString).toArray();
        Assert.assertArrayEquals(exp, actuals);
    }

}
