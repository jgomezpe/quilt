package fun_pl.syntax;

import unalcol.language.symbol.Encoder;

public class FunEncoder implements Encoder{
	public static final int EOF=-1;
	public static final int COMMENT=0;
	public static final int ASSIGN=1;
	public static final int RIGHT_LINK=2;
	public static final int LEFT_LINK=3;
	public static final int COMMA=4;
	public static final int OPEN=5;
	public static final int CLOSE=6;
	public static final int UNDER_SCORE=7;
	public static final int DOLLAR=8;
	public static final int SPECIAL=8;
	public static final int UPPER_CASE=9;
	public static final int LOWER_CASE=10;
	public static final int DIGIT=11;
	public static final int SPACE=12;
	public static final int EOL=13;
	public static final int EXTRA=14;

	protected String symbols;
	protected String space=" \t\r";
	
	public FunEncoder(){ this("%=|#,()_$"); }
	public FunEncoder( String symbols ){ this.symbols = symbols; } 
	
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
}