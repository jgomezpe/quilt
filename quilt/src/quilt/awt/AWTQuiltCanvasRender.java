package quilt.awt;

import java.awt.Dimension;
import java.awt.Graphics;

import quilt.Quilt;
import quilt.QuiltCanvasRender;
import unalcol.gui.render.AWTCanvasRender;

public class AWTQuiltCanvasRender extends AWTCanvasRender implements QuiltCanvasRender{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5282523442224484114L;
	
	public AWTQuiltCanvasRender( String id ){ super( id ); }
	
	/**
	 * Paints the graphic component
	 * @param g Graphic component
	 */
	protected void paintComponent(Graphics g){
		Dimension d = this.getSize();
		int w = (int)Math.min(d.getWidth(), d.getHeight());
		int n=objects.size();
		int q=1;
		if(n==1){
			Quilt quilt = (Quilt)objects.get(0);
			q=Math.max(quilt.columns(), quilt.rows());
		}
		getCanvas().setScale(w/(double)(Quilt.UNIT*q));
		super.paintComponent(g);
	}
}
