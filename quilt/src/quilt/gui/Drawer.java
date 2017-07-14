package quilt.gui;

import unalcol.gui.paint.Color;

//
//Quilt Sewer Machine 1.0 by Jonatan Gomez-Perdomo
//https://github.com/jgomezpe/quilt/tree/master/quilt/
//
/**
*
* Drawer
* <P>GUI for drawing quilts.
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/gui/Drawer.java" target="_blank">
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
public abstract class Drawer {
	protected int scale=100; // A value of 100 means 1 to 1 scale
	
	public int setScale( int scale ){
		int t = this.scale;
		this.scale = scale;
		return t;
	}
	
	public int scale( int value ){
		return value*scale/100;
	}
	
	public int[] scale( int[] value ){
		int[] svalue = new int[value.length];
		for( int i=0; i<svalue.length; i++ ) svalue[i] = scale(value[i]);
		return svalue;
	}
	
	public abstract Color setColor( Color color );
	public abstract void drawLine( int start_x, int start_y, int end_x, int end_y );	
	public abstract void drawFillPolygon( int[] x, int[] y );	
	public abstract void drawFill( int start_x, int start_y, int width, int height );	
	public abstract void drawImage( int start_x, int start_y, int width, int height, int rot, Object image );	
	public abstract void drawString( int x, int y, String str );
}