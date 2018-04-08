package quilt.factory;

import quilt.NilQuilt;
import quilt.Quilt;
import unalcol.util.Instance;

public class NilQuiltInstance implements Instance<Quilt> {
	public static final String NIL="nil";

	@Override
	public Quilt load(Object[] args) {
		if( args.length!=1 || !NIL.equals(args[0]) ) return null;
		return new NilQuilt();
	}

	@Override
	public Object[] store(Quilt obj) {
		if( !(obj instanceof NilQuilt) ) return null;
		return new Object[]{NIL};
	}
}