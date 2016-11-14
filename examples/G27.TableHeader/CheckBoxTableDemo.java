/*
 * @(#)CheckBoxTableDemo.java 3/6/2012
 *
 * Copyright 2002 - 2012 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.DoubleConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.*;
import com.jidesoft.icons.CheckBoxIcon;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.TristateCheckBox;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

public class CheckBoxTableDemo extends AbstractDemo {
    protected SortableTable _sortableTable;
    private TableModel _model;
    private TableModelListener _listener;
    private static final long serialVersionUID = -5373007282200581748L;

    public CheckBoxTableDemo() {
        ObjectConverterManager.initDefaultConverter();
        DecimalFormat format = new DecimalFormat();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(10);
        DoubleConverter converter = new DoubleConverter(format);
        ObjectConverterManager.registerConverter(Double.class, converter);
    }

    public String getName() {
        return "CellStyleTableHeader Demo (CheckBoxTable)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of CellStyleTableHeader. \n" +
                "\n Please click the check box icon on both the table header and the table to change selections.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.CellStyleTableHeader;" +
                "com.jidesoft.grid.HeaderStyleModel;" +
                "com.jidesoft.icon.CheckBoxIcon";
    }

    public Component getDemoPanel() {
        _model = DemoData.createQuoteTableModelWithSelected(true);

        _listener = new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                int viewColumn = _sortableTable.convertColumnIndexToView(0);
                if (viewColumn < 0) {
                    return;
                }
                boolean hasUnselected = false;
                boolean hasSelected = false;
                for (int row = 0; row < _sortableTable.getRowCount(); row++) {
                    Object value = _sortableTable.getValueAt(row, viewColumn);
                    if (value instanceof Boolean) {
                        if ((Boolean) value) {
                            hasSelected = true;
                        }
                        else {
                            hasUnselected = true;
                        }
                        if (hasSelected && hasUnselected) {
                            break;
                        }
                    }
                }
                int state;
                if (hasSelected && hasUnselected) {
                    state = TristateCheckBox.STATE_MIXED;
                }
                else if (hasSelected) {
                    state = TristateCheckBox.STATE_SELECTED;
                }
                else {
                    state = TristateCheckBox.STATE_UNSELECTED;
                }
                CellStyle style = ((HeaderStyleModel) _model).getHeaderStyleAt(0, 0);
                if (style.getIcon() instanceof CheckBoxIcon) {
                    ((CheckBoxIcon) style.getIcon()).setState(state);
                    _sortableTable.getTableHeader().repaint();
                }
            }
        };
        _model.addTableModelListener(_listener);
        _sortableTable = new SortableTable(_model);
        _sortableTable.setAutoResizeMode(JideTable.AUTO_RESIZE_FILL);
        _sortableTable.setFillsGrids(false);
        SortableTableModel sortableTableModel = (SortableTableModel) TableModelWrapperUtils.getActualTableModel(_sortableTable.getModel(), SortableTableModel.class);
        sortableTableModel.setColumnSortable(0, false);
        AutoFilterTableHeader header = new AutoFilterTableHeader(_sortableTable) {
            @Override
            protected IFilterableTableModel createFilterableTableModel(TableModel model) {
                return new FilterableTableModel(model) {
                    private static final long serialVersionUID = 3712608251826078508L;

                    @Override
                    public boolean isColumnAutoFilterable(int column) {
                        return column != 0;
                    }
                };
            }
        };
        header.setAutoFilterEnabled(true);
        header.setUseNativeHeaderRenderer(true);
        _sortableTable.setTableHeader(header);
        JideSwingUtilities.insertMouseListener(header, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() instanceof CellStyleTableHeader) {
                    CellStyleTableHeader _header = (CellStyleTableHeader) e.getSource();
                    Point p = e.getPoint();
                    TableColumnModel columnModel = _header.getColumnModel();
                    int index = _header.originalColumnAtPoint(p);
                    if (_header.getTable() != null && index >= 0 && index < columnModel.getColumnCount()) {
                        TableModel actualTableModel = TableModelWrapperUtils.getActualTableModel(_header.getTable().getModel(), HeaderStyleModel.class);
                        if (actualTableModel instanceof HeaderStyleModel && ((HeaderStyleModel) actualTableModel).isHeaderStyleOn()) {
                            int actualColumnIndex = TableModelWrapperUtils.getActualColumnAt(_header.getTable().getModel(), _header.getTable().convertColumnIndexToModel(index), actualTableModel);
                            int rowIndex = _header instanceof NestedTableHeader ? ((NestedTableHeader) _header).getRowCount() - 1 : 0;
                            CellStyle style = ((HeaderStyleModel) actualTableModel).getHeaderStyleAt(rowIndex, actualColumnIndex);
                            Rectangle headerRect = _header.getHeaderRect(index);
                            Point centerPoint = new Point(headerRect.x + headerRect.width / 2, headerRect.y + headerRect.height / 2);
                            if (style != null && style.getIcon() instanceof CheckBoxIcon && p.x >= centerPoint.x - style.getIcon().getIconWidth() / 2 && p.x <= centerPoint.x + style.getIcon().getIconWidth() / 2 && p.y >= centerPoint.y - style.getIcon().getIconHeight() / 2 && p.y <= centerPoint.y + style.getIcon().getIconHeight() / 2) {
                                int state = ((CheckBoxIcon) style.getIcon()).getState();
                                if (state == TristateCheckBox.STATE_MIXED || state == TristateCheckBox.STATE_UNSELECTED) {
                                    state = TristateCheckBox.STATE_SELECTED;

                                }
                                else if (state == TristateCheckBox.STATE_SELECTED) {
                                    state = TristateCheckBox.STATE_UNSELECTED;
                                }
                                ((CheckBoxIcon) style.getIcon()).setState(state);
                                _model.removeTableModelListener(_listener);
                                for (int row = 0; row < _model.getRowCount(); row++) {
                                    _model.setValueAt(state == TristateCheckBox.STATE_SELECTED, row, 0);
                                }
                                _model.addTableModelListener(_listener);
                                e.consume();
                            }
                        }
                    }
                }
            }
        }, 0);
        TableHeaderPopupMenuInstaller installer = new TableHeaderPopupMenuInstaller(_sortableTable);
        installer.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());

        TableUtils.autoResizeAllColumns(_sortableTable);

        return new JScrollPane(_sortableTable);
    }

    @Override
    public String getDemoFolder() {
        return "G27.TableHeader";
    }

    static public void main(String[] s) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
                showAsFrame(new CheckBoxTableDemo());
            }
        });

    }

    public void dispose() {
    }
}