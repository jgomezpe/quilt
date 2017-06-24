package quilt.util;

import quilt.remnant.Strip;

/**
*
* Strips
* <P>Utility for strips based Remnants.
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/strips/Strips.java" target="_blank">
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
public class Util {
	public static int compare(int[] one, int[] two) {
		if( one.length!=two.length ) return one.length-two.length;
		int k=0;
		while( k<one.length && one[k]==two[k] ) k++;
		if(k==one.length) return 0;
		else return one[k]-two[k];
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean compare( Comparable[] one, Comparable[] two ){
		if( one.length!=two.length ) return false;
		int i=0;
		while( i<one.length && one[i].compareTo(two[i])==0){
			i++;
		}
		return i==one.length;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void sort( Comparable[] strips ){
		int n = strips.length; 
		if(n<8){
			for( int i=0; i<n-1; i++ ){
				for( int j=i+1; j<n; j++ ){
					if( strips[i].compareTo(strips[j]) > 0 ){
						Comparable t = strips[i];
						strips[i] = strips[j];
						strips[j] = t;
					}
				}
			}
		}else{
			int m = n/2;
			Comparable[] L = new Comparable[m];
			Comparable[] R = new Strip[n-m];
			for( int i=0; i<m; i++){ L[i]=strips[i]; }
			int j=m;
			for( int i=0; i<R.length; i++){ R[i]=strips[j]; j++; }
			sort(L);
			sort(R);
			int i=0;
			j=0;
			int k=0;
			while(i<L.length&&j<R.length){
				if(L[i].compareTo(R[j])<0){
					strips[k] = L[i];
					i++;
				}else{
					strips[k] = R[j];
					j++;
				}
				k++;
			}
			while(i<L.length){
				strips[k] = L[i];
				i++;
				k++;
			}
			while(j<R.length){
				strips[k] = R[j];
				j++;
				k++;
			}
		}
	}
}