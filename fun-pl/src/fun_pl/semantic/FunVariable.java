package fun_pl.semantic;

import fun_pl.util.Constants;
import unalcol.io.Position;
import unalcol.language.LanguageException;
import unalcol.types.collection.keymap.KeyMap;

public class FunVariable extends FunCommandCall{
	public FunVariable(Position pos, FunMachine machine, String name) { super(pos, machine, name); }
	public Object execute( KeyMap<String,Object> variables ) throws Exception{
		Object obj = variables.get(name()); 
		if( obj != null ) return obj;
		throw new LanguageException(Constants.novar,name(),row()+1,column()+1);
	}

	public KeyMap<String, Object> match( KeyMap<String, Object> variables, Object... values ) throws Exception{
		if( values.length!=1 )  throw new LanguageException(Constants.argnumbermismatch,name(),row()+1,column()+1,1,values.length);
		String n=name();
		Object obj = variables.get(n);
		if( obj == null ){ 
			if(machine.can_assign(n, values[0])) variables.set(n,values[0]);
			else  throw new LanguageException(Constants.argmismatch,name(),row()+1,column()+1, values[0].toString());
		}else{
			if( !obj.equals(values[0]) ) throw new LanguageException(Constants.argmismatch,name(),row()+1,column()+1, values[0].toString());
		}		
		return variables;
	}
}