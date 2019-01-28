package toyplus;

import fun_pl.semantic.FunSymbolCommand;
import fun_pl.semantic.FunMachine;
//import fun_pl.semantic.FunSymbolCommand;
//import unalcol.i18n.I18N;
import unalcol.types.collection.Collection;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.keymap.HashMap;
import unalcol.types.collection.keymap.ImmutableKeyMap;
import unalcol.types.collection.vector.Vector;

public class ToyPlusMachine extends FunMachine{
	public ToyPlusMachine(ImmutableKeyMap<String, FunSymbolCommand> primitives, String symbol ){ super( primitives, symbol ); }

	@Override
	public void setValues(ImmutableKeyMap<String, ?> values){}
	
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
	public boolean can_assign(String variable, Object value) {
		return true; // (variable.charAt(0)!=FunEncoder.get_symbol(FunEncoder.DOLLAR) || (Integer)value == 1);
	}

	@Override
	public Collection<String> primitives() {
		HashMap<String,String> v = new HashMap<String,String>();
		for( String name:primitives.keys()) v.set(name,name);
		return v.keys();
	}

	@Override
	public Collection<String> values() { return null; }
}