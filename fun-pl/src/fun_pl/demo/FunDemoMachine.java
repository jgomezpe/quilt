package fun_pl.demo;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunSymbolCommand;
import unalcol.types.collection.Collection;
import unalcol.types.collection.SearchCollection;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.vector.Vector;

public class FunDemoMachine extends FunMachine{
	protected FunDemoCommand plus;
	
	public FunDemoMachine() {
		plus = new FunDemoCommand(this);
	}

	@Override
	public Object value(String value){ try{ return Integer.parseInt(value); }catch(NumberFormatException e){ return null; } }

	@Override
	public Array<String> composed(String value){
		if( this.value(value) != null ){
			Vector<String> v = new Vector<String>();
			v.add(value);
			return v; 
		}else return null;
	}

	@Override
	public FunCommand primitive(String command){
		if(plus.name().equals(command)) return plus;
		else return null;
	}

	@Override
	public FunSymbolCommand symbol_command() {
		return plus;
	}

	@Override
	public FunSymbolCommand symbol_command(String symbol) {
		if(plus.name().equals(symbol)) return plus;
		else return null;
	}

	@Override
	public boolean can_assign(String variable, Object value) {
		return true; // (variable.charAt(0)!=FunEncoder.get_symbol(FunEncoder.DOLLAR) || (Integer)value == 1);
	}

	@Override
	public SearchCollection<String> primitives() {
		HTKeyMap<String,String> v = new HTKeyMap<String,String>();
		v.set(plus.name(),plus.name());
		return v;
	}

	@Override
	public Collection<String> values() { return null; }
}