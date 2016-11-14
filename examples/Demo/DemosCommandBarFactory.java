/*
 * @(#)${NAME}.java
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.action.CommandBar;
import com.jidesoft.action.CommandBarFactory;
import com.jidesoft.action.CommandMenuBar;
import com.jidesoft.action.DockableBarContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockableHolder;
import com.jidesoft.swing.JideMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 */
class DemosCommandBarFactory extends CommandBarFactory {

    public static CommandBar createMenuCommandBar(Container container) {
        CommandBar commandBar = new CommandMenuBar(Resource.RB.getString("Demo.menuBar"));
        commandBar.setInitSide(DockableBarContext.DOCK_SIDE_NORTH);
        commandBar.setInitIndex(0);
        commandBar.setPaintBackground(false);
        commandBar.setStretch(true);
        commandBar.setFloatable(true);
        commandBar.setHidable(false);

        commandBar.add(createFileMenu(container));
        commandBar.add(createViewMenu(container));
        commandBar.add(createLookAndFeelMenu(container));

        return commandBar;
    }


    private static JMenu createFileMenu(final Container container) {
        JMenuItem item;

        JMenu fileMenu = new JideMenu(Resource.RB.getString("Demo.fileMenu"));
        fileMenu.setMnemonic(Resource.RB.getString("Demo.fileMenu.mnemonic").charAt(0));

        item = new JMenuItem(Resource.RB.getString("Demo.openMenu"), Resource.RB.getString("Demo.openMenu.mnemonic").charAt(0));
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -7000141946590406863L;

            public void actionPerformed(ActionEvent e) {
                if (container instanceof DockableHolder) {
                    DockableFrame dockableFrame = ((DockableHolder) container).getDockingManager().getFrame(JideDemos.DEMOS_DOCKABLE_FRAME_KEY);
                    if (dockableFrame != null && dockableFrame instanceof DemosDockableFrame) {
                        ((DemosDockableFrame) dockableFrame).openSelectedDemo();
                    }
                }
            }
        });
        fileMenu.add(item);

        fileMenu.addSeparator();

        item = new JMenuItem(Resource.RB.getString("Demo.exitMenu"), Resource.RB.getString("Demo.exitMenu.mnemonic").charAt(0));
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -1072905327487384539L;

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(item);
        return fileMenu;
    }

    private static JMenu createViewMenu(final Container container) {
        JMenuItem item;

        JMenu menu = new JideMenu(Resource.RB.getString("Demo.viewMenu"));
        menu.setMnemonic(Resource.RB.getString("Demo.viewMenu.mnemonic").charAt(0));

        item = new JMenuItem(Resource.RB.getString("Demo.allDemoMenu"), DemoIconsFactory.getImageIcon(DemoIconsFactory.Frame.DEMO));
        item.setMnemonic(Resource.RB.getString("Demo.allDemoMenu.mnemonic").charAt(0));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -2076783526255699125L;

            public void actionPerformed(ActionEvent e) {
                if (container instanceof DockableHolder) {
                    ((DockableHolder) container).getDockingManager().showFrame(JideDemos.DEMOS_DOCKABLE_FRAME_KEY);
                }
            }
        });
        menu.add(item);

        item = new JMenuItem(Resource.RB.getString("Demo.optionsMenu"), DemoIconsFactory.getImageIcon(DemoIconsFactory.Frame.OPTIONS));
        item.setMnemonic(Resource.RB.getString("Demo.optionsMenu.mnemonic").charAt(0));
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -6847572533298476676L;

            public void actionPerformed(ActionEvent e) {
                if (container instanceof DockableHolder) {
                    ((DockableHolder) container).getDockingManager().showFrame(JideDemos.OPTIONS_DOCKABLE_FRAME_KEY);
                }
            }
        });
        menu.add(item);

        return menu;
    }
}
