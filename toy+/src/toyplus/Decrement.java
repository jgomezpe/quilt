package toyplus;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunSymbolCommand;
import unalcol.i18n.I18N;
import unalcol.iterator.Position2DTrack;
import unalcol.language.LanguageException;

public class Decrement extends FunSymbolCommand{
	public static final String name="dec";
	public static final String invalid="invalid";
	public static final String irreducible="irreducible";
	
	public Decrement(FunMachine machine) { super(machine); }
	
	@Override
	public Object execute(Object... value) throws LanguageException {
		int s=(Integer)value[0];
		return s-1;
	}
	
	public LanguageException exception(int i, Position2DTrack pos ){
		return new LanguageException(pos, irreducible, pos.row()+1, pos.column()+1, i);
	}
	
	@Override 
	public int arity(){ return 1; }
	
	@Override
	public String name() { return I18N.get(name); }

	@Override
	public Object[] reverse(Object obj, Object[] toMatch, FunCommand[] args) throws LanguageException {
		Position2DTrack p = this.pos();
		Integer i = (Integer)obj;
		if( toMatch[0]==null ){
			if( i<=0 ) throw exception(i, p);
			return new Integer[]{i-1};
		}
		Integer j=(Integer)toMatch[0];
		if(j+1!=i) throw exception(i, p);
		return new Integer[]{j};
	}
}