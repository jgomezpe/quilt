package funpl.gui;

import java.io.IOException;

import funpl.FunAPI;
import nsgl.app.Component;
import nsgl.generic.hashmap.HashMap;
import nsgl.gui.Console;
import nsgl.gui.Render;
import nsgl.string.I18N;
import nsgl.gui.Editor;

public class Application implements nsgl.app.Application{
    public String PROGRAM = "program";
    public String COMMAND = "command";
    public String RENDER = "render";
    public String CONSOLE = "console";
    protected HashMap<String, Component> component = new HashMap<String, Component>();
    protected String id;
    protected FunAPI api;
    
    public Application(String id, Editor program, Editor command, 
	    Console console, Render render, FunAPI api){ 
	this.id = id;
	this.api = api;
	if( program instanceof nsgl.js.Component ) PROGRAM = ((nsgl.js.Component)program).id();
	component.set(PROGRAM, program);
	if( command instanceof nsgl.js.Component ) COMMAND = ((nsgl.js.Component)command).id();
	component.set(COMMAND, command);
	if( console instanceof nsgl.js.Component ) RENDER = ((nsgl.js.Component)render).id();
	component.set(RENDER, render);
	if( console instanceof nsgl.js.Component ) CONSOLE = ((nsgl.js.Component)console).id();
	component.set(CONSOLE, console);	
    }
  
    public String i18n(String code){ return I18N.process(code); }
 
    public void compile() { compile(program().getText()); }
    
    public void compile( String code ) {
	try {
	    api.compile(code);
	    ((Console)get(CONSOLE)).out(i18n("·No errors·"));
	} catch (IOException e) {
	    ((Console)get(CONSOLE)).error(i18n(e.getMessage()));
	}
    }

    public Object execute() { return execute(command().getText()); }
    
    public Object execute( String command ) {
	try {
	    Object obj = api.run(command);
	    ((Console)get(CONSOLE)).out(i18n("·No errors·"));
	    render().render(obj);
	    return obj;
	} catch (Exception e) {
	    e.printStackTrace();
	    ((Console)get(CONSOLE)).error(i18n(e.getMessage()));
	    return null;
	}
    }
    
    public Editor program() { return editor(PROGRAM); }
    public Editor command() { return editor(COMMAND); }
    protected Editor editor(String id) { return (Editor)get(id); }
    
    public Console console() { return (Console)get(CONSOLE); }
    
    public Render render() { return (Render)get(RENDER); }
       
    @Override
    public Component get(String id){ return component.get(id); }

    @Override
    public void id(String id) { this.id = id; }
    
    @Override
    public String id() { return id; }
}