package quilt.remnant;

import quilt.gui.Color;
import quilt.gui.ColorInstance;
import unalcol.gui.util.Instance;

public class StripInstance implements Instance<Strip>{
	public static final String STRIP="strip";
	
	protected ColorInstance c_instance = new ColorInstance();

	@Override
	public Strip load(Object[] args) {
		if( args.length<5 || args.length>6 || !STRIP.equals(args[0]) ) return null;
		if( args.length==6 ){
			Color c = c_instance.load((Object[])args[1]);
			int[] control = new int[]{(int)args[2], (int)args[3], (int)args[4], (int)args[5]};
			return new Strip(control,c);
		}else{
			int[] control = new int[]{(int)args[1], (int)args[2], (int)args[3], (int)args[4]};
			return new Strip(control);
		}		
	}

	@Override
	public Object[] store(Strip obj) {
		int[] control = obj.control;
		if( obj.color==null ) return new Object[]{STRIP, control[0], control[1], control[2], control[3]};
		else  return new Object[]{STRIP, c_instance.store(obj.color), control[0], control[1], control[2], control[3]};
	}
}