package quilt.factory;

import java.util.Hashtable;

import quilt.Quilt;
import quilt.QuiltInstance;
import quilt.QuiltMachine;
import quilt.Remnant;
import quilt.operation.Command;
import quilt.operation.Rotate;
import quilt.operation.Sew;
import quilt.remnant.FilledRemnant;
import quilt.remnant.FilledRemnantInstance;
import quilt.remnant.StripsRemnant;
import quilt.remnant.StripsRemnantInstance;
import quilt.syntax.QuiltMachineParser;
import unalcol.gui.editor.ErrorManager;
import unalcol.gui.util.Factory;
import unalcol.gui.util.Instance;

public class QuiltMachineInstance implements Instance<QuiltMachine> {
	public static final String MACHINE="machine";
	
	protected QuiltCommandInstance commands = new QuiltCommandInstance();
	protected Factory<Remnant> remnants = new Factory<Remnant>();
	protected ErrorManager language;
	
	public QuiltMachineInstance(ErrorManager language) {
		this.language = language;
		commands.register(new Sew());
		commands.register(new Rotate());
		
		remnants.register(StripsRemnantInstance.STRIPS, StripsRemnant.class.getName(), new StripsRemnantInstance());	
		remnants.register(QuiltInstance.QUILT, Quilt.class.getName(), new QuiltInstance(remnants));			
		remnants.register(FilledRemnantInstance.FILLED, FilledRemnant.class.getName(), new FilledRemnantInstance());
	}
	
	@Override
	public QuiltMachine load(Object[] args) {
		if( args.length<3 || args.length>4 || !MACHINE.equals(args[0]) ) return null;
		Command[] c = commands.load((Object[])args[1]);
		Object[] robj = (Object[])args[2];
		Hashtable<String, Remnant> r = new Hashtable<String,Remnant>();
		for( int i=0; i<robj.length; i++){
			Object[] pair = (Object[])robj[i];
			r.put((String)pair[0], remnants.load((Object[])pair[1]));
		}
		return new QuiltMachine(c, r, new QuiltMachineParser(), language);
	}

	@Override
	public Object[] store(QuiltMachine obj) {
		String[] keys = obj.remnants();
		Object[] r = new Object[keys.length];
		for( int i=0; i<r.length; i++ ) r[i] = new Object[]{keys[i],obj.remnant(keys[i])};
		return new Object[]{MACHINE,commands.store(obj.primitives()),r};
	}
}