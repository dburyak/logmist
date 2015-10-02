package dburyak.logmist.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.concurrent.Immutable;

import dburyak.jtools.AssertConst;
import dburyak.jtools.Validators;
import net.jcip.annotations.ThreadSafe;


/**
 * Project : logmist.<br/>
 * Represents one record from msg file. <br/>
 * <b>Created on:</b> <i>12:12:39 PM Jul 15, 2015</i>
 *
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.2
 */
@Immutable
@ThreadSafe
@net.jcip.annotations.Immutable
@javax.annotation.concurrent.ThreadSafe
public final class LogEntry implements Comparable<LogEntry> {

    /**
     * Separator to be used when there is a need to combine time and msg (to get fullMsg).
     * <br/>
     * <b>Created on:</b> <i>11:04:47 AM Jul 19, 2015</i>
     */
    private static final String SYNTAX_TIME_MSG_SEPARATOR = " - "; //$NON-NLS-1$

    /**
     * Special time stamp that indicates "no timestamp" situation.
     * <br/><b>Created on:</b> <i>1:51:41 AM Oct 2, 2015</i>
     */
    private static final LocalDateTime NO_TIME = LocalDateTime.MIN;


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
     * Line number in log file.
     * <br/><b>Created on:</b> <i>2:38:24 AM Oct 2, 2015</i>
     */
    private final long lineNum;


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
     * Validator for lineNum parameter. Non-negative lineNum is valid.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:40:11 AM Oct 2, 2015</i>
     * 
     * @param lineNum
     *            lineNum parameter to be validated
     * @return true if lineNum is valid
     * @throws IllegalArgumentException
     *             if lineNum is invalid
     */
    private static final boolean validateLineNum(final long lineNum) {
        if (lineNum < 0) {
            throw new IllegalArgumentException();
        }
        return true;
    }

    /**
     * Validator for timeFormat. Non-null timeFormat is valid.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:07:21 AM Oct 2, 2015</i>
     * 
     * @param timeFormat
     *            time formatter to be validated
     * @return true if formatter is valid
     * @throws IllegalArgumentException
     *             if timeFormat is invalid
     */
    private static final boolean validateTimeFormat(final DateTimeFormatter timeFormat) {
        return Validators.nonNull(timeFormat);
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
     * @param timeFormat
     *            time formatter for formatting time stamp string
     * @return full message for log entry constructed from given params
     * @throws IllegalArgumentException
     *             if any of parameters is illegal
     */
    private static final String buildFullMsg(
        final LocalDateTime time,
        final String msg,
        final DateTimeFormatter timeFormat) {

        // check pre-conditions
        assert(LogEntry.validateTime(time)) : AssertConst.ASRT_INVALID_ARG;
        assert(LogEntry.validateMsg(msg)) : AssertConst.ASRT_INVALID_ARG;
        assert(LogEntry.validateTimeFormat(timeFormat)) : AssertConst.ASRT_INVALID_ARG;

        final StringBuilder sb = new StringBuilder(time.format(timeFormat));
        sb.append(SYNTAX_TIME_MSG_SEPARATOR);
        sb.append(msg);
        final String msgFull = sb.toString();

        // post-conditions
        assert(LogEntry.validateMsgFull(msgFull)) : AssertConst.ASRT_INVALID_RESULT;

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
     * @param timeFormat
     *            time formatter for building full message for this log
     * @param lineNum
     *            line number in log file of this log entry
     */
    public LogEntry(
        final LocalDateTime time,
        final String msg,
        final DateTimeFormatter timeFormat,
        final long lineNum) {

        // check pre-conditions
        LogEntry.validateTime(time);
        LogEntry.validateMsg(msg);
        LogEntry.validateTimeFormat(timeFormat);
        LogEntry.validateLineNum(lineNum);

        final String msgFullBuilt = LogEntry.buildFullMsg(time, msg, timeFormat);
        LogEntry.validateMsgFull(msgFullBuilt);

        this.time = time;
        this.msg = msg;
        this.msgFull = msgFullBuilt;
        this.lineNum = lineNum;

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
     * @param lineNum
     *            line number in log file of this log entry
     */
    public LogEntry(final LocalDateTime time, final String msg, final String msgFull, final long lineNum) {
        // check pre-conditions
        LogEntry.validateTime(time);
        LogEntry.validateMsg(msg);
        LogEntry.validateMsgFull(msgFull);
        LogEntry.validateLineNum(lineNum);

        this.time = time;
        this.msg = msg;
        this.msgFull = msgFull;
        this.lineNum = lineNum;
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.LogEntry.<br/>
     * Create a simple log message without time stamp.
     * <br/><b>PRE-conditions:</b> non-null arg
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:58:00 AM Oct 2, 2015</i>
     * 
     * @param msgFull
     *            full message of log entry (usually this is the original string from log file)
     * @param lineNum
     *            line number in log file of this log entry
     */
    public LogEntry(final String msgFull, final long lineNum) {
        LogEntry.validateTime(NO_TIME);
        LogEntry.validateMsgFull(msgFull);
        LogEntry.validateMsg(msgFull);
        LogEntry.validateLineNum(lineNum);

        this.time = NO_TIME;
        this.msg = msgFull;
        this.msgFull = msgFull;
        this.lineNum = lineNum;
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
     * Getter method. <br/>
     * Get line number of this log entry in log file.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-negative result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:43:15 AM Oct 2, 2015</i>
     * 
     * @return line number of this log entry in log file
     */
    public final long getLineNum() {
        return this.lineNum;
    }

    /**
     * Indicates whether this log entry has a time stamp. If it doesn't have one, then {@link LogEntry#getTime()} method
     * will return not reliable value (some default time stamp).
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:52:55 AM Oct 2, 2015</i>
     * 
     * @return true if this log entry has a time stamp
     */
    public final boolean hasTimeStamp() {
        return (time != NO_TIME);
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
    public int compareTo(final LogEntry o) {
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
        result = (prime * result) + ((msg == null) ? 0 : msg.hashCode());
        result = (prime * result) + ((msgFull == null) ? 0 : msgFull.hashCode());
        result = (prime * result) + ((time == null) ? 0 : time.hashCode());
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
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof LogEntry)) {
            return false;
        }
        final LogEntry other = (LogEntry) obj;
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
        final StringBuilder sb = new StringBuilder(time.toString());
        sb.append(SYNTAX_TIME_MSG_SEPARATOR);
        sb.append(msg);

        return sb.toString();
    }

}
