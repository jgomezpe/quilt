package quilt;

import unalcol.util.Factory;
import unalcol.util.Instance;

/**
*
* QuiltInstance
* <P>Load and store Quilts: (Persistent methods of Quilt)
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/QuiltInstance.java" target="_blank">
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
public class QuiltInstance implements Instance<Quilt>{
	public static final String QUILT="quilt";
	protected Factory<Quilt> remnants;
	
	public QuiltInstance(Factory<Quilt> remnants) {
		this.remnants = remnants;
	}
	
	@Override
	public Quilt load(Object[] args) {
		if( args.length==0 || !QUILT.equals(args[0]) ) return null;
		Remnant[][] grid = new Remnant[args.length-1][];
		for( int i=0; i<grid.length; i++){
			Object[] line = (Object[])args[i+1];
			grid[i] = new Remnant[line.length];
			for( int j=0; j<grid[i].length; j++){
				grid[i][j] = (Remnant)remnants.load((Object[])line[j]);
			}
		}
		return new MatrixQuilt(grid);
	}

	@Override
	public Object[] store(Quilt obj) {
		if(!( obj instanceof MatrixQuilt )) return null;
		MatrixQuilt q = (MatrixQuilt)obj;
		Object[] lines = new Object[q.rows()+1];
		lines[0] = QUILT;
		for( int i=0; i<q.rows();i++ ){
			Object[] line = new Object[q.columns()];
			for( int j=0; j<q.columns(); j++ ) line[j] = remnants.store(q.get(i, j));
			lines[i+1] = line;
		}
		return lines;
	}	
}