package quilt.operation;

import java.util.Hashtable;

import quilt.QuiltMachine;
import quilt.Remnant;
import quilt.syntax.QuiltSymbols;
import quilt.util.Language;
import unalcol.gui.editor.ErrorManager;
import unalcol.gui.editor.Position;

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
public class CommandDef extends Position{
	protected String name;
	protected CommandCall[] args=null;
	protected CommandCall body=null;
	protected Command primitive=null;

	public CommandDef( Position pos, String name, CommandCall[] args, CommandCall body ){
		super( pos );
		this.name = name;
		this.args = args;
		this.body = body;
	}
		
	public CommandDef( Command c ){
		this.name = c.name();
		String[] c_args = c.args();
		args = new CommandCall[c_args.length];
		Position pos = new Position();
		for( int i=0; i<c_args.length; i++ ) args[i] = new CommandCall(pos, c_args[i]); 
		this.primitive = c;
	}
	
	public String name(){ return name; }
	public int arity(){ return args.length; }
	
	public Remnant execute( QuiltMachine machine, Remnant[] value ) throws Exception{
		ErrorManager error_manager = machine.language();
		if( value.length != args.length ) throw error_manager.error(this, error_manager.get(Language.ARGS)+" "+name());
		if( body != null ){
			Hashtable<String,Remnant> vars = new Hashtable<String,Remnant>();
			for( int i=0; i<args.length; i++ ){
				if( args[i].stitch() ){
					String name = args[i].name();
					String[] parts = name.split("[\\"+QuiltSymbols.stitch()+"\\"+QuiltSymbols.leftstitch()+"]");
					if( value[i].columns()<parts.length ) throw error_manager.error(this, error_manager.get(Language.UNSTITCH)+" "+name());
					Remnant last = value[i];
					int pos = name.indexOf(QuiltSymbols.leftstitch()); 
					while(pos>0){
						Remnant[] divided = last.leftunstitch();
						vars.put(name.substring(0, pos), divided[0]);
						last=divided[1];
						name = name.substring(pos+1);
						pos = name.indexOf(QuiltSymbols.leftstitch());
					}
					parts = name.split("\\"+QuiltSymbols.stitch());
					for( int k=parts.length-1; k>0; k--){
						Remnant[] divided = last.unstitch();
						vars.put(parts[k], divided[1]);
						last=divided[0];
					}
					vars.put(parts[0], last);					
				}else{
					if( args[i].primitive(machine.parser().symbols()) ){
						if( value[i].rows()>1 || value[i].columns()>1 ) throw error_manager.error(this, error_manager.get(Language.QUILT)+" "+name());
						vars.put(args[i].name(), value[i].get(0, 0));
					}else{
						Remnant r;
						try{
							r = args[i].execute(machine, new Hashtable<String, Remnant>());
						}catch(Exception e){
							r = null;
							vars.put(args[i].name(), value[i]);
						}	
						if( r!=null && !r.equals(value[i]) ) throw error_manager.error(this, error_manager.get(Language.MISMATCH)+" "+name());
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
	
	public static void main( String[] args ){
		String[] parts = "X#YZA|AAA#U|ZZZ".split("[\\"+QuiltSymbols.leftstitch()+"\\"+QuiltSymbols.stitch()+"]");
		for( String s:parts ) System.out.println(s); 
	}
}