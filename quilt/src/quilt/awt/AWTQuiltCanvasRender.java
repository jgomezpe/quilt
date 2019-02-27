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
	
	public AWTQuiltCanvasRender(){ super(); }
	
	public AWTQuiltCanvasRender( String id ){ super( id ); }
	
	/**
	 * Paints the graphic component
	 * @param g Graphic component
	 */
	protected void paintComponent(Graphics g){
		Dimension d = this.getSize();
		double w = d.getWidth();
		double h = d.getHeight();
		double wq = 1;
		double hq = 1;
		int n=objects.size();
		if(n==1){
			try{
				Quilt quilt = (Quilt)objects.get(0);
				wq=quilt.columns();
				hq=quilt.rows();
			}catch(Exception e){}
		}
		if( w/wq < h/hq ) getCanvas().setScale(w/(Quilt.UNIT*wq));
		else getCanvas().setScale(h/(Quilt.UNIT*hq));
		super.paintComponent(g);
	}
}
