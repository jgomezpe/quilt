package fun_pl.semantic;


import unalcol.json.Factory;
import unalcol.json.JSON;
import unalcol.json.JSON2Instance;
import unalcol.types.collection.Collection;
import unalcol.types.collection.keymap.HashMap;
import unalcol.types.collection.keymap.ImmutableKeyMap;
import unalcol.types.collection.keymap.KeyMap;
import unalcol.types.collection.vector.Vector;
import unalcol.types.object.Named;

public abstract class FunMachineInstance<T> implements JSON2Instance<FunMachine> {
	public static final String MACHINE="machine";
	public static final String COMMANDS="commands";
	public static final String VALUES="values";
	public static final String SYMBOL="symbol";
	
	protected HashMap<String, FunSymbolCommand> primitives = new HashMap<String,FunSymbolCommand>();
	protected Factory<T> factory = new Factory<T>();
	
	public FunMachineInstance(){
		this.initCommands();
		this.initValues();
	}
	
	public abstract void initCommands();
	public abstract void initValues();
	
	public abstract FunMachine init( ImmutableKeyMap<String, FunSymbolCommand> commands, String symbol );
	
	protected KeyMap<String, FunSymbolCommand> commands(JSON json){
		KeyMap<String, FunSymbolCommand> commands = new HashMap<String,FunSymbolCommand>();
		@SuppressWarnings("unchecked")
		Collection<Object> v = (Collection<Object>)json.get(COMMANDS);
		for( Object o:v ){
			String c = (String)o;
			commands.set( c, this.primitives.get(c) ); 
		}
		return commands;
	}
	
	protected ImmutableKeyMap<String, T> values(JSON json){
		HashMap<String, T> values = new HashMap<String,T>();
		@SuppressWarnings("unchecked")
		Collection<Object> v = (Collection<Object>)json.get(VALUES);
		for( Object o:v ){
			T value = factory.load((JSON)o); 
			values.set(((Named)value).id(), value);
		}
		return values;
	}
	
	@Override
	public FunMachine load(JSON json){
		KeyMap<String, FunSymbolCommand> primitives = commands(json);
		String symbol = (String)json.get(SYMBOL);
		if( symbol==null ) for( String s:primitives.keys() ) symbol=s;
		FunMachine machine = init(primitives, symbol);
		machine.setValues(values(json));
		return machine; 
	}

	@Override
	public JSON store(FunMachine obj) {
		JSON json = new JSON();
		Vector<Object> c = new Vector<Object>();
		for( String name:obj.primitives() ) c.add(name);
		json.set(COMMANDS, c);
		Vector<Object> v = new Vector<Object>();
		for( String name:obj.values() ) v.add(name);
		json.set(VALUES, v);
		if( primitives.size()>1 ) json.set(SYMBOL, obj.symbol().name());
		return json;
	}
}