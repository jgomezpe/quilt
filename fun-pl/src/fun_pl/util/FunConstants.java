package fun_pl.util;

public class FunConstants {
	// FunMachine
	public static final String novalue = "novalue";

	// FunEncoder
	public static final String code="code";
	public static final String arity="arity";
	public static final String priority="priority";
	public static final String extra="extra";
	public static final int EOF=-1;
	public static final int COMMENT=0;
	public static final int DOLLAR=1;
	public static final int ASSIGN=2;
	public static final int COMMA=3;
	public static final int OPEN=4;
	public static final int CLOSE=5;
	public static final int START_LINK_SYMBOLS=6;
	public static final int END_LINK_SYMBOLS=37;
	public static final int UPPER_CASE=38;
	public static final int LOWER_CASE=39;
	public static final int DIGIT=40;
	public static final int SPACE=41;
	public static final int EOL=42;
	public static final int EXTRA=43;

	// FunLexer
	public static final int VARIABLE = 45;
	public static final int VALUE = 128;
	public static final int PRIM_VALUE = 64;
	public static final int PRIM_COMMAND = 46;
	
	// FunParser
	public static final int COMMAND_CALL=50;
	public static final int COMMAND_EXP=51;
	public static final int COMMAND_DEF=52;
	public static final int COMMAND_DEF_LIST=53;
	public static final int COMMAND=54;
	
	public static final String unexpected="unexpected";
	public static final String noargs="noargs";
	public static final String validcommand="validcommand";
	public static final String redefined="redefined";
	public static final String norule="norule";

	// FunMeaner
	public static final String nocommand="nocommand";
	public static final String argmismatch="argmismatch";
	public static final String argnumbermismatch="argnumbermismatch";
	public static final String novar="novar";	
}
