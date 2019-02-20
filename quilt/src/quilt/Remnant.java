package quilt;

import quilt.operation.RemnantFunction;
import unalcol.gui.paint.Canvas;
import unalcol.gui.paint.Color;
import unalcol.gui.paint.ColorInstance;
import unalcol.json.JSON;
import unalcol.collection.keymap.HashMap;
import unalcol.object.Named;

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
public class Remnant extends Quilt implements Named{
	protected static HashMap<String,JSON> drawJSON = new HashMap<String,JSON>();
	public static void add( String remnant, JSON json ){ drawJSON.set(remnant, json); }
	public static JSON get( String remnant ){ try{ return drawJSON.get(remnant); }catch(Exception e){ return null; } }
	
	protected String command="";

	public Remnant(String id){ this(id,null); }
	
	public Remnant(String id, QuiltMachine machine){
		super(machine);
		command = id;
	}
	
	public void add( String command ){ this.command = machine.reduce(command + ' ' + this.command);	}
	
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
	
	public void translate(JSON json, int dx, int dy) {
		String command = json.getString(Canvas.COMMAND);
		if( command==null ) return;
		if( command.equals(Canvas.COMPOUND) ){
			Object[] objs = json.getArray(Canvas.COMMANDS);
			for(Object o:objs) translate((JSON)o, dx, dy);
		}else{
			Object x; 
			Object y;
			x = json.getArray(Canvas.X);
			if( x!=null ) y = json.getArray(Canvas.Y);
			else{
				x = json.getInt(Canvas.X);
				y = json.getInt(Canvas.Y);
			}
			if( x instanceof Object[] ){
				Object[] tx = (Object[])x;
				Object[] ty = (Object[])y;
				for(int i=0; i<tx.length; i++ ){
					tx[i] = (Integer)tx[i]+dx;
					ty[i] = (Integer)ty[i]+dy;
				}
			}else{
				json.set(Canvas.X, (Integer)x+dx);
				json.set(Canvas.Y, (Integer)y+dy);
				json.set(Canvas.WIDTH, Quilt.UNIT);
				json.set(Canvas.HEIGHT, Quilt.UNIT);
			}
		}
	}
	
	
	public JSON border( int row, int column ){
		int one = unit();
		JSON border = new JSON();
		border.set(Canvas.COMMAND, Canvas.RECT );
		border.set(Canvas.WIDTH, one);
		border.set(Canvas.HEIGHT, one);
		ColorInstance ci = new ColorInstance();
		JSON color = ci.store( new Color(0, 0, 0, 0) );
		border.set(ColorInstance.COLOR, color);
		border.set(Canvas.X, column);
		border.set(Canvas.Y, row);
		return border;
	}

	@Override
	public JSON draw( int column, int row ){
		column = units(column);
		row = units(row);
		JSON border = border(row,column);
		String[] commands = command.split(" ");
		int n = commands.length;
		JSON json = (JSON)Remnant.get(commands[n-1]).clone();
		if( json != null ){
			for( int i=n-2; i>=0; i--){
				RemnantFunction rf = (RemnantFunction)machine.primitive(commands[i]);
				rf.apply(json);
			}
			translate(json, column, row);
			Object[] v = new Object[2];
			v[0]=border;
			v[1]=json;
			json = new JSON();
			json.set(Canvas.COMMAND, Canvas.COMPOUND);
			json.set(Canvas.COMMANDS, v);
		}else json = border;
		return json;
	}

	@Override
	public Object clone(){ return new Remnant(command, machine); }

	@Override
	public boolean equals(Quilt quilt) {
		if(quilt.rows()==1 && quilt.columns()==1){
			Remnant r = quilt.get(0, 0);
			return command.equals(r.command);
		}
		return false;
	}
	
	@Override
	public String id(){ return command; }
	@Override
	
	public void setId(String id){ command = id; }
}