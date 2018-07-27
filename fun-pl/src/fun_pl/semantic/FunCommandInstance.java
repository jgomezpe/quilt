package fun_pl.semantic;

import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.keymap.ImmutableKeyMap;
import unalcol.types.collection.keymap.KeyMap;
import unalcol.util.Instance;

public class FunCommandInstance implements Instance<ImmutableKeyMap<String,FunCommand>> {
	protected HTKeyMap<String, FunCommand> map = new HTKeyMap<String,FunCommand>();
	public static final String COMMANDS="commands";

	public void register( FunCommand c ){ map.set(c.name(), c); }
	
	public void clear(){ map.clear(); }
	
	@Override
	public ImmutableKeyMap<String,FunCommand> load(Object[] args) {
		if( args.length<2 || !COMMANDS.equals(args[0]) ) return null;
		KeyMap<String, FunCommand> c = new HTKeyMap<String,FunCommand>();
		for( int i=1; i<args.length; i++) c.set( (String)args[i], map.get((String)args[i]) ); 
		return c;
	}

	@Override
	public Object[] store(ImmutableKeyMap<String,FunCommand> obj) {
		Object[] code = new Object[obj.size()+1];
		code[0] = COMMANDS;
		int i=1;
		for( FunCommand c:obj ){
			code[i] = c.name();
			i++;
		}
		return code;
	}
}