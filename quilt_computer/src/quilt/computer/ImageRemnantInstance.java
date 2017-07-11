package quilt.computer;

import quilt.Remnant;
import unalcol.gui.util.Instance;

public class ImageRemnantInstance implements Instance<Remnant>{
	public static final String IMAGE = "image";
	
	@Override
	public Remnant load(Object[] args) {
		if( args.length<3 || args.length>4 || !IMAGE.equals(args[0]) ) return null;
		int rot = args.length==3?0:(int)args[3];
		String name = (String)args[1];
		System.out.println(name);
		boolean asResource = (boolean)args[2];
		return new ImageRemnant(name, asResource, Util.image(name, asResource), rot);
	}

	@Override
	public Object[] store(Remnant obj) {
		if(!(obj instanceof ImageRemnant)) return null;
		ImageRemnant img = (ImageRemnant)obj;
		return img.rot==0?new Object[]{IMAGE,img.name, img.asResource}:new Object[]{IMAGE,img.name, img.asResource,img.rot};
	}
}