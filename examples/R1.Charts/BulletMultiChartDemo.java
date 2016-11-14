/*
 * @(#)BulletMultiChartDemo.java
 *
 * 2002 - 2013 JIDE Software Incorporated. All rights reserved.
 * Copyright (c) 2005 - 2013 Catalysoft Limited. All rights reserved.
 */

import com.jidesoft.chart.Orientation;
import com.jidesoft.gauge.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.awt.geom.Insets2D;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.*;

import static com.jidesoft.chart.Orientation.*;


@SuppressWarnings("serial")
public class BulletMultiChartDemo extends AbstractDemo {
    private final Color bg = Color.WHITE;
    private JPanel optionsPanel = null;
    private JPanel demoPanel = null;
    private Orientation orientation = Orientation.horizontal;
    
    @Override
	public String getName() {
		return "Bullet Multi-Chart demo";
	}

	@Override
	public String getProduct() {
		return PRODUCT_NAME_CHARTS;
	}	

    @Override
	public Component getOptionsPanel() {
    	if (optionsPanel == null) {
    		optionsPanel = new JPanel();
    		JPanel orientationPanel = new JPanel();
    		JideBoxLayout layout = new JideBoxLayout(orientationPanel, JideBoxLayout.Y_AXIS);
    		orientationPanel.setLayout(layout);
    		final JRadioButton horizontalButton = new JRadioButton("Horizontal");
    		final JRadioButton verticalButton = new JRadioButton("Vertical");
    		ButtonGroup buttonGroup = new ButtonGroup();
    		buttonGroup.add(horizontalButton);
    		buttonGroup.add(verticalButton);
    		orientationPanel.add(horizontalButton);
    		orientationPanel.add(verticalButton);
    		optionsPanel.add(orientationPanel);
    		ActionListener listener = new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				if (horizontalButton.isSelected()) {
    					orientation = Orientation.horizontal;
    				} else {
    					orientation = Orientation.vertical;
    				}
    				updateDemoPanel(orientation);
    			}
    		};
    		horizontalButton.addActionListener(listener);
    		verticalButton.addActionListener(listener);
    		horizontalButton.setSelected(true);
    	}
		return optionsPanel;
	}

	@Override
	public int getAttributes() {
		return ATTRIBUTE_NEW | ATTRIBUTE_BETA;
	}
	
	private void updateDemoPanel(Orientation orientation) {
		demoPanel.removeAll();
		demoPanel.add(createBullets(orientation));
		demoPanel.revalidate();
		demoPanel.repaint();
	}

	public JPanel getDemoPanel() {
		if (demoPanel == null) {
			demoPanel = new JPanel(new GridLayout(1, 1));
			demoPanel.setBackground(bg);
			updateDemoPanel(orientation);
			demoPanel.setPreferredSize(new Dimension(600, 500));
		}
		return demoPanel;
	}

    private Component createBullets(Orientation orientation) {
        GridBagLayout gb = new GridBagLayout();
        JPanel panel = new JPanel(gb);
        panel.setBackground(bg);
        Bullet revenueBullet = createBullet(orientation, 0, 300, 50, null, 270, 150, 230, 250);
        addLabel(panel, createBulletLabel(revenueBullet, orientation, "Revenue", "U.S. $ (1,000s)"), orientation, 0);
        addBullet(panel, revenueBullet, orientation, 0);
        Bullet profitBullet = createBullet(orientation, 0, 30, 5, new DecimalFormat("#'%'"), 22.5, 20, 25, 26.5);
        addLabel(panel, createBulletLabel(profitBullet, orientation, "Profit", "%"), orientation, 1);
        addBullet(panel, profitBullet, orientation, 1);
        Bullet orderSizeBullet = createBullet(orientation, 0, 600, 100, null, 320, 350, 500, 550);
        String orderString = orientation == horizontal ? "Avg. Order Size" : "Avg Order";
        addLabel(panel, createBulletLabel(orderSizeBullet, orientation, orderString, "U.S. $"), orientation, 2);
        addBullet(panel, orderSizeBullet, orientation, 2);
        String custString = orientation == horizontal ? "New Customers" : "New Cust";
        Bullet customersBullet = createBullet(orientation, 0, 2500, 500, null, 1650, 1400, 2000, 2100);
        addLabel(panel, createBulletLabel(customersBullet, orientation, custString, "Count"), orientation, 3);
        addBullet(panel, customersBullet, orientation, 3);
        Bullet satisfactionBullet = createBullet(orientation, 0, 5, 1, null, 4.6, 3.5, 4.3, 4.45);
        String satisfaction = orientation == horizontal ? "Cust. Satisfaction" : "Cust Sat";
        addLabel(panel, createBulletLabel(satisfactionBullet, orientation, satisfaction, "Top Rating of 5"), orientation, 4);
        addBullet(panel, satisfactionBullet, orientation, 4);
        return panel;
    }

    private BulletLabel createBulletLabel(Bullet bullet, Orientation orientation, String line1, String line2) {
        int alignment = orientation == horizontal ? BulletLabel.RIGHT : BulletLabel.CENTER;
        String alignmentStr = orientation == horizontal ? "right" : "center";
        String html = String.format("<html><p style=\"text-align:%s;font-size:100%%\"><b>%s</b><br><span style=\"font-size:66%%\">%s</span></p></html>", alignmentStr, line1, line2);
        Font f = new Font("Arial", Font.PLAIN, 18);
        BulletLabel label = new BulletLabel(bullet, html, alignment);
        label.setFont(f);
        return label;
    }

    private JPanel addBullet(JPanel panel, Bullet bullet, Orientation orientation, int count) {
        int x = orientation == horizontal ? 1 : count;
        int y = orientation == horizontal ? count : 1;
        Insets insets = orientation == horizontal ? new Insets(0, 0, 0, 2) : new Insets(0, 0, 2, 0);
        GridBagConstraints c = new GridBagConstraints(x, y, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
        panel.add(bullet, c);
        return panel;
    }

    private JPanel addLabel(JPanel panel, JComponent label, Orientation orientation, int count) {
        int x = orientation == horizontal ? 0 : count;
        int y = orientation == horizontal ? count : 0;
        int anchor = orientation == horizontal ? GridBagConstraints.EAST : GridBagConstraints.SOUTH;
        int fill = orientation == horizontal ? GridBagConstraints.HORIZONTAL : GridBagConstraints.BOTH;
        double xWeight = orientation == horizontal ? 0.15 : 0.15;
        double yWeight = orientation == horizontal ? 0 : 0.05;
        GridBagConstraints c = new GridBagConstraints(x, y, 1, 1, xWeight, yWeight, anchor, fill, new Insets(2, 2, 0, 0), 0, 0);
        panel.add(label, c);
        return panel;
    }

    private Bullet createBullet(Orientation orientation, double from, double to, double majorTickInterval, NumberFormat numberFormat, double value, double firstIntervalEnd, double secondIntervalEnd, double pointMarker) {
        Bullet bullet = new Bullet();
        bullet.setOrientation(orientation);
        // Use Proportional Insets to ensure that the bullet scales align
        // Otherwise the sizes of the tick labels are used and may be different across the different bullet instances
        Insets2D insets = orientation == horizontal ? new Insets2D.Double(0, 0.03, 0, 0.045) : new Insets2D.Double(0.045, 0.13, 0.03, 0);
        bullet.setProportionalInsets(insets);
        bullet.setBackground(bg);
        BarIndicatorRenderer barRenderer = new BarIndicatorRenderer();
        barRenderer.setBarBreadth(0.2);
        bullet.setRenderer(barRenderer);
        bullet.addDrawable(new BulletIntervalMarker(bullet, new Color(128, 128, 128), 0, firstIntervalEnd));
        bullet.addDrawable(new BulletIntervalMarker(bullet, new Color(180, 180, 180), firstIntervalEnd, secondIntervalEnd));
        final BulletPointMarker bulletPointMarker = new BulletPointMarker(bullet, Color.BLACK, pointMarker);
        bullet.addDrawable(bulletPointMarker);
        BulletAxis axis = new BulletAxis(from, to);
        if (numberFormat != null) {
            axis.setNumberFormat(numberFormat);
        }
        bullet.setAxis(axis);
        IndicatorStyle style = new IndicatorStyle();
        style.setFill(Color.BLACK);
        bullet.addIndicator("value", style);
        bullet.setValue("value", value);
        axis.setMajorTickInterval(majorTickInterval);
        BulletTickStyle majorTickStyle = new BulletTickStyle();
        majorTickStyle.setTickColor(Color.LIGHT_GRAY);
        majorTickStyle.setTickWidth(0.2);
        axis.setMajorTickStyle(majorTickStyle);
        BulletTickStyle minorTickStyle = new BulletTickStyle();
        minorTickStyle.setVisible(false);
        axis.setMinorTickStyle(minorTickStyle);
        return bullet;
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new BulletMultiChartDemo());
    }

	
}