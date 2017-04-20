package quilt;

import java.awt.Color;

import quilt.gui.Drawer;

public class StripsRemnant extends MinRemnant{
	protected Color color;
	protected int[][] strips;

	public StripsRemnant( Color color, int[][] strips ) {
		this.color = color;
		this.strips = strips;
	}
	
	public Color color(){ return color;	}
	
	public int[][] strips(){ return strips; }
	
	public void draw( Drawer g, int column, int row ){
		int one = unit();
		column = units(column);
		row = units(row);
		Color c = g.setColor(color());
		g.drawLine(column, row, column+one, row);
		g.drawLine(column+one, row, column+one, row+one);
		g.drawLine(column+one, row+one, column, row+one);
		g.drawLine(column, row+one, column, row);
		int[][] segments = strips();
		for( int i=0; i<segments.length; i++ ){
			g.drawLine(column+segments[i][0], row+segments[i][1], column+segments[i][2], row+segments[i][3]);
		}
		g.setColor(c);
	}

	public Remnant[] unstitch(){ return null; }	
	
	public boolean equals( Remnant r ){
		if( r==null ) return false;
		if( r instanceof StripsRemnant ){
			StripsRemnant other = (StripsRemnant)r;
			if( strips.length!=other.strips.length ) return false;
			boolean flag = true;
			int i=0;
			while( flag && i<strips.length ){
				int j=0; 
				while( j<strips[i].length && strips[i][j]==other.strips[i][j] ) j++;
				flag = j==strips[i].length;
				i++;
			}
			return flag;
		}else{
			return r.equals(this);
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
//		if( color()==Color.black ) sb.append('B');
		if( color()==Color.red ) sb.append('R');
		if( color()==Color.green ) sb.append('G');
		return sb.toString();
	}
}