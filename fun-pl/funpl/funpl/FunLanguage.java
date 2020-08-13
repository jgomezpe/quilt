package funpl;

import java.io.IOException;

import funpl.lexer.FunLexer;
import funpl.semantic.FunCommand;
import funpl.semantic.FunMachine;
import funpl.semantic.FunMeaner;
import funpl.syntax.FunParser;
import funpl.util.FunConstants;
import nsgl.character.CharacterSequence;
import nsgl.language.Language;

public class FunLanguage extends Language<FunCommand>{
	protected FunMachine machine;
	public FunLanguage(FunLexer lexer, FunParser parser, FunMachine machine){ super( lexer, parser, new FunMeaner(machine)); }
	
	public FunCommand process( CharacterSequence code, boolean program ) throws IOException{
	    	parser.setRule(program?FunConstants.DEF_LIST:FunConstants.EXPRESSION);
	    	return this.process(code);
	}
}