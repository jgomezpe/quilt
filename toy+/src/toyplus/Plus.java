package toyplus;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunSymbolCommand;
import unalcol.i18n.I18N;
import unalcol.iterator.Position2DTrack;
import unalcol.language.LanguageException;

public class Plus extends FunSymbolCommand{
	public static final String name="plus";
	public static final String invalid="invalid";
	public static final String irreducible="irreducible";
	
	public Plus(FunMachine machine) { super(machine); }
	
	@Override
	public Object execute(Object... value) throws LanguageException {
		int s=(Integer)value[0];
		for( int i=1; i<value.length; i++) s += (Integer)value[i];
		return s;
	}
	
	public LanguageException exception(int i, Position2DTrack pos ){
		return new LanguageException(pos, irreducible, pos.row()+1, pos.column()+1, i);
	}
	
	@Override
	public String name() { return I18N.get(name); }

	@Override
	public Object[] reverse(Object obj, Object[] toMatch, FunCommand[] args) throws LanguageException {
		Position2DTrack p = this.pos();
		Integer i = (Integer)obj;
		if( toMatch[0]==null && toMatch[1]==null ){
			if( i<= 0 ) throw exception(i, p);
			return new Integer[]{i-1,1};
		}
		if( toMatch[0]==null){
			Integer j=(Integer)toMatch[1];
			if(i-j<0) throw exception(i, p);
			return new Integer[]{i-j,j};
		}
		if( toMatch[1]==null){
			Integer j=(Integer)toMatch[0];
			if(i-j<0) throw exception(i, p);
			return new Integer[]{j,i-j};
		}
		Integer j=(Integer)toMatch[0];
		Integer k=(Integer)toMatch[1];
		if(j+k!=i) throw exception(i, p);
		return new Integer[]{j,k};
	}
}