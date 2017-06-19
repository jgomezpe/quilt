package quilt;

import java.util.Hashtable;

/**
*
* Language
* <P>Language used by the GUI.
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/Language.java" target="_blank">
* Source code </A> is available.
*
* <h3>License</h3>
*
* Copyright (c) 2017 by Jonatan Gomez-Perdomo. <br>
* All rights reserved. <br>
*
* <p>Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
* <ul>
* <li> Redistributions of source code must retain the above copyright notice,
* this list of conditions and the following disclaimer.
* <li> Redistributions in binary form must reproduce the above copyright notice,
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
* <li> Neither the name of the copyright owners, their employers, nor the
* names of its contributors may be used to endorse or promote products
* derived from this software without specific prior written permission.
* </ul>
* <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNERS OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*
*
*
* @author <A HREF="http://disi.unal.edu.co/profesores/jgomezpe"> Jonatan Gomez-Perdomo </A>
* (E-mail: <A HREF="mailto:jgomezpe@unal.edu.co">jgomezpe@unal.edu.co</A> )
* @version 1.0
*/
public class Language {
	public static final char MSG_SEPARATOR=':'; 
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
	public static final String PRIMITIVE = "primitive";
	public static final String MISMATCH = "mismatch";
	public static final String UNDEFINED = "undefined";
	public static final String OPEN = "open";
	public static final String SAVE = "save";
	public static final String COMPILE = "compile";
	public static final String EXECUTE = "execute";
	public static final String COMMAND = "command";
	public static final String TITLE = "title";
	public static final String FILE = "file";
	public static final String NO_ERRORS = "noerrors";
	public static final String ERRORS = "errors";
	public static final String AUTHOR = "author";
	
	protected String language = SPANISH;

	protected Hashtable<String, String> message = new Hashtable<String,String>();
	
	public Language(){
		init(SPANISH);
	}
	
	public Language( String language ){
		init(language);
	}
	
	public void spanish(){
		message.put(REMNANT, "Retazo");
		message.put(PRIMITIVE, "Funcion Basica");
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
		message.put(OPEN, "Cargar");		
		message.put(SAVE, "Guardar");		
		message.put(COMPILE, "Compilar");		
		message.put(EXECUTE, "Ejecutar");		
		message.put(COMMAND, "Comando:");		
		message.put(TITLE, "Ambiente para Maquina de Coser");		
		message.put(FILE, "Archivos Maquina Coser");
		message.put(NO_ERRORS, "Sin errores de compilación...!!!!");
		message.put(ERRORS, "Problemas!!! Se presentaron problemas al compilar.\nLa descripción de los errores esta en el Tab error");
		message.put(AUTHOR, "Desarrollado por el Profesor Jonatan Gómez Perdomo.\n jgomezpe at unal.edu.co \nUniversidad Nacional de Colombia");
	}
	
	public void english(){
		message.put(REMNANT, "Remnant");
		message.put(PRIMITIVE, "Primitive Function");
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
		message.put(OPEN, "Load");		
		message.put(SAVE, "Save");		
		message.put(COMPILE, "Compile");		
		message.put(EXECUTE, "Execute");		
		message.put(COMMAND, "Command:");		
		message.put(TITLE, "Sewer Machine Environment");		
		message.put(FILE, "Sewer machine files");		
		message.put(NO_ERRORS, "No errors...!!!!");
		message.put(ERRORS, "Problems!!! There are some errors.\nSee error tab for a complete description of the error");
		message.put(AUTHOR, "Developed by Professor Jonatan Gomez Perdomo.\n jgomezpe at unal.edu.co \nUniversidad Nacional de Colombia");
	}
	
	public void init(String language){
		message.clear();
		if( language.equals(SPANISH)) spanish();
		if( language.equals(ENGLISH)) english();
	}	
	
	public String current(){ return language; }
	
	public String get( String code ){
		return message.get(code);
	}
		
	public Exception error( Position pos, String c ){
		pos = new Position(pos);
		int row = pos.row();
		int column = pos.column();
		StringBuilder sb = new StringBuilder();
		sb.append(pos.toString());
		sb.append(MSG_SEPARATOR);
		sb.append(message.get(Language.ERROR));
		sb.append(' ');
		sb.append(message.get(Language.AT));
		sb.append(' ');
		sb.append(message.get(Language.ROW));
		sb.append(' ');
		sb.append(""+(row+1));
		sb.append(' ');
		sb.append(message.get(Language.COLUMN));
		sb.append(' ');
		sb.append(""+(column+1));
		sb.append(": ");
		sb.append(c);
		return new Exception(sb.toString());
	}	
}