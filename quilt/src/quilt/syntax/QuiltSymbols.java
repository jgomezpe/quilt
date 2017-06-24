package quilt.syntax;

public class QuiltSymbols{
	protected static final char EOF=(char)-1;
	protected static final char COMMENT='%';
	protected static final char UNDER_SCORE='_';
	protected static final char COMMA=',';
	protected static final char STITCH='|';
	protected static final char LEFTSTITCH='#';
	protected static final char EOL='\n';
	protected static final char SPACE=' ';
	protected static final char TAB='\t';
	protected static final char ASSIGN='=';
	protected static final char LEFT='(';
	protected static final char RIGHT=')';
	protected static final char DOLLAR='$';

	public boolean is_space( char c ){ return c==SPACE || c==TAB || c==EOL; }
	public boolean is_eol( char c ){ return c==EOL; }
	public boolean is_name(char c){ return Character.isLetterOrDigit(c) || c==DOLLAR; };
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

	public static char stitch(){ return STITCH; };
	public static char leftstitch(){ return LEFTSTITCH; };
	public static char comment(){ return COMMENT; };
	public static char left(){ return LEFT; }
	public static char right(){ return RIGHT; }
	public static char comma(){ return COMMA; }
	public static char dollar(){ return DOLLAR; }
	public char assign(){ return ASSIGN; }
	public char underscore(){ return UNDER_SCORE; }
	
}