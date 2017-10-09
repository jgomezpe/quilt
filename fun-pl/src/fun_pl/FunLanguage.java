package fun_pl;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunMeaner;
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
import unalcol.util.I18N;

public class FunLanguage {
	public static FunEncoder encoder() throws LanguageException { return new FunEncoder(I18N.get(Constants.code)); }
	
	public static Array<Token> lexer(FunMachine machine, FunEncoder encoder, String code) throws LanguageException{
		ShortTermMemoryReader reader = new CharReader(code);
		FunLexer lexer = new FunLexer(machine);
		Array<Token> tokens = lexer.apply(reader, 0, encoder);
		try{ reader.close(); }catch(Exception e){}
		return tokens;
	}
	
	public static Typed parser(Array<Token> tokens, boolean program) throws LanguageException{
		Parser p = new FunParser(program?Constants.COMMAND_DEF_LIST:Constants.COMMAND_CALL);
		return p.apply(tokens, 0);
	}	
	
	public static FunCommand meaner(Typed t, FunMachine machine, FunEncoder encoder) throws LanguageException{
		FunMeaner meaner = new FunMeaner(machine, encoder);
		return meaner.apply(t);
	}
	
	public static FunCommand analize(FunMachine machine, String code, boolean asProgram) throws Exception{
		FunEncoder encoder = FunLanguage.encoder();
		Array<Token> tokens = FunLanguage.lexer(machine, encoder, code);
		Typed t = FunLanguage.parser(tokens,asProgram);
		FunCommand c= FunLanguage.meaner(t,machine,encoder);
		return c;
	}	
}