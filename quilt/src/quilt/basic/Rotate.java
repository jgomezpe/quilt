package quilt.basic;

import quilt.Language;
import quilt.MinRemnant;
import quilt.Quilt;
import quilt.QuiltMachine;
import quilt.Remnant;
import quilt.StripsRemnant;
import quilt.language.Command;

public class Rotate extends Command{
	public Rotate() {
		super(BasicQuiltMachine.ROTATE, new String[]{"X"});
	}

	public Remnant execute(Remnant value){
		if( value instanceof StripsRemnant ){
			StripsRemnant r = (StripsRemnant)value;
			int[][] strips = r.strips().clone();
			for( int i=0; i<strips.length; i++ ){
				int x = strips[i][0];
				int y = strips[i][1];
				strips[i][0] = y;
				strips[i][1] = 100-x;
				x = strips[i][2];
				y = strips[i][3];
				strips[i][2] = y;
				strips[i][3] = 100-x;
			}
			return new StripsRemnant(r.color(), strips);
		}else{
			if( value instanceof NaturalNumberRemnant ){
				return value;
			}else{	
				MinRemnant[][] r = new MinRemnant[value.columns()][value.rows()];
				for( int i=0; i<r.length; i++ ){
					for( int j=0; j<r[0].length; j++ ){
						r[i][j] = (MinRemnant)execute(value.get(j, i));
					}
				}
				return new Quilt(r);
			}
		}		
	}
	
	@Override
	public Remnant execute(QuiltMachine machine, Remnant[] value) throws Exception{
		if( value.length == args.length ){
			return execute(value[0]);
		}
		throw new Exception(machine.message(Language.ARGS)+" "+name());
	}
}