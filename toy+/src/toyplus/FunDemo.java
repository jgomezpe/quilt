package toyplus;

import fun_pl.FunLanguage;
import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunCommandCall;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunProgram;
import fun_pl.semantic.FunSymbolCommand;
import fun_pl.syntax.FunLexer;
import fun_pl.util.FunConstants;
import unalcol.i18n.I18N;
import unalcol.types.collection.iterator.Position2DTrack;
import unalcol.language.Typed;
import unalcol.language.generalized.GeneralizedToken;
import unalcol.types.collection.Collection;
import unalcol.types.collection.keymap.HashMap;

public class FunDemo {
	public static String parser_error(){
		return "*((,Y)=Y\n*(X+3,Y)=*(X,Y)+Y";
	}
	
	public static String meaner_error(){
		return "*(0,Y)=Y\n*(X+$,Y)=*(X,Z)+Y";
	}

	public static String program(){
		return "*(0,Y)=0\n*(!X,Y)=*(X,Y)+Y";
	}

	public static String command(){
		return "*(!6,10)";
	}

	public static void i18n(){
		I18N.setLanguage("spanish");
		I18N.use("toy");
	}
	
	public static FunCommand analize(FunMachine machine, String code, boolean asProgram){
		try{
			FunLanguage funLang = new FunLanguage(machine);
			Collection<GeneralizedToken<Integer>> tokens = funLang.lexer(code);
			for( GeneralizedToken<Integer> t:tokens ){
			    Position2DTrack pos = (Position2DTrack)t.pos();
			    System.out.println(t.type()+","+pos.row()+","+pos.column()+","+t.length()+","+FunLexer.get(t.lexeme()));
			}
			Typed t = funLang.parser((asProgram?FunConstants.COMMAND_DEF_LIST:FunConstants.COMMAND_EXP),tokens);
			System.out.println(t);
			FunCommand c= funLang.meaner(t);
			System.out.println(c);
			return c;
		}catch( Exception e ){ e.printStackTrace();	}	
		return null;
	}
	
	public static void main( String[] args ){
		i18n(); // Defining the language for error messages
		HashMap<String, FunSymbolCommand> primitives = new HashMap<String, FunSymbolCommand>();
		Plus plus = new Plus(null);
		Decrement dec = new Decrement(null);
		primitives.set(plus.name(), plus);
		primitives.set(dec.name(), dec);
		System.out.println(dec.name());
		System.out.println(plus.name());
		FunMachine machine = new ToyPlusMachine(primitives, plus.name());
		//String code=parser_error(); //Test the compiler using a program written with a grammar error
		//String code=meaner_error(); //Test the compiler using a program written with a semantic error
		String code=program(); //Test the compiler without errors
		FunProgram program = (FunProgram)analize(machine, code, true);
		machine.setProgram(program);
		FunCommandCall command = (FunCommandCall)analize(machine,command(),false);
		try{
			System.out.println("The result is:"+command.execute(new HashMap<String, Object>()));
		}catch(Exception e ){ e.printStackTrace();}
	}
}
