package quilt.syntax;

/**
*
* QuiltSymbols
* <P>Symbols that are used by the Quilt language.
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/syntax/QuiltSymbols.java" target="_blank">
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
public class QuiltSymbols{
	protected static final char EOF=(char)-1;
	protected static final char COMMENT='%';
	protected static final char UNDER_SCORE='_';
	protected static final char COMMA=',';
	protected static final char STITCH='|';
	protected static final char LEFTSTITCH='#';
	protected static final char CR='\r';  // Just for windows compatibility
	protected static final char EOL='\n';
	protected static final char SPACE=' ';
	protected static final char TAB='\t';
	protected static final char ASSIGN='=';
	protected static final char LEFT='(';
	protected static final char RIGHT=')';
	protected static final char DOLLAR='$';
	
	protected String extra_name;
	
	public QuiltSymbols(){ this(""); }
	public QuiltSymbols( String extra_name ){ this.extra_name = extra_name; }

	public boolean is_space( char c ){ return c==SPACE || c==TAB || c==CR || c==EOL; }
	public boolean is_eol( char c ){ return c==EOL; }
	public boolean is_extra_name(char c){ return extra_name.indexOf(c)>=0; }
	public boolean is_name(char c){ return Character.isLetterOrDigit(c) || c==DOLLAR || is_extra_name(c); };
	public boolean is_special(char c){ return c==COMMA || c==ASSIGN || c==LEFT || c==RIGHT; };
	public boolean is_stitch(char c){ return c==STITCH; };
	public boolean is_leftstitch(char c){ return c==LEFTSTITCH; };
	public boolean is_comment(char c){ return c==COMMENT; };
	public boolean is_left(char c){ return c==LEFT; }
	public boolean is_right(char c){ return c==RIGHT; }
	public boolean is_dollar(char c){ return c==DOLLAR; }
	public boolean is_comma(char c){ return c==COMMA; }
	public boolean is_assign(char c){ return c==ASSIGN; }
	public boolean is_underscore(char c){ return c==UNDER_SCORE; }
	public boolean is_eof(char c){ return c==EOF; }

	public static char stitch(){ return STITCH; };
	public static char leftstitch(){ return LEFTSTITCH; };
	public static char comment(){ return COMMENT; };
	public static char left(){ return LEFT; }
	public static char right(){ return RIGHT; }
	public static char comma(){ return COMMA; }
	public static char dollar(){ return DOLLAR; }
	public char assign(){ return ASSIGN; }
	public char underscore(){ return UNDER_SCORE; }
	public String extra_name(){ return extra_name; }
	
}