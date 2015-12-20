package dburyak.logmist.ui.jfx;


import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.logmist.Resources;
import dburyak.logmist.Resources.MsgID;
import dburyak.logmist.Resources.UIConfigID;
import dburyak.logmist.exceptions.InaccessibleFileException;
import dburyak.logmist.exceptions.ParseException;
import dburyak.logmist.model.LogEntry;
import dburyak.logmist.model.parsers.ILogFileParser;
import dburyak.logmist.ui.data.DataUpdEventDispatcher;
import dburyak.logmist.ui.data.IDataUpdEventHandler;
import dburyak.logmist.ui.data.DataUpdEventType;
import dburyak.logmist.ui.data.LogTableData;
import dburyak.logmist.ui.data.ProgressData;
import dburyak.logmist.ui.data.signalers.LogTableDataUpdHandler;
import dburyak.logmist.ui.data.signalers.ParseProgressHandler;
import dburyak.logmist.ui.jfx.controllers.LogEntryTableLine;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import net.jcip.annotations.NotThreadSafe;


/**
 * Project : logmist.<br/>
 * Service that is responsible for parsing log files process and sending all the appropriate notifications to UI.<br/>
 * Not thread safe and not a singleton. Designed to be instantiated on demand by some UI handling code (JFX controller)
 * and be reused by single thread (thread confined, it should be JFX thread since it holds all the context knowledge
 * needed for the parse service).
 * <br/><b>Created on:</b> <i>1:33:19 AM Oct 5, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@NotThreadSafe
@javax.annotation.concurrent.NotThreadSafe
public final class ParseService extends Service<Void> {

    /**
     * Default system logger for this class.
     * <br/><b>Created on:</b> <i>1:35:13 AM Oct 5, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(ParseService.class);


    /**
     * Default parser to be used when specific parser throws {@link ParseException}. Fallback parser.
     * <br/><b>Created on:</b> <i>1:51:59 AM Oct 5, 2015</i>
     */
    private final ILogFileParser parserDefault;

    /**
     * Log parser to be used for next parse request.
     * <br/><b>Created on:</b> <i>1:51:10 AM Oct 5, 2015</i>
     */
    private ILogFileParser parser;

    /**
     * Path to the file to be parsed.
     * <br/><b>Created on:</b> <i>1:53:44 AM Oct 5, 2015</i>
     */
    private Path filePath;

    /**
     * Open button on the logs table. It is disabled during parse process and enabled on parse finish/error.
     * <br/><b>Created on:</b> <i>1:54:11 AM Oct 5, 2015</i>
     */
    private Button openBtnTable;

    /**
     * Progress indicator on the logs table. It is made visible during parse process and made invisible on parse
     * finish/error.
     * <br/><b>Created on:</b> <i>1:55:37 AM Oct 5, 2015</i>
     */
    private ProgressIndicator indicator;

    /**
     * Progress bar (in main status bar). Gets enabled during parse process, is updated with progress value, and gets
     * disabled on parse finish/error.
     * <br/><b>Created on:</b> <i>1:56:37 AM Oct 5, 2015</i>
     */
    private ProgressBar progressBar;

    /**
     * Table view that reflects parsed logs (unfiltered).
     * <br/><b>Created on:</b> <i>1:58:15 AM Oct 5, 2015</i>
     */
    private AutoSizableLogTableView logsTable;

    /**
     * Event identifier to be used for parse progress event signaling.
     * <br/><b>Created on:</b> <i>1:58:44 AM Oct 5, 2015</i>
     */
    private int idParseProgress;

    /**
     * Event id to be used for parse data ready event signaling (when table data should be updated).
     * <br/><b>Created on:</b> <i>2:11:57 AM Oct 5, 2015</i>
     */
    private int idParseDataReady;

    /**
     * UI data for logs table.
     * <br/><b>Created on:</b> <i>1:59:22 AM Oct 5, 2015</i>
     */
    private final LogTableData parsedLogData = new LogTableData();


    /**
     * UI updater that handles {@link DataUpdEventType#PARSE_PROGRESS_UPDATE} progress updates. Updates progress bar.
     * <br/><b>Created on:</b> <i>2:00:16 AM Oct 5, 2015</i>
     */
    private IDataUpdEventHandler parseProgressHandler;

    /**
     * UI updater that handles {@link DataUpdEventType#PARSE_FINISHED} event. Disables progress bar.
     * <br/><b>Created on:</b> <i>2:01:15 AM Oct 5, 2015</i>
     */
    private IDataUpdEventHandler parseFinishHandler;

    /**
     * UI updater that handles {@link DataUpdEventType#PARSE_ERROR} events.
     * <br/><b>Created on:</b> <i>2:04:51 AM Oct 5, 2015</i>
     */
    private IDataUpdEventHandler parseErrorHandler;

    /**
     * UI updater that handles {@link DataUpdEventType#LOG_TABLE_DATA_UPDATE} events. Prepares and sets data to be
     * displayed in the logs table.
     * <br/><b>Created on:</b> <i>2:05:37 AM Oct 5, 2015</i>
     */
    private IDataUpdEventHandler tableDataUpdateHandler;


    /**
     * Indicates whether this parse service is configured (not cleaned up) and can still be used.
     * <br/><b>Created on:</b> <i>5:06:15 AM Oct 5, 2015</i>
     */
    private final AtomicBoolean isConfigured = new AtomicBoolean(false);


    /**
     * Constructor for class : [logmist] dburyak.logmist.ui.jfx.controllers.ParseService.<br/>
     * <br/><b>PRE-conditions:</b> non-null parserDefault
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>1:53:14 AM Oct 5, 2015</i>
     * 
     * @param parserDefault
     *            default parser to be used for fallback parsing
     */
    public ParseService(final ILogFileParser parserDefault) {
        this.parserDefault = parserDefault;
        LogmistJFXApp.getInstance().registerService(this);
    }

    /**
     * Configuration method. Must be called by UI controllers for configuring parsing before starting this service.
     * <br/><b>PRE-conditions:</b> non-null arsg
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> provided arguments will be used by subsequent start() method call
     * <br/><b>Created on:</b> <i>2:07:12 AM Oct 5, 2015</i>
     * 
     * @param filePath
     *            log file to be parsed
     * @param parser
     *            log parser to be used for parsing
     * @param openBtnTable
     *            open button in table
     * @param progressBar
     *            progress bar in the main status bar
     * @param tableProgressIndicator
     *            progress indicator in table
     * @param logsTable
     *            table view that holds parsed logs
     */
    @SuppressWarnings("hiding")
    public final void configure(
        final Path filePath,
        final ILogFileParser parser,
        final Button openBtnTable,
        final ProgressBar progressBar,
        final ProgressIndicator tableProgressIndicator,
        final AutoSizableLogTableView logsTable) {

        this.filePath = filePath;
        this.parser = parser;
        this.openBtnTable = openBtnTable;
        this.indicator = tableProgressIndicator;
        this.progressBar = progressBar;
        this.logsTable = logsTable;
        idParseProgress = (int) (Math.random() * Integer.MAX_VALUE);
        idParseDataReady = (int) (Math.random() * Integer.MAX_VALUE);
        isConfigured.set(true);
        LOG.debug("parse service was configured : parser = [%s] ; filePath = [%s]", parser, filePath); //$NON-NLS-1$
    }

    /**
     * Clean-up method to be called when parsing task is finished (in succeeded(), cancelled(), failed() methods).
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> all internal state is cleaned, subsequent start() method calls will fail until
     * configure() is called
     * <br/><b>Created on:</b> <i>2:13:59 AM Oct 5, 2015</i>
     */
    @SuppressWarnings("unused")
    private final void cleanUp() {
        LOG.entry();

        if (isConfigured.compareAndSet(true, false)) {
            parser = null;
            filePath = null;
            openBtnTable = null;
            indicator = null;
            progressBar = null;
            logsTable = null;
            parseProgressHandler = null;
            parseFinishHandler = null;
            parseErrorHandler = null;
            tableDataUpdateHandler = null;
            idParseProgress = 0;
            idParseDataReady = 0;
        }
    }

    /**
     * Create runnable parsing task to be executed. All the parameters for this task are got from the configure()
     * method.
     * <br/><b>PRE-conditions:</b> configure() method was called
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:16:14 AM Oct 5, 2015</i>
     * 
     * @see javafx.concurrent.Service#createTask()
     * @return none
     */
    @Override
    protected final Task<Void> createTask() {
        return new Task<Void>() {

            @SuppressWarnings({ "synthetic-access", "nls", "boxing" })
            @Override
            protected final Void call() throws Exception {
                if (isConfigured.get()) {
                    try {
                        registerUIHandlers();
                        final ParseProgressHandler parseProgressUpdater = new ParseProgressHandler(idParseProgress);
                        Collection<LogEntry> logs = Collections.emptyList();
                        boolean defaultParseNeeded = true;
                        try {
                            // register progress data model handler (updates progress data)
                            parser.addListener(parseProgressUpdater);
                            logs = parser.parse(filePath);
                            defaultParseNeeded = false;
                        } catch (final InaccessibleFileException e) {
                            LOG.catching(Level.TRACE, e);
                            LOG.error("inaccessible log file : filePath = [%s]", filePath);
                            defaultParseNeeded = false; // no sense to try default parser on inaccessible file
                        } catch (final ParseException e) {
                            LOG.catching(Level.TRACE, e);
                            LOG.error("parse error occurred : parser = [%s] ; line = [%s] ; file = [%s]",
                                e.getParser(), e.getLine(), filePath);
                            defaultParseNeeded = true;
                        } finally {
                            parser.removeListener(parseProgressUpdater);
                        }

                        if (defaultParseNeeded) {
                            LOG.info("trying default parser : parser = [%s] ; file = [%s]", parserDefault, filePath);
                            try {
                                parserDefault.addListener(parseProgressUpdater);
                                logs = parserDefault.parse(filePath);
                                // don't catch exceptions, let them indicate parse service failure
                            } finally {
                                parserDefault.removeListener(parseProgressUpdater);
                            }
                        }

                        LOG.debug("logs parsed : numLogs = [%d] ; filePath = [%s]", logs.size(), filePath);

                        // parsing finished, lets update table data model
                        final LogTableDataUpdHandler tableDataUpdater = new LogTableDataUpdHandler(
                            parsedLogData, idParseDataReady, "mainParsedLogs");
                        tableDataUpdater.updateLogTableData(logs); // this will update model and send even to UI updater
                        return null;
                    } finally {
                    }
                } else {
                    LOG.error("trying to start unconfigured parse service", new Throwable());
                    return null;
                }
            }
        };
    }

    /**
     * Configures common UI controls basing on whether parsing is in progress.
     * <br/><b>PRE-conditions:</b> must be called only by UI JFX thread
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> UI state is changed
     * <br/><b>Created on:</b> <i>2:18:58 AM Oct 5, 2015</i>
     * 
     * @param isParsingInProgress
     *            indicates whether parsing is in progress
     */
    @SuppressWarnings({ "boxing" })
    private final void setUI(final boolean isParsingInProgress) {
        LOG.trace(isParsingInProgress);
        if (isConfigured.get()) {
            if (isParsingInProgress) {
                openBtnTable.setDisable(true);
                indicator.setVisible(true);
                progressBar.setDisable(false);
            } else {
                openBtnTable.setDisable(false);
                indicator.setVisible(false);
                progressBar.setDisable(true);
                progressBar.setProgress(0.0D);
            }
        }
    }

    /**
     * Hook method called immediately prior to the parsing actually doing its first bit of work.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> UI state is changed
     * <br/><b>Created on:</b> <i>2:20:15 AM Oct 5, 2015</i>
     * 
     * @see javafx.concurrent.Service#running()
     */
    @SuppressWarnings({ "nls", "boxing" })
    @Override
    protected final void running() {
        LOG.debug("parse service started parsing : idProgress = [%d] ; file = [%s] ; parser = [%s]",
            idParseProgress, filePath, parser);
        Platform.runLater(() -> {
            setUI(true);
        });
        super.running();
    }

    /**
     * Hook method called when service is in ready state.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:37:11 AM Oct 5, 2015</i>
     * 
     * @see javafx.concurrent.Service#ready()
     */
    @SuppressWarnings({ "nls", "boxing" })
    @Override
    protected final void ready() {
        LOG.debug("parse service ready : idProgress = [%d] ; idData = [%d] ; file = [%s] ; parser = [%s]",
            idParseProgress, idParseDataReady, filePath, parser);
        super.ready();
    }

    /**
     * Hook method called when service is in scheduled state.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:38:26 AM Oct 5, 2015</i>
     * 
     * @see javafx.concurrent.Service#scheduled()
     */
    @SuppressWarnings({ "nls", "boxing" })
    @Override
    protected final void scheduled() {
        LOG.debug("parse service scheduled : idProgress = [%d] ; idData = [%d] ; file = [%s] ; parser = [%s]",
            idParseProgress, idParseDataReady, filePath, parser);
        super.scheduled();
    }

    /**
     * Hook method called after parsing finished.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> UI state is changed
     * <br/><b>Created on:</b> <i>2:25:10 AM Oct 5, 2015</i>
     * 
     * @see javafx.concurrent.Service#succeeded()
     */
    @SuppressWarnings({ "nls", "boxing" })
    @Override
    protected final void succeeded() {
        LOG.debug("parse service finished parsing : idProgress = [%d] ; idData = [%d] ; file = [%s] ; parser = [%s]",
            idParseProgress, idParseDataReady, filePath, parser);
        Platform.runLater(() -> {
            setUI(false);
        });
        isConfigured.set(false);
        super.succeeded();
    }

    /**
     * Hook method called if parsing is cancelled. Not supposed to be called (no option to cancel parsing service by
     * design at the moment).
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> UI state is changed
     * <br/><b>Created on:</b> <i>2:28:44 AM Oct 5, 2015</i>
     * 
     * @see javafx.concurrent.Service#cancelled()
     */
    @SuppressWarnings({ "nls", "boxing" })
    @Override
    protected final void cancelled() {
        LOG.warn("parse service cancelled parsing : idProgress = [%d] ; idData = [%d] ; file = [%s] ; parser = [%s]",
            idParseProgress, idParseDataReady, filePath, parser);
        Platform.runLater(() -> {
            setUI(false);
        });
        isConfigured.set(false);
        super.cancelled();
    }

    /**
     * Hook method called if parsing failed. Not supposed to be called since all the exception handling should be done
     * inside the worker code.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> UI state is changed
     * <br/><b>Created on:</b> <i>2:30:44 AM Oct 5, 2015</i>
     * 
     * @see javafx.concurrent.Service#failed()
     */
    @SuppressWarnings({ "boxing", "nls" })
    @Override
    protected final void failed() {
        LOG.warn("parse service failed parsing : id = [%d] ; idData = [%d] ; file = [%s] ; parser = [%s]",
            idParseProgress, idParseDataReady, filePath, parser);
        Platform.runLater(() -> {
            setUI(false);
        });
        isConfigured.set(false);
        super.failed();
    }

    /**
     * Register UI updaters within {@link DataUpdEventDispatcher} service. These updaters receive model data update
     * events and update UI accordingly.
     * <br/><b>PRE-conditions:</b>
     * <br/><b>POST-conditions:</b>
     * <br/><b>Side-effects:</b>
     * <br/><b>Created on:</b> <i>2:32:22 AM Oct 5, 2015</i>
     */
    @SuppressWarnings({ "nls", "boxing", "unchecked" })
    private final void registerUIHandlers() {
        // register progress UI handler (listens parse events, reads progress data model, updates UI)
        parseProgressHandler = event -> {
            // TODO : add tick sequence check
            if (event.getEventID() == idParseProgress) { // filter only events for tihs service call
                final Double progress = ProgressData.getInstance().getData();
                Platform.runLater(() -> {
                    progressBar.progressProperty().setValue(progress);
                    indicator.setVisible(true);
                    openBtnTable.setDisable(true);
                    LOG.debug("update UI progress");
                });
            } else {
                LOG.debug("received event with unexpected id : id = [%d] ; expectedID = [%d] ; event = [%s]",
                    event.getEventID(), idParseProgress, event);
            }
        };
        parseFinishHandler = event -> {
            // TODO : add tick sequence check
            if (event.getEventID() == idParseProgress) { // filter only events for tihs service call
                Platform.runLater(() -> {
                    progressBar.setProgress(0.0D);
                    progressBar.setDisable(true);
                    indicator.setVisible(false);
                    openBtnTable.setDisable(false);
                    LOG.debug("update UI progress FINISHED");
                });
            } else {
                LOG.debug("received event with unexpected id : id = [%d] ; expectedID = [%d] ; event = [%s]",
                    event.getEventID(), idParseProgress, event);
            }
        };
        parseErrorHandler = event -> {
            if (event.getEventID() == idParseProgress) {
                Platform.runLater(() -> {
                    progressBar.setProgress(0.0D);
                    indicator.setVisible(false);
                    openBtnTable.setDisable(false);
                    LOG.debug("update UI progress ERROR");
                    // TODO : remove parse data and restart parsing of same file with default parser (fallback parser)
                });
            } else {
                LOG.debug("received event with unexpected id : id = [%d] ; expectedID = [%d] ; event = [%s]",
                    event.getEventID(), idParseProgress, event);
            }
        };
        final DataUpdEventDispatcher disp = DataUpdEventDispatcher.getInstance();
        disp.register(DataUpdEventType.PARSE_PROGRESS_UPDATE, parseProgressHandler);
        disp.register(DataUpdEventType.PARSE_FINISHED, parseFinishHandler);
        disp.register(DataUpdEventType.PARSE_ERROR, parseErrorHandler);

        // register logTableData update handler (this one is triggered when parse process is ready and table model was
        // updated with brand new bunch of logs, thus it updates UI table with data from table model)
        tableDataUpdateHandler = event -> {
            if (event.getEventID() == idParseDataReady) {
                final Resources res = Resources.getInstance();

                final double fixedCellSize = Double.parseDouble(Resources.getInstance().getUIProp(
                    UIConfigID.MAIN_LOGS_FIXED_CELL_SIZE));

                final String colNameLine = res.getMsg(MsgID.MAIN_LOGS_COLUMN_LINENUM_NAME);
                final TableColumn<LogEntryTableLine, Long> colLine = new TableColumn<>(colNameLine);
                colLine.setCellValueFactory(new PropertyValueFactory<>("lineNum"));
                colLine.setEditable(false);
                colLine.getStyleClass().add("columnLineNum"); // should correspond to .css style file

                final String colNameLog = res.getMsg(MsgID.MAIN_LOGS_COLUMN_LOG_NAME);
                final TableColumn<LogEntryTableLine, String> colLog = new TableColumn<>(colNameLog);
                colLog.setCellValueFactory(new PropertyValueFactory<>("log"));
                colLog.setEditable(false);

                final Collection<LogEntryTableLine> tableLines = new LinkedList<>();
                final Collection<LogEntry> parsedLogEntries = parsedLogData.getData();
                parsedLogEntries.stream().forEachOrdered(logEntry -> {
                    tableLines.add(new LogEntryTableLine(logEntry));
                });

                Platform.runLater(() -> {
                    final long timeStart = System.currentTimeMillis();

                    logsTable.getColumns().clear();
                    logsTable.refresh();
                    logsTable.getColumns().addAll(colLine, colLog);
                    logsTable.setFixedCellSize(fixedCellSize);

                    logsTable.getItems().clear();
                    logsTable.refresh();
                    logsTable.getItems().addAll(tableLines);

                    LOG.debug("calling autosize......");
                    logsTable.refresh();
                    logsTable.requestLayout();

                    logsTable.autoSizeFitContent();

                    final boolean unregisterSuccessful = unregisterUIHandlers();
                    if (!unregisterSuccessful) {
                        LOG.warn("failed unregister UI update handlers");
                    }

                    final long timeSpent = System.currentTimeMillis() - timeStart;
                    LOG.debug("update UI parsed logs table data : timeSpentMS = [%d]", timeSpent);
                });
            } else {
                LOG.debug("received event with unexpected id : id = [%d] ; expectedID = [%d] ; event = [%s]",
                    event.getEventID(), idParseDataReady, event);
            }
        };
        disp.register(DataUpdEventType.LOG_TABLE_DATA_UPDATE, tableDataUpdateHandler);
    }

    /**
     * Unregister all UI updaters.
     * <br/><b>PRE-conditions:</b> ui updaters are registered within {@link DataUpdEventDispatcher}
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> {@link DataUpdEventDispatcher} state is changed
     * <br/><b>Created on:</b> <i>2:48:13 AM Oct 5, 2015</i>
     * 
     * @return true if all updaters were unregistered successfully
     */
    private final boolean unregisterUIHandlers() {
        final DataUpdEventDispatcher disp = DataUpdEventDispatcher.getInstance();
        boolean unregisterSuccessfull = disp.unregister(DataUpdEventType.PARSE_PROGRESS_UPDATE, parseProgressHandler);
        unregisterSuccessfull = unregisterSuccessfull
            && disp.unregister(DataUpdEventType.PARSE_FINISHED, parseFinishHandler);
        unregisterSuccessfull = unregisterSuccessfull
            && disp.unregister(DataUpdEventType.PARSE_ERROR, parseErrorHandler);
        unregisterSuccessfull = unregisterSuccessfull
            && disp.unregister(DataUpdEventType.LOG_TABLE_DATA_UPDATE, tableDataUpdateHandler);
        return unregisterSuccessfull;
    }

}
