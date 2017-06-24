package quilt.factory;

import quilt.operation.Command;
import quilt.operation.Rotate;
import quilt.operation.Sew;

public class QuiltCommandFactory {
	public static Command[] little(){ return new Command[]{ new Rotate(), new Sew() }; }
	public static Command[] minimal(){ return new Command[]{ new Sew() }; }
}
