package funpl.lexer;

import funpl.util.FunConstants;
import nsgl.language.Lexer;
import nsgl.language.lexeme.Space;
import nsgl.language.lexeme.Symbol;

public class FunLexer extends Lexer{
	
	public FunLexer( boolean canStartWithNumberAtom, String value_regex, String primitive_regex ) {
		add( new VariableLexeme());
		add( new Space() );
		add( new FunctionLexeme(canStartWithNumberAtom) );
		add( new AtomicLexeme(value_regex, FunConstants.VALUE) );
		add( new AtomicLexeme(primitive_regex, FunConstants.PRIMITIVE) );
		add( new Symbol("\\(\\)=,") );
		add( new CommentLexeme() ); 
	}	
}