package quilt.factory;


import fun_pl.semantic.FunCommand;
import unalcol.gui.util.Instance;
import unalcol.types.collection.keymap.HTKeyMap;
import unalcol.types.collection.keymap.ImmutableKeyMap;
import unalcol.types.collection.keymap.KeyMap;

/**
*
* QuiltCommandInstance
* <P>Load and Store mechanism of Quilt commands (Persistent methods)
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/factory/QuiltCommandInstance.java" target="_blank">
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
public class QuiltCommandInstance implements Instance<ImmutableKeyMap<String,FunCommand>> {
	protected HTKeyMap<String, FunCommand> map = new HTKeyMap<String,FunCommand>();
	public static final String COMMANDS="commands";

	public void register( FunCommand c ){ map.set(c.name(), c); }
	
	public void clear(){ map.clear(); }
	
	@Override
	public ImmutableKeyMap<String,FunCommand> load(Object[] args) {
		if( args.length<2 || !COMMANDS.equals(args[0]) ) return null;
		KeyMap<String, FunCommand> c = new HTKeyMap<String,FunCommand>();
		for( int i=1; i<args.length; i++) c.set( (String)args[i], map.get((String)args[i]) ); 
		return c;
	}

	@Override
	public Object[] store(ImmutableKeyMap<String,FunCommand> obj) {
		Object[] code = new Object[obj.size()+1];
		code[0] = COMMANDS;
		int i=1;
		for( FunCommand c:obj ){
			code[i] = c.name();
			i++;
		}
		return code;
	}
}