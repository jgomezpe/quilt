package fun_pl.vc;

import fun_pl.FunLanguage;
import fun_pl.semantic.FunMachine;
import unalcol.gui.log.Log;
import unalcol.gui.render.Render;
import unalcol.i18n.I18N;
import unalcol.json.JSON2Instance;
import unalcol.vc.Controller;
import unalcol.vc.DefaultComponent;

public class FunController extends DefaultComponent implements Controller{
	protected static Log log = null;
	protected static Render render = null;
	protected static FunMachine machine;
	protected static FunLanguage quiltLang;
	protected static JSON2Instance<FunMachine> instance;
	
	public FunController(String id){ super(id); }	

	protected static String i18n(String code){ return I18N.get(code); }
	
	protected Log log(){
		if( log == null ) log = (Log)front().component(FunVCModel.LOG);
		return log;
	}
	
	public void setLog( Log log ){ FunController.log = log; }
	
	protected Render render(){
		if(render==null) render = (Render)front().component(FunVCModel.RENDER);
		return render;
	}
	
	public void setRender( Render render ){ FunController.render = render; }	
	
	public static JSON2Instance<FunMachine> instance(){ return instance; }
	
	public static void setInstance( JSON2Instance<FunMachine> instance ){ FunController.instance = instance; }
	
	public static FunMachine machine(){ return machine; }
}