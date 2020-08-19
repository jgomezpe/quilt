package qm;

import funpl.gui.awt.ProgrammingFrame;
import qm.awt.Render;

public class IDE {
	public static void main( String[] args ) {
		String lang = (args.length>0)?args[0]:"spanish";
		ProgrammingFrame frame = new ProgrammingFrame(new API(), "machine/basic.qmc", new Render(), IDE.class.getClassLoader(), lang);
		frame.setVisible(true);
	}
}
