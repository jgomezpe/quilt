package quilt.render;

import unalcol.gui.paint.Color;
import unalcol.gui.paint.ColorInstance;
import unalcol.util.Instance;

/**
*
* StripInstance
* <P>Load and Store mechanism of Strips (Persistent methods)
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/gui/StripInstance.java" target="_blank">
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
public class StripInstance implements Instance<Strip>{
	public static final String STRIP="strip";
	
	protected ColorInstance c_instance = new ColorInstance();

	@Override
	public Strip load(Object[] args) {
		if( args.length<5 || args.length>6 || !STRIP.equals(args[0]) ) return null;
		if( args.length==6 ){
			Color c = c_instance.load((Object[])args[1]);
			int[] control = new int[]{(int)args[2], (int)args[3], (int)args[4], (int)args[5]};
			return new Strip(control,c);
		}else{
			int[] control = new int[]{(int)args[1], (int)args[2], (int)args[3], (int)args[4]};
			return new Strip(control);
		}		
	}

	@Override
	public Object[] store(Strip obj) {
		if( obj.color==null ) return new Object[]{STRIP, obj.start[0], obj.start[1], obj.end[2], obj.end[3]};
		else  return new Object[]{STRIP, c_instance.store(obj.color), obj.start[0], obj.start[1], obj.end[2], obj.end[3]};
	}
}