package quilt.factory;

import quilt.syntax.QuiltMachineParser;
import quilt.syntax.QuiltSymbols;
import unalcol.gui.util.Instance;

/**
*
* QuiltMachineParserInstance
* <P>Load and Store mechanism of a Quilt machine parser (Persistent methods).
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/factory/QuiltMachineParserInstance.java" target="_blank">
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
public class QuiltMachineParserInstance implements Instance<QuiltMachineParser>{
	public static final String PARSER="parser";
	protected QuiltSymbolsInstance qs = new QuiltSymbolsInstance();

	@Override
	public QuiltMachineParser load(Object[] args){
		if( args.length==0 || args.length>3 || !PARSER.equals(args[0]) ) return null;
		if( args.length==1 ) return new QuiltMachineParser();
		if( args.length==2 ){
			if(args[1] instanceof Object[]) return new QuiltMachineParser(null,1,qs.load((Object[])args[1]));				
			else return new QuiltMachineParser(null,(Integer)args[1]);
		}
		return new QuiltMachineParser(null,(Integer)args[1], qs.load((Object[])args[2]));
	}

	@Override
	public Object[] store(QuiltMachineParser parser) {
		QuiltSymbols s = parser.symbols();
		String extra = s.extra_name();
		int stitch = parser.stitchs();
		if( stitch != 1 ){
			if( extra.length()>0 ) return new Object[]{PARSER, stitch, qs.store(s)};
			else return new Object[]{PARSER,stitch};
		}else{
			if( extra.length()>0 ) return new Object[]{PARSER,qs.store(s)};
			else return new Object[]{PARSER};
		}
	}
}