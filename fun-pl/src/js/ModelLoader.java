package js;

import unalcol.vc.BackEnd;
import unalcol.vc.Component;

import java.net.URL;
import java.net.URLClassLoader;

import fun_pl.vc.FunBackEnd;
import fun_pl.vc.FunVCModel;
import fun_pl.vc.GUIFunConstants;
import unalcol.js.Util;
import unalcol.js.gui.JSEditorView;
import unalcol.js.gui.JSLog;
import unalcol.js.vc.JSModel;
import unalcol.js.vc.JSModelLoader;
import unalcol.types.collection.keymap.HashMap;
import unalcol.types.collection.keymap.KeyMap;

public class ModelLoader implements JSModelLoader{
	
	@Override
	public BackEnd backend(String url) {
		try{
			URL aURL = new URL( url );
			String path = aURL.getProtocol()+"://"+aURL.getHost()+"/";
			String[] query = aURL.getQuery().split("&"); 
			String instance = Util.value(query, "instance");
			String pack = Util.value(query, "pack");
			if(pack.length()>0 ) pack = pack + "/";
			String conf_file = Util.value(query, "cfg");
			if(conf_file==null || conf_file.length()==0 ) conf_file = "default"+GUIFunConstants.FMC;
			String model = Util.value(query, "model");
			if( model==null || model.length()==0 ) model = "model.jar";
			URLClassLoader loader =  new URLClassLoader(new URL[]{new URL(path+"plugins/"+pack+model)}, JSModel.class.getClassLoader());
			return new FunBackEnd(loader, instance, conf_file);
		}catch(Exception e){ e.printStackTrace(); }	
		return null;
	}

	@Override
	public KeyMap<String, Component> frontend(String url) {
		HashMap<String, Component> comp = new HashMap<String,Component>();
		comp.set(FunVCModel.LOG, new JSLog(FunVCModel.LOG));
		comp.set(FunVCModel.PROGRAM, new JSEditorView(FunVCModel.PROGRAM));
		comp.set(FunVCModel.COMMAND, new JSEditorView(FunVCModel.COMMAND));
		return comp;
	}
}
