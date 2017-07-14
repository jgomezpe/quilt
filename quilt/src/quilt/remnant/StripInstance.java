package quilt.remnant;

import unalcol.gui.paint.Color;
import unalcol.gui.paint.ColorInstance;
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
		if( obj.color==null ) return new Object[]{STRIP, obj.start[0], obj.start[1], obj.end[2], obj.end[3]};
		else  return new Object[]{STRIP, c_instance.store(obj.color), obj.start[0], obj.start[1], obj.end[2], obj.end[3]};
	}
}