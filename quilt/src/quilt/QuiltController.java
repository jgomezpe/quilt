package quilt;

import fun_pl.FunLanguage;
import fun_pl.semantic.FunCommandCall;
import fun_pl.semantic.FunProgram;
import quilt.factory.QuiltMachineInstance;
import quilt.util.QuiltConstants;
import unalcol.language.LanguageException;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.util.ObjectParser;
import unalcol.vc.backend.SimpleController;

public class QuiltController extends SimpleController{
	protected QuiltMachine machine;
	protected FunLanguage quiltLang;
	protected QuiltMachineInstance qm= new QuiltMachineInstance();
	
	public void init(String machine_txt){
		try{ 
			this.machine = qm.load(ObjectParser.parse(machine_txt)); 
			this.quiltLang = new FunLanguage(machine);
		}catch(Exception e){ e.printStackTrace(); }		
	}
	
	public void compile( String program ){
		try{
			machine.clear();
			FunProgram prog = (FunProgram)quiltLang.process( program, true );
			machine.setProgram(prog);
			// ok();
		}catch(LanguageException e){
			// show_error_message(jProgram, e);
		}
	}
	
	public void command( String program ){
		FunCommandCall command=null;
		try{
			command = (FunCommandCall)quiltLang.process( program, false);
			command.setRow(-1);
		}catch(LanguageException e){
			//show_error_message(jCommand, e);
		}
		if( command != null ){
			try{
				Quilt r = (Quilt)command.execute( new HTKeyMap<String, Object>() );
				// @TODO: renderRender
				//drawPanel.set(r);
				// ok()
				//if(this.log.getOutArea().getText().contains(QuiltConstants.ERROR) ) this.log.getOutArea().setText(i18n(QuiltConstants.NO_ERRORS));
				//this.log.select(true);
				//this.log.getErrorArea().setText("");
			}catch(LanguageException e){
				// show_error_message(jProgram, e);
			}
		}	
	}
}