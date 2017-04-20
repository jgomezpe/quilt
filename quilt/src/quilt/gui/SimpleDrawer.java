package quilt.gui;

import java.awt.Color;
import java.awt.Graphics;

public class SimpleDrawer extends Drawer{
	
	protected Graphics g;
	
	public SimpleDrawer( Graphics g, int scale ){
		this.g = g;
		this.scale = scale;
	}
	
	@Override
	public void drawLine(int start_x, int start_y, int end_x, int end_y){
		g.drawLine(scale(start_x),scale(start_y),scale(end_x),scale(end_y));
	}

	@Override
	public void drawString(int x, int y, String str) {
		g.drawString(str, scale(x), scale(y));
	}

	@Override
	public Color setColor(Color color) {
		Color c = g.getColor();
		g.setColor(color);
		return c;
	}
}