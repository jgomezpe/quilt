package quilt.factory;

import quilt.Rotate;
import quilt.Sew;
import quilt.operation.Command;

public class QuiltCommandFactory {
	public static Command[] little(){ return new Command[]{ new Rotate(), new Sew() }; }
	public static Command[] minimal(){ return new Command[]{ new Sew() }; }
}
