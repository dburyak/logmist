package dburyak.logmist.ui.jfx;


import java.time.Duration;
import java.time.Instant;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.javafx.scene.control.skin.TableViewSkin;

import dburyak.logmist.ui.jfx.controllers.LogEntryTableLine;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Skin;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Region;
import javafx.util.Callback;


/**
 * Project : logmist.<br/>
 * <br/><b>Created on:</b> <i>2:21:53 AM Oct 6, 2015</i>
 * Table view of {@link LogEntryTableLine} that can be auto-resized so all the contens are not trimmed.<br/>
 * <i>This is a kind of a workaround, since there is no public method to be called in {@link TableView} to do
 * auto-resizing. However such code resides in protected method in {@link Skin} for {@link TableView} and can be
 * re-used.</i>
 * 
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@SuppressWarnings("restriction")
public final class AutoSizableLogTableView extends TableView<LogEntryTableLine> {

    /**
     * Default system logger for this class.
     * <br/><b>Created on:</b> <i>2:47:31 AM Oct 6, 2015</i>
     */
    private static final Logger LOG = LogManager.getFormatterLogger(AutoSizableLogTableView.class);


    /**
     * Project : logmist.<br/>
     * <br/><b>Created on:</b> <i>2:30:50 AM Oct 6, 2015</i>
     * Table skin that can perform auto-resizing of table columns.
     * 
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     */
    private static final class AutoSizableLogTableViewSkin extends TableViewSkin<LogEntryTableLine> {

        /**
         * Project : logmist.<br/>
         * <br/><b>Created on:</b> <i>10:32:26 PM Oct 30, 2015</i>
         * 
         * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
         * @version 0.1
         */
        private static final class ColumnAutosizingTask implements Runnable {

            /**
             * Default system logger for this class.
             * <br/><b>Created on:</b> <i>10:34:32 PM Oct 30, 2015</i>
             */
            @SuppressWarnings("hiding")
            private static final Logger LOG = LogManager.getFormatterLogger(ColumnAutosizingTask.class);

            /**
             * Column to be auto-resized by this task.
             * <br/><b>Created on:</b> <i>10:36:06 PM Oct 30, 2015</i>
             */
            private final TableColumn<LogEntryTableLine, ?> column;

            /**
             * Table view skin that initiates this resizing task.
             * <br/><b>Created on:</b> <i>10:40:11 PM Oct 30, 2015</i>
             */
            private final AutoSizableLogTableViewSkin skin;


            /**
             * Constructor for class : [logmist] dburyak.logmist.ui.jfx.ColumnAutosizingTask.<br/>
             * <br/><b>PRE-conditions:</b> non-null column
             * <br/><b>POST-conditions:</b> NONE
             * <br/><b>Side-effects:</b> NONE
             * <br/><b>Created on:</b> <i>10:36:03 PM Oct 30, 2015</i>
             * 
             * @param column
             *            table column to be auto-resized
             * @param skin
             *            table skin that knows internals needed to do resizing job
             */
            public ColumnAutosizingTask(
                final TableColumn<LogEntryTableLine, ?> column,
                final AutoSizableLogTableViewSkin skin) {

                this.column = column;
                this.skin = skin;
            }

            /**
             * Do resizing.
             * <br/><b>PRE-conditions:</b> NONE
             * <br/><b>POST-conditions:</b> NONE
             * <br/><b>Side-effects:</b> column is resized (asynchronous resizing task on JFX thread is started)
             * <br/><b>Created on:</b> <i>10:34:43 PM Oct 30, 2015</i>
             * 
             * @see java.lang.Runnable#run()
             */
            @SuppressWarnings({ "nls", "boxing", "synthetic-access", "deprecation" })
            @Override
            public void run() {
                // final TableColumn<T, ?> col = tc;
                final Collection<LogEntryTableLine> items = skin.itemsProperty().get();
                if (items == null) {
                    LOG.warn("expected that table contains non-null items property");
                    return;
                } else if (items.isEmpty()) {
                    LOG.warn("resizing of empty column called : column = [%s]", column.getText());
                    return;
                }

                @SuppressWarnings("rawtypes") final Callback/* <TableColumn<T, ?>, TableCell<T,?>> */ cellFactory =
                    column.getCellFactory();
                if (cellFactory == null) {
                    return;
                }

                @SuppressWarnings("unchecked") final TableCell<LogEntryTableLine, ?> cell =
                    (TableCell<LogEntryTableLine, ?>) cellFactory.call(column);
                if (cell == null) {
                    return;
                }

                // find item with the longest text
                LOG.debug("start search for longest cell in column : column = [%s]", column.getText());
                final Instant startTime = Instant.now();
                String longestStr = null;
                int longestRow = 0;
                for (int row = 0; row < items.size(); row++) {
                    cell.updateTableColumn(column);
                    cell.updateTableView(skin.tableView);
                    cell.updateIndex(row);

                    final String cellText = cell.getText();
                    if (cellText == null) {
                        LOG.warn("unexpected null in table cell : column = [%s] ; index = [%d]",
                            column.getText(), row);
                        continue;
                    }

                    if ((longestStr == null) || (cellText.length() > longestStr.length())) {
                        longestStr = cellText;
                        longestRow = row;
                    }
                }
                LOG.debug("longest cell found in column : time = [%s] ; column = [%s] ; longestRow = [%d] ; "
                    + "longestStr = [%s]",
                    Duration.between(startTime, Instant.now()), column.getText(), longestRow, longestStr);

                // calculate necessary column size by putting longest text to a temporary cell and rendering CSS
                cell.getProperties().put("deferToParentPrefWidth", Boolean.TRUE);
                double padding = GAP_PIXELS;
                final Node n = (cell.getSkin() == null) ? null : cell.getSkin().getNode();
                if (n instanceof Region) {
                    final Region r = (Region) n;
                    padding = r.snappedLeftInset() + r.snappedRightInset();
                } else {
                    LOG.debug("unexpected node type for table cell skin : node = [%s]", n);
                }

                final String longestStrResult = longestStr; // need to be final
                final double paddingResult = padding;
                Platform.runLater(() -> {
                    cell.updateTableColumn(column);
                    cell.updateTableView(skin.tableView);
                    cell.updateIndex(0); // insert in the beginning
                    cell.setText(longestStrResult);
                    LOG.debug("adding temp cell to the table : children = [%b]", (skin.getChildren() == null));
                    if (!skin.getChildren().add(cell)) {
                        LOG.error("cannot add temporary cell to table view");
                        return;
                    }
                    LOG.debug("temp cell updated and ready to be styled/rendered");
                    cell.applyCss();
                    LOG.debug("css applied to the cell successfully");
                    final double textWidth = cell.prefWidth(-1);
                    LOG.debug("width of rendered text : textWidth = [%f]", textWidth);
                    double width = paddingResult + textWidth;


                    // dispose of the cell to prevent it retaining listeners (see RT-31015)
                    skin.getChildren().remove(cell);
                    cell.updateIndex(-1);

                    width += GAP_PIXELS; // just in case for small cells
                    if (skin.tableView.getColumnResizePolicy() == TableView.CONSTRAINED_RESIZE_POLICY) {
                        width = Math.max(width, column.getWidth());
                    }
                    final double widthCalculated = width;


                    // make it resizable for a while (ideally, all the columns should be NOT resizable
                    column.setResizable(true);
                    column.impl_setWidth(widthCalculated);
                    column.setResizable(false);
                });

                // FIXME : old approach (Java standard lib, internal JFX code)
                // tableView.getColumns().stream().forEach(column -> {
                // if (!column.isResizable()) {
                // return;
                // }
                //
                // // final TableColumn<T, ?> col = tc;
                // final List<?> items = itemsProperty().get();
                // if ((items == null) || items.isEmpty()) {
                // return;
                // }
                //
                // final Callback/* <TableColumn<T, ?>, TableCell<T,?>> */ cellFactory = column.getCellFactory();
                // if (cellFactory == null) {
                // return;
                // }
                //
                // final TableCell<LogEntryTableLine, ?> cell = (TableCell<LogEntryTableLine, ?>)
                // cellFactory.call(column);
                // if (cell == null) {
                // return;
                // }
                //
                // // set this property to tell the TableCell we want to know its actual
                // // preferred width, not the width of the associated TableColumnBase
                // cell.getProperties().put("deferToParentPrefWidth", Boolean.TRUE);
                //
                // // determine cell padding
                // double padding = 10;
                // final Node n = cell.getSkin() == null ? null : cell.getSkin().getNode();
                // if (n instanceof Region) {
                // final Region r = (Region) n;
                // padding = r.snappedLeftInset() + r.snappedRightInset();
                // }
                //
                // final int rows = items.size();
                // double maxWidth = 0;
                // for (int row = 0; row < rows; row++) {
                // cell.updateTableColumn(column);
                // cell.updateTableView(tableView);
                // cell.updateIndex(row);
                //
                // if (((cell.getText() != null) && !cell.getText().isEmpty()) || (cell.getGraphic() != null)) {
                // getChildren().add(cell);
                // cell.applyCss();
                // maxWidth = Math.max(maxWidth, cell.prefWidth(-1));
                // getChildren().remove(cell);
                // }
                // }
                //
                // // dispose of the cell to prevent it retaining listeners (see RT-31015)
                // cell.updateIndex(-1);
                //
                // maxWidth += padding + 10; // for small line numbers
                // if (tableView.getColumnResizePolicy() == TableView.CONSTRAINED_RESIZE_POLICY) {
                // maxWidth = Math.max(maxWidth, column.getWidth());
                // }
                //
                // column.impl_setWidth(maxWidth);
                // });


            }

        }


        /**
         * Default system logger for this class.
         * <br/><b>Created on:</b> <i>2:35:50 AM Oct 6, 2015</i>
         */
        @SuppressWarnings("hiding")
        private static final Logger LOG = LogManager.getFormatterLogger(AutoSizableLogTableViewSkin.class);

        /**
         * Additional "just in case" gap to be used when calculating necessary size for column.
         * <br/><b>Created on:</b> <i>8:53:17 AM Oct 30, 2015</i>
         */
        private static final double GAP_PIXELS = 10D;

        /**
         * Table view this skin is instantiated for.
         * <br/><b>Created on:</b> <i>2:35:20 AM Oct 6, 2015</i>
         */
        private final TableView<LogEntryTableLine> tableView;


        /**
         * Constructor for class : [logmist] dburyak.logmist.ui.jfx.AutoSizableLogTableViewSkin.<br/>
         * <br/><b>PRE-conditions:</b> non-null tableView
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>2:36:20 AM Oct 6, 2015</i>
         * 
         * @param tableView
         *            table view this skin is tied to
         */
        public AutoSizableLogTableViewSkin(final TableView<LogEntryTableLine> tableView) {
            super(tableView);
            this.tableView = tableView;
        }

        /**
         * Auto resize all columns of table this skin is bound to.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> this is time consuming operation when table has large amount of data
         * <br/><b>Created on:</b> <i>2:38:04 AM Oct 6, 2015</i>
         */
        @SuppressWarnings("nls")
        public final void autoResizeAllColumns() {
            for (final TableColumn<LogEntryTableLine, ?> column : tableView.getColumns()) {
                final ColumnAutosizingTask resizeTask = new ColumnAutosizingTask(column, this);
                LOG.debug("column resizing task submitted : column = [%s]", column.getText());
                LogmistJFXApp.getInstance().getThreadPool().submit(resizeTask);
            }
        }

    }


    /**
     * Auto-sizing capable skin tied to this table.
     * <br/><b>Created on:</b> <i>2:48:04 AM Oct 6, 2015</i>
     */
    private final AutoSizableLogTableViewSkin skin;


    private final void init() {
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getSelectionModel().getSelectedItems().addListener((ListChangeListener<LogEntryTableLine>) c -> {
            if (c.getList().size() == 1) {
                LOG.info("single selection : line num = [%d]", c.getList().iterator().next().getLineNum());
            } else if (c.getList().size() > 1) {
                LOG.info("multiple selection : size = [%d]", c.getList().size());
            } else {
                LOG.warn("unexpected");
            }
        });
    }


    /**
     * Constructor for class : [logmist] dburyak.logmist.ui.jfx.AutoSizableTableView.<br/>
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:21:53 AM Oct 6, 2015</i>
     */
    public AutoSizableLogTableView() {
        super();
        skin = new AutoSizableLogTableViewSkin(this);
        setSkin(skin);
        init();
    }

    /**
     * Constructor for class : [logmist] dburyak.logmist.ui.jfx.AutoSizableTableView.<br/>
     * <br/><b>PRE-conditions:</b> non-null items
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:21:53 AM Oct 6, 2015</i>
     * 
     * @param items
     *            data for this table
     */
    public AutoSizableLogTableView(final ObservableList<LogEntryTableLine> items) {
        super(items);
        skin = new AutoSizableLogTableViewSkin(this);
        setSkin(skin);
        init();
    }

    /**
     * Auto-size columns of this table that way, so all the data fits (not trimmed).
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> this is time consuming operation when table holds large amount of data
     * <br/><b>Created on:</b> <i>2:23:54 AM Oct 6, 2015</i>
     */
    public final void autoSizeFitContent() {
        LOG.debug("start auto-resizing of log table"); //$NON-NLS-1$
        skin.autoResizeAllColumns();
    }

}
