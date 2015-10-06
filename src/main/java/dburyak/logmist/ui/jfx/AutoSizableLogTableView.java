package dburyak.logmist.ui.jfx;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.javafx.scene.control.skin.TableViewSkin;

import dburyak.logmist.ui.jfx.controllers.LogEntryTableLine;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.control.TableCell;
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
         * Default system logger for this class.
         * <br/><b>Created on:</b> <i>2:35:50 AM Oct 6, 2015</i>
         */
        private static final Logger LOG = LogManager.getFormatterLogger(AutoSizableLogTableViewSkin.class);

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
        public final void autoResizeAllColumns() {
            tableView.getColumns().stream().forEach(column -> {
                if (!column.isResizable()) {
                    return;
                }

                // final TableColumn<T, ?> col = tc;
                final List<?> items = itemsProperty().get();
                if ((items == null) || items.isEmpty()) {
                    return;
                }

                final Callback/* <TableColumn<T, ?>, TableCell<T,?>> */ cellFactory = column.getCellFactory();
                if (cellFactory == null) {
                    return;
                }

                final TableCell<LogEntryTableLine, ?> cell = (TableCell<LogEntryTableLine, ?>) cellFactory.call(column);
                if (cell == null) {
                    return;
                }

                // set this property to tell the TableCell we want to know its actual
                // preferred width, not the width of the associated TableColumnBase
                cell.getProperties().put("deferToParentPrefWidth", Boolean.TRUE);

                // determine cell padding
                double padding = 10;
                final Node n = cell.getSkin() == null ? null : cell.getSkin().getNode();
                if (n instanceof Region) {
                    final Region r = (Region) n;
                    padding = r.snappedLeftInset() + r.snappedRightInset();
                }

                final int rows = items.size();
                double maxWidth = 0;
                for (int row = 0; row < rows; row++) {
                    cell.updateTableColumn(column);
                    cell.updateTableView(tableView);
                    cell.updateIndex(row);

                    if (((cell.getText() != null) && !cell.getText().isEmpty()) || (cell.getGraphic() != null)) {
                        getChildren().add(cell);
                        cell.applyCss();
                        maxWidth = Math.max(maxWidth, cell.prefWidth(-1));
                        getChildren().remove(cell);
                    }
                }

                // dispose of the cell to prevent it retaining listeners (see RT-31015)
                cell.updateIndex(-1);

                maxWidth += padding + 10; // for small line numbers
                if (tableView.getColumnResizePolicy() == TableView.CONSTRAINED_RESIZE_POLICY) {
                    maxWidth = Math.max(maxWidth, column.getWidth());
                }

                column.impl_setWidth(maxWidth);


            });
        }

    }


    /**
     * Auto-sizing capable skin tied to this table.
     * <br/><b>Created on:</b> <i>2:48:04 AM Oct 6, 2015</i>
     */
    private final AutoSizableLogTableViewSkin skin;


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
    }

    /**
     * Auto-size columns of this table that way, so all the data fits (not trimmed).
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> this is time consuming operation when table holds large amount of data
     * <br/><b>Created on:</b> <i>2:23:54 AM Oct 6, 2015</i>
     */
    public final void autoSizeFitContent() {
        skin.autoResizeAllColumns();
    }

}
