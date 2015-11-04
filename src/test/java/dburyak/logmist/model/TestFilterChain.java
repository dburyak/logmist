package dburyak.logmist.model;


import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import dburyak.logmist.model.FilterChain.FilterChainBuilder;
import dburyak.logmist.model.FilterChain.LinkType;


/**
 * Project : logmist.<br/>
 * <br/><b>Created on:</b> <i>10:48:55 AM Jul 30, 2015</i>
 * JUnit test for {@link FilterChain} class. Implicitly tests the {@link FilterChainBuilder} class.
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public class TestFilterChain {

    /**
     * Time formatter for synthetic log entries.
     * <br/><b>Created on:</b> <i>8:04:43 PM Nov 2, 2015</i>
     */
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("MMM d HH:mm:ss"); //$NON-NLS-1$

    /**
     * Static data for tests.
     * <br/><b>Created on:</b> <i>11:11:57 AM Jul 30, 2015</i>
     */
    private static final LogEntry[] LOGS_STATIC = {
        new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4), "abcdef", TIME_FORMAT, 1), //$NON-NLS-1$
        new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 5), "acdef", TIME_FORMAT, 2), //$NON-NLS-1$
        new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 6), "adef", TIME_FORMAT, 3), //$NON-NLS-1$
        new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 7), "aef", TIME_FORMAT, 4), //$NON-NLS-1$
        new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 8), "af", TIME_FORMAT, 5), //$NON-NLS-1$
        new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 9), "a", TIME_FORMAT, 6), //$NON-NLS-1$
    };


    /**
     * Test method for {@link dburyak.logmist.model.FilterChain#accept(dburyak.logmist.model.LogEntry)}.
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testAccept() {
        final ContainsFilter a = new ContainsFilter("a", "a");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter b = new ContainsFilter("b", "b");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter c = new ContainsFilter("c", "c");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter d = new ContainsFilter("d", "d");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter e = new ContainsFilter("e", "e");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter f = new ContainsFilter("f", "f");  //$NON-NLS-1$//$NON-NLS-2$

        // a - d - f
        final FilterChain f1 = (new FilterChainBuilder("f1", a, LinkType.AND)).and(d).and(f).build(); //$NON-NLS-1$
        final boolean[] exp1 = { true, true, true, false, false, false };
        final boolean[] actual1 = new boolean[exp1.length];
        assert ((exp1.length == LOGS_STATIC.length) && (actual1.length == exp1.length));
        for (int i = 0; i < LOGS_STATIC.length; i++) {
            actual1[i] = f1.accept(LOGS_STATIC[i]);
        }
        Assert.assertArrayEquals(exp1, actual1);

        // a - d(OR) - f
        // effectively equal to : a - d - f
        final FilterChain f2 = (new FilterChainBuilder("f2", a, LinkType.AND)).or(d).and(f).build(); //$NON-NLS-1$
        final boolean[] exp2 = { true, true, true, false, false, false };
        final boolean[] actual2 = new boolean[exp2.length];
        assert ((exp2.length == LOGS_STATIC.length) && (actual2.length == exp2.length));
        for (int i = 0; i < LOGS_STATIC.length; i++) {
            actual2[i] = f2.accept(LOGS_STATIC[i]);
        }
        Assert.assertArrayEquals(exp2, actual2);

        // b\
        // c - ef
        // d/
        final FilterChain f3 = (new FilterChainBuilder("f3", b, LinkType.OR)) //$NON-NLS-1$
            .or(c)
            .or(d)
            .and(new ContainsFilter("ef", "ef"))  //$NON-NLS-1$//$NON-NLS-2$
            .build();
        final boolean[] exp3 = { true, true, true, false, false, false };
        final boolean[] actual3 = new boolean[exp3.length];
        assert ((exp3.length == LOGS_STATIC.length) && (actual3.length == exp3.length));
        for (int i = 0; i < LOGS_STATIC.length; i++) {
            actual3[i] = f3.accept(LOGS_STATIC[i]);
        }
        Assert.assertArrayEquals(exp3, actual3);

        // a \....... / f
        // ... c - e .
        // b /........\ d
        final FilterChain f4 = (new FilterChainBuilder("f4", a, LinkType.OR)).or(b).and(c).and(e).or(f).or(d).build(); //$NON-NLS-1$
        final boolean[] exp4 = { true, true, false, false, false, false };
        final boolean[] actual4 = new boolean[exp4.length];
        assert ((exp4.length == LOGS_STATIC.length) && (actual4.length == exp4.length));
        for (int i = 0; i < LOGS_STATIC.length; i++) {
            actual4[i] = f4.accept(LOGS_STATIC[i]);
        }
        Assert.assertArrayEquals(exp4, actual4);
    }

    /**
     * Test method for {@link dburyak.logmist.model.FilterChain#getName()}.
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testGetName() {
        final String name1 = "f1"; //$NON-NLS-1$
        final String name2 = "f2"; //$NON-NLS-1$
        final FilterChain f1 = (new FilterChainBuilder(name1, new ContainsFilter("a", "a"), LinkType.AND))  //$NON-NLS-1$//$NON-NLS-2$
            .build();
        final FilterChain f2 = (new FilterChainBuilder(
            name2,
            new RegexpFilter("b", Pattern.compile("b")),                       //$NON-NLS-1$//$NON-NLS-2$
            LinkType.AND))
                .build();

        final String exp1 = new String(name1);
        final String exp2 = new String(name2);

        Assert.assertEquals(exp1, f1.getName());
        Assert.assertEquals(exp1, f1.getName());
        Assert.assertNotEquals(null, f1.getName());
        Assert.assertNotEquals("", f1.getName()); //$NON-NLS-1$

        Assert.assertEquals(exp2, f2.getName());
        Assert.assertEquals(exp2, f2.getName());
        Assert.assertNotEquals(null, f2.getName());
        Assert.assertNotEquals("", f2.getName()); //$NON-NLS-1$
    }

    /**
     * Test method for {@link java.lang.Object#toString()}.
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testToString() {
        final ContainsFilter a = new ContainsFilter("a", "a");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter b = new ContainsFilter("b", "b");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter c = new ContainsFilter("c", "c");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter d = new ContainsFilter("d", "d");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter e = new ContainsFilter("e", "e");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter f = new ContainsFilter("f", "f");  //$NON-NLS-1$//$NON-NLS-2$

        // a - d - f
        final FilterChain f1 = (new FilterChainBuilder("f1", a, LinkType.AND)).and(d).and(f).build(); //$NON-NLS-1$

        // a - d(OR) - f
        // effectively equal to : a - d - f
        final FilterChain f2 = (new FilterChainBuilder("f2", a, LinkType.AND)).or(d).and(f).build(); //$NON-NLS-1$

        // b\
        // c - ef
        // d/
        final FilterChain f3 = (new FilterChainBuilder("f3", b, LinkType.OR)) //$NON-NLS-1$
            .or(c)
            .or(d)
            .and(new ContainsFilter("ef", "ef"))  //$NON-NLS-1$//$NON-NLS-2$
            .build();

        // a \....... / f
        // ... c - e .
        // b /........\ d
        final FilterChain f4 = (new FilterChainBuilder("f4", a, LinkType.OR)).or(b).and(c).and(e).or(f).or(d).build(); //$NON-NLS-1$

        // a - b - f4
        final FilterChain f5 = (new FilterChainBuilder("f5", a, LinkType.AND)).and(b).and(f4).build(); //$NON-NLS-1$

        final String exp1 = "{name=[f1],numOfJoints=[3]}"; //$NON-NLS-1$
        final String exp2 = "{name=[f2],numOfJoints=[3]}"; //$NON-NLS-1$
        final String exp3 = "{name=[f3],numOfJoints=[4]}"; //$NON-NLS-1$
        final String exp4 = "{name=[f4],numOfJoints=[6]}"; //$NON-NLS-1$
        final String exp5 = "{name=[f5],numOfJoints=[8]}"; //$NON-NLS-1$

        Assert.assertEquals(exp1, f1.toString());
        Assert.assertEquals(exp1, f1.toString());
        Assert.assertNotEquals(null, f1.toString());
        Assert.assertNotEquals("", f1.toString()); //$NON-NLS-1$

        Assert.assertEquals(exp2, f2.toString());
        Assert.assertEquals(exp2, f2.toString());
        Assert.assertNotEquals(null, f2.toString());
        Assert.assertNotEquals("", f2.toString()); //$NON-NLS-1$

        Assert.assertEquals(exp3, f3.toString());
        Assert.assertEquals(exp3, f3.toString());
        Assert.assertNotEquals(null, f3.toString());
        Assert.assertNotEquals("", f3.toString()); //$NON-NLS-1$

        Assert.assertEquals(exp4, f4.toString());
        Assert.assertEquals(exp4, f4.toString());
        Assert.assertNotEquals(null, f4.toString());
        Assert.assertNotEquals("", f4.toString()); //$NON-NLS-1$

        Assert.assertEquals(exp5, f5.toString());
        Assert.assertEquals(exp5, f5.toString());
        Assert.assertNotEquals(null, f5.toString());
        Assert.assertNotEquals("", f5.toString()); //$NON-NLS-1$
    }

    /**
     * Test method for {@link FilterChainBuilder#isValid} method.
     * <br/><b>Created on:</b> <i>11:59:54 PM Jul 31, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testIsValid() {
        final ContainsFilter a = new ContainsFilter("a", "a");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter b = new ContainsFilter("b", "b");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter c = new ContainsFilter("c", "c");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter d = new ContainsFilter("d", "d");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter e = new ContainsFilter("e", "e");  //$NON-NLS-1$//$NON-NLS-2$
        final ContainsFilter f = new ContainsFilter("f", "f");  //$NON-NLS-1$//$NON-NLS-2$

        // a - d - f
        final FilterChainBuilder b1 = (new FilterChainBuilder("f1", a, LinkType.AND)).and(d).and(f); //$NON-NLS-1$
        Assert.assertTrue(b1.isValid());
        @SuppressWarnings("unused") final FilterChain f1 = b1.build();

        // a - d(OR) - f
        // effectively equal to : a - d - f
        final FilterChainBuilder b2 = (new FilterChainBuilder("f2", a, LinkType.AND)).or(d).and(f); //$NON-NLS-1$
        Assert.assertTrue(b2.isValid());
        @SuppressWarnings("unused") final FilterChain f2 = b2.build();

        // a \....... / f
        // ... c - e .
        // b /........\ d
        final FilterChainBuilder b3 = (new FilterChainBuilder("f3", a, LinkType.OR)).or(b).and(c).and(e).or(f).or(d); //$NON-NLS-1$
        Assert.assertTrue(b3.isValid());
        final FilterChain f3 = b3.build();

        // a - b - f3
        final FilterChainBuilder b4 = (new FilterChainBuilder("f4", a, LinkType.AND)).and(b).and(f3); //$NON-NLS-1$
        Assert.assertTrue(b4.isValid());
        @SuppressWarnings("unused") final FilterChain f4 = b4.build();
    }

}
