package quilt;

import unalcol.gui.paint.Canvas;

public class NilQuilt extends Quilt{

	@Override
	public int rows(){ return 0; }

	@Override
	public int columns() { return 0; }

	@Override
	public Remnant get(int r, int c) { return null;	}

	@Override
	public Object clone() { return this; }

	@Override
	public boolean equals(Quilt quilt) { return quilt!=null && quilt instanceof NilQuilt; }

	@Override
	public void draw(Canvas g, int column, int row) {}
}