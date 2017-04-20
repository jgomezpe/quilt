package quilt;

import java.util.Hashtable;
import java.util.Vector;

import quilt.language.Command;
import quilt.language.CommandDef;

public abstract class QuiltMachine {
	protected Hashtable<String, Vector<CommandDef>> commands = new Hashtable<String,Vector<CommandDef>>();
	protected Hashtable<String, Remnant> remnants = new Hashtable<String,Remnant>();
	public static final String PRIMITIVE="prim";
	
	protected Language message;
	
	public QuiltMachine( Language message ){
		this.addPrimitives();
		this.message = message;
	}
	
	public String message( String code ){ return message.get(code); }
	
	public void addPrimitives(){
		Command[] primitives = this.primitives();
		CommandDef[] primitives_def = new CommandDef[primitives.length];
		for( int i=0; i<primitives.length; i++ ){
			//System.out.println( "Adding.. "+primitives[i].name());
			primitives_def[i] = new CommandDef(primitives[i].name(), primitives[i].args(), primitives[i]);
		}
		this.add(primitives_def);		
	}
	
	public void add( CommandDef[] def ){
		for( int i=0; i<def.length; i++ ){
			Vector<CommandDef> comm = commands.get(def[i].name());
			if( comm == null ){
				comm = new Vector<CommandDef>();
				commands.put(def[i].name(), comm);
			}
			comm.add(def[i]);
		}
	}
	
	public void init(){
		commands.clear();
		addPrimitives();
	}
	
	public Vector<CommandDef> get( String command ){
		return commands.get(command);
	}

	public abstract Command[] primitives();

	public Remnant remnant( String remnant ){
		return remnants.get(remnant);
	}	
}