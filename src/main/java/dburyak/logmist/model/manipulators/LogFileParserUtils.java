package dburyak.logmist.model.manipulators;


import java.nio.file.Files;
import java.nio.file.Path;

import dburyak.jtools.AssertConst;


/**
 * Project : logmist.<br/>
 * Utility class with utils for log files parsing.
 * <br/><b>Created on:</b> <i>4:32:33 AM Aug 22, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
final class LogFileParserUtils {

    /**
     * Check if file is accessible and can be read.
     * <br/><b>PRE-conditions:</b> non-null arg
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> IO is performed (checking file attributes)
     * <br/><b>Created on:</b> <i>4:06:06 AM Aug 22, 2015</i>
     * 
     * @param filePath
     *            file to be tested
     * @return true if file is accessible and can be opened for reading
     */
    static final boolean isAccessibleReadable(final Path filePath) {
        assert(filePath != null) : AssertConst.ASRT_NULL_ARG;
        return (Files.exists(filePath) && !Files.isDirectory(filePath) && Files.isReadable(filePath));
    }

}
