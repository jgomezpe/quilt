package fun_pl.syntax;

import java.util.NoSuchElementException;

import fun_pl.util.FunConstants;
import unalcol.io.Position2D;
import unalcol.language.LanguageException;
import unalcol.language.programming.lexer.Lexer;
import unalcol.language.programming.lexer.Token;
import unalcol.language.symbol.AbstractEncoder;
import unalcol.language.symbol.Encoder;
import unalcol.types.collection.UnalcolIterator;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.vector.Vector;

public class FunLexer implements Lexer{
	
	protected int offset;
	protected UnalcolIterator<Position2D, Integer> reader;
	protected Encoder encoder;
	
	protected FunLexerCheck machine;
	
	public FunLexer( String[] primitives, String[] values ){ this.machine = new SimpleFunLexerCheck(primitives, values); }

	public FunLexer( FunLexerCheck machine ){ this.machine = machine; }
	
	public static String get( int[] lexeme ){
		StringBuilder sb = new StringBuilder();
		for(int i:lexeme) sb.append((char)i);
		return sb.toString();
	}

	protected Token<Position2D> check_primitive(Token<Position2D> t) throws LanguageException{
		int[] tlexeme = t.lexeme();
		String lexeme = get(tlexeme);
		if( machine.composed(lexeme) != null )	t.setType(FunConstants.PRIM_VALUE);
		else if( machine.is_primitive(lexeme) )	t.setType(FunConstants.PRIM_COMMAND);
		return t; 
	}
	
	protected int original;
	protected int next(){
		try {
			original = reader.next();
			offset++;
			int c=encoder.apply(original);
			if( c==FunConstants.COMMENT){
				c = encoder.apply(reader.next());
				offset++;
				while( c!=FunConstants.COMMENT && c != FunConstants.EOL && c!=FunConstants.EOF ){
					c = encoder.apply(reader.next());
					offset++;
				}
				if(c==FunConstants.COMMENT) return next();
			}	
			return c;
		} catch (NoSuchElementException e) { return FunConstants.EOF; }
	}
	
	protected void back(){ if( reader.back() ) offset--; }
	
	protected Token<Position2D> variable() throws LanguageException {
		int off = offset-1;
		Position2D pos = (Position2D)reader.key();
		int row = pos.row();
		int column = pos.column();
		Vector<Integer> v = new Vector<Integer>();
		v.add(original);
		int c = next();
		while( c==FunConstants.UPPER_CASE || c==FunConstants.LOWER_CASE || c==FunConstants.DIGIT ){
			v.add(original);
			c=next();
		}
		if( c!=FunConstants.EOF ) back();
		return new Token<Position2D>(FunConstants.VARIABLE, new Position2D(off, row, column), v);
	}
	
	protected Token<Position2D> value() throws LanguageException {
		int off = offset-1;
		Position2D pos = (Position2D)reader.key();
		int row = pos.row();
		int column = pos.column();
		Vector<Integer> v = new Vector<Integer>();
		v.add(original);
		int c = next();
		while(	c==FunConstants.UPPER_CASE || c==FunConstants.LOWER_CASE ||
				c==FunConstants.DIGIT || c==FunConstants.EXTRA ){
			v.add(original);
			c=next();
		}
		if( c!=FunConstants.EOF ) back();
		return check_primitive(new Token<Position2D>(FunConstants.VALUE, new Position2D(off, row, column), v));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Array<Token<?>> apply(UnalcolIterator<?, Integer> reader, AbstractEncoder<Integer> encoder) throws LanguageException {
	    this.reader = (UnalcolIterator<Position2D,Integer>)reader;
	    this.encoder = (FunEncoder)encoder;
	    this.offset = 0;
		
	    Vector<Token<?>> v = new Vector<Token<?>>();
	    int c = next();
	    while(c!=FunConstants.EOF){
		Position2D pos = (Position2D)reader.key();
		int row = pos.row();
		int column = pos.column();
		if( FunConstants.DOLLAR < c && c<FunConstants.START_LINK_SYMBOLS ){
			v.add(new Token<Position2D>(c, new Position2D(this.offset-1, row, column-1), new int[]{original}));
			c = next();				
		}else
			if( FunConstants.START_LINK_SYMBOLS <= c && c<=FunConstants.END_LINK_SYMBOLS ){
				v.add(new Token<Position2D>(c, new Position2D(this.offset-1, row, column), new int[]{original}));
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
	    				break;    
		    		}
			}
		}
	    return v;
	}	
}