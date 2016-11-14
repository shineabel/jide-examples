/*
 * @(#)TreePaletteDashboardDemo.java 7/22/2014
 *
 * Copyright 2002 - 2014 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.dashboard.*;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.pane.CollapsiblePaneTitleButton;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideSplitPane;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Calendar;

public class TreePaletteDashboardDemo extends AbstractDemo {
    public static String _lastDirectory = ".";
    protected Dashboard _dashboard;
    protected JTree _tree;
    private static final long serialVersionUID = 502408740940055925L;
    private GadgetManager _gadgetManager;

    public TreePaletteDashboardDemo() {
    }

    public String getName() {
        return "Dashboard Demo (Custom Palette)";
    }

    public String getProduct() {
        return PRODUCT_NAME_DASHBOARD;
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_NEW;
    }

    @Override
    public String getDescription() {
        return "This is a demo of Dashboard using any components as the palette for a dashboard. \n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.dasbhoard.GadgetPaletteSupport\n" +
                "com.jidesoft.dasbhoard.GadgetManager\n" +
                "com.jidesoft.dasbhoard.Gadget\n" +
                "com.jidesoft.dasbhoard.GadgetComponent\n" +
                "com.jidesoft.dasbhoard.Dashboard\n";
    }


    // the component itself or one of its ancestors must implement GadgetPaletteSupport.
    private class PaletteTree extends JTree implements GadgetPaletteSupport {
        public PaletteTree(TreeNode root) {
            super(root);
            setCellRenderer(new DefaultTreeCellRenderer() {
                @Override
                public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                    Component rendererComponent = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                    if (rendererComponent instanceof JLabel) {
                        Gadget gadget = _gadgetManager.getGadget("" + value);
                        if (gadget != null) {
                            ((JLabel) rendererComponent).setIcon(gadget.getIcon());
                        }
                        else {
                            ((JLabel) rendererComponent).setIcon(JideIconsFactory.getImageIcon(JideIconsFactory.JIDE32));
                        }
                    }
                    return rendererComponent;
                }
            });
            setRowHeight(32);
            setFont(getFont().deriveFont(16f));
        }

        @Override
        public Gadget getGadget() {
            TreePath selectionPath = getSelectionPath();
            if (selectionPath != null) {
                return _gadgetManager.getGadget("" + ((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).getUserObject());
            }
            else {
                return null;
            }
        }
    }

    public Component getDemoPanel() {
        _gadgetManager = new GadgetManager();
        _gadgetManager.addGadget(createGadget("Calculator"));
        _gadgetManager.addGadget(createGadget("Call"));
        _gadgetManager.addGadget(createGadget("Clock"));
        _gadgetManager.addGadget(createGadget("Find"));
        _gadgetManager.addGadget(createGadget("Image"));
        _gadgetManager.addGadget(createGadget("Network"));
        _gadgetManager.addGadget(createGadget("News"));
        _gadgetManager.addGadget(createGadget("Notes"));
        _gadgetManager.addGadget(createGadget("Chart"));

        _dashboard = new Dashboard();
        _dashboard.setPreferredSize(new Dimension(600, 500));

        // add all the gadgets as tree nodes
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Palette");
        for (String s : _gadgetManager.getGadgets()) {
            root.add(new DefaultMutableTreeNode(s));
        }
        JTree paletteTree = new PaletteTree(root);
        paletteTree.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        // required to trigger the drag-n-drop action
        _gadgetManager.installListeners(paletteTree);

        JideSplitPane pane = new JideSplitPane();
        pane.setShowGripper(true);
        pane.add(new JScrollPane(paletteTree), JideBoxLayout.FLEXIBLE);
        pane.add(new JScrollPane(_dashboard));
        return pane;

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
                showAsFrame(new TreePaletteDashboardDemo());
            }
        });
    }

    protected AbstractGadget createGadget(String key) {
        AbstractGadget dashboardElement = new AbstractGadget(key,
                IconsFactory.getImageIcon(CollapsiblePaneSingleDashboardDemo.class, "icons/" + key.toLowerCase() + "_32x32.png"),
                IconsFactory.getImageIcon(CollapsiblePaneSingleDashboardDemo.class, "icons/" + key.toLowerCase() + "_64x64.png")) {
            public GadgetComponent createGadgetComponent() {
                final CollapsiblePaneGadgetComponent gadget = new CollapsiblePaneGadgetComponent(this);
                gadget.getContentPane().setLayout(new BorderLayout());
                gadget.getContentPane().setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
                if (getKey().startsWith("Calculator")) {
                    gadget.getContentPane().add(GadgetFactory.createCalculator());
                }
                else if (getKey().startsWith("Call")) {
                    gadget.getContentPane().add(GadgetFactory.createCalendar());
                }
                else if (getKey().startsWith("Notes")) {
                    gadget.getContentPane().add(GadgetFactory.createNotes());
                }
                else if (getKey().startsWith("Find")) {
                    gadget.getContentPane().add(GadgetFactory.createFind());
                }
                else if (getKey().startsWith("News")) {
                    gadget.getContentPane().add(GadgetFactory.createNews());
                }
                else if (getKey().startsWith("Chart")) {
                    gadget.getContentPane().add(GadgetFactory.createChart());
                }
                else if (getKey().startsWith("Clock")) {
                    gadget.getContentPane().add(GadgetFactory.createClock());
                }
                else {
                    gadget.getContentPane().setPreferredSize(new Dimension(200, 100 + (int) (Math.random() * 100)));
                }
                CollapsiblePaneTitleButton toolButton = new CollapsiblePaneTitleButton(gadget, IconsFactory.getImageIcon(CollapsiblePaneSingleDashboardDemo.class, "icons/gadget_tool.png"));
                toolButton.addActionListener(new AbstractAction() {
                    private static final long serialVersionUID = -332487105417025587L;

                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(gadget, "Settings dialog goes here!");
                    }
                });
                gadget.addButton(toolButton, 1);
                gadget.setMessage("Last update on " + ObjectConverterManager.toString(Calendar.getInstance()));
                return gadget;
            }

            public void disposeGadgetComponent(GadgetComponent component) {
                // do nothing in this case as we didn't allocate any resource in createGadgetComponent.
            }
        };
        dashboardElement.setDescription("Description is " + key);
        return dashboardElement;
    }
}
