package quilt.operation;

import java.util.Hashtable;
import java.util.Vector;

import quilt.Language;
import quilt.QuiltMachine;
import quilt.Remnant;

/**
*
* CommandCall
* <P>A command execution call.
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/operation/CommandCall.java" target="_blank">
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
public class CommandCall {
	protected String name;
	protected CommandCall[] args=null;

	public CommandCall( String name ){
		this.name = name;
	}
	
	public CommandCall( String name, CommandCall[] args ){
		this(name);
		this.args = args;
	}

	public String name(){ return name; }
	public CommandCall[] args(){ return args; }
	public int arity(){ return args!=null?args.length:0; }
	
	public Remnant execute( QuiltMachine machine, Hashtable<String, Remnant> values ) throws Exception{
		Remnant r=null;
		if( arity()==0 ){
			// Checking basic remnants
			r = machine.remnant( name() ); 
			if( r!=null ) return r; 
			// Checking variables with the given name
			r = values.get(name());
			if( r!=null ) return r;
			// Checking the list of defined commands
			Vector<CommandDef> def = machine.get(name());
			// If there is not a command with the given name return null
			if( def == null || def.size()==0 ) throw new Exception( machine.message(Language.UNDEFINED)+" "+name());
			// Checking for a command with no arguments
			int i=0; 
			while( i<def.size() && def.get(i).arity()!=0 ){ i++; }
			// If not command matches return null
			if( i==def.size() )  throw new Exception( machine.message(Language.ARGS)+" "+name());			
			// Executes the first command matching the name
			return def.get(i).execute(machine, new Remnant[0]);
		}else{
			// Obtains a command matching the name
			Vector<CommandDef> def = machine.get(name());
			// If not command matches the name return null
			if( def == null || def.size()==0 )  throw new Exception( machine.message(Language.UNDEFINED)+" "+name());
			Remnant[] val = null; 
			String str = machine.message(Language.ARGS)+" "+name();
			int i=0;
			while( r==null && i<def.size() ){
				if(def.get(i).arity()==arity() ){
					// Evaluating the arguments of the command call, if not already evaluated
					if( val == null ){
						val = new Remnant[arity()];
						boolean flag = true;
						int k=0;
						while(flag&&k<val.length){
							val[k]=args[k].execute(machine, values);
							k++;
						}
					}
					try{
						r = def.get(i).execute(machine, val);
					}catch(Exception e){
						str = e.getMessage();
					}	
				}
				i++; 
			}
			if( r==null ) throw new Exception(str);
			return r;
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
			sb.append("Call Args:");
			sb.append('\n');
			for( int i=0; i<args.length; i++ ){
				sb.append(args[i].toString(k+1));
			}
		}
		return sb.toString();
	}
	
	public String toString(){
		return toString(0);
	}	
}