package funpl.remote;

import funpl.FunAPI;
import nsgl.app.net.Channel;
import nsgl.app.remote.Console;
import nsgl.app.remote.Editor;
import nsgl.app.remote.Render;

public class Application extends funpl.gui.Application{
    public Application(String id, FunAPI api, Channel channel) {
	super(id, new Editor("program",channel), new Editor("command",channel), 
		new Console("console", channel), new Render("render", channel), api);
    }
}