package quilt.fx;

import fun_pl.FunPlIDE;
import unalcol.i18n.I18N;
import unalcol.i18n.LanguageLoader;

public class IDE {
	public static void main(String[] args){
		String language = args.length>=1?args[0]:"spanish";
		LanguageLoader l = new LanguageLoader(language);
		l.use("quilt");
		I18N.use(l);
		String conf_file = args.length>=2?args[1]:"default.qmc";
		String url = "http://localhost/index.html?mode=fx&pack=quilt&cfg="+conf_file+"&instance=quilt.factory.QuiltMachineInstance"+
						"&render=quilt.js.JSQuiltCanvasRender";
		
		FunPlIDE.fx(url);
	}
}
