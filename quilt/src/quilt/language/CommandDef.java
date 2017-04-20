package quilt.language;

import java.util.Hashtable;

import quilt.Language;
import quilt.QuiltMachine;
import quilt.Remnant;

public class CommandDef {
	protected String name;
	protected String[] args=null;
	protected CommandCall body=null;
	protected Command primitive=null;

	public CommandDef( String name, String[] args, CommandCall body ){
		this.name = name;
		this.args = args;
		this.body = body;
	}
	
	public CommandDef( String name, String[] args, Command primitive ){
		this.name = name;
		this.args = args;
		this.primitive = primitive;
	}
	
	public String name(){ return name; }
	public int arity(){ return args.length; }
	
	public Remnant execute( QuiltMachine machine, Remnant[] value ) throws Exception{
		if( value.length != args.length ) throw new Exception(machine.message(Language.ARGS)+" "+name());
		if( body != null ){
			Hashtable<String,Remnant> vars = new Hashtable<String,Remnant>();
			for( int i=0; i<args.length; i++ ){
				int p = args[i].indexOf('.');
				if( p >= 0 ){
					if( value[i].columns()==1 ) throw new Exception(machine.message(Language.UNSTITCH)+" "+name());
					Remnant[] divided = value[i].unstitch();
					vars.put(args[i].substring(0, p), divided[0]);
					vars.put(args[i].substring(p+1), divided[1]);
				}else{
					if( args[i].startsWith(QuiltMachine.PRIMITIVE) ){
						if( value[i].rows()>1 || value[i].columns()>1 ) throw new Exception(machine.message(Language.QUILT)+" "+name());
						vars.put(args[i], value[i].get(0, 0));
					}else{
						Remnant r = machine.remnant(args[i]);
						if( r!=null && !r.equals(value[i]) ) throw new Exception(machine.message(Language.MISMATCH)+" "+name());
						vars.put(args[i], value[i]);
					}
				}
			}
			return body.execute(machine, vars);
		}else{
			return primitive.execute(machine, value);
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
			sb.append("Args:");
			sb.append('\n');
			for( int i=0; i<args.length; i++ ){
				sb.append(tab(k));
				sb.append(args[i]);
				sb.append('\n');
			}
		}
		if( body!=null){
			sb.append(tab(k));
			sb.append("Body:");
			sb.append('\n');
			sb.append(body.toString(k+1));
		}
		return sb.toString();
	}
	
	public String toString(){
		return toString(0);
	}
}