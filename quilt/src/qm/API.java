package qm;

import funpl.FunAPI;
import nsgl.gui.canvas.Util;
import nsgl.json.JSON;
import qm.operation.Rotate;
import qm.operation.Sew;
import qm.quilt.Store;

public class API extends FunAPI{
	public static final String VALUE = "value";
	public static final String TYPE = "type";
    @Override
    public void config(JSON jxon) {
    	super.config(jxon);
    	JSON values = jxon.getJSON(VALUE);
    	String remnant = values.getString(TYPE);
    	Object[] id = values.getArray(Util.COMMANDS);
    	String[] v = new String[id.length];
    	for( int i=0; i<v.length; i++ ) v[i] = (String)id[i];
    	value = new Store(remnant, v);
    	assignment = new Assignment();
    	Object[] opers = jxon.getArray(Util.COMMANDS);
    	for( Object obj:opers ) {
    	    String o = (String)obj;
    	    if( o.equals("@") ) addOperator(new Rotate(), 3);
    	    else if( o.equals("|" ) ) addOperator(new Sew(), 1);
    	}
    }
}
