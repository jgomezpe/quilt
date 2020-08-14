package qm.awt;

public class Render extends nsgl.gui.awt.CanvasRender {
	protected double scale = 1.0;
	protected String id;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5282523442224484114L;
	
	public Render(){ super(); }
	
	public Render( String id ){ this.id=id ; }
	
	@Override
	public void render( Object obj ){
		init();
		super.render(obj);
	}	
}