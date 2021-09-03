package qm.awt;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import aplikigo.Component;
import aplikigo.gui.Render;
import speco.jxon.JXON;

public class QuiltTupleRender extends JPanel implements Render{
    /**
     * 
     */
    private static final long serialVersionUID = -1876658510765742067L;

    protected JXON cfg = null;
    
    protected String id;
	
    BorderLayout layout = new BorderLayout();
    JTabbedPane tabbedPane = new JTabbedPane();
    
    public QuiltTupleRender() { 
	this.setLayout(layout);
	this.add(tabbedPane,BorderLayout.CENTER);
    }
    
    @Override
    public Component get(String component) {
	if( component.equals(id()) ) return this;
	return null;
    }

    @Override
    public String id() { return id; }

    @Override
    public void id(String id) { this.id = id; }

    @Override
    public void config(JXON jxon) { cfg = jxon; }

    @Override
    public void init() {}

    @Override
    public void render(Object obj) {
	tabbedPane.removeAll();
	Object[] c;
	if(obj instanceof Object[]) {
	   c = (Object[])obj; 
	}else { c = new Object[] {obj}; }
	for(int i=0; i<c.length; i++) {
	    QuiltRender qr = new QuiltRender();
	    qr.config(cfg);
	    qr.render(c[i]);
	    tabbedPane.addTab("["+i+"]", qr);
	}
    }
}
