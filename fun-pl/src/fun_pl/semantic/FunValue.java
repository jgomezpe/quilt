package fun_pl.semantic;

import unalcol.io.Position;
import unalcol.types.collection.keymap.KeyMap;

public class FunValue extends FunCommandCall{
	protected Object obj = null;
	protected Exception e = null;
	public FunValue(Position pos, FunMachine machine, String name) {
		super(pos, machine, name);
		try{ obj = machine.value(name); }catch(Exception e){this.e = e;}
	}
	public Object execute( KeyMap<String,Object> variables ) throws Exception{
		if( e != null ) throw e;
		return obj;
	}
}