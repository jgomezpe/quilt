package qm.quilt;

import funpl.semantic.FunValueInterpreter;
import funpl.util.FunConstants;
import lifya.lexeme.Lexeme;
import lifya.Token;
import qm.remnant.CQueue;
import qm.remnant.Classic;
import qm.remnant.Immutable;
import qm.remnant.Remnant;
import speco.array.Array;

public class Store implements FunValueInterpreter{
	protected String[] remnant;
	protected String type;
	protected Lexeme<String> lexeme=null;
	
	protected Remnant create(String id) {
		if(type.equals("Immutable")) return new Immutable(id);
		if(type.equals("CQueue")) return new CQueue(id);
		return new Classic(id);
	}
	
	public Store(String type, String[] remnant, int[] reduction) {
		CQueue.ids = remnant;
		this.type = type;
		if( reduction!=null ) { 
			Classic.reductions.clear();
			for( int i=0; i<remnant.length; i++ ) 
				Classic.reductions.put(remnant[i], reduction[i]);
		}	
		for( int i=0; i<remnant.length-1; i++) {
			for( int j=i+1; j<remnant.length; j++) {
				if( remnant[i].length()<remnant[j].length()) {
					String t = remnant[i];
					remnant[i] = remnant[j];
					remnant[j] = t;
				}
			}
		}
		
		this.remnant = remnant;
		this.lexeme = new qm.quilt.Lexeme(FunConstants.VALUE, remnant);
	}

	@Override
	public String description() {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		String pipe = "";
		for( int i=0; i<remnant.length; i++ ) {
			sb.append(pipe);
			sb.append(this.remnant[i]);
			pipe = ",";
		}
		sb.append("]");
		return sb.toString();
	}
	
	@Override
	public Object get(String code) {
		if(valid(code )) {
			Array<Remnant> v = new Array<Remnant>();
			while(code.length()>0) {
				int length = 0;
				int k=-1;
				for( int i=0; i<remnant.length; i++ ) {
					if( remnant[i].length()>=length && code.startsWith(remnant[i])) {
						k = i;
						length = remnant[i].length();
					}  
				}
				v.add(create(remnant[k]));
				code = code.substring(remnant[k].length());
			}
			if(v.size()==1) return v.get(0);
			Remnant[][] m = new Remnant[1][v.size()];
			for( int i=0; i<v.size(); i++) m[0][i] = v.get(i);
			return new Matrix(m);
		}
		return null;
	}

	@Override
	public boolean valid(String code){
		Token t = lexeme.match(code);
		return !t.isError() && ((String)t.value()).length()==code.length();
	}

	@Override
	public Lexeme<?> lexeme() {
		return lexeme;
	}
}
