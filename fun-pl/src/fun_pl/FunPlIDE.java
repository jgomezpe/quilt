package fun_pl;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import fun_pl.semantic.FunMachine;
import fun_pl.vc.FunBackEnd;
import fun_pl.vc.FunController;
import fun_pl.vc.FunToolbarController;
import fun_pl.vc.FunVCModel;
import fun_pl.vc.GUIFunConstants;
import fun_pl.vc.awt.AWTFunFrontEnd;
import fun_pl.vc.awt.ProgrammingFrame;
import fun_pl.vc.awt.ProgrammingPanel;
import unalcol.gui.render.Render;
import unalcol.i18n.I18N;
import unalcol.js.vc.JSPreModel;
import unalcol.js.vc.mode.fx.FXPanel;
import unalcol.json.JSON2Instance;
import unalcol.util.FileResource;
import unalcol.vc.FrontEnd;
import unalcol.vc.VCModel;

public class FunPlIDE {
	public static void awt( JSON2Instance<FunMachine> instance, Render render, String conf_file ){
		FunController.setInstance(instance);
		GUIFunConstants.FMC=I18N.get(GUIFunConstants.FMC);
		GUIFunConstants.FMS=I18N.get(GUIFunConstants.FMS);
		GUIFunConstants.FMP=I18N.get(GUIFunConstants.FMP);
		String machine_txt = FileResource.config(conf_file);
		ProgrammingFrame frame = new ProgrammingFrame(render);
		ProgrammingPanel panel = frame.windowPanel();
		
		FunBackEnd backend = new FunBackEnd();
		FrontEnd frontend = new AWTFunFrontEnd(panel);
		new VCModel(backend,frontend);
		panel.setBackEnd(backend);
		panel.setMachine( machine_txt );

		Image img = FileResource.image("remnant.png");
		frame.setIconImage(img);
		frame.setVisible(true);
	}	

	public static void fx( JSON2Instance<FunMachine> instance, String conf_file, String url ){
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		
		String machine_txt = FileResource.config(conf_file);
		FunController.setInstance(instance);
		
		//String unalcol_url = "http://localhost/unalcol/";
		JSPreModel m = JSPreModel.get(url);
		FunBackEnd backend = new FunBackEnd();
		FunToolbarController toolbar = (FunToolbarController)backend.component(FunVCModel.TOOLBAR);
		toolbar.machine(machine_txt);
		FXPanel panel = new FXPanel(url, m.frontend());
		FrontEnd frontend = panel.frontend();
		new VCModel(backend, frontend);
        // Run this later:
        SwingUtilities.invokeLater(new Runnable() {  
            @Override
            public void run() {  
                final JFrame frame = new JFrame();  
                 
                frame.getContentPane().add(panel);  
        		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        		int width = (int)screenSize.getWidth();
        		int height = (int)screenSize.getHeight();
        		frame.setSize(new Dimension(width*4/5, height*4/5));                 
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
                frame.setVisible(true);  
            }  
        });     
		
	}
}
