/*
 * @(#)FilterableTableDemo.java
 *
 * Copyright 2002 - 2003 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideTabbedPane;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link FilterableTableModel} <br> Required jar files: jide-common.jar, jide-grids.jar <br> Required
 * L&F: any L&F
 */
public class FilterableTableDemo extends AbstractDemo {
    private static final long serialVersionUID = 4281658183288221123L;
    private String _preference;
    private SortableTable _table;

    public FilterableTableDemo() {
    }

    public String getName() {
        return "FilterableTableModel Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of FilterableTableModel. FilterableTableModel is a table model which supports filters on columns. You can add filters so that only data the satisfied the filters will be shown in the table.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.FilterableTableModel\n" +
                "com.jidesoft.grid.Filter";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        JButton addRowsSelection = new JButton(new AbstractAction("Add one row before the selected row") {
            private static final long serialVersionUID = 4738900276109744653L;

            public void actionPerformed(ActionEvent e) {
                int rowIndex = _table.getSelectionModel().getMinSelectionIndex();
                if (rowIndex == -1) {
                    return;
                }
                int actualRowIndex = TableModelWrapperUtils.getActualRowAt(_table.getModel(), rowIndex, DemoData.ProductTableModel.class);
                DemoData.ProductTableModel innerModel = (DemoData.ProductTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), DemoData.ProductTableModel.class);
                innerModel.getDataVector().add(actualRowIndex, innerModel.getDataVector().get(0));
                innerModel.fireTableRowsInserted(actualRowIndex, actualRowIndex);
            }
        });

        JButton deleteRow = new JButton(new AbstractAction("Delete the selected row(s)") {
            private static final long serialVersionUID = -8456309293833159549L;

            public void actionPerformed(ActionEvent e) {
                int startIndex = _table.getSelectionModel().getMinSelectionIndex();
                int endIndex = _table.getSelectionModel().getMaxSelectionIndex();
                for (int rowIndex = endIndex; rowIndex >= startIndex; rowIndex--) {
                    if (rowIndex < 0 || rowIndex >= _table.getRowCount()) {
                        return;
                    }
                    int actualRowIndex = TableModelWrapperUtils.getActualRowAt(_table.getModel(), rowIndex, DemoData.ProductTableModel.class);
                    DemoData.ProductTableModel innerModel = (DemoData.ProductTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), DemoData.ProductTableModel.class);
                    innerModel.removeRow(actualRowIndex);
                }
            }
        });

        JButton testDeleteRow = new JButton(new AbstractAction("Delete almost all rows") {
            private static final long serialVersionUID = -5220068228238042288L;

            public void actionPerformed(ActionEvent e) {
                DemoData.ProductTableModel innerModel = (DemoData.ProductTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), DemoData.ProductTableModel.class);
                if (innerModel.getRowCount() >= 3) {
                    int endRow = innerModel.getRowCount() - 3;
                    for (int i = endRow; i >= 0; i--) {
                        innerModel.getDataVector().remove(i);
                    }
                    innerModel.fireTableRowsDeleted(0, endRow);
                }
            }
        });

        JButton fireTableStructureChangedEvent = new JButton(new AbstractAction("Fire table structure changed event") {
            private static final long serialVersionUID = 4604545883324437771L;

            public void actionPerformed(ActionEvent e) {
                DemoData.ProductTableModel innerModel = (DemoData.ProductTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), DemoData.ProductTableModel.class);
                innerModel.fireTableStructureChanged();
            }
        });

        JButton fireTableDataChangedEvent = new JButton(new AbstractAction("Fire table data changed event") {
            private static final long serialVersionUID = 7284092794187071284L;

            public void actionPerformed(ActionEvent e) {
                DemoData.ProductTableModel innerModel = (DemoData.ProductTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), DemoData.ProductTableModel.class);
                innerModel.fireTableDataChanged();
            }
        });

        JButton fireTableRowUpdatedEvent = new JButton(new AbstractAction("Fire table row updated event") {
            private static final long serialVersionUID = 4443293008936148279L;

            public void actionPerformed(ActionEvent e) {
                DemoData.ProductTableModel innerModel = (DemoData.ProductTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), DemoData.ProductTableModel.class);
                if (innerModel.getRowCount() >= 2) {
                    innerModel.fireTableRowsUpdated(0, innerModel.getRowCount() - 2);
                }
            }
        });

        JButton fireTableCellUpdatedEvent = new JButton(new AbstractAction("Fire table cell updated event") {
            private static final long serialVersionUID = -6493447457782714380L;

            public void actionPerformed(ActionEvent e) {
                DemoData.ProductTableModel innerModel = (DemoData.ProductTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), DemoData.ProductTableModel.class);
                for (int row = innerModel.getRowCount() - 1; row >= Math.max(innerModel.getRowCount() - 11, 0); row--) {
                    innerModel.fireTableCellUpdated(row, 0);
                }
            }
        });

        JButton saveFilterPreference = new JButton(new AbstractAction("Save Filter Preference") {
            private static final long serialVersionUID = -4963472694624341746L;

            public void actionPerformed(ActionEvent e) {
                _preference = TableUtils.getFilterableTableModelPreference((IFilterableTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), IFilterableTableModel.class));
            }
        });

        JButton loadFilterPreference = new JButton(new AbstractAction("Load Filter Preference") {
            private static final long serialVersionUID = -8737479769703899324L;

            public void actionPerformed(ActionEvent e) {
                TableUtils.setFilterableTableModelPreference((IFilterableTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), IFilterableTableModel.class), _preference);
            }
        });

        buttonPanel.add(addRowsSelection);
        buttonPanel.add(deleteRow);
        buttonPanel.add(testDeleteRow);
        buttonPanel.add(fireTableStructureChangedEvent);
        buttonPanel.add(fireTableDataChangedEvent);
        buttonPanel.add(fireTableRowUpdatedEvent);
        buttonPanel.add(fireTableCellUpdatedEvent);
        buttonPanel.add(saveFilterPreference);
        buttonPanel.add(loadFilterPreference);
        return buttonPanel;
    }

    public Component getDemoPanel() {
        JideTabbedPane tabbedPane = new JideTabbedPane();
        tabbedPane.setTabShape(JideTabbedPane.SHAPE_BOX);

        TableModel model = DemoData.createProductReportsTableModel(false, 0);

        _table = new SortableTable(model);
        _table.setRowResizable(true);
        _table.setVariousRowHeights(true);
        _table.setSelectInsertedRows(false);

        AutoFilterTableHeader _header = new AutoFilterTableHeader(_table);
        _header.setAutoFilterEnabled(true);
        _header.setUseNativeHeaderRenderer(true);
        _table.setTableHeader(_header);

        tabbedPane.addTab("FilterableTableModel", new JScrollPane(_table));
        tabbedPane.addTab("JTable", new JScrollPane(new JTable(model)));

        return tabbedPane;
    }

    @Override
    public String getDemoFolder() {
        return "G7.FilterableTable";
    }

    static public void main(String[] s) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
                showAsFrame(new FilterableTableDemo());
            }
        });

    }
}
