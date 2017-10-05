package fun_pl.demo;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunSymbolCommand;
import fun_pl.util.Constants;
import unalcol.language.LanguageException;
import unalcol.types.collection.Collection;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.vector.Vector;

public class FunDemoMachine extends FunMachine{
	protected FunDemoCommand plus;
	
	public FunDemoMachine() {
		plus = new FunDemoCommand(this);
	}

	@Override
	public Object value(String value) throws Exception{ return Integer.parseInt(value); }

	@Override
	public Array<String> values(String value) throws LanguageException{
		try{ 
			Vector<String> v = new Vector<String>();
			v.add(""+this.value(value));
			return v; 
		}catch(Exception e){ throw new LanguageException(Constants.novalue, value); }
	}

	@Override
	public FunCommand primitive(String command) throws LanguageException{
		if(plus.name().equals(command)) return plus;
		throw new LanguageException(Constants.nocommand,command);
	}

	@Override
	public FunSymbolCommand symbol_command() {
		return plus;
	}

	@Override
	public FunSymbolCommand symbol_command(String symbol) {
		return plus;
	}

	@Override
	public boolean can_assign(String variable, Object value) {
		return true; // (variable.charAt(0)!=FunEncoder.get_symbol(FunEncoder.DOLLAR) || (Integer)value == 1);
	}

	@Override
	public Collection<String> primitives() {
		HTKeyMap<String,String> v = new HTKeyMap<String,String>();
		v.set(plus.name(),plus.name());
		return v;
	}

	@Override
	public Collection<String> values() { return null; }
}