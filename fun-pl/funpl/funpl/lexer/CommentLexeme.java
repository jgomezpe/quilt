package funpl.lexer;

import java.io.IOException;

import funpl.util.FunConstants;
import nsgl.character.CharacterSequence;
import nsgl.parse.Regex;

public class CommentLexeme extends Regex{
	public CommentLexeme(){ super("%.*", FunConstants.COMMENT); }
	
	@Override
	public Object instance(CharacterSequence input, String matched) throws IOException {
	    if(matched.charAt(0)!='%') throw input.exception("·Invalid "+type()+"· ", 0);
	    return matched.substring(1);
	}
}