package quilt;

import quilt.gui.Drawer;

public interface Remnant {
	public static final int UNIT = 100;
	public int rows();
	public int columns();
	public MinRemnant get( int r, int c );
	public Remnant[] unstitch();
	public default int[] bounding_box(){ return new int[]{rows(), columns()}; }
	public default int unit(){ return UNIT; };
	public default int units( int value ){ return value*UNIT; }
	public void draw( Drawer g, int column, int row );
	
	public default boolean equals(Remnant r) {
		if( r==null || r.rows()!=rows() || columns()!=r.columns() ) return false;
		boolean flag = true;
		for( int i=0; i<rows() && flag; i++ ){
			for( int j=0; j<columns() && flag; j++ ){
				flag = get(i,j).equals(r.get(i, j));
			}				
		}
		return flag;
	}	
}