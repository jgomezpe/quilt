package qm.quilt;

import nsgl.gui.paint.Command;
import nsgl.json.JSON;
import qm.remnant.Remnant;

public class Nil implements Quilt{

	@Override
	public int rows(){ return 0; }

	@Override
	public int columns() { return 0; }

	@Override
	public Remnant get(int r, int c) { return null;	}

	@Override
	public Object clone() { return this; }

	@Override
	public boolean equals(Object quilt) { return quilt!=null && quilt instanceof Nil; }

	@Override
	public JSON draw(){ return Command.beginPath(); }

	@Override
	public void rotate() {	}

	@Override
	public void undo_rotate() {	}
}