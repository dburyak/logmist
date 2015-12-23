/**
 * 
 */
package dburyak.logmist.ui.jfx.controllers;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.logmist.Resources;
import dburyak.logmist.Resources.ConfigID;
import dburyak.logmist.Resources.MsgID;
import dburyak.logmist.Utils;
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
import rx.Observable;
import rx.Single;
import rx.schedulers.Schedulers;


/**
 * @author Андрей
 */
public final class LogsController {

    private static final Logger LOG = LogManager.getFormatterLogger(LogsController.class);

    private MainController mainCtrl;
    private boolean areParsersInitialized = false;
    private final Collection<ILogFileParser> parsers = new ArrayList<>();
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
        if (areParsersInitialized) {
            LOG.exit();
            return;
        }

        LOG.info("initializing log parsers");
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
    private final Single<ILogFileParser> chooseParser(final Path filePath) {
        final int numLines = Integer.parseInt(
            Resources.getInstance().getConfigProp(ConfigID.CORE_PARSERS_NUM_LINES_TO_TEST));

        try {
            final Observable<String> linesSource = Utils.asLinesObservable(filePath)
                .observeOn(Schedulers.io())
                .take(numLines)
                .cache()
                .observeOn(Schedulers.computation());

            final Observable<Boolean> canParseObservable = Observable.concatEager(  // concat in direct order
                parsers.stream()
                    .map(parser -> parser.canParse(linesSource).toObservable())
                    .collect(Collectors.toList()));

            final Observable<ILogFileParser> parsersObservable = Observable.from(parsers);

            return Observable.zip(parsersObservable, canParseObservable,
                (parser, parseResult) -> {
                    return new AbstractMap.SimpleImmutableEntry<>(parser, parseResult);
                }).filter(entry -> entry.getValue())
                .first()
                .map(pair -> pair.getKey())   // extract parser from pair
                .defaultIfEmpty(parserDefault)
                .toSingle();
        } catch (final InaccessibleFileException ex) {
            LOG.catching(Level.TRACE, ex);
            LOG.error("cannot read file : filePath", filePath); //$NON-NLS-1$
            return Single.error(LOG.throwing(Level.TRACE, ex));
        }
    }


    /**
     * Callback to be called on "Open..." button click.
     * <br/><b>PRE-conditions:</b> non-null event
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> I/O activity if user chooses some file, state change
     * <br/><b>Created on:</b> <i>3:58:41 AM Dec 23, 2015</i>
     * 
     * @param event
     *            button click event
     */
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
