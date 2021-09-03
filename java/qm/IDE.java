package qm;

import funpl.gui.awt.ProgrammingFrame;
import funpl.util.FunConstants;
import qm.awt.QuiltTupleRender;

public class IDE {
	public static void main( String[] args ) {
		String lang = (args.length>0)?args[0]:"spanish";
		ProgrammingFrame frame = new ProgrammingFrame(new API(), FunConstants.machine + "basic.qmc", new QuiltTupleRender(), lang);
		frame.setVisible(true);
	}
}
