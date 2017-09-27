package fun_pl.semantic;

import unalcol.io.Position;

public abstract class FunCommandDef extends FunObject{
	protected FunCommand left;
	protected FunCommand right=null;

	public FunCommandDef( Position pos, FunCommand left, FunCommand right ){
		super( pos );
		this.left = left;
		this.right = right;
	}

	public abstract Object execute( FunMachine machine, Object[] value ) throws Exception;

	public String name(){ return left.name(); }
}