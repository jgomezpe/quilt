package fun_pl.demo;

import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunSymbolCommand;
import unalcol.io.SimplePosition;
import unalcol.util.I18N;

public class FunDemoCommand extends FunSymbolCommand{
	public static final String plus="plus";
	public static final String invalid="invalid";
	public FunDemoCommand(FunMachine machine) {
		super(new SimplePosition(), machine);
	}
	@Override
	public Object execute(Object... value) throws Exception {
		int s=(Integer)value[0];
		for( int i=1; i<value.length; i++) s += (Integer)value[i];
		return s;
	}
	
	@Override
	public int arity() { return 2; }
	@Override
	public String name() { return I18N.get(plus); }

	@Override
	public Object[] reverse(Object obj) throws Exception {
		Integer i = (Integer)obj;
		if( i<= 0 ) throw new Exception("Number cannot be reduced.."+i);
		return new Integer[]{i-1,1};
	}
}