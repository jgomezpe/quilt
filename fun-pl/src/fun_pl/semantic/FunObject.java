package fun_pl.semantic;

import unalcol.io.Position;
import unalcol.io.SimplePosition;

public abstract class FunObject extends SimplePosition{
	public FunObject(){ super(); }
	
	public FunObject( Position pos ){ super(pos); }
	
	public FunObject( int row, int column ){ super(row,column); }
	
	public abstract String name();
}