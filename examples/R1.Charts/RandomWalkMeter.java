/*
 * @(#)RandomWalkMeter.java 8/22/2009
 *
 * 2002 - 2013 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2013 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.gauge.*;
import com.jidesoft.utils.ColorUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class RandomWalkMeter extends JPanel {
    private Timer timer;
    private Dial dial;
    private Color tickColor = new Color(250, 250, 255, 150);
    private String title;

    public RandomWalkMeter(final String title, Color faceColor, Color needleColor) {
        setLayout(new GridBagLayout());
        this.title = title;
        GridBagConstraints c1 = new GridBagConstraints(1, 1, 1, 1, 1.0, 0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
        JLabel label = new JLabel(title, JLabel.CENTER);
        label.setVerticalAlignment(JLabel.BOTTOM);
        add(label, c1);

        GridBagConstraints c2 = new GridBagConstraints(1, 3, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
        dial = new Dial();
        dial.setFacePaint(faceColor);
        dial.setShadowVisible(true);
        dial.setAnimateOnChange(true);
        DialAxis dialAxis = new DialAxis(0, 100, 10, 0);
        dialAxis.setInnerRadius(0.85);
        dialAxis.setOuterRadius(0.95);

        DialTickStyle tickStyle = new DialTickStyle(1.0);
        tickStyle.setTickWidth(5);
        tickStyle.setTickColor(tickColor);
        dialAxis.setMajorTickStyle(tickStyle);
        DialFrame frame = new DialFrame();
        Color topColor = ColorUtils.getDerivedColor(faceColor, 0.6f);
        Color bottomColor = ColorUtils.getDerivedColor(faceColor, 0.2f);
        float frameWidth = 0.2f;
        float fillStart = 1.0f/(1+frameWidth);
        // The float values for the fill need to be scaled down as the maximum value is 1
        // and then we call setRadiusProportion so that rescaling occurs when painting
        // This is a special case because the extent of the DialFrame goes beyond 1 radius from the centre.
        DialRadialPaint fill = new DialRadialPaint(dial,
                new float[] {fillStart, (1+fillStart)/2, 1f},
                new Color[] {bottomColor, topColor, bottomColor});
        fill.setRadiusProportion(1 + frameWidth);
        Paint pivotPaint = new DialRadialPaint(dial,
                new float[] {0.0f, 0.1f},
                new Color[] {ColorUtils.getDerivedColor(needleColor, 0.6f), ColorUtils.getDerivedColor(needleColor, 0.4f)});
        Pivot pivot = new Pivot(dial, 0.1, pivotPaint);
        pivot.setOutlineColor(null);
        pivot.setOutlineWidth(1);
        pivot.setZOrder(101);

        Paint bottomPivotPaint = new DialRadialPaint(dial,
                new float[] {0.0f, 0.18f},
                new Color[] {Color.gray, faceColor});
        Pivot bottomPivot = new Pivot(dial, 0.18f, bottomPivotPaint);
        dial.addDrawable(pivot);
        dial.addDrawable(bottomPivot);
        NeedleStyle needleStyle = new NeedleStyle();
        needleStyle.setOutlineColor(null);
        needleStyle.setHeadLength(0.95);
        needleStyle.setHeadWidth(0.01);
        needleStyle.setBaseWidth(0.08);
        needleStyle.setTailShape(NeedleEndShape.ROUND);
        needleStyle.setTailWidth(0.1);
        needleStyle.setFillPaint(needleColor);
        dial.addNeedle(title, needleStyle);
        final DialLabel dialLabel = new DialLabel(dial, 0.5, 270, null);
        dialLabel.setColor(tickColor);
        dial.addDrawable(dialLabel);
        frame.setFill(fill);
        frame.setFrameWidth(frameWidth);
        dial.setFrame(frame);
        dial.setAxis(dialAxis);
        dialAxis.setTickLabelsVisible(false);
        add(dial, c2);


        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double r = 50 * Math.random() - 25;
                Double oldValue = dial.getValue(title);
                double newValue = oldValue == null ? r : oldValue + r;
                if (newValue < 0) {
                    newValue = 0;
                }
                else if (newValue > 100) {
                    newValue = 100;
                }
                dial.setValue(title, newValue);
                dialLabel.setLabel(String.format("%.0f%%", newValue));
                // Use null as the old value to make sure that an event is fired
                // (If the old and new values are the same no event is fired)
                firePropertyChange("value", null, newValue);
            }
        };
        timer = new Timer(1000, listener);
        timer.setRepeats(true);
        timer.start();
    }

    public double getValue() {
        Double value = dial.getValue(title);
        return value == null ? 0.0 : value;
    }
}
