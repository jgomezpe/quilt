package fun_pl.semantic;

import fun_pl.syntax.FunEncoder;
import unalcol.io.Position;
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
		if( arity == 0 ) throw new Exception("Command call cannot be matched with some parameters...");
		if( values.length != arity() ) throw new Exception("Mistmatch number of parameters...");
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
				if( args[k].variable ){
					Object obj = variables.get(aname);
					if( obj == null ) variables.set(aname,values[k]);
					else if( !obj.equals(values[k]) ) throw new Exception("Mismatch variable..");
					index.remove(i);
				}else{
					try{
						if(aname.equals(machine.left_link())){
							Object[] objs = machine.left_unlink(values[k]);
							args[k].match(variables,objs);
						}else if( aname.equals(machine.right_link()) ){
							Object[] objs = machine.right_unlink(values[k]);
							args[k].match(variables,objs);
						}else{
							Object obj = args[i].execute(variables);
							if( obj==null || !obj.equals(values[index.get(i)]) )  throw new Exception("Mismatch parameter..");
						}
						index.remove(i);
					}catch( Exception e ){
						ex = e;
						i++;
					}
				}
			}	
		}
		if( index.size() > 0 ) throw ex;
		return variables; 
	}

	public KeyMap<String, Object> match( Object... values ) throws Exception{ return match( new HTKeyMap<String,Object>(), values ); }
	
	public Object execute( KeyMap<String,Object> variables ) throws Exception{
		Object[] obj = new Object[args.length];
		for( int i=0; i<args.length; i++ ) obj[i] = args[i].execute(variables);
		return machine.execute(name, obj);
	}

	public Object execute( Object... value ) throws Exception{ return machine.execute( name, value ); }

	public String toString( FunEncoder encoder ){
		StringBuilder sb = new StringBuilder();
		sb.append(I18N.get(name));
		sb.append(name);
		if( args!=null && args.length>0 ){
			sb.append(encoder.symbol(FunEncoder.OPEN));
			sb.append(args[0]);
			for( int i=1; i<args.length; i++ ){
				sb.append(encoder.symbol(FunEncoder.COMMA));
				sb.append(args[i]);
			}
			sb.append(encoder.symbol(FunEncoder.CLOSE));
		}
		return sb.toString();
	}

	@Override
	public int arity() { return (args!=null)?args.length:0; }	
}
