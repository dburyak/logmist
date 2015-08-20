package dburyak.logmist.model.manipulators;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;

import dburyak.jtools.Validators;
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
@javax.annotation.concurrent.Immutable
@ThreadSafe
@javax.annotation.concurrent.ThreadSafe
public final class DefaultLogParser implements ILogFileParser {

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
    @Override
    public boolean canParse(final Path filePath) {
        Validators.nonNull(filePath);
        Validators.exists(filePath);
        return (!Files.isDirectory(filePath) && Files.isReadable(filePath));
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
     */
    @Override
    public Collection<LogEntry> parse(final Path filePath) {
        final Collection<LogEntry> resultLogs = new LinkedList<>();

        // TODO : currently implementing here ......

        return resultLogs;
    }


}
