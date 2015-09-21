/**
 * 
 */
package dburyak.logmist.ui.jfx;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.jcip.annotations.NotThreadSafe;

/**
 * TODO : code style
 * @author Андрей
 */
@NotThreadSafe
@javax.annotation.concurrent.NotThreadSafe
final class Resources {

    private static final Logger LOG = LogManager.getFormatterLogger(Resources.class);
    
    static enum MsgID {
        APP_TITLE("app.title"),
        MAIN_LOGS_PLACEHOLDER_TEXT("main.logsTable.placeholder.text"),
        MAIN_LOGS_PLACEHOLDER_BTN_TEXT("main.logsTable.placeholder.btn.text"),
        APP_RESOURCES_NO_USER_CONFIG_TITLE("app.resources_check_no_user_config_title"),
        APP_RESOURCES_NO_USER_CONFIG_TEXT("app.resources_check_no_user_config_text"),
        APP_RESOURCES_NOT_READABLE_USER_CONFIG_TITLE("app.resources_check_not_readable_user_config_title"),
        APP_RESOURCES_NOT_READABLE_USER_CONFIG_TEXT("app.resources_check_not_readable_user_config_text"),
        APP_RESOURCES_NOT_WRITEABLE_USER_CONFIG_TITLE("app.resources_check_not_writeable_user_config_title"),
        APP_RESOURCES_NOT_WRITEABLE_USER_CONFIG_TEXT("app.resources_check_not_writeable_user_config_text"),
        MAIN_STATUS_BAR_LOGS_TOTAL_NAME("main.statusBar.logsTotal.name"),
        MAIN_STATUS_BAR_SELECTED_NAME("main.statusBar.selected.name"),
        MAIN_STATUS_BAR_SELECTED_VALUE("main.statusBar.selected.def_value"),
        MAIN_STATUS_BAR_LOGS_FILTERED_NAME("main.statusBar.logsFiltered.name"),
        MAIN_STATUS_BAR_SELECTED_CAT_NAME("main.statusBar.selectedCat.name"),
        MAIN_STATUS_BAR_SELECTED_CAT_VALUE("main.statusBar.selectedCat.def_value"),
        MAIN_STATUS_BAR_RIGHT_PANEL_TYPE_QUICK_NAME("main.statusBar.rightPanelType.quick.name"),
        MAIN_STATUS_BAR_RIGHT_PANEL_TYPE_LIBRARY_NAME("main.statusBar.rightPanelType.library.name"),
        ;
        
        private final String name;
        
        private MsgID(final String name) {
            this.name = name;
        }
        
        String getName() {
            return name;
        }
    }
    
    static enum UIConstID {
        MAIN_WINDOW_START_WIDTH_FACTOR("main.window_start_width_factor"),
        MAIN_WINDOW_START_HEIGHT_FACTOR("main.window_start_height_factor"),
        ;
        
        private final String name;
        
        private UIConstID(final String name) {
            this.name = name;
        }
        
        String getName() {
            return name;
        }
    }
    
    private static final String BUNDLE_NAME_MESSAGES = "dburyak.logmist.ui.jfx.messages";
    private static final String BUNDLE_NAME_UI_CONST = "dburyak.logmist.ui.jfx.ui_const";
    
    private static Resources INSTANCE = null;
    
    public static final Resources getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Resources();
        }
        return INSTANCE;
    }
    
    private final ResourceBundle msg;
    private final ResourceBundle uiConst;
    private final Locale locale;
    
    
    private Resources() {
        locale = new Locale(Config.getInstance().getValue(Config.PropID.LOCALE));
        
        try {
            msg = ResourceBundle.getBundle(BUNDLE_NAME_MESSAGES, locale);
            LOG.debug("messages resource bundle loaded : file = [%s]", BUNDLE_NAME_MESSAGES);
        } catch (final MissingResourceException e) {
            LOG.catching(Level.TRACE, e);
            LOG.fatal("cannot load JFX UI messages : resource = [%s]", BUNDLE_NAME_MESSAGES);
            throw LOG.throwing(Level.TRACE, new AssertionError("accessible UI messages file in source folder "
                    + "expected : resource = [" + BUNDLE_NAME_MESSAGES + "]"));
        }
        
        try {
            uiConst = ResourceBundle.getBundle(BUNDLE_NAME_UI_CONST);
            LOG.debug("UI const resource bundle loaded : file = [%s]", BUNDLE_NAME_UI_CONST);
        } catch (final MissingResourceException e) {
            LOG.catching(Level.TRACE, e);
            LOG.fatal("cannot load JFX UI constants : resource = [%s]", BUNDLE_NAME_UI_CONST);
            throw LOG.throwing(Level.TRACE, new AssertionError("accessible UI constants file in source folder "
                    + "expected : resource = [" + BUNDLE_NAME_UI_CONST + "]"));
        }
    }
    
    public final String getMsg(final MsgID key) {
        return msg.getString(key.getName());
    }
    
    public final String getUIConst(final UIConstID key) {
        return uiConst.getString(key.getName());
    }
    
    public final Locale getCurrentLocale() {
        return locale;
    }
    
    final ResourceBundle getMsgBundle() {
        return msg;
    }

}
