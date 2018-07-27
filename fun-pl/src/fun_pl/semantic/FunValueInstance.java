package fun_pl.semantic;

import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.keymap.ImmutableKeyMap;
import unalcol.types.collection.keymap.KeyMap;
import unalcol.util.Factory;
import unalcol.util.Instance;

public class FunValueInstance<T>  implements Instance<ImmutableKeyMap<String,T>> {
	protected String VALUES="values";

	protected Factory<T> values = new Factory<T>();

	public FunValueInstance(String VALUES) {
		this.VALUES = VALUES;
	}
	
	public void register(String tag, String type, Instance<T> instance ){
		values.register(tag, type, instance);
	}

	
	@Override
	public ImmutableKeyMap<String, T> load(Object[] args) {
		if( args.length<2 || !VALUES.equals(args[0]) ) return null;
		KeyMap<String, T> r = new HTKeyMap<String,T>();
		for( int i=1; i<args.length; i++){
			Object[] pair = (Object[])args[i];
			r.set((String)pair[0], values.load((Object[])pair[1]));
		}
		return r;
	}

	@Override
	public Object[] store(ImmutableKeyMap<String, T> remnants) {
		if( remnants==null ) return new Object[]{VALUES};
		Object[] obj = new Object[remnants.size()+1];
		obj[0] = VALUES;
		int i=1;
		for( T q:remnants ) obj[i++]=this.values.store(q); 
		return null;
	}
	
	public Factory<T> factory(){ return values; }
}
