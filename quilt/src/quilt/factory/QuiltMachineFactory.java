package quilt.factory;

import quilt.QuiltMachine;
import quilt.gui.Color;
import quilt.syntax.QuiltMachineParser;
import unalcol.gui.editor.ErrorManager;
import unalcol.gui.util.ObjectParser;

public class QuiltMachineFactory {
	public static QuiltMachine little( ErrorManager message ){
		return new QuiltMachine(QuiltCommandFactory.little(), RemnantFactory.little("diag", "squa", new Color(255,0,0,255)), new QuiltMachineParser(), message);
	}

	public static QuiltMachine little2( ErrorManager message ){
		return new QuiltMachine(QuiltCommandFactory.little(), RemnantFactory.little2("red", new Color(255,0,0,255), "blue", new Color(0,0,255,255)), new QuiltMachineParser(), message);
	}

	public static QuiltMachine little3( ErrorManager message ){
		return new QuiltMachine(QuiltCommandFactory.little(), RemnantFactory.little3("diag", "squa", "line", new Color(255,0,0,255)), new QuiltMachineParser(), message);
	}

	public static QuiltMachine minimal( ErrorManager message ){
		return new QuiltMachine(QuiltCommandFactory.minimal(), RemnantFactory.single("line", RemnantFactory.zero(new Color(255,0,0,255))), new QuiltMachineParser(), message);
	}		
	
	public static QuiltMachine get( ErrorManager message, Object[] machine ){
		if( machine.length==0 || !"machine".equals(machine[0]) ) return null;
		// @TODO: Use loading 
		return null;	
	}
	
	public static QuiltMachine get( ErrorManager message, String machine ){
		try {
			return get( message, ObjectParser.parse(machine) );			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
