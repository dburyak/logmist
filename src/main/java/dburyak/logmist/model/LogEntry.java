package dburyak.logmist.model;


import java.time.Instant;

import javax.annotation.concurrent.Immutable;

import dburyak.jtools.AssertConst;


/**
 * Project : logmist.<br/>
 * Represents one record from msg file. <br/>
 * <b>Created on:</b> <i>12:12:39 PM Jul 15, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
@net.jcip.annotations.Immutable
public final class LogEntry {
    
    
    /**
     * Time stamp of log entry. <br/>
     * <b>Created on:</b> <i>11:18:19 AM Jul 16, 2015</i>
     */
    private final Instant time;
    
    
    /**
     * Message of log entry (without time stamp). <br/>
     * <b>Created on:</b> <i>11:19:02 AM Jul 16, 2015</i>
     */
    private final String msg;
    
    
    /**
     * Validator for time. Non-null time is valid.<br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE<br/>
     * <b>Created on:</b> <i>11:42:09 AM Jul 16, 2015</i>
     * 
     * @param time
     *            time stamp for log entry to be validated
     * @throws IllegalArgumentException
     *             if time is invalid
     */
    private static final void validateTime(final Instant time) {
        if (time == null) {
            throw new IllegalArgumentException(AssertConst.ASRT_NULL_ARG.toString());
        }
    }
    
    /**
     * Validator for msg. Non-null msg is valid. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>11:44:09 AM Jul 16, 2015</i>
     * 
     * @param msg
     *            message for log entry to be validated
     * @throws IllegalArgumentException
     *             if msg is invalid
     */
    private static final void validateMsg(final String msg) {
        if (msg == null) {
            throw new IllegalArgumentException(AssertConst.ASRT_NULL_ARG.toString());
        }
    }
    
    /**
     * Constructor for class : [logmist] dburyak.logmist.model.LogEntry.<br/>
     * <br/>
     * <b>PRE-conditions:</b> non-null args <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>11:23:46 AM Jul 16, 2015</i>
     * 
     * @param time
     *            time stamp of log entry
     * @param msg
     *            message of log entry (without time stamp)
     */
    public LogEntry(final Instant time, final String msg) {
        // check pre-conditions
        validateTime(time);
        validateMsg(msg);
        
        this.time = time;
        this.msg = msg;
    }
    
    /**
     * Getter method.<br/>
     * Get time mark of this log entry. <br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>11:21:11 AM Jul 16, 2015</i>
     * 
     * @return the time
     */
    public final Instant getTime() {
        return time;
    }
    
    /**
     * Getter method.<br/>
     * Get message of this log entry (without time stamp). <br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>11:21:11 AM Jul 16, 2015</i>
     * 
     * @return the msg
     */
    public final String getMsg() {
        return msg;
    }
    
}
