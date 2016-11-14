/*
 * @(#)UndoableTableDemo.java 1/24/2012
 *
 * Copyright 2002 - 2012 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * Demoed Component: {@link com.jidesoft.grid.DefaultUndoableTableModel} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: any L&F
 */
public class UndoableTableDemo extends AbstractDemo {
    private SortableTable _sortableTable;
    private AbstractUndoableTableModel _abstractModel;
    private DefaultUndoableTableModel _defaultModel;
    private static final long serialVersionUID = -8262453726101747955L;

    public UndoableTableDemo() {
    }

    public String getName() {
        return "Undoable Table Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo to demonstrate the undo feature of JideTable. You could use CTRL+C and CTR+V to copy and paste inside the table.\n" +
                "You could drag rows from the table to an excel file or from an excel file to the table\n" +
                "You could also press CTRL+Z to undo the edit and CTRL+SHIFT+Z to redo the edit\n" +
                "\n" +
                "You have to have JDK6 above to make this demo run\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.DefaultUndoableTableModel\n" +
                "com.jidesoft.grid.AbstractUndoableTableModel\n" +
                "com.jidesoft.grid.UndoableTableModelEvent\n" +
                "com.jidesoft.grid.JideTableTransferHandler";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 2));
        JCheckBox acceptImport = new JCheckBox("Allow Paste");
        acceptImport.setSelected(true);
        acceptImport.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                ((JideTableTransferHandler) _sortableTable.getTransferHandler()).setAcceptImport(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        panel.add(acceptImport);

        JCheckBox dragEnabled = new JCheckBox("Drag Enabled");
        dragEnabled.setSelected(true);
        dragEnabled.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _sortableTable.setDragEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        panel.add(dragEnabled);

        JCheckBox modelSelected = new JCheckBox("Use AbstractUndoTableModel");
        modelSelected.setSelected(true);
        modelSelected.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _sortableTable.setModel(e.getStateChange() == ItemEvent.SELECTED ? _abstractModel : _defaultModel);
            }
        });
        panel.add(modelSelected);

        JCheckBox nonContiguous = new JCheckBox("Non-contiguous Cell Selection");
        nonContiguous.setSelected(_sortableTable.isNonContiguousCellSelection());
        nonContiguous.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _sortableTable.setNonContiguousCellSelection(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        panel.add(nonContiguous);
        panel.add(Box.createVerticalStrut(4), JideBoxLayout.FIX);

        final String[] dropMode = new String[]{
                "ON",
                "INSERT",
                "INSERT_ROWS",
                "ON_OR_INSERT",
                "ON_OR_INSERT_ROWS",
        };
        final JComboBox dropModeComboBox = new JComboBox(dropMode);
        dropModeComboBox.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -397673522775424461L;

            public void actionPerformed(ActionEvent e) {
                if (dropMode[0].equals(dropModeComboBox.getSelectedItem())) {
                    _sortableTable.setDropMode(DropMode.ON);
                }
                else if (dropMode[1].equals(dropModeComboBox.getSelectedItem())) {
                    _sortableTable.setDropMode(DropMode.INSERT);
                }
                else if (dropMode[2].equals(dropModeComboBox.getSelectedItem())) {
                    _sortableTable.setDropMode(DropMode.INSERT_ROWS);
                }
                else if (dropMode[3].equals(dropModeComboBox.getSelectedItem())) {
                    _sortableTable.setDropMode(DropMode.ON_OR_INSERT);
                }
                else if (dropMode[4].equals(dropModeComboBox.getSelectedItem())) {
                    _sortableTable.setDropMode(DropMode.ON_OR_INSERT_ROWS);
                }
            }
        });
        dropModeComboBox.setSelectedIndex(3);
        panel.add(new JLabel("Drop Mode: "));
        panel.add(dropModeComboBox);
        panel.add(Box.createVerticalStrut(4), JideBoxLayout.FIX);

        return panel;
    }

    private class QuoteUndoableTableModel extends AbstractUndoableTableModel {
        List _rows;
        private static final long serialVersionUID = -1833788400649626721L;

        public QuoteUndoableTableModel() {
            super();
            _rows = new ArrayList<Object>();
            _rows.addAll(Arrays.asList(DemoData.QUOTES));
        }

        public boolean insertRowImpl(int rowIndex, Vector<Object> rowData) {
            if (rowData == null || rowData.size() != getColumnCount() || rowIndex < 0 || rowIndex > getRowCount()) {
                return false;
            }
            Object[] objects = rowData.toArray();
            _rows.add(rowIndex, objects);
            return true;
        }

        public boolean removeRowImpl(int rowIndex) {
            if (rowIndex < 0 || rowIndex >= getRowCount()) {
                return false;
            }
            _rows.remove(rowIndex);
            return true;
        }

        public boolean updateCellImpl(Object value, int rowIndex, int columnIndex) {
            if (rowIndex < 0 || rowIndex >= getRowCount() || columnIndex < 0 || columnIndex >= getColumnCount()) {
                return false;
            }
            Object[] rowData = (Object[]) _rows.get(rowIndex);
            rowData[columnIndex] = value;
            return true;
        }

        public int getRowCount() {
            return _rows.size();
        }

        public int getColumnCount() {
            return DemoData.QUOTE_COLUMNS.length;
        }

        @Override
        public String getColumnName(int column) {
            return DemoData.QUOTE_COLUMNS[column];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (rowIndex >= _rows.size() || rowIndex < 0 || columnIndex >= DemoData.QUOTE_COLUMNS.length || columnIndex < 0) {
                return null;
            }
            return ((Object[]) _rows.get(rowIndex))[columnIndex];
        }
    }

    private AbstractUndoableTableModel createAbstractUndoableModel() {
        return new QuoteUndoableTableModel();
    }

    public Component getDemoPanel() {
        _defaultModel = (DefaultUndoableTableModel) DemoData.createQuoteTableModel(true);
        _abstractModel = createAbstractUndoableModel();
        final JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Undoable Table Demo", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        _sortableTable = new SortableTable(_abstractModel);
        _sortableTable.setPreferredScrollableViewportSize(new Dimension(600, 500));
        InputMap map = _sortableTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        map.put(KeyStroke.getKeyStroke("control Z"), "undo");
        map.put(KeyStroke.getKeyStroke("control shift Z"), "redo");
//        _sortableTable.setNonContiguousCellSelection(true);
        ((JideTableTransferHandler) _sortableTable.getTransferHandler()).setAcceptImport(true);
        _sortableTable.setClickCountToStart(2);
        _sortableTable.setDragEnabled(true);
        _sortableTable.setDropMode(DropMode.INSERT_ROWS);

        TableHeaderPopupMenuInstaller installer = new TableHeaderPopupMenuInstaller(_sortableTable);
        installer.addTableHeaderPopupMenuCustomizer(new AutoResizePopupMenuCustomizer());
        installer.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());
        panel.add(new JScrollPane(_sortableTable));
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G12.JideTable";
    }

    static public void main(String[] s) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
                showAsFrame(new UndoableTableDemo());
            }
        });
    }
}