package fun_pl.semantic;

import unalcol.types.collection.Collection;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.keymap.ImmutableKeyMap;
import unalcol.util.Instance;

public abstract class FunMachineInstance<T> implements Instance<FunMachine> {
	public static final String MACHINE="machine";
	
	protected FunCommandInstance commands;
	protected FunValueInstance<T> values;
	
	public FunMachineInstance(){
		this.initCommands();
		this.initValues();
	}
	
	public void register(String tag, String type, Instance<T> instance ){ values.register(tag, type, instance); }
	
	public abstract void initCommands();
	public abstract void initValues();
	
	public abstract FunMachine init( ImmutableKeyMap<String, FunCommand> commands, ImmutableKeyMap<String, T> r );
	
	@Override
	public FunMachine load(Object[] args) {
		if( args.length<2 || args.length>3 || !MACHINE.equals(args[0]) ) return null;
		ImmutableKeyMap<String, FunCommand> c = commands.load((Object[])args[1]);
		ImmutableKeyMap<String, T> r=null;
		if(args.length==3 )	r = values.load((Object[])args[2]);
		return init(c, r);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] store(FunMachine obj) {
		Collection<String> keys = obj.values();
		HTKeyMap<String,T> r = new HTKeyMap<String,T>();
		if( keys!=null ) for( String val:keys ) try{ r.set(val, (T)obj.value(val)); }catch(Exception e){}
		HTKeyMap<String,FunCommand> c = new HTKeyMap<String,FunCommand>();
		for( String name:obj.primitives() ) try{ c.set(name, obj.primitive(name)); }catch(Exception e){}
		return new Object[]{MACHINE,commands.store(c),values.store(r)};
	}
}