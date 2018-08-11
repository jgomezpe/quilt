package fun_pl.vc;

import fun_pl.FunLanguage;
import unalcol.gui.log.Log;
import unalcol.i18n.I18N;
import unalcol.types.collection.Collection;
import unalcol.util.ObjectParser;

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
		theLog.error("");
		theLog.display(true);		
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
		theLog.error("");
		theLog.display(true);
	}
	
	public void machine(String machine_txt){
		try{ 
			FunController.machine = FunController.instance().load(ObjectParser.parse(machine_txt)); 
			FunController.quiltLang = new FunLanguage(machine);
		}catch(Exception e){ e.printStackTrace(); }		
	}	
}