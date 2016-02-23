/**
 * 
 */
package dburyak.logmist;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.jcip.annotations.NotThreadSafe;


/**
 * TODO : code style
 */
@NotThreadSafe
@javax.annotation.concurrent.NotThreadSafe
public final class Resources {

    private static final Logger LOG = LogManager.getFormatterLogger(Resources.class);


    @SuppressWarnings("nls")
    public static enum MsgID {
            APP_TITLE("app.title"),
            MAIN_LOGS_PLACEHOLDER_TEXT("main.logsTable.placeholder.text"),
            MAIN_LOGS_PLACEHOLDER_BTN_TEXT("main.logsTable.placeholder.btn.text"),

            APP_RESOURCES_NO_USER_CONFIG_TITLE("app.resources_check_no_user_config_title"),
            APP_RESOURCES_NO_USER_CONFIG_TEXT("app.resources_check_no_user_config_text"),
            APP_RESOURCES_NON_READABLE_USER_CONFIG_TITLE("app.resources_check_non_readable_user_config_title"),
            APP_RESOURCES_NON_READABLE_USER_CONFIG_TEXT("app.resources_check_non_readable_user_config_text"),
            APP_RESOURCES_NON_WRITABLE_USER_CONFIG_TITLE("app.resources_check_non_writable_user_config_title"),
            APP_RESOURCES_NON_WRITABLE_USER_CONFIG_TEXT("app.resources_check_non_writable_user_config_text"),

            APP_RESOURCES_NO_UI_CONFIG_TITLE("app.resources_check_no_ui_config_title"),
            APP_RESOURCES_NO_UI_CONFIG_TEXT("app.resources_check_no_ui_config_text"),
            APP_RESOURCES_NON_READABLE_UI_CONFIG_TITLE("app.resources_check_non_readable_ui_config_title"),
            APP_RESOURCES_NON_READABLE_UI_CONFIG_TEXT("app.resources_check_non_readable_ui_config_text"),
            APP_RESOURCES_NON_WRITABLE_UI_CONFIG_TITLE("app.resources_check_non_writable_ui_config_title"),
            APP_RESOURCES_NON_WRITABLE_UI_CONFIG_TEXT("app.resources_check_non_writable_ui_config_text"),

            MAIN_STATUS_BAR_LOGS_TOTAL_NAME("main.statusBar.logsTotal.name"),
            MAIN_STATUS_BAR_SELECTED_NAME("main.statusBar.selected.name"),
            MAIN_STATUS_BAR_SELECTED_VALUE("main.statusBar.selected.def_value"),
            MAIN_STATUS_BAR_LOGS_FILTERED_NAME("main.statusBar.logsFiltered.name"),
            MAIN_STATUS_BAR_SELECTED_CAT_NAME("main.statusBar.selectedCat.name"),
            MAIN_STATUS_BAR_SELECTED_CAT_VALUE("main.statusBar.selectedCat.def_value"),
            MAIN_STATUS_BAR_RIGHT_PANEL_TYPE_QUICK_NAME("main.statusBar.rightPanelType.quick.name"),
            MAIN_STATUS_BAR_RIGHT_PANEL_TYPE_LIBRARY_NAME("main.statusBar.rightPanelType.library.name"),
            MAIN_LOGS_FILE_CHOOSER_TITLE("main.logsTable.fileChooser.title"),
            MAIN_LOGS_COLUMN_LINENUM_NAME("main.logsTable.column.lineNum.name"),
            MAIN_LOGS_COLUMN_LOG_NAME("main.logsTable.column.log.name"),;

        private final String name;


        private MsgID(final String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    public static enum UIConfigID {
            MAIN_WINDOW_START_WIDTH_FACTOR("main.window.start_width_factor"),
            MAIN_WINDOW_START_HEIGHT_FACTOR("main.window.start_height_factor"),
            MAIN_WINDOW_WIDTH("main.window.width"),
            MAIN_WINDOW_HEIGHT("main.window.height"),
            MAIN_WINDOW_XPOS("main.window.xpos"),
            MAIN_WINDOW_YPOS("main.window.ypos"),
            MAIN_WINDOW_MAXIMIZED("main.window.maximized"),
            MAIN_STATUS_BAR_HEAP_BAR_START_DELAY_MS("main.statusBar.heapBar.startDelayMS"),
            MAIN_STATUS_BAR_HEAP_BAR_UPD_PERIOD_MS("main.statusBar.heapBar.updPeriodMS"),
            MAIN_STATUS_BAR_PROGRESS_BAR_UPD_PERIOD_MS("main.statusBar.progressBar.updPeriodMS"),
            MAIN_LOGS_FIXED_CELL_SIZE("main.logs.fixedCellSize"),
            MAIN_LOGS_TABLE_UPDATE_INACTIVE_TIME_MS("main.logs.tableUpdateInactiveTimeMS");

        private final String name;


        private UIConfigID(final String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }
    }

    @SuppressWarnings("nls")
    public static enum ConfigID {
            LOCALE("core.locale"),
            MAIN_LOGS_FILE_CHOOSER_DIR("main.logsTable.fileChooser.dir"),
            CORE_PARSERS_DEFAULT("core.parsers.default"),
            CORE_PARSERS_SECONDARY("core.parsers.secondary"),
            CORE_THREAD_POOL_SIZE_ADD("core.threadPoolSizeAdd"),
            CORE_UIDATA_EVENT_QUEUE_SIZE("core.UIDataEventQueue.size"),
            CORE_UIDATA_EVENT_QUEUE_PUT_TIMEOUT_MS("core.UIDataEventQueue.putTimeoutMS"),
            CORE_UIDATA_EVENT_QUEUE_HANDLER_THREAD_POOL_SIZE("core.UIDataEventQueue.handlerThreadPoolSize"),
            CORE_UIDATA_EVENT_QUEUE_HANDLER_WAIT("core.UIDataEventQueue.handlerWait"),
            CORE_UIDATA_RWLOCK_TIMEOUT_MS("core.UIData.rwLock.timoutMS"),
            CORE_THREAD_POOL_AWAIT_TERMINATION_TIMEOUT_MS("core.threadPool.awaitTerminationTimeoutMS"),
            CORE_THREAD_POOL_KEEP_ALIVE_TIME_S("core.threadPool.keepAliveTimeS"),
            CORE_THREAD_POOL_TASK_QUEUE_CAPACITY("core.threadPool.taskQueueCapacity"),
            CORE_PARSERS_NUM_LINES_TO_TEST("core.parsers.num_lines_to_test"),;

        private final String name;


        private ConfigID(final String name) {
            this.name = name;
        }

        final String getName() {
            return name;
        }
    }

    public final static class UserFileStatus {

        private final Path filePath;
        private final boolean exists;
        private final boolean canRead;
        private final boolean canWrite;


        public UserFileStatus(
            final Path filePath,
            final boolean exists,
            final boolean canRead,
            final boolean canWrite) {

            this.filePath = filePath;
            this.exists = exists;
            this.canRead = canRead;
            this.canWrite = canWrite;
        }

        public final Path getFilePath() {
            return filePath;
        }

        public final boolean exists() {
            return exists;
        }

        public final boolean canRead() {
            return canRead;
        }

        public final boolean canWrite() {
            return canWrite;
        }

        @Override
        public String toString() {
            final StringBuilder sb = (new StringBuilder("{filePath=[")).append(filePath);
            sb.append("],exists=[").append(exists);
            sb.append("],canRead=[").append(canRead);
            sb.append("],canWrite=[").append(canWrite).append("]}");
            return sb.toString();
        }
    }


    private static final String BUNDLE_NAME_MESSAGES = "dburyak.logmist.ui.messages";
    private static final String BUNDLE_NAME_UI_CONFIG = "dburyak.logmist.ui.ui_default";
    private static final String BUNDLE_NAME_CONFIG = "dburyak.logmist.ui.config_default";
    private static final Path PATH_USER_CONFIG = Paths.get("config.properties");
    private static final Path PATH_USER_UI_CONFIG = Paths.get("ui.properties");

    private static final String PROP_UNDEFINED = "<none>";

    private static Resources INSTANCE = null;


    public static final Resources getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Resources();
        }
        return INSTANCE;
    }


    private final ResourceBundle msg;
    private final ResourceBundle uiConfigDefault;
    private final ResourceBundle configDefault;
    private final Properties uiConfigUser;
    private final Properties configUser;
    private final UserFileStatus configUserStatus;
    private final UserFileStatus uiConfigUserStatus;

    private final Locale locale;


    private Resources() {
        try {
            configDefault = ResourceBundle.getBundle(BUNDLE_NAME_CONFIG);
            LOG.debug("default config resource bundle loaded : file = [%s]", BUNDLE_NAME_CONFIG);
        } catch (final MissingResourceException e) {
            LOG.catching(Level.TRACE, e);
            LOG.fatal("cannot load default config : file = [%s]", BUNDLE_NAME_CONFIG);
            throw LOG.throwing(Level.TRACE, new AssertionError("cannot load default config : file = ["
                + BUNDLE_NAME_CONFIG
                + "]"));
        }
        configUserStatus = testUserFile(PATH_USER_CONFIG);
        LOG.debug("user config file status : status = [%s]", configUserStatus);
        configUser = initUserFile(PATH_USER_CONFIG, configUserStatus);

        locale = new Locale(getValue(ConfigID.LOCALE.getName(), configDefault, configUser));
        try {
            msg = ResourceBundle.getBundle(BUNDLE_NAME_MESSAGES, locale);
            LOG.debug("messages resource bundle loaded : file = [%s]", BUNDLE_NAME_MESSAGES);
        } catch (final MissingResourceException e) {
            LOG.catching(Level.TRACE, e);
            LOG.fatal("cannot load JFX UI messages : file = [%s]", BUNDLE_NAME_MESSAGES);
            throw LOG.throwing(Level.TRACE, new AssertionError("cannot load UI messages : file = ["
                + BUNDLE_NAME_MESSAGES
                + "]"));
        }

        try {
            uiConfigDefault = ResourceBundle.getBundle(BUNDLE_NAME_UI_CONFIG);
            LOG.debug("UI const resource bundle loaded : file = [%s]", BUNDLE_NAME_UI_CONFIG);
        } catch (final MissingResourceException e) {
            LOG.catching(Level.TRACE, e);
            LOG.fatal("cannot load JFX UI constants : resource = [%s]", BUNDLE_NAME_UI_CONFIG);
            throw LOG.throwing(Level.TRACE, new AssertionError("accessible UI constants file in source folder "
                + "expected : resource = ["
                + BUNDLE_NAME_UI_CONFIG
                + "]"));
        }
        uiConfigUserStatus = testUserFile(PATH_USER_UI_CONFIG);
        LOG.debug("user UI config file status : status = [%s]", uiConfigUserStatus);
        uiConfigUser = initUserFile(PATH_USER_UI_CONFIG, uiConfigUserStatus);
    }

    private static final UserFileStatus testUserFile(final Path filePath) {
        final boolean exists = Files.exists(filePath);
        final boolean canRead = (exists) ? Files.isReadable(filePath) : false;
        final boolean canWrite = (exists) ? Files.isWritable(filePath) : false;
        return new UserFileStatus(filePath, exists, canRead, canWrite);
    }

    /**
     * Read if exists. Create if not exists and return empty props.
     * 
     * @param filePath
     * @param status
     * @return
     */
    private static final Properties initUserFile(final Path filePath, final UserFileStatus status) {
        final Properties props = new Properties();
        if (!status.exists) {
            try {
                Files.createFile(filePath);
            } catch (final IOException e) {
                LOG.catching(Level.TRACE, e);
                LOG.error("error when creating user resource file : file = [%s]", filePath);
            }
        } else { // user file exists, load it
            if (status.canRead) {
                try {
                    props.load(Files.newBufferedReader(filePath));
                } catch (final IOException e) {
                    LOG.catching(Level.TRACE, e);
                    LOG.error("error when reading user resource file : file = [%s]", filePath);
                }
            }
        }
        return props;
    }

    private static final void persistUserFile(
        final Path filePath,
        final Properties props,
        final UserFileStatus status,
        final String comments) {

        if (!status.exists || (status.exists && status.canRead && status.canWrite)) {
            try (final BufferedWriter out = Files.newBufferedWriter(filePath)) {
                props.store(out, comments);
                LOG.debug("persisted successfully : file = [%s] ; numProps = [%d]", filePath, props.size());
            } catch (final IOException e) {
                LOG.catching(Level.TRACE, e);
                LOG.error("error when writing user resource file : file = [%s]", filePath);
            }
        } else {
            LOG.warn("user resource file was not persisted : file = [%s] ; status = [%s]", filePath, status);
        }
    }

    private static final String getValue(final String key, final ResourceBundle deflt, final Properties user) {
        // search value in user
        final String userVal = user.getProperty(key);
        if (userVal != null) {
            LOG.trace("found value in user : key = [%s] ; value = [%s]", key, userVal);
            return userVal;
        } else {
            // not found in user, search in defaults
            try {
                final String defltVal = deflt.getString(key);
                assert (defltVal != null);
                LOG.trace("found value in default : key = [%s] ; value = [%s]", key, defltVal);
                return defltVal;
            } catch (final MissingResourceException e) {
                // not found at all, return null (null is not valid value for any property)
                return null;
            }
        }
    }

    public final String getMsg(final MsgID key) {
        return msg.getString(key.getName());
    }

    public final String getUIProp(final UIConfigID key) {
        return getValue(key.getName(), uiConfigDefault, uiConfigUser);
    }

    public final String setUIProp(final UIConfigID key, final String newValue) {
        final Object prev = uiConfigUser.put(key.getName(), newValue);
        LOG.trace("UI property set : key = [%s] ; newVal = [%s] ; oldVal = [%s]", key, newValue, (prev != null)
            ? prev
            : "<none>");
        return (prev != null) ? prev.toString() : null;
    }

    public final UserFileStatus getUIConfigStatus() {
        return uiConfigUserStatus;
    }

    public final String getConfigProp(final ConfigID key) {
        return getValue(key.getName(), configDefault, configUser);
    }

    public final String setConfigProp(final ConfigID key, final String newValue) {
        final Object prev = configUser.put(key.getName(), newValue);
        LOG.trace("config property set : key = [%s] ; newVal = [%s] ; oldVal = [%s]", key, newValue, (prev != null)
            ? prev
            : "<none>");
        return (prev != null) ? prev.toString() : null;
    }

    public final UserFileStatus getConfigStatus() {
        return configUserStatus;
    }

    public final void persist() {
        persistUserFile(PATH_USER_CONFIG, configUser, configUserStatus, "DO NOT EDIT MANUALLY\nlogmist configuration");
        persistUserFile(PATH_USER_UI_CONFIG, uiConfigUser, uiConfigUserStatus,
            "DO NOT EDIT MANUALLY\nUI configuration");
    }

    public final Locale getCurrentLocale() {
        return locale;
    }

    public final ResourceBundle getMsgBundle() {
        return msg;
    }

    public final boolean isUndefined(final String propValue) {
        return (PROP_UNDEFINED.equals(propValue));
    }

    public final boolean isUndefined(final UIConfigID key) {
        return (PROP_UNDEFINED.equals(getValue(key.getName(), uiConfigDefault, uiConfigUser)));
    }

    public final boolean isUndefined(final ConfigID key) {
        return (PROP_UNDEFINED.equals(getValue(key.getName(), configDefault, configUser)));
    }
}
