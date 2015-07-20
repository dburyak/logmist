package dburyak.logmist.model;


import java.time.LocalDateTime;

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
public final class LogEntry implements Comparable<LogEntry> {

    /**
     * Separator to be used when there is a need to combine time and msg (to get fullMsg).
     * <br/>
     * <b>Created on:</b> <i>11:04:47 AM Jul 19, 2015</i>
     */
    private static final String SYNTAX_TIME_MSG_SEPARATOR = " - "; //$NON-NLS-1$
    
    
    /**
     * Time stamp of log entry. <br/>
     * <b>Created on:</b> <i>11:18:19 AM Jul 16, 2015</i>
     */
    private final LocalDateTime time;
    
    
    /**
     * Message of log entry (without time stamp). <br/>
     * <b>Created on:</b> <i>11:19:02 AM Jul 16, 2015</i>
     */
    private final String msg;
    
    /**
     * Full message for this log (msg with time-stamp). Usually, when log entry is constructed by parsing log file, this
     * field holds original string from log file. <br/>
     * <b>Created on:</b> <i>8:42:13 PM Jul 18, 2015</i>
     */
    private final String msgFull;
    
    
    /**
     * Validator for time. Non-null time is valid.<br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE<br/>
     * <b>Created on:</b> <i>11:42:09 AM Jul 16, 2015</i>
     *
     * @param time
     *            time stamp for log entry to be validated
     * @return true if input is valid
     * @throws IllegalArgumentException
     *             if time is invalid
     */
    private static final boolean validateTime(final LocalDateTime time) {
        if (time == null) {
            throw new IllegalArgumentException(AssertConst.ASRT_NULL_ARG.toString());
        }
        return true;
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
     * @return true if input if valid
     * @throws IllegalArgumentException
     *             if msg is invalid
     */
    private static final boolean validateMsg(final String msg) {
        if (msg == null) {
            throw new IllegalArgumentException(AssertConst.ASRT_NULL_ARG.toString());
        }
        return true;
    }
    
    /**
     * Validator for msgFull. Non-null msgFull is valid. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>8:45:51 PM Jul 18, 2015</i>
     *
     * @param msgFull
     *            full message for log entry
     * @return true if input is valid
     * @throws IllegalArgumentException
     *             if msgFull is invalid
     */
    private static final boolean validateMsgFull(final String msgFull) {
        if (msgFull == null) {
            throw new IllegalArgumentException(AssertConst.ASRT_NULL_ARG.toString());
        }
        return true;
    }

    /**
     * Create full message from time and msg. Usually, this method should be used when log entry is created without full
     * message, i.e. user provides only time and msg. <br/>
     * <b>PRE-conditions:</b> valid time and msg (validators are used) <br/>
     * <b>POST-conditions:</b> valid msgFull <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>8:52:56 PM Jul 18, 2015</i>
     *
     * @param time
     *            time stamp of log entry
     * @param msg
     *            message of log log entry (without stamp)
     * @return full message for log entry constructed from given params
     * @throws IllegalArgumentException
     *             if any of parameters is illegal
     */
    private static final String getFullMsg(final LocalDateTime time, final String msg) {
        // check pre-conditions
        assert(validateTime(time)) : AssertConst.ASRT_INVALID_ARG;
        assert(validateMsg(msg)) : AssertConst.ASRT_INVALID_ARG;

        final StringBuilder sb = new StringBuilder(time.toString());
        sb.append(SYNTAX_TIME_MSG_SEPARATOR);
        sb.append(msg);
        final String msgFull = sb.toString();
        
        // post-conditions
        assert(validateMsgFull(msgFull)) : AssertConst.ASRT_INVALID_RESULT;

        return msgFull;
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
    public LogEntry(final LocalDateTime time, final String msg) {
        // check pre-conditions
        validateTime(time);
        validateMsg(msg);
        
        this.time = time;
        this.msg = msg;
        this.msgFull = getFullMsg(time, msg);
        validateMsgFull(msgFull);
    }
    
    /**
     * Constructor for class : [logmist] dburyak.logmist.model.LogEntry.<br/>
     * Also remembers full string that is used for toString method.
     * <br/>
     * <b>PRE-conditions:</b> non-null args <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>8:48:22 PM Jul 18, 2015</i>
     *
     * @param time
     *            time stamp of log entry
     * @param msg
     *            message of log entry (without time stamp)
     * @param msgFull
     *            full message of log entry (usually this is the original string from log file)
     */
    public LogEntry(final LocalDateTime time, final String msg, final String msgFull) {
        // check pre-conditions
        validateTime(time);
        validateMsg(msg);
        validateMsgFull(msgFull);
        
        this.time = time;
        this.msg = msg;
        this.msgFull = msgFull;
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
    public final LocalDateTime getTime() {
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
    
    /**
     * Getter method. <br/>
     * Get full message of this log entry (with time stamp included). If this log entry was constructed by parsing log
     * file, usually fullMsg is the original log message from log file. Otherwise, if this log entry was constructed
     * manually, full message is generated by combining time stamp and log message. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>12:22:13 PM Jul 19, 2015</i>
     *
     * @return full message of this log entry
     */
    public final String getMsgFull() {
        return msgFull;
    }
    
    /**
     * Comparator method.<br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>11:46:50 AM Jul 17, 2015</i>
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * @param o
     *            other {@link LogEntry} to compare this object to
     * @return -1 if this is less than other ; 0 if both are equal ; +1 if this is greater than other
     */
    @Override
    public int compareTo(LogEntry o) {
        // final int BEFORE = -1;
        final int EQUAL = 0;
        // final int AFTER = 1;

        if (this == o) {
            return EQUAL;
        }

        int comp = this.time.compareTo(o.time);
        if (comp != EQUAL) {
            return comp;
        }

        comp = this.msg.compareTo(o.msg);
        if (comp != EQUAL) {
            return comp;
        }

        comp = this.msgFull.compareTo(o.msgFull);
        if (comp != EQUAL) {
            return comp;
        }

        assert(this.equals(o)) : AssertConst.ASRT_INVALID_RESULT;
        return EQUAL;
    }
    
    /**
     * Default generated implementation.
     * <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>11:08:36 AM Jul 19, 2015</i>
     *
     * @see java.lang.Object#hashCode()
     * @return hash code of this log entry
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((msg == null) ? 0 : msg.hashCode());
        result = prime * result + ((msgFull == null) ? 0 : msgFull.hashCode());
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        return result;
    }
    
    /**
     * Default generated implementation. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>11:08:36 AM Jul 19, 2015</i>
     *
     * @see java.lang.Object#equals(java.lang.Object)
     * @param obj
     *            other object to check equality with
     * @return true if objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof LogEntry)) {
            return false;
        }
        LogEntry other = (LogEntry) obj;
        if (msg == null) {
            if (other.msg != null) {
                return false;
            }
        } else
            if (!msg.equals(other.msg)) {
            return false;
        }
        if (msgFull == null) {
            if (other.msgFull != null) {
                return false;
            }
        } else
            if (!msgFull.equals(other.msgFull)) {
            return false;
        }
        if (time == null) {
            if (other.time != null) {
                return false;
            }
        } else
            if (!time.equals(other.time)) {
            return false;
        }
        return true;
    }
    
    /**
     * Get string for log entry. "Time - message" - in case if this entry was constructed manually (without fullMsg).
     * Otherwise, the fullMsg is returned.
     * <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>11:01:09 AM Jul 19, 2015</i>
     *
     * @see java.lang.Object#toString()
     * @return string representation of this log entry
     */
    @Override
    public String toString() {
        // use msgFull if it is available
        if (msgFull != null) {
            return msgFull;
        }

        // otherwise, construct string representation
        StringBuilder sb = new StringBuilder(time.toString());
        sb.append(SYNTAX_TIME_MSG_SEPARATOR);
        sb.append(msg);
        
        return sb.toString();
    }
    
}
