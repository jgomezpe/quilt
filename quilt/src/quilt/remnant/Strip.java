package quilt.remnant;

import quilt.gui.Color;
import quilt.gui.Drawer;
import quilt.util.Util;

public class Strip implements Comparable<Object>{
	protected Color color=null;
	protected int[] control;
	public Strip( int[] control ){
		this.control = control.clone();
		if( control[0]>control[2] || (control[0]==control[2] && control[1]>control[3]) ){
			int t = control[0];
			control[0] = control[2];
			control[2] = t;
			t = control[1];
			control[1] = control[3];
			control[3] = t;
		}
	}

	public Strip( int[] control, Color color ){
		this(control);
		this.color = color;
	}
	
	public static Strip[] clone( Strip[] strips ){
		Strip[] _strips = new Strip[strips.length];
		for( int i=0; i<_strips.length; i++ ){
		    _strips[i] = strips[i].clone();
		}
		return _strips;
		
	}
	
	public Strip clone(){
		return new Strip(control, color);
	}
	
	public void rotate(){
		int x = control[0];
		int y = control[1];
		control[0] = y;
		control[1] = 100-x;
		x = control[2];
		y = control[3];
		control[2] = y;
		control[3] = 100-x;		
	}

	@Override
	public int compareTo(Object other) {
		if( !(other instanceof Strip) ) return Integer.MAX_VALUE;
		Strip two = (Strip)other;
		int c = (color==null||two.color==null)?0:color.compareTo(two.color);
		if( c==0 ) return Util.compare(control, two.control);
		return c;
	}
	
	public void draw( Drawer g, int column, int row ){
		if( color != null ){ g.setColor(color); }
		g.drawLine(column+control[0], row+control[1], column+control[2], row+control[3]);
	}
}