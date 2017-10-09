package fun_pl.semantic;

import unalcol.io.Position;
import unalcol.language.LanguageException;
import unalcol.types.collection.Collection;
import unalcol.types.collection.array.Array;

public abstract class FunMachine{

	protected FunProgram program = new FunProgram(this);

	public FunMachine(){}
	public FunMachine(FunProgram program){
		this.program = program;
		program.machine = this;
	}
	public FunProgram get(){ return program; }
	public void setProgram( FunProgram program ){ this.program = program; }

	public abstract FunCommand primitive(String command) throws Exception;
	public abstract Array<String> values(String value) throws Exception;

	public abstract Collection<String> primitives();
	public abstract Collection<String> values();
	
	public abstract FunSymbolCommand symbol_command();
	public abstract FunSymbolCommand symbol_command(String symbol);

	public abstract Object value(String value)  throws Exception;
	public abstract boolean can_assign( String variable, Object value );
	
//	public Object execute( String command, Object... args ) throws LanguageException{
		
//	}
	
	public Object execute( Position pos, String command, Object... args ) throws LanguageException{
		if(args.length==0) try{ return value(command); }catch(Exception e){}
		try{ return primitive(command).execute(args); }catch(Exception e ){} 
		return program.execute(pos, command, args);
	}
}