package funpl.lexer;

import java.io.IOException;

import nsgl.character.CharacterSequence;
import nsgl.parse.Regex;

public class AtomicLexeme extends Regex{
	public AtomicLexeme(String regexp, String type){ super(regexp, type); }

	@Override
	public Object instance(CharacterSequence input, String matched) throws IOException {
	    String m = match(new CharacterSequence(matched));
	    if( m!=null ) return matched;
	    throw input.exception("·Invalid "+type()+"· ", 0);
	}	
}