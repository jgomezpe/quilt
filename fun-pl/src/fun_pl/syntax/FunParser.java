package fun_pl.syntax;

import fun_pl.basic.BasicFunLexer;
import unalcol.io.CharReader;
import unalcol.io.ShortTermMemoryReader;
import unalcol.language.LanguageException;
import unalcol.language.Typed;
import unalcol.language.TypedValue;
import unalcol.language.programming.lexer.RCToken;
import unalcol.language.programming.lexer.Token;
import unalcol.language.programming.parser.Parser;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.vector.Vector;
import unalcol.util.I18N;

public class FunParser implements Parser{
	public static final int COMMAND_CALL=100;
	public static final int COMMAND_EXP=105;
	public static final int COMMAND_DEF=110;
	public static final int COMMAND_DEF_LIST=120;
	public static final int COMMAND=130;
	
	public static final String unexpected="unexpected";
	public static final String noargs="noargs";
	public static final String nocommand="nocommand";
	public static final String norule="norule";
	
	protected int main;
	protected int offset;
	protected Array<Token> tokens;
	
	public FunParser(int main){ this.main = main; }
	
	protected Token get(){
		if( offset >= tokens.size() ) return new Token(offset); 
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
		Token t = get();
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
		}
		throw new LanguageException(noargs,((RCToken)t).row(),((RCToken)t).column());
	}
	
	protected Typed command() throws LanguageException{
		Token t = get();
		if( t.type()==FunLexer.PRIM_COMMAND || (t.type()&FunLexer.VALUE)==FunLexer.VALUE ){
			Vector<Typed> v = new Vector<Typed>();
			v.add(t);
			t = next();
			if( t.type()==FunEncoder.OPEN ){
				args(v);
			}
			return new TypedValue<Vector<Typed>>(COMMAND, v);
		}
		throw new LanguageException(nocommand,((RCToken)t).row(),((RCToken)t).column());
	} 
	
	protected Typed command_def() throws LanguageException {
		Typed f = command(); 
		if( get().type()!=FunEncoder.ASSIGN ) throw new LanguageException(unexpected, (char)get().lexeme()[0], ((RCToken)get()).row(),((RCToken)get()).column(), '=');
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
	
	public static void i18n(){
		HTKeyMap<String, String> x = new HTKeyMap<String,String>();
		x.add(nocommand, "Not valid definition of a command at row %d, column %d");
		x.add(noargs, "Not valid definition of arguments at row %d, column %d");
		x.add(unexpected, "Unexpected symbol %c at row %d, column %d. Expecting %c");
		x.add(norule, "Undefined fun language component %s.");
		I18N.add("english",x);
		I18N.use("english");
	}
	
	public static void main(String[] args){
		i18n();
		String code="+34.22(X1#$2|$3)=rot(horzdiag)diagdemo(Y#$)\n%commentX#1\r\n=Ave|cesar";
		FunEncoder encoder = new FunEncoder();
		ShortTermMemoryReader reader = new CharReader(code);
		String[] prim_func = new String[]{"rot","sew"};
		String[] prim_value = new String[]{"horz","diag"};
		FunLexer lexer = new BasicFunLexer(prim_func,prim_value);
		try {
			Array<Token> tokens = lexer.apply(reader, 0, encoder);
			reader.close();
			for( Token t:tokens ){
				System.out.println(t.type()+","+t.offset()+","+t.length());
			}
			Parser p = new FunParser(FunParser.COMMAND_DEF_LIST);
			Typed t = p.apply(tokens, 0);
			System.out.println(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}