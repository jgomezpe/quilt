package fun_pl.semantic;

import fun_pl.syntax.FunEncoder;
import fun_pl.util.FunConstants;
import unalcol.io.Position;
import unalcol.language.LanguageException;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.keymap.KeyMap;
import unalcol.types.collection.vector.Vector;
import unalcol.util.I18N;

public class FunCommandCall extends FunCommand {
	protected String name;
	protected String ho_name;
	protected FunCommandCall[] args=null;
	protected boolean variable = false;

	public FunCommandCall( Position pos, FunMachine machine, String name, boolean variable ){
		this(pos, machine, name);
		this.variable = variable;
	}
	
	public FunCommandCall( Position pos, FunMachine machine, String name ){
		super(pos, machine);
		this.name = name;
		this.ho_name = name;
	}
	
	public FunCommandCall( Position pos, FunMachine machine, String name, FunCommandCall[] args ){
		this(pos, machine, name);
		this.args = args;
	}	

	public String name(){ return name; }
	public FunCommandCall[] args(){ return args; }
	
	public KeyMap<String, Object> match( KeyMap<String, Object> variables, Object... values ) throws LanguageException{
		String _ho_name = (String)variables.get(name);
		ho_name = (_ho_name!=null)?_ho_name:name; 
		int arity = arity();
		if( arity == 0 ){
			Object obj=machine.execute(this, ho_name);
			if(obj==null || values.length!=1 || !obj.equals(values[0])) throw exception(FunConstants.argmismatch, values[0]);
			return variables;
		}
		if( values.length != arity ) throw exception(FunConstants.argnumbermismatch,values.length,arity);
		LanguageException ex = null;
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
						if( obj==null || !obj.equals(values[k]) ) throw exception(FunConstants.argmismatch,values[k]);
						index.remove(i);
					}else{					
						try{
							FunSymbolCommand c = machine.symbol_command(aname);
							if(c != null ){
								Object[] toMatch = new Object[]{null,null};
								try{ toMatch[0]=args[k].args[0].execute(variables); }catch(Exception x){}
								try{ toMatch[1]=args[k].args[1].execute(variables); }catch(Exception x){}
								c.init(args[k]);
								Object[] objs = c.reverse(values[k], toMatch,args[k].args);
								args[k].match(variables, objs);
							}else{
								Object obj = args[k].execute(variables);
								if( obj==null || !obj.equals(values[k]) ) throw args[k].exception(FunConstants.argmismatch, values[k]);
							}
							index.remove(i);
						}catch( LanguageException e ){
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

	public KeyMap<String, Object> match( Object... values ) throws LanguageException{ return match( new HTKeyMap<String,Object>(), values ); }
	
	public Object execute( KeyMap<String,Object> variables ) throws LanguageException{
		String _ho_name = (String)variables.get(name);
		ho_name = (_ho_name!=null)?_ho_name:name; 
		int a = arity();
		Object[] obj = new Object[a];
		for( int i=0; i<a; i++ ) obj[i] = args[i].execute(variables);
		return machine.execute(this, ho_name, obj);
	}

	public Object execute( Object... value ) throws LanguageException{
		return machine.execute( this, ho_name, value ); 
	}

	public String toString( FunEncoder encoder ){
		StringBuilder sb = new StringBuilder();
		sb.append(I18N.get(name));
		sb.append(name);
		if( args!=null && args.length>0 ){
			sb.append(encoder.symbol(FunConstants.OPEN));
			sb.append(args[0]);
			for( int i=1; i<args.length; i++ ){
				sb.append(encoder.symbol(FunConstants.COMMA));
				sb.append(args[i]);
			}
			sb.append(encoder.symbol(FunConstants.CLOSE));
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
			sb.append(FunEncoder.get_symbol(FunConstants.OPEN));
			sb.append(args[0]);
			for( int i=1; i<n;i++ ){
				sb.append(FunEncoder.get_symbol(FunConstants.COMMA));			
				sb.append(args[i]);
			}
			sb.append(FunEncoder.get_symbol(FunConstants.CLOSE));			
		}
		return sb.toString();
	}
}
