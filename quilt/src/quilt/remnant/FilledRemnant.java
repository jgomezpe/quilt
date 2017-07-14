package quilt.remnant;

import quilt.MinRemnant;
import quilt.Remnant;
import quilt.gui.Drawer;
import unalcol.gui.paint.Color;

public class FilledRemnant extends MinRemnant{
	protected Color color;
	protected int side;

	public FilledRemnant( Color color, int side ) {
		this.color = color;
		this.side = side;
	}

	public Remnant clone(){	return new FilledRemnant(color(), side);	}
	
	public Color color(){ return color;	}
	
	public int side(){ return side; }
	
	@Override
	public void draw( Drawer g, int column, int row ){
		super.draw(g,column,row);
		column = units(column);
		row = units(row);
		int one = unit();
		g.setColor(color());
		g.drawFill(column+(one-side)/2, row+(one-side)/2, side, side);
	}

	public boolean equals( Remnant r ){
		r = check(r);
		if( r!=null && r instanceof FilledRemnant ){
			FilledRemnant other = (FilledRemnant)r;
			return super.equals(r) && side==other.side;
		}
		return false;
	}	
}