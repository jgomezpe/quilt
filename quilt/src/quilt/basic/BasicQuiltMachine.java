package quilt.basic;

import java.awt.Color;
import java.util.Hashtable;

import quilt.Language;
import quilt.QuiltMachine;
import quilt.Remnant;
import quilt.StripsRemnant;
import quilt.operation.Command;
import quilt.operation.CommandCall;
import quilt.operation.CommandDef;
import quilt.operation.QuiltMachineParser;

/**
*
* BasicQuiltMachine
* <P>A quilt machine that has defined rotate and sew operations and uses three types of remnants
* two StripsRemnants (diagonal and square) and natural number (without the zero).
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/basic/BasicQuiltMachine.java" target="_blank">
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
public class BasicQuiltMachine extends QuiltMachine{
	protected static final String DIAGONAL = "diag";
	protected static final String SQUARE = "squa";
	
	public static final String ROTATE = "rot";
	public static final String SEW = "sew";

	public BasicQuiltMachine(){
		this(new Language());
	}
	
	public BasicQuiltMachine(Language message){
		super(message);
		remnants.put(DIAGONAL, new StripsRemnant(Color.green, 
				new int[][]{ {40,0,100,60}, {50,0,100,50}, {60,0,100,40} } ) );
		remnants.put(SQUARE, new StripsRemnant(Color.red, 
				new int[][]{ {40,0,40,60}, {40,60,100,60},
				 {50,0,50,50}, {50,50,100,50},
				 {60,0,60,40}, {60,40,100,40} }) );
	}
	
	public Remnant remnant(String remnant) {
		try{
			int n = Integer.parseInt(remnant);
			return new NaturalNumberRemnant(n, (StripsRemnant)remnants.get(DIAGONAL));
		}catch( NumberFormatException e ){}
		return super.remnant(remnant);
	}

	public Command[] primitives() {
		return new Command[]{ new Rotate(), new Sew()};
	}

	public Remnant execute(String command) throws Exception{
		QuiltMachineParser parser = new QuiltMachineParser(message, command);
		CommandCall comm = parser.command();
		return comm.execute(this, new Hashtable<String,Remnant>());
	}
	
	public void addDef( String program ) throws Exception{
		QuiltMachineParser parser = new QuiltMachineParser(message, program);
		CommandDef[] comm_def = parser.apply();
		this.add(comm_def);
	}
	
	public void setProgram( String program ) throws Exception{
		init();
		addDef(program);
	}

	public static void main( String[] args ){
		Language message = new Language();
		BasicQuiltMachine m = new BasicQuiltMachine(message);
		try{
			Remnant r = m.execute("sew(rot(sew(diag,diag)),rot(sew(squa,squa)))");
			System.out.println(r);
		}catch( Exception e ){
			System.out.println(e.getMessage());
		}
	}
}
