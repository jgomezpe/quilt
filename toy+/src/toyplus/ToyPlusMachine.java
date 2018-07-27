package toyplus;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunSymbolCommand;
import unalcol.i18n.I18N;
import unalcol.types.collection.Collection;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.keymap.ImmutableKeyMap;
import unalcol.types.collection.vector.Vector;

public class ToyPlusMachine extends FunMachine{
	protected Plus plus=null;
	protected Decrement dec=null;
	
	public ToyPlusMachine() {
		plus = new Plus(this);
		dec = new Decrement(this);
	}
	
	public ToyPlusMachine(ImmutableKeyMap<String, FunCommand> primitives){
		for( FunCommand c:primitives) 
			if( c instanceof FunSymbolCommand ){
				if( c.name().equals(I18N.get(Plus.name))) this.plus = (Plus)c;
				if( c.name().equals(I18N.get(Decrement.name))) this.dec = (Decrement)c;
			}
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
		if(dec.name().equals(command)) return dec;
		else return null;
	}

	@Override
	public FunSymbolCommand symbol_command() {
		return plus;
	}

	@Override
	public FunSymbolCommand symbol_command(String symbol) {
		if(plus!=null && plus.name().equals(symbol)) return plus;
		if(dec!=null && dec.name().equals(symbol)) return dec;
		else return null;
	}

	@Override
	public boolean can_assign(String variable, Object value) {
		return true; // (variable.charAt(0)!=FunEncoder.get_symbol(FunEncoder.DOLLAR) || (Integer)value == 1);
	}

	@Override
	public Collection<String> primitives() {
		HTKeyMap<String,String> v = new HTKeyMap<String,String>();
		if( plus!=null ) v.set(plus.name(),plus.name());
		if( dec!=null ) v.set(dec.name(),dec.name());
		return v.keys();
	}

	@Override
	public Collection<String> values() { return null; }
}