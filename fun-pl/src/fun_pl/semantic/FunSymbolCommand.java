package fun_pl.semantic;

import unalcol.io.Position;

public abstract class FunSymbolCommand extends FunCommand{
	public FunSymbolCommand(Position pos, FunMachine machine) { super(pos, machine); }

	public abstract Object[] reverse(Object obj) throws Exception;
}