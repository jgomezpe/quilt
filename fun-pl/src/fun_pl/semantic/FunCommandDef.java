package fun_pl.semantic;

import fun_pl.syntax.FunEncoder;
import fun_pl.util.FunConstants;
import unalcol.language.LanguageException;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.keymap.KeyMap;

public class FunCommandDef extends FunCommand{
	protected FunCommandCall left;
	protected FunCommandCall right;

	public FunCommandDef(FunMachine machine, FunCommandCall left, FunCommandCall right ){
		super( left.pos, machine );
		this.left = left;
		this.right = right;
	}
	
	public KeyMap<String,Object> match( Object... values ) throws LanguageException{
		if(left.arity()==0) return new HTKeyMap<String,Object>();
		return left.match(values);
	}

	@Override
	public Object execute( Object... values ) throws LanguageException{ 
		return right.execute(match(values)); 
	}

	public String name(){ return left.name(); }

	@Override
	public int arity(){ return left.arity(); }
	
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append(left);
		sb.append(FunEncoder.get_symbol(FunConstants.ASSIGN));
		sb.append(right);
		return sb.toString();
	}
	
}