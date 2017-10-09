package fun_pl.syntax;

import fun_pl.util.Constants;
import unalcol.io.Position2D;
import unalcol.language.LanguageException;
import unalcol.language.Typed;
import unalcol.language.TypedValue;
import unalcol.language.programming.lexer.Token;
import unalcol.language.programming.parser.Parser;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.vector.Vector;
import unalcol.util.I18N;

public class FunParser implements Parser{
	
	protected int main;
	protected int offset;
	protected Array<Token> tokens;
	
	public FunParser(int main){ this.main = main; }
	
	protected Token get(){
		if( offset >= tokens.size() ) return new Token(new Position2D(offset,0,0)); 
		return tokens.get(offset); 
	}
	
	protected Token next(){
		if( get().type() == Token.EOF ) return get();
		offset++;
		return get(); 
	}

	protected Typed command_call() throws LanguageException {
		LanguageException le = null;
		int off = offset;
		try{ return command();	}catch(LanguageException e){ le=e; }
		offset=off;
		Token t = get();
		if( t.type()==Constants.VARIABLE || t.type()==Constants.PRIM_VALUE || (t.type()&Constants.VALUE)==Constants.VALUE ){
			next();
			return t;
		}
		throw le;
	}
	
	public static int[] pos( Token t ){
	    Position2D pos = (Position2D)t.pos();
	    return new int[]{pos.row(),pos.row()};
	}
	
	public static int[] posf( Token t ){
	    Position2D pos = (Position2D)t.pos();
	    return new int[]{pos.row()+1,pos.row()+1};
	}
	
	protected LanguageException exception(Token t, int symbol ){
		int[] pos = posf(t);
		return new LanguageException(t.pos(), Constants.unexpected, FunLexer.get(t.lexeme()),pos[0],pos[1],""+FunEncoder.get_symbol(symbol));
	}
	
	protected LanguageException exception(Token t, String code, String symbol ){
		int[] pos = posf(t);
		return new LanguageException(t.pos(), code, FunLexer.get(t.lexeme()),pos[0],pos[1],symbol);
	}
	
	protected Typed wrap_command_exp() throws LanguageException{
		Token t = get();
		if(t.type()==Constants.OPEN){
			next();
			Typed c = command_exp();
			t = get();
			if(t.type()!=Constants.CLOSE) throw exception(t, Constants.CLOSE);
			next();
			return c;
		}else return command_call();
	}
	
	protected Typed command_exp() throws LanguageException {
		Vector<Typed> v = new Vector<Typed>();
		Typed c = wrap_command_exp();
		v.add(c);
		Token t = get();
		while(Constants.START_LINK_SYMBOLS<=t.type() && t.type()<=Constants.END_LINK_SYMBOLS){
			v.add(t);
			t = next();
			v.add(wrap_command_exp());
			t=get();
		}
		return new TypedValue<Vector<Typed>>(Constants.COMMAND_EXP, v);
	} 
	
	protected void args(Vector<Typed> v) throws LanguageException{
		Token t = get();
		int[] pos = posf(t);
		if( t.type()==Constants.OPEN ){
			next();
			v.add(command_exp());
			while(get().type()==Constants.COMMA){
				next();
				v.add(command_exp());
			}
			t=get();
			if(t.type()==Constants.CLOSE){
				next();
				return;
			}
			throw exception(t,Constants.CLOSE);
		}
		throw new LanguageException(t.pos(), Constants.noargs, pos[0],pos[1]);
	}
	
	protected Typed command() throws LanguageException{
		Token t = get();
		if( t.type()==Constants.PRIM_COMMAND || (t.type()&Constants.VALUE)==Constants.VALUE ){
			Vector<Typed> v = new Vector<Typed>();
			v.add(t);
			t = next();
			if( t.type()==Constants.OPEN ){
				args(v);
			}
			return new TypedValue<Vector<Typed>>(Constants.COMMAND, v);
		}
		throw exception(t, Constants.unexpected, I18N.get(Constants.command));
	} 
	
	protected Typed command_def() throws LanguageException {
		Typed f = command(); 
		Token t = get();
		if( t.type()!=Constants.ASSIGN ) throw exception(t,Constants.ASSIGN);
		next();
		Typed c = command_exp();
		Vector<Typed> v = new Vector<Typed>();
		v.add(f);
		v.add(c);
		return new TypedValue<Vector<Typed>>(Constants.COMMAND_DEF, v);
	} 
	
	protected Typed command_def_list() throws LanguageException {		
		Vector<Typed> v = new Vector<Typed>();
		while(get().type()!=Token.EOF) v.add(command_def());
		return new TypedValue<Vector<Typed>>(Constants.COMMAND_DEF_LIST, v);
	} 
	
	@Override
	public Typed apply(Array<Token> tokens, int offset) throws LanguageException {
		this.tokens = tokens;
		this.offset=offset;
		switch( main ){
			case Constants.COMMAND_CALL: return command_call();
			case Constants.COMMAND_EXP: return command_exp();
			case Constants.COMMAND_DEF: return command_def();
			case Constants.COMMAND_DEF_LIST: return command_def_list();
			case Constants.COMMAND: return command();
		}
		throw new LanguageException(new Position2D(), Constants.norule, 1, 1);
	}	
}