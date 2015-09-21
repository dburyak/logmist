/**
 * 
 */
package dburyak.logmist.exceptions;

import java.io.File;
import java.nio.file.Path;

/**
 * TODO : code style
 * @author Андрей
 *
 */
public final class CannotCreateFileException extends LogmistException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    
    public CannotCreateFileException(final Path file) {
        super(file.toString());
    }
    
    public CannotCreateFileException(final File file) {
        super(file.getPath());
    }
    
    
}
