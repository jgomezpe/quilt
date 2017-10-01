package fun_pl.semantic;

import unalcol.language.LanguageException;

public abstract class FunMachine{
	public static final String novalue = "novalue";
	public static final String nocommand = "nocommand";

	protected FunProgram program = new FunProgram(this);

	public FunMachine(){}
	public FunMachine(FunProgram program){
		this.program = program;
		program.machine = this;
	}
	public FunProgram get(){ return program; }
	public void setProgram( FunProgram program ){ this.program = program; }

	public abstract FunCommand primitive(String command) throws LanguageException;
	public abstract String[] values(String value) throws LanguageException;
	
	public abstract FunSymbolCommand symbol_command();
	public abstract FunSymbolCommand symbol_command(String symbol);

	public abstract Object value(String value)  throws Exception;
	public abstract boolean can_assign( String variable, Object value );
	
	public Object execute( String command, Object... args ) throws Exception{
		if(args.length==0) try{ return value(command); }catch(Exception e){}
		try{ return primitive(command).execute(args); }catch(Exception e ){} 
		return program.execute(command, args);
	}
}