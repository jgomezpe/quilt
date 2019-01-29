package quilt;

import unalcol.json.JSON;

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
	public JSON draw(int column, int row){ return new JSON(); }
}