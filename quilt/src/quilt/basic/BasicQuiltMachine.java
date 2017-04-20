package quilt.basic;

import java.awt.Color;
import java.util.Hashtable;

import quilt.Language;
import quilt.QuiltMachine;
import quilt.Remnant;
import quilt.StripsRemnant;
import quilt.language.Command;
import quilt.language.CommandCall;
import quilt.language.CommandDef;
import quilt.language.QuiltMachineParser;

public class BasicQuiltMachine extends QuiltMachine{
	protected static final String DIAGONAL = "diag";
	protected static final String SQUARE = "squa";
	
	public static final String ROTATE = "rot";
	public static final String SEW = "sew";

	public BasicQuiltMachine(){
		this(new Language());
	}
	
	public BasicQuiltMachine(Language message){
		super(message);
		remnants.put(DIAGONAL, new StripsRemnant(Color.green, 
				new int[][]{ {40,0,100,60}, {50,0,100,50}, {60,0,100,40} } ) );
		remnants.put(SQUARE, new StripsRemnant(Color.red, 
				new int[][]{ {40,0,40,60}, {40,60,100,60},
				 {50,0,50,50}, {50,50,100,50},
				 {60,0,60,40}, {60,40,100,40} }) );
	}
	
	public Remnant remnant(String remnant) {
		try{
			int n = Integer.parseInt(remnant);
			return new NaturalNumberRemnant(n, (StripsRemnant)remnants.get(DIAGONAL));
		}catch( NumberFormatException e ){}
		return super.remnant(remnant);
	}

	public Command[] primitives() {
		return new Command[]{ new Rotate(), new Sew()};
	}

	public Remnant execute(String command) throws Exception{
		QuiltMachineParser parser = new QuiltMachineParser(message, command);
		CommandCall comm = parser.command();
		return comm.execute(this, new Hashtable<String,Remnant>());
	}
	
	public void addDef( String program ) throws Exception{
		QuiltMachineParser parser = new QuiltMachineParser(message, program);
		CommandDef[] comm_def = parser.apply();
		this.add(comm_def);
	}
	
	public void setProgram( String program ) throws Exception{
		init();
		addDef(program);
	}

	public static void main( String[] args ){
		Language message = new Language();
		BasicQuiltMachine m = new BasicQuiltMachine(message);
		try{
			Remnant r = m.execute("sew(rot(sew(diag,diag)),rot(sew(squa,squa)))");
			System.out.println(r);
		}catch( Exception e ){
			System.out.println(e.getMessage());
		}
	}
}
