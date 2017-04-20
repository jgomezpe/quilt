package quilt.language;

import quilt.QuiltMachine;
import quilt.Remnant;

public abstract class Command {
	protected String name;
	protected String[] args=null;

	public Command( String name ){
		this.name = name;
	}
	
	public Command( String name, String[] args ){
		this(name);
		this.args = args;
	}	

	public String name(){ return name; }
	public String[] args(){ return args; }

	public abstract Remnant execute( QuiltMachine machine, Remnant[] value ) throws Exception;
}