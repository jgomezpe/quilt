package toyplus;

import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunMachineInstance;
import fun_pl.semantic.FunSymbolCommand;
import unalcol.types.collection.keymap.ImmutableKeyMap;

public class ToyPlusMachineInstance extends FunMachineInstance<Integer> {
	public static final String MACHINE="machine";
	
	public ToyPlusMachineInstance() {}
	
	@Override
	public void initCommands() {
		primitives.clear();
		FunSymbolCommand c = new Plus(null);
		primitives.set(c.name(), c);
		c = new Decrement(null);
		primitives.set(c.name(),c);		
	}

	@Override
	public void initValues(){}

	@Override
	public FunMachine init(ImmutableKeyMap<String, FunSymbolCommand> commands, String symbol) {
		return new ToyPlusMachine(commands, symbol);
	}
}