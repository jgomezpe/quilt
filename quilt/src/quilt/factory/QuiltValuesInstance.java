package quilt.factory;

import quilt.Quilt;
import unalcol.util.Factory;
import unalcol.util.Instance;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.keymap.ImmutableKeyMap;
import unalcol.types.collection.keymap.KeyMap;

public class QuiltValuesInstance  implements Instance<ImmutableKeyMap<String,Quilt>> {
	public static final String REMNANTS="remnants";

	protected Factory<Quilt> remnants = new Factory<Quilt>();

	public QuiltValuesInstance() {}
	
	public void register(String tag, String type, Instance<Quilt> instance ){
		remnants.register(tag, type, instance);
	}

	
	@Override
	public ImmutableKeyMap<String, Quilt> load(Object[] args) {
		if( args.length<2 || !REMNANTS.equals(args[0]) ) return null;
		KeyMap<String, Quilt> r = new HTKeyMap<String,Quilt>();
		for( int i=1; i<args.length; i++){
			Object[] pair = (Object[])args[i];
			r.set((String)pair[0], remnants.load((Object[])pair[1]));
		}
		return r;
	}

	@Override
	public Object[] store(ImmutableKeyMap<String, Quilt> remnants) {
		if( remnants==null ) return new Object[]{REMNANTS};
		Object[] obj = new Object[remnants.size()+1];
		obj[0] = REMNANTS;
		int i=1;
		for( Quilt q:remnants ) obj[i++]=this.remnants.store(q); 
		return null;
	}
	
	public Factory<Quilt> factory(){ return remnants; }
}