package fun_pl.demo;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunCommandCall;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunMeaner;
import fun_pl.semantic.FunProgram;
import fun_pl.syntax.FunEncoder;
import fun_pl.syntax.FunLexer;
import fun_pl.syntax.FunParser;
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
	public static FunEncoder encoder() throws LanguageException { return new FunEncoder(I18N.get(FunEncoder.code)); }
	
	public static Array<Token> lexer(FunMachine machine, FunEncoder encoder, String code) throws LanguageException{
		ShortTermMemoryReader reader = new CharReader(code);
		FunLexer lexer = new FunLexer(machine);
		Array<Token> tokens = lexer.apply(reader, 0, encoder);
		try{ reader.close(); }catch(Exception e){}
		for( Token t:tokens ){
			System.out.println(t.type()+","+t.offset()+","+t.length()+","+FunLexer.get(t.lexeme()));
		}
		return tokens;
	}
	
	public static Typed parser(Array<Token> tokens, boolean program) throws LanguageException{
		Parser p = new FunParser(program?FunParser.COMMAND_DEF_LIST:FunParser.COMMAND_CALL);
		Typed t = p.apply(tokens, 0);
		System.out.println(t);
		return t;
	}	
	
	public static FunCommand meaner(Typed t, FunMachine machine, FunEncoder encoder) throws LanguageException{
		FunMeaner meaner = new FunMeaner(machine, encoder);
		return meaner.apply(t);
	}
	
	public static String parser_error(){
		return "*($,Y)=Y\n*(X+3,Y)=plus(*(X,Y),Y)";
	}
	
	public static String meaner_error(){
		return "*($,Y)=Y\n*(X+$,Y)=plus(*(X,Z),Y)";
	}

	public static String program(){
		return "*($,Y)=Y\n*(X+$,Y)=plus(*(X,Y),Y)";
	}

	public static String command(){
		return "*(5,10)";
	}

	public static void i18n(){
		HTKeyMap<String, String> x = new HTKeyMap<String,String>();
		x.add(FunEncoder.code, "%$=,()+");
		x.add(FunDemoCommand.plus,"plus");
		x.add(FunEncoder.extra, "Number of binary operands %s not admisible, expecting at most %d");
		x.add(FunMeaner.nocommand, "Not valid definition of a command at row %d, column %d");
		x.add(FunParser.noargs, "Not valid definition of arguments at row %d, column %d");
		x.add(FunParser.unexpected, "Unexpected %s at row %d, column %d. Expecting %s");
		x.add(FunParser.norule, "Undefined fun language component %s.");
		x.add(FunMachine.novalue, "Undefined fun language value %s.");
		x.add(FunMachine.nocommand, "Undefined fun language command %s.");
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
