package fun_pl.syntax;

import java.io.IOException;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunMachine;
import unalcol.io.ShortTermMemoryReader;
import unalcol.language.LanguageException;
import unalcol.language.programming.lexer.Lexer;
import unalcol.language.programming.lexer.RCToken;
import unalcol.language.programming.lexer.Token;
import unalcol.language.symbol.Encoder;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.vector.Vector;

public class FunLexer implements Lexer{
	
	public static final int VARIABLE = 20;
	public static final int VALUE = 64;
	public static final int PRIM_VALUE = 22;
	public static final int PRIM_COMMAND = 23;
	
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

	protected RCToken check_primitive(RCToken t){
		int[] tlexeme = t.lexeme();
		String lexeme = get(tlexeme);
		String[] values = machine.values(lexeme);
		if( values != null ){
			Vector<Integer> nlexeme = new Vector<Integer>();
			int j=values[0].length();
			for( int k=0; k<j; k++ ) nlexeme.add(tlexeme[k]);
			for(int i=0;i<values.length;i++){
				nlexeme.add((int)' ');
				for( int k=0; k<values[i].length(); k++ ){
					nlexeme.add(tlexeme[j]);
					j++;
				}
			}
			return new RCToken(PRIM_VALUE, t.offset(), nlexeme, t.row(), t.column());
		}
		FunCommand c = machine.primitive(lexeme);
		if( c != null ){ t.setType(PRIM_COMMAND); }
		return t; 
	}
	
	protected int original;
	protected int next(){
		try {
			original = reader.read();
			offset++;
			int c=encoder.apply(original);
			if( c==FunEncoder.COMMENT){
				c = encoder.apply(reader.read());
				offset++;
				while( c!=FunEncoder.COMMENT && c != FunEncoder.EOL && c!=FunEncoder.EOF ){
					c = encoder.apply(reader.read());
					offset++;
				}
				if(c==FunEncoder.COMMENT) return next();
			}	
			return c;
		} catch (IOException e) { return FunEncoder.EOF; }
	}
	
	protected void back(){ if( reader.back() ) offset--; }
	
	protected Token variable() throws LanguageException {
		int off = offset-1;
		Vector<Integer> v = new Vector<Integer>();
		v.add(original);
		int c = next();
		while( c==FunEncoder.UPPER_CASE || c==FunEncoder.LOWER_CASE || c==FunEncoder.DIGIT ){
			v.add(original);
			c=next();
		}
		if( c!=FunEncoder.EOF ) back();
		return new RCToken(VARIABLE, off, v, reader.row(), reader.column());
	}
	
	protected RCToken value() throws LanguageException {
		int off = offset-1;
		Vector<Integer> v = new Vector<Integer>();
		v.add(original);
		int c = next();
		while(	c==FunEncoder.UPPER_CASE || c==FunEncoder.LOWER_CASE ||
				c==FunEncoder.DIGIT || c==FunEncoder.EXTRA ){
			v.add(original);
			c=next();
		}
		if( c!=FunEncoder.EOF ) back();
		return check_primitive(new RCToken(VALUE, off, v, reader.row(), reader.column()));
	}
	
	@Override
	public Array<Token> apply(ShortTermMemoryReader reader, int offset, Encoder encoder) throws LanguageException {
		this.reader = reader;
		this.encoder = encoder;
		this.offset = offset;
		Vector<Token> v = new Vector<Token>();
		int c = next();
		while(c!=FunEncoder.EOF){
			if( c!=FunEncoder.COMMENT && c<FunEncoder.SPECIAL ){
				v.add(new RCToken(c, this.offset-1, new int[]{original}, reader.row(), reader.column()));
				c = next();				
			}else{
				switch(c){
					case FunEncoder.SPACE: // We do not care about spaces
					case FunEncoder.EOL:
						c=next();
					break;
					case FunEncoder.COMMENT: // It is a comment, we do not care until next EOL
						c = next();
						while( c != FunEncoder.EOL && c!=FunEncoder.EOF ){ c = next(); }
						if( c==FunEncoder.EOL ) c=next();
					break;	
					case FunEncoder.UPPER_CASE: // It is a variable
					case FunEncoder.DOLLAR:
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