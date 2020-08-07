package toyplus;

import funpl.semantic.FunCommand;
import funpl.semantic.FunMachine;

public class Plus extends FunCommand{
    public Plus(int pos, String src) {
	super(pos, src, null);
    }

    public Plus(int pos, String src, FunMachine machine) {
	super(pos, src, machine);
    }

    @Override
    public Object execute(Object... value) throws Exception {
	int s=(Integer)value[0];
	for( int i=1; i<value.length; i++) s += (Integer)value[i];
	return s;
    }

    @Override
    public int arity() {
	return 2;
    }

    @Override
    public String name() {
	return "+";
    }
   
    public Object[] reverse(Object value, Object[] original ) throws Exception{
	Integer i = (Integer)value;
	if( original[0]==null && original[1]==null ){
		if( i<= 0 ) throw exception("·Invalid reversed operation· " + name() + "(" + i + ", 1)");
		return new Integer[]{i-1,1};
	}
	if( original[0]==null){
		Integer j=(Integer)original[1];
		if(i-j<0) throw exception("·Invalid reversed operation· " + name() + "(" + i + "," + j + ")");
		return new Integer[]{i-j,j};
	}
	if( original[1]==null){
		Integer j=(Integer)original[0];
		if(i-j<0) throw exception("·Invalid reversed operation· " + name() + "(" + j + "," + i + ")");
		return new Integer[]{j,i-j};
	}
	Integer j=(Integer)original[0];
	Integer k=(Integer)original[1];
	if(j+k!=i) throw exception("·Argument mismatch· " + name() + "( " + j + "," +k + ") =" + i);
	return new Integer[]{j,k};
    }
}