package fun_pl.syntax;

import java.util.Iterator;

import unalcol.collection.Collection;
import unalcol.collection.Array;
import unalcol.collection.Vector;

public interface FunLexerCheck{
	public Collection<String> primitives();
	default boolean is_primitive(String command){ 
		for( String s:primitives() ) if( s.equals(command) ) return true;
		return false;
	}

	public Collection<String> values();
	default Array<String> composed(String value){
		Vector<String> v = new Vector<String>();
		boolean ok = true;
		while(value.length()>0 && ok){
			Iterator<String> iter = values().iterator();
			String r=null;
			ok = false;
			while(iter.hasNext() && !ok){
				r = iter.next();
				ok = value.indexOf(r)==0;
				if(ok){
					v.add(r);
					value = value.substring(r.length());
				}
			}
		}
		if( ok ) return v;
		return null;
	}
}