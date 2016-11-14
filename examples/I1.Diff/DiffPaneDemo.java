/*
 * @(#)DiffPaneDemo.java 1/14/2010
 *
 * Copyright 2002 - 2010 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.ColorExComboBox;
import com.jidesoft.diff.CodeEditorDiffPane;
import com.jidesoft.editor.CodeEditor;
import com.jidesoft.editor.tokenmarker.JavaTokenMarker;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;

public class DiffPaneDemo extends AbstractDemo {
    private static final long serialVersionUID = -6466141167786979238L;
    public CodeEditorDiffPane _diffPane;
    protected String _lastDirectory = ".";

    public DiffPaneDemo() {
    }

    public String getName() {
        return "DiffPane Demo (CodeEditor)";
    }

    public String getProduct() {
        return PRODUCT_NAME_DIFF;
    }

    @Override
    public String getDescription() {
        return "This is a demo for CodeEditorDiffPane to compare the difference between two text files." +
                "Demoed classes:\n" +
                "CodeEditorDiffPane";
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_UPDATED;
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(new JButton(new AbstractAction("Left Pane") {
            private static final long serialVersionUID = -1501679694752319112L;

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser() {
                    @Override
                    protected JDialog createDialog(Component parent) throws HeadlessException {
                        JDialog dialog = super.createDialog(parent);
                        dialog.setTitle("Load a file to compare");
                        return dialog;
                    }
                };
                chooser.setCurrentDirectory(new File(_lastDirectory));
                int result = chooser.showDialog(((JButton) e.getSource()).getTopLevelAncestor(), "Open");
                if (result == JFileChooser.APPROVE_OPTION) {
                    _lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
                    ((CodeEditor) _diffPane.getFromComponent()).setFileName(chooser.getSelectedFile().getAbsolutePath());
                }

            }
        }));
        buttonPanel.add(new JButton(new AbstractAction("Right Pane") {
            private static final long serialVersionUID = -738598516365361196L;

            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser() {
                    @Override
                    protected JDialog createDialog(Component parent) throws HeadlessException {
                        JDialog dialog = super.createDialog(parent);
                        dialog.setTitle("Load a file to compare");
                        return dialog;
                    }
                };
                chooser.setCurrentDirectory(new File(_lastDirectory));
                int result = chooser.showDialog(((JButton) e.getSource()).getTopLevelAncestor(), "Open");
                if (result == JFileChooser.APPROVE_OPTION) {
                    _lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
                    ((CodeEditor) _diffPane.getToComponent()).setFileName(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        }));
        panel.add(JideSwingUtilities.createLabeledComponent(new JLabel("Choose a File for: "), buttonPanel, BorderLayout.BEFORE_FIRST_LINE));
        panel.add(Box.createVerticalStrut(6));

        ColorExComboBox changedColorExComboBox = new ColorExComboBox();
        changedColorExComboBox.setSelectedColor(UIDefaultsLookup.getColor("DiffMerge.changed"));
        changedColorExComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _diffPane.setChangedColor((Color) e.getItem());
                }
            }
        });
        panel.add(JideSwingUtilities.createLabeledComponent(new JLabel("Changed: "), changedColorExComboBox, BorderLayout.BEFORE_FIRST_LINE));
        panel.add(Box.createVerticalStrut(2));

        ColorExComboBox insertedColorExComboBox = new ColorExComboBox();
        insertedColorExComboBox.setSelectedColor(UIDefaultsLookup.getColor("DiffMerge.inserted"));
        insertedColorExComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _diffPane.setInsertedColor((Color) e.getItem());
                }
            }
        });
        panel.add(JideSwingUtilities.createLabeledComponent(new JLabel("Inserted: "), insertedColorExComboBox, BorderLayout.BEFORE_FIRST_LINE));
        panel.add(Box.createVerticalStrut(2));

        ColorExComboBox deletedColorExComboBox = new ColorExComboBox();
        deletedColorExComboBox.setSelectedColor(UIDefaultsLookup.getColor("DiffMerge.deleted"));
        deletedColorExComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _diffPane.setDeletedColor((Color) e.getItem());
                }
            }
        });
        panel.add(JideSwingUtilities.createLabeledComponent(new JLabel("Deleted"), deletedColorExComboBox, BorderLayout.BEFORE_FIRST_LINE));

        panel.add(Box.createVerticalStrut(6));
        JCheckBox highlightExactChange = new JCheckBox("Highlight Exact Change");
        highlightExactChange.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _diffPane.setHighlightExactChange(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        highlightExactChange.setSelected(_diffPane.isHighlightExactChange());
        panel.add(JideSwingUtilities.createCenterPanel(highlightExactChange));
        panel.add(JideSwingUtilities.createCenterPanel(new JButton(new AbstractAction("Clear Diff Results") {
            private static final long serialVersionUID = 50201280516163621L;

            public void actionPerformed(ActionEvent e) {
                _diffPane.clearDiff();
            }
        })));

        return panel;
    }

    public Component getDemoPanel() {
        try {
            StringBuffer buf1 = readInputStream(DiffPaneDemo.class.getClassLoader().getResourceAsStream("Diff1.txt"));
            StringBuffer buf2 = readInputStream(DiffPaneDemo.class.getClassLoader().getResourceAsStream("Diff2.txt"));
            _diffPane = new CodeEditorDiffPane() {
                @Override
                protected void customizeEditor(CodeEditor editor, int index) {
                    super.customizeEditor(editor, index);
                    editor.setTokenMarker(new JavaTokenMarker());
                }
            };
            _diffPane.setPreferredSize(new Dimension(600, 500));

            _diffPane.setFromTitle("Local");
            _diffPane.setToTitle("Remote");
            _diffPane.setFromText(buf1.toString());
            _diffPane.setToText(buf2.toString());

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(_diffPane);

            panel.add(new JButton(new AbstractAction("Compare") {
                private static final long serialVersionUID = -4133857847867200358L;

                public void actionPerformed(ActionEvent e) {
                    _diffPane.diff();
                }
            }), BorderLayout.AFTER_LAST_LINE);
            return panel;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static StringBuffer readInputStream(InputStream in) throws IOException {
        Reader reader = new InputStreamReader(in);
        char[] buf = new char[1024];
        StringBuffer buffer = new StringBuffer();
        int read;
        while ((read = reader.read(buf)) != -1) {
            buffer.append(buf, 0, read);
        }
        reader.close();
        return buffer;
    }

    @Override
    public String getDemoFolder() {
        return "I1.DiffTextPane";
    }

    static public void main(String[] s) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
                AbstractDemo.showAsFrame(new DiffPaneDemo());
            }
        });
    }
}