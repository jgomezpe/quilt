package quilt.syntax;

import java.util.Vector;

import quilt.QuiltMachine;
import quilt.Remnant;
import quilt.operation.Command;
import quilt.operation.CommandCall;
import quilt.operation.CommandDef;
import quilt.util.Language;
import unalcol.gui.editor.Position;
import unalcol.gui.editor.Token;
import unalcol.gui.editor.Tokenizer;

/**
*
* QuiltMachineParser
* <P>Abstract parser of the Quilt programming language.
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/syntax/QuiltMachineParser.java" target="_blank">
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
public class QuiltMachineParser extends ParserPos implements Tokenizer{

	protected QuiltMachine machine=null;
	protected QuiltSymbols symbols = null;
	protected String program="";
	protected int MAX_STITCHS=Integer.MAX_VALUE;
	
	public QuiltMachineParser(){
		this(null, Integer.MAX_VALUE, new QuiltSymbols());
	}
	
	public QuiltMachineParser( QuiltMachine machine, int max_stitchs, QuiltSymbols symbols ){
		super();
		setMachine(machine);
		this.symbols = symbols;
		this.MAX_STITCHS = max_stitchs;
	}

	public QuiltMachineParser( QuiltMachine machine, int max_stitchs ){
		this( machine, max_stitchs, new QuiltSymbols() );
	}

	public QuiltMachineParser( QuiltMachine machine ){
		this(machine, Integer.MAX_VALUE, new QuiltSymbols());
	}
	
	public void setMachine( QuiltMachine machine ){
		this.machine = machine;
		if( this.machine != null ){
			
		}
	}
	

	public void init(String program){
		init();
		this.program = program;
		next();
	}
	
	public boolean eof(){ return offset >= program.length(); }
	
	protected char current(){ return eof()?QuiltSymbols.EOF:program.charAt(offset); }

	public char advance(){
		char c = current();
		if( symbols.is_eol(c) ){
			row++;
			column=0;
		}else column++;
		offset++;
		return current();
	}
	
	public char next(){
		char c = current();
		while( symbols.is_space(c) || symbols.is_comment(c) ){
			comment();
			c = advance();
		}
		return c;
	}
	
	public char comment(){
		while( symbols.is_comment(current()) ){
			while(!eof() && !symbols.is_eol(advance())){};
		}
		return current();
	}	

	/**
	 * Determines if the next component  is a name. A name is defined as name :- [name_symbol]+
	 * @return  A string with the name component.
	 * @throws Exception if a name component cannot be obtained from the source  
	 */
	public String name() throws Exception{
		StringBuilder sb = new StringBuilder();
		char c = next();
		while( symbols.is_name(c) ){
			sb.append(c);
			c = advance();
		}
		String txt = sb.toString();
		if(txt.length()>0) return txt;
		throw error_message("Not a valid symbol for a variables name");
	}

	/**
	 * Determines if the next component is a variable. A variable is defined as var :- start_variable_symbol [follows_variable_symbol]*
	 * @return  A string with the variable component.
	 * @throws Exception if a variable component cannot be obtained from the source  
	 */
	public String variable() throws Exception{
		StringBuilder sb = new StringBuilder();
		char c = next();
		if( !symbols.is_starts_variable(c) ){ throw new Exception("Not a letter or "+symbols.dollar()); }
		c = advance();
		while( symbols.is_follows_variable(c) ){
			sb.append(c);
			c = advance();
		}
		return sb.toString();
	}

	public CommandCall prim_remnant() throws Exception{
		String[] remnants = machine.remnants();
		int[] index = new int[remnants.length];
		for( int i=0; i<index.length; i++ ){ index[i]=i; }
		int count = index.length;
		int pos=0;
		char c = next();
		Position p = new Position(this);
		while( count>0 ){
			int i=0; 
			while( i<count ){
				if( pos==remnants[index[i]].length() ){
					advance();
					return new CommandCall(p, remnants[index[i]]);
				}	
				if( remnants[index[i]].charAt(pos) != c ){
					System.arraycopy(index, i+1, index, i, count-i-1);
					count--;
				}else{
					i++;
				}
			}
			if( count>0 ){
				c = advance();
				pos++;
			}
		}
		throw new Exception("No primitive remnant");
	}

	public CommandCall prim_quilt() throws Exception{
		CommandCall r = null;
		try{
			r=prim_remnant();
			while(r!=null){
				CommandCall r1 = prim_remnant();
				r = new CommandCall( r, QuiltMachine.SEW, new CommandCall[]{r, r1});
			}	
		}catch(Exception e){}
		if( r != null )	return r;
		throw new Exception("Invalid prim quilt");
	}
	
	public CommandCall command() throws Exception{
		next();
		ParserPos pos = new ParserPos(this);

		// Trying a function call
		String fmessage = null;
		CommandCall f = null;
		try{
			f = function();
			if( f.arity()>0 ){ return f; }
		}catch(Exception e){ fmessage = e.getMessage(); }
		ParserPos fpos = new ParserPos(this);		
		
		// Trying a primitive quilt (a quilt built using primitive remnant names)
		this.set(pos);
		
		String pqmessage = null;
		CommandCall pq = null;
		try{ pq = prim_quilt(); }catch(Exception e){ pqmessage = e.getMessage(); }

		CommandCall c=f;
		String message =fmessage;
		if( fpos.offset() < offset ){
			c = pq;
			message=pqmessage;
			fpos = new ParserPos(this);
		}
		
		// Trying a variable
		this.set(pos);
		String vmessage=null;
		CommandCall var = null;
		try{ 
			String var_name = variable();
			var = new CommandCall(pos, var_name);
		}catch(Exception e){ vmessage = e.getMessage(); }
		
		if( fpos.offset() < offset ){
			c = var;
			message = vmessage;
			fpos = new ParserPos(this);
		}
		
		if( c!=null ) return c;		
		throw new Exception(message);
	}

	public CommandCall function() throws Exception{
		next();
		Position pos = new Position(this);
		String name = name();
		CommandCall[] args = null;
		char c = next();
		if(symbols.is_left(c)) args = params();
		return new CommandCall(pos, name,args);
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
	
	public CommandCall[] values() throws Exception{
		char c = next();
		if( !symbols.is_left(c)) throw error_message(symbols.left());

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
	

	
	public CommandCall[] params() throws Exception{
		char c = next();
		if(!symbols.is_left(c)) throw error_message(symbols.left());

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

	public CommandCall command_exp() throws Exception{
		CommandCall c = command();		
		while( symbols.is_leftstitch(current()) ){
			advance();
			CommandCall c2 = command();
			c = new CommandCall(new Position(this), ""+symbols.leftstitch(), new CommandCall[]{c,c2});
		}
		while( symbols.is_stitch(current()) ){
			advance();
			CommandCall c2 = command();
			c = new CommandCall(new Position(this), ""+symbols.stitch(), new CommandCall[]{c,c2});
		}	
		return c;
	}
	
	public CommandDef command_def() throws Exception{
		Position pos = new Position(this);
		CommandCall left = function();
		if( !symbols.is_assign(current()) ) throw new Exception("Assign expected");
		char c = advance();
		CommandCall right = command_exp();
	}	
/*		
		String name = name();
		if( machine.is_primitive(name) || machine.composed(name).length > 0 ) throw machine.error(this,machine.message(Language.REDEFINED));
		CommandCall[] args = null;
		char c = next();
		if(symbols.is_left(c)) args = params();
		else args = new CommandCall[0];		
		c=next();
		if( !symbols.is_assign(c)) throw error_message(symbols.assign());
		advance_next();
		return new CommandDef(pos,name, args, command());
*/		
	}
		
	public CommandDef[] apply() throws Exception{
		next();
		Vector<CommandDef> commands = new Vector<CommandDef>();
		while( !eof() ){
			commands.add( command_def() );
			next();
		}
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
	
/*
  	public String variable() throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append(name());
		char c = current();
		int count_stitchs=0;
		while( count_stitchs<MAX_STITCHS && symbols.is_leftstitch(c) ){
			count_stitchs++;
			sb.append(c);
			c = advance();
			sb.append(name());
			c = current();
		}
		count_stitchs=0;
		while( count_stitchs<MAX_STITCHS && symbols.is_stitch(c) ){
			count_stitchs++;
			sb.append(c);
			c = advance();
			sb.append(name());
			c = current();
		}
		return sb.toString();
	}	
*/	
	public static final int UNDEFINED = 0;
	public static final int SPACE = 1;
	public static final int COMMENT = 2;
	public static final int SYMBOL = 3;
	public static final int STITCH = 4;
	public static final int NAME = 5;
	public static final int PRIMITIVE = 6;
	public static final int REMNANT = 7;

	protected int token(char c){
		if(symbols.is_space(c)) return SPACE;
		if(symbols.is_comment(c)) return COMMENT;
		if(symbols.is_special(c)) return SYMBOL;
		if(symbols.is_stitch(c)||symbols.is_leftstitch(c)) return STITCH;
		if(symbols.is_name(c)) return NAME;
		return UNDEFINED;
	}
	
	public Token[] tokens(String code){
		if( code==null || code.length()==0 ) return null;
		Vector<int[]> v = new Vector<int[]>();
		int[] prev=new int[]{token(code.charAt(0)),0,1};
		v.addElement(prev);
		for( int k=1; k<code.length(); k++){
			int t = token(code.charAt(k));
			if( t==prev[0] || (prev[0]==COMMENT && !symbols.is_eol(code.charAt(k))) ) prev[2]++;
			else{
				String name=code.substring(prev[1],prev[2]);
				if(symbols.is_dollar(name.charAt(0)) || machine.is_primitive(name)) prev[0]=PRIMITIVE;
				else if(machine.composed(name).length>0) prev[0]=REMNANT;
				prev = new int[]{t,k,k+1};
				v.add(prev);
			}
		}
		String name=code.substring(prev[1],prev[2]);
		if(symbols.is_dollar(name.charAt(0)) || machine.is_primitive(name)) prev[0]=PRIMITIVE;
		else if(machine.composed(name).length>0) prev[0]=REMNANT;
		Token[] tokens = new Token[v.size()];
		for(int i=0; i<v.size(); i++){
			int[] t = v.get(i);
			tokens[i] = new Token(t[0],t[1],t[2]-t[1]);
		}
		return tokens;
	}
	
	public QuiltSymbols symbols(){ return symbols; }
	
	public int stitchs(){ return MAX_STITCHS; }
}