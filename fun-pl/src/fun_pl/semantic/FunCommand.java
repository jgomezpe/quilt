package fun_pl.semantic;

import unalcol.io.Position;

public abstract class FunCommand extends FunObject{
	public FunCommand( Position pos ){ super(pos); }
	
	public abstract Object execute( FunMachine machine, Object[] value ) throws Exception;
	public abstract int arity();
}