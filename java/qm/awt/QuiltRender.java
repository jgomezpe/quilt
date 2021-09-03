package qm.awt;

import aplikigo.awt.canvas.AWTCanvasRender;

public class QuiltRender extends AWTCanvasRender {
	protected double scale = 1.0;
	protected String id;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5282523442224484114L;
	
	public QuiltRender(){ super(); }
	
	public QuiltRender( String id ){ this.id=id ; }
	
	@Override
	public void render( Object obj ){
		init();
		super.render(obj);
	}	
}