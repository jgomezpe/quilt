package quilt.logic;

import java.awt.Color;
import java.util.Hashtable;

import quilt.Language;
import quilt.QuiltMachine;
import quilt.Remnant;
import quilt.Sew;
import quilt.basic.BasicQuiltMachineParser;
import quilt.basic.Rotate;
import quilt.operation.Command;
import quilt.strips.StripsRemnant;

public class LogicQuiltMachine extends QuiltMachine{
	protected static final String ZERO = "F";
	
	public static Hashtable<String, Remnant> loadRemnants(){
		Hashtable<String, Remnant> t = new Hashtable<String,Remnant>();
		t.put(ZERO, new StripsRemnant(Color.green, 
				new int[][]{ {0,40,100,40}, {0,50,100,50}, {0,60,100,60} } ) );
		return t;
	}
	
	public LogicQuiltMachine(){
		this(new Language());
	}
	
	
	public LogicQuiltMachine( Language message){
		super(new Command[]{ new Rotate(), new Sew()}, loadRemnants(), new BasicQuiltMachineParser(message), message);
	}		
}