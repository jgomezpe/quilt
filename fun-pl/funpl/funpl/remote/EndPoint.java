package funpl.remote;

import funpl.FunAPI;

public class EndPoint extends nsgl.app.web.EndPoint{
    /**
     * 
     */
    private static final long serialVersionUID = 3184723192570333996L;

    public EndPoint( String id, FunAPI api ) {
	server =  new Application(id, api, this);
    }
}