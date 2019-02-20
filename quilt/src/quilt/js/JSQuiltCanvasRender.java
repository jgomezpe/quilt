package quilt.js;

import quilt.Quilt;
import quilt.QuiltCanvasRender;
import unalcol.gui.paint.Drawable;
import unalcol.js.gui.JSCanvasRender;

public class JSQuiltCanvasRender extends JSCanvasRender implements QuiltCanvasRender{

	public JSQuiltCanvasRender() { super(""); }

	public JSQuiltCanvasRender(String id) {
		super(id);
	}

	@Override
	public void add( Drawable obj ){
		init();
		objects().add(obj);
		Quilt q = (Quilt)obj;
		int width = q.columns()* Quilt.UNIT;
		int height = q.rows()* Quilt.UNIT;
		System.out.println("[JSQuiltCanvasRender]"+width+","+height);
		execute("fitCanvas", width, height);
	}
}
