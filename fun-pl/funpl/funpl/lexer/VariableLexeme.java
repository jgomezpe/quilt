package funpl.lexer;

import java.io.IOException;

import funpl.util.FunConstants;
import nsgl.character.CharacterSequence;
import nsgl.parse.Regex;

public class VariableLexeme extends Regex{
	public VariableLexeme(){ super("[A-Z]\\w*",FunConstants.VARIABLE); }

	@Override
	protected Object instance(CharacterSequence input, String matched) throws IOException {
	    return matched;
	}		
}