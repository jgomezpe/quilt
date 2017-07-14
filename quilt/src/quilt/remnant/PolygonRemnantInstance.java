package quilt.remnant;

import quilt.Remnant;
import unalcol.gui.paint.ColorInstance;
import unalcol.gui.util.Instance;

public class PolygonRemnantInstance implements Instance<Remnant>{
	public static final String POLYGONS="polygons";
	protected ColorInstance c = new ColorInstance();
	protected PolygonInstance p = new PolygonInstance(); 

	@Override
	public Remnant load(Object[] args) {
		if( args.length<2 || !POLYGONS.equals(args[0])) return null;
		Polygon[] pol = new Polygon[args.length-2];
		for( int i=0; i<pol.length; i++ ) pol[i] = p.load((Object[])args[i+2]); 
		return new PolygonRemnant(c.load((Object[])args[1]), pol);
	}

	@Override
	public Object[] store(Remnant r) {
		if( !(r instanceof PolygonRemnant)) return null;
		PolygonRemnant pol = (PolygonRemnant)r;
		Object[] obj = new Object[2+pol.p.length];
		obj[0] = POLYGONS;
		obj[1] = c.store(pol.color);
		for( int i=0; i<pol.p.length; i++ ) obj[2+i] = p.store(pol.p[i]);
		return obj;
	}	
/*
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
		PolygonRemnant r = new PolygonRemnant(new Color(255,0,0,255), new Polygon[]{p});
		PolygonRemnantInstance pr = new PolygonRemnantInstance();
		obj = pr.store(r);
		str = ObjectParser.store(obj);
		System.out.println(str);		
	} 
	*/	
}