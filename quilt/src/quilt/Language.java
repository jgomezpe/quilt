package quilt;

import java.util.Hashtable;

public class Language {
	public static final String SPANISH = "español";
	public static final String ENGLISH = "english";
	public static final String ERROR = "error";
	public static final String ROW = "row";
	public static final String COLUMN = "column";
	public static final String AT = "at";
	public static final String UNSYMBOL = "unsymbol";
	public static final String EXPECTING = "expecting";
	public static final String LETTER = "letter";
	public static final String DIGIT = "digit";
	public static final String LETTER_DIGIT = "letterdigit";
	public static final String ARGS = "args";
	public static final String UNSTITCH = "unstitch";
	public static final String QUILT = "quilt";
	public static final String REMNANT = "remnant";
	public static final String MISMATCH = "mismatch";
	public static final String UNDEFINED = "undefined";
	
	protected String language = SPANISH;

	protected Hashtable<String, String> message = new Hashtable<String,String>();
	
	public Language(){
		init(SPANISH);
	}
	
	public Language( String language ){
		init(language);
	}
	
	public void spanish(){
		message.put(ERROR, "Error");
		message.put(AT, "en la");	
		message.put(ROW, "fila");
		message.put(COLUMN, "Columna");
		message.put(UNSYMBOL, "Símbolo no esperado");
		message.put(EXPECTING, "se esperaba");
		message.put(LETTER, "letra");
		message.put(DIGIT, "digito");
		message.put(LETTER_DIGIT, "letra o dígito");
		message.put(ARGS, "Número invalido de argumentos en el llamado de la operación");
		message.put(UNSTITCH, "No se puede asociar un retazo primitivo a una colcha en la operación");
		message.put(QUILT, "No se puede asociar una colcha a un retazo primitivo en la operación");
		message.put(MISMATCH, "No se puede asociar el retazo al retazo definido en la operación");
		message.put(UNDEFINED, "Variable u operación no definida");		
	}
	
	public void english(){
		message.put(ERROR, "Error");
		message.put(AT, "at");
		message.put(ROW, "Row");
		message.put(COLUMN, "Column");
		message.put(UNSYMBOL, "Unexpected symbol");
		message.put(EXPECTING, "expecting");
		message.put(LETTER, "letter");
		message.put(DIGIT, "digit");
		message.put(LETTER_DIGIT, "letter or digit");
		message.put(ARGS, "Invalid number of arguments for function's call");
		message.put(UNSTITCH, "Primitive remnant cannot be associated to a quilt in function");
		message.put(QUILT, "Quilt cannot be associated to a primitive remnant in function");
		message.put(MISMATCH, "Remnant cannot be associated to remnant defined in function");
		message.put(UNDEFINED, "Undefined variable or function");		
	}
	
	public void init(String language){
		message.clear();
		if( language.equals(SPANISH)) spanish();
		if( language.equals(ENGLISH)) english();
	}	
	
	public String get( String code ){
		return message.get(code);
	}
}