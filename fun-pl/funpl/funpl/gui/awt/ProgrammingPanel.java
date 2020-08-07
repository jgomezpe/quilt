package funpl.gui.awt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;
import javax.swing.text.JTextComponent;

import org.fife.ui.rsyntaxtextarea.Token;

import funpl.gui.GUIFunConstants;
import funpl.lexer.FunLexer;
import funpl.FunAPI;
import funpl.gui.Application;
import funpl.util.FunConstants;
import nsgl.generic.hashmap.HashMap;
import nsgl.generic.keymap.KeyMap;
import nsgl.gui.Render;
import nsgl.gui.TitleComponent;
import nsgl.gui.awt.Console;
import nsgl.gui.awt.FileFilter;
import nsgl.gui.awt.JFontChooser;
import nsgl.gui.awt.LogPanel;
import nsgl.gui.awt.rsyntax.Editor;
import nsgl.json.JXON;
import nsgl.language.lexeme.Space;
import nsgl.language.lexeme.Symbol;
import nsgl.stream.Resource;
import nsgl.stream.loader.FromOS;
import nsgl.string.I18N;


public class ProgrammingPanel  extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3339559167222155206L;

	protected static final String images="image/";
	protected static final String i18n="language/";
	
	String title;
	String fileName = null;
	String fileDir = ".";
	String fileDirStyle = ".";
	String fileDirMachine = ".";
	TitleComponent title_component;
	
	JSplitPane splitPane;
	protected Render render; 
	
	// Window area
	BorderLayout windowLayout = new BorderLayout();
	JPanel windowPanel = new JPanel();
	BorderLayout windowPaneLayout = new BorderLayout();
	
	// The program area
	Editor program_editor;
	
	// The tool bar
	JToolBar jToolBar = new JToolBar();
	JButton jNewButton = new JButton();
	JButton jOpenButton = new JButton();
	JButton jSaveButton = new JButton();
	JButton jCompileButton = new JButton();
	JButton jRemnantButton = new JButton();
	JButton jPrimitiveButton = new JButton();
	JButton jMachineButton = new JButton();
	JButton jStyleButton = new JButton();
	JButton jLangButton = new JButton();
	
	// The log area
	LogPanel logPanel = new LogPanel();
	Console log;   
	
	// The command area
	JPanel jCommandBar = new JPanel();
	BorderLayout commandLayout = new BorderLayout();
	JLabel jCommandLabel = new JLabel();
	Editor command_editor;
	JButton jCommandButton = new JButton();

	//Log command area
	BorderLayout logCommandLayout = new BorderLayout();
	JPanel logCommandArea = new JPanel();
	
	// Resources
	Resource resource = new Resource();
	
	//
	FunAPI api;
	Application app;
	protected KeyMap<Integer, ?> tokens;
	
	public LogPanel getLogPanel(){ return logPanel; }

	public ProgrammingPanel(TitleComponent parent, FunAPI api, String api_code, Render render ) {
		this.resource.add("local", new FromOS(""));
		this.render = render;
		this.api = api;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
		this.setSize(new Dimension(width*4/5, height*4/5));
		build(parent, api_code, width, height);
	}
	
	public static String i18n(String code){ return I18N.process(code); }
	
	public static KeyMap<String, Integer> rSyntaxEditorTokens(){
		KeyMap<String, Integer> converter = new HashMap<String, Integer>();
		converter.set(FunConstants.VALUE,Token.LITERAL_NUMBER_DECIMAL_INT);
		converter.set(FunConstants.COMMENT,Token.COMMENT_EOL);
		converter.set(FunConstants.VARIABLE, Token.VARIABLE);
		converter.set(Space.TAG, Token.WHITESPACE);
		converter.set(FunConstants.FUNCTION, Token.IDENTIFIER);
		converter.set(Symbol.TAG,Token.SEPARATOR);
		converter.set(FunConstants.PRIMITIVE, Token.OPERATOR);
		return converter;
	}
	
	public void setAPI( String file ) {
	    try {
		String api_code = readFile(file);
		if(api_code!=null) {
		    	JXON json = new JXON(api_code);
			api.config(json.getJXON(GUIFunConstants.FUN));
			render.config(json.getJXON(Render.TAG));
			KeyMap<String, Integer> map = rSyntaxEditorTokens();
			FunLexer lexer = api.lexer();
			program_editor.setLexer(lexer, map);
			command_editor.setLexer(api.lexer(), map);	
		}
	    }catch(Exception e) { log.error(i18n(e.getMessage())); }
	}
	
	public void setLanguage() {
		title =i18n(GUIFunConstants.TITLE);
		if( fileName == null ) fileName = i18n(GUIFunConstants.NONAME);
		this.title_component.setTitle(title + " [" + fileName + "]");
		jNewButton.setToolTipText(i18n(GUIFunConstants.NEW));
		jOpenButton.setToolTipText(i18n(GUIFunConstants.OPEN));
		jSaveButton.setToolTipText(i18n(GUIFunConstants.SAVE));
		jCompileButton.setToolTipText(i18n(GUIFunConstants.COMPILE));
		jRemnantButton.setToolTipText(i18n(GUIFunConstants.VALUE));
		jPrimitiveButton.setToolTipText(i18n(GUIFunConstants.PRIMITIVE));
		jMachineButton.setToolTipText(i18n(GUIFunConstants.MACHINE));
		jStyleButton.setToolTipText(i18n(GUIFunConstants.STYLE));
		jCommandButton.setToolTipText(i18n(GUIFunConstants.EXECUTE));
		jLangButton.setToolTipText(i18n(GUIFunConstants.LANGUAGE));
		logPanel.setLanguage(i18n(GUIFunConstants.OUT), i18n(GUIFunConstants.ERROR));
		jCommandLabel.setText(i18n(GUIFunConstants.COMMAND));
	}
	
	public void build( TitleComponent parent, String api, int width, int height ){
		this.title_component = parent;
		try{		
			this.setSize(new Dimension(width*4/5, height*4/5));
			this.setLayout(windowLayout);

			// Program area
			program_editor = new Editor("program");
			
			//program_editor.setStyle(SyntaxStyle.get(styles));
			JTextComponent jProgram = program_editor.editArea();
			jProgram.setToolTipText("");
			jProgram.setText("");
			program_editor.scroll().setMaximumSize(new Dimension(261, 261));
			program_editor.scroll().setMinimumSize(new Dimension(261, 261));
			
			// Tool bar 
			initButton(jNewButton, "new.png");
			jNewButton.addActionListener(new ProgrammingPanel_jNewButton_actionAdapter(this));
			initButton(jOpenButton, "open.png");
			jOpenButton.addActionListener(new ProgrammingPanel_jOpenButton_actionAdapter(this));
			initButton(jSaveButton, "save.png");
			jSaveButton.addActionListener(new ProgrammingPanel_jSaveButton_actionAdapter(this));
			initButton(jCompileButton, "compile.png");
			jCompileButton.addActionListener(new ProgrammingPanel_jCompileButton_actionAdapter(this));
			initButton(jRemnantButton, "remnant.png");
			jRemnantButton.addActionListener(new ProgrammingPanel_jRemnantButton_actionAdapter(this));
			initButton(jPrimitiveButton, "tools.png");
			jPrimitiveButton.addActionListener(new ProgrammingPanel_jPrimitiveButton_actionAdapter(this));
			initButton(jMachineButton, "machine.png");
			jMachineButton.addActionListener(new ProgrammingPanel_jMachineButton_actionAdapter(this));
			initButton(jStyleButton, "style.png");
			jStyleButton.addActionListener(new ProgrammingPanel_jStyleButton_actionAdapter(this));
			initButton(jLangButton, "language.png");
			jLangButton.addActionListener(new ProgrammingPanel_jLangButton_actionAdapter(this));
			jToolBar.add(jNewButton);
			jToolBar.add(jOpenButton);
			jToolBar.add(jSaveButton);
			jToolBar.add(jCompileButton);
			jToolBar.add(jRemnantButton);
			jToolBar.add(jPrimitiveButton);
			jToolBar.add(jMachineButton);
			jToolBar.add(jStyleButton);
			jToolBar.add(jLangButton);

			// Command area
			jCommandBar.setLayout(commandLayout);
			jCommandBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			
			command_editor = new Editor("command");
			
			JTextComponent jCommand = command_editor.editArea();
			jCommand.setToolTipText("");
			jCommand.setText("");
			
			initButton(jCommandButton, "quilt-run.png");
			jCommandButton.addActionListener(new ProgrammingPanel_jCommandButton_actionAdapter(this));
			jCommandBar.add(jCommandLabel, java.awt.BorderLayout.WEST);
			jCommandBar.add(command_editor.scroll(), java.awt.BorderLayout.CENTER);
			jCommandBar.add(jCommandButton, java.awt.BorderLayout.EAST);

			//Log area
			log = new Console(logPanel);
			logCommandArea.setLayout(logCommandLayout);	
			logCommandArea.add(logPanel, java.awt.BorderLayout.CENTER);
			logCommandArea.add(jCommandBar, java.awt.BorderLayout.NORTH);
			logCommandArea.setMinimumSize(new Dimension(width/5, height/5));
			logCommandArea.setPreferredSize(new Dimension(width/5, height/5));

			// Window area
			windowPanel.setLayout(windowPaneLayout);
			windowPanel.add(program_editor.scroll(), java.awt.BorderLayout.CENTER);
			windowPanel.add(jToolBar, java.awt.BorderLayout.NORTH);
			windowPanel.add(logCommandArea, java.awt.BorderLayout.SOUTH);
			
			//Create a split pane with the two scroll panes in it.
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			                           windowPanel, (JPanel)render);
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(width*2/5);

			log.out("Desarrollado por/Developed by:\nProfesor/Professor Jonatan Gomez, PhD\njgomezpe[at]unal.edu.co\nhttps://disi.unal.edu.co/~jgomezpe\nUniversidad Nacional de Colombia");

			//Provide minimum sizes for the two components in the split pane
			Dimension minimumSize = new Dimension(width/5, height/5);
			windowPanel.setMinimumSize(minimumSize);
			((JPanel)render).setMinimumSize(minimumSize);		
			this.add(splitPane, java.awt.BorderLayout.CENTER);
			((ProgrammingFrame)parent).setIconImage( image("quilt.png"));
			this.setAPI(api);
			
			this.setLanguage();
			this.app = new Application("funpl", program_editor, command_editor, log, render, this.api);
		}catch(Exception e){ e.printStackTrace();  }
	}
	
	protected Image image(String name) {
	    try{ return resource.image(images+name); }catch(Exception e) { e.printStackTrace();}
	    return null;
	}
	
	protected void initButton( JButton button, String resource ){
		Image img = image(resource);
		button.setIcon(new ImageIcon(img.getScaledInstance((button==jCommandButton)?60:30, 30, Image.SCALE_SMOOTH)));
	}
	
	protected String readFile(String file ){
		try { return resource.txt(file); }catch (Exception e){ log.error(i18n(e.getMessage())); }
		return null;
	}
	
	protected String tmpName;
	protected String chooseFile(String TYPE, String path){
		FileFilter filter = new FileFilter( i18n(GUIFunConstants.FILE)+" (*"+TYPE+")" );
		filter.add(TYPE.substring(1));
		JFileChooser file = new JFileChooser("./");
		file.setFileFilter(filter);
		if( file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ){
			tmpName= file.getSelectedFile().getName();
			String thePath = file.getSelectedFile().getPath();
			int loc = thePath.lastIndexOf("/./");
			if( loc>=0 ) thePath = thePath.substring(loc+3);
			return thePath;
		}	
		return null;
	}
	   
	public void jStyleButton_actionPerformed(ActionEvent actionEvent) {
	    JFontChooser chooser = new JFontChooser();
	    if(chooser.showDialog(this)==JFontChooser.OK_OPTION ) {
		Font font = chooser.getSelectedFont();
		command_editor.editArea().setFont(font);
		program_editor.editArea().setFont(font);
		((JPanel)render).setFont(font);
	    }
 	}

	public void jMachineButton_actionPerformed(ActionEvent actionEvent) {
		String file = chooseFile( api.cfg(), fileDirMachine );
		if( file!=null ){
		    this.setAPI(file);
		}
	}

	public void jNewButton_actionPerformed(ActionEvent actionEvent) {
		fileName = i18n(GUIFunConstants.NONAME);
		JTextComponent jProgram = program_editor.editArea();
		JTextComponent jCommand = command_editor.editArea();
		if( (jProgram.getText().length()>0 || jCommand.getText().length()>0) && JOptionPane.showConfirmDialog(this, i18n(GUIFunConstants.CLEAN)) == JOptionPane.YES_OPTION ){
			jProgram.setText("");
			jCommand.setText("");
		}
		title_component.setTitle(title + " [" + fileName + "]");
	}
	
	public void jOpenButton_actionPerformed(ActionEvent actionEvent) {
		String file = chooseFile( api.type(), fileDir );
		if( file != null ){
			fileDir = file;
			fileName = tmpName;
			program_editor.editArea().setText(readFile(fileDir));
			title_component.setTitle(title + " [" + fileName + "]");
		}
	}

	public void jSaveButton_actionPerformed(ActionEvent actionEvent) {
		if( fileName.equals(i18n(GUIFunConstants.NONAME)) ){
			String QMP = api.type();
			FileFilter filter = new FileFilter( i18n(GUIFunConstants.FILE)+" (*"+QMP+")" );
			filter.add(QMP.substring(1));
			JFileChooser file = new JFileChooser( fileDir );
			file.setFileFilter(filter);
			if( file.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ){
				try{
					String fileExt = QMP;  
					fileDir = file.getSelectedFile().getAbsolutePath();
					fileName = file.getSelectedFile().getName();
					int pos = fileName.lastIndexOf(fileExt);
					if( pos == -1 || pos != fileName.length()-fileExt.length() ){
						fileDir += fileExt;
						fileName += fileExt;
					}
					FileWriter writer = new FileWriter(fileDir);
					writer.write(program_editor.editArea().getText());
					writer.close();
					title_component.setTitle(title + " [" + fileName + "]");
				}catch( Exception e ){ i18n(e.getMessage()); }
			}
		}else{
			try{
				FileWriter writer = new FileWriter(fileDir);
				writer.write(program_editor.editArea().getText());
				writer.close();
			}catch( Exception e ){ i18n(e.getMessage()); }
		}	
	}
	
	public void jPrimitiveButton_actionPerformed(ActionEvent actionEvent){ app.console().out(api.opers_explain('\n')); }

	public void jRemnantButton_actionPerformed(ActionEvent actionEvent) { app.console().out(api.values()); }
	
	public void jCompileButton_actionPerformed(ActionEvent actionEvent){ app.compile(); }
	
	public void jCommandButton_actionPerformed(ActionEvent actionEvent){ app.execute();  }
	
	public void jLangButton_actionPerformed(ActionEvent actionEvent){ 
		String file = chooseFile( GUIFunConstants.FML, i18n );
		if( file != null ){
			try {
			    I18N.clear();
			    I18N.set(resource.txt(file));
			    setLanguage();
			} catch (IOException e) {
			    log.error(e.getMessage());
			}
		}	    
	}
}


class ProgrammingPanel_jCompileButton_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_jCompileButton_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.jCompileButton_actionPerformed(actionEvent); }
}

class ProgrammingPanel_jRemnantButton_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	ProgrammingPanel_jRemnantButton_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.jRemnantButton_actionPerformed(actionEvent); }
}

class ProgrammingPanel_jPrimitiveButton_actionAdapter implements ActionListener{
	private ProgrammingPanel adaptee;
	ProgrammingPanel_jPrimitiveButton_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.jPrimitiveButton_actionPerformed(actionEvent); }
}

class ProgrammingPanel_jCommandButton_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	ProgrammingPanel_jCommandButton_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.jCommandButton_actionPerformed(actionEvent); }
}


class ProgrammingPanel_jSaveButton_actionAdapter implements ActionListener{
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_jSaveButton_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.jSaveButton_actionPerformed(actionEvent); }
}

class ProgrammingPanel_jNewButton_actionAdapter implements ActionListener{
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_jNewButton_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.jNewButton_actionPerformed(actionEvent); }	
}

class ProgrammingPanel_jOpenButton_actionAdapter implements ActionListener{
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_jOpenButton_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.jOpenButton_actionPerformed(actionEvent); }	
}

class ProgrammingPanel_jMachineButton_actionAdapter implements ActionListener{
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_jMachineButton_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.jMachineButton_actionPerformed(actionEvent); }	
}

class ProgrammingPanel_jStyleButton_actionAdapter implements ActionListener{
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_jStyleButton_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.jStyleButton_actionPerformed(actionEvent); }	
}

class ProgrammingPanel_jLangButton_actionAdapter implements ActionListener{
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_jLangButton_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.jLangButton_actionPerformed(actionEvent); }	
}