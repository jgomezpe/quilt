package qm.remnant;

import java.util.HashMap;

import aplikigo.gui.canvas.CanvasConstants;
import aplikigo.gui.canvas.DrawMaker;
import speco.jxon.JXON;

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
	public JXON draw() {
		JXON json = super.draw();
		if( rot > 0 ) {
			double r = (Math.PI*rot)/2.0;
			JXON wrap = DrawMaker.create(CanvasConstants.ROTATE);
			wrap.set(CanvasConstants.R, r);
			wrap.set(CanvasConstants.X, 0.5);
			wrap.set(CanvasConstants.Y, 0.5);
			Object[] commands = new Object[1];
			commands[0] = json;
			wrap.set(CanvasConstants.COMMANDS, commands);
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
