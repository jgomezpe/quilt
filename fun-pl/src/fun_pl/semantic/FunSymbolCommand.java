package fun_pl.semantic;

import unalcol.language.LanguageException;

public abstract class FunSymbolCommand extends FunCommand{
	public FunSymbolCommand(FunMachine machine) { super(null,machine); }
//	public FunSymbolCommand() { this(null); }

	@Override
	public int arity(){ return 2; }
	
	public abstract Object[] reverse(Object obj, Object[] toMatch, FunCommand[] args) throws LanguageException;
}