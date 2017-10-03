package fun_pl.semantic;

import fun_pl.util.Constants;
import unalcol.language.LanguageException;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.vector.Vector;

public class FunProgram extends FunCommand{
	public static String MAIN="main";
	
	public FunProgram(FunMachine machine){ super(machine); }

	public FunProgram(FunMachine machine, Vector<FunCommandDef> commands) throws LanguageException{
		this(machine);
		add(commands);
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

	public void add(Vector<FunCommandDef> def) throws LanguageException{ 
		for( FunCommandDef d:def ) add(d);
	}
	
	public void clear(){ commands.clear(); }
	
	public Object execute( String command, Object... values ) throws Exception{
		Exception e=null;
		Vector<FunCommandDef> v = commands.get(command);
		if( v==null ) throw new LanguageException(Constants.nocommand,command);
		for( FunCommandDef c:v ) try{ return c.execute(values); }catch(Exception ex){e=ex;}
		throw e;
	}

	@Override
	public Object execute(Object... args) throws Exception { return execute(MAIN,args); }

	@Override
	public int arity(){ return 0; }

	@Override
	public String name() { return MAIN; }
	
	public String toString(){
		StringBuilder sb=new StringBuilder();
		for( Vector<FunCommandDef> d:this.commands )
			for( FunCommandDef c:d ) sb.append(c+"\n");		
		return sb.toString();
	}	
}