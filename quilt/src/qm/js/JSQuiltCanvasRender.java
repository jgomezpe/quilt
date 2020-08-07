package qm.js;

import nsgl.app.net.Channel;
import nsgl.gui.CanvasRender;
import nsgl.gui.paint.Drawable;
import qm.quilt.Quilt;

public class JSQuiltCanvasRender extends nsgl.js.CanvasRender implements CanvasRender{

	public JSQuiltCanvasRender(String id, Channel client) {
		super(id, client);
	}

	@Override
	public void add( Drawable obj ){
		init();
		add(obj);
		Quilt q = (Quilt)obj;
		int width = q.columns();
		int height = q.rows();
		System.out.println("[JSQuiltCanvasRender]"+width+","+height);
		try { run("fit", width, height); } 
		catch (Exception e) { e.printStackTrace(); }
	}
}