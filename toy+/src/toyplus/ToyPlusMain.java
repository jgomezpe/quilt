package toyplus;

import fun_pl.vc.FunVCModel;
import fun_pl.vc.awt.ProgrammingFrame;
import unalcol.gui.render.TextRenderPanel;
import unalcol.i18n.I18N;
import unalcol.util.FileResource;

public class ToyPlusMain {
	public static void main( String[] args ){		
		String language = args.length>=1?args[0]:"spanish";
		I18N.use(language);
		String conf_file = args.length>=2?args[1]:"default.tmc";
		String styles = args.length==3?FileResource.config(args[2]):null;
		ProgrammingFrame.load(new ToyPlusMachineInstance(), new TextRenderPanel(FunVCModel.RENDER), conf_file, styles);
	}
}