package fun_pl.vc.awt;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import fun_pl.semantic.FunMachine;
import fun_pl.util.Util;
import fun_pl.vc.FunBackEnd;
import fun_pl.vc.FunController;
import fun_pl.vc.GUIFunConstants;
import unalcol.gui.render.RenderPanel;
import unalcol.i18n.I18N;
import unalcol.util.Instance;
import unalcol.vc.FrontEnd;
import unalcol.vc.VCModel;

/**
*
* ProgrammingFrame
* <P>GUI for the sewer machine.
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt_computer/src/quilt/computer/ProgrammingFrame.java" target="_blank">
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
public class ProgrammingFrame extends JFrame implements TitleComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7085997916395110108L;
	
	// Window area
	protected ProgrammingPanel windowPanel;
	BorderLayout windowLayout = new BorderLayout();
	BorderLayout windowPaneLayout = new BorderLayout();

	public ProgrammingFrame(String machine_txt, RenderPanel drawPanel){ this(machine_txt,drawPanel,null); }
	
	public ProgrammingFrame(String machine_txt, RenderPanel drawPanel, String styles){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
		this.setSize(new Dimension(width*4/5, height*4/5));
		windowPanel = new ProgrammingPanel(this, drawPanel, styles);
		this.getContentPane().setLayout(windowLayout);
		this.getContentPane().add(windowPanel, java.awt.BorderLayout.CENTER);
		// Closing the window
		this.addWindowListener( new WindowAdapter(){
			public void windowClosing( WindowEvent e ){
				System.exit(0);
			} 
		} );
	}
	
	public ProgrammingPanel windowPanel(){ return windowPanel; }
	
	public static void load( Instance<FunMachine> instance, RenderPanel render, String conf_file, String styles ){
		FunController.setInstance(instance);
		GUIFunConstants.FMC=I18N.get(GUIFunConstants.FMC);
		GUIFunConstants.FMS=I18N.get(GUIFunConstants.FMS);
		GUIFunConstants.FMP=I18N.get(GUIFunConstants.FMP);
		String machine_txt = Util.config(conf_file);
		ProgrammingFrame frame = new ProgrammingFrame(machine_txt, render, styles);
		ProgrammingPanel panel = frame.windowPanel();
		
		FunBackEnd backend = new FunBackEnd();
		FrontEnd frontend = new AWTFunFrontEnd(panel);
		new VCModel(backend,frontend);
		panel.setBackEnd(backend);
		panel.setMachine( machine_txt );

		Image img = Util.image("remnant.png");
		frame.setIconImage(img);
		frame.setVisible(true);
	}	
}