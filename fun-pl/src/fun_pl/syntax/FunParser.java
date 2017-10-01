package fun_pl.syntax;

import unalcol.language.LanguageException;
import unalcol.language.Typed;
import unalcol.language.TypedValue;
import unalcol.language.programming.lexer.RCToken;
import unalcol.language.programming.lexer.Token;
import unalcol.language.programming.parser.Parser;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.vector.Vector;
import unalcol.util.I18N;

public class FunParser implements Parser{
	public static final int COMMAND_CALL=50;
	public static final int COMMAND_EXP=51;
	public static final int COMMAND_DEF=52;
	public static final int COMMAND_DEF_LIST=53;
	public static final int COMMAND=54;
	
	public static final String unexpected="unexpected";
	public static final String noargs="noargs";
	public static final String command="command";
	public static final String norule="norule";
	
	protected int main;
	protected int offset;
	protected Array<Token> tokens;
	
	public FunParser(int main){ this.main = main; }
	
	protected RCToken get(){
		if( offset >= tokens.size() ) return new RCToken(offset,0,0); 
		return (RCToken)tokens.get(offset); 
	}
	
	protected RCToken next(){
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
		if( t.type()==FunLexer.VARIABLE || t.type()==FunLexer.PRIM_VALUE || (t.type()&FunLexer.VALUE)==FunLexer.VALUE ){
			next();
			return t;
		}
		throw le;
	} 
	
	protected Typed command_exp() throws LanguageException {
		Vector<Typed> v = new Vector<Typed>();
		Typed c = command_call();
		v.add(c);
		Token t = get();
		while(t.type()==FunEncoder.LEFT_LINK){
			v.add(t);
			t = next();
			v.add(command_call());
			t=get();
		}
		while(t.type()==FunEncoder.RIGHT_LINK){
			v.add(t);
			t = next();
			v.add(command_call());
			t=get();
		}
		return new TypedValue<Vector<Typed>>(COMMAND_EXP, v);
	} 
	
	protected void args(Vector<Typed> v) throws LanguageException{
		RCToken t = get();
		if( t.type()==FunEncoder.OPEN ){
			next();
			v.add(command_exp());
			while(get().type()==FunEncoder.COMMA){
				next();
				v.add(command_exp());
			}
			t=get();
			if(t.type()==FunEncoder.CLOSE){
				next();
				return;
			}
			throw new LanguageException(unexpected, FunLexer.get(t.lexeme()),t.row(),t.column(),""+(I18N.get(FunEncoder.code)).charAt(FunEncoder.CLOSE));
		}
		throw new LanguageException(noargs,t.row(),t.column());
	}
	
	protected Typed command() throws LanguageException{
		RCToken t = get();
		if( t.type()==FunLexer.PRIM_COMMAND || (t.type()&FunLexer.VALUE)==FunLexer.VALUE ){
			Vector<Typed> v = new Vector<Typed>();
			v.add(t);
			t = next();
			if( t.type()==FunEncoder.OPEN ){
				args(v);
			}
			return new TypedValue<Vector<Typed>>(COMMAND, v);
		}
		throw new LanguageException(unexpected, FunLexer.get(t.lexeme()),t.row(),t.column(),I18N.get(command));
	} 
	
	protected Typed command_def() throws LanguageException {
		Typed f = command(); 
		if( get().type()!=FunEncoder.ASSIGN ) throw new LanguageException(unexpected, FunLexer.get(get().lexeme()), get().row(),get().column(), ""+(I18N.get(FunEncoder.code)).charAt(FunEncoder.ASSIGN));
		next();
		Typed c = command_exp();
		Vector<Typed> v = new Vector<Typed>();
		v.add(f);
		v.add(c);
		return new TypedValue<Vector<Typed>>(COMMAND_DEF, v);
	} 
	
	protected Typed command_def_list() throws LanguageException {		
		Vector<Typed> v = new Vector<Typed>();
		while(get().type()!=Token.EOF) v.add(command_def());
		return new TypedValue<Vector<Typed>>(COMMAND_DEF_LIST, v);
	} 
	
	@Override
	public Typed apply(Array<Token> tokens, int offset) throws LanguageException {
		this.tokens = tokens;
		this.offset=offset;
		switch( main ){
			case COMMAND_CALL: return command_call();
			case COMMAND_EXP: return command_exp();
			case COMMAND_DEF: return command_def();
			case COMMAND_DEF_LIST: return command_def_list();
			case COMMAND: return command();
		}
		throw new LanguageException(norule,0,0);
	}	
}