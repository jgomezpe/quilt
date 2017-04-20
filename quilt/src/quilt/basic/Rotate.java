package quilt.basic;

import quilt.Language;
import quilt.MinRemnant;
import quilt.Quilt;
import quilt.QuiltMachine;
import quilt.Remnant;
import quilt.StripsRemnant;
import quilt.operation.Command;

/**
*
* Rotate
* <P>Rotates a quilt 90 degrees counter clock wise.
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/basic/Rotate.java" target="_blank">
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
public class Rotate extends Command{
	public Rotate() {
		super(BasicQuiltMachine.ROTATE, new String[]{"X"});
	}

	public Remnant execute(Remnant value){
		if( value instanceof StripsRemnant ){
			StripsRemnant r = (StripsRemnant)value;
			int[][] strips = r.strips().clone();
			for( int i=0; i<strips.length; i++ ){
				int x = strips[i][0];
				int y = strips[i][1];
				strips[i][0] = y;
				strips[i][1] = 100-x;
				x = strips[i][2];
				y = strips[i][3];
				strips[i][2] = y;
				strips[i][3] = 100-x;
			}
			return new StripsRemnant(r.color(), strips);
		}else{
			if( value instanceof NaturalNumberRemnant ){
				return value;
			}else{	
				MinRemnant[][] r = new MinRemnant[value.columns()][value.rows()];
				for( int i=0; i<r.length; i++ ){
					for( int j=0; j<r[0].length; j++ ){
						r[i][j] = (MinRemnant)execute(value.get(j, i));
					}
				}
				return new Quilt(r);
			}
		}		
	}
	
	@Override
	public Remnant execute(QuiltMachine machine, Remnant[] value) throws Exception{
		if( value.length == args.length ){
			return execute(value[0]);
		}
		throw new Exception(machine.message(Language.ARGS)+" "+name());
	}
}