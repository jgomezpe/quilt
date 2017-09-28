package fun_pl.semantic;

import unalcol.io.Position;

public class FunCommandDef extends FunCommand{
	protected FunCommandCall left;
	protected FunCommandCall right;

	public FunCommandDef( Position pos, FunMachine machine, FunCommandCall left, FunCommandCall right ){
		super( pos, machine );
		this.left = left;
		this.right = right;
	}

	@Override
	public Object execute( Object... values ) throws Exception{ return right.execute(left.match(values)); }

	public String name(){ return left.name(); }

	@Override
	public int arity(){ return left.arity(); }
}