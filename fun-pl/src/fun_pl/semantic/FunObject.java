package fun_pl.semantic;

import unalcol.iterator.Position2DTrack;

public abstract class FunObject{
	protected Position2DTrack pos;
	
	public FunObject( Position2DTrack pos ){ this.pos = pos; }
	
	public Position2DTrack pos(){ return pos; }
	
	public void setPos( Position2DTrack pos ){ this.pos = pos; }
	
	public abstract String name();
}