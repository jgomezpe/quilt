package quilt;

import java.util.Hashtable;
import java.util.Vector;

import quilt.operation.Command;
import quilt.operation.CommandDef;

/**
*
* QuiltMachine
* <P>Abstract definition of a sewer of quilts
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/QuiltMachine.java" target="_blank">
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
public abstract class QuiltMachine {
	protected Hashtable<String, Vector<CommandDef>> commands = new Hashtable<String,Vector<CommandDef>>();
	protected Hashtable<String, Remnant> remnants = new Hashtable<String,Remnant>();
	public static final String PRIMITIVE="prim";
	
	protected Language message;
	
	public QuiltMachine( Language message ){
		this.addPrimitives();
		this.message = message;
	}
	
	public String message( String code ){ return message.get(code); }
	
	public void addPrimitives(){
		Command[] primitives = this.primitives();
		CommandDef[] primitives_def = new CommandDef[primitives.length];
		for( int i=0; i<primitives.length; i++ ){
			//System.out.println( "Adding.. "+primitives[i].name());
			primitives_def[i] = new CommandDef(primitives[i].name(), primitives[i].args(), primitives[i]);
		}
		this.add(primitives_def);		
	}
	
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
	
	public void init(){
		commands.clear();
		addPrimitives();
	}
	
	public Vector<CommandDef> get( String command ){
		return commands.get(command);
	}

	public abstract Command[] primitives();

	public Remnant remnant( String remnant ){
		return remnants.get(remnant);
	}	
}