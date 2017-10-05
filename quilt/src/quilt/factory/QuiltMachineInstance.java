package quilt.factory;

import fun_pl.semantic.FunCommand;
import quilt.MatrixQuilt;
import quilt.QuiltInstance;
import quilt.QuiltMachine;
import quilt.Quilt;
import quilt.operation.Rotate;
import quilt.operation.Sew;
import quilt.remnant.EmptyRemnant;
import quilt.remnant.EmptyRemnantInstance;
import quilt.remnant.FilledRemnant;
import quilt.remnant.FilledRemnantInstance;
import quilt.remnant.PolygonsRemnant;
import quilt.remnant.PolygonsRemnantInstance;
import quilt.remnant.StripsRemnant;
import quilt.remnant.StripsRemnantInstance;
import unalcol.gui.util.Instance;
import unalcol.types.collection.Collection;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.keymap.ImmutableKeyMap;

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
public class QuiltMachineInstance implements Instance<QuiltMachine> {
	public static final String MACHINE="machine";
	
	protected QuiltCommandInstance commands = new QuiltCommandInstance();
	protected QuiltValuesInstance remnants = new QuiltValuesInstance();
	public QuiltMachineInstance() {
		commands.register(new Sew());
		commands.register(new Rotate());		
		remnants.register(StripsRemnantInstance.STRIPS, StripsRemnant.class.getName(), new StripsRemnantInstance());	
		remnants.register(QuiltInstance.QUILT, MatrixQuilt.class.getName(), new QuiltInstance(remnants.factory()));			
		remnants.register(FilledRemnantInstance.FILLED, FilledRemnant.class.getName(), new FilledRemnantInstance());
		remnants.register(PolygonsRemnantInstance.POLYGONS, PolygonsRemnant.class.getName(), new PolygonsRemnantInstance());
		remnants.register(EmptyRemnantInstance.EMPTY, EmptyRemnant.class.getName(), new EmptyRemnantInstance());
	}
	
	@Override
	public QuiltMachine load(Object[] args) {
		if( args.length!=3 || !MACHINE.equals(args[0]) ) return null;
		ImmutableKeyMap<String, FunCommand> c = commands.load((Object[])args[1]);
		ImmutableKeyMap<String, Quilt> r = remnants.load((Object[])args[2]);
		return new QuiltMachine(c, r);
	}

	@Override
	public Object[] store(QuiltMachine obj) {
		Collection<String> keys = obj.values();
		HTKeyMap<String,Quilt> r = new HTKeyMap<String,Quilt>();
		if( keys!=null ) for( String val:keys ) try{ r.set(val, (Quilt)obj.value(val)); }catch(Exception e){}
		HTKeyMap<String,FunCommand> c = new HTKeyMap<String,FunCommand>();
		for( String name:obj.primitives() ) try{ c.set(name, obj.primitive(name)); }catch(Exception e){}
		return new Object[]{MACHINE,commands.store(c),remnants.store(r)};
	}
}