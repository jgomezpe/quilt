package fun_pl.semantic;

import unalcol.io.Position2D;

public abstract class FunObject{
	protected Position2D pos;
	
	public FunObject( Position2D pos ){ this.pos = pos; }
	
	public Position2D pos(){ return pos; }
	
	public void setPos( Position2D pos ){ this.pos = pos; }
	
	public abstract String name();
}