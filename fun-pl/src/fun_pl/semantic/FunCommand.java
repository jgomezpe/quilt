package fun_pl.semantic;

import unalcol.io.Position;

public abstract class FunCommand extends FunObject{
	protected FunMachine machine;
	
	public FunCommand( Position pos, FunMachine machine ){ 
		super(pos);
		this.machine = machine;
	}
	
	public abstract Object execute( Object... value ) throws Exception;
	public abstract int arity();
}