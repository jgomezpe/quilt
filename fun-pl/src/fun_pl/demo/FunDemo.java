package fun_pl.demo;

import fun_pl.FunLanguage;
import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunCommandCall;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunProgram;
import fun_pl.syntax.FunEncoder;
import fun_pl.syntax.FunLexer;
import fun_pl.util.FunConstants;
import unalcol.language.Typed;
import unalcol.language.programming.lexer.Token;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.util.I18N;

public class FunDemo {
	public static String parser_error(){
		return "*((,Y)=Y\n*(X+3,Y)=*(X,Y)+Y";
	}
	
	public static String meaner_error(){
		return "*(0,Y)=Y\n*(X+$,Y)=*(X,Z)+Y";
	}

	public static String program(){
		return "*(0,Y)=0\n*(X+1,Y)=*(X,Y)+Y";
	}

	public static String command(){
		return "*(6,10)";
	}

	public static void i18n(){
		HTKeyMap<String, String> x = new HTKeyMap<String,String>();
		x.add(FunDemoCommand.plus,"+");
		x.add(FunDemoCommand.invalid,"Number %d cannot be reduced.");
		x.add(FunConstants.novalue, "Undefined value %s.");
		x.add(FunConstants.code, "%$=,()+");
		x.add(FunConstants.extra, "Number of link operands %s not admisible, expecting at most %d link operands");
		x.add(FunConstants.unexpected, "Unexpected %s at row %d, column %d. Expecting %s");
		x.add(FunConstants.noargs, "Not valid definition of arguments at row %d, column %d");
		x.add(FunConstants.validcommand, "valid command name");
		x.add(FunConstants.norule, "Undefined component %s.");
		x.add(FunConstants.nocommand, "Undefined command %s at row %d, column %d.");
		x.add(FunConstants.argmismatch, "Mismatch in arguments calling command %s at row %d, column %d. Receiving %s");
		x.add(FunConstants.argnumbermismatch, "Mismatch in the number of arguments calling command %s at row %d, column %d. Expecting %d but receiving %d.");
		x.add(FunConstants.novar, "Undefined variable %s at row %d, column %d.");
		I18N.add("english",x);
		I18N.use("english");
	}
	
	public static FunCommand analize(FunMachine machine, String code, boolean asProgram){
		try{
			FunEncoder encoder = FunLanguage.encoder();
			Array<Token> tokens = FunLanguage.lexer(machine, encoder, code);
			for( Token t:tokens ){
				System.out.println(t.type()+","+t.pos().offset()+","+t.length()+","+FunLexer.get(t.lexeme()));
			}
			Typed t = FunLanguage.parser(tokens,asProgram);
			System.out.println(t);
			FunCommand c= FunLanguage.meaner(t,machine,encoder);
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
