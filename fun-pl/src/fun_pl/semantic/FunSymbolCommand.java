package fun_pl.semantic;

public abstract class FunSymbolCommand extends FunCommand{
	public FunSymbolCommand(FunMachine machine) { super(machine); }

	@Override
	public int arity() {
		return 2;
	}
	
	public abstract Object[] reverse(Object obj, Object[] toMatch) throws Exception;
}