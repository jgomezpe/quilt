package qm;

import funpl.gui.awt.ProgrammingFrame;
import qm.awt.QuiltRender;

public class IDE {
	public static void main( String[] args ) {
		String lang = (args.length>0)?args[0]:"spanish";
		ProgrammingFrame frame = new ProgrammingFrame(new API(), "machine/basic.qmc", new QuiltRender(), IDE.class.getClassLoader(), lang);
		frame.setVisible(true);
	}
}
