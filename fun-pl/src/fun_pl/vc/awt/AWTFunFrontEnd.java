package fun_pl.vc.awt;

import fun_pl.vc.FunVCModel;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.vc.Component;
import unalcol.vc.FrontEnd;
import unalcol.vc.KeyMapSide;

public class AWTFunFrontEnd extends KeyMapSide implements FrontEnd{
	public AWTFunFrontEnd(ProgrammingPanel panel){
		super(FunVCModel.FRONTEND, new HTKeyMap<String,Component>());
		register(panel.log);
		register(panel.program_editor);
		register(panel.command_editor);
		register(panel.drawPanel.render());
	}
}
