package quilt.render;

import quilt.util.Util;
import unalcol.gui.paint.ColorInstance;
import unalcol.util.Instance;

/**
*
* PolygonInstance
* <P>Load and Store mechanism of Polygons (Persistent methods)
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/gui/PolygonInstance.java" target="_blank">
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
public class PolygonInstance implements Instance<Polygon>{
	public static final String POLYGON="polygon";
	
	protected ColorInstance c = new ColorInstance();
	
	@Override
	public Polygon load(Object[] args) {
		if(args.length<3 || args.length>4 || !POLYGON.equals(args[0]) ) return null;
		return ( args.length == 3 )? new Polygon(Util.load((Object[])args[1], 0),Util.load((Object[])args[2], 0)) :
			new Polygon(Util.load((Object[])args[1], 0),Util.load((Object[])args[2], 0),c.load((Object[])args[3]));
	}

	@Override
	public Object[] store(Polygon p) {
		return p.color!=null?new Object[]{POLYGON,c.store(p.color),Util.store(p.x),Util.store(p.y)}:new Object[]{POLYGON,Util.store(p.x),Util.store(p.y)};
	}
	
/*	public static void main( String[] args ){
		int[] x = new int[]{40,60,60,40};
		int[] y = new int[]{0,0,100,100};
		Polygon p = new Polygon(x, y);
		PolygonInstance pi = new PolygonInstance();
		Object[] obj = pi.store(p);
		String str = ObjectParser.store(obj);
		System.out.println(str);
		p.rotate(Remnant.UNIT);
		obj = pi.store(p);
		str = ObjectParser.store(obj);
		System.out.println(str);
	} */	
}