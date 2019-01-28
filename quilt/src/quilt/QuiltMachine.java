package quilt;

import java.util.Iterator;

import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunSymbolCommand;
import fun_pl.util.FunConstants;
import unalcol.i18n.I18N;
import unalcol.json.JSON;
import unalcol.types.collection.Collection;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.keymap.ImmutableKeyMap;
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
	protected FunSymbolCommand rot;
	protected ImmutableKeyMap<String, ?> remnants;
	protected ImmutableKeyMap<String, String> reductions;

	public QuiltMachine(ImmutableKeyMap<String, FunSymbolCommand> primitives, String symbol){ super(primitives, symbol); }

	@Override
	public Collection<String> primitives() { return primitives.keys(); }
	
	public String reduce( String command ){
		if( reductions==null) return command;
		for( String key:reductions.keys() ){
			if(command.startsWith(key))
				return reduce(reductions.get(key)+command.substring(key.length()));
		}
		return command;
	}
	
	public JSON evaluate( String command ){ return null; }
	
	@Override
	public boolean can_assign(String variable, Object remnant) {
		if( remnant instanceof Quilt){
			Quilt q = (Quilt)remnant;
			return (variable.charAt(0)!=I18N.get(FunConstants.code).charAt(FunConstants.DOLLAR) ||q.columns()==1);
		}else return super.can_assign(variable,remnant);	
	}

	@Override
	public Object value(String remnant){ return remnants.get(remnant); }

	@Override
	public Array<String> composed(String compose){
		Vector<String> v = new Vector<String>();
		boolean ok = true;
		while(compose.length()>0 && ok){
			Iterator<String> iter = values().iterator();
			String r=null;
			ok = false;
			while(iter.hasNext() && !ok){
				r = iter.next();
				ok = compose.indexOf(r)==0;
				if(ok){
					v.add(r);
					compose = compose.substring(r.length());
				}
			}
		}
		if( ok ) return v;
		return null;
	}

	@Override
	public Collection<String> values() { return (remnants!=null)?remnants.keys():null; }

	@Override
	public void setValues(ImmutableKeyMap<String, ?> values) {
		this.remnants = values;
		for( Object q:remnants ) ((Quilt)q).setMachine(this);
	}
	
	public void setReductions( ImmutableKeyMap<String, String> reductions ){ this.reductions = reductions; }
	
	public ImmutableKeyMap<String, String> reductions(){ return reductions; }
}