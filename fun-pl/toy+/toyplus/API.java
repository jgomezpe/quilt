package toyplus;

import funpl.FunAPI;
import nsgl.gui.paint.Command;
import nsgl.json.JSON;

public class API extends FunAPI{
    @Override
    public void config(JSON jxon) {
	super.config(jxon);
	canStartWithNumber = false;
	value = new NatValues();
	this.setValue(value.regex(), value);
	this.setAssignment(new Assignment());
	Object[] opers = jxon.getArray(Command.COMMANDS);
	for( Object obj:opers ) {
	    String o = (String)obj;
	    if( o.equals("¬") ) addOperator(new Decrement(0, ""), 3);
	    else if( o.equals("+" ) ) addOperator(new Plus(0, ""), 1);
	}
    }
}