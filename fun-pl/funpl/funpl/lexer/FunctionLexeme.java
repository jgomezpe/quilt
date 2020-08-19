package funpl.lexer;

import java.io.IOException;

import funpl.util.FunConstants;
import nsgl.character.CharacterSequence;
import nsgl.parse.Regex;

public class FunctionLexeme extends Regex{
    protected boolean withNumber;
	public FunctionLexeme(){ this(true); }
	
	public FunctionLexeme(boolean canStartWithNumber ){ 
		super( canStartWithNumber?"[a-z0-9][a-zA-Z0-9_]*":"[a-z][a-zA-Z0-9_]*", FunConstants.FUNCTION); 
		this.withNumber = canStartWithNumber;
	}

	@Override
	public Object instance(CharacterSequence input, String matched) throws IOException {
	    char c = matched.charAt(0);
	    if( !Character.isLowerCase(c) && !(withNumber && Character.isDigit(c)) )
		throw input.exception("·Invalid "+type()+"· ", 0);
	    return matched; 
	}	
}