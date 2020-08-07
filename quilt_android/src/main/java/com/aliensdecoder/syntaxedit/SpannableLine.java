package com.aliensdecoder.syntaxedit;
import android.text.SpannableStringBuilder;

import java.util.Vector;

import unalcol.gui.editor.Token;
import unalcol.gui.editor.Tokenizer;

/**
 *
 * SpannableLine
 * <P>A line of text for a SyntaxStyle Edit text.
 *
 * <P>
 * <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt_android/src/main/java/com/aliensdecoder/syntaxedit/SpannableLine.java" target="_blank">
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
public class SpannableLine {
    private int offset;
    private int length;
    private Vector<Object> spans = new Vector<Object>();
    private SpanFactory factory;

    public SpannableLine(int offset, SpannableStringBuilder str, SpanFactory factory){
        this.offset=offset;
        this.factory = factory;
        updateSpans(str);
    }

    public void removeSpans( SpannableStringBuilder str ){
        for( Object span:spans ) str.removeSpan(span);
        spans.clear();
    }

    public void updateSpans( SpannableStringBuilder str ){
        removeSpans(str);
        int i=offset;
        while(i<str.length()&&str.charAt(i)!='\n') i++;
        length = i-offset+(i<str.length()?1:0);
        if( i>offset ){
            String line = str.subSequence(offset,i).toString();
            Tokenizer tokenizer = factory.tokenizer();
            Token[] tokens = tokenizer.tokens(line);
            if( tokens!=null )
                for (Token t : tokens) {
                    Object[] spans = factory.get(t.type());
                    for (Object span : spans) {
                        str.setSpan(span, offset + t.offset(), offset + t.endOffset(), 0);
                        this.spans.add(span);
                    }
                }
        }
    }

    public int start(){ return offset; }
    public int end(){ return offset+length; }

    public void shift( int s ){ offset+=s; }
}