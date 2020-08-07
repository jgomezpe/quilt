package toyplus;

import funpl.semantic.FunAssignment;

public class Assignment implements FunAssignment{

    @Override
    public boolean check(String variable, Object obj) {
	return obj instanceof Integer;
    }

}
