package fun_pl.demo;

import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunSymbolCommand;
import unalcol.language.LanguageException;
import unalcol.util.I18N;

public class FunDemoCommand extends FunSymbolCommand{
	public static final String plus="plus";
	public static final String invalid="invalid";
	public static final String invalid="invalid";
	
	public FunDemoCommand(FunMachine machine) { super(machine); }
	
	@Override
	public Object execute(Object... value) throws LanguageException {
		int s=(Integer)value[0];
		for( int i=1; i<value.length; i++) s += (Integer)value[i];
		return s;
	}
	
	@Override
	public String name() { return I18N.get(plus); }

	@Override
	public Object[] reverse(Object obj, Object[] toMatch) throws LanguageException {
		Integer i = (Integer)obj;
		if( toMatch[0]==null && toMatch[1]==null ){
			if( i<= 0 ) throw new Exception("Number cannot be reduced.."+i);
			return new Integer[]{i-1,1};
		}
		if( toMatch[0]==null){
			Integer j=(Integer)toMatch[1];
			if(i-j<0) throw new Exception("Number cannot be reduced.."+i);
			return new Integer[]{i-j,j};
		}
		if( toMatch[1]==null){
			Integer j=(Integer)toMatch[0];
			if(i-j<0) throw new Exception("Number cannot be reduced.."+i);
			return new Integer[]{j,i-j};
		}
		Integer j=(Integer)toMatch[0];
		Integer k=(Integer)toMatch[1];
		if(j+k!=i) throw new Exception("Number cannot be reduced.."+i);
		return new Integer[]{j,k};
	}
}