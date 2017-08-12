package com.aliensdecoder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.net.URL;

import quilt.QuiltMachine;
import quilt.Remnant;
import quilt.util.Util;
import unalcol.gui.editor.ErrorManager;
import unalcol.gui.util.ObjectParser;

/**
 *
 * Store
 * <P>Store of the Quilt programming environment objects.
 *
 * <P>
 * <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt_android/src/main/java/com/aliensdecoder/Store.java" target="_blank">
 * Source code </A> is available.
 *
 * <h3>License</h3>
 *
 * Copyright (c) 2017 by Jonatan Gomez-Perdomo. <br>
 * All rights reserved. <br>
 *
 * <p>Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <ul>
 * <li> Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * <li> Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <li> Neither the name of the copyright owners, their employers, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * </ul>
 * <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 *
 *
 * @author <A HREF="http://disi.unal.edu.co/profesores/jgomezpe"> Jonatan Gomez-Perdomo </A>
 * (E-mail: <A HREF="mailto:jgomezpe@unal.edu.co">jgomezpe@unal.edu.co</A> )
 * @version 1.0
 */
public class Store {
    public static QuiltMachine machine;
    public static Remnant remnant;
    public static ErrorManager manager;

    public static ErrorManager load_language(String language){
        manager = Util.i18n(language);
        return manager;
    }

    public static QuiltMachine load(String machine_txt){
        QuiltMachineInstanceForAndroid qm = new QuiltMachineInstanceForAndroid(manager);
        try{
            machine = qm.load(ObjectParser.parse(machine_txt));
        }catch(Exception e){
            e.printStackTrace();
            machine = null;
        }
        return machine;
    }

    public static Bitmap image(String name){
        try{ if( name.startsWith("http://") || name.startsWith("https://")) return BitmapFactory.decodeStream((new URL(name)).openStream()); }catch(Exception ex){}
        try{ return BitmapFactory.decodeStream(new FileInputStream(name)); }catch(Exception ex){};
        try{ return BitmapFactory.decodeStream(Util.class.getResourceAsStream("/"+Util.images+name)); }catch(Exception ex){}
        try{ return BitmapFactory.decodeStream(new FileInputStream(Util.resources+Util.images+name)); }catch(Exception ex){};
        return null;
    }


}
