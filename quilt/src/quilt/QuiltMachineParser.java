package quilt;

import java.util.Vector;

import quilt.operation.CommandCall;
import quilt.operation.CommandDef;

/**
*
* QuiltMachineParser
* <P>Abstract parser of the Quilt programming language.
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/QuiltMachineParser.java" target="_blank">
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
public abstract class QuiltMachineParser extends Position{
	protected static final char EOF=(char)-1;
	protected static final char COMMENT='%';
	public static final char COMMA=',';
	public static final char STITCH='|';
	protected static final char EOL='\n';
	protected static final char SPACE=' ';
	protected static final char TAB='\t';
	public static final char ASSIGN='=';
	public static final char LEFT='(';
	public static final char RIGHT=')';

	protected Language message;
	protected String program="";
	protected int offset;
	
	public QuiltMachineParser(){ this( new Language() ); }
	
	public QuiltMachineParser( Language message ){
		super(0,0);
		this.message = message;
		offset = 0;
	}
	
	public QuiltMachineParser( Language message, String program){
		this(message);
		this.program = program;
	}
	
	public void init( String language ){
		message.init(language);
	}
	
	public boolean eof(){ return offset >= program.length(); }
	
	protected char current(){ return eof()?EOF:program.charAt(offset); }

	public char advance(){
		char c = current();
		if( c==EOL ){
			row++;
			column=0;
		}else{
			column++;
		}
		offset++;
		return current();
	}
	
	public char next(){
		char c = current();
		while( c==SPACE || c==EOL || c==TAB || c==COMMENT){
			comment();
			c = advance();
		};
		return c;
	}
	
	public char advance_next(){
		advance();
		return next();
	}
	
	public Exception error_message( char c ){ return error_message(""+c); }
	
	public Exception error_message( String c ){
		StringBuilder sb = new StringBuilder();
		sb.append(message.get(Language.UNSYMBOL));
		sb.append(" [");
		sb.append(current());
		sb.append(']');
		sb.append(", ");
		sb.append(message.get(Language.EXPECTING));
		sb.append(' ');
		sb.append(c);
		return message.error(this,sb.toString());
	}
	
	public char comment(){
		while( current()==COMMENT ){
			while(!eof() && advance()!=EOL){};
		}
		return current();
	}	
	
	public CommandCall[] values() throws Exception{
		char c = next();
		if(c != LEFT) throw error_message(LEFT);

		Vector<CommandCall> args = new Vector<CommandCall>();
		c = advance_next();
		while( c!=RIGHT ){
			args.add(command());
			c = next();
			if( c==COMMA ){
				c = advance_next();
				if( c==RIGHT ) throw error_message(message.get(Language.LETTER_DIGIT));
			}
		}
		advance_next();
		CommandCall[] s_args = new CommandCall[args.size()];
		for( int i=0; i<s_args.length; i++ ) s_args[i] = args.get(i);
		return s_args;
	}
	
	public CommandCall command() throws Exception{
		Position pos = new Position(this);
		String name = variable();
		if( name.indexOf(STITCH)>=0 ) return new CommandCall( pos, name ); 
		char c = next();
		if(c == LEFT) return new CommandCall(pos, name,values());
		else return new CommandCall(pos, name );
	}

	
	public CommandCall[] params() throws Exception{
		char c = next();
		if(c != LEFT) throw error_message(LEFT);

		Vector<CommandCall> args = new Vector<CommandCall>();
		c = advance_next();
		while( c!=RIGHT ){
			args.add(command());
			c = next();
			if( c==COMMA ){
				c = advance_next();
				if( c==RIGHT ) throw error_message(message.get(Language.LETTER_DIGIT));
			}
		}
		advance_next();
		CommandCall[] s_args = new CommandCall[args.size()];
		for( int i=0; i<s_args.length; i++ ) s_args[i] = args.get(i);
		return s_args;
	}

	public CommandDef command_def() throws Exception{
		Position pos = new Position(this);
		String name = s_name();
		CommandCall[] args = null;
		char c = next();
		if(c == LEFT) args = params();
		else args = new CommandCall[0];		
		c=next();
		if( c!=ASSIGN) throw error_message(ASSIGN);
		advance_next();
		return new CommandDef(pos,name, args, command());
	}
		
	public CommandDef[] apply() throws Exception{
		next();
		Vector<CommandDef> commands = new Vector<CommandDef>();
		while( !eof() )	commands.add( command_def() );
		CommandDef[] cargs = new CommandDef[commands.size()];
		for( int i=0;i<cargs.length; i++ ) cargs[i] = commands.get(i); 
		return cargs;
	}

	public CommandDef[] apply( String program ) throws Exception{
		offset = 0;
		this.program = program;
		return apply();
	}

	public abstract String name() throws Exception;
	public abstract String s_name() throws Exception;
	public abstract String variable() throws Exception;	
}