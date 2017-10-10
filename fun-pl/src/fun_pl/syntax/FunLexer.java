package fun_pl.syntax;

import java.io.IOException;

import fun_pl.semantic.FunMachine;
import fun_pl.util.FunConstants;
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
		if( machine.composed(lexeme) != null )	t.setType(FunConstants.PRIM_VALUE);
		else if( machine.is_primitive(lexeme) )	t.setType(FunConstants.PRIM_COMMAND);
		return t; 
	}
	
	protected int original;
	protected int next(){
		try {
			original = reader.read();
			offset++;
			int c=encoder.apply(original);
			if( c==FunConstants.COMMENT){
				c = encoder.apply(reader.read());
				offset++;
				while( c!=FunConstants.COMMENT && c != FunConstants.EOL && c!=FunConstants.EOF ){
					c = encoder.apply(reader.read());
					offset++;
				}
				if(c==FunConstants.COMMENT) return next();
			}	
			return c;
		} catch (IOException e) { return FunConstants.EOF; }
	}
	
	protected void back(){ if( reader.back() ) offset--; }
	
	protected Token variable() throws LanguageException {
		int off = offset-1;
		Vector<Integer> v = new Vector<Integer>();
		v.add(original);
		int c = next();
		while( c==FunConstants.UPPER_CASE || c==FunConstants.LOWER_CASE || c==FunConstants.DIGIT ){
			v.add(original);
			c=next();
		}
		if( c!=FunConstants.EOF ) back();
		return new Token(FunConstants.VARIABLE, new Position2D(off, reader.row(), reader.column()), v);
	}
	
	protected Token value() throws LanguageException {
		int off = offset-1;
		Vector<Integer> v = new Vector<Integer>();
		v.add(original);
		int c = next();
		while(	c==FunConstants.UPPER_CASE || c==FunConstants.LOWER_CASE ||
				c==FunConstants.DIGIT || c==FunConstants.EXTRA ){
			v.add(original);
			c=next();
		}
		if( c!=FunConstants.EOF ) back();
		return check_primitive(new Token(FunConstants.VALUE, new Position2D(off, reader.row(), reader.column()), v));
	}
	
	@Override
	public Array<Token> apply(ShortTermMemoryReader reader, int offset, Encoder encoder) throws LanguageException {
		this.reader = reader;
		this.encoder = encoder;
		this.offset = offset;
		Vector<Token> v = new Vector<Token>();
		int c = next();
		while(c!=FunConstants.EOF){
			if( FunConstants.DOLLAR < c && c<FunConstants.START_LINK_SYMBOLS ){
				v.add(new Token(c, new Position2D(this.offset-1, reader.row(), reader.column()), new int[]{original}));
				c = next();				
			}else
				if( FunConstants.START_LINK_SYMBOLS <= c && c<=FunConstants.END_LINK_SYMBOLS ){
					v.add(new Token(c, new Position2D(this.offset-1, reader.row(), reader.column()), new int[]{original}));
					c = next();				
				}else{
					switch(c){
						case FunConstants.SPACE: // We do not care about spaces
						case FunConstants.EOL:
							c=next();
						break;
						case FunConstants.COMMENT: // It is a comment, we do not care until next EOL
							c = next();
							while( c != FunConstants.EOL && c!=FunConstants.EOF ){ c = next(); }
							if( c==FunConstants.EOL ) c=next();
						break;	
						case FunConstants.UPPER_CASE: // It is a variable
						case FunConstants.DOLLAR:
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