package fun_pl.semantic;

import fun_pl.syntax.FunEncoder;
import fun_pl.util.Constants;
import unalcol.io.Position;
import unalcol.language.LanguageException;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.keymap.KeyMap;
import unalcol.types.collection.vector.Vector;
import unalcol.util.I18N;

public class FunCommandCall extends FunCommand {
	protected String name;
	protected FunCommandCall[] args=null;
	protected boolean variable = false;

	public FunCommandCall( Position pos, FunMachine machine, String name, boolean variable ){
		this(pos, machine, name);
		this.variable = variable;
	}
	
	public FunCommandCall( Position pos, FunMachine machine, String name ){
		super(pos, machine);
		this.name = name;
	}
	
	public FunCommandCall( Position pos, FunMachine machine, String name, FunCommandCall[] args ){
		this(pos, machine, name);
		this.args = args;
	}	

	public String name(){ return name; }
	public FunCommandCall[] args(){ return args; }
	
	public KeyMap<String, Object> match( KeyMap<String, Object> variables, Object... values ) throws Exception{
		int arity = arity();
		if( arity == 0 ){
			Object obj=machine.execute(name);
			if(obj==null || values.length!=1 || !obj.equals(values[0])) throw new LanguageException(Constants.argmismatch,name(),row()+1,column()+1, values[0].toString());
			return variables;
		}
		if( values.length != arity ) throw new LanguageException(Constants.argnumbermismatch,name(),row(),column(),values.length,arity);
		Exception ex = null;
		Vector<Integer> index = new Vector<Integer>();
		for( int i=0; i<arity; i++ ) index.add(i);
		int n = 0;
		int m = 0;
		while( index.size()>0 && (index.size()!=m || n!=variables.size())){
			m = index.size();
			n = variables.size();
			int i=0; 
			while( i<index.size() ){
				int k = index.get(i);
				String aname = args[k].name();
				if( args[k] instanceof FunVariable ){
					((FunVariable)args[k]).match(variables,values[k]);
					index.remove(i);
				}else{
					if( args[k] instanceof FunValue ){
						Object obj = args[k].execute(variables);
						if( obj==null || !obj.equals(values[k]) ) throw new LanguageException(Constants.argmismatch,name(),row()+1,column()+1, values[k].toString());
						index.remove(i);
					}else{					
						try{
							FunCommand c = machine.primitive(aname);
							if(c instanceof FunSymbolCommand){
								Object[] toMatch = new Object[]{null,null};
								try{ toMatch[0]=args[k].args[0].execute(variables); }catch(Exception x){}
								try{ toMatch[1]=args[k].args[1].execute(variables); }catch(Exception x){}
								Object[] objs = ((FunSymbolCommand)c).reverse(values[k], toMatch);
								args[k].match(variables, objs);
							}else{
								Object obj = args[k].execute(variables);
								if( obj==null || !obj.equals(values[k]) )  throw new LanguageException(Constants.argmismatch,name(),row()+1,column()+1, values[k].toString());
							}
							index.remove(i);
						}catch( Exception e ){
							ex = e;
							i++;
						}
					}
				}
			}	
		}
		if( index.size() > 0 ) throw ex;
		return variables; 
	}

	public KeyMap<String, Object> match( Object... values ) throws Exception{ return match( new HTKeyMap<String,Object>(), values ); }
	
	public Object execute( KeyMap<String,Object> variables ) throws Exception{
		int a = arity();
		Object[] obj = new Object[a];
		for( int i=0; i<a; i++ ) obj[i] = args[i].execute(variables);
		return machine.execute(name, obj);
	}

	public Object execute( Object... value ) throws Exception{ return machine.execute( name, value ); }

	public String toString( FunEncoder encoder ){
		StringBuilder sb = new StringBuilder();
		sb.append(I18N.get(name));
		sb.append(name);
		if( args!=null && args.length>0 ){
			sb.append(encoder.symbol(Constants.OPEN));
			sb.append(args[0]);
			for( int i=1; i<args.length; i++ ){
				sb.append(encoder.symbol(Constants.COMMA));
				sb.append(args[i]);
			}
			sb.append(encoder.symbol(Constants.CLOSE));
		}
		return sb.toString();
	}

	@Override
	public int arity() { return (args!=null)?args.length:0; }	
	
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append(name());
		int n = arity();
		if( n>0 ){
			sb.append(FunEncoder.get_symbol(Constants.OPEN));
			sb.append(args[0]);
			for( int i=1; i<n;i++ ){
				sb.append(FunEncoder.get_symbol(Constants.COMMA));			
				sb.append(args[i]);
			}
			sb.append(FunEncoder.get_symbol(Constants.CLOSE));			
		}
		return sb.toString();
	}
}
