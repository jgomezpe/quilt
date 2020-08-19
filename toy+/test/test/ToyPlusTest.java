package test;

import funpl.FunAPI;
import funpl.lexer.FunLexer;
import funpl.semantic.FunCommand;
import funpl.semantic.FunMachine;
import funpl.semantic.FunMeaner;
import funpl.semantic.FunProgram;
import funpl.syntax.FunParser;
import funpl.util.FunConstants;
import nsgl.character.CharacterSequence;
import nsgl.generic.array.Vector;
import nsgl.generic.hashmap.HashMap;
import nsgl.language.Token;
import nsgl.language.Typed;
import nsgl.language.TypedValue;
import toyplus.Assignment;
import toyplus.Decrement;
import toyplus.NatValues;
import toyplus.Plus;

public class ToyPlusTest {
	public static void print( int tab, Typed t ) {
	    @SuppressWarnings("rawtypes")
	    Object obj = ((TypedValue)t).value();
	    if( obj instanceof Vector ) {
		for( int k=0; k<tab; k++ ) {
		    System.out.print(' ');
		}
		System.out.println(t.type());
		@SuppressWarnings("unchecked")
		Vector<Typed> v = (Vector<Typed>)obj;
		for( int i=0; i<v.size(); i++ ) {
		    print(tab+1, v.get(i));
		}
	    }else {
		for( int k=0; k<tab; k++ ) {
		    System.out.print(' ');
		}
		System.out.println(t);
	    }
	}

	public static FunAPI load(boolean decrement) {
	    FunAPI api = new FunAPI();
	    api.setValue("\\d+", new NatValues());
	    api.setAssignment(new Assignment());
	    api.addOperator(new Plus(0, ""), 1);
	    if( decrement ) api.addOperator(new Decrement(0, ""), 2);
	    return api;	    
	}

	public static void usingAPI() {
	    System.out.println("==============================");
	    FunAPI api = load(true);
	    try {
		String code = "% Hello World\ndec(X)=¬X\nsum(5+X,Y)=X+Y";
		String command = "sum(23, 10)";
		api.compile(new CharacterSequence(code));
		Object obj = api.run(new CharacterSequence(command));
		System.out.println("Result:"+obj);
	    }catch(Exception e) { e.printStackTrace(); System.err.print(e.getMessage()); }
	}
	
	public static void stepByStep() {
	    System.out.println("==============================");
	    String code = "% Hello World\ndec(X)=¬X\nsum(5+X,Y)=X+Y";
	    FunLexer lexer = new FunLexer(true, "\\d+", "¬|\\+");
	    HashMap<String, int[]> opers = new HashMap<String, int[]>();
	    opers.set("¬", new int[] {1, 10});
	    opers.set("+", new int[] {2, 2});
	    FunParser parser = new FunParser(opers);
	    FunMachine machine = new FunMachine();
	    HashMap<String, FunCommand> primitive = new HashMap<String, FunCommand>();
	    primitive.set("+", new Plus(0, "", machine));
	    primitive.set("¬", new Decrement(0, "", machine));
	    machine.setPrimitives(primitive);
	    machine.setValues( new NatValues() );
	    machine.setAssignment( new Assignment() );
	    FunMeaner meaner = new FunMeaner(machine);
	    parser.setRule(FunConstants.DEF_LIST);
	    try {
		System.out.println(code);
		Vector<Token> tokens = lexer.analize(new CharacterSequence(code));
		System.out.println(tokens.size());
		for( Token t:tokens ) System.out.println(t);
		Typed t = parser.analize(tokens);
		print(0,t);
		FunProgram p = (FunProgram)meaner.apply(t);
		System.out.println(p);
		Integer result = (Integer)p.execute("sum", new Object[] {10,23});
		System.out.println("Result:"+result);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    
	}
	
	public static void main( String[] args) {
	    usingAPI(); // Uncomment to use the FunAPI associated to ToyPlus 
//	    stepByStep(); // Uncomment to see step by step
	}

}
