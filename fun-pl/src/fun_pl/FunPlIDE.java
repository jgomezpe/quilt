package fun_pl;
import fun_pl.vc.FunBackEnd;
import fun_pl.vc.GUIFunConstants;
import fun_pl.vc.awt.AWTFunFrontEnd;
import unalcol.i18n.I18N;
import unalcol.js.vc.JSModel;
import unalcol.vc.FrontEnd;
import unalcol.vc.VCModel;

public class FunPlIDE {
	public static void awt( String instance, String render, String conf_file ){
		GUIFunConstants.FMC=I18N.get(GUIFunConstants.FMC);
		GUIFunConstants.FMS=I18N.get(GUIFunConstants.FMS);
		GUIFunConstants.FMP=I18N.get(GUIFunConstants.FMP);

		ClassLoader loader = FunPlIDE.class.getClassLoader();
		FunBackEnd backend = new FunBackEnd( loader, instance, conf_file);
		FrontEnd frontend = new AWTFunFrontEnd(loader, render);
		new VCModel(backend, frontend);
	}	

	public static void fx( String url ){
		GUIFunConstants.FMC=I18N.get(GUIFunConstants.FMC);
		GUIFunConstants.FMS=I18N.get(GUIFunConstants.FMS);
		GUIFunConstants.FMP=I18N.get(GUIFunConstants.FMP);
		new JSModel(url);		
	}
}
