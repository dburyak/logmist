package dburyak.logmist.model;


import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dburyak.jtools.AssertConst;


/**
 * Project : logmist.<br/>
 * JUnit test for {@link RegexpFilter}. <br/>
 * <b>Created on:</b> <i>2:13:00 AM Aug 1, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public class TestRegexpFilter {

    /**
     * One time initialization code. <br/>
     * <b>Created on:</b> <i>2:13:00 AM Aug 1, 2015</i>
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        // nothing to be done for this test
    }

    /**
     * One time teardown code. <br/>
     * <b>Created on:</b> <i>2:13:00 AM Aug 1, 2015</i>
     */
    @AfterClass
    public static void tearDownAfterClass() {
        // nothing to be done for this test
    }

    /**
     * Initialization code for each test. <br/>
     * <b>Created on:</b> <i>2:13:00 AM Aug 1, 2015</i>
     */
    @Before
    public void setUp() {
        // nothing to be done there
    }

    /**
     * Teardown code for each test. <br/>
     * <b>Created on:</b> <i>2:13:01 AM Aug 1, 2015</i>
     */
    @After
    public void tearDown() {
        // nothing to be done there
    }

    /**
     * Test method for {@link dburyak.logmist.model.RegexpFilter#predicateToString()}.
     * <br/><b>Created on:</b> <i>1:15:04 AM Aug 3, 2015</i>
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testPredicateToString() {
        final RegexpFilter[] filters = {
            new RegexpFilter("f1", Pattern.compile("a")),
            new RegexpFilter("f2", Pattern.compile("b"), true),
            new RegexpFilter("f3", Pattern.compile("c"), false),
            new RegexpFilter("f4", Pattern.compile("d"), true, true),
            new RegexpFilter("f5", Pattern.compile("e"), true, false),
            new RegexpFilter("f6", Pattern.compile("f"), false, true),
            new RegexpFilter("f7", Pattern.compile("g"), false, false)
        };

        final String[] exp = {
            "{pattern=[a],ignoreCase=[false],fullMatch=[false]}",
            "{pattern=[b],ignoreCase=[true],fullMatch=[false]}",
            "{pattern=[c],ignoreCase=[false],fullMatch=[false]}",
            "{pattern=[d],ignoreCase=[true],fullMatch=[true]}",
            "{pattern=[e],ignoreCase=[true],fullMatch=[false]}",
            "{pattern=[f],ignoreCase=[false],fullMatch=[true]}",
            "{pattern=[g],ignoreCase=[false],fullMatch=[false]}"
        };

        assert(filters.length == exp.length) : AssertConst.ASRT_INVALID_VALUE;

        for (int i = 0; i < filters.length; i++) {
            Assert.assertEquals(exp[i], filters[i].predicateToString());
            Assert.assertEquals(exp[i], filters[i].predicateToString());
            Assert.assertNotEquals(null, filters[i].predicateToString());
            Assert.assertNotEquals("", filters[i].predicateToString()); //$NON-NLS-1$
        }

    }

    /**
     * Test method for
     * {@link dburyak.logmist.model.RegexpFilter#RegexpFilter(java.lang.String, java.util.regex.Pattern)} .
     * <br/><b>Created on:</b> <i>1:15:26 AM Aug 3, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused", "static-method" })
    @Test
    public final void testRegexpFilterStringPattern() {
        final RegexpFilter f1 = new RegexpFilter("f1", Pattern.compile("a"));
    }

    /**
     * Test method for {@link RegexpFilter#RegexpFilter(String, Pattern)} with null name.
     * <br/><b>Created on:</b> <i>1:48:20 AM Aug 3, 2015</i>
     */
    @SuppressWarnings({ "static-method", "nls", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testRegexpFilterStringPatternInvalid1() {
        final RegexpFilter f1 = new RegexpFilter(null, Pattern.compile("a"));
    }

    /**
     * Test method for {@link RegexpFilter#RegexpFilter(String, Pattern)} with empty name.
     * <br/><b>Created on:</b> <i>1:48:57 AM Aug 3, 2015</i>
     */
    @SuppressWarnings({ "nls", "static-method", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testRegexpFilterStringPatternInvalid1_v2() {
        final RegexpFilter f2 = new RegexpFilter("", Pattern.compile("a"));
    }

    /**
     * Test method for {@link RegexpFilter#RegexpFilter(String, Pattern)} with null pattern.
     * <br/><b>Created on:</b> <i>1:51:07 AM Aug 3, 2015</i>
     */
    @SuppressWarnings({ "static-method", "nls", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testRegexpFilterStringPatternInvalid2() {
        final RegexpFilter f3 = new RegexpFilter("f1", null);
    }

    /**
     * Test method for
     * {@link dburyak.logmist.model.RegexpFilter#RegexpFilter(java.lang.String, java.util.regex.Pattern, boolean)} .
     * <br/><b>Created on:</b> <i>1:15:51 AM Aug 3, 2015</i>
     */
    @SuppressWarnings({ "nls", "unused", "static-method" })
    @Test
    public final void testRegexpFilterStringPatternBoolean() {
        final RegexpFilter f1 = new RegexpFilter("f1", Pattern.compile("a"), true);
        final RegexpFilter f2 = new RegexpFilter("f2", Pattern.compile("b"), false);
    }

    /**
     * Test method for
     * {@link dburyak.logmist.model.RegexpFilter#RegexpFilter(java.lang.String, java.util.regex.Pattern, boolean, boolean)}
     * .
     * <br/><b>Created on:</b> <i>1:20:23 AM Aug 3, 2015</i>
     */
    @SuppressWarnings({ "static-method", "nls", "unused" })
    @Test
    public final void testRegexpFilterStringPatternBooleanBoolean() {
        final RegexpFilter f1 = new RegexpFilter("f1", Pattern.compile("a"), true, true);
        final RegexpFilter f2 = new RegexpFilter("f2", Pattern.compile("b"), true, false);
        final RegexpFilter f3 = new RegexpFilter("f3", Pattern.compile("c"), false, true);
        final RegexpFilter f4 = new RegexpFilter("f4", Pattern.compile("d"), false, false);
    }

    /**
     * Test method for
     * {@link dburyak.logmist.model.PredicateFilter#accept(dburyak.logmist.model.LogEntry)} .
     * <br/><b>Created on:</b> <i>1:20:43 AM Aug 3, 2015</i>
     */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public final void testAccept() {
        final LogEntry[] logs = {
            new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4), "abcdef"),
            new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 5), "abcdf"),
            new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 6), "abcf"),
            new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 7), "abf"),
            new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 8), "ab")
        };

        final RegexpFilter[] filters = {
            new RegexpFilter("f1", Pattern.compile("a")),
            new RegexpFilter("f2", Pattern.compile("cd")),
            new RegexpFilter("f3", Pattern.compile("AB[\\w]+f"), true),
            new RegexpFilter("f4", Pattern.compile("ab"), false, true)
        };

        final Boolean[][] exp = {
            { TRUE, TRUE, TRUE, TRUE, TRUE },
            { TRUE, TRUE, FALSE, FALSE, FALSE },
            { TRUE, TRUE, TRUE, FALSE, FALSE },
            { FALSE, FALSE, FALSE, FALSE, TRUE }
        };

        assert(filters.length == exp.length);

        for (int i = 0; i < filters.length; i++) {
            final RegexpFilter filter = filters[i];
            @SuppressWarnings("boxing") final Object[] actuals = Arrays.stream(logs).map(log -> filter.accept(log))
                .toArray();
            Assert.assertArrayEquals("i = [" + i + "]", exp[i], actuals);
        }
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#getName()}.
     * <br/><b>Created on:</b> <i>1:37:15 AM Aug 3, 2015</i>
     */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public final void testGetName() {
        final RegexpFilter[] filters = {
            new RegexpFilter("f1", Pattern.compile("a")),
            new RegexpFilter("f2", Pattern.compile("cd")),
            new RegexpFilter("f3", Pattern.compile("AB[\\w]+f"), true),
            new RegexpFilter("f4", Pattern.compile("ab"), false, true)
        };
        final String[] exp = { "f1", "f2", "f3", "f4" };
        final Object[] actuals = Arrays.stream(filters).map(RegexpFilter::getName).toArray();

        Assert.assertArrayEquals(exp, actuals);
    }

    /**
     * Test method for {@link dburyak.logmist.model.PredicateFilter#toString()}.
     * <br/><b>Created on:</b> <i>1:37:58 AM Aug 3, 2015</i>
     */
    @SuppressWarnings({ "static-method", "nls" })
    @Test
    public final void testToString() {
        final RegexpFilter[] filters = {
            new RegexpFilter("f1", Pattern.compile("a")),
            new RegexpFilter("f2", Pattern.compile("cd")),
            new RegexpFilter("f3", Pattern.compile("AB[\\w]+f"), true),
            new RegexpFilter("f4", Pattern.compile("ab"), false, true)
        };
        final String[] exp = {
            "{name=[f1],type=[RegexpFilter],predicate=[{pattern=[a],ignoreCase=[false],fullMatch=[false]}]}",
            "{name=[f2],type=[RegexpFilter],predicate=[{pattern=[cd],ignoreCase=[false],fullMatch=[false]}]}",
            "{name=[f3],type=[RegexpFilter],predicate=[{pattern=[AB[\\w]+f],ignoreCase=[true],fullMatch=[false]}]}",
            "{name=[f4],type=[RegexpFilter],predicate=[{pattern=[ab],ignoreCase=[false],fullMatch=[true]}]}"
        };

        final Object[] actuals = Arrays.stream(filters).map(RegexpFilter::toString).toArray();
        Assert.assertArrayEquals(exp, actuals);
    }

}
