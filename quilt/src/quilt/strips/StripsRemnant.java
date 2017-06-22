package quilt.strips;

import quilt.MinRemnant;
import quilt.Remnant;
import quilt.gui.Drawer;
import quilt.util.Util;
import quilt.gui.Color;


/**
*
* StripsRemnant
* <P>A remnant with some strips.
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/strips/StripsRemnant.java" target="_blank">
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
public class StripsRemnant extends MinRemnant{
	protected Color color;
	protected Strip[] strips;

	public StripsRemnant( Color color, int[][] strips ) {
		this.color = color;
		this.strips = new Strip[strips.length];
		for( int i=0; i<strips.length; i++){ this.strips[i] = new Strip(strips[i], color); }
		Util.sort(this.strips);
	}

	public StripsRemnant( Color color, Strip[] strips ) {
		this.color = color;
		this.strips = strips;
		Util.sort(this.strips);
	}
	
	public Color color(){ return color;	}
	
	public Strip[] strips(){ return strips; }
	
	public void draw( Drawer g, int column, int row ){
		super.draw(g,column,row);
		column = units(column);
		row = units(row);
		Color c = g.setColor(color());
		for( Strip s:strips){
			s.draw(g,column, row);
		}
		g.setColor(c);
	}

	public Remnant[] unstitch(){ return null; }	
	
	public boolean equals( Remnant r ){
		if( r==null ) return false;
		if( r instanceof StripsRemnant ){
			StripsRemnant other = (StripsRemnant)r;
			return Util.compare(strips, other.strips);
		}else{
			return r.equals(this);
		}
	}	
}