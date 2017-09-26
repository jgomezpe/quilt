package fun_pl.syntax;

import unalcol.io.CharReader;
import unalcol.io.ShortTermMemoryReader;
import unalcol.language.programming.lexer.Token;
import unalcol.types.collection.array.Array;

public class BasicFunLexer extends FunLexer{
	protected String[] prim_function;
	protected String[] prim_value;
	public BasicFunLexer( String[] prim_function, String[] prim_value ){
		this.prim_function = prim_function;
		this.prim_value = prim_value;
	}
	
	@Override
	protected Token check_primitive(Token t){
		String lexeme = get(t.lexeme());
		if( prim_function!=null ){
			int i=0;
			while(i<prim_function.length && !lexeme.equals(prim_function[i])) i++;
			if( i<prim_function.length ){
				t.setType(PRIM_FUNCTION);
				return t;
			}
		}
		if( prim_value!=null ){
			boolean flag = true;
			while(flag && lexeme.length()>0){
				int i=0;
				while(i<prim_value.length && lexeme.indexOf(prim_value[i])!=0) i++;
				flag = ( i<prim_value.length );
				if( flag ) lexeme = lexeme.substring(prim_value[i].length());
			}
			if( flag ){
				t.setType(PRIM_VALUE);
				return t;	
			}
		}		
		return t;
	}
	
	public static void main(String[] args){
		String code="+34.22(X1_2|$3)=rot(horzdiag)diagdemo(Y#$$)\n%commentX#1\r\nAve|cesar";
		FunEncoder encoder = new FunEncoder();
		ShortTermMemoryReader reader = new CharReader(code);
		String[] prim_func = new String[]{"rot","sew"};
		String[] prim_value = new String[]{"horz","diag"};
		FunLexer lexer = new BasicFunLexer(prim_func,prim_value);
		try {
			Array<Token> tokens = lexer.apply(reader, 0, encoder);
			reader.close();
			for( Token t:tokens ){
				System.out.println(t.type()+","+t.offset()+","+t.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
