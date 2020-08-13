package qm.remnant;

import nsgl.generic.Named;
import nsgl.gui.paint.Command;
import nsgl.json.JSON;
import qm.quilt.Quilt;
import qm.util.QuiltConstants;

/**
*
* MinRemnant
* <P>Minimal Remnant used by a Quilt.
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/MinRemnant.java" target="_blank">
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
public abstract class Remnant implements Quilt, Named{
	public static boolean border=true;
	
	protected String id="";

	public Remnant(String id) { this.id = id; }
	
	public int rows(){ return 1; }

	public int columns(){ return 1; }
	
	public Quilt check( Quilt r ){
		if( r!=null && r.rows()==1 && r.columns()==1 && !(r instanceof Remnant) ) return r.get(0, 0);
		return r;
	}
	
	@Override
	public Remnant get(int r, int c) {
		if( r==0 &&  c==0 ) return this;
		return null;
	}
		
	public abstract Object clone();
	
	public JSON border(){ return Command.create(QuiltConstants.BORDER); }

	@Override
	public JSON draw(){
		Object[] commands = new Object[border?2:1];
		int n=0;
		if( border ) {
			commands[0] = border();
			n++;
		}
		commands[n] = Command.create(id); 		
		JSON json = Command.create(Command.COMPOUND);
		json.set(Command.COMMANDS, commands);
		return json;
	}

	@Override
	public boolean equals(Object obj) {
		Quilt q = (Quilt)obj; 
		if(q.rows()==1 && q.columns()==1){
			Remnant r = q.get(0, 0);
			return id.equals(r.id);
		}
		return false;
	}
	
	@Override
	public String id(){ return id; }

	@Override	
	public void id(String id){ this.id = id; }
}