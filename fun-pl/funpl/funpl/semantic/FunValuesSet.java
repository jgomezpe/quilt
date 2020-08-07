package funpl.semantic;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public interface FunValuesSet {
	default boolean valid(String val){ 
		try{ 
			obtain(val);
			return true;
		}catch(Exception e) {}
		return false;
	}
	Object obtain(String value) throws NoSuchElementException;
	
	String parse( Iterator<Integer> iter ) throws IOException;	
}
