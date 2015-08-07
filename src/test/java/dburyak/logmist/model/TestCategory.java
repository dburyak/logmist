package dburyak.logmist.model;

import java.util.regex.Pattern;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Project : logmist.<br/>
 * JUnit test for {@link Category} class.
 * <br/><b>Created on:</b> <i>2:08:52 AM Aug 8, 2015</i>
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public class TestCategory {
    
    private IFilter filter1 = null;
    private IFilter filter2 = null;
    private Category cat1 = null;
    private Category cat2 = null;
    private String catName1 = null;
    private String catName2 = null;
    private String filtName1 = null;
    private String filtName2 = null;
    private String filtArg1 = null;
    private String filtArg2 = null;

    /**
         * One-time initialization before running this set of tests.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>2:08:53 AM Aug 8, 2015</i>
         * @throws java.lang.Exception
         */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // nothing to do here
    }

    /**
         * One time clean-up after running this set of test.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>2:08:53 AM Aug 8, 2015</i>
         * @throws java.lang.Exception
         */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // nothing to do here
    }

    /**
         * Initialization code for each test.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>2:08:53 AM Aug 8, 2015</i>
         * @throws java.lang.Exception
         */
    @SuppressWarnings("nls")
    @Before
    public void setUp() throws Exception {
        filtName1 = "f1";
        filtName2 = "f2";
        filtArg1 = "abc";
        filtArg2 = "def";
        catName1 = "cat1";
        catName2 = "cat2";
        
        filter1 = new RegexpFilter(filtName1, Pattern.compile(filtArg1));
        filter2 = new ContainsFilter(filtName2, filtArg2);
        
        cat1 = new Category(catName1, filter1);
        cat2 = new Category(catName2, filter2);
    }

    /**
         * Clean-up code after each test.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>2:08:53 AM Aug 8, 2015</i>
         * @throws java.lang.Exception
         */
    @After
    public void tearDown() throws Exception {
        filter1 = null;
        filter2 = null;
        cat1 = null;
        cat2 = null;
    }

    /** Test constructor
         * Test method for {@link dburyak.logmist.model.Category#Category(java.lang.String, dburyak.logmist.model.IFilter)}.
         */
    @SuppressWarnings({ "nls" })
    @Test
    public void testCategory() {
        final Category c1 = new Category("c1", filter1);
        final Category c2 = new Category("c2", filter2);
        Assert.assertNotEquals(null, c1);
        Assert.assertNotEquals(null, c2);
    }
    
    @SuppressWarnings("unused")
    @Test(expected = IllegalArgumentException.class)
    public final void testCategoryInvalid1() {
        final Category c1 = new Category(null, filter1);
        Assert.fail();
    }
    
    @SuppressWarnings({ "nls", "unused" })
    @Test(expected = IllegalArgumentException.class)
    public final void testCategoryInvalid1_v2() {
        final Category c1 = new Category("", filter1);
        Assert.fail();
    }
    
    @SuppressWarnings({ "nls", "unused", "static-method" })
    @Test(expected = IllegalArgumentException.class)
    public final void testCategoryInvalid2() {
        final Category c1 = new Category("c1", null);
        Assert.fail();
    }

    /**
         * Test method for {@link dburyak.logmist.model.Category#getName()}.
         */
    @Test
    public void testGetName() {
        final String exp1 = new String(catName1);
        final String exp2 = new String(catName2);
        
        Assert.assertEquals(exp1, cat1.getName());
        Assert.assertEquals(exp1, cat1.getName());
        Assert.assertEquals(exp2, cat2.getName());
        Assert.assertEquals(exp2, cat2.getName());
    }

    /**
         * Test method for {@link dburyak.logmist.model.Category#getFilter()}.
         */
    @Test
    public void testGetFilter() {
        Assert.assertEquals(cat1.getFilter(), filter1);
        Assert.assertEquals(cat1.getFilter(), filter1);
        
        Assert.assertEquals(cat2.getFilter(), filter2);
        Assert.assertEquals(cat2.getFilter(), filter2);
    }

    /**
         * Test method for {@link dburyak.logmist.model.Category#toString()}.
         */
    @SuppressWarnings("nls")
    @Test
    public void testToString() {
        final String exp1 = "{name=[cat1],filter=[{name=[f1],type=[RegexpFilter],predicate=[{pattern=[abc],ignoreCase=[false],fullMatch=[false]}]}]}";
        final String exp2 = "{name=[cat2],filter=[{name=[f2],type=[ContainsFilter],predicate=[{subStr=[def],ignoreCase=[false]}]}]}";
        
        Assert.assertEquals(exp1, cat1.toString());
        Assert.assertEquals(exp1, cat1.toString());
        
        Assert.assertEquals(exp2, cat2.toString());
        Assert.assertEquals(exp2, cat2.toString());
    }

}
