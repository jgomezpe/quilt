package quilt;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import quilt.operation.Command;
import quilt.operation.CommandCall;
import quilt.operation.CommandDef;
import quilt.syntax.QuiltMachineParser;
import unalcol.gui.editor.ErrorManager;
import unalcol.gui.editor.Position;

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
public class QuiltMachine {
	protected Hashtable<String, Vector<CommandDef>> commands = new Hashtable<String,Vector<CommandDef>>();
	protected Hashtable<String, Remnant> remnants = new Hashtable<String,Remnant>();
	protected Command[] primitives;
	protected QuiltMachineParser parser;
	
	public static final String SEW = "sew";
	
	public static final String QMP = ".qmp";	
	public static final String QMC = ".qmc";	
	
	protected ErrorManager language;
	
	public QuiltMachine( Command[] primitives, Hashtable<String, Remnant> remnants, 
						QuiltMachineParser parser, ErrorManager language ){
		this.addPrimitives(primitives);
		this.remnants.putAll(remnants);
		this.parser = parser;
		this.parser.setMachine(this);
		this.language = language;
	}
	
	public String message( String code ){ return language.get(code); }
	public Exception error( Position pos, String message ){ return language.error(pos, message); }
	
	public ErrorManager language(){ return language; }
	
	public void add( CommandDef[] def ){
		for( int i=0; i<def.length; i++ ){
			Vector<CommandDef> comm = commands.get(def[i].name());
			if( comm == null ){
				comm = new Vector<CommandDef>();
				commands.put(def[i].name(), comm);
			}
			comm.add(def[i]);
		}
	}
	
	public void addPrimitives(Command[] primitives){
		CommandDef[] primitives_def = new CommandDef[primitives.length];
		for( int i=0; i<primitives.length; i++ ) primitives_def[i] = new CommandDef(primitives[i]);
		this.add(primitives_def);
		this.primitives = primitives;
	}
	
	public Command[] primitives(){ return primitives; }
	
	public Command primitive( String command ){
		int i=0;
		while( i<primitives.length && !primitives[i].name().equals(command) ) i++;
		return (i<primitives.length)?primitives[i]:null;
	}
	
	public boolean is_primitive( String command ){
		int i=0;
		while( i<primitives.length && !primitives[i].name().equals(command) ) i++;
		return i<primitives.length;
	}
	
	public void init(){
		commands.clear();
		addPrimitives(primitives);
	}
	
	public Vector<CommandDef> get( String command ){
		return commands.get(command);
	}

	public Remnant remnant( String remnant ){
		return remnants.get(remnant);
	}	
	
	public String[] remnants(){
		Enumeration<String> keys = remnants.keys();
		Vector<String> v = new Vector<String>();
		while( keys.hasMoreElements() ){
			v.add(keys.nextElement());
		}
		String[] r = new String[v.size()];
		for( int i=0; i<v.size(); i++ ) r[i] = v.get(i);
		return r;
	}
	
	public CommandCall command(String command) throws Exception{
		return parser.command(command);
	}
	
	public Remnant execute(CommandCall comm) throws Exception{
		return comm.execute(this, new Hashtable<String,Remnant>());
	}
	
	public void addDef( String program ) throws Exception{
		this.add(parser.apply(program));
	}
	
	public void setProgram( String program ) throws Exception{
		init();
		addDef(program);
	}
	
	protected int pos(String str, String[] prim){
		int k=0;
		while( k<prim.length && str.indexOf(prim[k])!=0 ) k++;
		if( k<prim.length ) return prim[k].length();
		else return -1;
	}
		
	public String[] composed( String name ){
		String[] prim = this.remnants();
		Vector<String> rs = new Vector<String>();
		int k = pos(name, prim);
		while( k>0 ){
			rs.add(name.substring(0,k));
			name = name.substring(k);
			k = pos(name, prim);
		}
		int l=(name.length()==0)?rs.size():0;
		String[] r = new String[l];
		for(int i=0; i<l; i++ ) r[i] = rs.get(i);
		return r;
	}
	
	public QuiltMachineParser parser(){
		return parser;
	}
}