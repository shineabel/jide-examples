/*
 * @(#)UndoableListDemo.java 2/3/2012
 *
 * Copyright 2002 - 2012 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.list.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Demoed Component: {@link com.jidesoft.list.UndoableListInstaller} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: any L&F
 */
public class UndoableListDemo extends AbstractDemo {

    private static final long serialVersionUID = 7471245502223857731L;
    private JList _list;
    private DefaultUndoableListModel _defaultModel;
    private AbstractUndoableListModel _abstractModel;
    private SortableListModel _sortableListModel;
    private JCheckBox _checkBox;

    public UndoableListDemo() {
    }

    public String getName() {
        return "UndoableList Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo to demonstrate the undo feature of JList. You could use CTRL+C and CTR+V to copy and paste inside the list.\n" +
                "You could drag rows from the list to an excel file or from an excel file to the list\n" +
                "You could also press CTRL+Z to undo the edit and CTRL+SHIFT+Z to redo the edit\n" +
                "\n" +
                "You have to have JDK6 above to make this demo run\n" +
                "Demoed classes:\n" +
                "com.jidesoft.list.UndoableListInstaller\n" +
                "com.jidesoft.list.DefaultUndoableListModel\n" +
                "com.jidesoft.list.AbstractUndoableListModel\n" +
                "com.jidesoft.list.UndoableListDataEvent\n" +
                "com.jidesoft.list.ListTransferHandler";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 2));
        JCheckBox acceptImport = new JCheckBox("Allow Paste");
        acceptImport.setSelected(true);
        acceptImport.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                ((ListTransferHandler) _list.getTransferHandler()).setAcceptImport(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        panel.add(acceptImport);

        JCheckBox dragEnabled = new JCheckBox("Drag Enabled");
        dragEnabled.setSelected(true);
        dragEnabled.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _list.setDragEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        panel.add(dragEnabled);

        JCheckBox modelSelected = new JCheckBox("Use AbstractUndoListModel");
        modelSelected.setSelected(false);
        modelSelected.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _sortableListModel = new SortableListModel(e.getStateChange() == ItemEvent.SELECTED ? _abstractModel : _defaultModel);
                int[] selected = ListUtils.saveSelection(_list);
                if (_checkBox.isSelected()) {
                    _sortableListModel.sort(SortableListModel.SORT_ASCENDING);
                }
                else {
                    _sortableListModel.sort(SortableListModel.UNSORTED);
                }
                ListUtils.loadSelection(_list, selected);
                _list.setModel(_sortableListModel);
            }
        });
        panel.add(modelSelected);
        panel.add(Box.createVerticalStrut(4), JideBoxLayout.FIX);

        final String[] dropMode = new String[]{
                "ON",
                "INSERT",
                "ON_OR_INSERT",
        };
        final JComboBox dropModeComboBox = new JComboBox(dropMode);
        dropModeComboBox.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -397673522775424461L;

            public void actionPerformed(ActionEvent e) {
                if (dropMode[0].equals(dropModeComboBox.getSelectedItem())) {
                    _list.setDropMode(DropMode.ON);
                }
                else if (dropMode[1].equals(dropModeComboBox.getSelectedItem())) {
                    _list.setDropMode(DropMode.INSERT);
                }
                else if (dropMode[2].equals(dropModeComboBox.getSelectedItem())) {
                    _list.setDropMode(DropMode.ON_OR_INSERT);
                }
            }
        });
        dropModeComboBox.setSelectedIndex(2);
        panel.add(new JLabel("Drop Mode: "));
        panel.add(dropModeComboBox);
        panel.add(Box.createVerticalStrut(4), JideBoxLayout.FIX);

        return panel;
    }

    public Component getDemoPanel() {
        _defaultModel = new DefaultUndoableListModel();
        String[] fontNames = DemoData.getFontNames();
        for (String fontName : fontNames) {
            if (Math.random() > 0.5) {
                _defaultModel.add(0, fontName);
            }
            else {
                _defaultModel.addElement(fontName);
            }
            if (_defaultModel.getSize() >= 20) {
                break;
            }
        }
        _abstractModel = new FontNameUndoableListModel();
        _sortableListModel = new SortableListModel(_defaultModel);
        _sortableListModel.setSortOrder(SortableListModel.UNSORTED);

        final JList list = new JList(_sortableListModel);
        new UndoableListInstaller(list);
        _list = list;
        list.setDragEnabled(true);
        list.setVisibleRowCount(20);
        InputMap map = list.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        map.put(KeyStroke.getKeyStroke("control Z"), "undo");
        map.put(KeyStroke.getKeyStroke("control shift Z"), "redo");
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.add(new JScrollPane(list));
        _checkBox = new JCheckBox("Sort");
        _checkBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int[] selected = ListUtils.saveSelection(list);
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _sortableListModel.sort(SortableListModel.SORT_ASCENDING);
                }
                else {
                    _sortableListModel.sort(SortableListModel.UNSORTED);
                }
                ListUtils.loadSelection(list, selected);
            }
        });
        panel.add(_checkBox, BorderLayout.BEFORE_FIRST_LINE);

        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G19.SortableList";
    }

    static public void main(String[] s) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
                showAsFrame(new UndoableListDemo());
            }
        });
    }

    private class FontNameUndoableListModel extends AbstractUndoableListModel {
        List<Object> _elements;
        private static final long serialVersionUID = -1833788400649626721L;

        public FontNameUndoableListModel() {
            super();
            _elements = new ArrayList<Object>();
            _elements.addAll(Arrays.asList(DemoData.getFontNames()));
        }

        @Override
        public boolean setElementAtImpl(Object content, int index) {
            if (index < 0 || index >= getSize()) {
                return false;
            }
            _elements.remove(index);
            _elements.add(index, content);
            return true;
        }

        @Override
        public boolean removeElementAtImpl(int index) {
            if (index < 0 || index >= getSize()) {
                return false;
            }
            _elements.remove(index);
            return true;
        }

        @Override
        public boolean insertElementAtImpl(Object content, int index) {
            if (_elements == null || index < 0 || index > getSize()) {
                return false;
            }
            _elements.add(index, content);
            return true;
        }

        public int getSize() {
            return _elements == null ? 0 : _elements.size();
        }

        public Object getElementAt(int index) {
            return _elements == null || index < 0 || index > getSize() ? null : _elements.get(index);
        }
    }
}
