package fun_pl.syntax;

import fun_pl.util.Constants;
import unalcol.language.LanguageException;
import unalcol.language.symbol.Encoder;
import unalcol.util.I18N;

public class FunEncoder implements Encoder{

	protected String symbols;
	protected String space=" \t\r";
	
	public FunEncoder(){ this.symbols = "%$=,()|#"; }
	public FunEncoder( String symbols ) throws LanguageException{
		this.symbols = symbols;
		if(symbols.length()>Constants.END_LINK_SYMBOLS) 
			throw new LanguageException(Constants.extra, symbols.substring(Constants.START_LINK_SYMBOLS), 
					(Constants.END_LINK_SYMBOLS-Constants.START_LINK_SYMBOLS));
	} 
	
	@Override
	public int apply(int c) {
		if(c==Constants.EOF) return Constants.EOF;
		int index=symbols.indexOf((char)c);
		if( index>=0 ) return index;
		if( Character.isUpperCase(c)) return Constants.UPPER_CASE;
		if( Character.isLowerCase(c)) return Constants.LOWER_CASE;
		if( Character.isDigit(c)) return Constants.DIGIT;
		if( space.indexOf(c)>=0 ) return Constants.SPACE;
		if(c=='\n') return Constants.EOL;
		return Constants.EXTRA;
	}
	
	public char symbol( int index ){
		if( 0<=index && index <symbols.length() ) return symbols.charAt(index);
		return (char)-1;
	}
	
	public int symbols_number(){ return symbols.length(); }
	
	public static char get_symbol(int index){ return I18N.get(Constants.code).charAt(index); }
}