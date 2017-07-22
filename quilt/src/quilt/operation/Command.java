package quilt.operation;

import quilt.QuiltMachine;
import quilt.Remnant;
import quilt.syntax.QuiltSymbols;
import unalcol.gui.editor.ErrorManager;

/**
*
* Command
* <P>A command that can be executed by the quilt machine
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/operation/Command.java" target="_blank">
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
public abstract class Command{
	protected String name;
	protected String[] args=null;

	public Command( String name ){
		this.name = name;
	}
	
	public Command(String name, String[] args ){
		this(name);
		this.args = args;
	}	

	public String name(){ return name; }
	public String[] args(){ return args; }

	public abstract Remnant execute( QuiltMachine machine, Remnant[] value ) throws Exception;

	public String toString(ErrorManager language){
		StringBuilder sb = new StringBuilder();
		sb.append(language.get(name));
		sb.append(name);
		if( args!=null && args.length>0 ){
			sb.append(QuiltSymbols.left());
			sb.append(args[0]);
			for( int i=1; i<args.length; i++ ){
				sb.append(QuiltSymbols.comma());
				sb.append(args[i]);
			}
			sb.append(QuiltSymbols.right());
		}
		return sb.toString();
	}	
}