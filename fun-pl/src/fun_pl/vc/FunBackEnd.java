package fun_pl.vc;

import fun_pl.semantic.FunMachine;
import unalcol.json.JSON2Instance;
import unalcol.collection.keymap.HashMap;
import unalcol.util.FileResource;
import unalcol.vc.BackEnd;
import unalcol.vc.Component;
import unalcol.vc.KeyMapSide;

public class FunBackEnd extends KeyMapSide implements BackEnd{
	public FunBackEnd(ClassLoader loader, String machineLoader, String conf_file){
		this();
		try{
			Class<?> cl = loader.loadClass(machineLoader);
			@SuppressWarnings("unchecked")
			JSON2Instance<FunMachine> instance = (JSON2Instance<FunMachine>)cl.newInstance();
			FunController.setInstance(instance);
			String machine_txt = FileResource.config(loader, conf_file);
			toolbar().machine(machine_txt);
		} catch(Exception e) { e.printStackTrace(); };
	}
	
	public FunBackEnd(){ 
		super(FunVCModel.BACKEND, new HashMap<String,Component>());
		this.register(new FunToolbarController());
		this.register(new FunEditorController(FunVCModel.PROGRAM));
		this.register(new FunEditorController(FunVCModel.COMMAND));
	}
	
	public FunToolbarController toolbar(){ return (FunToolbarController)this.controller(FunVCModel.TOOLBAR); }
}
