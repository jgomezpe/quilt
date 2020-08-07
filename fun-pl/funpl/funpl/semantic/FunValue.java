package funpl.semantic;

import funpl.util.FunConstants;
import nsgl.generic.keymap.KeyMap;

public class FunValue extends FunCommandCall{
	protected Object obj = null;
	protected Exception e = null;
	public FunValue(int pos, String src, FunMachine machine, String name) {
		super(pos, src, machine, name);
		try{ obj = machine.value.get(name); }catch(Exception e){this.e = exception(FunConstants.novalue + name);}
	}
	public Object execute( KeyMap<String,Object> variables ) throws Exception{
		if( e != null ) throw e;
		return obj;
	}
}