package quilt;

import unalcol.gui.util.Factory;
import unalcol.gui.util.Instance;

public class QuiltInstance implements Instance<Remnant>{
	public static final String QUILT="quilt";
	protected Factory<Remnant> remnants;
	
	public QuiltInstance(Factory<Remnant> remnants) {
		this.remnants = remnants;
	}
	
	@Override
	public Remnant load(Object[] args) {
		if( args.length==0 || !QUILT.equals(args[0]) ) return null;
		MinRemnant[][] grid = new MinRemnant[args.length-1][];
		for( int i=0; i<grid.length; i++){
			Object[] line = (Object[])args[i+1];
			grid[i] = new MinRemnant[line.length];
			for( int j=0; j<grid[i].length; j++){
				grid[i][j] = (MinRemnant)remnants.load((Object[])line[j]);
			}
		}
		return new Quilt(grid);
	}

	@Override
	public Object[] store(Remnant obj) {
		if(!( obj instanceof Quilt )) return null;
		Quilt q = (Quilt)obj;
		Object[] lines = new Object[q.rows()+1];
		lines[0] = QUILT;
		for( int i=0; i<q.rows();i++ ){
			Object[] line = new Object[q.columns()];
			for( int j=0; j<q.columns(); j++ ) line[j] = remnants.store(q.get(i, j));
			lines[i+1] = line;
		}
		return lines;
	}	
}