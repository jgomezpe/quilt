package quilt;

import java.io.Serializable;

import quilt.gui.Drawer;
import quilt.operation.Rotatable;
import quilt.operation.Rotate;

/**
*
* Quilt
* <P>A quilt: Matrix of Minimal Remnants
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/Quilt.java" target="_blank">
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
public class Quilt extends Remnant implements Rotatable<Remnant>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7564986718868499795L;
	protected MinRemnant[][] remnant;
	public Quilt( Remnant left, Remnant right ){
		int c = left.columns();
		remnant = new MinRemnant[left.rows()][c+right.columns()];
		for( int i=0; i<rows(); i++ ){
			for( int j=0; j<left.columns(); j++ ) remnant[i][j] = left.get(i, j);
			for( int j=0; j<right.columns(); j++ ) remnant[i][j+c] = right.get(i, j);
		}
	}
	
	public Quilt( MinRemnant[][] remnant ){
		this.remnant = remnant;
	}

	public Object clone(){
		MinRemnant[][] r = new MinRemnant[rows()][columns()];
		for( int i=0; i<r.length; i++ ){
			for( int j=0; j<r[0].length; j++ ){
				r[i][j] = (MinRemnant)get(i,j).clone();
			}
		}
		return new Quilt(r);
	}
	
	public MinRemnant get( int r, int c ){
		if( 0<=r && r<rows() && 0<=c && c<columns()){
			return remnant[r][c];
		}
		return null;
	}

	@Override
	public int rows() {
		return remnant.length;
	}

	@Override
	public int columns() {
		return remnant[0].length;
	}

	@Override
	public void draw(Drawer g, int column, int row) {
		for( int i=0; i<rows(); i++ )
			for( int j=0; j<columns(); j++ ) remnant[i][j].draw(g, column+j, row+i);
	}	

	public Remnant[] unstitch(){
		int c = columns();
		if( c>1 ){
			int r = rows();
			c--;
			MinRemnant[][] left_m = new MinRemnant[r][c];
			MinRemnant[][] right_m = new MinRemnant[r][1];
			for( int i=0; i<r; i++ ){
				for( int j=0; j<c; j++){
					left_m[i][j] = remnant[i][j];
				}
				right_m[i][0] = remnant[i][c];
			}
			if( r==1 ){
				if(c==1) return new Remnant[]{left_m[0][0],right_m[0][0]};
				else return new Remnant[]{new Quilt(left_m),right_m[0][0]};
			}else{
				return new Remnant[]{new Quilt(left_m),new Quilt(right_m)};
			}
		}
		return null;
	}
	
	public Remnant[] leftunstitch(){
		int c = columns();
		if( c>1 ){
			int r = rows();
			c--;
			MinRemnant[][] right_m = new MinRemnant[r][c];
			MinRemnant[][] left_m = new MinRemnant[r][1];
			for( int i=0; i<r; i++ ){
				for( int j=0; j<c; j++){
					right_m[i][j] = remnant[i][j+1];
				}
				left_m[i][0] = remnant[i][0];
			}
			if( r==1 ){
				if(c==1) return new Remnant[]{left_m[0][0],right_m[0][0]};
				else return new Remnant[]{left_m[0][0],new Quilt(right_m)};
			}else{
				return new Remnant[]{new Quilt(left_m),new Quilt(right_m)};
			}
		}
		return null;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for( int i=0; i<rows(); i++ ){
			for( int j=0; j<columns(); j++ ){
				sb.append(get(i,j));
			}
			sb.append('\n');
		}
		return sb.toString();
	}

	@Override
	public Remnant rotate( Rotate command ) {
		MinRemnant[][] r = new MinRemnant[columns()][rows()];
		for( int i=0; i<r.length; i++ ){
			for( int j=0; j<r[0].length; j++ ){
				r[i][j] = (MinRemnant)command.execute(get(j,columns()-i-1));
			}
		}
		return new Quilt(r);
	}	
}