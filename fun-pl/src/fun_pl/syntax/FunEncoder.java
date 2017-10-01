package fun_pl.syntax;

import unalcol.language.LanguageException;
import unalcol.language.symbol.Encoder;
import unalcol.util.I18N;

public class FunEncoder implements Encoder{
	public static final String code="code";
	public static final String extra="extra";
	public static final int EOF=-1;
	public static final int COMMENT=0;
	public static final int DOLLAR=1;
	public static final int ASSIGN=2;
	public static final int COMMA=3;
	public static final int OPEN=4;
	public static final int CLOSE=5;
	public static final int LEFT_LINK=6;
	public static final int RIGHT_LINK=7;
	public static final int SYMBOL=6;
	public static final int SPECIAL=37;
	public static final int UPPER_CASE=38;
	public static final int LOWER_CASE=39;
	public static final int DIGIT=40;
	public static final int SPACE=41;
	public static final int EOL=42;
	public static final int EXTRA=43;

	protected String symbols;
	protected String space=" \t\r";
	
	public FunEncoder(){ this.symbols = "%$=,()|#"; }
	public FunEncoder( String symbols ) throws LanguageException{
		this.symbols = symbols;
		if(symbols.length()>SPECIAL) throw new LanguageException(extra, symbols.substring(SYMBOL), (SPECIAL-SYMBOL));
	} 
	
	@Override
	public int apply(int c) {
		if(c==EOF) return EOF;
		int index=symbols.indexOf((char)c);
		if( index>=0 ) return index;
		if( Character.isUpperCase(c)) return UPPER_CASE;
		if( Character.isLowerCase(c)) return LOWER_CASE;
		if( Character.isDigit(c)) return DIGIT;
		if( space.indexOf(c)>=0 ) return SPACE;
		if(c=='\n') return EOL;
		return EXTRA;
	}
	
	public char symbol( int index ){
		if( 0<=index && index <symbols.length() ) return symbols.charAt(index);
		return (char)-1;
	}
	
	public static char get_symbol(int index){
		return I18N.get(code).charAt(index);
	}
}