package quilt.strips;

import java.awt.Color;

import quilt.MinRemnant;
import quilt.Remnant;
import quilt.gui.Drawer;

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
	protected int[][] strips;

	public StripsRemnant( Color color, int[][] strips ) {
		this.color = color;
		this.strips = strips;
		Strips.sort(strips);
	}
	
	public Color color(){ return color;	}
	
	public int[][] strips(){ return strips; }
	
	public void draw( Drawer g, int column, int row ){
		int one = unit();
		column = units(column);
		row = units(row);
		Color c = g.setColor(color());
		g.drawLine(column, row, column+one, row);
		g.drawLine(column+one, row, column+one, row+one);
		g.drawLine(column+one, row+one, column, row+one);
		g.drawLine(column, row+one, column, row);
		int[][] segments = strips();
		for( int i=0; i<segments.length; i++ ){
			g.drawLine(column+segments[i][0], row+segments[i][1], column+segments[i][2], row+segments[i][3]);
		}
		g.setColor(c);
	}

	public Remnant[] unstitch(){ return null; }	
	
	public boolean equals( Remnant r ){
		if( r==null ) return false;
		if( r instanceof StripsRemnant ){
			StripsRemnant other = (StripsRemnant)r;
			if( strips.length!=other.strips.length ) return false;
			boolean flag = true;
			int i=0;
			while( flag && i<strips.length ){
				int j=0; 
				while( j<strips[i].length && strips[i][j]==other.strips[i][j] ) j++;
				flag = j==strips[i].length;
				i++;
			}
			return flag;
		}else{
			return r.equals(this);
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
//		if( color()==Color.black ) sb.append('B');
		if( color()==Color.red ) sb.append('R');
		if( color()==Color.green ) sb.append('G');
		return sb.toString();
	}
}