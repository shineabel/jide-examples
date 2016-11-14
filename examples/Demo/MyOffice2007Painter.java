
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.Icon;
import javax.swing.JComponent;

import com.jidesoft.plaf.office2007.Office2007Painter;

public class MyOffice2007Painter extends Office2007Painter {

	@Override
	public void paintCommandBarBackground(JComponent c, Graphics g, Rectangle rect, int orientation, int state) {
		// TODO Auto-generated method stub
		super.paintCommandBarBackground(c, g, rect, orientation, state);
		g.setColor(new Color(61, 90, 156));
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}

	@Override
	public void paintCollapsiblePanesBackground(JComponent c, Graphics g, Rectangle rect, int orientation, int state) {
		// TODO Auto-generated method stub
		super.paintCollapsiblePanesBackground(c, g, rect, orientation, state);
		g.setColor(new Color(12,26,55));
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}

	@Override
	public void paintCollapsiblePaneTitlePaneBackground(JComponent c, Graphics g, Rectangle arg2, int arg3,
			int arg4) {
		// TODO Auto-generated method stub
		super.paintCollapsiblePaneTitlePaneBackground(c, g, arg2, arg3, arg4);
		g.setColor(Color.red);
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}
	
	@Override
	public void paintButtonBackground(JComponent c, Graphics g, Rectangle arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		super.paintButtonBackground(c, g, arg2, arg3, arg4);
		g.setColor(Color.yellow);
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}
	
	
	@Override
	public void paintDockableFrameBackground(JComponent c, Graphics g, Rectangle arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		super.paintDockableFrameBackground(c, g, arg2, arg3, arg4);
		g.setColor(Color.green);
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}
	
	@Override
	public void paintDockableFrameTitlePane(JComponent c, Graphics g, Rectangle arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub
		super.paintDockableFrameTitlePane(c, g, arg2, arg3, arg4);
		
		g.setColor(Color.pink);
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}
	@Override
	public void paintCollapsiblePaneTitlePaneBackgroundEmphasized(JComponent c, Graphics g, Rectangle rect,
			int orientation, int state) {
		// TODO Auto-generated method stub
		super.paintCollapsiblePaneTitlePaneBackgroundEmphasized(c, g, rect, orientation, state);
		
		g.setColor(Color.green);
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}
	
	@Override
	public void paintSortableTableHeaderColumn(JComponent c, Graphics g, Rectangle rect, int orientation, int state,
			int sortOrder, Icon sortIcon, int orderIndex, Color indexColor, boolean paintIndex) {
		// TODO Auto-generated method stub
		super.paintSortableTableHeaderColumn(c, g, rect, orientation, state, sortOrder, sortIcon, orderIndex, indexColor,
				paintIndex);
		
		g.setColor(Color.orange);
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}
	
	@Override
	public void paintTabBackground(JComponent c, Graphics g, Shape region, Color[] colors, int orientation, int state) {
		// TODO Auto-generated method stub
		super.paintTabBackground(c, g, region, colors, orientation, state);
		g.setColor(Color.gray);
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
	}
}
