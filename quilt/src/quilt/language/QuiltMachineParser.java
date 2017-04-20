package quilt.language;

import java.util.Vector;

import quilt.Language;

public class QuiltMachineParser{
	protected static final char EOF=(char)-1;
	
	protected int offset;
	protected String program="";
	protected int row;
	protected int column;
	protected Language message;
	
	public QuiltMachineParser(){
		this( new Language() );
	}
	
	public QuiltMachineParser( Language message ){
		this.message = message;
		offset = 0;
	}
	
	public QuiltMachineParser( Language message, String program ){
		this( message );
		this.program = program;
	}
	
	public void init( String language ){
		message.init(language);
	}
	
	public boolean eof(){ return offset >= program.length(); }
	
	protected char current(){ return eof()?EOF:program.charAt(offset); }

	public char advance(){
		char c = current();
		if( c=='\n' ){
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
		while( c==' ' || c=='\n' || c=='\t' || c=='%'){
			comment();
			c = advance();
		};
		return c;
	}
	
	public char advance_next(){
		advance();
		return next();
	}
	
	public Exception error_message( String c ){
		StringBuilder sb = new StringBuilder();
		sb.append(message.get(Language.ERROR));
		sb.append(" ");
		sb.append(message.get(Language.AT));
		sb.append(" ");
		sb.append(message.get(Language.ROW));
		sb.append(" ");
		sb.append(""+(row+1));
		sb.append(" ");
		sb.append(message.get(Language.COLUMN));
		sb.append(" ");
		sb.append(""+(column+1));
		sb.append(": ");
		sb.append(message.get(Language.UNSYMBOL));
		sb.append(" [");
		sb.append(current());
		sb.append("]");
		sb.append(", ");
		sb.append(message.get(Language.EXPECTING));
		sb.append(" ");
		sb.append(c);
		return new Exception(sb.toString());
	}
	
	public char comment(){
		while( current()=='%' ){
			while(!eof() && advance()!='\n'){};
		}
		return current();
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
		if( c=='.' ){
			sb.append(c);
			c = advance();
			sb.append(name());
		}
		return sb.toString();
	}
	
	public String[] params() throws Exception{
		char c = next();
		if(c != '(') throw error_message("(");

		Vector<String> args = new Vector<String>();
		c = advance_next();
		while( c!=')' ){
			args.add(variable());
			c = next();
			if( c==',' ){
				c = advance_next();
				if( c==')' ) throw error_message(message.get(Language.LETTER_DIGIT));
			}
		}
		advance_next();
		String[] s_args = new String[args.size()];
		for( int i=0; i<s_args.length; i++ ) s_args[i] = args.get(i);
		return s_args;
	}

	public CommandCall[] values() throws Exception{
		char c = next();
		if(c != '(') throw error_message("(");

		Vector<CommandCall> args = new Vector<CommandCall>();
		c = advance_next();
		while( c!=')' ){
			args.add(command());
			c = next();
			if( c==',' ){
				c = advance_next();
				if( c==')' ) throw error_message(message.get(Language.LETTER_DIGIT));
			}
		}
		advance_next();
		CommandCall[] s_args = new CommandCall[args.size()];
		for( int i=0; i<s_args.length; i++ ) s_args[i] = args.get(i);
		return s_args;
	}
	
	public CommandCall command() throws Exception{
		String name = name();
		char c = next();
		if(c == '(') return new CommandCall(name,values());
		else return new CommandCall( name );
	}
	
	public CommandDef command_def() throws Exception{
		String name = s_name();
		String[] args = null;
		char c = next();
		if(c == '(') args = params();
		else args = new String[0];		
		c=next();
		if( c!='=') throw error_message("=");
		advance_next();
		return new CommandDef(name, args, command());
	}
	
	public CommandDef[] apply( String program ) throws Exception{
		offset = 0;
		this.program = program;
		return apply();
	}	
	
	public CommandDef[] apply() throws Exception{
		next();
		Vector<CommandDef> commands = new Vector<CommandDef>();
		while( !eof() )	commands.add( command_def() );
		CommandDef[] cargs = new CommandDef[commands.size()];
		for( int i=0;i<cargs.length; i++ ) cargs[i] = commands.get(i); 
		return cargs;
	}
	
	public CommandDef[] apply( String[] remnants, CommandDef[] commands, String program ){
		return null;
	}
	
	public static void main( String[] args ){
		String program = "function(X,Y.Z),=rot(sew(X,Y))";
		QuiltMachineParser parser = new QuiltMachineParser();
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
