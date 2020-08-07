package qm.remnant;

import nsgl.gui.paint.Command;
import nsgl.json.JXON;

public class Classic extends Remnant{
	protected int rot = 0;
	public Classic(String id) {
		super(id);
	}

	public Classic(String id, int rot) {
		super(id);
		this.rot = rot;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && obj instanceof Classic && ((Classic)obj).rot==rot;
	}

	@Override
	public JXON draw() {
		JXON json = super.draw();
		if( rot > 0 ) {
			double r = (Math.PI*rot)/2.0;
			JXON wrap = Command.create(Command.ROTATE);
			wrap.set(Command.R, r);
			wrap.set(Command.X, 0.5);
			wrap.set(Command.Y, 0.5);
			Object[] commands = new Object[1];
			commands[0] = json;
			wrap.set(Command.COMMANDS, commands);
			json = wrap;			
		}
		return json;
	}

	@Override
	public void rotate() {
		rot = (rot + 1)%4;
	}

	@Override
	public void undo_rotate() {
		rot = (rot + 3)%4;
	}

	@Override
	public Object clone() {
		return new Classic(this.id,this.rot);
	}

}
