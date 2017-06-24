package quilt.computer;

import java.awt.Image;

import quilt.QuiltMachine;
import quilt.QuiltMachineParser;
import quilt.Remnant;
import quilt.factory.QuiltCommandFactory;
import quilt.factory.QuiltMachineFactory;
import quilt.factory.RemnantFactory;
import quilt.util.Language;

public class QuiltMachinePicker {
	public static Remnant image( Image img ){
		return new ImageRemnant(img);
	}
	
	public static QuiltMachine image( Language message, Image img ){
		return new QuiltMachine(QuiltCommandFactory.little(), RemnantFactory.single("image", image(img)), new QuiltMachineParser(), message);
	}
	
	public static QuiltMachine get(int machine, String language, Image img){
		Language l = new Language(language);
		switch(machine){
			case 1: return QuiltMachineFactory.little2(l);
			case 2: return QuiltMachineFactory.little3(l);
			case 3: return QuiltMachineFactory.minimal(l);
			case 4: return image(l,img);
			default: return QuiltMachineFactory.little(l);
		}
	}
}