package fun_pl.semantic;

import unalcol.language.LanguageException;

public abstract class FunSymbolCommand extends FunCommand{
	public FunSymbolCommand(FunMachine machine) { super(machine); }
	public FunSymbolCommand() { super(); }

	@Override
	public int arity(){ return 2; }
	
	public abstract Object[] reverse(Object obj, Object[] toMatch, FunCommand[] args) throws LanguageException;
}