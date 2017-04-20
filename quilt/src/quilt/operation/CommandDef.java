package quilt.operation;

import java.util.Hashtable;

import quilt.Language;
import quilt.QuiltMachine;
import quilt.Remnant;

/**
*
* CommandDef
* <P>A command definition in terms of Command calls.
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/operation/CommandDef.java" target="_blank">
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
public class CommandDef {
	protected String name;
	protected String[] args=null;
	protected CommandCall body=null;
	protected Command primitive=null;

	public CommandDef( String name, String[] args, CommandCall body ){
		this.name = name;
		this.args = args;
		this.body = body;
	}
	
	public CommandDef( String name, String[] args, Command primitive ){
		this.name = name;
		this.args = args;
		this.primitive = primitive;
	}
	
	public String name(){ return name; }
	public int arity(){ return args.length; }
	
	public Remnant execute( QuiltMachine machine, Remnant[] value ) throws Exception{
		if( value.length != args.length ) throw new Exception(machine.message(Language.ARGS)+" "+name());
		if( body != null ){
			Hashtable<String,Remnant> vars = new Hashtable<String,Remnant>();
			for( int i=0; i<args.length; i++ ){
				int p = args[i].indexOf('.');
				if( p >= 0 ){
					if( value[i].columns()==1 ) throw new Exception(machine.message(Language.UNSTITCH)+" "+name());
					Remnant[] divided = value[i].unstitch();
					vars.put(args[i].substring(0, p), divided[0]);
					vars.put(args[i].substring(p+1), divided[1]);
				}else{
					if( args[i].startsWith(QuiltMachine.PRIMITIVE) ){
						if( value[i].rows()>1 || value[i].columns()>1 ) throw new Exception(machine.message(Language.QUILT)+" "+name());
						vars.put(args[i], value[i].get(0, 0));
					}else{
						Remnant r = machine.remnant(args[i]);
						if( r!=null && !r.equals(value[i]) ) throw new Exception(machine.message(Language.MISMATCH)+" "+name());
						vars.put(args[i], value[i]);
					}
				}
			}
			return body.execute(machine, vars);
		}else{
			return primitive.execute(machine, value);
		}
	}
		
	public String tab( int k ){
		StringBuilder sb = new StringBuilder();
		for( int i=0; i<k; i++ ) sb.append(' ');
		return sb.toString();
	}

	public String toString(int k){
		StringBuilder sb = new StringBuilder();
		sb.append(tab(k));
		sb.append("Name:");
		sb.append(name);
		sb.append('\n');
		if( args != null ){
			sb.append(tab(k));
			sb.append("Args:");
			sb.append('\n');
			for( int i=0; i<args.length; i++ ){
				sb.append(tab(k));
				sb.append(args[i]);
				sb.append('\n');
			}
		}
		if( body!=null){
			sb.append(tab(k));
			sb.append("Body:");
			sb.append('\n');
			sb.append(body.toString(k+1));
		}
		return sb.toString();
	}
	
	public String toString(){
		return toString(0);
	}
}