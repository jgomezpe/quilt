package quilt.factory;

import java.util.Hashtable;

import quilt.operation.Command;
import unalcol.gui.util.Instance;

public class QuiltCommandInstance implements Instance<Command[]> {
	protected Hashtable<String, Command> map = new Hashtable<String,Command>();
	public static final String COMMANDS="commands";

	public void register( Command c ){ map.put(c.name(), c); }
	
	public void clear(){ map.clear(); }
	
	@Override
	public Command[] load(Object[] args) {
		if( args.length<2 || !COMMANDS.equals(args[0]) ) return null;
		Command[] c = new Command[args.length-1];
		for( int i=0;i<c.length; i++) c[i] = map.get(args[i+1]); 
		return c;
	}

	@Override
	public Object[] store(Command[] obj) {
		Object[] code = new Object[obj.length+1];
		code[0] = COMMANDS;
		for( int i=0; i<obj.length; i++ ) code[i+1] = obj[i].name();
		return code;
	}
}