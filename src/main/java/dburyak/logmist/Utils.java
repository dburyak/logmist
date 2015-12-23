package dburyak.logmist;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.jtools.Validators;
import dburyak.logmist.exceptions.InaccessibleFileException;
import rx.Observable;


/**
 * Project : logmist.<br/>
 * Utility class with utils for log files parsing.
 * <br/><b>Created on:</b> <i>4:32:33 AM Aug 22, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class Utils {

    /**
     * Default system logger for this class.
     * <br/><b>Created on:</b> <i>7:57:09 AM Dec 22, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(Utils.class);


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
    public static final boolean isAccessibleReadable(final Path filePath) {
        Validators.nonNull(filePath);
        return (Files.exists(filePath) && !Files.isDirectory(filePath) && Files.isReadable(filePath));
    }

    /**
     * Get observable that reads file line-by-line and stops reading when unsubscribed.
     * <br/><b>PRE-conditions:</b> non-null and accessible and readable textFile
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> I/O operations are performed (file attributes check, file reading in result Observable)
     * <br/><b>Created on:</b> <i>8:06:54 AM Dec 22, 2015</i>
     * 
     * @param textFile
     *            text file to be read
     * @return observable that emits lines read from file
     * @throws InaccessibleFileException
     *             if given file cannot be read
     */
    @SuppressWarnings("nls")
    public static final Observable<String> asLinesObservable(final Path textFile) throws InaccessibleFileException {
        Validators.nonNull(textFile);
        if (!isAccessibleReadable(textFile)) {
            throw LOG.throwing(Level.TRACE, new InaccessibleFileException(textFile));
        }

        return Observable.<String> create(subscriber -> {
            try (final BufferedReader in = Files.newBufferedReader(textFile)) {
                String line = null;
                do {
                    line = in.readLine();
                    subscriber.onNext(line);
                } while (!subscriber.isUnsubscribed() && line != null);
                subscriber.onCompleted();
            } catch (final IOException ex) {
                LOG.catching(Level.TRACE, ex);
                LOG.error("I/O exception when reading file : file = [%s]", textFile);
                subscriber.onError(ex);
            }
        });
    }

}
