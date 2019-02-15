package fun_pl.syntax;

import java.util.NoSuchElementException;

import fun_pl.util.FunConstants;
import unalcol.collection.Collection;
import unalcol.iterator.Position2DTrack;
import unalcol.language.LanguageException;
import unalcol.language.Lexer;
import unalcol.language.Token;
import unalcol.language.generalized.GeneralizedEncoder;
import unalcol.language.generalized.GeneralizedToken;
import unalcol.iterator.BT;
import unalcol.iterator.Backable;
import unalcol.collection.Vector;

public class FunLexer implements Lexer{
	protected int offset;
	protected BT<Integer> reader;
	protected GeneralizedEncoder<Integer> encoder;
	
	protected FunLexerCheck machine;
	
	public FunLexer( String[] primitives, String[] values ){ this( new SimpleFunLexerCheck(primitives, values) ); }

	public FunLexer( FunLexerCheck machine ){ this.machine = machine; }
	
	public static String get( Vector<Integer> lexeme ){
		StringBuilder sb = new StringBuilder();
		for(int i:lexeme) sb.append((char)i);
		return sb.toString();
	}

	protected Token check_primitive(Token t) throws LanguageException{
		String lexeme = get(t.lexeme());
		if( machine.composed(lexeme) != null )	t.setType(FunConstants.PRIM_VALUE);
		else if( machine.is_primitive(lexeme) )	t.setType(FunConstants.PRIM_COMMAND);
		return t; 
	}
	
	protected int original;
	protected int next(){
		try {
			original = reader.next();
			offset++;
			int c=encoder.encode(original);
			if( c==FunConstants.COMMENT){
				c = encoder.encode(reader.next());
				offset++;
				while( c!=FunConstants.COMMENT && c != FunConstants.EOL && c!=FunConstants.EOF ){
					c = encoder.encode(reader.next());
					offset++;
				}
				if(c==FunConstants.COMMENT) return next();
			}	
			return c;
		} catch (NoSuchElementException e) { return FunConstants.EOF; }
	}
	
	protected void back(){ if( reader.back() ) offset--; }
	
	protected Token variable() throws LanguageException {
		int off = offset-1;
		Position2DTrack pos = (Position2DTrack)reader.pos();
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
		return new Token(FunConstants.VARIABLE, new Position2DTrack(pos.src(),off, row, column), v);
	}
	
	protected Token value() throws LanguageException {
		int off = offset-1;
		Position2DTrack pos = (Position2DTrack)reader.pos();
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
		return check_primitive(new Token(FunConstants.VALUE, new Position2DTrack(pos.src(), off, row, column), v));
	}
	
	@Override
	public Collection<GeneralizedToken<Integer>> process(Backable<Integer> reader) throws LanguageException {
	    this.reader = (BT<Integer>)reader;
	    this.encoder = (FunEncoder)encoder;
	    this.offset = 0;
		
	    Vector<GeneralizedToken<Integer>> v = new Vector<GeneralizedToken<Integer>>();
	    int c = next();
	    while(c!=FunConstants.EOF){
		Position2DTrack pos = (Position2DTrack)this.reader.pos();
		int row = pos.row();
		int column = pos.column();
		if( FunConstants.DOLLAR < c && c<FunConstants.START_LINK_SYMBOLS ){
			v.add(new Token(c, new Position2DTrack(pos.src(), this.offset-1, row, column), new int[]{original}));
			c = next();				
		}else
			if( FunConstants.START_LINK_SYMBOLS <= c && c<=FunConstants.END_LINK_SYMBOLS ){
				v.add(new Token(c, new Position2DTrack(pos.src(),this.offset-1, row, column), new int[]{original}));
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

	@Override
	public GeneralizedEncoder<Integer> encoder(){ return encoder; }

	@Override
	public void setEncoder(GeneralizedEncoder<Integer> encoder) { this.encoder = encoder; }	
}