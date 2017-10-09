package fun_pl.semantic;

import fun_pl.syntax.FunEncoder;
import fun_pl.util.Constants;
import unalcol.language.LanguageException;

public class FunCommandDef extends FunCommand{
	protected FunCommandCall left;
	protected FunCommandCall right;

	public FunCommandDef(FunMachine machine, FunCommandCall left, FunCommandCall right ){
		super( left, machine );
		this.left = left;
		this.right = right;
	}
	
	public void match( Object... values ) throws LanguageException{
		left.match(values);
	}

	@Override
	public Object execute( Object... values ) throws LanguageException{ return right.execute(left.match(values)); }

	public String name(){ return left.name(); }

	@Override
	public int arity(){ return left.arity(); }
	
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append(left);
		sb.append(FunEncoder.get_symbol(Constants.ASSIGN));
		sb.append(right);
		return sb.toString();
	}
	
}