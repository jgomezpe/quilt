package quilt.factory;

import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunMachineInstance;
import fun_pl.semantic.FunSymbolCommand;
import quilt.MatrixQuilt;
import quilt.NilQuilt;
import quilt.QuiltMachine;
import quilt.Remnant;
import quilt.Quilt;
import quilt.operation.Rotate;
import quilt.operation.Sew;
import unalcol.json.JSON;
import unalcol.collection.keymap.HashMap;
import unalcol.collection.keymap.Immutable;

/**
*
* QuiltMachineInstance
* <P>Load and Store mechanism of a Quilt machine (Persistent methods)
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/factory/QuiltMachineInstance.java" target="_blank">
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
public class QuiltMachineInstance extends FunMachineInstance<Quilt> {
	public static final String MACHINE="machine";
	public static final String REDUCTIONS="reductions";
	public static final String IN="in";
	public static final String OUT="out";

	@Override
	public void initCommands() {
		primitives.clear();
		FunSymbolCommand c = new Sew(null);
		primitives.set(c.name(),c);
		c = new Rotate(null);
		primitives.set(c.name(),c);
	}

	@Override
	public void initValues() {
		QuiltInstance qinstance = new QuiltInstance(true);
		factory.register(QuiltInstance.QUILT, MatrixQuilt.class.getName(), qinstance);
		factory.register(QuiltInstance.NIL, NilQuilt.class.getName(), qinstance);
		factory.register(RemnantInstance.REMNANT, Remnant.class.getName(), qinstance);
	}
	
	@Override
	public FunMachine init(Immutable<String, FunSymbolCommand> commands, String symbol){ return new QuiltMachine(commands, symbol); }

	protected Immutable<String, String> reductions(JSON json){
		HashMap<String, String> reductions = new HashMap<String,String>();
		try{
			JSON r =(JSON)json.get(REDUCTIONS);
			for( String in:r.keys() ) reductions.set(in, (String)r.get(in));
		}catch(Exception e){}
		return reductions;
	}
	
	@Override
	public FunMachine load(JSON json){
		QuiltMachine machine = (QuiltMachine)super.load(json);
		machine.setReductions(reductions(json));
		return machine;
	}
	
	@Override
	public JSON store(FunMachine machine){
		JSON json = super.store(machine);
		Immutable<String, String> reductions = ((QuiltMachine)machine).reductions();
		if( reductions != null && reductions.size()>0 ){
			JSON r = new JSON();
			try{ for( String in:reductions.keys()) r.set(in, reductions.get(in)); }catch(Exception e){}
			json.set(REDUCTIONS, r);
		}
		return json;
	}
}