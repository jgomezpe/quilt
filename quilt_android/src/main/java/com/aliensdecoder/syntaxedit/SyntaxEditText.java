package com.aliensdecoder.syntaxedit;

import android.text.SpannableStringBuilder;
import android.widget.EditText;

import unalcol.gui.editor.SyntaxEditComponent;
import unalcol.gui.editor.SyntaxStyle;
import unalcol.gui.editor.Tokenizer;

/**
 *
 * SyntaxEditText
 * <P>An edit text hat can be used for syntax highlighting.
 *
 * <P>
 * <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt_android/src/main/java/com/aliensdecoder/syntaxedit/SyntaxEditText.java" target="_blank">
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
public class SyntaxEditText implements SyntaxEditComponent{
    private SpanFactory span_factory=new SpanFactory();
    private SyntaxTextWatcher watcher = null;
    private SpannableParagraph paragraph=null;
    private EditText input;

    public SyntaxEditText(EditText input){ this.input = input; }

    public void update( int start, int before, int count ){
        if( paragraph!=null ) paragraph.update(start,before,count,(SpannableStringBuilder)input.getText());
    }

    public void locate( int row, int column ){
        if( paragraph != null ) input.setSelection(paragraph.offset(row,column));
    }

    public EditText input(){ return input; }

    public void setTokenizer(Tokenizer tokenizer, String[] token_style){
        span_factory.setTokenizer(tokenizer, token_style);
        update();
    }

    public void setStyle( SyntaxStyle[] styles ){
        span_factory.setStyles(styles);
        update();
    }

    public String getText(){ return input.getText().toString(); }

    public void setText( String str ){ input.setText(str); }

    public void update(){
        if( watcher != null ) input.removeTextChangedListener(watcher);
        paragraph = new SpannableParagraph((SpannableStringBuilder)input.getText(),span_factory);
        watcher = new SyntaxTextWatcher(this);
        input.addTextChangedListener(watcher);
    }
}