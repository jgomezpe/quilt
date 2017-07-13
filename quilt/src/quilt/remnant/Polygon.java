package quilt.remnant;

import quilt.gui.Color;
import quilt.gui.Drawer;
import quilt.util.Util;

public class Polygon implements Comparable<Object>{
	protected Color color=null;
	protected int[] x;
	protected int[] y;
	
	public Polygon(int[] x, int[] y){
		init(x,y);
	}

	public Polygon(int[] x, int[] y, Color color){
		this(x,y);
		this.color = color;
	}
	
	public void init( int[] x, int[] y ){
		this.x = new int[x.length];
		this.y = new int[x.length];
		
		int min = 0;
		for( int i=1; i<x.length; i++ )	if( x[i]<x[min] || (x[i]==x[min] && y[i]<y[min]) ) min = i;
		double pu = (double)(y[(min+x.length-1)%x.length]-y[min])/(double)(x[(min+x.length-1)%x.length]-x[min]+1);
		double pd = (double)(y[(min+1)%x.length]-y[min])/(double)(x[(min+1)%x.length]-x[min]+1);
		int inc = (pd<pu)?1:-1;
		for( int i=0; i<x.length; i++ ){
			this.x[i] = x[min];
			this.y[i] = y[min];
			min = (min+inc+x.length)%x.length;
		}
	}

	public Polygon clone(){
		return new Polygon(x, y, color);
	}
	
	public void rotate(int SIDE){
		for( int i=0; i<x.length; i++ ){
			int[] p = Util.rotate(x[i], y[i], SIDE);
			x[i] = p[0];
			y[i] = p[1];
		}
		init( x, y );
	}
	
	@Override
	public int compareTo(Object other) {
		if( !(other instanceof Polygon) ) return Integer.MAX_VALUE;
		Polygon p = (Polygon)other;
		int c = Util.compare(x, p.x);
		return (c==0)?Util.compare(y, p.y):c;
	}
	
	public void draw( Drawer g, int column, int row ){
		if( color != null ){ g.setColor(color); }
		int[] mx = x.clone();
		int[] my = y.clone();
		for( int i=0; i<x.length; i++ ){
			mx[i] +=column;
			my[i] += row;
		}
		g.drawFillPolygon(mx, my);
	}	
}