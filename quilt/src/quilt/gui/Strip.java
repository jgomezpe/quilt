package quilt.gui;

import quilt.util.Util;
import unalcol.gui.paint.Canvas;
import unalcol.gui.paint.Color;

/**
*
* Strip
* <P>A straight line with a single color.
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/gui/Strip.java" target="_blank">
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
public class Strip implements Comparable<Object>{
	protected Color color=null;
	protected int[] start;
	protected int[] end;
	
	public Strip( int[] control ){
		this(new int[]{control[0],control[1]}, new int[]{control[2],control[3]});
	}

	public Strip( int[] control, Color color ){
		this(control);
		this.color = color;
	}
	
	public Strip( int[] start, int[] end ){
		init(start,end);
	}
	
	public Strip( int[] start, int[] end, Color color ){
		this(start,end);
		this.color = color;
	}

	public void init( int[] start, int[] end ){
		if( Util.compare(start, end) <= 0){
			this.start = start.clone();
			this.end = end.clone();
		}else{
			this.start = end.clone();
			this.end = start.clone();
		}
	}
	
	public static Strip[] clone( Strip[] strips ){
		Strip[] _strips = new Strip[strips.length];
		for( int i=0; i<_strips.length; i++ ) _strips[i] = strips[i].clone();
		return _strips;
	}
	
	public Strip clone(){ return new Strip(start, end, color); }
	
	public void rotate(int SIDE){ init( Util.rotate(start, SIDE), Util.rotate(end, SIDE) );	}

	@Override
	public int compareTo(Object other) {
		if( !(other instanceof Strip) ) return Integer.MAX_VALUE;
		Strip two = (Strip)other;
		int c = (color==null||two.color==null)?0:color.compareTo(two.color);
		if( c==0 ){
			c=Util.compare(start, two.start);
			return c!=0?c:Util.compare(end, two.end);
		}
		return c;
	}
	
	public void draw( Canvas g, int column, int row ){
		if( color != null ){ g.setColor(color); }
		g.drawLine(column+start[0], row+start[1], column+end[2], row+end[3]);
	}
}