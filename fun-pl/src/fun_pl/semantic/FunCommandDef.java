package fun_pl.semantic;

import fun_pl.syntax.FunEncoder;
import unalcol.util.I18N;

public class FunCommandDef extends FunCommand{
	protected FunCommandCall left;
	protected FunCommandCall right;

	public FunCommandDef(FunMachine machine, FunCommandCall left, FunCommandCall right ){
		super( left, machine );
		this.left = left;
		this.right = right;
	}

	@Override
	public Object execute( Object... values ) throws Exception{ return right.execute(left.match(values)); }

	public String name(){ return left.name(); }

	@Override
	public int arity(){ return left.arity(); }
	
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append(left);
		sb.append(I18N.get(FunEncoder.code).charAt(FunEncoder.ASSIGN));
		sb.append(right);
		return sb.toString();
	}
	
}