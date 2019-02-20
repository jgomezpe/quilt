package js;

import unalcol.vc.BackEnd;
import unalcol.vc.Component;

import java.net.URL;
import java.net.URLClassLoader;

import fun_pl.vc.FunBackEnd;
import fun_pl.vc.FunVCModel;
import fun_pl.vc.GUIFunConstants;
import unalcol.js.Util;
import unalcol.js.gui.JSCanvasRender;
import unalcol.js.gui.JSEditorView;
import unalcol.js.gui.JSLog;
import unalcol.js.vc.JSModel;
import unalcol.js.vc.JSModelLoader;
import unalcol.collection.keymap.HashMap;
import unalcol.gui.paint.CanvasRender;
import unalcol.collection.KeyMap;

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
		try{
			URL aURL = new URL( url );
			String path = aURL.getProtocol()+"://"+aURL.getHost()+"/";
			String[] query = aURL.getQuery().split("&"); 
			String render = Util.value(query, "render");
			String pack = Util.value(query, "pack");
			if(pack.length()>0 ) pack = pack + "/";
			String model = Util.value(query, "model");
			if( model==null || model.length()==0 ) model = "model.jar";
			@SuppressWarnings("resource")
			URLClassLoader loader =  new URLClassLoader(new URL[]{new URL(path+"plugins/"+pack+model)}, JSModel.class.getClassLoader());
			Class<?> cl = loader.loadClass(render);
			CanvasRender cr = (CanvasRender)cl.newInstance();
			cr.setId(FunVCModel.RENDER);
			comp.set(FunVCModel.RENDER,cr);
		}catch(Exception e){ 
			comp.set(FunVCModel.RENDER, new JSCanvasRender(FunVCModel.RENDER));
		}	
		return comp;
	}
}
