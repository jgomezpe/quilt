package funpl.gui;

import java.io.IOException;

import funpl.FunAPI;
import nsgl.app.Component;
import nsgl.character.CharacterSequence;
import nsgl.generic.Configurable;
import nsgl.generic.Named;
import nsgl.generic.hashmap.HashMap;
import nsgl.gui.Console;
import nsgl.gui.Render;
import nsgl.json.JSON;
import nsgl.string.I18N;
import nsgl.gui.Editor;

public class Application implements nsgl.app.Application, Configurable{
    public String PROGRAM = "program";
    public String COMMAND = "command";
    public String RENDER = "render";
    public String CONSOLE = "console";
    protected HashMap<String, Component> component = new HashMap<String, Component>();
    protected String id = "funpl";
    protected FunAPI api;
    
    public Application() {}

    public Application(String id, Editor program, Editor command, Console console, Render render, FunAPI api){ 
	this.id = id;
	this.api = api;
	if( program instanceof Named ) PROGRAM = ((Named)program).id();
	component.set(PROGRAM, program);
	if( command instanceof Named ) COMMAND = ((Named)command).id();
	component.set(COMMAND, command);
	if( render instanceof Named ) RENDER = ((Named)render).id();
	component.set(RENDER, render);
	if( console instanceof Named ) CONSOLE = ((Named)console).id();
	component.set(CONSOLE, console);	
    }
  
    public String i18n(String code){ return I18N.process(code); }
 
    public void compile() { compile(program().getText()); }
    
    public void compile( String code ) {
	try {
	    api.compile(new CharacterSequence(code,PROGRAM));
	    ((Console)get(CONSOLE)).out(i18n("·No errors·"));
	} catch (IOException e) {
	    ((Console)get(CONSOLE)).error(i18n(e.getMessage()));
	}
    }

    public Object execute() { return execute(command().getText()); }
    
    public Object execute( String command ) {
	try {
	    Object obj = api.run(new CharacterSequence(command,COMMAND));
	    ((Console)get(CONSOLE)).out(i18n("·No errors·"));
	    render().render(obj);
	    return obj;
	} catch (Exception e) {
	    ((Console)get(CONSOLE)).error(i18n(e.getMessage()));
	    return null;
	}
    }
    
    public Object apply() { return apply(command().getText()); }
    
    public Object apply( String command ) {
	try {
	    Object obj = api.apply(new CharacterSequence(command,COMMAND));
	    ((Console)get(CONSOLE)).out(i18n("·No errors·"));
	    render().render(obj);
	    return obj;
	} catch (Exception e) {
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

    @Override
    public void config(JSON json) {
	if( json.string("id")!=null ) id = json.string("id");
	String tag = json.string("program");
	if( tag!=null ) {
	    component.set(tag, program());
	    PROGRAM = tag;
	}
	tag = json.string("command");
	if( tag!=null ) {
	    component.set(tag, command());
	    COMMAND = tag;
	}
	tag = json.string("console");
	if( tag!=null ) {
	    component.set(tag, console());
	    CONSOLE = tag;
	}
	tag = json.string("render");
	if( tag!=null ) {
	    component.set(tag, render());
	    RENDER = tag;
	}
	api.config(json.object("api"));
    }

    @Override
    public boolean accessible(String object, String method) {
	// TODO Auto-generated method stub
	return true;
    }

    @Override
    public boolean authorized(JSON user, String object, String method) {
	// TODO Auto-generated method stub
	return false;
    }
}