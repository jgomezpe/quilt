package funpl.syntax;

import java.io.IOException;

import funpl.semantic.FunMachine;
import funpl.util.FunConstants;
import nsgl.language.Typed;
import nsgl.language.TypedValue;
import nsgl.language.Token;
import nsgl.language.Lexer;
import nsgl.language.Parser;
import nsgl.generic.array.Vector;
import nsgl.generic.keymap.KeyMap;

public class FunParser extends Parser{
	protected int offset;
	protected FunMachine machine;
	
	public static final int ARITY = 0;
	public static final int PRIORITY = 1;
	protected KeyMap<String,int[]> operator;
	
	public FunParser(KeyMap<String, int[]> operator){ this.operator = operator; }
	
	protected IOException exception(String code){
	    return token.exception(code); 
	}
	
	
	protected Typed command_call() throws IOException {
		IOException le = null;
		int off = offset;
		try{ return command(); }catch(IOException e){ le=e; }
		offset=off;
		Token t = token;
		// Trying to make it higher order
		if( check_type(FunConstants.VARIABLE) ){
			Vector<Typed> v = new Vector<Typed>();
			v.add(t);
			if(pos==tokens.size()) return t;
			next();
			if( check_symbol(FunConstants.OPEN) ){
				args(v);
				return new TypedValue<Vector<Typed>>(FunConstants.COMMAND, v);
			}else{
				return t;
			}
		}
		if( check_type(FunConstants.VALUE) ){
		    	if( available() ) next();
		    	else pos++;
			return t;
		}
		throw le;
	}
	
	protected Typed wrap_command_exp() throws IOException{
		Token t = token;
		if(check_symbol(FunConstants.OPEN)){
			next();
			Typed c = command_exp();
			t = token;
			if(!check_symbol(FunConstants.CLOSE)) throw exception(FunConstants.expected+" )");
			if(available()) next();
			return c;
		}else{
			if(check_type(FunConstants.PRIMITIVE) && arity(t)==1){
				Vector<Typed> v = new Vector<Typed>();
				v.add(t);
				next();
				v.add( wrap_command_exp() );
				return new TypedValue<Vector<Typed>>(FunConstants.COMMAND, v);
			}else return command_call();
		}
	}
	
	protected int arity( Token t ) { return operator.get((String)t.value())[ARITY]; }
	
	protected int priority( Token t ) { return operator.get((String)t.value())[PRIORITY]; }
	
	protected int highestPriorityIndex(Vector<Typed> v){
		try{
			int index = 1;
			int priority = priority((Token)v.get(1));
			for( int i=3; i<v.size(); i+=2 ){
				int p = priority((Token)v.get(i));
				if( p>priority ){
					index = i;
					priority = p;
				}
			}
			return index;
		}catch(Exception e){}
		return 1;
	}
	
	protected Typed command_exp(Vector<Typed> v){
		Vector<Typed> n = new Vector<Typed>();
		switch( v.size() ) {
			case 1:
				return v.get(0);
			case 2:
 				return new TypedValue<Vector<Typed>>(FunConstants.COMMAND, v);
			default:    
                		try{
                			int index = highestPriorityIndex(v);
                			Vector<Typed> left = new Vector<Typed>();
                			for( int i=0; i<index; i++ ) left.add(v.get(i));
                			Typed l = command_exp(left);
                			Vector<Typed> right = new Vector<Typed>();
                			for( int i=index+1; i<v.size(); i++ ) right.add(v.get(i));			
                			Typed r = command_exp(right);
                			n.add(v.get(index));
                			n.add(l);
                			n.add(r);
                			return new TypedValue<Vector<Typed>>(FunConstants.COMMAND, n);
                		}catch(Exception e){}	
		}
		return null;
	}
	
	protected Typed command_exp() throws IOException {
		Vector<Typed> v = new Vector<Typed>();
		Typed c = wrap_command_exp();
		v.add(c);
		Token t = token;
		while(available()&& check_type(FunConstants.PRIMITIVE)  && arity(t)==2){
			v.add(t);
			t = next();
			v.add(wrap_command_exp());
			t=token;
		}
		return command_exp(v);
	} 
	
	protected void args(Vector<Typed> v) throws IOException{
		if( check_symbol(FunConstants.OPEN) ){
			next();
			v.add(command_exp());
			while(check_symbol(FunConstants.COMMA)){
				next();
				v.add(command_exp());
			}
			if(check_symbol(FunConstants.CLOSE)){
				if( pos<tokens.size() ) next();
				return;
			}
			throw exception(FunConstants.expected+" )");
		}
		throw exception(FunConstants.noargs);
	}
		
	protected Typed command() throws IOException{
	    	int ppos = pos;
	    	Token ptoken = token;
		if( check_type(FunConstants.FUNCTION) || check_type(FunConstants.PRIMITIVE) ){
			Vector<Typed> v = new Vector<Typed>();
			int a = -1;
			if(check_type(FunConstants.PRIMITIVE)) a = arity(token);
			v.add(token);
			if( this.available() ){
			    next();
			    if( check_symbol(FunConstants.OPEN) ) args(v);
			}
			if(a==-1 || a+1 == v.size() )
			    return new TypedValue<Vector<Typed>>(FunConstants.COMMAND, v);
		}
		pos = ppos;
		token = ptoken;
		throw exception(FunConstants.unexpected+token.value());
	} 
		
	protected Typed command_def() throws IOException {
		if( !check_type(FunConstants.FUNCTION) ) throw exception(FunConstants.unexpected + token.value());
		Typed f = command(); 
		if( !check_symbol(FunConstants.ASSIGN) ) throw exception(FunConstants.expected+" =");
		next();
		Typed c = command_exp();
		Vector<Typed> v = new Vector<Typed>();
		v.add(f);
		v.add(c);
		return new TypedValue<Vector<Typed>>(FunConstants.DEFINITION, v);
	} 
	
	protected Typed command_def_list() throws IOException {		
		Vector<Typed> v = new Vector<Typed>();
		while(pos<this.tokens.size()) v.add(command_def());
		return new TypedValue<Vector<Typed>>(FunConstants.DEF_LIST, v);
	} 
	
	@Override
	protected Typed process() throws IOException {
		switch( rule() ){
			case FunConstants.EXPRESSION: return command_exp();
			case FunConstants.DEFINITION: return command_def();
			case FunConstants.DEF_LIST: return command_def_list();
			case FunConstants.COMMAND: return command_call();
			default: return null;
		}
	}	
	
	@Override
	public Typed analize( Vector<Token> tokens ) throws IOException{
		tokens = Lexer.remove_space(tokens);
		for( int i=tokens.size()-1; i>=0; i-- )	if( tokens.get(i).type().equals(FunConstants.COMMENT) ) tokens.remove(i);
		return super.analize(tokens);
	}	
}