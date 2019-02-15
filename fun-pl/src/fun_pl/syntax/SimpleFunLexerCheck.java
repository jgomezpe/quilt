package fun_pl.syntax;

import unalcol.collection.Collection;
import unalcol.collection.Vector;

public class SimpleFunLexerCheck implements FunLexerCheck{
	protected Collection<String> primitives;
	protected Collection<String> values;
	
	public SimpleFunLexerCheck( Collection<String> primitives, Collection<String> values ){
		this.primitives = primitives;
		this.values = values;
	}
	
	public SimpleFunLexerCheck( String[] primitives, String[] values ){
		Vector<String> prim = new Vector<String>();
		Vector<String> val = new Vector<String>();
		for( String s:primitives ) prim.add(s);
		for( String s:values ) val.add(s);
		this.primitives = prim;
		this.values = val;
	}
	
	@Override
	public Collection<String> primitives(){ return primitives; }

	@Override
	public Collection<String> values(){ return values; }
}