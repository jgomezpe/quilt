package fun_pl.semantic;

import fun_pl.util.Constants;
import unalcol.io.Position;
import unalcol.language.LanguageException;
import unalcol.types.collection.keymap.KeyMap;

public class FunValue extends FunCommandCall{
	protected Object obj = null;
	protected LanguageException e = null;
	public FunValue(Position pos, FunMachine machine, String name) {
		super(pos, machine, name);
		try{ obj = machine.value(name); }catch(Exception e){this.e = new LanguageException(pos, Constants.novalue, name);}
	}
	public Object execute( KeyMap<String,Object> variables ) throws LanguageException{
		if( e != null ) throw e;
		return obj;
	}
}