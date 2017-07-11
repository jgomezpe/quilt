package quilt.remnant;

import quilt.Remnant;
import quilt.gui.Color;
import quilt.gui.ColorInstance;
import unalcol.gui.util.Instance;

public class FilledRemnantInstance implements Instance<Remnant>{
	public static final String FILLED="filled";
	protected ColorInstance c_instance = new ColorInstance();
	
	@Override
	public Remnant load(Object[] args) {
		if( args.length!=3 || !FILLED.equals(args[0]) ) return null;
		Color c = c_instance.load((Object[])args[1]);
		return new FilledRemnant(c,(int)args[2]);
	}

	@Override
	public Object[] store(Remnant obj) {
		if(!( obj instanceof FilledRemnant)) return null;
		FilledRemnant q = (FilledRemnant)obj;
		Object[] lines = new Object[3];
		lines[0] = FILLED;
		lines[1] = c_instance.store(q.color());
		lines[2] = q.side();
		return lines;
	}	
}