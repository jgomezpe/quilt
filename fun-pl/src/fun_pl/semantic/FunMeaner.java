package fun_pl.semantic;

import fun_pl.syntax.FunEncoder;
import fun_pl.syntax.FunLexer;
import fun_pl.util.FunConstants;
import unalcol.iterator.Position2DTrack;
import unalcol.language.LanguageException;
import unalcol.language.Typed;
import unalcol.language.TypedValue;
import unalcol.language.Token;
import unalcol.language.Meaner;
import unalcol.collection.Array;
import unalcol.collection.Vector;

public class FunMeaner implements Meaner<FunCommand>{
	protected FunMachine machine;
	protected FunEncoder encoder;
	
	public FunMeaner(){}
	
	public FunMeaner( FunMachine machine, FunEncoder encoder ){
		this.machine = machine;
		this.encoder = encoder;
	}
	
	public void setEncoder( FunEncoder encoder ){ this.encoder = encoder; }

	public void setMachine( FunMachine machine ){ this.machine = machine; }
	
	protected FunCommandCall prim(Token t) throws LanguageException{
		String value = FunLexer.get(t.lexeme());
		Array<String> compose = machine.composed(value);
		Position2DTrack pos = (Position2DTrack)t.pos();
		try{
			FunCommandCall c = new FunValue(pos, machine, compose.get(0));
			for( int i=1; i<compose.size(); i++ )
				c = new FunCommandCall(c.pos, machine, machine.symbol().name(), new FunCommandCall[]{c, new FunValue(pos, machine, compose.get(i))} );
			return c;
		}catch(Exception e){ return null; }	
	}

	@SuppressWarnings("unchecked")
	protected FunCommandCall command(TypedValue<Vector<Typed>> t) throws LanguageException{
		Vector<Typed> v = t.value();
		FunCommandCall[] args = new FunCommandCall[v.size()-1];
		int i=0;
		int j=-1;
		String name = null;
		Position2DTrack pos = null;
		for( Typed xt : v ){
			if( i>0 ) args[j] = command_exp((TypedValue<Vector<Typed>>)xt);
			else{
				name = FunLexer.get(((Token)xt).lexeme());
				pos =  (Position2DTrack)(((Token)xt).pos());
			}
			i++;
			j++;
		}
		return new FunCommandCall(pos, machine, name, args);
	}
	
	protected Object get( Vector<?> v, int i ){
		try{ return v.get(i); }catch(Exception e){ return null; }
	}
	
	protected FunCommandCall command_exp(TypedValue<Vector<Typed>> t) throws LanguageException{
		Vector<Typed> vo = t.value();
		if( vo.size()==1 ) return (FunCommandCall)apply((Typed)get(vo, 0));
		Vector<Object> v = new Vector<Object>();
		for(Typed c:vo) v.add(c);
		for( int i=FunConstants.START_LINK_SYMBOLS; i<encoder.symbols_number() && v.size()>1; i++ ){
			int k=1;
			int pk=0;
			int nk=2;
			while(k<v.size()){
				Token token = (Token)get(v,k); 
				if(token.type()==i){
					String name = FunLexer.get(token.lexeme()); // machine.symbol_command(FunLexer.get(token.lexeme())).name();
					FunCommandCall[] args = new FunCommandCall[2];
					Object obj = get(v,pk); 
					args[0]=( obj instanceof Typed)?(FunCommandCall)apply((Typed)obj):(FunCommandCall)obj;
					obj = get(v,nk); 
					args[1]=(obj instanceof Typed)?(FunCommandCall)apply((Typed)obj):(FunCommandCall)obj;
					FunCommandCall c = new FunCommandCall(args[0].pos(), machine, name, args); 
					v.set(pk,c);
					v.remove(k);
					v.remove(k);
					
				}else{
					k+=2;
					nk+=2;
					pk+=2;
				}
			}
		}
		return (FunCommandCall)get(v,0);
		/*
		FunCommandCall c = (FunCommandCall)apply(v.get(v.size()-1));
		for( int i=v.size()-2; i>0; i-=2 ){
			String name = machine.symbol_command(FunLexer.get(((Token)v.get(i)).lexeme())).name();
			c = new FunCommandCall(c, machine, name, new FunCommandCall[]{(FunCommandCall)apply(v.get(i-1)),c} );
		}	
		return c;
		*/		
	}
	
	protected FunCommand command_def(TypedValue<Vector<Typed>> t) throws LanguageException{
		Vector<Typed> v = t.value();
		return new FunCommandDef(machine, (FunCommandCall)apply((Typed)get(v,0)), (FunCommandCall)apply((Typed)get(v,1)));
	}

	protected FunProgram command_def_list(TypedValue<Vector<Typed>> t) throws LanguageException{
		Vector<Typed> v = t.value();
		Vector<FunCommandDef> defs = new Vector<FunCommandDef>();
		for( Typed s:v ) defs.add((FunCommandDef)apply(s));
		return new FunProgram(machine, defs);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FunCommand apply(Typed rule) throws LanguageException {
		switch( rule.type() ){
			case FunConstants.VARIABLE:
				Token t = (Token)rule;
				return new FunVariable((Position2DTrack)t.pos(), machine, FunLexer.get(t.lexeme()));
			//case FunLexer.VALUE: throw new LanguageException("Don't know to deal with values");
			case FunConstants.PRIM_VALUE: return prim((Token)rule);
			case FunConstants.COMMAND_EXP: return command_exp((TypedValue<Vector<Typed>>)rule);
			case FunConstants.COMMAND_DEF: return command_def((TypedValue<Vector<Typed>>)rule);
			case FunConstants.COMMAND_DEF_LIST: return command_def_list((TypedValue<Vector<Typed>>)rule);
			case FunConstants.COMMAND: return command((TypedValue<Vector<Typed>>)rule);
		}
		return null;
	}

}
