package dburyak.logmist.model;


import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Assert;
import org.junit.Test;


/**
 * Project : logmist.<br/>
 * JUnit test for {@link ContainsFilter} class.
 * <br/><b>Created on:</b> <i>12:00:22 AM Jul 30, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public class TestContainsFilter {

    /**
     * Static source data for testing.
     * <br/><b>Created on:</b> <i>12:10:30 AM Jul 30, 2015</i>
     */
    private static final LogEntry[] LOGS_STATIC =
        { new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4), "abcdef"),                                                                      //$NON-NLS-1$
        new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 5), "acdef"),                                                                      //$NON-NLS-1$
        new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 6), "adef"),                                                                    //$NON-NLS-1$
        new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 7), "aef"),                                                                    //$NON-NLS-1$
        new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 8), "af"),                                                                     //$NON-NLS-1$
        new LogEntry(LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 9), "a"),  //$NON-NLS-1$
        };


    /**
     * Test method for {@link dburyak.logmist.model.ContainsFilter#predicateToString()}.
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testPredicateToString() {
        final ContainsFilter f1 = new ContainsFilter("f1", "ab"); //$NON-NLS-1$ //$NON-NLS-2$
        final String exp1 = "{subStr=[ab],ignoreCase=[false]}"; //$NON-NLS-1$
        Assert.assertEquals(exp1, f1.predicateToString());
        Assert.assertNotEquals(null, f1.predicateToString());

        final ContainsFilter f2 = new ContainsFilter("f2", "aBcDEF", true); //$NON-NLS-1$ //$NON-NLS-2$
        final String exp2 = "{subStr=[aBcDEF],ignoreCase=[true]}"; //$NON-NLS-1$
        Assert.assertEquals(exp2, f2.predicateToString());
        Assert.assertNotEquals(null, f2.predicateToString());
    }

    /**
     * Test method for
     * {@link dburyak.logmist.model.ContainsFilter#ContainsFilter(java.lang.String, java.lang.String, boolean)}.
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testContainsFilterStringStringBoolean() {
        @SuppressWarnings("unused") final ContainsFilter f1 = new ContainsFilter("f1", "abc", false);   //$NON-NLS-1$//$NON-NLS-2$
        @SuppressWarnings("unused") final ContainsFilter f2 = new ContainsFilter("f2", "acFE", true);   //$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     * Test method for {@link ContainsFilter#ContainsFilter(String, String, boolean)} invalid argument 1.
     * <br/><b>Created on:</b> <i>12:26:14 AM Jul 30, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testContainsFilterStringStringBooleanFail1() {
        @SuppressWarnings("unused") final ContainsFilter f1 = new ContainsFilter(null, "abCD", true); //$NON-NLS-1$
        Assert.fail();
    }

    /**
     * Test method for {@link ContainsFilter#ContainsFilter(String, String, boolean)} invalid argument 1 v2.
     * <br/><b>Created on:</b> <i>12:38:30 AM Jul 30, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testContainsFilterStringStringBooleanFail1_v2() {
        @SuppressWarnings("unused") final ContainsFilter f1 = new ContainsFilter("", "asdfe", true);   //$NON-NLS-1$//$NON-NLS-2$
        Assert.fail();
    }

    /**
     * Test method for {@link ContainsFilter#ContainsFilter(String, String, boolean)} invalid argument 2.
     * <br/><b>Created on:</b> <i>12:29:06 AM Jul 30, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testContainsFilterStringStringBooleanFail2() {
        @SuppressWarnings("unused") final ContainsFilter f1 = new ContainsFilter("f1", null, false); //$NON-NLS-1$
        Assert.fail();
    }

    /**
     * Test method for {@link ContainsFilter#ContainsFilter(String, String, boolean)} invalid argument 2 v2.
     * <br/><b>Created on:</b> <i>12:57:11 AM Jul 30, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testContainsFilterStringStringBooleanFail2_v2() {
        @SuppressWarnings("unused") final ContainsFilter f1 = new ContainsFilter("f1", "", true);  //$NON-NLS-1$//$NON-NLS-2$
        Assert.fail();
    }

    /**
     * Test method for {@link ContainsFilter#ContainsFilter(String, String, boolean)} invalid argumetns 1 and 2.
     * <br/><b>Created on:</b> <i>12:31:08 AM Jul 30, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testContainsFilterStringStringBooleanFail1_2() {
        @SuppressWarnings("unused") final ContainsFilter f1 = new ContainsFilter(null, null, false);
        Assert.fail();
    }

    /**
     * Test method for {@link ContainsFilter#ContainsFilter(String, String, boolean)} invalid arguments 1 and 2 v2.
     * <br/><b>Created on:</b> <i>1:09:33 AM Jul 30, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testContainsFilterStringStringBooleanFail1_2_v2() {
        @SuppressWarnings("unused") final ContainsFilter f1 = new ContainsFilter("", "", true);  //$NON-NLS-1$//$NON-NLS-2$
        Assert.fail();
    }

    /**
     * Test method for {@link dburyak.logmist.model.ContainsFilter#ContainsFilter(java.lang.String, java.lang.String)}.
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testContainsFilterStringString() {
        @SuppressWarnings("unused") final ContainsFilter f1 = new ContainsFilter("f1", "abcDeF");   //$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     * Test method for {@link ContainsFilter#ContainsFilter(String, String)} invalid argument 1.
     * <br/><b>Created on:</b> <i>12:35:08 AM Jul 30, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testContainsFilterStringStringFail1() {
        @SuppressWarnings("unused") final ContainsFilter f1 = new ContainsFilter(null, "abcEF"); //$NON-NLS-1$
        Assert.fail();
    }

    /**
     * Test method for {@link ContainsFilter#ContainsFilter(String, String)} invalid argument 1 v2.
     * <br/><b>Created on:</b> <i>1:14:45 AM Jul 30, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testContainsFilterStringStringFail1_v2() {
        @SuppressWarnings("unused") final ContainsFilter f1 = new ContainsFilter("", "abcaEfS");  //$NON-NLS-1$//$NON-NLS-2$
        Assert.fail();
    }

    /**
     * Test method for {@link ContainsFilter#ContainsFilter(String, String)} invalid argument 2.
     * <br/><b>Created on:</b> <i>1:16:25 AM Jul 30, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testContainsFilterStringStringFail2() {
        @SuppressWarnings("unused") final ContainsFilter f1 = new ContainsFilter("f1", null); //$NON-NLS-1$
        Assert.fail();
    }

    /**
     * Test method for {@link ContainsFilter#ContainsFilter(String, String)} invalid argument 2 v2.
     * <br/><b>Created on:</b> <i>1:18:01 AM Jul 30, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testContainsFilterStringStringFail2_v2() {
        @SuppressWarnings("unused") final ContainsFilter f1 = new ContainsFilter("f1", "");   //$NON-NLS-1$//$NON-NLS-2$
        Assert.fail();
    }

    /**
     * Test method for {@link ContainsFilter#ContainsFilter(String, String)} invalid arguments 1 and 2.
     * <br/><b>Created on:</b> <i>1:19:45 AM Jul 30, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testContainsFilterStringString1_2() {
        @SuppressWarnings("unused") final ContainsFilter f1 = new ContainsFilter(null, null);
        Assert.fail();
    }

    /**
     * Test method for {@link ContainsFilter#ContainsFilter(String, String)} invalid arguments 1 and 2 v2.
     * <br/><b>Created on:</b> <i>1:21:16 AM Jul 30, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testContainsFilterStringString1_2_v2() {
        @SuppressWarnings("unused") final ContainsFilter f1 = new ContainsFilter("", "");  //$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     * Test method for {@link ContainsFilter#getName()}.
     * <br/><b>Created on:</b> <i>1:35:16 AM Jul 30, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testGetName() {
        final String name1 = "f1"; //$NON-NLS-1$
        final String name2 = "f2"; //$NON-NLS-1$
        final String name3 = "f3"; //$NON-NLS-1$
        final ContainsFilter f1 = new ContainsFilter(name1, "abcDeFG"); //$NON-NLS-1$
        final ContainsFilter f2 = new ContainsFilter(name2, "abbDweer", true); //$NON-NLS-1$
        final ContainsFilter f3 = new ContainsFilter(name3, "asdfIOpopPNdijs", false); //$NON-NLS-1$

        final String exp1 = new String(name1);
        final String exp2 = new String(name2);
        final String exp3 = new String(name3);

        Assert.assertEquals(exp1, f1.getName());
        Assert.assertEquals(exp1, f1.getName());
        Assert.assertNotEquals(null, f1.getName());
        Assert.assertNotEquals("", f1.getName()); //$NON-NLS-1$

        Assert.assertEquals(exp2, f2.getName());
        Assert.assertEquals(exp2, f2.getName());
        Assert.assertNotEquals(null, f2.getName());
        Assert.assertNotEquals("", f2.getName()); //$NON-NLS-1$

        Assert.assertEquals(exp3, f3.getName());
        Assert.assertEquals(exp3, f3.getName());
        Assert.assertNotEquals(null, f2.getName());
        Assert.assertNotEquals("", f2.getName()); //$NON-NLS-1$
    }

    /**
     * Test method for {@link ContainsFilter#toString()}.
     * <br/><b>Created on:</b> <i>1:36:23 AM Jul 30, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testToString() {
        final String name1 = "f1"; //$NON-NLS-1$
        final String name2 = "f2"; //$NON-NLS-1$
        final String name3 = "f3"; //$NON-NLS-1$
        final ContainsFilter f1 = new ContainsFilter(name1, "abcDeFG"); //$NON-NLS-1$
        final ContainsFilter f2 = new ContainsFilter(name2, "abbDweer", true); //$NON-NLS-1$
        final ContainsFilter f3 = new ContainsFilter(name3, "asdfIOpopPNdijs", false); //$NON-NLS-1$

        final String exp1 = "{name=[f1],type=[ContainsFilter],predicate=[{subStr=[abcDeFG],ignoreCase=[false]}]}"; //$NON-NLS-1$
        final String exp2 = "{name=[f2],type=[ContainsFilter],predicate=[{subStr=[abbDweer],ignoreCase=[true]}]}"; //$NON-NLS-1$
        final String exp3 =
            "{name=[f3],type=[ContainsFilter],predicate=[{subStr=[asdfIOpopPNdijs],ignoreCase=[false]}]}"; //$NON-NLS-1$

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
        Assert.assertNotEquals(null, f2.toString());
        Assert.assertNotEquals("", f2.toString()); //$NON-NLS-1$

    }

    @Test
    public final void testAccept() {

    }

}
