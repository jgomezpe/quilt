package quilt.remnant;

import quilt.gui.Drawer;
import quilt.util.Util;
import unalcol.gui.paint.Color;

public class Strip implements Comparable<Object>{
	protected Color color=null;
	protected int[] start;
	protected int[] end;
	
	public Strip( int[] control ){
		this(new int[]{control[0],control[1]}, new int[]{control[2],control[3]});
	}

	public Strip( int[] control, Color color ){
		this(control);
		this.color = color;
	}
	
	public Strip( int[] start, int[] end ){
		init(start,end);
	}
	
	public Strip( int[] start, int[] end, Color color ){
		this(start,end);
		this.color = color;
	}

	public void init( int[] start, int[] end ){
		if( Util.compare(start, end) <= 0){
			this.start = start.clone();
			this.end = end.clone();
		}else{
			this.start = end.clone();
			this.end = start.clone();
		}
	}
	
	public static Strip[] clone( Strip[] strips ){
		Strip[] _strips = new Strip[strips.length];
		for( int i=0; i<_strips.length; i++ ) _strips[i] = strips[i].clone();
		return _strips;
	}
	
	public Strip clone(){ return new Strip(start, end, color); }
	
	public void rotate(int SIDE){ init( Util.rotate(start, SIDE), Util.rotate(end, SIDE) );	}

	@Override
	public int compareTo(Object other) {
		if( !(other instanceof Strip) ) return Integer.MAX_VALUE;
		Strip two = (Strip)other;
		int c = (color==null||two.color==null)?0:color.compareTo(two.color);
		if( c==0 ){
			c=Util.compare(start, two.start);
			return c!=0?c:Util.compare(end, two.end);
		}
		return c;
	}
	
	public void draw( Drawer g, int column, int row ){
		if( color != null ){ g.setColor(color); }
		g.drawLine(column+start[0], row+start[1], column+end[2], row+end[3]);
	}
}