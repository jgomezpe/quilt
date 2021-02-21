package qm.awt;

import aplikigo.gui.awt.CanvasRender;

public class QuiltRender extends CanvasRender {
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