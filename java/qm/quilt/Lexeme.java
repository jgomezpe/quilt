package qm.quilt;

import lifya.lexeme.Words;
import lifya.Source;
import lifya.Token;

public class Lexeme extends Words{

	public Lexeme(String type, String[] word) {
		super(type, word);
	}

    @Override
    public Token match(Source input, int start, int end) {
    	int s = start;
    	while(s<end && startsWith(input.get(s))) {
    		Token t = super.match(input,s,end);
    		if(t.isError()) return t;
    		s = t.end();
    	}
    	if(s==start) return error(input,start,s);
	    String x = input.substring(start,s);
	    return token(input,start,s,x);
	}
}
