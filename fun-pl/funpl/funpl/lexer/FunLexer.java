package funpl.lexer;

import funpl.util.FunConstants;
import nsgl.language.Lexer;
import nsgl.language.lexeme.Space;
import nsgl.language.lexeme.Symbol;

public class FunLexer extends Lexer{
	
	public FunLexer( boolean canStartWithNumberAtom, String value_regex, String primitive_regex ) {
		add( new VariableLexeme(), 1 );
		add( new Space(), 1 );
		add( new FunctionLexeme(canStartWithNumberAtom), 1 );
		add( new AtomicLexeme(value_regex, FunConstants.VALUE), 1);
		add( new AtomicLexeme(primitive_regex, FunConstants.PRIMITIVE), 1);
		add( new Symbol("\\(\\)=,"), 1 );
		add( new CommentLexeme(), 1 ); 
	}	
}