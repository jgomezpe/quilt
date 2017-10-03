package fun_pl.semantic;

import fun_pl.syntax.FunEncoder;
import fun_pl.syntax.FunLexer;
import fun_pl.util.Constants;
import unalcol.language.LanguageException;
import unalcol.language.Typed;
import unalcol.language.TypedValue;
import unalcol.language.programming.lexer.RCToken;
import unalcol.language.programming.lexer.Token;
import unalcol.language.programming.meaner.Meaner;
import unalcol.types.collection.array.Array;
import unalcol.types.collection.vector.Vector;

public class FunMeaner implements Meaner<FunCommand>{
	protected FunMachine machine;
	protected FunEncoder encoder;
	
	public FunMeaner( FunMachine machine, FunEncoder encoder ){
		this.machine = machine;
		this.encoder = encoder;
	}
	
	protected FunCommandCall prim(RCToken t) throws LanguageException{
		Array<String> compose = machine.values(FunLexer.get(t.lexeme()));
		FunCommandCall c = new FunValue(t, machine, compose.get(0));
		for( int i=1; i<compose.size(); i++ )
			c = new FunCommandCall(c, machine, machine.symbol_command().name(), new FunCommandCall[]{c, new FunValue(t, machine, compose.get(i))} );
		return c;
	}

	@SuppressWarnings("unchecked")
	protected FunCommandCall command(TypedValue<Vector<Typed>> t) throws LanguageException{
		Vector<Typed> v = t.value();
		String name = FunLexer.get(((Token)v.get(0)).lexeme());
		FunCommandCall[] args = new FunCommandCall[v.size()-1];
		for( int i=0; i<args.length; i++) args[i] = command_exp((TypedValue<Vector<Typed>>)v.get(i+1));
		return new FunCommandCall((RCToken)v.get(0), machine, name, args);
	}
	
	protected FunCommandCall command_exp(TypedValue<Vector<Typed>> t) throws LanguageException{
		Vector<Typed> vo = t.value();
		if( vo.size()==1 ) return (FunCommandCall)apply(vo.get(0));
		Vector<Object> v = new Vector<Object>();
		for(Typed c:vo) v.add(c);
		for( int i=Constants.START_LINK_SYMBOLS; i<encoder.symbols_number() && v.size()>1; i++ ){
			int k=1;
			int pk=0;
			int nk=2;
			while(k<v.size()){
				Token token = (Token)v.get(k); 
				if(token.type()==i){
					String name = machine.symbol_command(FunLexer.get(token.lexeme())).name();
					FunCommandCall[] args = new FunCommandCall[2];
					args[0]=(v.get(pk) instanceof Typed)?(FunCommandCall)apply((Typed)v.get(pk)):(FunCommandCall)v.get(pk);
					args[1]=(v.get(nk) instanceof Typed)?(FunCommandCall)apply((Typed)v.get(nk)):(FunCommandCall)v.get(nk);
					FunCommandCall c = new FunCommandCall(args[0], machine, name, args); 
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
		return (FunCommandCall)v.get(0);
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
		return new FunCommandDef(machine, (FunCommandCall)apply(v.get(0)), (FunCommandCall)apply(v.get(1)));
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
			case Constants.VARIABLE:
				RCToken t = (RCToken)rule;
				return new FunVariable(t, machine, FunLexer.get(t.lexeme()));
			//case FunLexer.VALUE: throw new LanguageException("Don't know to deal with values");
			case Constants.PRIM_VALUE: return prim((RCToken)rule);
			case Constants.COMMAND_EXP: return command_exp((TypedValue<Vector<Typed>>)rule);
			case Constants.COMMAND_DEF: return command_def((TypedValue<Vector<Typed>>)rule);
			case Constants.COMMAND_DEF_LIST: return command_def_list((TypedValue<Vector<Typed>>)rule);
			case Constants.COMMAND: return command((TypedValue<Vector<Typed>>)rule);
		}
		return null;
	}

}
