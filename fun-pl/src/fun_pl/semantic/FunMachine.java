package fun_pl.semantic;

import fun_pl.util.FunConstants;
import unalcol.io.Position;
import unalcol.language.LanguageException;
import unalcol.types.collection.Collection;
import unalcol.types.collection.array.Array;

public abstract class FunMachine{

	protected FunProgram program = new FunProgram(this);

	public FunMachine(){}
	public FunMachine(FunProgram program){ setProgram(program);	}
	
	public FunProgram get(){ return program; }
	public void setProgram( FunProgram program ){
		this.program = program;
		program.machine = this;
	}
	
	public void clear(){ this.program.clear(); }

	public boolean is_primitive(String command){ 
		for( String s:primitives() ) if( s.equals(command) ) return true;
		return false;
	}
	
	public abstract Collection<String> primitives();
	public abstract FunCommand primitive(String command);

	public boolean is_value(String val){ return value(val)!=null; }
	public abstract Object value(String value);
	public abstract Collection<String> values();
	public abstract Array<String> composed(String value);
	
	public boolean is_symbol_command(String command){ return symbol_command(command)!=null; }
	public abstract FunSymbolCommand symbol_command();
	public abstract FunSymbolCommand symbol_command(String symbol);

	public boolean can_assign( String variable, Object value ){
		if(value instanceof String ){
			String cmd = (String)value;
			return primitive(cmd)!=null || program.defined(cmd);
		}
		return false;
	}
	
//	public Object execute( String command, Object... args ) throws LanguageException{
		
//	}
	
	public Object execute( Position pos, String command, Object... args ) throws LanguageException{
		if(is_value(command)){
			if( args.length>0) throw new LanguageException(pos, FunConstants.argnumbermismatch, command);
			return value(command);
		}	
		if( is_primitive(command) ){
			FunCommand c = primitive(command); 
			c.init(pos);
			if( args.length != c.arity() ){
				if( args.length > 0 ) throw new LanguageException(pos, FunConstants.argnumbermismatch, command);
				else return command;
			}
			try{ return c.execute(args); }
			catch(LanguageException e){
				e.setPosition(pos);
				throw e;
			} 
		}
		program.init(pos);
		try{
			return program.execute(command, args);
		}catch(LanguageException e ){
			if(program.defined(command) && args.length==0 ) return command;
			else throw e;
		}	
	}
}