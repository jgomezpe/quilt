package fun_pl.vc;

import fun_pl.FunLanguage;
import fun_pl.semantic.FunMachine;
import unalcol.gui.log.Log;
import unalcol.i18n.I18N;
import unalcol.json.JSON;
import unalcol.json.JSONParser;
import unalcol.collection.Collection;

public class FunToolbarController extends FunController{
	public FunToolbarController(){ super(FunVCModel.TOOLBAR); } 
	public void primitives(){
		Collection<String> commands = machine.primitives();
		StringBuilder sb = new StringBuilder();
		for( String c:commands ){
			try{
				sb.append(machine.primitive(c).toString());
				sb.append('\n');
			}catch(Exception e){}
		}
		Log theLog = log();
		theLog.out(sb.toString());
	}
	
	public void values(){
		Collection<String> remnants = machine.values();
		StringBuilder sb = new StringBuilder();
		if( remnants != null )
			for( String r:remnants ){
				sb.append(r);
				sb.append('\n');
			}
		else sb.append(I18N.get(GUIFunConstants.VALUE));
		Log theLog = log();
		theLog.out(sb.toString());
	}
	
	public void machine(String machine_txt){
		try{
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(machine_txt);
			if( obj instanceof JSON ){
				Object x =  FunController.instance().load((JSON)obj);
				System.out.println("[FunToolbarController]"+x.getClass());
				System.out.println("[FunToolbarController]"+x.getClass().getClassLoader());
				System.out.println("[FunToolbarController]"+FunMachine.class.getClassLoader());
				FunController.machine = FunController.instance().load((JSON)obj); 
				FunController.quiltLang = new FunLanguage(machine);
			}
		}catch(Exception e){ e.printStackTrace(); }		
	}	
}