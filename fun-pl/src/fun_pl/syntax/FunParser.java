package fun_pl.syntax;

import fun_pl.util.FunConstants;
import unalcol.i18n.I18N;
import unalcol.io.Position2D;
import unalcol.language.LanguageException;
import unalcol.language.Typed;
import unalcol.language.TypedValue;
import unalcol.language.programming.lexer.Token;
import unalcol.language.programming.parser.Parser;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.vector.Vector;

public class FunParser implements Parser{
	
	protected int offset;
	protected Array<Token<?>> tokens;
	
	public FunParser(){}
	
	@SuppressWarnings("unchecked")
	protected Token<Position2D> get(){
		if( offset >= tokens.size() ){
			if( tokens.size()==0 ) return new Token<Position2D>(new Position2D(0,0,0,0)); 
			else return new Token<Position2D>( (Position2D)tokens.get(tokens.size()-1).pos() ); 
		}
		return (Token<Position2D>)tokens.get(offset); 
	}
	
	protected Token<Position2D> next(){
		if( get().type() == Token.EOF ) return get();
		offset++;
		return get(); 
	}

	public static int[] pos( Token<Position2D> t ){
	    Position2D pos = (Position2D)t.pos();
	    return new int[]{pos.row(),pos.column()};
	}
	
	public static int[] posf( Token<Position2D> t ){
	    Position2D pos = (Position2D)t.pos();
	    return new int[]{pos.row()+1,pos.column()};
	}
	
	protected LanguageException exception(Token<Position2D> t, int symbol ){
		int[] pos = posf(t);
		return new LanguageException(t.pos(), FunConstants.unexpected, FunLexer.get(t.lexeme()),pos[0],pos[1],""+FunEncoder.get_symbol(symbol));
	}
	
	protected LanguageException exception(Token<Position2D> t, String code, String symbol ){
		int[] pos = posf(t);
		return new LanguageException(t.pos(), code, FunLexer.get(t.lexeme()),pos[0],pos[1],symbol);
	}
	
	protected Typed command_call() throws LanguageException {
		LanguageException le = null;
		int off = offset;
		try{ return extended_command(true);	}catch(LanguageException e){ le=e; }
		offset=off;
		Token<Position2D> t = get();
		// Trying to make it higher order
		if( t.type()==FunConstants.VARIABLE ){
			Vector<Typed> v = new Vector<Typed>();
			v.add(t);
			Token<Position2D> t1 = next();
			if( t1.type()==FunConstants.OPEN ){
				args(v);
				return new TypedValue<Vector<Typed>>(FunConstants.COMMAND, v);
			}else{
				return t;
			}
		}
		if( t.type()==FunConstants.PRIM_VALUE || (t.type()&FunConstants.VALUE)==FunConstants.VALUE ){
			next();
			return t;
		}
		throw le;
	}
	
	protected Typed wrap_command_exp() throws LanguageException{
		Token<Position2D> t = get();
		int type = t.type(); 
		if(type==FunConstants.OPEN){
			next();
			Typed c = command_exp();
			t = get();
			if(t.type()!=FunConstants.CLOSE) throw exception(t, FunConstants.CLOSE);
			next();
			return c;
		}else{
			if(FunConstants.START_LINK_SYMBOLS<=type && type<=FunConstants.END_LINK_SYMBOLS && I18N.get(FunConstants.arity).charAt(type)=='1'){
				Vector<Typed> v = new Vector<Typed>();
				v.add(t);
				next();
				Vector<Typed> v2 = new Vector<Typed>();
				Typed c = wrap_command_exp();
				v2.add(c);
				v.add(new TypedValue<Vector<Typed>>(FunConstants.COMMAND_EXP, v2));
				return new TypedValue<Vector<Typed>>(FunConstants.COMMAND, v);
			}else return command_call();
		}
	}
	
	protected Typed command_exp() throws LanguageException {
		Vector<Typed> v = new Vector<Typed>();
		Typed c = wrap_command_exp();
		v.add(c);
		Token<Position2D> t = get();
		while(FunConstants.START_LINK_SYMBOLS<=t.type() && t.type()<=FunConstants.END_LINK_SYMBOLS  && I18N.get(FunConstants.arity).charAt(t.type())=='2'){
			v.add(t);
			t = next();
			v.add(wrap_command_exp());
			t=get();
		}
		// @TODO: Check priority
		while( v.size()>3 ){
			int start = 0;
			int end = 2;
			int priority=I18N.get(FunConstants.priority).charAt(v.get(1).type())-'0';
			int k=end+1;
			while(k<v.size()){
				int p = I18N.get(FunConstants.priority).charAt(v.get(k).type())-'0'; 
				if(p!=priority){
					if( p<priority ){
						Vector<Typed> v2 = new Vector<Typed>();
						for( int i=start; i<=end; i++ ){
							v2.add(v.get(start));
							v.remove(start);
						}
						v.add(start, new TypedValue<Vector<Typed>>(FunConstants.COMMAND_EXP, v2));
						k=v.size();
					}else{
						start=end;
						end+=2;
						priority=p;
					}
				}
				k+=2;
			}
		}
		return new TypedValue<Vector<Typed>>(FunConstants.COMMAND_EXP, v);
	} 
	
	protected void args(Vector<Typed> v) throws LanguageException{
		Token<Position2D> t = get();
		int[] pos = posf(t);
		if( t.type()==FunConstants.OPEN ){
			next();
			v.add(command_exp());
			while(get().type()==FunConstants.COMMA){
				next();
				v.add(command_exp());
			}
			t=get();
			if(t.type()==FunConstants.CLOSE){
				next();
				return;
			}
			throw exception(t,FunConstants.CLOSE);
		}
		throw new LanguageException(t.pos(), FunConstants.noargs, pos[0],pos[1]);
	}
	
	protected Typed command() throws LanguageException{
		return extended_command(false);
	} 
	
	protected Typed extended_command(boolean call ) throws LanguageException{
		Token<Position2D> t = get();
		if(  (t.type()&FunConstants.VALUE)==FunConstants.VALUE  || 
			 call && (t.type()==FunConstants.PRIM_COMMAND || (FunConstants.START_LINK_SYMBOLS<=t.type()&&t.type()<=FunConstants.END_LINK_SYMBOLS)) ){
			Vector<Typed> v = new Vector<Typed>();
			v.add(t);
			t = next();
			if( t.type()==FunConstants.OPEN ){
				args(v);
			}
			return new TypedValue<Vector<Typed>>(FunConstants.COMMAND, v);
		}
		if( t.type()==FunConstants.PRIM_COMMAND || (FunConstants.START_LINK_SYMBOLS<=t.type()&&t.type()<=FunConstants.END_LINK_SYMBOLS) )
			throw exception(t, FunConstants.redefined, " ");
		else throw exception(t, FunConstants.unexpected, I18N.get(FunConstants.validcommand));
	} 
	
	protected Typed command_def() throws LanguageException {
		Typed f = command(); 
		Token<Position2D> t = get();
		if( t.type()!=FunConstants.ASSIGN ) throw exception(t,FunConstants.ASSIGN);
		next();
		Typed c = command_exp();
		Vector<Typed> v = new Vector<Typed>();
		v.add(f);
		v.add(c);
		return new TypedValue<Vector<Typed>>(FunConstants.COMMAND_DEF, v);
	} 
	
	protected Typed command_def_list() throws LanguageException {		
		Vector<Typed> v = new Vector<Typed>();
		while(get().type()!=Token.EOF) v.add(command_def());
		return new TypedValue<Vector<Typed>>(FunConstants.COMMAND_DEF_LIST, v);
	} 
	
	@SuppressWarnings("unchecked")
	@Override
	public Typed apply(int main, Array<Token<?>> tokens) throws LanguageException {
		this.tokens = (Array<Token<?>>)tokens;
		this.offset=0;
		switch( main ){
			case FunConstants.COMMAND_CALL: return command_call();
			case FunConstants.COMMAND_EXP: return command_exp();
			case FunConstants.COMMAND_DEF: return command_def();
			case FunConstants.COMMAND_DEF_LIST: return command_def_list();
			case FunConstants.COMMAND: return command();
		}
		
		Position2D pos = new Position2D(0,0,0,0);
		if( tokens.size()>0 ) pos = ((Token<Position2D>)tokens.get(0)).pos();
		throw new LanguageException(pos, FunConstants.norule, 1, 1);
	}	
}