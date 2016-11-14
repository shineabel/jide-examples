/*
 * @(#)CellUnderlayOverlayDemo.java 1/12/2012
 *
 * Copyright 2002 - 2012 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.OverlayableIconsFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link com.jidesoft.grid.JideTable} <br> Required jar files: jide-common.jar, jide-grids.jar <br>
 * Required L&F: any L&F
 */
public class CellUnderlayOverlayDemo extends AbstractDemo {
    private static final long serialVersionUID = 7343096906542360847L;
    public SortableTable _table;
    boolean[] _rowFont;

    public CellUnderlayOverlayDemo() {
    }

    public String getName() {
        return "CellStyleTable Demo (Cell Overlay/Underlay)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of CellStyle's setOverlayCellPainter and setUnderlayCellPainter feature which eventually uses " +
                "JideTable's paintCellOverlay and paintCellUnderlay methods.\n" +
                "\nYou can use these two painters to implement some customized painting code, " +
                "either under the cell content or above the cell content." +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.CellPainter\n" +
                "com.jidesoft.grid.CellStyle\n" +
                "com.jidesoft.grid.CellStyleTable";
    }

    private boolean _underlayEnabled = true;
    private boolean _overlayEnabled = true;

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        JCheckBox cellUnderlay = new JCheckBox("Show Cell Underlay");
        cellUnderlay.setMnemonic('U');
        cellUnderlay.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _underlayEnabled = e.getStateChange() == ItemEvent.SELECTED;
                _table.repaint();
            }
        });
        cellUnderlay.setSelected(_underlayEnabled);

        JCheckBox cellOverlay = new JCheckBox("Show Cell Overlay");
        cellOverlay.setMnemonic('O');
        cellOverlay.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _overlayEnabled = e.getStateChange() == ItemEvent.SELECTED;
                _table.repaint();
            }
        });
        cellOverlay.setSelected(_overlayEnabled);

        panel.add(cellUnderlay);
        panel.add(cellOverlay);
        return panel;
    }

    final static CellStyle CELL_STYLE_OVERLAY = new CellStyle();
    final static CellStyle CELL_STYLE_UNDERLAY = new CellStyle();

    static {
        CELL_STYLE_UNDERLAY.setUnderlayCellPainter(new CellPainter() {
            public void paint(Graphics g, Component component, int row, int column, Rectangle cellRect, Object value) {
                if (value instanceof Double) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    double dv = (Double) value;
                    Rectangle clip = new Rectangle(cellRect.x, cellRect.y, (int) (cellRect.width * dv / 100.0), cellRect.height);
                    g2d.clip(clip);
                    if (dv > 66.0) {
                        JideSwingUtilities.fillGradient(g2d, cellRect,
                                new Color(147, 98, 184), new Color(229, 193, 255), false);
                    }
                    else if (dv > 33.0) {
                        JideSwingUtilities.fillGradient(g2d, cellRect,
                                new Color(173, 135, 24), new Color(246, 218, 135), false);
                    }
                    else {
                        JideSwingUtilities.fillGradient(g2d, cellRect,
                                new Color(75, 126, 176), new Color(170, 208, 246), false);
                    }
                    g2d.dispose();
                }
            }
        });

        CELL_STYLE_OVERLAY.setOverlayCellPainter(new CellPainter() {
            public void paint(Graphics g, Component component, int row, int column, Rectangle cellRect, Object value) {
                if (row % 5 == 0) {
                    Icon icon = OverlayableIconsFactory.getImageIcon(OverlayableIconsFactory.ATTENTION);
                    icon.paintIcon(component, g, cellRect.x + cellRect.width - icon.getIconWidth() - 1, cellRect.y);
                }
                else if (row % 7 == 0) {
                    Icon icon = OverlayableIconsFactory.getImageIcon(OverlayableIconsFactory.ERROR);
                    icon.paintIcon(component, g, cellRect.x + cellRect.width - icon.getIconWidth() - 1, cellRect.y);
                }
            }
        });
    }

    class QuoteTableModel extends DefaultTableModel implements ContextSensitiveTableModel, StyleModel {
        private static final long serialVersionUID = 8709426264095661391L;

        public QuoteTableModel() {
            super(DemoData.QUOTES, DemoData.QUOTE_COLUMNS);
        }

        public ConverterContext getConverterContextAt(int rowIndex, int columnIndex) {
            return null;
        }

        public EditorContext getEditorContextAt(int rowIndex, int columnIndex) {
            return null;
        }

        public Class<?> getCellClassAt(int rowIndex, int columnIndex) {
            return getColumnClass(columnIndex);
        }

        public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
            if (columnIndex == 2) {
                return _underlayEnabled ? CELL_STYLE_UNDERLAY : null;
            }
            else if (columnIndex == 4) {
                return _overlayEnabled ? CELL_STYLE_OVERLAY : null;
            }
            return null;
        }

        public boolean isCellStyleOn() {
            return true;
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        _table = new SortableTable(new QuoteTableModel());
        _table.setAutoResizeMode(JideTable.AUTO_RESIZE_FILL);
        _table.setTableStyleProvider(new RowStripeTableStyleProvider());

        _table.setColumnAutoResizable(true);

        _table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        _table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        panel.add(new JScrollPane(_table), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G17.StyleTable";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
                showAsFrame(new CellUnderlayOverlayDemo());
            }
        });

    }
}

