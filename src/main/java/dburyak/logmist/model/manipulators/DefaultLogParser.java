package dburyak.logmist.model.manipulators;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.jtools.AssertConst;
import dburyak.jtools.Validators;
import dburyak.logmist.exceptions.InaccessibleFileException;
import dburyak.logmist.model.LogEntry;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;


/**
 * Project : logmist.<br/>
 * Default implementation of {@link ILogFileParser}. Uses generated time stamps starting from the epoch and incrementing
 * by time unit for each next log.
 * <br/><b>Created on:</b> <i>4:26:11 AM Aug 20, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
@ThreadSafe
@javax.annotation.concurrent.Immutable
@javax.annotation.concurrent.ThreadSafe
public final class DefaultLogParser implements ILogFileParser {

    /**
     * Default logger for this class.
     * <br/><b>Created on:</b> <i>12:21:28 AM Aug 21, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(DefaultLogParser.class);

    /**
     * Time point where the count starts.
     * <br/><b>Created on:</b> <i>1:15:05 AM Aug 27, 2015</i>
     */
    private static final LocalDateTime TIME_START = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);


    /**
     * Get time when counting starts.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:15:17 AM Aug 27, 2015</i>
     * 
     * @return time when this parser starts counting
     */
    public static final LocalDateTime getTimeStart() {
        return TIME_START;
    }


    /**
     * Duration of one tick to be used when generating time stamps.
     * <br/><b>Created on:</b> <i>5:05:12 AM Aug 22, 2015</i>
     */
    private final Duration tickDuration;


    // private final Time

    /**
     * Constructor for class : [logmist] dburyak.logmist.model.manipulators.DefaultLogParser.<br/>
     * <br/><b>PRE-conditions:</b> non-null, non-negative, non-zero
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>5:04:54 AM Aug 22, 2015</i>
     * 
     * @param tickDuration
     *            duration of one tick to be used when generating time stamps
     */
    public DefaultLogParser(final Duration tickDuration) {
        this.tickDuration = tickDuration;
    }

    /**
     * Can parse any existing and accessible file.
     * <br/><b>PRE-conditions:</b> is a file and can be read
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> I/O operations are performed
     * <br/><b>Created on:</b> <i>4:52:05 AM Aug 20, 2015</i>
     * 
     * @see dburyak.logmist.model.manipulators.ILogFileParser#canParse(java.nio.file.Path)
     * @param filePath
     *            file to be tested
     * @return true if file can be parsed by this parser (always if file is readable)
     */
    @SuppressWarnings("boxing")
    @Override
    public boolean canParse(final Path filePath) {
        LOG.entry(filePath);

        Validators.nonNull(filePath);
        return LOG.exit(LogFileParserUtils.isAccessibleReadable(filePath));
    }

    /**
     * Parse given file.
     * <br/><b>PRE-conditions:</b> is file and is readable
     * <br/><b>POST-conditions:</b> non-null result; can be empty
     * <br/><b>Side-effects:</b> I/O operations are performed; may lead to significant memory consumption growth
     * depending on file size
     * <br/><b>Created on:</b> <i>5:15:35 AM Aug 20, 2015</i>
     * 
     * @see dburyak.logmist.model.manipulators.ILogFileParser#parse(java.nio.file.Path)
     * @param filePath
     *            file to be parsed
     * @return parsed log entries
     * @throws InaccessibleFileException
     *             if file cannot be opened for read
     */
    @Override
    public Collection<LogEntry> parse(final Path filePath) throws InaccessibleFileException {
        LOG.entry(filePath);
        Validators.nonNull(filePath);

        if (!LogFileParserUtils.isAccessibleReadable(filePath)) {
            LOG.warn("file is not accessible : file = [%s]", filePath); //$NON-NLS-1$
            throw LOG.throwing(Level.DEBUG, new InaccessibleFileException(filePath));
        }

        Collection<LogEntry> resultLogs = new LinkedList<>();
        try {
            LocalDateTime timeStamp = getTimeStart();
            final List<String> lines = Files.readAllLines(filePath);
            for (final String line : lines) {
                resultLogs.add(new LogEntry(timeStamp, line));
                timeStamp = timeStamp.plus(tickDuration);
            }
        } catch (final IOException ex) {
            LOG.error("error when reading file : file = [%s]", filePath, ex); //$NON-NLS-1$
            resultLogs = Collections.emptyList();
        }

        assert(resultLogs != null) : AssertConst.ASRT_NULL_RESULT;
        return LOG.exit(resultLogs);
    }


}
