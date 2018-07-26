package fun_pl.vc;

import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.vc.BackEnd;
import unalcol.vc.Component;
import unalcol.vc.KeyMapSide;

public class FunBackEnd extends KeyMapSide implements BackEnd{

	public FunBackEnd(){ 
		super(FunVCModel.BACKEND, new HTKeyMap<String,Component>());
		this.register(new FunToolbarController());
		this.register(new FunEditorController(FunVCModel.PROGRAM));
		this.register(new FunEditorController(FunVCModel.COMMAND));
	}
}
