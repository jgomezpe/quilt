package toyplus;

import fun_pl.FunPlIDE;
import fun_pl.vc.FunVCModel;
import unalcol.gui.render.AWTTextRender;
import unalcol.i18n.I18N;

public class ToyPlusMain {
	public static void main( String[] args ){		
		String language = args.length>=1?args[0]:"spanish";
		I18N.setLanguage(language);
		I18N.use("toy");
		String conf_file = args.length>=2?args[1]:"default.tmc";
		FunPlIDE.awt(new ToyPlusMachineInstance(), new AWTTextRender(FunVCModel.RENDER), conf_file);
	}
}