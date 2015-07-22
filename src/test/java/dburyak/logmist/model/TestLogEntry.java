package dburyak.logmist.model;


import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Assert;
import org.junit.Test;


/**
 * Project : logmist.<br/>
 * JUnit test for {@link LogEntry} class.
 * <br/>
 * <b>Created on:</b> <i>11:41:57 AM Jul 17, 2015</i>
 *
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class TestLogEntry {
    
    /**
     * Test constructor with normal parameters.
     * Test method for {@link dburyak.logmist.model.LogEntry#LogEntry(LocalDateTime, java.lang.String)}.
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testConstructor1NormalParams() {
        final LocalDateTime t1 = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
        final String m1 = "message 1"; //$NON-NLS-1$
        @SuppressWarnings("unused") final LogEntry le1 = new LogEntry(t1, m1);
    }

    /**
     * Test constructor 1 with all illegal parameters. <br/>
     * Test method for {@link LogEntry#LogEntry(LocalDateTime, String)}
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testConstructor1InvalidAll() {
        final LocalDateTime t1 = null;
        final String m1 = null;
        @SuppressWarnings("unused") final LogEntry le1 = new LogEntry(t1, m1);
        Assert.fail();
    }
    
    /**
     * Test constructor 1 with first illegal parameter. <br/>
     * Test method for {@link LogEntry#LogEntry(LocalDateTime, String)} <br/>
     * <b>Created on:</b> <i>11:22:12 AM Jul 19, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testConstructor1InvalidFirst() {
        final LocalDateTime t1 = null;
        final String m1 = "test"; //$NON-NLS-1$
        @SuppressWarnings("unused") final LogEntry le1 = new LogEntry(t1, m1);
        Assert.fail();
    }

    /**
     * Test constructor 1 with second illegal parameter. <br/>
     * Test method for {@link LogEntry#LogEntry(LocalDateTime, String)} <br/>
     * <b>Created on:</b> <i>11:24:57 AM Jul 19, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testConstructor1InvalidSecond() {
        final LocalDateTime t1 = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
        final String m1 = null;
        @SuppressWarnings("unused") final LogEntry le1 = new LogEntry(t1, m1);
        Assert.fail();
    }
    
    /**
     * Test constructor 2 with all legal parameters. <br/>
     * Test method for {@link LogEntry#LogEntry(LocalDateTime, String, String)} <br/>
     * <b>Created on:</b> <i>11:31:57 AM Jul 19, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testConstructor2NormalParams() {
        final LocalDateTime t1 = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
        final String m1 = "test"; //$NON-NLS-1$
        final String mFull1 = "Jan 1 00:00:00 - test"; //$NON-NLS-1$
        @SuppressWarnings("unused") final LogEntry le1 = new LogEntry(t1, m1, mFull1);
    }
    
    /**
     * Test constructor 2 with fist illegal parameter. <br/>
     * Test method of {@link LogEntry#LogEntry(LocalDateTime, String, String)} <br/>
     * <b>Created on:</b> <i>11:36:59 AM Jul 19, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testConstructor2InvalidFirst() {
        final LocalDateTime t1 = null;
        final String m1 = "test"; //$NON-NLS-1$
        final String mFull1 = "Jan 1 00:00:00 - test"; //$NON-NLS-1$
        @SuppressWarnings("unused") final LogEntry le1 = new LogEntry(t1, m1, mFull1);
        Assert.fail();
    }
    
    /**
     * Test constructor 2 with second illegal parameter. <br/>
     * Test method of {@link LogEntry#LogEntry(LocalDateTime, String, String)} <br/>
     * <b>Created on:</b> <i>11:39:52 AM Jul 19, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testConstructor2InvalidSecond() {
        final LocalDateTime t1 = LocalDateTime.of(1970, Month.JANUARY, 1, 1, 2, 3);
        final String m1 = null;
        final String mFull1 = "Jan 1 00:00:00 - test"; //$NON-NLS-1$
        @SuppressWarnings("unused") final LogEntry le1 = new LogEntry(t1, m1, mFull1);
        Assert.fail();
    }


    /**
     * Test constructor 2 with third illegal argument. <br/>
     * Test method for {@link LogEntry#LogEntry(LocalDateTime, String, String)} <br/>
     * <b>Created on:</b> <i>12:12:51 PM Jul 19, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testConstructor2InvalidThird() {
        final LocalDateTime t1 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String m1 = "test";  //$NON-NLS-1$
        final String mFull = null;
        @SuppressWarnings("unused") final LogEntry le1 = new LogEntry(t1, m1, mFull);
        Assert.fail();
    }

    /**
     * Test constructor 2 with first and second illegal arguments. <br/>
     * Test method for {@link LogEntry#LogEntry(LocalDateTime, String, String)} <br/>
     * <b>Created on:</b> <i>12:15:11 PM Jul 19, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testConstructor2TwoInvalid1() {
        final LocalDateTime t1 = null;
        final String m1 = null;
        final String mFull = "Jan 1 00:00:00 - test"; //$NON-NLS-1$
        @SuppressWarnings("unused") final LogEntry le1 = new LogEntry(t1, m1, mFull);
        Assert.fail();
    }

    /**
     * Test constructor 2 with first and third illegal arguments. <br/>
     * Test method for {@link LogEntry#LogEntry(LocalDateTime, String, String)} <br/>
     * <b>Created on:</b> <i>12:17:23 PM Jul 19, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testConstructor2TwoInvalid2() {
        final LocalDateTime t1 = null;
        final String m1 = "test"; //$NON-NLS-1$
        final String mFull = null;
        @SuppressWarnings("unused") final LogEntry le1 = new LogEntry(t1, m1, mFull);
        Assert.fail();
    }
    
    /**
     * Test constructor 2 with second and third invalid argumets. <br/>
     * Test method for {@link LogEntry#LogEntry(LocalDateTime, String, String)} <br/>
     * <b>Created on:</b> <i>12:20:04 PM Jul 19, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = IllegalArgumentException.class)
    public final void testConstructor2TwoInvalid3() {
        final LocalDateTime t1 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String m1 = null;
        final String mFull1 = null;
        @SuppressWarnings("unused") final LogEntry le1 = new LogEntry(t1, m1, mFull1);
        Assert.fail();
    }
    
    /**
     * Test getter. <br/>
     * Test method for {@link dburyak.logmist.model.LogEntry#getTime()}. <br/>
     * <b>Created on:</b> <i>12:27:18 PM Jul 19, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testGetTime() {
        final LocalDateTime t1 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String m1 = "test"; //$NON-NLS-1$
        final LogEntry le1 = new LogEntry(t1, m1);

        final LocalDateTime t2 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        Assert.assertEquals(t2, le1.getTime());
        Assert.assertEquals(le1.getTime(), le1.getTime());
        Assert.assertNotEquals(null, le1.getTime());
    }
    
    /**
     * Test getter. <br/>
     * Test method for {@link dburyak.logmist.model.LogEntry#getMsg()}. <br/>
     * <b>Created on:</b> <i>7:20:15 PM Jul 19, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testGetMsg() {
        final LocalDateTime t1 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String m1 = "test"; //$NON-NLS-1$
        final LogEntry le1 = new LogEntry(t1, m1);

        final String expected = new String(m1);
        Assert.assertEquals(expected, le1.getMsg());
        Assert.assertEquals(le1.getMsg(), le1.getMsg());
        Assert.assertNotEquals(null, le1.getMsg());
    }
    
    /**
     * Test getter. <br/>
     * Test method for {@link LogEntry#getMsgFull()} <br/>
     * <b>Created on:</b> <i>7:39:01 PM Jul 19, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testGetMsgFull() {
        // test generated full message
        final LocalDateTime t1 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String m1 = "test"; //$NON-NLS-1$
        final LogEntry le1 = new LogEntry(t1, m1);
        final String exp1 = "1970-01-01T02:03:04 - test"; //$NON-NLS-1$
        
        // test stored full message
        final LocalDateTime t2 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 5);
        final String m2 = "test"; //$NON-NLS-1$
        final String mFull2 = "Jan 1 02:03:05 - test"; //$NON-NLS-1$
        final LogEntry le2 = new LogEntry(t2, m2, mFull2);
        final String exp2 = new String(mFull2);
        
        Assert.assertEquals(exp1, le1.getMsgFull());
        Assert.assertEquals(le1.getMsgFull(), le1.getMsgFull());
        Assert.assertNotEquals(null, le1.getMsgFull());
        
        Assert.assertEquals(exp2, le2.getMsgFull());
        Assert.assertEquals(le2.getMsgFull(), le2.getMsgFull());
        Assert.assertNotEquals(null, le2.getMsgFull());
    }

    /**
     * Test lower.
     * Test method for {@link LogEntry#compareTo(LogEntry)}. <br/>
     * <b>Created on:</b> <i>12:30:55 AM Jul 20, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testCompareTo1() {
        final LocalDateTime tEntry = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String mEntry = "test1"; //$NON-NLS-1$
        final String mEntryFull = "aa003"; //$NON-NLS-1$
        final LogEntry entry = new LogEntry(tEntry, mEntry, mEntryFull);

        final LocalDateTime tLower1 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 3);
        final String mLower1 = "pest3"; //$NON-NLS-1$
        final LogEntry lower1 = new LogEntry(tLower1, mLower1);

        final LocalDateTime tLower2 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String mLower2 = "test0"; //$NON-NLS-1$
        final LogEntry lower2 = new LogEntry(tLower2, mLower2);

        final LocalDateTime tLower3 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 2);
        final String mLower3 = "test0"; //$NON-NLS-1$
        final LogEntry lower3 = new LogEntry(tLower3, mLower3);

        final LocalDateTime tLower4 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String mLower4 = "test1"; //$NON-NLS-1$
        final String mLowerFull4 = "aa002"; //$NON-NLS-1$
        final LogEntry lower4 = new LogEntry(tLower4, mLower4, mLowerFull4);

        Assert.assertTrue(lower1.compareTo(entry) < 0);
        Assert.assertTrue(lower2.compareTo(entry) < 0);
        Assert.assertTrue(lower3.compareTo(entry) < 0);
        Assert.assertTrue(lower4.compareTo(entry) < 0);
    }

    /**
     * Test equal.
     * Test method for {@link LogEntry#compareTo(LogEntry)}. <br/>
     * <b>Created on:</b> <i>12:30:55 AM Jul 20, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testCompareTo2() {
        // test generated full message
        final LocalDateTime t1 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String m1 = "test1"; //$NON-NLS-1$
        final LogEntry entry1Gen = new LogEntry(t1, m1);

        final LocalDateTime t2 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String m2 = "test1";  //$NON-NLS-1$
        final LogEntry entry2Gen = new LogEntry(t2, m2);

        Assert.assertTrue(entry1Gen.compareTo(entry2Gen) == 0);
        
        // test explicit full message
        final LocalDateTime t3 = LocalDateTime.of(1970, Month.FEBRUARY, 2, 3, 4, 5);
        final String m3 = "test2"; //$NON-NLS-1$
        final String mFull3 = "full2"; //$NON-NLS-1$
        final LogEntry entry3Explct = new LogEntry(t3, m3, mFull3);
        
        final LocalDateTime t4 = LocalDateTime.of(1970, Month.FEBRUARY, 2, 3, 4, 5);
        final String m4 = "test2"; //$NON-NLS-1$
        final String mFull4 = "full2"; //$NON-NLS-1$
        final LogEntry entry4Explct = new LogEntry(t4, m4, mFull4);
        
        Assert.assertTrue(entry3Explct.compareTo(entry4Explct) == 0);
    }
    
    /**
     * Test greater.
     * Test method for {@link LogEntry#compareTo(LogEntry)}. <br/>
     * <b>Created on:</b> <i>12:30:55 AM Jul 20, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testCompareTo3() {
        final LocalDateTime tEntry = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String mEntry = "test1"; //$NON-NLS-1$
        final String mEntryFull = "aa003"; //$NON-NLS-1$
        final LogEntry entry = new LogEntry(tEntry, mEntry, mEntryFull);

        final LocalDateTime tGreater1 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 5);
        final String mGreater1 = "test0"; //$NON-NLS-1$
        final LogEntry greater1 = new LogEntry(tGreater1, mGreater1);
        
        final LocalDateTime tGreater2 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String mGreater2 = "test2"; //$NON-NLS-1$
        final LogEntry greater2 = new LogEntry(tGreater2, mGreater2);
        
        final LocalDateTime tGreater3 = LocalDateTime.of(1971, Month.JANUARY, 1, 2, 3, 4);
        final String mGreater3 = "xxxxxx"; //$NON-NLS-1$
        final LogEntry greater3 = new LogEntry(tGreater3, mGreater3);

        final LocalDateTime tGreater4 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String mGreater4 = "test1"; //$NON-NLS-1$
        final String mFullGreater4 = "bb114"; //$NON-NLS-1$
        final LogEntry greater4 = new LogEntry(tGreater4, mGreater4, mFullGreater4);

        Assert.assertTrue(greater1.compareTo(entry) > 0);
        Assert.assertTrue(greater2.compareTo(entry) > 0);
        Assert.assertTrue(greater3.compareTo(entry) > 0);
        Assert.assertTrue(greater4.compareTo(entry) > 0);
    }

    /**
     * Test null.
     * Test method for {@link LogEntry#compareTo(LogEntry)}. <br/>
     * <b>Created on:</b> <i>12:30:55 AM Jul 20, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test(expected = NullPointerException.class)
    public final void testCompareTo4() {
        final LocalDateTime t1 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String m1 = "test1"; //$NON-NLS-1$
        final LogEntry entry = new LogEntry(t1, m1);
        
        entry.compareTo(null);
        Assert.fail();
    }

    // hashCode() testing discarded intentionally
    
    /**
     * Test method for {@link LogEntry#equals(Object)} .<br/>
     * <b>Created on:</b> <i>11:15:34 PM Jul 20, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testEquals() {
        final LocalDateTime t1 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String m1 = "test1"; //$NON-NLS-1$
        final String mFull1 = "aa00"; //$NON-NLS-1$
        final LogEntry le1 = new LogEntry(t1, m1, mFull1);
        
        final LocalDateTime tEq = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String mEq = "test1"; //$NON-NLS-1$
        final String mFullEq = "aa00"; //$NON-NLS-1$
        final LogEntry equal = new LogEntry(tEq, mEq, mFullEq);

        final LocalDateTime tUneq1 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 5);
        final String mUneq1 = "test1"; //$NON-NLS-1$
        final String mUneqFull1 = "aa00"; //$NON-NLS-1$
        final LogEntry unequal1 = new LogEntry(tUneq1, mUneq1, mUneqFull1);
        
        final LocalDateTime tUneq2 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String mUneq2 = "test2"; //$NON-NLS-1$
        final String mUneqFull2 = "aa00"; //$NON-NLS-1$
        final LogEntry unequal2 = new LogEntry(tUneq2, mUneq2, mUneqFull2);
        
        final LocalDateTime tUneq3 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String mUneq3 = "test1"; //$NON-NLS-1$
        final String mFullUneq3 = "bb11"; //$NON-NLS-1$
        final LogEntry unequal3 = new LogEntry(tUneq3, mUneq3, mFullUneq3);
        
        final LocalDateTime tUneq4 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 3);
        final String mUneq4 = "test2"; //$NON-NLS-1$
        final String mFullUneq4 = "aa00"; //$NON-NLS-1$
        final LogEntry unequal4 = new LogEntry(tUneq4, mUneq4, mFullUneq4);
        
        final LocalDateTime tUneq5 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 2);
        final String mUneq5 = "test1"; //$NON-NLS-1$
        final String mFullUneq5 = "bbv9"; //$NON-NLS-1$
        final LogEntry unequal5 = new LogEntry(tUneq5, mUneq5, mFullUneq5);
        
        final LocalDateTime tUneq6 = LocalDateTime.of(1970, Month.JANUARY, 1, 2, 3, 4);
        final String mUneq6 = "test8"; //$NON-NLS-1$
        final String mFullUneq6 = "cc73"; //$NON-NLS-1$
        final LogEntry unequal6 = new LogEntry(tUneq6, mUneq6, mFullUneq6);
        
        final LogEntry unequal7 = null;


        Assert.assertTrue(le1.equals(equal));
        Assert.assertTrue(equal.equals(le1));

        Assert.assertFalse(le1.equals(unequal1));
        Assert.assertFalse(unequal1.equals(le1));

        Assert.assertFalse(le1.equals(unequal2));
        Assert.assertFalse(unequal2.equals(le1));

        Assert.assertFalse(le1.equals(unequal3));
        Assert.assertFalse(unequal3.equals(le1));
        
        Assert.assertFalse(le1.equals(unequal4));
        Assert.assertFalse(unequal4.equals(le1));
        
        Assert.assertFalse(le1.equals(unequal5));
        Assert.assertFalse(unequal5.equals(le1));
        
        Assert.assertFalse(le1.equals(unequal6));
        Assert.assertFalse(unequal6.equals(le1));
        
        Assert.assertFalse(le1.equals(unequal7));
    }

    /**
     * Test method for {@link LogEntry#toString()}.<br/>
     * <b>Created on:</b> <i>12:19:44 AM Jul 21, 2015</i>
     */
    @SuppressWarnings("static-method")
    @Test
    public final void testToString() {
        // generated
        final LocalDateTime t1 = LocalDateTime.of(2015, Month.JULY, 21, 00, 42, 15);
        final String msg1 = "test1"; //$NON-NLS-1$
        final LogEntry entry1 = new LogEntry(t1, msg1);
        final String exp1 = "2015-07-21T00:42:15 - test1"; //$NON-NLS-1$
        Assert.assertEquals(exp1, entry1.toString());

        // manual
        final LocalDateTime t2 = LocalDateTime.of(2014, Month.JUNE, 4, 14, 15, 3);
        final String msg2 = "test2"; //$NON-NLS-1$
        final String msgFull2 = "Jun 4 14:15:03"; //$NON-NLS-1$
        final LogEntry entry2 = new LogEntry(t2, msg2, msgFull2);
        final String exp2 = new String(msgFull2);
        Assert.assertEquals(exp2, entry2.toString());

    }

}
