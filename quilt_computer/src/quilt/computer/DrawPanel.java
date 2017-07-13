package quilt.computer;
import javax.swing.JPanel;

import quilt.Remnant;
import quilt.gui.Drawer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;

/**
*
* DrawPanel
* <P>Panel of the Frame window for drawing quilts.
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/gui/DrawPanel.java" target="_blank">
* Source code </A> is available.
*
* <h3>License</h3>
*
* Copyright (c) 2017 by Jonatan Gomez-Perdomo. <br>
* All rights reserved. <br>
*
* <p>Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
* <ul>
* <li> Redistributions of source code must retain the above copyright notice,
* this list of conditions and the following disclaimer.
* <li> Redistributions in binary form must reproduce the above copyright notice,
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
* <li> Neither the name of the copyright owners, their employers, nor the
* names of its contributors may be used to endorse or promote products
* derived from this software without specific prior written permission.
* </ul>
* <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNERS OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*
*
*
* @author <A HREF="http://disi.unal.edu.co/profesores/jgomezpe"> Jonatan Gomez-Perdomo </A>
* (E-mail: <A HREF="mailto:jgomezpe@unal.edu.co">jgomezpe@unal.edu.co</A> )
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
		this.updateUI();
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
			Drawer drawer = new AWTDrawer( g, w/wr );
			remnant.draw(drawer, 0, 0);
		}
	}
}