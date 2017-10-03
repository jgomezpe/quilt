package fun_pl.semantic;

import unalcol.io.Position;
import unalcol.io.SimplePosition;

public abstract class FunCommand extends FunObject{
	protected FunMachine machine;
	
	public FunCommand( Position pos, FunMachine machine ){ 
		super(pos);
		this.machine = machine;
	}
	
	public FunCommand( FunMachine machine ){ this( new SimplePosition(), machine); }
	
	public abstract Object execute( Object... value ) throws Exception;
	public abstract int arity();
}