package fun_pl.semantic;

import fun_pl.util.FunConstants;
import unalcol.language.LanguageException;
import unalcol.language.util.LanguageMultiException;
import unalcol.types.collection.keymap.HashMap;
import unalcol.types.collection.vector.Vector;

public class FunProgram extends FunCommand{
	public static String MAIN="main";
	
	public FunProgram(FunMachine machine){ super(null,machine); }

	public FunProgram(FunMachine machine, Vector<FunCommandDef> commands) throws LanguageException{
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

	public void add(Vector<FunCommandDef> def) throws LanguageException{ 
		for( FunCommandDef d:def ) add(d);
	}
	
	public void clear(){ commands.clear(); }
	
	public boolean defined(String command){	return commands.get(command)!=null;	} 
	
	protected Vector<FunCommandDef> candidates(String command, int arity ){
		Vector<FunCommandDef> candidates = new Vector<FunCommandDef>();
		Vector<FunCommandDef> v = commands.get(command);
		if( v!=null )
		for( FunCommandDef c:v ) if( c.arity()==arity ) candidates.add(c);
		return candidates;
	}
	
	public boolean constant(String command){ return candidates(command,0).size()>0;	}

	@Override
	protected LanguageException exception( String code, Object... args){
		Object[] nargs = new Object[2+args.length]; 
		if( args.length>0 ) System.arraycopy(args, 1, nargs, 3, args.length-1);
		nargs[0] = args[0];
		nargs[1] = pos.row()+1;
		nargs[2] = pos.column()+1;
		return new LanguageException(pos, code, nargs);
	}
	
	public Object execute( String command, Object... values ) throws LanguageException{
		if( !defined(command) ) throw exception(FunConstants.nocommand, command);
		Vector<FunCommandDef> candidates = candidates(command,values.length );
		if(candidates.size()==0) throw exception(FunConstants.argnumbermismatch, command);
		LanguageMultiException e=null;
		int i=0; 
		while( i<candidates.size() ){ 
			try{ 
				candidates.get(i).match(values);
				i++;
			}catch(LanguageException ex){
				if( e!=null ){
					e.add(ex);
				}else e = new LanguageMultiException(ex);
				candidates.remove(i);
			}
		}	
		if( candidates.size() == 0 ) throw e;
		e = null;
		for( FunCommandDef c:candidates ){
			try{ return c.execute(values); }
			catch(LanguageException ex){
				if( e != null ) e.add(ex); else e = new LanguageMultiException(ex);
			}
		}	
		throw e;
	}
	
	@Override
	public Object execute(Object... args) throws LanguageException { return execute(MAIN,args); }

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