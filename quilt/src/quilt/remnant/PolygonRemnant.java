package quilt.remnant;

import quilt.Remnant;
import quilt.gui.Color;
import quilt.gui.Drawer;
import quilt.operation.Rotatable;
import quilt.operation.Rotate;

public class PolygonRemnant extends ColoredRemnant implements Rotatable<Remnant>{
	protected Polygon[] p;
	public PolygonRemnant(Color color, Polygon[] p) {
		super(color);
		this.p = new Polygon[p.length];
		for( int i=0; i<p.length; i++ ) this.p[i] = p[i].clone();
	}

	@Override
	public void draw( Drawer g, int column, int row ){
		super.draw(g,column,row);
		column = units(column);
		row = units(row);
		g.setColor(color());
		for( Polygon pol:p ) pol.draw(g, column, row);
	}
	
	@Override
	public Remnant rotate(Rotate command) {
		PolygonRemnant pr = new PolygonRemnant(color, p);
		for( Polygon pol:pr.p ) pol.rotate(Remnant.UNIT);
		return pr;
	}

	@Override
	public Object clone() {
		return new PolygonRemnant(color, p);
	}
}