package quilt.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import quilt.Remnant;
import quilt.basic.BasicQuiltMachine;


public class DrawFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3169120551000963064L;

	protected BorderLayout borderLayout1 = new BorderLayout();
	protected DrawPanel drawPanel = new DrawPanel();
	
	public DrawFrame() {
		super();
	    try {
	        jbInit();
	      }
	      catch(Exception e) {
	        e.printStackTrace();
	      }
	}
	
	public void setRemnant( Remnant remnant ){
		drawPanel.set(remnant);
		drawPanel.repaint();
	}

	protected void jbInit() throws Exception {
		this.setSize(new Dimension(500, 550));
		this.setTitle("Colcha");
		this.getContentPane().setLayout(borderLayout1);
	    this.getContentPane().add(drawPanel,  BorderLayout.CENTER);
	}  
	
	public static void main( String[] args ){
		BasicQuiltMachine m = new BasicQuiltMachine();
		try{
			Remnant r = m.execute("sew(rot(sew(diag,diag)),rot(sew(squa,squa)))");
			DrawFrame frame = new DrawFrame();
			frame.setVisible(true);
			frame.setRemnant(r);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
}