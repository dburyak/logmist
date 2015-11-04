/**
 * 
 */
package dburyak.logmist.ui;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.logmist.ui.jfx.LogmistJFXApp;
import edu.emory.mathcs.backport.java.util.Arrays;
import javafx.application.Application;


// FIXME : code style

/**
 * Project : logmist.<br/>
 * Entry point for all the application.
 * <br/><b>Created on:</b> <i>9:56:45 PM Nov 1, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class App {

    /**
     * Default system logger for this class.
     * <br/><b>Created on:</b> <i>9:57:09 PM Nov 1, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(App.class);


    // TODO : application cli arguments (at least abstract framework).
    /**
     * Main entry point for all the application.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> all the application is started (core and chosen front-end)
     * <br/><b>Created on:</b> <i>9:57:36 PM Nov 1, 2015</i>
     * 
     * @param args
     *            command-line arguments
     */
    public static void main(final String[] args) {
        final Class<? extends Application> appClass = LogmistJFXApp.class;
        try {
            // TODO : add logic for choosing front-end (JFX/Console)


            // no arguments needed
            Application.launch(appClass, new String[] {});
        } catch (final Throwable t) {
            LOG.catching(Level.DEBUG, t);
            LOG.fatal("application did not start : appClass = [%s] ; args = [%s]", appClass, Arrays.toString(args), t);
            System.out.println("application did not start");
            t.printStackTrace(System.out);
        }
    }

}
