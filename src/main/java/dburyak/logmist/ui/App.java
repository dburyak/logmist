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
 * @author Андрей
 *
 */
public final class App {

    private static final Logger LOG = LogManager.getFormatterLogger(App.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
        Class<? extends Application> appClass = LogmistJFXApp.class;
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
