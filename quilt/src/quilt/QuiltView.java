package quilt;

import fun_pl.FunLanguage;
import fun_pl.syntax.SimpleFunLexerCheck;
import unalcol.io.Tokenizer;
import unalcol.language.LanguageException;
import unalcol.collection.Collection;

public class QuiltView {
	protected Tokenizer tokenizer;
	public void tokenizer( Collection<String> primitives, Collection<String> remnants ){
		tokenizer = FunLanguage.tokenizer(new SimpleFunLexerCheck(primitives, remnants));
	}
	
	public void primitives( Collection<String> primitives ){}

	public void values( Collection<String> values ){}
	
	public void exception( LanguageException e ){}
}