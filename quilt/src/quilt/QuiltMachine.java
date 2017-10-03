package quilt;

import java.util.Iterator;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunSymbolCommand;
import fun_pl.util.Constants;
import quilt.operation.Rotate;
import quilt.operation.Sew;
import unalcol.language.LanguageException;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.keymap.KeyMap;
import unalcol.types.collection.keymap.KeyValue;
import unalcol.types.collection.vector.Vector;

/**
*
* QuiltMachine
* <P>Definition of a sewer of quilts
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/QuiltMachine.java" target="_blank">
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
public class QuiltMachine extends FunMachine{
	protected FunSymbolCommand sew;
	protected FunCommand rot;
	protected KeyMap<String, Quilt> remnants = new HTKeyMap<String,Quilt>();

	public QuiltMachine(){
		sew = new Sew(this);
		rot = new Rotate(this);
	}
	
	@Override
	public boolean can_assign(String variable, Object remnant) {
		return true;
	}

	@Override
	public FunCommand primitive(String command) throws LanguageException {
		if(sew.name().equals(command)) return sew;
		if(rot.name().equals(command)) return rot;
		throw new LanguageException(Constants.nocommand,command);
	}

	@Override
	public FunSymbolCommand symbol_command() { return sew; }

	@Override
	public FunSymbolCommand symbol_command(String arg0){ return sew; }

	@Override
	public Object value(String remnant) throws Exception {
		Quilt r = remnants.get(remnant);
		if( r!=null ) return r;
		throw new Exception("Unknown value...");
	}

	@Override
	public Array<String> values(String compose) throws LanguageException {
		Vector<String> v = new Vector<String>();
		boolean ok = true;
		while(compose.length()>0 && ok){
			Iterator<KeyValue<String,Quilt>> iter = remnants.pairs().iterator();
			String r=null;
			ok = false;
			while(iter.hasNext() && !ok){
				r = iter.next().key();
				ok = compose.indexOf(r)==0;
			}
			if(ok){
				v.add(r);
				compose.substring(r.length());
			}
		}
		if( ok ) return v;
		throw new LanguageException("Uknown value");
	}
}