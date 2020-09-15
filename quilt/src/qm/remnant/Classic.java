package qm.remnant;

import nsgl.generic.hashmap.HashMap;
import nsgl.gui.canvas.Util;
import nsgl.json.JSON;

public class Classic extends Remnant{
	public static HashMap<String, Integer> reductions = new HashMap<String, Integer>();
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
	public JSON draw() {
		JSON json = super.draw();
		if( rot > 0 ) {
			double r = (Math.PI*rot)/2.0;
			JSON wrap = Util.create(Util.ROTATE);
			wrap.set(Util.R, r);
			wrap.set(Util.X, 0.5);
			wrap.set(Util.Y, 0.5);
			Object[] commands = new Object[1];
			commands[0] = json;
			wrap.set(Util.COMMANDS, commands);
			json = wrap;			
		}
		return json;
	}

	@Override
	public void rotate() {
		Integer reduction = reductions.get(id);
		int mod = reduction!=null?reduction:4;
		rot = (rot + 1)%mod;
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
