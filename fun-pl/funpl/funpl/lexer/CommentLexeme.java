package funpl.lexer;

import java.io.IOException;

import funpl.util.FunConstants;
import nsgl.character.CharacterSequence;
import nsgl.parse.Regex;

public class CommentLexeme extends Regex{
	public CommentLexeme(){ super("%.*", FunConstants.COMMENT); }
	
	@Override
	protected Object instance(CharacterSequence input, String matched) throws IOException {
	    return matched.substring(1);
	}
}