package quilt.remnant;

import quilt.Remnant;
import quilt.Quilt;
import quilt.gui.Drawer;
import unalcol.gui.paint.Color;

/**
*
* FilledRemnant
* <P>A remnant having a single color defined by a centered square.
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/remnant/FilledRemnant.java" target="_blank">
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
public class FilledRemnant extends Remnant{
	protected Color color;
	protected int side;

	public FilledRemnant( Color color, int side ) {
		this.color = color;
		this.side = side;
	}

	public Quilt clone(){	return new FilledRemnant(color(), side);	}
	
	public Color color(){ return color;	}
	
	public int side(){ return side; }
	
	@Override
	public void draw( Drawer g, int column, int row ){
		super.draw(g,column,row);
		column = units(column);
		row = units(row);
		int one = unit();
		g.setColor(color());
		g.drawFill(column+(one-side)/2, row+(one-side)/2, side, side);
	}

	public boolean equals( Quilt r ){
		r = check(r);
		if( r!=null && r instanceof FilledRemnant ){
			FilledRemnant other = (FilledRemnant)r;
			return super.equals(r) && side==other.side;
		}
		return false;
	}	
}