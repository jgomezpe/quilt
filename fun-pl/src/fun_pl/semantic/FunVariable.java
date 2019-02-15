package fun_pl.semantic;

import fun_pl.util.FunConstants;
import unalcol.iterator.Position2DTrack;
import unalcol.language.LanguageException;
import unalcol.collection.KeyMap;

public class FunVariable extends FunCommandCall{
	public FunVariable(Position2DTrack pos, FunMachine machine, String name) { super(pos, machine, name); }
	public Object execute( KeyMap<String,Object> variables ) throws LanguageException{
		try{ return variables.get(name()); }catch(Exception e ){ throw exception(FunConstants.novar); }
	}	

	public KeyMap<String, Object> match( KeyMap<String, Object> variables, Object... values ) throws LanguageException{
		if( values.length!=1 )  throw exception(FunConstants.argnumbermismatch,1,values.length);
		String n=name();
		boolean match = true;
		try{
			Object obj = variables.get(n);
			match = obj.equals(values[0]);
		}catch(Exception e){ 
			match = machine.can_assign(n, values[0]);
			if(match) variables.set(n,values[0]);
		}		
		if( !match ) throw exception(FunConstants.argmismatch, values[0]);
		return variables;
	}
}