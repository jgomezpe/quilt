package quilt.basic;

import quilt.Language;
import quilt.Quilt;
import quilt.QuiltMachine;
import quilt.Remnant;
import quilt.language.Command;

public class Sew extends Command{
	public Sew() {
		super(BasicQuiltMachine.SEW, new String[]{"X", "Y"});
	}

	public Remnant execute( Remnant left, Remnant right ){
		if( left.rows() == right.rows() ){
			return new Quilt( left, right );
		}
		return null;
	}
	
	@Override
	public Remnant execute(QuiltMachine machine, Remnant[] value)  throws Exception{
		if( value.length == args.length ){
			return execute( value[0], value[1] );
		}
		throw new Exception(machine.message(Language.ARGS)+" "+name());
	}
}