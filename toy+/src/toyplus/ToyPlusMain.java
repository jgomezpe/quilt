package toyplus;

import fun_pl.FunPlIDE;
import unalcol.i18n.I18N;
import unalcol.i18n.LanguageLoader;

public class ToyPlusMain {
	public static void main( String[] args ){		
		String language = args.length>=1?args[0]:"spanish";
		LanguageLoader l = new LanguageLoader(language);
		l.use("toy");
		I18N.use(l);
		String conf_file = args.length>=2?args[1]:"default.tmc";
		FunPlIDE.awt("toyplus.ToyPlusMachineInstance", "unalcol.gui.render.AWTTextRender", conf_file);
	}
}