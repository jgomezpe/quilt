package toyplus;

import funpl.gui.awt.ProgrammingFrame;
import nsgl.string.awt.Render;

public class IDE {
	public static void main( String[] args ) {
		String lang = (args.length>0)?args[0]:"spanish";
		ProgrammingFrame frame = 
			new ProgrammingFrame(new API(), "machine/basic.toyc", new Render(), IDE.class.getClassLoader(), lang );
		frame.setVisible(true);
	}
}
