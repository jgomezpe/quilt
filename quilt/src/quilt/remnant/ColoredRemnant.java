package quilt.remnant;

import quilt.MinRemnant;
import quilt.Remnant;
import quilt.gui.Color;
import quilt.util.Util;

public abstract class ColoredRemnant extends MinRemnant{
	protected Color color;
	public ColoredRemnant( Color color ){ this.color = color; }
	public Color color(){ return color;	}
	public boolean equals(Remnant r){
		r = check(r);
		if( r!=null && r instanceof ColoredRemnant ){
			ColoredRemnant other = (ColoredRemnant)r;
			return color()==null || other.color()==null || Util.compare(color().values(), other.color().values())==0;
		}
		return false;
	}	
}
