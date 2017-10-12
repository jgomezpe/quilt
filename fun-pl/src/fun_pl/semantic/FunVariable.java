package fun_pl.semantic;

import fun_pl.util.FunConstants;
import unalcol.io.Position;
import unalcol.language.LanguageException;
import unalcol.types.collection.keymap.KeyMap;

public class FunVariable extends FunCommandCall{
	public FunVariable(Position pos, FunMachine machine, String name) { super(pos, machine, name); }
	public Object execute( KeyMap<String,Object> variables ) throws LanguageException{
		Object obj = variables.get(name()); 
		if( obj != null ) return obj;
		throw exception(FunConstants.novar);
	}

	public KeyMap<String, Object> match( KeyMap<String, Object> variables, Object... values ) throws LanguageException{
		if( values.length!=1 )  throw exception(FunConstants.argnumbermismatch,1,values.length);
		String n=name();
		Object obj = variables.get(n);
		if( obj == null ){ 
			if(machine.can_assign(n, values[0])) variables.set(n,values[0]);
			else  throw exception(FunConstants.argmismatch, values[0]);
		}else{
			if( !obj.equals(values[0]) ) throw exception(FunConstants.argmismatch, values[0]);
		}		
		return variables;
	}
}