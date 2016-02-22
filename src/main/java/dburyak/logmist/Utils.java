package dburyak.logmist;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactfx.util.Tuple2;
import org.reactfx.util.Tuples;

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
     * Constructor for class : [logmist] dburyak.logmist.Utils.<br/>
     * Constructor for utility class must NOT be called.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>9:04:58 AM Dec 27, 2015</i>
     */
    private Utils() {
        throw LOG.throwing(Level.ERROR, new AssertionError("not supposed to be instantiated")); //$NON-NLS-1$
    }


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
                } while (!subscriber.isUnsubscribed() && (line != null));
                subscriber.onCompleted();
            } catch (final IOException ex) {
                LOG.catching(Level.TRACE, ex);
                LOG.error("I/O exception when reading file : file = [%s]", textFile);
                subscriber.onError(ex);
            }
        });
    }

    /**
     * Get couple:
     * 
     * <pre>
     *  - observable that emits lines from text file
     *  - total number of lines in text file
     * </pre>
     * 
     * <br/><b>PRE-conditions:</b>non-null and accessible and readable textFile
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> I/O operations (attributes check, read full file)
     * <br/><b>Created on:</b> <i>9:51:36 AM Dec 27, 2015</i>
     * 
     * @param textFile
     *            text file to be read
     * @return observable that emits lines read from file and total number of lines in text file
     * @throws InaccessibleFileException
     *             if file cannot be read
     * @emits IOException if I/O error occurs when reading file
     */
    @SuppressWarnings({ "nls", "boxing" })
    public static final Tuple2<Observable<String>, Integer> asLinesObservableAndSize(final Path textFile)
        throws InaccessibleFileException {

        Validators.nonNull(textFile);
        if (!isAccessibleReadable(textFile)) {
            throw LOG.throwing(Level.TRACE, new InaccessibleFileException(textFile));
        }

        try {
            final List<String> linesList = Files.readAllLines(textFile);
            final Observable<String> lines = Observable.<String> create(subscriber -> {
                final Iterator<String> it = linesList.iterator();
                while (it.hasNext() && !subscriber.isUnsubscribed()) {
                    subscriber.onNext(it.next());
                }
                subscriber.onCompleted();
            });
            return Tuples.t(lines, linesList.size());
        } catch (final IOException ex) {
            LOG.catching(Level.TRACE, ex);
            LOG.error("I/O exception when reading file : textFile = [%s]", textFile);
            return Tuples.t(Observable.error(ex), 0);
        }
    }

}
