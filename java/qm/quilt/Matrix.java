package qm.quilt;

import aplikigo.gui.canvas.Util;
import jxon.JXON;
import qm.remnant.Remnant;

/**
*
* Quilt
* <P>A quilt: Matrix of Minimal Remnants
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/Quilt.java" target="_blank">
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
public class Matrix implements Quilt{
	protected Remnant[][] quilt;

	public Matrix( Quilt left, Quilt right ){
		int c = left.columns();
		quilt = new Remnant[left.rows()][c+right.columns()];
		for( int i=0; i<rows(); i++ ){
			for( int j=0; j<left.columns(); j++ ) quilt[i][j] = left.get(i, j);
			for( int j=0; j<right.columns(); j++ ) quilt[i][j+c] = right.get(i, j);
		}
	}
	
	public Matrix( Remnant[][] remnant ){
		this.quilt = remnant;
	}

	public Object clone(){
		Remnant[][] r = new Remnant[rows()][columns()];
		for( int i=0; i<r.length; i++ ){
			for( int j=0; j<r[0].length; j++ ){
				r[i][j] = (Remnant)get(i,j).clone();
			}
		}
		return new Matrix(r);
	}
	
	public Remnant get( int r, int c ){
		if( 0<=r && r<rows() && 0<=c && c<columns()){
			return quilt[r][c];
		}
		return null;
	}

	@Override
	public int rows() {
		return quilt.length;
	}

	@Override
	public int columns() {
		return quilt[0].length;
	}

	@Override
	public JXON draw() {
		JXON json = Util.create(Util.COMPOUND);
		Object[] commands = new Object[rows()*columns()];
		int k=0;
		for( int i=0; i<rows(); i++ )
			for( int j=0; j<columns(); j++ ) 
				commands[k++] = quilt[i][j].draw(j,i);
		json.set(Util.COMMANDS, commands);
		return json;
	}	

	@Override
	public void rotate() {
		int c = columns();
		int r = rows();
		Remnant[][] newquilt = new Remnant[c][r];
		for( int i=0; i<r; i++ ){
			int k = r-1-i;
			for( int j=0; j<c; j++ ){
				quilt[i][j].rotate();
				newquilt[j][k] = quilt[i][j];
			}
		}
		quilt = newquilt;
	}

	@Override
	public void undo_rotate() {
		int c = columns();
		int r = rows();
		Remnant[][] newquilt = new Remnant[c][r];
		for( int j=0; j<c; j++ ){
			int k = c-1-j;
			for( int i=0; i<r; i++ ){
				quilt[i][j].undo_rotate();
				newquilt[k][i] = quilt[i][j];
			}
		}
		quilt = newquilt;
	}
	
	public boolean equals(Object value) {
	    return equals((Quilt)value);
	}
	
	public boolean equals(Quilt quilt) {
		if( quilt.rows()!=rows() || columns()!=quilt.columns() ) return false; 
		boolean flag = true;
		for( int i=0; i<rows() && flag; i++ ){
			for( int j=0; j<columns() && flag; j++ ){
				flag = get(i,j).equals(quilt.get(i, j));
			}				
		}
		return flag;
	}	
}