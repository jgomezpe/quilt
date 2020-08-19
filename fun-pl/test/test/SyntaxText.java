package test;

import funpl.lexer.FunLexer;
import funpl.syntax.FunParser;
import funpl.util.FunConstants;
import nsgl.character.CharacterSequence;
import nsgl.generic.array.Vector;
import nsgl.generic.hashmap.HashMap;
import nsgl.language.Token;
import nsgl.language.Typed;
import nsgl.language.TypedValue;

public class SyntaxText {
	public static void lexer() {
	    String code = "% Hello World\n//<<|rot(X)";
	    FunLexer lexer = new FunLexer(true, "(?:-|/|<|_)+", "@|\\|");
	    try {
		System.out.println(code);
		Vector<Token> tokens = lexer.analize(new CharacterSequence(code));
		System.out.println(tokens.size());
		for( Token t:tokens ) System.out.println(t);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

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
	
	public static void parser() {
	    String code = "% Hello World\n0=<\n1=@<|rot(X,Y)|@Z+<|Z";
	    FunLexer lexer = new FunLexer(true, "(?:-|/|<|_)+", "@|\\+|\\|");
	    HashMap<String, int[]> opers = new HashMap<String, int[]>();
	    opers.set("@", new int[] {1, 10});
	    opers.set("|", new int[] {2, 1});
	    opers.set("+", new int[] {2, 2});
	    FunParser parser = new FunParser(opers);
	    parser.setRule(FunConstants.DEF_LIST);
	    try {
		System.out.println(code);
		Vector<Token> tokens = lexer.analize(new CharacterSequence(code));
		System.out.println(tokens.size());
		for( Token t:tokens ) System.out.println("Token..."+t);
		Typed t = parser.analize(tokens);
		print(0,t);
	    } catch (Exception e) {
		e.printStackTrace();
	    } 
	}

	public static void main(String[] args) {
	    lexer(); // Uncomment to test the lexer
	    parser(); // Uncomment to test the parser
	}
}
