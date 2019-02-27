package quilt.operation;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunSymbolCommand;
import fun_pl.util.FunConstants;
import quilt.MatrixQuilt;
import quilt.Quilt;
import quilt.Remnant;
import unalcol.gui.paint.Command;
import unalcol.i18n.I18N;
import unalcol.language.LanguageException;

/**
*
* Rotate
* <P>Rotates a quilt 90 degrees counter clock wise.
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/basic/Rotate.java" target="_blank">
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
public class Rotate extends FunSymbolCommand implements RemnantFunction{
	public static final String name="rot";
	public Rotate(FunMachine machine) { super(machine); }

	public Quilt execute(Quilt quilt){
		if( quilt instanceof MatrixQuilt ){
			int n=quilt.rows();
			int m=quilt.columns();
			Remnant[][] r = new Remnant[m][n];
			for( int i=0; i<m; i++ )
				for( int j=0; j<n; j++ )
					r[i][j] = (Remnant)execute(quilt.get(j, m-1-i));
			return new MatrixQuilt(r);
		}else if( quilt instanceof Remnant ){
			Remnant r = (Remnant)quilt.clone();
			r.add(name());
			return r;
		}else return (Quilt)quilt.clone();
	}
	
	@Override
	public int arity() { return 1; }

	@Override
	public Object execute(Object... args) throws LanguageException {
		if( !(args[0] instanceof Quilt) ) throw exception(FunConstants.argmismatch, args[0]);
		return execute((Quilt)args[0]); 
	}

	@Override
	public String name() { return I18N.get(name); }	

	public String comment(){ return I18N.get(name+"c"); }

	@Override
	public Object[] reverse(Object obj, Object[] toMatch, FunCommand[] args) throws LanguageException {
		Quilt q = execute(execute(execute((Quilt)obj)));
		if( toMatch[0]==null ){
			return new Quilt[]{q};
		}
		Quilt q2 = (Quilt)toMatch[0];
		if( q2.equals(q)) return new Quilt[]{q2};
		return null;
	}

	//@Override
	public void apply(Command json) {
		String command = json.type(); 
		if( command.equals(Command.COMPOUND) ){
			Command[] objs = json.commands();
			for(Command o:objs) apply(o);
		}else{
			Object x; 
			Object y;
			x = json.x();
			y = json.y();
			if( x==null ) return;
			if( x instanceof Object[] ){
				Object[] tx = (Object[])x;
				Object[] ty = (Object[])y;
				for(int i=0; i<tx.length; i++ ){
					Object t = tx[i];
					tx[i]=ty[i];
					ty[i]=Quilt.UNIT-unalcol.real.Util.cast(t);
				}
			}else{
				json.set(Command.X, y);
				json.set(Command.Y, x);
			}
			if(command.equals(Command.IMAGE)){
				int r = (Integer)json.getInt(Command.IMAGE_ROT);
				r = (r+270)%360;
				json.set(Command.IMAGE_ROT,r);
			}
		}
	}
}