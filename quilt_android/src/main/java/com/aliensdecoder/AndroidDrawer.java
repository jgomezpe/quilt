package com.aliensdecoder;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import unalcol.gui.paint.Color;
import quilt.gui.Drawer;

/**
 *
 * AndroidDrawer
 * <P>Simple GUI for drawing quilts.
 *
 * <P>
 * <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt_android/src/main/java/com/aliensdecoder/AndroidDrawer.java" target="_blank">
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
public class AndroidDrawer extends Drawer{
    private Canvas g;
    private Paint paint = new Paint();

    public AndroidDrawer( Canvas g, int scale ){
        this.g = g;
        this.scale = scale;
    }

    @Override
    public void drawLine(int start_x, int start_y, int end_x, int end_y){
        g.drawLine(scale(start_x),scale(start_y),scale(end_x),scale(end_y),paint);
    }

    @Override
    public void drawString(int x, int y, String str) {
        g.drawText(str, scale(x), scale(y), paint);
    }

    @Override
    public Color setColor(Color color){
        int c = paint.getColor();
        Color quilt_color = convert(c);
        c = convert(color);
        paint.setColor(c);
        return quilt_color;
    }

    @Override
    public void drawFill(int x, int y, int width, int height) {
        int left = scale(x);
        int top = scale(y);
        int right = scale(x+width);
        int bottom = scale(y+height);
        g.drawRect(left,top,right,bottom,paint);
    }

    @Override
    public void drawImage(int x, int y, int width, int height, int rotation, Object obj) {
       // Image img = ((Image) obj).getScaledInstance(scale(width), scale(height), Image.SCALE_SMOOTH);
        Bitmap img = (Bitmap)obj;
        g.drawBitmap(img,x,y,null);
    }

    @Override
    public void drawFillPolygon( int[] x, int[] y ){
        paint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        path.moveTo(scale(x[0]),scale(y[0]));
        for( int i=1; i<x.length; i++) path.lineTo(scale(x[i]),scale(y[i]));
        path.lineTo(scale(x[0]),scale(y[0]));
        g.drawPath(path,paint);
    }

    public static int convert( Color color ){ return android.graphics.Color.argb(color.alpha(),color.red(),color.green(),color.blue()); }
    public static Color convert( int color ){
        return new Color(android.graphics.Color.red(color), android.graphics.Color.green(color), android.graphics.Color.blue(color), android.graphics.Color.alpha(color));
    }
}
