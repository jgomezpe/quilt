package quilt.factory;

import java.util.Hashtable;

import quilt.Remnant;
import quilt.gui.Color;
import quilt.remnant.FilledRemnant;
import quilt.remnant.StripsRemnant;

public class RemnantFactory {
	public static Remnant diag(Color c){
		return new StripsRemnant( c, new int[][]{ {40,0,100,60}, {50,0,100,50}, {60,0,100,40} } );
	}
	
	public static Remnant corner(Color c){
		return new StripsRemnant( c, new int[][]{ {40,0,40,60}, {40,60,100,60}, {50,0,50,50}, {50,50,100,50}, {60,0,60,40}, {60,40,100,40} } );
	}

	public static Remnant zero(Color c){
		return new StripsRemnant( c, new int[][]{ {0,40,100,40}, {0,50,100,50}, {0,60,100,60} } );
	}

	public static Remnant empty(){
		return new StripsRemnant( new Color(255,255,255,255), new int[][]{} );
	}

	public static Remnant filled(Color c){
		return new FilledRemnant( c, 50 );
	}
	
	public static Hashtable<String, Remnant> little(String DIAG, String SQUA, Color c){
		Hashtable<String, Remnant> t = new Hashtable<String,Remnant>();
		t.put(DIAG, diag(c));
		t.put(SQUA, corner(c));
		return t;
	}

	public static Hashtable<String, Remnant> little2(String TAG1, Color c1, String TAG2, Color c2){
		Hashtable<String, Remnant> t = new Hashtable<String,Remnant>();
		t.put(TAG1, diag(c1));
		t.put(TAG2, diag(c2));
		return t;
	}	

	public static Hashtable<String, Remnant> little3(String DIAG, String SQUA, String LINE, Color c){
		Hashtable<String, Remnant> t = new Hashtable<String,Remnant>();
		t.put(DIAG, diag(c));
		t.put(SQUA, corner(c));
		t.put(LINE, zero(c));
		return t;
	}	
	
	public static Hashtable<String, Remnant> single( String TAG, Remnant r ){
		Hashtable<String, Remnant> t = new Hashtable<String,Remnant>();
		t.put(TAG, r);
		return t;
	}
}