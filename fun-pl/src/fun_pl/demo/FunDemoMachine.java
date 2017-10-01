package fun_pl.demo;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunSymbolCommand;
import fun_pl.syntax.FunEncoder;
import unalcol.language.LanguageException;

public class FunDemoMachine extends FunMachine{
	protected FunDemoCommand plus;
	
	public FunDemoMachine() {
		plus = new FunDemoCommand(this);
	}

	@Override
	public Object value(String value) throws Exception{ return Integer.parseInt(value); }

	@Override
	public String[] values(String value) throws LanguageException{
		try{ return new String[]{""+this.value(value)}; }catch(Exception e){ throw new LanguageException(FunMachine.novalue, value); }
	}

	@Override
	public FunCommand primitive(String command) throws LanguageException{
		if(plus.name().equals(command)) return plus;
		throw new LanguageException(FunMachine.nocommand,command);
	}

	@Override
	public FunSymbolCommand symbol_command() {
		return plus;
	}

	@Override
	public FunSymbolCommand symbol_command(String symbol) {
		return plus;
	}

	@Override
	public boolean can_assign(String variable, Object value) {
		return (variable.charAt(0)!=FunEncoder.get_symbol(FunEncoder.DOLLAR) || (Integer)value == 1);
	}
}