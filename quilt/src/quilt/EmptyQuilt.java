package quilt;

import quilt.gui.Drawer;

public class EmptyQuilt extends Quilt{
	public Quilt[] unstitch() throws Exception { throw new Exception("Unable to unstitch"); }	

	public int rows(){ return 0; }

	public int columns(){ return 0; }
	
	public Quilt check( Quilt r ){
		if( r!=null && r.rows()==1 && r.columns()==1 && !(r instanceof Remnant) ) return r.get(0, 0);
		return r;
	}
	
	@Override
	public Remnant get(int r, int c) { return null; }

	public void draw( Drawer g, int column, int row ){}	
	@Override
	public Object clone() {	return this; }
}