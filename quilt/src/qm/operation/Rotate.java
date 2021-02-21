package qm.operation;

import funpl.semantic.FunCommand;
import funpl.semantic.FunMachine;
import funpl.util.FunConstants;
import utila.I18N;
import qm.quilt.Quilt;

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
public class Rotate extends FunCommand {
	public Rotate(FunMachine machine) { super(machine); }
	public Rotate() { super(); }

	public Quilt execute(Quilt quilt){
		quilt = (Quilt)quilt.clone();
		quilt.rotate();
		return quilt;
	}
	
	@Override
	public int arity() { return 1; }

	@Override
	public Object execute(Object... args) throws Exception{
		if( !(args[0] instanceof Quilt) ) throw exception(FunConstants.argmismatch + args[0]);
		return execute((Quilt)args[0]); 
	}

	@Override
	public String name() { return "@"; }	

	public String comment(){ return I18N.process("·@·"); }

	@Override
	public Object[] reverse(Object obj, Object[] toMatch) throws Exception {
		Quilt quilt = (Quilt)((Quilt)obj).clone();
		quilt.undo_rotate();
		if( toMatch[0]==null ){
			return new Quilt[]{quilt};
		}
		Quilt q2 = (Quilt)toMatch[0];
		if( q2.equals(quilt)) return new Quilt[]{q2};
		throw exception(FunConstants.argmismatch + name());
	}
}