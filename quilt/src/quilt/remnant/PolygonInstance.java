package quilt.remnant;

import quilt.Remnant;
import quilt.gui.ColorInstance;
import quilt.util.Util;
import unalcol.gui.util.Instance;
import unalcol.gui.util.ObjectParser;

public class PolygonInstance implements Instance<Polygon>{
	public static final String POLYGON="polygon";
	
	protected ColorInstance c = new ColorInstance();
	
	@Override
	public Polygon load(Object[] args) {
		if(args.length<3 || args.length>4 || !POLYGON.equals(args[0]) ) return null;
		return ( args.length == 3 )? new Polygon(Util.load((Object[])args[1], 0),Util.load((Object[])args[2], 0)) :
			new Polygon(Util.load((Object[])args[1], 0),Util.load((Object[])args[2], 0),c.load((Object[])args[3]));
	}

	@Override
	public Object[] store(Polygon p) {
		return p.color!=null?new Object[]{POLYGON,c.store(p.color),Util.store(p.x),Util.store(p.y)}:new Object[]{POLYGON,Util.store(p.x),Util.store(p.y)};
	}
	
	public static void main( String[] args ){
		int[] x = new int[]{40,60,60,40};
		int[] y = new int[]{0,0,100,100};
		Polygon p = new Polygon(x, y);
		PolygonInstance pi = new PolygonInstance();
		Object[] obj = pi.store(p);
		String str = ObjectParser.store(obj);
		System.out.println(str);
		p.rotate(Remnant.UNIT);
		obj = pi.store(p);
		str = ObjectParser.store(obj);
		System.out.println(str);
	}	
}