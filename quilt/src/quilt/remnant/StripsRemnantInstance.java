package quilt.remnant;

import quilt.Remnant;
import unalcol.gui.paint.Color;
import unalcol.gui.paint.ColorInstance;
import unalcol.gui.util.Instance;

public class StripsRemnantInstance implements Instance<Remnant>{
	public static final String STRIPS="strips";
	
	protected StripInstance s_instance = new StripInstance();
	protected ColorInstance c_instance = new ColorInstance();
	
	@Override
	public Remnant load(Object[] args) {
		if( args.length<2 || !STRIPS.equals(args[0]) ) return null;
		Color c = c_instance.load((Object[])args[1]);
		Strip[] grid = new Strip[args.length-1];
		for( int i=0; i<grid.length; i++){
			grid[i] = s_instance.load((Object[])args[i+2]);
		}
		return new StripsRemnant(c,grid);
	}

	@Override
	public Object[] store(Remnant obj) {
		if(!( obj instanceof StripsRemnant)) return null;
		StripsRemnant q = (StripsRemnant)obj;
		Object[] lines = new Object[q.rows()+2];
		lines[0] = STRIPS;
		lines[1] = c_instance.store(q.color());
		for( int i=0; i<q.rows();i++ ){
			lines[i+2] = s_instance.store(q.strips()[i]);
		}
		return lines;
	}	
}