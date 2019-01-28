package fun_pl.syntax;

import fun_pl.util.FunConstants;
import unalcol.i18n.I18N;
import unalcol.types.collection.iterator.Position2DTrack;
import unalcol.language.LanguageException;
import unalcol.language.Encoder;

public class FunEncoder implements Encoder{

	protected String symbols;
	protected String space=" \t\r";
	
	public FunEncoder( String symbols ) throws LanguageException{
		this.symbols = symbols;
		if(symbols.length()>FunConstants.END_LINK_SYMBOLS) 
			throw new LanguageException(new Position2DTrack(0,0,0,0), FunConstants.extra, symbols.substring(FunConstants.START_LINK_SYMBOLS), 
					(FunConstants.END_LINK_SYMBOLS-FunConstants.START_LINK_SYMBOLS));
	} 
	
	@Override
	public int encode(Integer c) { return encode((int)c); }

	public int encode(int c) {
		if(c==FunConstants.EOF) return FunConstants.EOF;
		int index=symbols.indexOf((char)c);
		if( index>=0 ) return index;
		if( Character.isUpperCase(c)) return FunConstants.UPPER_CASE;
		if( Character.isLowerCase(c)) return FunConstants.LOWER_CASE;
		if( Character.isDigit(c)) return FunConstants.DIGIT;
		if( space.indexOf(c)>=0 ) return FunConstants.SPACE;
		if(c=='\n') return FunConstants.EOL;
		return FunConstants.EXTRA;
	}
	
	public char symbol( int index ){
		if( 0<=index && index <symbols.length() ) return symbols.charAt(index);
		return (char)-1;
	}
	
	public int symbols_number(){ return symbols.length(); }
	
	public static char get_symbol(int index){ return I18N.get(FunConstants.code).charAt(index); }

	@Override
	public Integer decode(int code) { return null; }
}