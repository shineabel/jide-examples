/*
 * @(#)BulletStyleEditor.java
 *
 * 2002 - 2013 JIDE Software Incorporated. All rights reserved.
 * Copyright (c) 2005 - 2013 Catalysoft Limited. All rights reserved.
 */

import com.jidesoft.chart.Orientation;
import com.jidesoft.converter.EnumConverter;
import com.jidesoft.converter.ObjectConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.gauge.Bullet;
import com.jidesoft.gauge.BulletAxis;
import com.jidesoft.gauge.BulletAxisPlacement;
import com.jidesoft.gauge.BulletTickStyle;
import com.jidesoft.gauge.IndicatorStyle;
import com.jidesoft.grid.*;
import com.jidesoft.introspector.BeanIntrospector;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Paint;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.logging.Logger;

public class BulletStyleEditor extends JPanel {
    private Bullet bullet;
    private static final Logger logger = Logger.getLogger(BulletStyleEditor.class.getName());
    private String[] bulletProps = {
            "value", "The displayed value of the bullet", "Bullet",
            "background", "Background Colour", "Bullet",
            "bulletBackground", "Background colour of the bullet area (this is actually a Paint property rather than a Color).", "Bullet",
            "resizeFonts", "Whether to automatically resize fonts", "Bullet",
            "orientation", "The orientation of the bullet", "Bullet"
    };
    private String[] styleProps = {
            "fill", "The paint fill to use for the value renderer", "Bullet Style"
    };
    private String[] axisProps = {
            "tickLabelColor", "Tick Label Colour", "Axis",
            "tickLabelRotation", "Tick Label Rotation", "Axis",
            "visible", "Whether the axis is painted", "Axis",
            "placement", "Which side of the bullet the axis is placed", "Axis"
    };
    private String[] majorTickStyleProps = {"tickColor", "Tick Colour", "Major Tick Style",
                                            "visible", "Ticks Visible", "Major Tick Style",
                                            "tickLength", "The length of a tick given as a proportion of the bullet breadth", "Major Tick Style",
                                            "tickWidth", "The width of a tick in proportion to its length (min 1 pixel)", "Major Tick Style"};
    private String[] minorTickStyleProps = {"tickColor", "Tick Colour", "Minor Tick Style",
                                            "visible", "Ticks Visible", "Minor Tick Style",
                                            "tickLength", "The length of a tick given as a proportion of the bullet breadth", "Minor Tick Style",
                                            "tickWidth", "The width of a tick in proportion to its length (min 1 pixel)", "Minor Tick Style"};

    private EditorContext sliderContext = new EditorContext("Slider");


    public BulletStyleEditor(final Bullet bullet,
                             final BulletAxis axis,
                             IndicatorStyle indicatorStyle,
                             BulletTickStyle majorTickStyle,
                             BulletTickStyle minorTickStyle) {
        super(new BorderLayout());
        ObjectConverter orientationConverter = createConverter(Orientation.class, Orientation.values());
        ObjectConverter axisPlacementConverter = createConverter(BulletAxisPlacement.class, BulletAxisPlacement.values());
        CellRendererManager.registerRenderer(double.class, new DegreesRenderer(), sliderContext);
        CellEditorManager.registerEditor(double.class, new CellEditorFactory() {
            @Override
            public CellEditor create() {
                final SliderCellEditor editor = new DegreesSliderEditor();
                final JSlider slider = editor.getSlider();
                slider.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        int value = slider.getValue();
                        double radians = Math.toRadians(value);
                        axis.setTickLabelRotation(radians);
                        bullet.repaint();
                    }
                });
                return editor;
            }
        }, sliderContext);
        this.bullet = bullet;
        try {
            // Use a bit of configuration so that java.awt.Paint properties can be viewed and edited in the
            // property pane as if they were plain colors
            CellEditorManager.registerEditor(Paint.class, new CellEditorFactory() {
                @Override
                public CellEditor create() {
                    return new ColorCellEditor();
                }
            });
            CellRendererManager.registerRenderer(Paint.class, new PaintRenderer());
            List<Property> bulletProperties = createProperties(Bullet.class, bullet, bulletProps);
            List<Property> styleProperties = createProperties(IndicatorStyle.class, indicatorStyle, styleProps);
            List<Property> axisProperties = createProperties(BulletAxis.class, axis, axisProps);
            List<Property> majorTickProperties = createProperties(BulletTickStyle.class, majorTickStyle, majorTickStyleProps);
            List<Property> minorTickProperties = createProperties(BulletTickStyle.class, minorTickStyle, minorTickStyleProps);
            List<Property> props = new ArrayList<Property>();
            props.addAll(bulletProperties);
            props.addAll(axisProperties);
            props.addAll(styleProperties);
            props.addAll(majorTickProperties);
            props.addAll(minorTickProperties);
            PropertyTableModel tableModel = new PropertyTableModel(props);
            PropertyTable table = new PropertyTable(tableModel);
            PropertyPane propertyPane = new PropertyPane(table);
            add(propertyPane, BorderLayout.CENTER);
            table.expandAll();
            // Need to force a repaint for tick style changes
            tableModel.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    bullet.repaint();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a converter for the specified enum values and also registers it for the conversion
     * @param enumType the class of the enum
     * @param values the values that you want to convert and offer in the list box
     * @return an object converter to use for the class
     */
    private <T extends Enum<T>> ObjectConverter createConverter(final Class<T> enumType, final T[] values) {
        final EnumConverter converter = new EnumConverter(enumType);
        ObjectConverterManager.registerConverter(enumType, converter);
        ContextSensitiveCellRenderer renderer = new ContextSensitiveCellRenderer(enumType);
        CellRendererManager.registerRenderer(enumType, renderer);
        CellEditorManager.registerEditor(enumType, new CellEditorFactory() {
            @Override
            public CellEditor create() {
                ListComboBoxCellEditor editor = new ListComboBoxCellEditor(values, enumType);
                editor.setConverter(converter);
                return editor;
            }
        });
        return converter;
    }

    class DegreesRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Double radians = (Double) value;
            double degrees = Math.toDegrees(radians);
            int deg = (int) Math.round(degrees);
            String str = String.format("%.1f radians (%d\u00B0)", radians, deg);
            label.setText(str);
            return label;
        }
    }

    static class DegreesSliderEditor extends SliderCellEditor {
        DegreesSliderEditor() {
            super(-90, 90);
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        @Override
        public void setCellEditorValue(Object o) {
            Double radians = (Double) o;
            double degrees = Math.toDegrees(radians);
            int deg = (int) Math.round(degrees);
            super.setCellEditorValue(Integer.valueOf(deg));
        }

        @Override
        public Object getCellEditorValue() {
            Object obj = super.getCellEditorValue();
            Integer i = (Integer) obj;
            double radians = Math.toRadians(i);
            return Double.valueOf(radians);
        }
    }

    /**
     * Create a List of Property objects. We create lists of property objects in this way so that we can
     * combine the lists of properties that originate from different objects into the same Property Pane.
     */
    private <T> List<Property> createProperties(Class<T> cls, T instance, String[] propDescriptions) {
        List<Property> properties = new ArrayList<Property>();
        try {
            BeanIntrospector beanIntrospector = new BeanIntrospector(cls, propDescriptions);
            properties = beanIntrospector.createPropertyList(instance);
            if (cls.equals(BulletAxis.class)) {
                beanIntrospector.getProperty("tickLabelRotation").setEditorContext(sliderContext);
            }
        } catch (IntrospectionException ie) {
            ie.printStackTrace();
        }
        return properties;
    }

    class PaintRenderer implements TableCellRenderer {
        private ColorCellRenderer delegate = new ColorCellRenderer();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Color color = (Color) value;
            return delegate.getTableCellRendererComponent(table, color, isSelected, hasFocus, row, column);
        }
    }

}
