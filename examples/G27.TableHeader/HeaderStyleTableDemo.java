/*
 * @(#)HeaderStyleTableDemo.java 8/17/2011
 *
 * Copyright 2002 - 2011 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.DoubleConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.*;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.text.DecimalFormat;

public class HeaderStyleTableDemo extends AbstractDemo {
    protected SortableTable _sortableTable;
    private static final long serialVersionUID = -5373007282200581748L;

    public HeaderStyleTableDemo() {
        ObjectConverterManager.initDefaultConverter();
        DecimalFormat format = new DecimalFormat();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(10);
        DoubleConverter converter = new DoubleConverter(format);
        ObjectConverterManager.registerConverter(Double.class, converter);
    }

    public String getName() {
        return "CellStyleTableHeader Demo (HeaderStyleModel)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of CellStyleTableHeader. \n" +
                "\n Please adjust the HeaderStyleModel to adjust the alignment, background, foreground, font and icon of the table header.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.CellStyleTableHeader;" +
                "com.jidesoft.grid.HeaderStyleModel";
    }

    public Component getDemoPanel() {
        TableModel model = new SampleTableModel();

        _sortableTable = new SortableTable(model);
        _sortableTable.setAutoResizeMode(JideTable.AUTO_RESIZE_FILL);
        _sortableTable.setNestedTableHeader(true);
        _sortableTable.setFillsGrids(false);
        TableColumnGroup food = new TableColumnGroup("Food");
        TableColumnGroup beverage = new TableColumnGroup("Beverage");
        beverage.add(_sortableTable.getColumnModel().getColumn(1));
        beverage.add(_sortableTable.getColumnModel().getColumn(2));
        beverage.add(_sortableTable.getColumnModel().getColumn(3));
        beverage.add(_sortableTable.getColumnModel().getColumn(4));
        TableColumnGroup condiments = new TableColumnGroup("Condiments");
        condiments.add(_sortableTable.getColumnModel().getColumn(5));
        condiments.add(_sortableTable.getColumnModel().getColumn(6));
        condiments.add(_sortableTable.getColumnModel().getColumn(7));
        food.add(beverage);
        food.add(condiments);
        food.add(_sortableTable.getColumnModel().getColumn(8));

        if (_sortableTable.getTableHeader() instanceof NestedTableHeader) {
            NestedTableHeader header = (NestedTableHeader) _sortableTable.getTableHeader();
            header.addColumnGroup(food);
        }
        TableHeaderPopupMenuInstaller installer = new TableHeaderPopupMenuInstaller(_sortableTable);
        installer.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());

        TableUtils.autoResizeAllColumns(_sortableTable);

        return new JScrollPane(_sortableTable);
    }

    static class SampleTableModel extends AbstractTableModel implements ContextSensitiveTableModel, ToolTipSupport, HeaderStyleModel {
        private static final long serialVersionUID = 8798261997256893224L;
        private static CellStyle CENTER_STYLE = new CellStyle();
        private static CellStyle ICON_STYLE = new CellStyle();
        private static CellStyle BEVERAGE_STYLE = new CellStyle();
        private static CellStyle CONDIMENTS_STYLE = new CellStyle();
        private static CellStyle FOOD_STYLE = new CellStyle();

        static {
            CENTER_STYLE.setForeground(Color.gray);
            CENTER_STYLE.setHorizontalAlignment(SwingConstants.CENTER);
            ICON_STYLE.setHorizontalAlignment(SwingConstants.CENTER);
            ICON_STYLE.setIcon(JideIconsFactory.getImageIcon(JideIconsFactory.FileType.HTML));
            ICON_STYLE.setText("");
            BEVERAGE_STYLE.setForeground(new Color(0, 128, 0));
            BEVERAGE_STYLE.setHorizontalAlignment(SwingConstants.CENTER);
            CONDIMENTS_STYLE.setForeground(Color.blue);
            CONDIMENTS_STYLE.setHorizontalAlignment(SwingConstants.CENTER);
            FOOD_STYLE.setFontStyle(Font.BOLD);
            FOOD_STYLE.setHorizontalAlignment(SwingConstants.CENTER);
        }

        public int getColumnCount() {
            return 9;
        }

        public int getRowCount() {
            return 8;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public ConverterContext getConverterContextAt(int row, int column) {
            return null;
        }

        public EditorContext getEditorContextAt(int row, int column) {
            return null;
        }

        public Class<?> getCellClassAt(int row, int column) {
            return getColumnClass(column);
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Others";
                case 1:
                    return "Chai";
                case 2:
                    return "Chang";
                case 3:
                    return "Chartreuse verte";
                case 4:
                    return "Ipoh coffee";
                case 5:
                    return "Genen Shouyu";
                case 6:
                    return "Gula malacca";
                case 7:
                    return "Vegie-spread";
                case 8:
                    return "Food-others";
            }
            return "";
        }

        public Object getValueAt(int row, int column) {
            return null;
        }

        public String getToolTipText(int columnIndex) {
            return "Click to sort this " + getColumnName(columnIndex);
        }

        public CellStyle getHeaderStyleAt(int rowIndex, int columnIndex) {
            if (rowIndex == 0) {
                return FOOD_STYLE;
            }
            else if (columnIndex == 0) {
                return ICON_STYLE;
            }
            else if (columnIndex <= 4) {
                return BEVERAGE_STYLE;
            }
            else if (columnIndex <= 7) {
                return CONDIMENTS_STYLE;
            }
            return CENTER_STYLE;
        }

        public boolean isHeaderStyleOn() {
            return true;
        }
    }

    @Override
    public String getDemoFolder() {
        return "G27.TableHeader";
    }

    static public void main(String[] s) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
                showAsFrame(new HeaderStyleTableDemo());
            }
        });

    }

    public void dispose() {
    }
}