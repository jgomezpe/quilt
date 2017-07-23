package com.aliensdecoder.syntaxedit;

import android.text.SpannableStringBuilder;

import java.util.Vector;

/**
 *
 * SpannableParagraph
 * <P>A paragraph for a SyntaxStyle Edit text.
 *
 * <P>
 * <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt_android/src/main/java/com/aliensdecoder/syntaxedit/SpannableParagraph.java" target="_blank">
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
public class SpannableParagraph {
    private Vector<SpannableLine> lines = new Vector<SpannableLine>();
    private SpanFactory factory;

    public SpannableParagraph(SpannableStringBuilder str, SpanFactory factory){
        this.factory = factory;
        int offset = 0;
        while(offset<str.length()){
            SpannableLine line = new SpannableLine(offset,str,factory);
            offset = line.end();
        }
    }

    public int index( int offset ){
        int i=0;
        while( i<lines.size() && offset>lines.get(i).end() ) i++;
        return i;
    }

    public int offset( int row, int column ){ return lines.get(row).start()+column; }

    public void update( int start, int before, int count, SpannableStringBuilder str ){
        int start_line = index(start);
        int end_line = index(start+before);
        int i=start_line+1;
        while( i<=end_line ) {
            //lines.get(i).removeSpans(str);
            lines.remove(i);
            end_line--;
        }
        if( start_line < lines.size() ){
            SpannableLine line = lines.get(start_line);
            line.updateSpans(str);
        }else{
            SpannableLine line = new SpannableLine(lines.size()>0?lines.get(lines.size()-1).end():0, str,factory);
            lines.add(line);
        }
        count += start;
        while( lines.get(start_line).end()<count ){
            SpannableLine line = new SpannableLine(lines.get(start_line).end(), str,factory);
            if( start_line == lines.size()-1 )
                lines.add(line);
            else
                lines.add(start_line+1,line);
            start_line++;
        }
    }
}
