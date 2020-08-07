package funpl.semantic;

import java.io.IOException;

import funpl.util.FunConstants;
import nsgl.language.Typed;
import nsgl.language.TypedValue;
import nsgl.language.Token;
import nsgl.language.Meaner;
import nsgl.generic.array.Vector;

public class FunMeaner implements Meaner<FunCommand>{
	protected FunMachine machine;
	
	public FunMeaner(){}
	
	public FunMeaner( FunMachine machine ){
		this.machine = machine;
	}
	
	public void setMachine( FunMachine machine ){ this.machine = machine; }
	
	@SuppressWarnings("unchecked")
	protected FunCommandCall command(TypedValue<Vector<Typed>> t) throws IOException{
		Vector<Typed> v = t.value();
		FunCommandCall[] args = new FunCommandCall[v.size()-1];
		int i=0;
		int j=-1;
		String name = null;
		int pos = 0;
		String src = "";
		for( Typed xt : v ){
			if( i>0 ) args[j] = (FunCommandCall)apply((TypedValue<Vector<Typed>>)xt);
			else{
				name = (String)((Token)xt).value();
				pos =  ((Token)xt).location();
				src = ((Token)xt).owner();
			}
			i++;
			j++;
		}
		return new FunCommandCall(pos, src, machine, name, args);
	}
	
	protected Object get( Vector<?> v, int i ){
		try{ return v.get(i); }catch(Exception e){ return null; }
	}
		
	protected FunCommand command_def(TypedValue<Vector<Typed>> t) throws IOException{
		Vector<Typed> v = t.value();
		return new FunCommandDef(machine, (FunCommandCall)apply((Typed)get(v,0)), (FunCommandCall)apply((Typed)get(v,1)));
	}

	protected FunProgram command_def_list(TypedValue<Vector<Typed>> t) throws IOException{
		Vector<Typed> v = t.value();
		Vector<FunCommandDef> defs = new Vector<FunCommandDef>();
		for( Typed s:v ) defs.add((FunCommandDef)apply(s));
		return new FunProgram(machine, defs);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public FunCommand apply(Typed rule) throws IOException {
		switch( rule.type() ){
			case FunConstants.VARIABLE: return new FunVariable(((Token)rule).location(), ((Token)rule).owner(), machine, (String)((Token)rule).value());
			case FunConstants.VALUE: return new FunValue(((Token)rule).location(), ((Token)rule).owner(), machine, (String)((Token)rule).value());
			case FunConstants.DEFINITION: return command_def((TypedValue<Vector<Typed>>)rule);
			case FunConstants.DEF_LIST: return command_def_list((TypedValue<Vector<Typed>>)rule);
			case FunConstants.COMMAND: return command((TypedValue<Vector<Typed>>)rule);
		}
		return null;
	}

}
