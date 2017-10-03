package fun_pl.demo;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunCommandCall;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunMeaner;
import fun_pl.semantic.FunProgram;
import fun_pl.syntax.FunEncoder;
import fun_pl.syntax.FunLexer;
import fun_pl.syntax.FunParser;
import fun_pl.util.Constants;
import unalcol.io.CharReader;
import unalcol.io.ShortTermMemoryReader;
import unalcol.language.LanguageException;
import unalcol.language.Typed;
import unalcol.language.programming.lexer.Token;
import unalcol.language.programming.parser.Parser;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.util.I18N;

public class FunDemo {
	public static FunEncoder encoder() throws LanguageException { return new FunEncoder(I18N.get(Constants.code)); }
	
	public static Array<Token> lexer(FunMachine machine, FunEncoder encoder, String code) throws LanguageException{
		ShortTermMemoryReader reader = new CharReader(code);
		FunLexer lexer = new FunLexer(machine);
		Array<Token> tokens = lexer.apply(reader, 0, encoder);
		try{ reader.close(); }catch(Exception e){}
		for( Token t:tokens ){
			System.out.println(t.type()+","+t.pos().offset()+","+t.length()+","+FunLexer.get(t.lexeme()));
		}
		return tokens;
	}
	
	public static Typed parser(Array<Token> tokens, boolean program) throws LanguageException{
		Parser p = new FunParser(program?Constants.COMMAND_DEF_LIST:Constants.COMMAND_CALL);
		Typed t = p.apply(tokens, 0);
		System.out.println(t);
		return t;
	}	
	
	public static FunCommand meaner(Typed t, FunMachine machine, FunEncoder encoder) throws LanguageException{
		FunMeaner meaner = new FunMeaner(machine, encoder);
		return meaner.apply(t);
	}
	
	public static String parser_error(){
		return "*((,Y)=Y\n*(X+3,Y)=*(X,Y)+Y";
	}
	
	public static String meaner_error(){
		return "*(0,Y)=Y\n*(X+$,Y)=*(X,Z)+Y";
	}

	public static String program(){
		return "*(0,Y)=0\n*(X+2,Y)=*(X,Y)+Y";
	}

	public static String command(){
		return "*(6,10)";
	}

	public static void i18n(){
		HTKeyMap<String, String> x = new HTKeyMap<String,String>();
		x.add(FunDemoCommand.plus,"+");
		x.add(FunDemoCommand.invalid,"Number %d cannot be reduced.");
		x.add(Constants.novalue, "Undefined value %s.");
		x.add(Constants.code, "%$=,()+");
		x.add(Constants.extra, "Number of link operands %s not admisible, expecting at most %d link operands");
		x.add(Constants.unexpected, "Unexpected %s at row %d, column %d. Expecting %s");
		x.add(Constants.noargs, "Not valid definition of arguments at row %d, column %d");
		x.add(Constants.command, "command");
		x.add(Constants.norule, "Undefined component %s.");
		x.add(Constants.nocommand, "Undefined command %s.");
		x.add(Constants.argmismatch, "Mismatch in arguments calling command %s at row %d, column %d. Receiving %s");
		x.add(Constants.argnumbermismatch, "Mismatch in the number of arguments calling command %s at row %d, column %d. Expecting %d but receiving %d.");
		x.add(Constants.novar, "Undefined variable %s at row %d, column %d.");
		I18N.add("english",x);
		I18N.use("english");
	}
	
	public static FunCommand analize(FunMachine machine, String code, boolean asProgram){
		try{
			FunEncoder encoder = encoder();
			Array<Token> tokens = lexer(machine, encoder, code);
			Typed t = parser(tokens,asProgram);
			FunCommand c= meaner(t,machine,encoder);
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
