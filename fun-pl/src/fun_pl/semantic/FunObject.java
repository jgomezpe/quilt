package fun_pl.semantic;

import unalcol.io.Position;
import unalcol.io.Position2D;

public abstract class FunObject extends Position2D{
	public FunObject(){ super(); }
	
	public FunObject( Position pos ){ super(pos); }
	
	public FunObject( int row, int column ){ super(0,row,column); }
	
	public abstract String name();
}