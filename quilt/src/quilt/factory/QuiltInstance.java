package quilt.factory;

import quilt.MatrixQuilt;
import quilt.NilQuilt;
import quilt.Quilt;
import quilt.Remnant;
import unalcol.json.JSON;
import unalcol.json.JSON2Instance;
import unalcol.types.collection.vector.Vector;

public class QuiltInstance implements JSON2Instance<Quilt>{
	public static final String QUILT="quilt";
	public static final String NIL="nil";
	public static final String REMNANTS="remnants";
	
	protected RemnantInstance rinstance = new RemnantInstance(false);
	
	public QuiltInstance(boolean register){ rinstance = new RemnantInstance(register); }
	
	@Override
	public Quilt load(JSON json) {
		if( json.size()==0 ) return new NilQuilt();
		@SuppressWarnings("unchecked")
		Vector<Object> rows = (Vector<Object>)json.get(REMNANTS);
		if(rows==null) return rinstance.load(json);
		else if( rows.size()==0 ) return new NilQuilt();  
		Remnant[][] matrix = new Remnant[rows.size()][];
		for( int i=0; i<matrix.length; i++ ){
			@SuppressWarnings("unchecked")
			Vector<Object> row = (Vector<Object>)rows.get(i);
			matrix[i] = new Remnant[row.size()];
			for( int j=0; j<matrix[i].length; j++ ) matrix[i][j] = rinstance.load((JSON)row.get(j));
		}
		if( matrix.length==1 && matrix[0].length==1) return matrix[0][0];
		else return new MatrixQuilt(matrix);
	}

	@Override
	public JSON store(Quilt quilt) {
		if( quilt instanceof Remnant ) return rinstance.store((Remnant)quilt);
		if( quilt instanceof NilQuilt ) return new JSON();
		MatrixQuilt q = (MatrixQuilt)quilt;
		Vector<Object> rows = new Vector<Object>();
		for( int i=0; i<q.rows();i++ ){
			Vector<Object> row = new Vector<Object>();
			for( int j=0; j<q.columns(); j++ ) row.add(rinstance.store(q.get(i, j)));
			rows.add(row);
		}
		JSON json = new JSON();
		json.set(REMNANTS, rows);
		return json;
	}	
}