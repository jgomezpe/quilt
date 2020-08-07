package funpl.lexer;

import java.io.IOException;

import nsgl.character.CharacterSequence;
import nsgl.parse.Regex;

public class AtomicLexeme extends Regex{
	public AtomicLexeme(String regexp, String type){ super(regexp, type); }

	@Override
	protected Object instance(CharacterSequence input, String matched) throws IOException { return matched; }	
}