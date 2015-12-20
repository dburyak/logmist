/**
 * 
 */
package dburyak.logmist.ui.jfx.controllers;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.logmist.Resources;
import dburyak.logmist.Resources.ConfigID;
import dburyak.logmist.Resources.MsgID;
import dburyak.logmist.exceptions.InaccessibleFileException;
import dburyak.logmist.model.parsers.ILogFileParser;
import dburyak.logmist.ui.jfx.AutoSizableLogTableView;
import dburyak.logmist.ui.jfx.LogmistJFXApp;
import dburyak.logmist.ui.jfx.ParseService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * @author Андрей
 */
public final class LogsController {

    static final Logger LOG = LogManager.getFormatterLogger(LogsController.class);

    private MainController mainCtrl;
    private boolean areParsersInited = false;
    private final Collection<ILogFileParser> parsers = new LinkedList<>();
    private ILogFileParser parserDefault;
    private ParseService parseSrv = null;

    @FXML
    private Button logsTableOpenBtn;
    @FXML
    private ProgressIndicator logsTableProgressIndicator;

    @FXML
    private AutoSizableLogTableView mainLogsTable;


    public void init(final MainController mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    private final void initParsers() {
        LOG.entry();
        if (areParsersInited) {
            LOG.exit();
            return;
        }

        LOG.info("initializing log parsers");
        final Resources res = Resources.getInstance();
        // first - default parser, which is used when none of other parsers can
        // recognize format
        assert(!res.isUndefined(ConfigID.CORE_PARSERS_DEFAULT));
        final String parserDefaultClassStr = res.getConfigProp(ConfigID.CORE_PARSERS_DEFAULT);
        try {
            final Object parserDefaultObj = Class.forName(parserDefaultClassStr).newInstance();
            assert(parserDefaultObj instanceof ILogFileParser);
            parserDefault = (ILogFileParser) parserDefaultObj;
            LOG.info("default log parser : className = [%s]", parserDefaultClassStr);
        } catch (final InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOG.catching(Level.TRACE, e);
            LOG.fatal("cannot instantiate default parser : className = [%s]", parserDefaultClassStr);
            throw LOG.throwing(Level.TRACE,
                new AssertionError("default log parser must be instantiable via default constructor", e));
        }

        // instantiate secondary parsers and add them to the list
        final String[] secClassesStr = res.getConfigProp(ConfigID.CORE_PARSERS_SECONDARY).split(",");
        LOG.debug("secondary log parsers in config : num = [%d]", secClassesStr.length);
        Arrays.stream(secClassesStr).forEachOrdered(classStr -> {
            try {
                final ILogFileParser parser = (ILogFileParser) Class.forName(classStr).newInstance();
                parsers.add(parser);
                LOG.info("log parser registered : className = [%s]", classStr);
            } catch (final Exception e) {
                LOG.catching(Level.TRACE, e);
                LOG.warn("cannot instantiate log parser, discarding : className = [%s]", classStr);
            }
        });

        // init parse service
        assert(parseSrv == null);
        parseSrv = new ParseService(parserDefault);

        areParsersInited = true;
    }

    private static final Path chooseFile() {
        final Stage stage = LogmistJFXApp.getInstance().getStage();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Resources.getInstance().getMsg(MsgID.MAIN_LOGS_FILE_CHOOSER_TITLE));
        final Path dir = Paths.get(Resources.getInstance().getConfigProp(ConfigID.MAIN_LOGS_FILE_CHOOSER_DIR));
        if (Files.exists(dir)) {
            fileChooser.setInitialDirectory(dir.toFile());
        } else {
            fileChooser.setInitialDirectory(Paths.get(".").toFile());
        }
        final File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            final Path dirChosen = file.getParentFile().toPath();
            Resources.getInstance().setConfigProp(ConfigID.MAIN_LOGS_FILE_CHOOSER_DIR,
                dirChosen.toAbsolutePath().toString());
            LOG.trace("logs file chosen : file = [%s]", file.toPath().toString());
            return file.toPath();
        } else { // user cancelled opening
            return null;
        }
    }

    private final ILogFileParser chooseParser(final Path filePath) {
        for (final ILogFileParser parser : parsers) {
            try {
                if (parser.canParse(filePath)) {
                    return parser;
                }
            } catch (final InaccessibleFileException e) {
                LOG.catching(Level.TRACE, e);
                LOG.error("inaccessible log file : filePath = [%s]", filePath);
            }
        }
        return parserDefault;
    }


    @FXML
    public final void handleOpenBtn(final ActionEvent event) {
        LOG.entry(event);
        logsTableOpenBtn.setDisable(true);
        initParsers();
        final Path filePath = chooseFile();
        if (filePath == null) {
            logsTableOpenBtn.setDisable(false);
            return;
        }

        final ILogFileParser parser = chooseParser(filePath);
        parseSrv.setExecutor(LogmistJFXApp.getInstance().getThreadPool());
        Platform.runLater(() -> {
            parseSrv.reset();
            parseSrv.configure(filePath, parser, logsTableOpenBtn, mainCtrl.getStatusProgressBar(),
                logsTableProgressIndicator, mainLogsTable);
            parseSrv.start();
        });
        LOG.exit();
    }

}
