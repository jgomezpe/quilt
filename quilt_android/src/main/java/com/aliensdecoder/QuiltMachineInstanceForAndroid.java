package com.aliensdecoder;

import quilt.factory.QuiltMachineInstance;
import unalcol.gui.editor.ErrorManager;

/**
 * Created by jgomez on 14/07/17.
 */

public class QuiltMachineInstanceForAndroid  extends QuiltMachineInstance {

    public QuiltMachineInstanceForAndroid(ErrorManager language) {
        super(language);
        remnants.register(ImageRemnantInstance.IMAGE, ImageRemnant.class.getName(), new ImageRemnantInstance());
    }
}