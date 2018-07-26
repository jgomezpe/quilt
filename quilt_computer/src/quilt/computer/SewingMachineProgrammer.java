package quilt.computer;

import java.awt.Image;

import fun_pl.vc.FunBackEnd;
import fun_pl.vc.FunController;
import fun_pl.vc.GUIFunConstants;
import fun_pl.vc.awt.AWTFunFrontEnd;
import fun_pl.vc.awt.ProgrammingFrame;
import fun_pl.vc.awt.ProgrammingPanel;
import quilt.factory.QuiltMachineInstance;
import quilt.util.QuiltConstants;
import fun_pl.util.Util;
import unalcol.i18n.I18N;
import unalcol.vc.FrontEnd;
import unalcol.vc.VCModel;

/**
*
* SewingMachineProgrammer
* <P>Main class.
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt_computer/src/quilt/computer/SewingMachingProgrammer.java" target="_blank">
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
public class SewingMachineProgrammer {
	public static void main( String[] args ){
		
		String language = args.length>=1?args[0]:QuiltConstants.SPANISH;
		Util.i18n(language);
		FunController.setInstance(new QuiltMachineInstance());
		GUIFunConstants.FMC=I18N.get(GUIFunConstants.FMC);
		GUIFunConstants.FMS=I18N.get(GUIFunConstants.FMS);
		GUIFunConstants.FMP=I18N.get(GUIFunConstants.FMP);
		String conf_file = args.length>=2?args[1]:"default"+GUIFunConstants.FMC;
		String machine_txt = Util.config(conf_file);
		String styles = args.length==3?Util.config(args[2]):null;
		ProgrammingFrame frame = new ProgrammingFrame(machine_txt, styles);
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