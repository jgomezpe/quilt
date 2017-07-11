package quilt.computer;

import quilt.QuiltMachine;
import quilt.factory.QuiltMachineFactory;
import unalcol.gui.editor.ErrorManager;
import unalcol.gui.util.ObjectParser;

public class QuiltMachinePicker {
	public static QuiltMachine image( ErrorManager language, String resource, boolean asResource){
		//resource = "http://www.mkyong.com/image/mypic.jpg";
		resource = ObjectParser.store(resource);
		String machine_txt = "[\"machine\",[\"commands\",\"sew\",\"rot\"],[[\"image\",[\"image\","+resource+","+asResource+"]]]]";
		System.out.println(machine_txt);
		QuiltMachineInstanceForComputer qm = new QuiltMachineInstanceForComputer(language);
		try{
			return qm.load(ObjectParser.parse(machine_txt));
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		//ImageRemnant img = new ImageRemnant(resource, asResource, Util.image(resource, asResource));
		//return new QuiltMachine(QuiltCommandFactory.little(), RemnantFactory.single("image", img), new QuiltMachineParser(), language);
	}
	
	public static QuiltMachine get(int machine, ErrorManager error_manager, String resource, boolean asResource){
/*
		InputStream in=null;
		try{
			if( asResource ) in = this.getClass().getResourceAsStream(fileName);
			else in = new FileInputStream(fileName);
		ErrorManager error_manager = new ErrorManager(language,asResource);
*/			
		switch(machine){
			case 1: return QuiltMachineFactory.little2(error_manager);
			case 2: return QuiltMachineFactory.little3(error_manager);
			case 3: return QuiltMachineFactory.minimal(error_manager);
			case 4: return image(error_manager,resource,asResource);
			default: return QuiltMachineFactory.little(error_manager);
		}
	}
}