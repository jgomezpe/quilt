package quilt.basic;

import quilt.MinRemnant;
import quilt.Remnant;
import quilt.StripsRemnant;
import quilt.gui.Drawer;

public class NaturalNumberRemnant implements Remnant{
	protected StripsRemnant one;
	protected int n;
	public NaturalNumberRemnant( int n, StripsRemnant one ){
		this.n = n;
		this.one = one;
	}
	
	public int n(){ return n; } 

	@Override
	public int rows() {
		return 1;
	}

	@Override
	public int columns() {
		return n;
	}

	@Override
	public void draw(Drawer g, int column, int row) {
		row = units(row);
		column = units(column);
		g.drawString(column, row, ""+n);
	}
	
	protected StripsRemnant one(){ return one; }

	@Override
	public MinRemnant get(int r, int c) {
		if( 0<=r && r<rows() && 0<=c && c<columns()){
			return one();
		}
		return null;
	}

	public Remnant[] unstitch(){ return n>0?new Remnant[]{ new NaturalNumberRemnant(n-1,one()), one()}:null; }

	public String toString(){
		StringBuilder sb = new StringBuilder();
//		if( color()==Color.black ) sb.append('B');
		if( 0<=n && n<10 ) sb.append(n);
		else sb.append('N');
		return sb.toString();
	}

	@Override
	public boolean equals(Remnant r) {
		if( r==null || r.rows()!=rows() || columns()!=r.columns() ) return false;
		if( r instanceof NaturalNumberRemnant ) return n==((NaturalNumberRemnant)r).n;
		int i=1;
		while(i<=n && one().equals(r.get(0, i-1))) i++;
		return i>n;
	}
}