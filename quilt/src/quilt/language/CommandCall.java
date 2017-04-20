package quilt.language;

import java.util.Hashtable;
import java.util.Vector;

import quilt.Language;
import quilt.QuiltMachine;
import quilt.Remnant;

public class CommandCall {
	protected String name;
	protected CommandCall[] args=null;

	public CommandCall( String name ){
		this.name = name;
	}
	
	public CommandCall( String name, CommandCall[] args ){
		this(name);
		this.args = args;
	}

	public String name(){ return name; }
	public CommandCall[] args(){ return args; }
	public int arity(){ return args!=null?args.length:0; }
	
	public Remnant execute( QuiltMachine machine, Hashtable<String, Remnant> values ) throws Exception{
		Remnant r=null;
		if( arity()==0 ){
			// Checking basic remnants
			r = machine.remnant( name() ); 
			if( r!=null ) return r; 
			// Checking variables with the given name
			r = values.get(name());
			if( r!=null ) return r;
			// Checking the list of defined commands
			Vector<CommandDef> def = machine.get(name());
			// If there is not a command with the given name return null
			if( def == null || def.size()==0 ) throw new Exception( machine.message(Language.UNDEFINED)+" "+name());
			// Checking for a command with no arguments
			int i=0; 
			while( i<def.size() && def.get(i).arity()!=0 ){ i++; }
			// If not command matches return null
			if( i==def.size() )  throw new Exception( machine.message(Language.ARGS)+" "+name());			
			// Executes the first command matching the name
			return def.get(i).execute(machine, new Remnant[0]);
		}else{
			// Obtains a command matching the name
			Vector<CommandDef> def = machine.get(name());
			// If not command matches the name return null
			if( def == null || def.size()==0 )  throw new Exception( machine.message(Language.UNDEFINED)+" "+name());
			Remnant[] val = null; 
			String str = machine.message(Language.ARGS)+" "+name();
			int i=0;
			while( r==null && i<def.size() ){
				if(def.get(i).arity()==arity() ){
					// Evaluating the arguments of the command call, if not already evaluated
					if( val == null ){
						val = new Remnant[arity()];
						boolean flag = true;
						int k=0;
						while(flag&&k<val.length){
							val[k]=args[k].execute(machine, values);
							k++;
						}
					}
					try{
						r = def.get(i).execute(machine, val);
					}catch(Exception e){
						str = e.getMessage();
					}	
				}
				i++; 
			}
			if( r==null ) throw new Exception(str);
			return r;
		}
	}
	
	public String tab( int k ){
		StringBuilder sb = new StringBuilder();
		for( int i=0; i<k; i++ ) sb.append(' ');
		return sb.toString();
	}

	public String toString(int k){
		StringBuilder sb = new StringBuilder();
		sb.append(tab(k));
		sb.append("Name:");
		sb.append(name);
		sb.append('\n');
		if( args != null ){
			sb.append(tab(k));
			sb.append("Call Args:");
			sb.append('\n');
			for( int i=0; i<args.length; i++ ){
				sb.append(args[i].toString(k+1));
			}
		}
		return sb.toString();
	}
	
	public String toString(){
		return toString(0);
	}	
}