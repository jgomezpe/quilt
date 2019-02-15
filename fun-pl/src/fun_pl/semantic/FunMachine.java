package fun_pl.semantic;

import fun_pl.syntax.FunLexerCheck;
import fun_pl.util.FunConstants;
import unalcol.iterator.Position2DTrack;
import unalcol.collection.keymap.Immutable;
import unalcol.language.LanguageException;

public abstract class FunMachine implements FunLexerCheck{
	protected Immutable<String, FunSymbolCommand> primitives;
	protected FunSymbolCommand symbol=null;
	protected FunProgram program = new FunProgram(this);

	//public FunMachine(){}
	// public FunMachine(FunProgram program){ setProgram(program);	}
	public FunMachine( Immutable<String, FunSymbolCommand> primitives, String symbol ){
		this.primitives = primitives;
		try{ this.symbol = primitives.get(symbol); }catch(Exception e){}
	}
	
	public FunProgram get(){ return program; }
	public void setProgram( FunProgram program ){
		this.program = program;
		program.machine = this;
	}
	
	public void clear(){ this.program.clear(); }

	public FunSymbolCommand primitive(String command){ try{ return primitives.get(command); }catch(Exception e){ return null; } }

	public abstract void setValues( Immutable<String, ?> values );
	public boolean is_value(String val){ return value(val)!=null; }
	public abstract Object value(String value);
	
	public boolean is_primitive(String command){ return primitives.valid(command); }
	public FunSymbolCommand symbol(){ return symbol; }

	public boolean can_assign( String variable, Object value ){
		if(value instanceof String ){
			String cmd = (String)value;
			return primitive(cmd)!=null || program.defined(cmd);
		}
		return false;
	}
	
//	public Object execute( String command, Object... args ) throws LanguageException{
		
//	}
	
	public Object execute( Position2DTrack pos, String command, Object... args ) throws LanguageException{
		if(is_value(command)){
			if( args.length>0) throw new LanguageException(pos, FunConstants.argnumbermismatch, command);
			return value(command);
		}	
		if( is_primitive(command) ){
			FunCommand c = primitive(command); 
			c.setPos(pos);
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
		program.setPos(pos);
		try{
			return program.execute(command, args);
		}catch(LanguageException e ){
			if(program.defined(command) && !program.constant(command) && args.length==0 ) return command;
			else throw e;
		}	
	}
}