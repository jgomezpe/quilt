package fun_pl.demo;

import fun_pl.FunLanguage;
import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunCommandCall;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunProgram;
import fun_pl.syntax.FunLexer;
import fun_pl.util.FunConstants;
import unalcol.i18n.I18N;
import unalcol.io.Position2D;
import unalcol.language.Typed;
import unalcol.language.programming.lexer.Token;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.keymap.HTKeyMap;

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

	public static String english(){
		return "<?xml version='1.0'?>\n" +
		"  <language id='english'>\n" +
		"    <i18n id='" + FunDemoPlus.plus + "' msg='+'/>\n" +		
		"    <i18n id='" + FunDemoDecrement.dec + "' msg='!'/>\n" +		
		"    <i18n id='" + FunDemoPlus.invalid + "' msg='Number %d cannot be reduced.'/>\n" +		
		"    <i18n id='" + FunConstants.novalue + "' msg='Undefined value %s.'/>\n" +		
		"    <i18n id='" + FunConstants.code + "' msg='%$=,()+!'/>\n" +		
		"    <i18n id='" + FunConstants.arity + "' msg='00000021'/>\n" +		
		"    <i18n id='" + FunConstants.priority + "' msg='0000001'/>\n" +		
		"    <i18n id='" + FunConstants.extra + "' msg='Number of link operands %s not admisible, expecting at most %d link operands'/>\n" +		
		"    <i18n id='" + FunConstants.unexpected + "' msg='Unexpected %s at row %d, column %d. Expecting %s'/>\n" +		
		"    <i18n id='" + FunConstants.noargs + "' msg='Not valid definition of arguments at row %d, column %d'/>\n" +		
		"    <i18n id='" + FunConstants.validcommand + "' msg='valid command name'/>\n" +		
		"    <i18n id='" + FunConstants.norule + "' msg='Undefined component %s.'/>\n" +		
		"    <i18n id='" + FunConstants.nocommand + "' msg='Undefined command %s at row %d, column %d.'/>\n" +		
		"    <i18n id='" + FunConstants.argmismatch + "' msg='Mismatch in arguments calling command %s at row %d, column %d. Receiving %s'/>\n" +		
		"    <i18n id='" + FunConstants.argnumbermismatch + "' msg='Mismatch in the number of arguments calling command %s at row %d, column %d. Expecting %d but receiving %d.'/>\n" +		
		"    <i18n id='" + FunConstants.novar + "' msg='Undefined variable %s at row %d, column %d.'/>\n"+
		" </language>";
	}
	
	public static void i18n(){
		I18N.add("english",english());
		I18N.use("english");
	}
	
	public static FunCommand analize(FunMachine machine, String code, boolean asProgram){
		try{
			FunLanguage funLang = new FunLanguage(machine);
			Array<Token<?>> tokens = funLang.lexer(code);
			for( Token<?> t:tokens ){
			    Position2D pos = (Position2D)t.pos();
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
		FunMachine machine = new FunDemoMachine();
		//String code=parser_error(); //Test the compiler using a program written with a grammar error
		//String code=meaner_error(); //Test the compiler using a program written with a semantic error
		String code=program(); //Test the compiler without errors
		FunProgram program = (FunProgram)analize(machine, code, true);
		machine.setProgram(program);
		FunCommandCall command = (FunCommandCall)analize(machine,command(),false);
		try{
			System.out.println("The result is:"+command.execute(new HTKeyMap<String, Object>()));
		}catch(Exception e ){ e.printStackTrace();}
	}
}
