package quilt.computer;

import java.awt.Image;

import quilt.QuiltMachine;
import quilt.Remnant;
import quilt.factory.QuiltCommandFactory;
import quilt.factory.QuiltMachineFactory;
import quilt.factory.RemnantFactory;
import quilt.syntax.QuiltMachineParser;
import unalcol.gui.editor.ErrorManager;

public class QuiltMachinePicker {
	public static Remnant image( Image img ){
		return new ImageRemnant(img);
	}
	
	public static QuiltMachine image( ErrorManager message, Image img ){
		return new QuiltMachine(QuiltCommandFactory.little(), RemnantFactory.single("image", image(img)), new QuiltMachineParser(), message);
	}
	
	public static QuiltMachine get(int machine, ErrorManager error_manager, Image img){
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
			case 4: return image(error_manager,img);
			default: return QuiltMachineFactory.little(error_manager);
		}
	}
}