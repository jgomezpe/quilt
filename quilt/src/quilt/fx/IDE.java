package quilt.fx;

import fun_pl.FunPlIDE;
import quilt.util.QuiltConstants;
import unalcol.i18n.I18N;

public class IDE {
	public static void main(String[] args){
		String language = args.length>=1?args[0]:QuiltConstants.SPANISH;
		I18N.setLanguage(language);
		I18N.use("quilt");
		String conf_file = args.length>=2?args[1]:"default.qmc";
		String url = "http://localhost/index.html?mode=fx&pack=quilt&cfg="+conf_file+"&instance=quilt.factory.QuiltMachineInstance";
		
		FunPlIDE.fx(url);
	}
}
