package quilt.basic;

import quilt.Language;
import quilt.QuiltMachineParser;
import quilt.operation.CommandDef;

/**
*
* BasicQuiltMachineParser
* <P>A parser for the language of the basic quilt machine.
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/basic/BasicQuiltMachineParser.java" target="_blank">
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
public class BasicQuiltMachineParser extends QuiltMachineParser{
	
	public BasicQuiltMachineParser(){
		super();
	}
	
	public BasicQuiltMachineParser( Language message ){
		super(message);
	}
	
	public BasicQuiltMachineParser( Language message, String program ){
		super( message, program );
	}
	
	
	public String name() throws Exception{
		StringBuilder sb = new StringBuilder();
		char c = current();
		while( Character.isLetterOrDigit(c) || c=='_' ){
			sb.append(c);
			c = advance();
		}
		String txt = sb.toString();
		if(txt.length()>0) return txt;
		throw error_message(message.get(Language.LETTER_DIGIT));
	}
	
	public String s_name() throws Exception{
		char c = current();
		if( !Character.isLetter(c) ) throw error_message(message.get(Language.LETTER));
		StringBuilder sb = new StringBuilder();
		sb.append(c);
		c = advance();
		String txt = name(); 
		sb.append(txt);
		return sb.toString();
	}
	
	public String variable() throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append(name());
		char c = current();
		if( c==STITCH ){
			sb.append(c);
			c = advance();
			sb.append(name());
		}
		return sb.toString();
	}	
	
	public CommandDef[] apply( String[] remnants, CommandDef[] commands, String program ){
		return null;
	}
	
	public static void main( String[] args ){
		String program = "function(X,Y.Z),=rot(sew(X,Y))";
		BasicQuiltMachineParser parser = new BasicQuiltMachineParser();
		parser.init(Language.SPANISH);
		try{
			CommandDef[] commands = parser.apply(program);
			for( int i=0; i<commands.length; i++){
				System.out.println(commands[i]);
			}			
		}catch( Exception e ){
			System.out.println(e.getMessage());
		}
	}
}
