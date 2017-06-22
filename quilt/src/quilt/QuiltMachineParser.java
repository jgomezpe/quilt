package quilt;

import java.util.Vector;

import quilt.operation.CommandCall;
import quilt.operation.CommandDef;
import quilt.util.Language;
import quilt.util.Position;

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
public class QuiltMachineParser extends Position{

	protected QuiltMachine machine=null;
	protected QuiltSymbols symbols = null;
	protected String program="";
	protected int offset;
	protected int MAX_STITCHS;
	
	public QuiltMachineParser(){
		this(null, 1, new QuiltSymbols());
	}
	
	public QuiltMachineParser( QuiltMachine machine, int max_stitchs, QuiltSymbols symbols ){
		super(0,0);
		offset = 0;
		this.machine = machine;
		this.symbols = symbols;
		this.MAX_STITCHS = max_stitchs;
	}

	public QuiltMachineParser( QuiltMachine machine, int max_stitchs ){
		this( machine, max_stitchs, new QuiltSymbols() );
	}

	public QuiltMachineParser( QuiltMachine machine ){
		this(machine, 1, new QuiltSymbols());
	}
	
	public void setMachine( QuiltMachine machine ){
		this.machine = machine;
	}
	
	public void init(){
		row=0;
		column=0;
		offset=0;
	}

	public void init(String program){
		init();
		this.program = program;
	}
	
	public boolean eof(){ return offset >= program.length(); }
	
	protected char current(){ return eof()?QuiltSymbols.EOF:program.charAt(offset); }

	public char advance(){
		char c = current();
		if( symbols.is_eol(c) ){
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
		while( symbols.is_space(c) || symbols.is_comment(c) ){
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
		sb.append(machine.message(Language.UNSYMBOL));
		sb.append(" [");
		sb.append(current());
		sb.append(']');
		sb.append(", ");
		sb.append(machine.message(Language.EXPECTING));
		sb.append(' ');
		sb.append(c);
		return machine.error(this,sb.toString());
	}
	
	public char comment(){
		while( symbols.is_comment(current()) ){
			while(!eof() && !symbols.is_eol(advance())){};
		}
		return current();
	}	
	
	public CommandCall[] values() throws Exception{
		char c = next();
		if( !symbols.is_left(c)) throw error_message(QuiltSymbols.left());

		Vector<CommandCall> args = new Vector<CommandCall>();
		c = advance_next();
		while( !symbols.is_right(c) ){
			args.add(command());
			c = next();
			if( symbols.is_comma(c) ){
				c = advance_next();
				if( symbols.is_right(c) ) throw error_message(machine.message(Language.LETTER_DIGIT)); 
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
		if( name.indexOf(QuiltSymbols.stitch())>=0 ) return new CommandCall( pos, name ); 
		char c = next();
		if(symbols.is_left(c)) return new CommandCall(pos, name,values());
		else return new CommandCall(pos, name );
	}

	
	public CommandCall[] params() throws Exception{
		char c = next();
		if(!symbols.is_left(c)) throw error_message(QuiltSymbols.left());

		Vector<CommandCall> args = new Vector<CommandCall>();
		c = advance_next();
		while( !symbols.is_right(c) ){
			args.add(command());
			c = next();
			if( symbols.is_comma(c) ){
				c = advance_next();
				if( symbols.is_right(c) ) throw error_message(machine.message(Language.LETTER_DIGIT));
			}
		}
		advance_next();
		CommandCall[] s_args = new CommandCall[args.size()];
		for( int i=0; i<s_args.length; i++ ) s_args[i] = args.get(i);
		return s_args;
	}

	public CommandDef command_def() throws Exception{
		Position pos = new Position(this);
		String name = name();
		if( machine.is_primitive(name) || machine.composed(name).length > 0 ) throw error_message(machine.message(Language.REDEFINED));
		CommandCall[] args = null;
		char c = next();
		if(symbols.is_left(c)) args = params();
		else args = new CommandCall[0];		
		c=next();
		if( !symbols.is_assign(c)) throw error_message(symbols.assign());
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
	
	public CommandCall command( String call ) throws Exception{
		init(call);
		return command();
	}

	public CommandDef[] apply( String program ) throws Exception{
		init(program);
		return apply();
	}

	public String name() throws Exception{
		StringBuilder sb = new StringBuilder();
		char c = current();
		while( symbols.is_name(c) ){
			sb.append(c);
			c = advance();
		}
		String txt = sb.toString();
		if(txt.length()>0) return txt;
		throw error_message(machine.message(Language.LETTER_DIGIT));
	}

	public String variable() throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append(name());
		char c = current();
		int count_stitchs=0;
		while( count_stitchs<MAX_STITCHS && symbols.is_stitch(c) ){
			count_stitchs++;
			sb.append(c);
			c = advance();
			sb.append(name());
		}
		return sb.toString();
	}	
	
	public static final int UNDEFINED = 0;
	public static final int SPACE = 1;
	public static final int COMMENT = 2;
	public static final int SYMBOL = 3;
	public static final int STITCH = 4;
	public static final int NAME = 5;
	public static final int PRIMITIVE = 6;

	protected int token(char c){
		if(symbols.is_space(c)) return SPACE;
		if(symbols.is_comment(c)) return COMMENT;
		if(symbols.is_special(c)) return SYMBOL;
		if(symbols.is_stitch(c)) return STITCH;
		if(symbols.is_name(c)) return NAME;
		return UNDEFINED;
	}
	
	public Vector<int[]> tokenize(String code){
		if( code==null || code.length()==0 ) return null;
		Vector<int[]> v = new Vector<int[]>();
		int[] prev=new int[]{token(code.charAt(0)),0,1};
		v.addElement(prev);
		for( int k=1; k<code.length(); k++){
			int t = token(code.charAt(k));
			if( t==prev[0] || (prev[0]==COMMENT && !symbols.is_eol(code.charAt(k))) ) prev[2]++;
			else{
				String name=code.substring(prev[1],prev[2]);
				if(name.startsWith(QuiltMachine.PRIMITIVE) || machine.is_primitive(name)||machine.composed(name).length>0) prev[0]=PRIMITIVE;
				prev = new int[]{t,k,k+1};
				v.add(prev);
			}
		}
		String name=code.substring(prev[1],prev[2]);
		if(name.startsWith(QuiltMachine.PRIMITIVE) || machine.is_primitive(name)||machine.composed(name).length>0) prev[0]=PRIMITIVE;
		return v;
	}
}