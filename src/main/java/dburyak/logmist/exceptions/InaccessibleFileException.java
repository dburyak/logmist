package dburyak.logmist.exceptions;


import java.io.File;
import java.nio.file.Path;


/**
 * Project : logmist.<br/>
 * <br/><b>Created on:</b> <i>4:09:23 AM Aug 22, 2015</i>
 * Indicates that file cannot be accessed (cannot be opened for read for some reason).
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class InaccessibleFileException extends LogmistException {

    /**
     * <br/><b>Created on:</b> <i>4:10:08 AM Aug 22, 2015</i>
     */
    private static final long serialVersionUID = 1L;

    /**
     * File string representation in case if null is passed to constructor.
     * <br/><b>Created on:</b> <i>4:24:11 AM Aug 22, 2015</i>
     */
    private static final String NULL_FILE_STR = "NULL"; //$NON-NLS-1$


    /**
     * Constructor for class : [logmist] dburyak.logmist.exceptions.InaccessibleFileException.<br/>
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:25:57 AM Aug 22, 2015</i>
     * 
     * @param filePath
     *            file that caused this exception
     */
    public InaccessibleFileException(final Path filePath) {
        super((filePath != null) ? filePath.toAbsolutePath().toString() : NULL_FILE_STR);
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.exceptions.InaccessibleFileException.<br/>
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>4:26:25 AM Aug 22, 2015</i>
     * 
     * @param file
     *            file that caused this exception
     */
    public InaccessibleFileException(final File file) {
        super((file != null) ? file.getAbsolutePath() : NULL_FILE_STR);
    }

}
