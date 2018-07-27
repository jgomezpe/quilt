package toyplus;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunCommandInstance;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunMachineInstance;
import unalcol.types.collection.keymap.ImmutableKeyMap;

public class ToyPlusMachineInstance extends FunMachineInstance<Integer> {
	public static final String MACHINE="machine";
	
	public ToyPlusMachineInstance() {}
	
	@Override
	public FunMachine init(ImmutableKeyMap<String, FunCommand> commands, ImmutableKeyMap<String, Integer> values) {
		return new ToyPlusMachine(commands);
	}

	@Override
	public void initCommands() {
		commands = new FunCommandInstance();
		commands.register(new Plus(null));
		commands.register(new Decrement(null));		
	}

	@Override
	public void initValues() {}
}