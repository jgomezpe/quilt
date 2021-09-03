package qm;

import funpl.FunAPI;
import aplikigo.gui.canvas.CanvasConstants;
import speco.jxon.JXON;
import qm.operation.Rotate;
import qm.operation.Sew;
import qm.quilt.Store;

public class API extends FunAPI{
	public static final String VALUE = "value";
	public static final String TYPE = "type";
	public static final String REDUCTIONS = "reductions";
    @Override
    public void config(JXON jxon) {
    	super.config(jxon);
    	JXON values = jxon.object(VALUE);
    	String remnant = values.string(TYPE);
    	Object[] id = values.array(CanvasConstants.COMMANDS);
    	String[] v = new String[id.length];
    	for( int i=0; i<v.length; i++ ) v[i] = (String)id[i];
    	Object[] red = values.array(REDUCTIONS);
    	int[] r = null;
    	if( red != null ) {
    		r = new int[red.length];
    		for( int i=0; i<red.length; i++ ) r[i] = (Integer)red[i];
    	}
    	value = new Store(remnant, v, r);
    	assignment = new Assignment();
    	Object[] opers = jxon.array(CanvasConstants.COMMANDS);
    	for( Object obj:opers ) {
    	    String o = (String)obj;
    	    if( o.equals("@") ) addOperator(new Rotate(), 3);
    	    else if( o.equals("|" ) ) addOperator(new Sew(), 1);
    	}
    }
}
