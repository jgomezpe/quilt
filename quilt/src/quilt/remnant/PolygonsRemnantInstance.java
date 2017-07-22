package quilt.remnant;

import quilt.Remnant;
import quilt.gui.Polygon;
import quilt.gui.PolygonInstance;
import unalcol.gui.paint.ColorInstance;
import unalcol.gui.util.Instance;

/**
*
* PolygonsRemnantInstance
* <P>Load and Store mechanism of PolygonRemnants (Persistent methods)
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/remnant/PolygonsRemnantInstance.java" target="_blank">
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
public class PolygonsRemnantInstance implements Instance<Remnant>{
	public static final String POLYGONS="polygons";
	protected ColorInstance c = new ColorInstance();
	protected PolygonInstance p = new PolygonInstance(); 

	@Override
	public Remnant load(Object[] args) {
		if( args.length<2 || !POLYGONS.equals(args[0])) return null;
		Polygon[] pol = new Polygon[args.length-2];
		for( int i=0; i<pol.length; i++ ) pol[i] = p.load((Object[])args[i+2]); 
		return new PolygonsRemnant(c.load((Object[])args[1]), pol);
	}

	@Override
	public Object[] store(Remnant r) {
		if( !(r instanceof PolygonsRemnant)) return null;
		PolygonsRemnant pol = (PolygonsRemnant)r;
		Object[] obj = new Object[2+pol.p.length];
		obj[0] = POLYGONS;
		obj[1] = c.store(pol.color);
		for( int i=0; i<pol.p.length; i++ ) obj[2+i] = p.store(pol.p[i]);
		return obj;
	}	
/*
	public static void main( String[] args ){
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
		PolygonRemnant r = new PolygonRemnant(new Color(255,0,0,255), new Polygon[]{p});
		PolygonRemnantInstance pr = new PolygonRemnantInstance();
		obj = pr.store(r);
		str = ObjectParser.store(obj);
		System.out.println(str);		
	} 
	*/	
}