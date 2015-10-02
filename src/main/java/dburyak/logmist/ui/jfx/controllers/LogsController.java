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

import dburyak.logmist.exceptions.InaccessibleFileException;
import dburyak.logmist.model.LogEntry;
import dburyak.logmist.model.manipulators.ILogFileParser;
import dburyak.logmist.ui.Resources;
import dburyak.logmist.ui.Resources.ConfigID;
import dburyak.logmist.ui.Resources.MsgID;
import dburyak.logmist.ui.Resources.UIConfigID;
import dburyak.logmist.ui.data.DataUpdEventDispatcher;
import dburyak.logmist.ui.data.DataUpdEventHandler;
import dburyak.logmist.ui.data.DataUpdEventType;
import dburyak.logmist.ui.data.ParseProgressHandler;
import dburyak.logmist.ui.data.ProgressData;
import dburyak.logmist.ui.jfx.LogmistJFXApp;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * @author Андрей
 */
public final class LogsController {

    private static final Logger LOG = LogManager.getFormatterLogger(LogsController.class);

    private MainController mainCtrl;
    private boolean areParsersInited = false;
    private final Collection<ILogFileParser> parsers = new LinkedList<>();
    private ILogFileParser parserDefault;
    @FXML
    private Button logsTableOpenBtn;
    @FXML
    private ProgressIndicator logsTableProgressIndicator;

    @FXML
    private TableView mainLogsTable;


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
        Arrays.stream(secClassesStr).forEach(classStr -> {
            try {
                final ILogFileParser parser = (ILogFileParser) Class.forName(classStr).newInstance();
                parsers.add(parser);
                LOG.info("log parser registered : className = [%s]", classStr);
            } catch (final Exception e) {
                LOG.catching(Level.TRACE, e);
                LOG.warn("cannot instantiate log parser, discarding : className = [%s]", classStr);
            }
        });

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


    private static final class ParseService extends Service<Void> {

        private final ILogFileParser parser;
        private final Path filePath;
        private final Button openBtn;
        private final ProgressIndicator indicator;
        private final ProgressBar progressBar;
        private final TableView<LogEntryTableLine> logsTable;
        private final int id;


        public ParseService(
            final Path filePath,
            final ILogFileParser parser,
            final Button openBtn,
            final ProgressBar progressBar,
            final ProgressIndicator tableProgressIndicator,
            final TableView<LogEntryTableLine> logsTable) {
            this.filePath = filePath;
            this.parser = parser;
            this.openBtn = openBtn;
            this.indicator = tableProgressIndicator;
            this.progressBar = progressBar;
            this.logsTable = logsTable;
            id = (int) (Math.random() * Integer.MAX_VALUE);
        }

        @Override
        protected final Task<Void> createTask() {
            return new Task<Void>() {

                @Override
                protected final Void call() throws Exception {
                    openBtn.setDisable(true);
                    indicator.setVisible(true);

                    // register progress data model handler (updates progress data)
                    final ParseProgressHandler handler = new ParseProgressHandler(id);
                    parser.addListener(handler);

                    // register progress UI handler (listens parse events, reads progress data model, updates UI)
                    final DataUpdEventHandler parseProgressUpdUIHandler = event -> {
                        // TODO : add tick sequence check
                        if (event.getEventID() == id) { // filter only events for tihs service call
                            final double progress = ProgressData.getInstance().getData();
                            Platform.runLater(() -> {
                                progressBar.setProgress(progress);
                                indicator.setVisible(true);
                                openBtn.setDisable(true);
                                LOG.debug("update UI progress");
                            });
                        } else {
                            LOG.debug("received event with unexpected id : id = [%d] ; expected = [%d]",
                                event.getEventID(), id);
                        }
                    };
                    final DataUpdEventHandler parseFinishedUpdUIHandler = event -> {
                        // TODO : add tick sequence check
                        if (event.getEventID() == id) { // filter only events for tihs service call
                            Platform.runLater(() -> {
                                progressBar.setProgress(0.0D);
                                indicator.setVisible(false);
                                openBtn.setDisable(false);
                                LOG.debug("update UI progress FINISHED");
                            });
                        } else {
                            LOG.debug("received event with unexpected id : id = [%d] ; expected = [%d]",
                                event.getEventID(), id);
                        }
                    };
                    DataUpdEventDispatcher.getInstance().register(
                        DataUpdEventType.PARSE_PROGRESS_UPDATE, parseProgressUpdUIHandler);
                    DataUpdEventDispatcher.getInstance().register(
                        DataUpdEventType.PARSE_FINISHED, parseFinishedUpdUIHandler);
                    DataUpdEventDispatcher.getInstance().register(
                        DataUpdEventType.PARSE_ERROR, parseFinishedUpdUIHandler);
                    try {
                        final Collection<LogEntry> logs = parser.parse(filePath);
                        // TODO : currently here ...............
                        final ObservableList<LogEntryTableLine> logsTableData = FXCollections.observableArrayList();
                        logs.stream().forEachOrdered((logEntry) -> {
                            logsTableData.add(new LogEntryTableLine(logEntry));
                        });
                        final TableColumn<LogEntryTableLine, String> column = new TableColumn<>("log");
                        column.setCellValueFactory(new PropertyValueFactory<>("logFull"));
                        final double fixedCellSize = Double.parseDouble(Resources.getInstance().getUIProp(
                            UIConfigID.MAIN_LOGS_FIXED_CELL_SIZE));
                        Platform.runLater(() -> {
                            logsTable.getColumns().add(column);
                            logsTable.setFixedCellSize(fixedCellSize);
                            logsTable.setItems(logsTableData);
                        });

                        LOG.debug("logs parsed : numLogs = [%d] ; filePath = [%s]", logs.size(), filePath);
                        return null;
                    } catch (final InaccessibleFileException e) {
                        LOG.catching(Level.TRACE, e);
                        LOG.error("inaccessible log file : filePath = [%s]", filePath);
                        return null;
                    } finally {
                        parser.removeListener(handler);
                        boolean unregisterSuccessfull =
                            DataUpdEventDispatcher.getInstance().unregister(
                                DataUpdEventType.PARSE_PROGRESS_UPDATE, parseProgressUpdUIHandler);
                        unregisterSuccessfull = unregisterSuccessfull
                            &&
                            DataUpdEventDispatcher.getInstance().unregister(
                                DataUpdEventType.PARSE_FINISHED, parseFinishedUpdUIHandler);
                        unregisterSuccessfull = unregisterSuccessfull
                            &&
                            DataUpdEventDispatcher.getInstance().unregister(
                                DataUpdEventType.PARSE_ERROR, parseFinishedUpdUIHandler);
                        if (!unregisterSuccessfull) {
                            LOG.warn("failed unregister UI progress update handlers");
                        }
                    }
                }
            };
        }

        private final void setUI(final boolean isParsingInProgress) {
            if (isParsingInProgress) {
                openBtn.setDisable(true);
                indicator.setVisible(true);
                progressBar.setDisable(false);
            } else {
                openBtn.setDisable(false);
                indicator.setVisible(false);
                progressBar.setDisable(true);
                progressBar.setProgress(0.0D);
            }
        }

        @Override
        protected final void running() {
            LOG.debug("parse service started parsing : id = [%d] ; file = [%s] ; parser = [%s]",
                id, filePath, parser.getClass().getSimpleName());
            Platform.runLater(() -> {
                setUI(true);
            });
            super.running();
        }

        @Override
        protected final void succeeded() {
            LOG.debug("parse service finished parsing : id = [%d] ; file = [%s] ; parser = [%s]",
                id, filePath, parser.getClass().getSimpleName());
            Platform.runLater(() -> {
                setUI(false);
            });
            super.succeeded();
        }

        @Override
        protected final void cancelled() {
            LOG.debug("parse service cancelled parsing : id = [%d] ; file = [%s] ; parser = [%s]",
                id, filePath, parser.getClass().getSimpleName());
            Platform.runLater(() -> {
                setUI(false);
            });
            super.cancelled();
        }

        @Override
        protected final void failed() {
            LOG.debug("parse service failed parsing : id = [%d] ; file = [%s] ; parser = [%s]",
                id, filePath, parser.getClass().getSimpleName(), new Throwable());
            Platform.runLater(() -> {
                setUI(false);
            });
            super.failed();
        }

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
        // FIXME : new daemon thread (service thread) is created on each call, need to re-use it
        final ParseService parseSrv = new ParseService(filePath,
            parser,
            logsTableOpenBtn,
            mainCtrl.getStatusProgressBar(),
            logsTableProgressIndicator,
            mainLogsTable);
        parseSrv.setExecutor(LogmistJFXApp.getInstance().getThreadPool());
        LogmistJFXApp.getInstance().getThreadPool().execute(() -> {
            parseSrv.start();
        });
        LOG.exit();
    }

}
