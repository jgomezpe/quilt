package qm.operation;

import funpl.semantic.FunCommand;
import funpl.semantic.FunMachine;
import funpl.util.FunConstants;
import utila.I18N;
import qm.quilt.Matrix;
import qm.quilt.Nil;
import qm.quilt.Quilt;
import qm.remnant.Remnant;
import qm.util.QuiltConstants;

/**
*
* Sew
* <P>Sews two quilts (vertical direction) if the quilts have the same number of rows (high).
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt/src/quilt/Sew.java" target="_blank">
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
public class Sew extends FunCommand{
	public Sew() { super(); }
	public Sew(FunMachine machine) { super( machine); } //		super(QuiltMachine.SEW, new String[]{"X", "Y"});

	public Quilt execute( Quilt left, Quilt right ) throws Exception{
		if(left instanceof Nil) return right;
		if(right instanceof Nil) return left;
		if( left.rows() == right.rows() ) return new Matrix( left, right );
		throw exception(QuiltConstants.STITCH);
	}

	public boolean check_right(Quilt quilt, Quilt right){
		int c = quilt.columns();
		int r = quilt.rows();
		int rc = right.columns(); 
		boolean nofail=(r==right.rows() && c>=rc);
		int k=c-rc;
		for( int i=0;i<r&&nofail;i++ )
			for( int j=0; j<rc&&nofail; j++ )
				nofail = quilt.get(i, j+k).equals(right.get(i, j));
		return nofail;
	}
	
	public boolean check_left(Quilt quilt, Quilt left){
		int c = quilt.columns();
		int r = quilt.rows();
		int k = left.columns(); 
		boolean nofail=(r==left.rows() && c>=k);
		for( int i=0;i<r&&nofail;i++ )
			for( int j=0; j<k&&nofail; j++ )
				nofail = quilt.get(i, j).equals(left.get(i, j));
		return nofail;
	}
	
	public Quilt getRight( Quilt quilt, int k ){
		if( k==0 ) return new Nil();
		int c = quilt.columns();
		int r = quilt.rows();
		int sc = c-k;
		Remnant[][] right = new Remnant[r][k];
		for( int i=0; i<r; i++ ) for( int j=0; j<k; j++) right[i][j] = quilt.get(i,j+sc);
		return (r==1 && k==1)? right[0][0]: new Matrix(right);
	}
	
	public Quilt getLeft( Quilt quilt, int k ){
		if( k==0 ) return new Nil();
		int r = quilt.rows();
		Remnant[][] left = new Remnant[r][k];
		for( int i=0; i<r; i++ ) for( int j=0; j<k; j++) left[i][j] = quilt.get(i,j);
		return (r==1 && k==1)? left[0][0]: new Matrix(left);
	}
	
	protected Exception exception(){
		return exception(QuiltConstants.UNSTITCH); 
	}
	
	public Quilt[] reverse(Quilt obj, Quilt left, Quilt right) throws Exception{	
		int c = obj.columns();
		/*
		if( c!=1 && ((left!=null && left instanceof NilQuilt) || (right!=null && right instanceof NilQuilt)) ) throw exception(); 
		if( c==1 && left!=null && left instanceof NilQuilt ) return new Quilt[]{left,obj}; 
		if( c==1 && right!=null && right instanceof NilQuilt ) return new Quilt[]{obj,right};
		*/
		if(c<2) throw exception();
		
		if(left==null && right==null ){
//					return new Quilt[]{getLeft(obj, 1),getRight(obj, c-1)};
				return new Quilt[]{getLeft(obj, c-1),getRight(obj, 1)};
		}else if(left==null ){
				if( !check_right(obj, right) ) throw exception();
				return new Quilt[]{getLeft(obj, c-right.columns()), right};
		}else if( right==null ){
			if( !check_left(obj, left) ) throw exception();
			return new Quilt[]{left, getRight(obj, c-left.columns())};
		}else{
			if( !check_left(obj, left) || !check_right(obj,right) || c!=left.columns()+right.columns()) throw exception();
			return new Quilt[]{left,right};
		}
	}

	@Override
	public Object[] reverse(Object obj, Object[] args) throws Exception{ return reverse((Quilt)obj, (Quilt)args[0], (Quilt)args[1]); }

	@Override
	public Object execute(Object... args) throws Exception {
		if( !(args[0] instanceof Quilt) ) throw exception(FunConstants.argmismatch + args[0]);
		if( !(args[1] instanceof Quilt) ) throw exception(FunConstants.argmismatch + args[1]);
		return execute((Quilt)args[0], (Quilt)args[1]); 
	}

	@Override
	public String name(){ return "|"; }
	
	public String comment(){ return I18N.process("·|·"); }

	@Override
	public int arity() { return 2; }	
}