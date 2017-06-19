package quilt.basic;

import quilt.MinRemnant;
import quilt.Remnant;
import quilt.gui.Drawer;
import quilt.strips.StripsRemnant;

/**
*
* NaturalNumberRemnant
* <P>A quilt that represents a natural number (not zero)
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/basic/NaturalNumberRemnant.java" target="_blank">
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
public class NaturalNumberRemnant implements Remnant{
	protected StripsRemnant one;
	protected int n;
	public NaturalNumberRemnant( int n, StripsRemnant one ){
		this.n = n;
		this.one = one;
	}
	
	public int n(){ return n; } 

	@Override
	public int rows() {
		return 1;
	}

	@Override
	public int columns() {
		return n;
	}

	@Override
	public void draw(Drawer g, int column, int row) {
		row = units(row);
		column = units(column);
		g.drawString(column, row, ""+n);
	}
	
	protected StripsRemnant one(){ return one; }

	@Override
	public MinRemnant get(int r, int c) {
		if( 0<=r && r<rows() && 0<=c && c<columns()){
			return one();
		}
		return null;
	}

	public Remnant[] unstitch(){ return n>0?new Remnant[]{ new NaturalNumberRemnant(n-1,one()), one()}:null; }

	public String toString(){
		StringBuilder sb = new StringBuilder();
//		if( color()==Color.black ) sb.append('B');
		if( 0<=n && n<10 ) sb.append(n);
		else sb.append('N');
		return sb.toString();
	}

	@Override
	public boolean equals(Remnant r) {
		if( r==null || r.rows()!=rows() || columns()!=r.columns() ) return false;
		if( r instanceof NaturalNumberRemnant ) return n==((NaturalNumberRemnant)r).n;
		int i=1;
		while(i<=n && one().equals(r.get(0, i-1))) i++;
		return i>n;
	}
}