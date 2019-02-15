package fun_pl.vc.awt;

import java.awt.Image;

import fun_pl.vc.FunVCModel;
import unalcol.gui.render.Render;
import unalcol.collection.keymap.HashMap;
import unalcol.util.FileResource;
import unalcol.vc.Component;
import unalcol.vc.FrontEnd;
import unalcol.vc.KeyMapSide;

public class AWTFunFrontEnd extends KeyMapSide implements FrontEnd{
	public AWTFunFrontEnd(ClassLoader loader, String render){
		super(FunVCModel.FRONTEND, new HashMap<String,Component>());
		try{
			Class<?> cl = loader.loadClass(render);
			Render r = (Render)cl.newInstance();
			r.setId(FunVCModel.RENDER);
			ProgrammingFrame frame = new ProgrammingFrame(this,r);
			ProgrammingPanel panel = frame.windowPanel();
			register(panel.log);
			register(panel.program_editor);
			register(panel.command_editor);
			register(panel.drawPanel);
			panel.setMachine();

			Image img = FileResource.image("remnant.png");
			frame.setIconImage(img);
			frame.setVisible(true);
			
		} catch(Exception e) { e.printStackTrace(); };	
	}	
}