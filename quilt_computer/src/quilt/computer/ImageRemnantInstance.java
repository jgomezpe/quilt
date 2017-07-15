package quilt.computer;

import quilt.Remnant;
import quilt.util.Util;
import unalcol.gui.util.Instance;

public class ImageRemnantInstance implements Instance<Remnant>{
	public static final String IMAGE = "image";
	
	@Override
	public Remnant load(Object[] args) {
		if( args.length<2 || args.length>3 || !IMAGE.equals(args[0]) ) return null;
		int rot = args.length==2?0:(int)args[2];
		String name = (String)args[1];
		System.out.println(name);
		return new ImageRemnant(name, Util.image(name), rot);
	}

	@Override
	public Object[] store(Remnant obj) {
		if(!(obj instanceof ImageRemnant)) return null;
		ImageRemnant img = (ImageRemnant)obj;
		return img.rot==0?new Object[]{IMAGE,img.name}:new Object[]{IMAGE,img.name,img.rot};
	}
}