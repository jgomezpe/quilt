package funpl.lexer;

import java.io.IOException;

import funpl.util.FunConstants;
import nsgl.character.CharacterSequence;
import nsgl.parse.Regex;

public class FunctionLexeme extends Regex{
	public FunctionLexeme(){ this(true); }
	
	public FunctionLexeme(boolean canStartWithNumber ){ 
		super( canStartWithNumber?"[a-z0-9][a-zA-Z0-9_]*":"[a-z][a-zA-Z0-9_]*", FunConstants.FUNCTION); 
	}

	@Override
	protected Object instance(CharacterSequence input, String matched) throws IOException { return matched; }	
}