package quilt.factory;

import quilt.QuiltMachine;
import quilt.QuiltMachineParser;
import quilt.gui.Color;
import quilt.util.Language;

public class QuiltMachineFactory {
	public static QuiltMachine little( Language message ){
		return new QuiltMachine(QuiltCommandFactory.little(), RemnantFactory.little("diag", "squa", new Color(255,0,0,255)), new QuiltMachineParser(), message);
	}

	public static QuiltMachine little2( Language message ){
		return new QuiltMachine(QuiltCommandFactory.little(), RemnantFactory.little2("red", new Color(0,0,255,255), "blue", new Color(255,0,0,255)), new QuiltMachineParser(), message);
	}

	public static QuiltMachine little3( Language message ){
		return new QuiltMachine(QuiltCommandFactory.little(), RemnantFactory.little3("diag", "squa", "line", new Color(255,0,0,255)), new QuiltMachineParser(), message);
	}

	public static QuiltMachine minimal( Language message ){
		return new QuiltMachine(QuiltCommandFactory.minimal(), RemnantFactory.single("line", RemnantFactory.zero(new Color(255,0,0,255))), new QuiltMachineParser(), message);
	}		
}
