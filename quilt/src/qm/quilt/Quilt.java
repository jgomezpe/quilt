package qm.quilt;

import nsgl.gui.canvas.Util;
import nsgl.generic.JSONCastable;
import nsgl.json.JSON;
import qm.remnant.Remnant;

/**
*
* Remnant
* <P>Abstract definition of a remnant
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/Remnant.java" target="_blank">
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
public interface Quilt extends JSONCastable{
	
	// Size
	int rows();
	int columns();
	default int[] bounding_box(){ return new int[]{rows(), columns()}; }

	// MinRemnant 
	Remnant get( int r, int c );
	
	Object clone();
		
	// Drawing
	default JSON draw( int column, int row ) {
		JSON json = draw();
		if( column!=0 || row != 0 ) {
			JSON wrap = Util.create(Util.TRANSLATE);
			wrap.set(Util.X, column);
			wrap.set(Util.Y, row);
			Object[] commands = new Object[1];
			commands[0] = json;
			wrap.set(Util.COMMANDS, commands);
			json = wrap;
		}
		return json;
	}
	
	JSON draw();

	default JSON json() {
		JSON json = draw();
		if( json.string(Util.COMMAND).equals(Util.COMPOUND) ) {
			json.set(Util.COMMAND, Util.FIT);
		}else {
			JSON njson = Util.create(Util.FIT);
			Object[] commands = new Object[1];
			commands[0] = json;
			njson.set(Util.COMMANDS, commands);
			json = njson;
		}
		json.set(Util.X, 1.0/columns());
		json.set(Util.Y, 1.0/rows());
		json.set(Util.R, true);
		return json;
	}
	
	void rotate();	
	void undo_rotate();	
}