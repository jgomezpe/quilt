package toyplus;

import funpl.semantic.FunCommand;
import funpl.semantic.FunMachine;

public class Decrement extends FunCommand{

    public Decrement(int pos, String src, FunMachine machine) {
	super(pos, src, machine);
    }

    public Decrement(int pos, String src) {
	super(pos, src, null);
    }

    @Override
    public Object execute(Object... value) throws Exception {
	int s=(Integer)value[0];
	return s-1;
    }

    @Override
    public int arity() {
	return 1;
    }

    @Override
    public String name() {
	return "¬";
    }

	@Override
	public Object[] reverse(Object obj, Object[] toMatch) throws Exception {
		Integer i = (Integer)obj;
		if( toMatch[0]==null ){
			if( i<=0 ) throw exception("·Invalid operation· " + name() + "(" + i +")");
			return new Integer[]{i-1};
		}
		Integer j=(Integer)toMatch[0];
		if(j+1!=i) throw exception("·Mismatch· " + i);
		return new Integer[]{j};
	}
    
}
