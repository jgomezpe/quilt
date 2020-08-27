package funpl.gui.awt;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import funpl.FunAPI;
import nsgl.gui.Render;
import nsgl.gui.TitleComponent;
import nsgl.stream.Resource;
import nsgl.stream.loader.FromClassLoader;
import nsgl.stream.loader.FromOS;
import nsgl.string.I18N;

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
   	public static Resource init( ClassLoader loader, String lang ) {
		lang = "language/"+lang+".i18n";
		Resource resource = new Resource();
		resource.add("local", new FromOS("resources/"));
		resource.add("loader", new FromClassLoader(loader));
		resource.add("guilocal", new FromOS(""));
		try { I18N.set(resource.txt(lang)); }
		catch(Exception e) { e.printStackTrace(); } 
		return resource;
   	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7085997916395110108L;
	
	// Window area
	protected ProgrammingPanel windowPanel;
	BorderLayout windowLayout = new BorderLayout();
	BorderLayout windowPaneLayout = new BorderLayout();

	public ProgrammingFrame(FunAPI api, String api_code, Render render, ClassLoader loader, String lang){
	    	Resource resource = init(loader, lang);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
		this.setSize(new Dimension(width*4/5, height*4/5));
		windowPanel = new ProgrammingPanel(this, api, api_code, render, resource);
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
}