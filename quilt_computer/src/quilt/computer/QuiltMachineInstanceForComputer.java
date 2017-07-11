package quilt.computer;

import quilt.factory.QuiltMachineInstance;
import unalcol.gui.editor.ErrorManager;

public class QuiltMachineInstanceForComputer extends QuiltMachineInstance{

	public QuiltMachineInstanceForComputer(ErrorManager language) {
		super(language);
		remnants.register(ImageRemnantInstance.IMAGE, ImageRemnant.class.getName(), new ImageRemnantInstance());
	}
}