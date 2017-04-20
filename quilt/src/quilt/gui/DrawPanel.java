package quilt.gui;
import javax.swing.JPanel;

import quilt.Remnant;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 *
 * @author Jonatan Gómez
 * @version 1.0
 */
public class DrawPanel extends JPanel{
	/**
	 * Serialization purposes 
	 */
	private static final long serialVersionUID = -608013051247107752L;
  
	public Remnant remnant = null;

	public DrawPanel(){
		this( null );
	}

	public DrawPanel( Remnant remnant ) {
		this.remnant = remnant;
		setBackground(new Color(255,255,255));
	}

	public Remnant get(){
		return remnant;
	}

	public void set( Remnant remnant ){
		this.remnant = remnant;
	}

	/**
	 * Paints the graphic component
	 * @param g Graphic component
	 */
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		if( remnant != null ){
			Dimension d = this.getSize();
			//System.out.println( "Painting..." + d );
			int w = Math.min(d.width, d.height);
			int wr = Math.max(remnant.columns(), remnant.rows());
			Drawer drawer = new SimpleDrawer( g, w/wr );
			remnant.draw(drawer, 0, 0);
		}
	}
}