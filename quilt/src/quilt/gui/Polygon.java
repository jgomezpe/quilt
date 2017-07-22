package quilt.gui;

import quilt.util.Util;
import unalcol.gui.paint.Color;

/**
*
* Polygon
* <P>A graphic representation of a polygon with an inner single color.
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/gui/Polygon.java" target="_blank">
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
public class Polygon implements Comparable<Object>{
	protected Color color=null;
	protected int[] x;
	protected int[] y;
	
	public Polygon(int[] x, int[] y){
		init(x,y);
	}

	public Polygon(int[] x, int[] y, Color color){
		this(x,y);
		this.color = color;
	}
	
	public void init( int[] x, int[] y ){
		this.x = new int[x.length];
		this.y = new int[x.length];
		
		int min = 0;
		for( int i=1; i<x.length; i++ )	if( x[i]<x[min] || (x[i]==x[min] && y[i]<y[min]) ) min = i;
		double pu = (double)(y[(min+x.length-1)%x.length]-y[min])/(double)(x[(min+x.length-1)%x.length]-x[min]+1);
		double pd = (double)(y[(min+1)%x.length]-y[min])/(double)(x[(min+1)%x.length]-x[min]+1);
		int inc = (pd<pu)?1:-1;
		for( int i=0; i<x.length; i++ ){
			this.x[i] = x[min];
			this.y[i] = y[min];
			min = (min+inc+x.length)%x.length;
		}
	}

	public Polygon clone(){
		return new Polygon(x, y, color);
	}
	
	public void rotate(int SIDE){
		for( int i=0; i<x.length; i++ ){
			int[] p = Util.rotate(x[i], y[i], SIDE);
			x[i] = p[0];
			y[i] = p[1];
		}
		init( x, y );
	}
	
	@Override
	public int compareTo(Object other) {
		if( !(other instanceof Polygon) ) return Integer.MAX_VALUE;
		Polygon p = (Polygon)other;
		int c = Util.compare(x, p.x);
		return (c==0)?Util.compare(y, p.y):c;
	}
	
	public void draw( Drawer g, int column, int row ){
		if( color != null ){ g.setColor(color); }
		int[] mx = x.clone();
		int[] my = y.clone();
		for( int i=0; i<x.length; i++ ){
			mx[i] +=column;
			my[i] += row;
		}
		g.drawFillPolygon(mx, my);
	}	
}