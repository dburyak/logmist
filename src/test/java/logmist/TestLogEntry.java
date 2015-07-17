package logmist;


import static org.junit.Assert.fail;

import java.time.Instant;

import org.junit.Test;

import dburyak.logmist.model.LogEntry;


/**
 * Project : logmist.<br/>
 * JUnit test for {@link LogEntry}.
 * <br/>
 * <b>Created on:</b> <i>11:41:57 AM Jul 17, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public class TestLogEntry {
    
    /**
     * Test method for {@link dburyak.logmist.model.LogEntry#LogEntry(java.time.Instant, java.lang.String)}.
     */
    @Test
    public final void testLogEntry() {
        final Instant t1 = Instant.ofEpochMilli(100000L);
        final Instant t2 = Instant.ofEpochMilli(100001L);
        final Instant t3 = Instant.ofEpochMilli(100002L);
        
        final String m1 = "message 1";
        final String m2 = "message 2";
        final String m3 = "message 3";
    }
    
    /**
     * Test method for {@link dburyak.logmist.model.LogEntry#getTime()}.
     */
    @Test
    public final void testGetTime() {
        fail("Not yet implemented");
    }
    
    /**
     * Test method for {@link dburyak.logmist.model.LogEntry#getMsg()}.
     */
    @Test
    public final void testGetMsg() {
        fail("Not yet implemented");
    }
    
}
