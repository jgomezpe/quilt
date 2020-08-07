package qm;

import funpl.gui.awt.ProgrammingFrame;
import nsgl.stream.Resource;
import nsgl.stream.loader.FromOS;
import nsgl.string.I18N;
import qm.awt.Render;

public class IDE {
	public static void main( String[] args ) {
		String lang = "language/spanish.i18n";
		if(args.length>0) lang = "language/"+args[0]+".i18n";
		Resource resource = new Resource();
		resource.add("local", new FromOS(""));
		try { I18N.set(resource.txt(lang)); }
		catch(Exception e) { e.printStackTrace(); }
		ProgrammingFrame frame = new ProgrammingFrame(new API(), "machine/basic.qmc", new Render());
		frame.setVisible(true);
	}
}
