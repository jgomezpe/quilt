package quilt.factory;

import fun_pl.semantic.FunCommand;
import fun_pl.semantic.FunCommandInstance;
import fun_pl.semantic.FunMachine;
import fun_pl.semantic.FunMachineInstance;
import fun_pl.semantic.FunValueInstance;
import quilt.MatrixQuilt;
import quilt.NilQuilt;
import quilt.QuiltInstance;
import quilt.QuiltMachine;
import quilt.Quilt;
import quilt.operation.Rotate;
import quilt.operation.Sew;
import quilt.remnant.EmptyRemnant;
import quilt.remnant.EmptyRemnantInstance;
import quilt.remnant.FilledRemnant;
import quilt.remnant.FilledRemnantInstance;
import quilt.remnant.ImageRemnant;
import quilt.remnant.ImageRemnantInstance;
import quilt.remnant.PolygonsRemnant;
import quilt.remnant.PolygonsRemnantInstance;
import quilt.remnant.StripsRemnant;
import quilt.remnant.StripsRemnantInstance;
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
public class QuiltMachineInstance extends FunMachineInstance<Quilt> {
	public static final String MACHINE="machine";

	@Override
	public FunMachine init(ImmutableKeyMap<String, FunCommand> commands, ImmutableKeyMap<String, Quilt> remnants) {
		return new QuiltMachine(commands, remnants);
	}

	@Override
	public void initCommands() {
		commands = new FunCommandInstance();
		commands.register(new Sew(null));
		commands.register(new Rotate(null));
	}

	@Override
	public void initValues() {
		values = new FunValueInstance<Quilt>("remnants");
		values.register(StripsRemnantInstance.STRIPS, StripsRemnant.class.getName(), new StripsRemnantInstance());	
		values.register(QuiltInstance.QUILT, MatrixQuilt.class.getName(), new QuiltInstance(values.factory()));			
		values.register(FilledRemnantInstance.FILLED, FilledRemnant.class.getName(), new FilledRemnantInstance());
		values.register(PolygonsRemnantInstance.POLYGONS, PolygonsRemnant.class.getName(), new PolygonsRemnantInstance());
		values.register(EmptyRemnantInstance.EMPTY, EmptyRemnant.class.getName(), new EmptyRemnantInstance());
		values.register(NilQuiltInstance.NIL, NilQuilt.class.getName(), new NilQuiltInstance());
		values.register(ImageRemnantInstance.IMAGE, ImageRemnant.class.getName(), new ImageRemnantInstance());
	}
}