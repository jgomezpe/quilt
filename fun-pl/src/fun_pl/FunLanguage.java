package fun_pl;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunMeaner;
import fun_pl.syntax.FunEncoder;
import fun_pl.syntax.FunLexer;
import fun_pl.syntax.FunLexerCheck;
import fun_pl.syntax.FunParser;
import fun_pl.util.FunConstants;
import unalcol.i18n.I18N;
import unalcol.io.Tokenizer;
import unalcol.language.LanguageException;
import unalcol.language.programming.ProgrammingLanguage;

public class FunLanguage extends ProgrammingLanguage<FunCommand>{
	protected FunMachine machine;
	public FunLanguage(FunMachine machine) throws LanguageException{ this( machine, encoder()); }
	
	public FunLanguage(FunMachine machine, FunEncoder encoder){
		super( encoder, new FunLexer(machine), new FunParser(), 
				new FunMeaner(machine,encoder), FunConstants.COMMAND_DEF_LIST);
		this.machine = machine;
	}
	
	public FunCommand process( String code, boolean program ) throws LanguageException{
		return process( code, (program?FunConstants.COMMAND_DEF_LIST:FunConstants.COMMAND_EXP));
	}
	public static FunEncoder encoder() throws LanguageException{
		return new FunEncoder(I18N.get(FunConstants.code));
	}
	
	public static Tokenizer tokenizer(FunLexerCheck machine){
		try{ return new Tokenizer(encoder(), new FunLexer(machine)); }catch(LanguageException e){}
		return null;
	}
}