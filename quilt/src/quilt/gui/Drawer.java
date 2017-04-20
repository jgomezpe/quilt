package quilt.gui;

import java.awt.Color;

public abstract class Drawer {
	protected int scale=100; // A value of 100 means 1 to 1 scale
	
	public int setScale( int scale ){
		int t = this.scale;
		this.scale = scale;
		return t;
	}
	
	public int scale( int value ){
		return value*scale/100;
	}
	
	public abstract Color setColor( Color color );
	public abstract void drawLine( int start_x, int start_y, int end_x, int end_y );	
	public abstract void drawString( int x, int y, String str );
}