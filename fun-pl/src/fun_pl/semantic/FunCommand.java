package fun_pl.semantic;

import unalcol.io.Position;
import unalcol.io.Position2D;

public abstract class FunCommand extends FunObject{
	protected FunMachine machine;
	
	public FunCommand( Position pos, FunMachine machine ){ 
		super(pos);
		this.machine = machine;
	}
	
	public FunCommand( FunMachine machine ){ this( new Position2D(), machine); }

	public FunCommand(){ this( null ); }
	
	public void setMachine(FunMachine machine){ this.machine = machine; }
	public FunMachine machine(){ return machine; }
	
	public abstract Object execute( Object... value ) throws Exception;
	public abstract int arity();
}