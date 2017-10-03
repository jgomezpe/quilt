package fun_pl.syntax;

import java.io.IOException;

import fun_pl.semantic.FunMachine;
import fun_pl.util.Constants;
import unalcol.io.Position2D;
import unalcol.io.ShortTermMemoryReader;
import unalcol.language.LanguageException;
import unalcol.language.programming.lexer.Lexer;
import unalcol.language.programming.lexer.Token;
import unalcol.language.symbol.Encoder;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.vector.Vector;

public class FunLexer implements Lexer{
	
	protected int offset;
	protected ShortTermMemoryReader reader;
	protected Encoder encoder;
	
	protected FunMachine machine;
	
	public FunLexer( FunMachine machine ){ this.machine = machine; }
	
	public static String get( int[] lexeme ){
		StringBuilder sb = new StringBuilder();
		for(int i:lexeme) sb.append((char)i);
		return sb.toString();
	}

	protected Token check_primitive(Token t) throws LanguageException{
		int[] tlexeme = t.lexeme();
		String lexeme = get(tlexeme);
		try{
			machine.values(lexeme);
			t.setType(Constants.PRIM_VALUE);
			return t;
		}catch(LanguageException e){}
		try{ 
			machine.primitive(lexeme);
			t.setType(Constants.PRIM_COMMAND);			
		}catch(LanguageException e){}
		return t; 
	}
	
	protected int original;
	protected int next(){
		try {
			original = reader.read();
			offset++;
			int c=encoder.apply(original);
			if( c==Constants.COMMENT){
				c = encoder.apply(reader.read());
				offset++;
				while( c!=Constants.COMMENT && c != Constants.EOL && c!=Constants.EOF ){
					c = encoder.apply(reader.read());
					offset++;
				}
				if(c==Constants.COMMENT) return next();
			}	
			return c;
		} catch (IOException e) { return Constants.EOF; }
	}
	
	protected void back(){ if( reader.back() ) offset--; }
	
	protected Token variable() throws LanguageException {
		int off = offset-1;
		Vector<Integer> v = new Vector<Integer>();
		v.add(original);
		int c = next();
		while( c==Constants.UPPER_CASE || c==Constants.LOWER_CASE || c==Constants.DIGIT ){
			v.add(original);
			c=next();
		}
		if( c!=Constants.EOF ) back();
		return new Token(Constants.VARIABLE, new Position2D(off, reader.row(), reader.column()), v);
	}
	
	protected Token value() throws LanguageException {
		int off = offset-1;
		Vector<Integer> v = new Vector<Integer>();
		v.add(original);
		int c = next();
		while(	c==Constants.UPPER_CASE || c==Constants.LOWER_CASE ||
				c==Constants.DIGIT || c==Constants.EXTRA ){
			v.add(original);
			c=next();
		}
		if( c!=Constants.EOF ) back();
		return check_primitive(new Token(Constants.VALUE, new Position2D(off, reader.row(), reader.column()), v));
	}
	
	@Override
	public Array<Token> apply(ShortTermMemoryReader reader, int offset, Encoder encoder) throws LanguageException {
		this.reader = reader;
		this.encoder = encoder;
		this.offset = offset;
		Vector<Token> v = new Vector<Token>();
		int c = next();
		while(c!=Constants.EOF){
			if( Constants.DOLLAR < c && c<Constants.START_LINK_SYMBOLS ){
				v.add(new Token(c, new Position2D(this.offset-1, reader.row(), reader.column()), new int[]{original}));
				c = next();				
			}else
				if( Constants.START_LINK_SYMBOLS <= c && c<=Constants.END_LINK_SYMBOLS ){
					v.add(new Token(c, new Position2D(this.offset-1, reader.row(), reader.column()), new int[]{original}));
					c = next();				
				}else{
					switch(c){
						case Constants.SPACE: // We do not care about spaces
						case Constants.EOL:
							c=next();
						break;
						case Constants.COMMENT: // It is a comment, we do not care until next EOL
							c = next();
							while( c != Constants.EOL && c!=Constants.EOF ){ c = next(); }
							if( c==Constants.EOL ) c=next();
						break;	
						case Constants.UPPER_CASE: // It is a variable
						case Constants.DOLLAR:
							v.add(variable());
							c=next();
						break;	
						default:
							v.add(value());
							c=next();
					}
				}
		}
		return v;
	}	
}