package fun_pl.semantic;

import fun_pl.syntax.FunLexer;
import fun_pl.syntax.FunParser;
import unalcol.language.LanguageException;
import unalcol.language.Typed;
import unalcol.language.programming.lexer.RCToken;
import unalcol.language.programming.meaner.Meaner;

public class FunMeaner implements Meaner<FunCommand>{

	protected FunMachine machine;
	
	public FunMeaner( FunMachine machine ){ this.machine = machine; }
	
	@Override
	public FunCommand apply(Typed rule) throws LanguageException {
		switch( rule.type() ){
			case FunLexer.VARIABLE:
				RCToken t = (RCToken)rule;
				return new FunCommandCall(t, machine, FunLexer.get(t.lexeme()), true);
			case FunLexer.VALUE:
			case FunLexer.PRIM_VALUE:
			case FunParser.COMMAND:
		}
		return null;
	}

}
