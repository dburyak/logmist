/**
 * 
 */
package dburyak.logmist.ui.jfx.controllers;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import org.reactfx.util.Tuple2;
import org.reactfx.util.Tuples;

import dburyak.jtools.Validators;
import dburyak.logmist.Resources;
import dburyak.logmist.Resources.ConfigID;
import dburyak.logmist.Resources.MsgID;
import dburyak.logmist.Resources.UIConfigID;
import dburyak.logmist.Utils;
import dburyak.logmist.exceptions.InaccessibleFileException;
import dburyak.logmist.model.LogEntry;
import dburyak.logmist.model.parsers.ILogFileParser;
import dburyak.logmist.ui.jfx.AutoSizableLogTableView;
import dburyak.logmist.ui.jfx.LogmistJFXApp;
import dburyak.logmist.ui.jfx.ParseService;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;


/**
 * Project : logmist.<br/>
 * FXML controller for "mainLogsTable.fxml" fxml.
 * <br/><b>Created on:</b> <i>7:51:54 PM Dec 26, 2015</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public final class LogsController {

    /**
     * Default system logger for this class.
     * <br/><b>Created on:</b> <i>7:52:40 PM Dec 26, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(LogsController.class);

    /**
     * Path to default directory for "Open..." file chooser. Current directory.
     * <br/><b>Created on:</b> <i>8:22:58 PM Dec 26, 2015</i>
     */
    private static final String DEFAULT_DIR_PATH = "."; //$NON-NLS-1$


    /**
     * Validator for "clicksOpenBtn" event stream field.
     * <br/><b>PRE-conditions:</b> non-null clicksOpenBtn
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>7:52:54 PM Dec 26, 2015</i>
     * 
     * @param clicksOpenBtn
     *            "clicksOpenBtn" field to be validated
     * @return true if "clicksOpenBtn" is valid
     * @throws IllegalArgumentException
     *             if "clicksOpenBtn" is invalid
     */
    private static final boolean validateClicksOpenBtn(final EventStream<ActionEvent> clicksOpenBtn) {
        return Validators.nonNull(clicksOpenBtn);
    }


    private MainController mainCtrl;
    private boolean areParsersInitialized = false;
    private final Collection<ILogFileParser> parsers = new ArrayList<>();
    private ILogFileParser parserDefault;
    private ParseService parseSrv = null;

    private final BooleanProperty isParseInProgress = new SimpleBooleanProperty(false);
    private final IntegerProperty numLinesToParse = new SimpleIntegerProperty(0);


    @FXML
    private Button logsTableOpenBtn;
    @FXML
    private ProgressIndicator logsTableProgressIndicator;

    @FXML
    private AutoSizableLogTableView mainLogsTable;

    // event streams
    private EventStream<ActionEvent> clicksOpenBtn = null;
    private EventStream<ActionEvent> clicksMenuOpen = null;

    // subscriptions


    public final void bindTo(final MainController mainCtrl) {
        this.mainCtrl = mainCtrl;
        LOG.debug("controllers binding done");
    }

    public final void initActions() {
        initEventStreams();
        initParseAction();
        LOG.debug("actions initialized");
    }

    private final void initEventStreams() {
        clicksOpenBtn = EventStreams.eventsOf(logsTableOpenBtn, ActionEvent.ACTION);
        clicksMenuOpen = mainCtrl.mainMenuFileOpenClicks();
    }

    @SuppressWarnings({ "boxing", "nls" })
    private final void initParseAction() {
        // create source of clicks
        final EventStream<ActionEvent> actionSource = clicksOpenBtn.or(clicksMenuOpen)
            .map(either -> either.isLeft() ? either.getLeft() : either.getRight()); // get any
        final PublishSubject<ActionEvent> actionsSubject = PublishSubject.create();
        actionSource.subscribe(actionsSubject::onNext);  // convert to rxjava

        // create source of ParseInfo, which is emitted when user requests to parse log file
        // TODO : add support for multiple files
        final Observable<ParseInfo> parseInfoSource = actionsSubject
            .flatMap(click -> chooseFile()) // this can emit multiple selected files
            .filter(path -> path != null)
            .doOnNext(path -> loadParsers()) // side-effect that loads parsers from config only once
            .doOnNext(path -> LOG.debug("path chosen : path = [%s]", path))
            // choose parser, convert to path+parser tuple
            .map(path -> Tuples.t(path, chooseParser(path)))
            // convert path to source of lines (open file and read line by line), emit ParseInfo
            .map(tuple2 -> ParseInfo.from(tuple2.get1(), tuple2.get2()))
            .observeOn(Schedulers.computation());
        final PublishSubject<ParseInfo> subjectParseInfo = PublishSubject.create();
        parseInfoSource.subscribe(subjectParseInfo);

        // lifetime subscription for parse progress indicators
        final Observable<ParseInfo> parseInfoFromSubject = subjectParseInfo
            .doOnNext(info -> LOG.debug("emit parse info : parseInfo = [%s]", info)); //$NON-NLS-1$
        subscribeParseInProgressProperty(parseInfoFromSubject);
        subscribeNumLinesProperty(parseInfoFromSubject);

        // lifetime subscription for logs table contents
        parseInfoFromSubject
            .map(info -> { // parse lines with parser and emit log entries source
                final Observable<ILogFileParser> parser = info.getParser().toObservable();
                final Observable<String> lines = info.getLines();
                return parser.flatMap(p -> p.parse(lines));
            }).subscribe( // lifetime subscription
                logsSource -> { // next source of parsed log entries
                    bindParseInProgressProperty();
                    subscribeLogTableContent(logsSource);
                },
                error -> LOG.error("error on parsing file :", error) // error
        );
    }

    private final void bindParseInProgressProperty() {
        logsTableProgressIndicator.visibleProperty().bind(isParseInProgress);
        mainCtrl.getStatusStopBtn().disableProperty().bind(isParseInProgress.not());
        mainCtrl.getStatusProgressBar().disableProperty().bind(isParseInProgress.not());
        logsTableOpenBtn.disableProperty().bind(isParseInProgress);
        mainCtrl.getMenuFileOpen().disableProperty().bind(isParseInProgress);
    }

    private final void unbindParseInProgressProperty() {
        logsTableProgressIndicator.visibleProperty().unbind();
        mainCtrl.getStatusStopBtn().disableProperty().unbind();
        mainCtrl.getStatusProgressBar().disableProperty().unbind();
        logsTableOpenBtn.disableProperty().unbind();
        mainCtrl.getMenuFileOpen().disableProperty().unbind();
    }

    private final void subscribeParseInProgressProperty(final Observable<ParseInfo> parseInfoStream) {
        parseInfoStream.subscribe(new Subscriber<ParseInfo>() {

            @SuppressWarnings("synthetic-access")
            @Override
            public void onCompleted() {
                LOG.error("this observable is not supposed to complete"); //$NON-NLS-1$
            }

            @SuppressWarnings("synthetic-access")
            @Override
            public void onError(final Throwable e) {
                LOG.error("error occurred", e); //$NON-NLS-1$
            }

            @SuppressWarnings("synthetic-access")
            @Override
            public void onNext(@SuppressWarnings("unused") final ParseInfo parseInfo) {
                LOG.debug("toggle isParseInProgress : value = [%s]", isParseInProgress); //$NON-NLS-1$
                isParseInProgress.set(true);
            }
        });
    }

    private final void subscribeNumLinesProperty(final Observable<ParseInfo> parseInfoStream) {
        parseInfoStream.subscribe(new Subscriber<ParseInfo>() {

            @SuppressWarnings({ "synthetic-access" })
            @Override
            public void onCompleted() {
                LOG.error("this observable is not supposed to complete"); //$NON-NLS-1$
            }

            @SuppressWarnings("synthetic-access")
            @Override
            public void onError(final Throwable e) {
                LOG.error("error occurred", e); //$NON-NLS-1$
            }

            @SuppressWarnings("synthetic-access")
            @Override
            public void onNext(final ParseInfo parseInfo) {
                LOG.debug("set numLinesToParse : value = [%s]", numLinesToParse); //$NON-NLS-1$
                numLinesToParse.set(parseInfo.getNumLines());
            }

        });
    }


    private final void subscribeLogTableContent(final Observable<LogEntry> logsStream) {
        logsStream.subscribe(new Subscriber<LogEntry>() {

            /**
             * Default system logger for this class.
             * <br/><b>Created on:</b> <i>4:51:37 PM Dec 29, 2015</i>
             */
            @SuppressWarnings({ "hiding", "nls" })
            private final Logger LOG = LogManager.getFormatterLogger(
                LogsController.class.getCanonicalName() + "$ANONYMOUS$LogTableUpdater");

            private final double PRECISION = 0.01D;

            /**
             * Received log entries are stored in this collection. When log entries emission completes, this collection
             * is
             * used to construct log table entries.
             * <br/><b>Created on:</b> <i>4:38:22 PM Dec 29, 2015</i>
             */
            private final Collection<LogEntry> logs = new LinkedList<>();

            private final int numLines = numLinesToParse.get();

            private volatile double prevProgress = 0.0D;


            /**
             * Update configuration and contents of log table using current state of logs ("logs" field).
             * <br/><b>PRE-conditions:</b> NONE
             * <br/><b>POST-conditions:</b> NONE
             * <br/><b>Side-effects:</b> log table state is changed
             * <br/><b>Created on:</b> <i>6:41:59 PM Dec 29, 2015</i>
             */
            @SuppressWarnings({ "synthetic-access", "nls" })
            private final void updateLogTable() {
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

                final Collection<LogEntryTableLine> tableLines = logs.stream()
                    .map(LogEntryTableLine::new)
                    .collect(Collectors.toList());

                Platform.runLater(() -> {
                    final Instant timeStart = Instant.now();

                    mainLogsTable.getColumns().clear();
                    mainLogsTable.refresh();
                    mainLogsTable.getColumns().add(colLine);
                    mainLogsTable.getColumns().add(colLog);
                    mainLogsTable.setFixedCellSize(fixedCellSize);

                    mainLogsTable.getItems().clear();
                    mainLogsTable.refresh();
                    mainLogsTable.getItems().addAll(tableLines);

                    LOG.debug("calling autosize......");
                    mainLogsTable.refresh();
                    mainLogsTable.requestLayout();

                    mainLogsTable.autoSizeFitContent();

                    final Duration timeSpent = Duration.between(timeStart, Instant.now());
                    LOG.debug("update UI parsed logs table data : timeSpent = [%s]", timeSpent);
                });
            }

            @Override
            public void onNext(final LogEntry log) {
                LOG.trace("inserting log into table logs collection : log = [%s]", log); //$NON-NLS-1$
                logs.add(log);

                // evaluate progress bar percent update
                final double progress = log.getLineNum() / (double) numLines;
                LOG.trace("progress = [%f]", progress);
                if (progress >= (prevProgress + PRECISION)) { // need update
                    prevProgress = progress;
                    Platform.runLater(() -> {
                        LOG.trace("update progress bar : progress = [%f]", progress);
                        mainCtrl.getStatusBarController().getProgressBar().setProgress(progress);
                    });
                }
            }

            @SuppressWarnings("synthetic-access")
            @Override
            public void onCompleted() {
                LOG.debug("finished parsing, updating log table"); //$NON-NLS-1$
                updateLogTable();

                // for inhibeans, close guard

                // toggle off parse-in-progress indicator
                isParseInProgress.set(false);
                Platform.runLater(() -> {
                    final ProgressBar bar = mainCtrl.getStatusBarController().getProgressBar();
                    bar.setProgress(0.0D);
                    LOG.trace("update progress bar - parse finished : progress = [%f]", bar.getProgress());
                });
                unbindParseInProgressProperty();
                LOG.trace("isParseInProgress indicator unbound"); //$NON-NLS-1$
            }

            @SuppressWarnings("synthetic-access")
            @Override
            public void onError(final Throwable error) {
                LOG.error("error when receiving log entries : error = [%s]", error.getClass().getSimpleName(), error); //$NON-NLS-1$
            }

        });
    }


    /**
     * Project : logmist.<br/>
     * Data object. Contains parsing information.
     * <br/><b>Created on:</b> <i>10:04:02 AM Dec 27, 2015</i>
     * 
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     */
    private static final class ParseInfo {

        /**
         * Default system logger for this class.
         * <br/><b>Created on:</b> <i>7:08:21 PM Feb 25, 2016</i>
         */
        private static final Logger LOG = LogManager.getFormatterLogger(ParseInfo.class);

        /**
         * Source of lines to be parsed.
         * <br/><b>Created on:</b> <i>7:43:12 PM Dec 28, 2015</i>
         */
        private final Observable<String> lines;

        /**
         * Emits only single parser to be used for parsing lines.
         * <br/><b>Created on:</b> <i>7:43:26 PM Dec 28, 2015</i>
         */
        private final Single<ILogFileParser> parser;

        /**
         * Total number of lines to be parsed in lines source.
         * <br/><b>Created on:</b> <i>7:43:47 PM Dec 28, 2015</i>
         */
        private final int numLines;


        /**
         * Constructor for class : [logmist] dburyak.logmist.ui.jfx.controllers.ParseInfo.<br/>
         * <br/><b>PRE-conditions:</b> non-null lines, non-null parser, non-negative numLines
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>7:44:19 PM Dec 28, 2015</i>
         * 
         * @param lines
         *            source of lines
         * @param parser
         *            source of single parser
         * @param numLines
         *            total number of lines in lines source
         */
        public ParseInfo(final Observable<String> lines, final Single<ILogFileParser> parser, final int numLines) {
            this.lines = lines;
            this.parser = parser;
            this.numLines = numLines;
        }

        /**
         * Factory method to produce {@link ParseInfo} from given file and parser. Effectively, reads entire file and
         * stores number of lines.
         * <br/><b>PRE-conditions:</b> non-null path, non-null and non-empty parser
         * <br/><b>POST-conditions:</b> non-null result
         * <br/><b>Side-effects:</b> I/O activity (file is read)
         * <br/><b>Created on:</b> <i>7:08:40 PM Feb 25, 2016</i>
         * 
         * @param path
         *            file to be parsed
         * @param parser
         *            source of all available parsers
         * @return parse info for given file and parsers
         */
        @SuppressWarnings("boxing")
        public static final ParseInfo from(final Path path, final Single<ILogFileParser> parser) {
            Observable<String> lines = Observable.empty();
            int size = 0;
            try {
                final Tuple2<Observable<String>, Integer> linesAndSize = Utils.asLinesObservableAndSize(path);
                lines = linesAndSize.get1();
                size = linesAndSize.get2();
            } catch (@SuppressWarnings("unused") final InaccessibleFileException ex) {
                LOG.error("cannot read file : path = [%s]", path); //$NON-NLS-1$
            }
            return new ParseInfo(lines, parser, size);
        }


        /**
         * Getter method.<br/>
         * <br/><b>POST-conditions:</b>
         * <br/><b>Side-effects:</b>
         * <br/><b>Created on:</b> <i>10:03:45 AM Dec 27, 2015</i>
         * 
         * @return the lines
         */
        public final Observable<String> getLines() {
            return lines;
        }


        /**
         * Getter method.<br/>
         * <br/><b>POST-conditions:</b>
         * <br/><b>Side-effects:</b>
         * <br/><b>Created on:</b> <i>10:03:45 AM Dec 27, 2015</i>
         * 
         * @return the parser
         */
        public final Single<ILogFileParser> getParser() {
            return parser;
        }


        /**
         * Getter method.<br/>
         * <br/><b>POST-conditions:</b>
         * <br/><b>Side-effects:</b>
         * <br/><b>Created on:</b> <i>10:03:45 AM Dec 27, 2015</i>
         * 
         * @return the numLines
         */
        public final int getNumLines() {
            return numLines;
        }

        /**
         * Get string representation of this parse info.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> non-empty result
         * <br/><b>Side-effects:</b> hash code is evaluated
         * <br/><b>Created on:</b> <i>7:45:23 PM Dec 28, 2015</i>
         * 
         * @see java.lang.Object#toString()
         * @return string representation of this parse info
         */
        @SuppressWarnings("nls")
        @Override
        public String toString() {
            final StringBuilder sb = (new StringBuilder("{hash=[")).append(this.hashCode())
                .append("],numLines=[").append(numLines)
                .append("],lines=[").append(lines)
                .append("],parser=[").append(parser).append("]}");
            return sb.toString();
        }

    }


    private final void loadParsers() {
        LOG.entry();
        if (areParsersInitialized) {
            LOG.exit();
            return;
        }

        LOG.info("instantiating log parsers");
        final Resources res = Resources.getInstance();
        // first - default parser, which is used when none of other parsers can recognize format
        assert (!res.isUndefined(ConfigID.CORE_PARSERS_DEFAULT));
        final String parserDefaultClassStr = res.getConfigProp(ConfigID.CORE_PARSERS_DEFAULT);
        try {
            final Object parserDefaultObj = Class.forName(parserDefaultClassStr).newInstance();
            assert (parserDefaultObj instanceof ILogFileParser);
            parserDefault = (ILogFileParser) parserDefaultObj;
            LOG.info("default log parser : className = [%s]", parserDefaultClassStr);
        } catch (final InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOG.catching(Level.TRACE, e);
            LOG.fatal("cannot instantiate default parser : className = [%s]", parserDefaultClassStr);
            throw LOG.throwing(Level.TRACE,
                new AssertionError("default log parser must be instantiable via default constructor", e));
        }

        // instantiate secondary parsers and add them to the list
        parsers.clear();
        final String[] secClassesStr = res.getConfigProp(ConfigID.CORE_PARSERS_SECONDARY).split(",");
        LOG.debug("secondary log parsers in config : num = [%d]", secClassesStr.length);
        parsers.addAll(Arrays.stream(secClassesStr)
            .map(className -> { // instantiate parsers by className
                try {
                    final ILogFileParser parser = (ILogFileParser) Class.forName(className).newInstance();
                    LOG.info("log parser registered : className = [%s]", className);
                    return parser;
                } catch (final Exception e) {
                    LOG.catching(Level.TRACE, e);
                    LOG.warn("cannot instantiate log parser, discarding : className = [%s]", className);
                    return null;
                }
            }).filter(parser -> parser != null) // filter out failed parsers
            .collect(Collectors.toList()));

        // init parse service
        assert (parseSrv == null);
        parseSrv = new ParseService(parserDefault);

        areParsersInitialized = true;
        LOG.exit();
    }

    /**
     * Get the observable that opens file open dialog on subscibtion. Chosen file is emitted.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> I/O activity when reading file stats; MAIN_LOGS_FILE_CHOOSER_DIR config property is
     * changed
     * <br/><b>Created on:</b> <i>7:48:48 PM Dec 26, 2015</i>
     * 
     * @return observable that emits chosen file
     */
    private static final Observable<Path> chooseFile() {
        final Stage stage = LogmistJFXApp.getInstance().getStage();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(Resources.getInstance().getMsg(MsgID.MAIN_LOGS_FILE_CHOOSER_TITLE));
        final Path dir = Paths.get(Resources.getInstance().getConfigProp(ConfigID.MAIN_LOGS_FILE_CHOOSER_DIR));
        if (Files.exists(dir)) {
            fileChooser.setInitialDirectory(dir.toFile());
        } else {
            fileChooser.setInitialDirectory(Paths.get(DEFAULT_DIR_PATH).toFile());
        }
        return Single.<Path> create(subscriber -> {
            final File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                final Path dirChosen = file.getParentFile().toPath();
                Resources.getInstance().setConfigProp(ConfigID.MAIN_LOGS_FILE_CHOOSER_DIR,
                    dirChosen.toAbsolutePath().toString());
                LOG.trace("logs file chosen : file = [%s]", file.toPath().toString()); //$NON-NLS-1$
            } // else user cancelled opening
            subscriber.onSuccess((file != null) ? file.toPath() : null);
        }).toObservable();
    }

    /**
     * Choose file parser (any first matching) that can parse given file.
     * <br/><b>PRE-conditions:</b> non-null and accessible and readable filePath
     * <br/><b>POST-conditions:</b> non-null result, always emits a value
     * <br/><b>Side-effects:</b> I/O activity on given file (open, read, close)
     * <br/><b>Created on:</b> <i>3:48:49 AM Dec 23, 2015</i>
     * 
     * @param filePath
     *            file to be tested
     * @return single that emits parser capable to parse given file
     */
    @SuppressWarnings("nls")
    private final Single<ILogFileParser> chooseParser(final Path filePath) {
        LOG.debug("choosing parser : filePath = [%s]", filePath);
        final int numLines = Integer.parseInt(
            Resources.getInstance().getConfigProp(ConfigID.CORE_PARSERS_NUM_LINES_TO_TEST));

        try {
            final Observable<String> linesSource = Utils.asLinesObservable(filePath)
                .take(numLines)
                .observeOn(Schedulers.io())
                .cache()
                .observeOn(Schedulers.computation());

            final Observable<Boolean> canParseObservable = Observable.concatEager(  // concat in direct order
                parsers.stream()
                    .map(parser -> parser.canParse(linesSource).toObservable())
                    .collect(Collectors.toList()));

            final Observable<ILogFileParser> parsersObservable = Observable.from(parsers);

            return Observable.zip(parsersObservable, canParseObservable,
                (parser, parseResult) -> {
                    LOG.debug("parse result for file : canParse = [%b] ; parser = [%s]", parseResult, parser);
                    return Tuples.t(parser, parseResult);
                })
                .filter(t2 -> t2.get2())
                .map(t2 -> t2.get1())   // extract parser from pair
                .firstOrDefault(parserDefault)
                .doOnNext(parser -> LOG.debug("parser chosen for file : parser = [%s] ; file = [%s]", parser, filePath))
                .toSingle();
        } catch (final InaccessibleFileException ex) {
            LOG.catching(Level.TRACE, ex);
            LOG.error("cannot read file : filePath", filePath); //$NON-NLS-1$
            return Single.error(LOG.throwing(Level.TRACE, ex));
        }
    }

    /**
     * Get event stream that sends mouse click events on "Open ..." button in log table.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>8:32:21 PM Dec 25, 2015</i>
     * 
     * @return event stream of "Open ..." button (in log table) mouse click events
     */
    public final EventStream<ActionEvent> clicksOpenBtnStream() {
        validateClicksOpenBtn(clicksOpenBtn);
        return clicksOpenBtn;
    }

}
