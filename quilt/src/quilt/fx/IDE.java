package quilt.fx;

import fun_pl.FunPlIDE;
import quilt.factory.QuiltMachineInstance;
import quilt.util.QuiltConstants;
import unalcol.i18n.I18N;

public class IDE {
	public static void main(String[] args){
		String language = args.length>=1?args[0]:QuiltConstants.SPANISH;
		I18N.setLanguage(language);
		I18N.use("quilt");
//		String conf_file = args.length>=2?args[1]:"default.qmc";
		String conf_file = args.length>=2?args[1]:"two_images.qmc";
		String url = "http://localhost/quilt/";
		
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		
		FunPlIDE.fx(new QuiltMachineInstance(), conf_file, url);
	}

}
