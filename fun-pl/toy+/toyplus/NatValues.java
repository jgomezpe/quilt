package toyplus;

import funpl.semantic.FunValueInterpreter;

public class NatValues implements FunValueInterpreter{

    @Override
    public Object get(String value) {
	return Integer.parseInt(value);
    }

    @Override
    public boolean valid(String value) {
	try{
	    Integer.parseInt(value);
	    return true;
	}catch(NumberFormatException e) {
	    return false;
	}
    }

    @Override
    public String description() {
	return "·Natural numbers·";
    }

    @Override
    public String regex() { return "\\d+"; }
}