package qm;

import qm.quilt.Quilt;

public class Assignment implements funpl.semantic.FunAssignment {
	@Override
	public boolean check(String arg, Object obj) {
		return obj instanceof Quilt;
	}
}
