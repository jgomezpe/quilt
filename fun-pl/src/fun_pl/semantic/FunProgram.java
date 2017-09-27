package fun_pl.semantic;

import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.vector.Vector;

public class FunProgram{
	protected HTKeyMap<String, Vector<FunCommandDef>> commands = new HTKeyMap<String,Vector<FunCommandDef>>();
	
	public void add(FunCommandDef def){
		String name = def.name();
		Vector<FunCommandDef> vdef = commands.get(name);
		if( vdef == null ){
			vdef = new Vector<FunCommandDef>();
			commands.set(name, vdef);
		}
		vdef.add(def);
	}

	public void add(Vector<FunCommandDef> def){ for( FunCommandDef d:def ) add(d); }
	
	public void clear(){ commands.clear(); }
}