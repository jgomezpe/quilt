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
import javax.swing.JFrame;
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
import nsgl.json.JSON;
import nsgl.language.lexeme.Space;
import nsgl.language.lexeme.Symbol;
import nsgl.stream.Resource;
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
		
	// Window area
	BorderLayout windowLayout = new BorderLayout();
	JPanel windowPanel = new JPanel();
	BorderLayout windowPaneLayout = new BorderLayout();
	
	// The program area
	Editor programEditor;
	
	// The tool bar
	JToolBar toolBar = new JToolBar();
	JButton newBtn = new JButton();
	JButton openBtn = new JButton();
	JButton saveBtn = new JButton();
	JButton compileBtn = new JButton();
	JButton remnantBtn = new JButton();
	JButton operBtn = new JButton();
	JButton machineBtn = new JButton();
	JButton styleBtn = new JButton();
	JButton langBtn = new JButton();
	
	// The log area
	LogPanel logPanel = new LogPanel();
	Console log;   


	//Log command area
	BorderLayout logCommandLayout = new BorderLayout();
	JPanel logCommandArea = new JPanel();
	
	// Output area
	JPanel outPanel = new JPanel();
	BorderLayout outLayout = new BorderLayout();
	protected Render render; 
	
	// The command area
	JPanel commandBar = new JPanel();
	BorderLayout commandLayout = new BorderLayout();
	JLabel commandLabel = new JLabel();
	Editor commandEditor;
	JButton commandBtn = new JButton();
	JButton applyBtn = new JButton();
	JPanel commandBtnsPanel = new JPanel();

	// Resources
	Resource resource;
	
	//
	FunAPI api;
	Application app;
	protected KeyMap<Integer, ?> tokens;
	
	public LogPanel getLogPanel(){ return logPanel; }

	public ProgrammingPanel(TitleComponent parent, FunAPI api, String api_code, Render render, Resource resource ) {
		this.render = render;
		this.resource = resource;
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
		    	JSON json = new JSON(api_code);
			api.config(json.object(GUIFunConstants.FUN));
			render.config(json.object(Render.TAG));
			KeyMap<String, Integer> map = rSyntaxEditorTokens();
			FunLexer lexer = api.lexer();
			programEditor.setLexer(lexer, map);
			commandEditor.setLexer(lexer, map);	
		}
	    }catch(Exception e) { log.error(i18n(e.getMessage())); }
	}
	
	public void setLanguage() {
		title =i18n(GUIFunConstants.TITLE);
		if( fileName == null ) fileName = i18n(GUIFunConstants.NONAME);
		this.title_component.setTitle(title + " [" + fileName + "]");
		newBtn.setToolTipText(i18n(GUIFunConstants.NEW));
		openBtn.setToolTipText(i18n(GUIFunConstants.OPEN));
		saveBtn.setToolTipText(i18n(GUIFunConstants.SAVE));
		compileBtn.setToolTipText(i18n(GUIFunConstants.COMPILE));
		remnantBtn.setToolTipText(i18n(GUIFunConstants.VALUE));
		operBtn.setToolTipText(i18n(GUIFunConstants.PRIMITIVE));
		machineBtn.setToolTipText(i18n(GUIFunConstants.MACHINE));
		styleBtn.setToolTipText(i18n(GUIFunConstants.STYLE));
		commandBtn.setToolTipText(i18n(GUIFunConstants.EXECUTE));
		langBtn.setToolTipText(i18n(GUIFunConstants.LANGUAGE));
		logPanel.setLanguage(i18n(GUIFunConstants.OUT), i18n(GUIFunConstants.ERROR));
		commandLabel.setText(i18n(GUIFunConstants.COMMAND));
		applyBtn.setToolTipText(i18n(GUIFunConstants.APPLY));
	}
	
	public void build( TitleComponent parent, String api, int width, int height ){
		this.title_component = parent;
		try{		
			this.setSize(new Dimension(width*4/5, height*4/5));
			this.setLayout(windowLayout);

			((JFrame)parent).setIconImage(image("remnant.png"));
			
			// Program area
			programEditor = new Editor("program");
			
			//program_editor.setStyle(SyntaxStyle.get(styles));
			JTextComponent jProgram = programEditor.editArea();
			jProgram.setToolTipText("");
			jProgram.setText("");
			programEditor.scroll().setMaximumSize(new Dimension(261, 261));
			programEditor.scroll().setMinimumSize(new Dimension(261, 261));
			
			// Tool bar 
			initButton(newBtn, "new.png");
			newBtn.addActionListener(new ProgrammingPanel_newBtn_actionAdapter(this));
			initButton(openBtn, "open.png");
			openBtn.addActionListener(new ProgrammingPanel_openBtn_actionAdapter(this));
			initButton(saveBtn, "save.png");
			saveBtn.addActionListener(new ProgrammingPanel_saveBtn_actionAdapter(this));
			initButton(compileBtn, "compile.png");
			compileBtn.addActionListener(new ProgrammingPanel_compileBtn_actionAdapter(this));
			initButton(remnantBtn, "remnant.png");
			remnantBtn.addActionListener(new ProgrammingPanel_remnantBtn_actionAdapter(this));
			initButton(operBtn, "tools.png");
			operBtn.addActionListener(new ProgrammingPanel_opersBtn_actionAdapter(this));
			initButton(machineBtn, "machine.png");
			machineBtn.addActionListener(new ProgrammingPanel_machineBtn_actionAdapter(this));
			initButton(styleBtn, "style.png");
			styleBtn.addActionListener(new ProgrammingPanel_styleBtn_actionAdapter(this));
			initButton(langBtn, "language.png");
			langBtn.addActionListener(new ProgrammingPanel_langBtn_actionAdapter(this));
			toolBar.add(newBtn);
			toolBar.add(openBtn);
			toolBar.add(saveBtn);
			toolBar.add(compileBtn);
			toolBar.add(remnantBtn);
			toolBar.add(operBtn);
			toolBar.add(machineBtn);
			toolBar.add(styleBtn);
			toolBar.add(langBtn);


			//Log area
			log = new Console(logPanel);
			logCommandArea.setLayout(logCommandLayout);	
			logCommandArea.add(logPanel, java.awt.BorderLayout.CENTER);
			// logCommandArea.add(jCommandBar, java.awt.BorderLayout.NORTH);
			logCommandArea.setMinimumSize(new Dimension(width/5, height/5));
			logCommandArea.setPreferredSize(new Dimension(width/5, height/5));

			// Window area
			windowPanel.setLayout(windowPaneLayout);
			windowPanel.add(programEditor.scroll(), java.awt.BorderLayout.CENTER);
			windowPanel.add(toolBar, java.awt.BorderLayout.NORTH);
			windowPanel.add(logCommandArea, java.awt.BorderLayout.SOUTH);

			// Command area
			commandBar.setLayout(commandLayout);
			commandBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			
			commandEditor = new Editor("command");
			
			JTextComponent jCommand = commandEditor.editArea();
			jCommand.setToolTipText("");
			jCommand.setText("");
			
			initButton(commandBtn, "quilt-run.png");
			commandBtn.addActionListener(new ProgrammingPanel_commandBtn_actionAdapter(this));
			commandBar.add(commandLabel, java.awt.BorderLayout.WEST);
			commandBar.add(commandEditor.scroll(), java.awt.BorderLayout.CENTER);			// jCommandBar.add(jCommandButton, java.awt.BorderLayout.EAST);
			initButton(applyBtn, "redo.png");
			applyBtn.addActionListener(new ProgrammingPanel_applyBtn_actionAdapter(this));
			commandBtnsPanel.add(commandBtn);
			commandBtnsPanel.add(applyBtn);
			commandBar.add(commandBtnsPanel,java.awt.BorderLayout.EAST);
			
			// Out area
			outPanel.setLayout(outLayout);
			outPanel.add((JPanel)render, java.awt.BorderLayout.CENTER);
			outPanel.add(commandBar, java.awt.BorderLayout.SOUTH);
			
			//Create a split pane with the two scroll panes in it.
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			                           windowPanel, outPanel);
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(width*2/5);

			log.out("Desarrollado por/Developed by:\nProfesor/Professor Jonatan Gomez, PhD\njgomezpe[at]unal.edu.co\nhttps://disi.unal.edu.co/~jgomezpe\nUniversidad Nacional de Colombia");

			//Provide minimum sizes for the two components in the split pane
			Dimension minimumSize = new Dimension(width/5, height/5);
			windowPanel.setMinimumSize(minimumSize);
			outPanel.setMinimumSize(minimumSize);		
			this.add(splitPane, java.awt.BorderLayout.CENTER);
			((ProgrammingFrame)parent).setIconImage( image("quilt.png"));
			this.setAPI(api);
			
			this.setLanguage();
			this.app = new Application("funpl", programEditor, commandEditor, log, render, this.api);
		}catch(Exception e){ e.printStackTrace();  }
	}
	
	protected Image image(String name) {
	    try{ return resource.image(images+name); }catch(Exception e) { e.printStackTrace();}
	    return null;
	}
	
	protected void initButton( JButton button, String resource ){
		Image img = image(resource);
		button.setIcon(new ImageIcon(img.getScaledInstance((button==commandBtn)?60:30, 30, Image.SCALE_SMOOTH)));
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
//			int loc = thePath.lastIndexOf("/./");
//			if( loc>=0 ) thePath = thePath.substring(loc+3);
			return thePath;
		}	
		return null;
	}
	   
	public void styleBtn_actionPerformed(ActionEvent actionEvent) {
	    JFontChooser chooser = new JFontChooser();
	    if(chooser.showDialog(this)==JFontChooser.OK_OPTION ) {
		Font font = chooser.getSelectedFont();
		commandEditor.editArea().setFont(font);
		programEditor.editArea().setFont(font);
		((JPanel)render).setFont(font);
	    }
 	}

	public void machineBtn_actionPerformed(ActionEvent actionEvent) {
		String file = chooseFile( api.cfg(), fileDirMachine );
		if( file!=null ){
		    this.setAPI(file);
		}
	}

	public void newBtn_actionPerformed(ActionEvent actionEvent) {
		fileName = i18n(GUIFunConstants.NONAME);
		JTextComponent jProgram = programEditor.editArea();
		JTextComponent jCommand = commandEditor.editArea();
		if( (jProgram.getText().length()>0 || jCommand.getText().length()>0) && JOptionPane.showConfirmDialog(this, i18n(GUIFunConstants.CLEAN)) == JOptionPane.YES_OPTION ){
			jProgram.setText("");
			jCommand.setText("");
		}
		title_component.setTitle(title + " [" + fileName + "]");
	}
	
	public void openBtn_actionPerformed(ActionEvent actionEvent) {
		String file = chooseFile( api.type(), fileDir );
		if( file != null ){
			fileDir = file;
			fileName = tmpName;
			programEditor.editArea().setText(readFile(fileDir));
			title_component.setTitle(title + " [" + fileName + "]");
		}
	}

	public void saveBtn_actionPerformed(ActionEvent actionEvent) {
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
					writer.write(programEditor.editArea().getText());
					writer.close();
					title_component.setTitle(title + " [" + fileName + "]");
				}catch( Exception e ){ i18n(e.getMessage()); }
			}
		}else{
			try{
				FileWriter writer = new FileWriter(fileDir);
				writer.write(programEditor.editArea().getText());
				writer.close();
			}catch( Exception e ){ i18n(e.getMessage()); }
		}	
	}
	
	public void opersBtn_actionPerformed(ActionEvent actionEvent){ app.console().out(api.opers_explain('\n')); }

	public void remnantBtn_actionPerformed(ActionEvent actionEvent) { app.console().out(api.values()); }
	
	public void compileBtn_actionPerformed(ActionEvent actionEvent){ app.compile(); }
	
	public void commandBtn_actionPerformed(ActionEvent actionEvent){ app.execute();  }

	public void applyBtn_actionPerformed(ActionEvent actionEvent){ app.apply(); }
	
	public void langBtn_actionPerformed(ActionEvent actionEvent){ 
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


class ProgrammingPanel_compileBtn_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_compileBtn_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.compileBtn_actionPerformed(actionEvent); }
}

class ProgrammingPanel_remnantBtn_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	ProgrammingPanel_remnantBtn_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.remnantBtn_actionPerformed(actionEvent); }
}

class ProgrammingPanel_opersBtn_actionAdapter implements ActionListener{
	private ProgrammingPanel adaptee;
	ProgrammingPanel_opersBtn_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.opersBtn_actionPerformed(actionEvent); }
}

class ProgrammingPanel_commandBtn_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	ProgrammingPanel_commandBtn_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.commandBtn_actionPerformed(actionEvent); }
}

class ProgrammingPanel_applyBtn_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	ProgrammingPanel_applyBtn_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.applyBtn_actionPerformed(actionEvent); }
}


class ProgrammingPanel_saveBtn_actionAdapter implements ActionListener{
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_saveBtn_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.saveBtn_actionPerformed(actionEvent); }
}

class ProgrammingPanel_newBtn_actionAdapter implements ActionListener{
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_newBtn_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.newBtn_actionPerformed(actionEvent); }	
}

class ProgrammingPanel_openBtn_actionAdapter implements ActionListener{
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_openBtn_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.openBtn_actionPerformed(actionEvent); }	
}

class ProgrammingPanel_machineBtn_actionAdapter implements ActionListener{
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_machineBtn_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.machineBtn_actionPerformed(actionEvent); }	
}

class ProgrammingPanel_styleBtn_actionAdapter implements ActionListener{
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_styleBtn_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.styleBtn_actionPerformed(actionEvent); }	
}

class ProgrammingPanel_langBtn_actionAdapter implements ActionListener{
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_langBtn_actionAdapter(ProgrammingPanel adaptee){ this.adaptee = adaptee; }

	public void actionPerformed(ActionEvent actionEvent){ adaptee.langBtn_actionPerformed(actionEvent); }	
}