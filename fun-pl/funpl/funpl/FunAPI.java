package funpl;

import java.io.IOException;

import funpl.gui.GUIFunConstants;
import funpl.lexer.FunLexer;
import funpl.semantic.FunAssignment;
import funpl.semantic.FunCommand;
import funpl.semantic.FunCommandCall;
import funpl.semantic.FunMachine;
import funpl.semantic.FunProgram;
import funpl.semantic.FunValueInterpreter;
import funpl.syntax.FunParser;
import funpl.util.FunConstants;
import nsgl.character.CharacterSequence;
import nsgl.generic.Configurable;
import nsgl.generic.hashmap.*;
import nsgl.generic.keymap.KeyMap;
import nsgl.json.JSON;
import nsgl.parse.Regex;

public class FunAPI implements Configurable{
	protected FunMachine machine;
	protected FunLanguage lang;
	protected KeyMap<String,FunCommand> primitive = new HashMap<String, FunCommand>();
	protected KeyMap<String,int[]> operator = new HashMap<String, int[]>();
	protected FunValueInterpreter value;
	protected FunAssignment assignment=null;
	protected boolean canStartWithNumber=true;
	protected String filetype = ".fmp";
	protected String conftype = ".fmc";
	protected Object output = null;

	public FunAPI() { machine = new FunMachine(); }
	
	public void clear() {
	    primitive = new HashMap<String, FunCommand>();
	    operator = new HashMap<String, int[]>();
	    value = null;
	    assignment = null;
	}
	
	public void config(JSON json) {
	    this.clear();
	    filetype = json.string(GUIFunConstants.FMP);
	    conftype = json.string(GUIFunConstants.FMC);
	    if( json.get(FunConstants.NUMBERID) != null )
		canStartWithNumber = json.bool(FunConstants.NUMBERID);
	}
	
	public String type() { return filetype; }
	
	public String cfg() { return conftype; }
	
	public void setAssignment( FunAssignment assignment ) {this.assignment = assignment; }
		
	public void addOperator( FunCommand command, int priority ){
	    primitive.set(command.name(), command);
	    operator.set(command.name(), new int[] {command.arity(), priority});
	}
	
	public void setValue( String regex, FunValueInterpreter value ){
		this.value = value; 
	}
	
	public void setNames( boolean canStartWithNumber ) { this.canStartWithNumber = canStartWithNumber; }
	
	public String values() { return value.description(); }
	
	public String operators(char separator) {
	    	StringBuilder sb = new StringBuilder();
	    	String pipe = "";
	    	String sep = ""+separator;
	    	for( String k:operator.keys()) {
	    	    char c = k.charAt(0);
	    	    sb.append(pipe);
	    	    pipe = sep;
	    	    if(Regex.escapechar(c) && separator=='|') sb.append("\\");
	    	    sb.append(c);
	    	}
	    	return sb.toString(); 	    
	}
	
	public String opers_explain(char separator) {
	    	StringBuilder sb = new StringBuilder();
	    	String pipe = "";
	    	String sep = ""+separator;
	    	for( String k:primitive.keys()) {
	    	    sb.append(pipe);
	    	    pipe = sep;
	    	    sb.append(primitive.get(k).toString());
	    	}
	    	return sb.toString(); 	    
	}
	
	public FunLexer lexer() {
	    	return new FunLexer(canStartWithNumber, value.regex(), operators('|')); 
	}
	
	protected void init() {
	    	FunLexer lexer = lexer();
		
	    	FunParser parser = new FunParser(operator);
	    	
	    	machine= new FunMachine(primitive, value, assignment);
	    	
		lang = new FunLanguage(lexer,parser,machine);
	}

	public void compile( CharacterSequence program ) throws IOException{
	    	init();
	    	machine.addSrc(program);
		FunProgram prog = (FunProgram)lang.process( program, true );
		machine.setProgram(prog);
	}
	
	public Object run( CharacterSequence command ) throws Exception{
		if( lang==null ) init();
	    	machine.addSrc(command);
		FunCommandCall cmd=null;
		cmd = (FunCommandCall)lang.process( command, false);
		if( cmd != null ) {
		    output = cmd.execute( new HashMap<String, Object>() );
		    return output;
		}
		return null;
	}	

	public Object apply( CharacterSequence command ) throws Exception{
		if( lang==null ) init();
	    	machine.addSrc(command);
		FunCommandCall cmd=null;
		cmd = (FunCommandCall)lang.process( command, false);
		if( cmd != null ) {
		    output = cmd.execute( output );
		    return output;
		}
		return null;
	}	
}