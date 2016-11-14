/*
 * @(#)BulletChartDemo.java
 *
 * 2002 - 2013 JIDE Software Incorporated. All rights reserved.
 * Copyright (c) 2005 - 2013 Catalysoft Limited. All rights reserved.
 */
import com.jidesoft.gauge.*;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

public class BulletChartDemo extends AbstractDemo {
    private BulletStyleEditor editor;
    private Bullet bullet;

    @Override
    public Component getDemoPanel() {
        bullet = new Bullet();
        bullet.setPreferredSize(new Dimension(600, 300));
        IndicatorStyle style = new IndicatorStyle();
        style.setFill(Color.DARK_GRAY);
        bullet.setBulletBackground(Color.orange);
        bullet.setBorder(new EmptyBorder(10,15,10,20));
        BulletAxis axis = new BulletAxis();
        //axis.setTickLabelRotation(Math.PI/4);
        axis.setPlacement(BulletAxisPlacement.LEADING);
        BulletTickStyle majorTickStyle = new BulletTickStyle();
        BulletTickStyle minorTickStyle = new BulletTickStyle(0.1);
        axis.setMajorTickStyle(majorTickStyle);
        axis.setMinorTickStyle(minorTickStyle);
        bullet.setAxis(axis);
        editor = new BulletStyleEditor(bullet, axis, style, majorTickStyle, minorTickStyle);

        bullet.setBulletBorder(new LineBorder(Color.GRAY, 1));
        bullet.setIndicatorStyle(style);
        bullet.setValue(80);
        BarIndicatorRenderer renderer = new BarIndicatorRenderer();
        renderer.setBarBreadth(0.6);
        renderer.setBorder(new LineBorder(Color.BLACK, 1));
        bullet.setRenderer(renderer);

        bullet.addDrawable(new BulletIntervalMarker(bullet, new Color(255, 0, 0, 150), 0, 25));
        bullet.addDrawable(new BulletIntervalMarker(bullet, new Color(255, 200, 0, 150), 25, 75));
        bullet.addDrawable(new BulletIntervalMarker(bullet, new Color(0, 255, 0, 150), 75, 100));
        return bullet;
    }

    @Override
    public Component getOptionsPanel() {
        return editor;
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_NEW | ATTRIBUTE_BETA;
    }

    @Override
    public String getName() {
        return "Bullet Chart Demo";
    }

    @Override
    public String getProduct() {
        return PRODUCT_NAME_CHARTS;
    }

    public static void main(String[] args) throws Exception {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new BulletChartDemo());
    }
}