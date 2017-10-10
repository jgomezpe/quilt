package fun_pl.syntax;

import fun_pl.util.FunConstants;
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

	public static int[] pos( Token t ){
	    Position2D pos = (Position2D)t.pos();
	    return new int[]{pos.row(),pos.row()};
	}
	
	public static int[] posf( Token t ){
	    Position2D pos = (Position2D)t.pos();
	    return new int[]{pos.row()+1,pos.column()};
	}
	
	protected LanguageException exception(Token t, int symbol ){
		int[] pos = posf(t);
		return new LanguageException(t.pos(), FunConstants.unexpected, FunLexer.get(t.lexeme()),pos[0],pos[1],""+FunEncoder.get_symbol(symbol));
	}
	
	protected LanguageException exception(Token t, String code, String symbol ){
		int[] pos = posf(t);
		return new LanguageException(t.pos(), code, FunLexer.get(t.lexeme()),pos[0],pos[1],symbol);
	}
	
	protected Typed command_call() throws LanguageException {
		LanguageException le = null;
		int off = offset;
		try{ return extended_command(true);	}catch(LanguageException e){ le=e; }
		offset=off;
		Token t = get();
		if( t.type()==FunConstants.VARIABLE || t.type()==FunConstants.PRIM_VALUE || (t.type()&FunConstants.VALUE)==FunConstants.VALUE ){
			next();
			return t;
		}
		throw le;
	}
	
	protected Typed wrap_command_exp() throws LanguageException{
		Token t = get();
		if(t.type()==FunConstants.OPEN){
			next();
			Typed c = command_exp();
			t = get();
			if(t.type()!=FunConstants.CLOSE) throw exception(t, FunConstants.CLOSE);
			next();
			return c;
		}else return command_call();
	}
	
	protected Typed command_exp() throws LanguageException {
		Vector<Typed> v = new Vector<Typed>();
		Typed c = wrap_command_exp();
		v.add(c);
		Token t = get();
		while(FunConstants.START_LINK_SYMBOLS<=t.type() && t.type()<=FunConstants.END_LINK_SYMBOLS){
			v.add(t);
			t = next();
			v.add(wrap_command_exp());
			t=get();
		}
		return new TypedValue<Vector<Typed>>(FunConstants.COMMAND_EXP, v);
	} 
	
	protected void args(Vector<Typed> v) throws LanguageException{
		Token t = get();
		int[] pos = posf(t);
		if( t.type()==FunConstants.OPEN ){
			next();
			v.add(command_exp());
			while(get().type()==FunConstants.COMMA){
				next();
				v.add(command_exp());
			}
			t=get();
			if(t.type()==FunConstants.CLOSE){
				next();
				return;
			}
			throw exception(t,FunConstants.CLOSE);
		}
		throw new LanguageException(t.pos(), FunConstants.noargs, pos[0],pos[1]);
	}
	
	protected Typed command() throws LanguageException{
		return extended_command(false);
	} 
	
	protected Typed extended_command(boolean call ) throws LanguageException{
		Token t = get();
		if(  (t.type()&FunConstants.VALUE)==FunConstants.VALUE  || 
			 call && (t.type()==FunConstants.PRIM_COMMAND || (FunConstants.START_LINK_SYMBOLS<=t.type()&&t.type()<=FunConstants.END_LINK_SYMBOLS)) ){
			Vector<Typed> v = new Vector<Typed>();
			v.add(t);
			t = next();
			if( t.type()==FunConstants.OPEN ){
				args(v);
			}
			return new TypedValue<Vector<Typed>>(FunConstants.COMMAND, v);
		}
		if( t.type()==FunConstants.PRIM_COMMAND || (FunConstants.START_LINK_SYMBOLS<=t.type()&&t.type()<=FunConstants.END_LINK_SYMBOLS) )
			throw exception(t, FunConstants.redefined, " ");
		else throw exception(t, FunConstants.unexpected, I18N.get(FunConstants.validcommand));
	} 
	
	protected Typed command_def() throws LanguageException {
		Typed f = command(); 
		Token t = get();
		if( t.type()!=FunConstants.ASSIGN ) throw exception(t,FunConstants.ASSIGN);
		next();
		Typed c = command_exp();
		Vector<Typed> v = new Vector<Typed>();
		v.add(f);
		v.add(c);
		return new TypedValue<Vector<Typed>>(FunConstants.COMMAND_DEF, v);
	} 
	
	protected Typed command_def_list() throws LanguageException {		
		Vector<Typed> v = new Vector<Typed>();
		while(get().type()!=Token.EOF) v.add(command_def());
		return new TypedValue<Vector<Typed>>(FunConstants.COMMAND_DEF_LIST, v);
	} 
	
	@Override
	public Typed apply(Array<Token> tokens, int offset) throws LanguageException {
		this.tokens = tokens;
		this.offset=offset;
		switch( main ){
			case FunConstants.COMMAND_CALL: return command_call();
			case FunConstants.COMMAND_EXP: return command_exp();
			case FunConstants.COMMAND_DEF: return command_def();
			case FunConstants.COMMAND_DEF_LIST: return command_def_list();
			case FunConstants.COMMAND: return command();
		}
		throw new LanguageException(new Position2D(), FunConstants.norule, 1, 1);
	}	
}