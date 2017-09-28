package fun_pl.semantic;

import unalcol.io.SimplePosition;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.vector.Vector;

public class FunProgram extends FunCommand{
	public static String MAIN="main";
	
	public FunProgram(FunMachine machine) {
		super(new SimplePosition(), machine);
	}

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
	
	public Object execute( String command, Object... values ) throws Exception{
		Vector<FunCommandDef> v = commands.get(command);
		if( v==null ) throw new Exception("Undefined command");
		for( FunCommandDef c:v ) try{ return c.execute(values); }catch(Exception e){}
		throw new Exception("Mistmatch parameters..");
	}

	@Override
	public Object execute(Object... args) throws Exception { return execute(MAIN,args); }

	@Override
	public int arity(){ return 0; }

	@Override
	public String name() { return MAIN; }
}