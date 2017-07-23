package com.aliensdecoder.syntaxedit;

import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

import com.aliensdecoder.AndroidDrawer;

import java.util.Hashtable;
import java.util.Vector;

import unalcol.gui.editor.SyntaxStyle;
import unalcol.gui.editor.Tokenizer;

/**
 *
 * SpanFactory
 * <P>A SpanFactory returns a set of spans according to the style associated to a token type.
 *
 * <P>
 * <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt_android/src/main/java/com/aliensdecoder/syntaxedit/ImageRemnant.java" target="_blank">
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
public class SpanFactory {
    protected Hashtable<String,SyntaxStyle> styles = new Hashtable<String,SyntaxStyle>();
    private Tokenizer tokenizer=null;
    private String[] token_types=null;

    public SpanFactory(){}

    public void setTokenizer(Tokenizer tokenizer, String[] token_style){
        this.tokenizer = tokenizer;
        this.token_types = token_style;
    }

    public void setStyles( SyntaxStyle[] styles ){
        this.styles.clear();
        for( SyntaxStyle s:styles ) this.styles.put(s.tag(),s);
    }

    public Tokenizer tokenizer(){ return tokenizer; }

    public String style( int token_type ){ return token_types[token_type]; }

    public Object[] get( int token_type ){ return get(style(token_type)); }

    public Object[] get(String style){
        SyntaxStyle s = styles.get(style);
        Vector<Object> v = new Vector<Object>();
        if(s.color()!=null) v.add(new ForegroundColorSpan(AndroidDrawer.convert(s.color())));
        if( !s.bold() && !s.italic() && !s.under_line() ) v.add(new StyleSpan(Typeface.NORMAL));
        if(s.bold()) v.add(new StyleSpan(Typeface.BOLD));
        if(s.italic()) v.add(new StyleSpan(Typeface.ITALIC));
        if(s.under_line()) v.add(new UnderlineSpan());
        // @TODO Deal with font size and family
        Object[] obj = new Object[v.size()];
        for( int i=0; i<obj.length; i++ ) obj[i] = v.get(i);
        return obj;
    }
}