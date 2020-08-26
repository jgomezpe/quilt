package funpl.semantic;

import nsgl.generic.hashmap.HashMap;
import nsgl.generic.keymap.KeyMap;
import funpl.util.FunConstants;
import nsgl.generic.array.Vector;

public class FunCommandCall extends FunCommand {
	protected String name;
	protected String ho_name;
	protected FunCommandCall[] args=null;
	protected boolean variable = false;

	public FunCommandCall( int pos, String src, FunMachine machine, String name, boolean variable ){
		this(pos, src, machine, name);
		this.variable = variable;
	}
	
	public FunCommandCall( int pos, String src, FunMachine machine, String name ){
		super(pos, src, machine);
		this.name = name;
		this.ho_name = name;
	}
	
	public FunCommandCall( int pos, String src, FunMachine machine, String name, FunCommandCall[] args ){
		this(pos, src, machine, name);
		this.args = args;
	}	

	public String name(){ return name; }
	public FunCommandCall[] args(){ return args; }
	
	public KeyMap<String, Object> match( KeyMap<String, Object> variables, Object... values ) throws Exception{
		ho_name = (String)variables.get(name); 

		if( ho_name == null ) ho_name = name;

		int arity = arity();
		if( arity == 0 ){
			Object obj=machine.execute(this.src, this.pos(), ho_name);
			if(obj==null || values.length!=1 || !obj.equals(values[0])) throw exception(FunConstants.argmismatch + values[0]);
			return variables;
		}
		if( values.length != arity ) throw exception(FunConstants.argnumbermismatch + values.length + "!=" + arity);
		Exception ex = null;
		Vector<Integer> index = new Vector<Integer>();
		for( int i=0; i<arity; i++ ) index.add(i);
//		int n = 0;
		int m = 0;
		while( index.size()>0 && (index.size()!=m)) { // || n!=variables.size())){
				m = index.size();
//				n = variables.size();
				int i=0; 
				while( i<index.size() ){
					int k;
					try{ k = index.get(i); }catch(Exception e){k=1;}
					String aname = args[k].name();
					if( args[k] instanceof FunVariable ){
						((FunVariable)args[k]).match(variables,values[k]);
						index.remove(i);
					}else{
						if( args[k] instanceof FunValue ){
							Object obj = args[k].execute(variables);
							if( obj==null || !obj.equals(values[k]) ) throw exception(FunConstants.argmismatch + values[k]);
							index.remove(i);
						}else{					
							try{
								FunCommand c = machine.primitive.get(aname);
								if(c != null ){
									int a = c.arity();
									Object[] toMatch = new Object[a];
									for( int j=0; j<a; j++ )
										try{ toMatch[j]=args[k].args[j].execute(variables); }
										catch(Exception x){ toMatch[j]=null; }
									c.setPos(args[k].pos);
									Object[] objs = c.reverse(values[k], toMatch);
									args[k].match(variables, objs);
								}else{
									Object obj = args[k].execute(variables);
									if( obj==null || !obj.equals(values[k]) ) throw args[k].exception(FunConstants.argmismatch + values[k]);
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

	public KeyMap<String, Object> match( Object... values ) throws Exception{ return match( new HashMap<String,Object>(), values ); }
	
	public Object execute( KeyMap<String,Object> variables ) throws Exception{
		ho_name = (String)variables.get(name); 
		if( ho_name == null ) ho_name = name;
		int a = arity();
		Object[] obj = new Object[a];
		for( int i=0; i<a; i++ ) obj[i] = args[i].execute(variables);
		return machine.execute(this.src, this.pos, ho_name, obj);
	}

	public void getVars(KeyMap<String,Object> vars) {
	    if(this instanceof FunVariable) {
		vars.set(name,name);
	    }else {
		if( args != null )
		    for( int i=0; i<args.length; i++ )
			args[i].getVars(vars);
	    }
	}
	
	public KeyMap<String,Object> getVars() {
	    HashMap<String, Object> vars = new HashMap<String, Object>();
	    getVars(vars);
	    return vars;
	}
	
	public Object execute( Object... value ) throws Exception{
		HashMap<String,Object> vars = (HashMap<String, Object>)getVars();
		if(vars.size()!=1) throw exception(FunConstants.argnumbermismatch);
		
		for(String k:vars.keys()) vars.set(k,value[0]);		
		return execute(vars); 
	}

	@Override
	public int arity() { return (args!=null)?args.length:0; }	
	
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append(name());
		int n = arity();
		if( n>0 ){
			sb.append(FunConstants.OPEN);
			sb.append(args[0]);
			for( int i=1; i<n;i++ ){
				sb.append(FunConstants.COMMA);			
				sb.append(args[i]);
			}
			sb.append(FunConstants.CLOSE);			
		}
		return sb.toString();
	}
}
