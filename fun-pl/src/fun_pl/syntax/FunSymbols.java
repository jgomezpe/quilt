package fun_pl.syntax;

public class FunSymbols {
	public static final String ALL = "all";
	public static final String LETTER = "letter";
	public static final String DIGIT = "digit";
	public static final String US = "_";
	
	public static final int USES_ALL = 8;
	public static final int USES_LETTER = 4;
	public static final int USES_DIGIT = 2;
	public static final int USES_US = 1;
	
	protected int var;
	protected int name;
	protected String symbols;
	
	protected static final char EOF=(char)-1;
	protected static final char CR='\r';  // Just for windows compatibility
	protected static final char EOL='\n';
	protected static final char SPACE=' ';
	protected static final char TAB='\t';
	protected static final char UNDER_SCORE='_';

	protected char COMMENT='%';
	protected char COMMA=',';
	protected char STITCH='|';
	protected char LEFTSTITCH='#';
	protected char ASSIGN='=';
	protected char LEFT='(';
	protected char RIGHT=')';
	protected char DOLLAR='$';
	
	protected int get(String code){
		String[] txt = code.split(",");
		int v = 0;
		for( String s:txt ){
			if( s.equals(ALL) ) v |= USES_ALL;
			else if( s.equals(LETTER) ) v |= USES_LETTER;
			else if( s.equals(DIGIT) ) v |= USES_DIGIT;
			else if( s.equals(US) ) v |= USES_US;
		}
		return v;
	}
	
	public FunSymbols(){ this("%=|#,()", "letter,_,digit", "all"); }
	
	public FunSymbols( String var, String name ){ this("%=|#,()",var,name); }

	public FunSymbols( String symbols, String var, String name ){
		this.symbols = symbols;
		COMMENT=symbols.charAt(0);
		ASSIGN=symbols.charAt(1);
		STITCH=symbols.charAt(2);
		LEFTSTITCH=symbols.charAt(3);
		COMMA=symbols.charAt(4);
		LEFT=symbols.charAt(5);
		RIGHT=symbols.charAt(6);
		
		this.var = get(var);
		this.name = get(name);
	}
	
	public boolean is_symbol( char c ){ return symbols.indexOf(c)>=0; }
	
	public boolean is_space( char c ){ return c==SPACE || c==TAB || c==CR || c==EOL; }
	public boolean is_eol( char c ){ return c==EOL; }

	protected boolean check(char c, int code){
		return !is_symbol(c) && !is_space(c) &&
					(   (code & USES_ALL) == USES_ALL ||
					  ( (code & USES_LETTER) == USES_LETTER && Character.isLetter(c) ) ||
					  ( (code & USES_DIGIT) == USES_DIGIT && Character.isDigit(c) ) ||
					  ( (code & USES_US) == USES_US  && c==UNDER_SCORE )
					);
	}
		
	public boolean is_name(char c){ return check(c, name); }
	
	public boolean is_follows_variable(char c){ return check(c, var); }
	public boolean is_starts_variable(char c){ return c==DOLLAR || Character.isLetter(c); }
	
	public boolean is_special(char c){ return c==COMMA || c==ASSIGN || c==LEFT || c==RIGHT; };
	public boolean is_stitch(char c){ return c==STITCH; };
	public boolean is_leftstitch(char c){ return c==LEFTSTITCH; };
	public boolean is_comment(char c){ return c==COMMENT; };
	public boolean is_left(char c){ return c==LEFT; }
	public boolean is_right(char c){ return c==RIGHT; }
	public boolean is_dollar(char c){ return c==DOLLAR; }
	public boolean is_comma(char c){ return c==COMMA; }
	public boolean is_assign(char c){ return c==ASSIGN; }
	public boolean is_underscore(char c){ return c==UNDER_SCORE; }
	public boolean is_eof(char c){ return c==EOF; }

	public char stitch(){ return STITCH; };
	public char leftstitch(){ return LEFTSTITCH; };
	public char comment(){ return COMMENT; };
	public char left(){ return LEFT; }
	public char right(){ return RIGHT; }
	public char comma(){ return COMMA; }
	public char dollar(){ return DOLLAR; }
	public char assign(){ return ASSIGN; }
	public char underscore(){ return UNDER_SCORE; }
	
/*	
	public static void main( String[] args){
		QuiltSymbols qs = new QuiltSymbols("letter,digit", "letter,_");
		System.out.println( qs.is_follows_variable('4'));
		System.out.println( qs.is_name('4'));
		System.out.println( qs.is_follows_variable('_'));
		System.out.println( qs.is_name('_'));
		System.out.println( qs.is_follows_variable('s'));
		System.out.println( qs.is_name('s'));
		System.out.println( qs.is_follows_variable('%'));
		System.out.println( qs.is_name('%'));
	}
*/	

}
