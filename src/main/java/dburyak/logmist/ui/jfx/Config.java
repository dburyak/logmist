/**
 * 
 */
package dburyak.logmist.ui.jfx;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.jtools.AssertConst;
import net.jcip.annotations.NotThreadSafe;

/**
 * TODO : code style TODO : make it thread safe
 * 
 * @author Андрей
 *
 */
@NotThreadSafe
@javax.annotation.concurrent.NotThreadSafe
final class Config {

    private static final Logger LOG = LogManager.getFormatterLogger(Config.class);

    static enum PropID {
        LOCALE("locale");
        
        private final String name;
        
        private PropID(final String name) {
            this.name = name;
        }
        
        final String getName() {
            return name;
        }
    }
    
    private static final Path PATH_CONFIG = Paths.get("config.properties");
    private static final String DEFAULT_BUNDLE_NAME = "dburyak.logmist.ui.jfx.config_default";
    private static Config INSTANCE = null;

    private final ResourceBundle configDefault;
    private final Properties configUser;
    private boolean hasUserConfig;
    private boolean canReadUserConfig;
    private boolean canSaveUserConfig;

    public static final Config getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }
        return INSTANCE;
    }

    private Config() {
        // load default config packaged with distribution
        try {
            configDefault = ResourceBundle.getBundle(DEFAULT_BUNDLE_NAME);
            LOG.debug("default config loaded");
        } catch (final MissingResourceException e) {
            // fatal, distribution error - default config MUST be included in
            // dist jar
            LOG.catching(Level.TRACE, e);
            LOG.fatal("cannot load default config : bundleName = [%s]", DEFAULT_BUNDLE_NAME);
            throw LOG.throwing(Level.TRACE, new AssertionError("accessible default config file in resources folder "
                    + "expected : bundleName = [" + DEFAULT_BUNDLE_NAME + "]"));
        }

        canReadUserConfig = true;
        configUser = new Properties();
        if (!Files.exists(PATH_CONFIG)) {
            hasUserConfig = false;
            try {
                Files.createFile(PATH_CONFIG);
                LOG.info("no config file found, created new : file = [%s]", PATH_CONFIG);
                canReadUserConfig = true;
            } catch (final IOException e) {
                LOG.catching(Level.TRACE, e);
                canReadUserConfig = false;
                canSaveUserConfig = false;
                LOG.error("cannot create user config file, using defaults : file = [%s]", PATH_CONFIG);
            }
        } else {
            hasUserConfig = true;
            // load user's settings from user config file
            if (canReadUserConfig) {
                try {
                    configUser.load(Files.newBufferedReader(PATH_CONFIG));
                    LOG.debug("user config loaded : file = [%s]", PATH_CONFIG);
                } catch (final IOException e) {
                    LOG.catching(Level.TRACE, e);
                    LOG.error("cannot load config, using defaults : file = [%s]", PATH_CONFIG);
                    canReadUserConfig = false;
                }
            }
        }



        // check write permissions
        canSaveUserConfig = Files.isWritable(PATH_CONFIG);
        if (!canSaveUserConfig) {
            LOG.error("cannot write to config file, setting will NOT be saved : file = [%s]", PATH_CONFIG);
        }
    }

    /**
     * @param name
     * @return
     * @throws NoSuchElementException
     */
    public final String getValue(final PropID key) {
        LOG.entry(key);
        // first, search in user config
        final String resultUsr = configUser.getProperty(key.getName());
        if (resultUsr != null) {
            return LOG.exit(resultUsr);
        }

        // not found in user config, then search in default
        assert(configDefault.containsKey(key.getName())) : AssertConst.ASRT_INVALID_ARG;
        final String resultDflt = configDefault.getString(key.getName());
        return LOG.exit(resultDflt);
    }
    
    public final boolean hasUserConfig() {
        return hasUserConfig;
    }
    
    public final boolean canReadUserConfig() {
        return canReadUserConfig;
    }
    
    public final boolean canSaveUserConfig() {
        return canSaveUserConfig;
    }
}
