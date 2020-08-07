package qm.awt;

import java.awt.Dimension;
import java.awt.Graphics;

import nsgl.generic.Collection;
import nsgl.gui.paint.Command;
import nsgl.gui.paint.Drawable;
import nsgl.json.JXON;
import qm.quilt.Quilt;

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
		Quilt q = (Quilt)obj;
		add(q);
		updateUI();
	}
	
	protected double fit(int columns, int rows) {
		Dimension d = this.getSize();
		double w = d.getWidth();
		double h = d.getHeight();
		if( w/columns < h/rows ) scale = w/columns;
		else scale = h/rows;
		return scale;
	}
	
	/**
	 * Paints the graphic component
	 * @param g Graphic component
	 */
	protected void paintComponent(Graphics g){
		int n=objects.size();
		if(n==1){
			try{
				Quilt quilt = (Quilt)objects.get(0);
				fit( quilt.columns(), quilt.rows() );
			}catch(Exception e){}
		}
		super.paintComponent(g);
	}
	
	@Override
	public void render(){
		Collection<Drawable> c = objects();
		for( Drawable d:c ) {
			JXON json = Command.create(Command.SCALE);
			json.set(Command.X, scale);
			json.set(Command.COMMANDS, new Object[] {d.draw()});
			getCanvas().draw(json);
		}
	}	
}