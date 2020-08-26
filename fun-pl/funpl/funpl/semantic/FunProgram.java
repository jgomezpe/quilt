package funpl.semantic;

import java.io.IOException;

import funpl.util.FunConstants;
import nsgl.generic.hashmap.HashMap;
import nsgl.generic.array.Vector;

public class FunProgram extends FunCommand{
	public static String MAIN="main";
	
	public FunProgram(FunMachine machine){ super(0,"program",machine); }

	public FunProgram(FunMachine machine, Vector<FunCommandDef> commands) throws IOException{
		this(machine);
		add(commands);
	}

	protected HashMap<String, Vector<FunCommandDef>> commands = new HashMap<String,Vector<FunCommandDef>>();
	
	public void add(FunCommandDef def){
		String name = def.name();
		Vector<FunCommandDef> vdef = commands.get(name);
		if( vdef == null ){
			vdef = new Vector<FunCommandDef>();
			commands.set(name, vdef);
		}
		vdef.add(def);
	}

	public void add(Vector<FunCommandDef> def) throws IOException{ 
		for( FunCommandDef d:def ) add(d);
	}
	
	public void clear(){ commands.clear(); }
	
	public boolean defined(String command){	return commands.valid(command);	} 
	
	protected Vector<FunCommandDef> candidates(String command, int arity ){
		Vector<FunCommandDef> candidates = new Vector<FunCommandDef>();
		try{
			Vector<FunCommandDef> v = commands.get(command);
			for( FunCommandDef c:v ) if( c.arity()==arity ) candidates.add(c);
		}catch(Exception e){}
		return candidates;
	}
	
	public boolean constant(String command){ return candidates(command,0).size()>0;	}

	
	public Object execute( String command, Object... values ) throws Exception{
		if( !defined(command) ) throw exception(FunConstants.nocommand + command);
		Vector<FunCommandDef> candidates = candidates(command,values.length );
		if(candidates.size()==0) throw exception(FunConstants.argnumbermismatch + command);
		Exception e=null;
//		LanguageMultiException e=null;
		int i=0; 
		while( i<candidates.size() ){
			FunCommandDef cand = candidates.get(i);
			try{ 
				cand.match(values);
				i++;
			}catch(Exception ex){
/*					if( e!=null ){
						e.add(ex);
					}else e = new LanguageMultiException(ex); */
			    	e = ex;
				candidates.remove(i);
			}
		}	
		if( candidates.size() == 0 ) throw e;
		e = null;
		for( FunCommandDef c:candidates ){
			try{ return c.execute(values); }
			catch(IOException ex){
			    e = ex;
				//if( e != null ) e.add(ex); else e = new LanguageMultiException(ex);
			}
		}	
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