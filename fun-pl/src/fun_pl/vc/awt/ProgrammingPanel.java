package fun_pl.vc.awt;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

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

import fun_pl.FunLanguage;
import fun_pl.util.FunConstants;
import fun_pl.vc.FunController;
import fun_pl.vc.FunEditorController;
import fun_pl.vc.FunToolbarController;
import fun_pl.vc.FunVCModel;
import fun_pl.vc.GUIFunConstants;
import unalcol.gui.editor.AWTEditor;
import unalcol.gui.editor.rsyntax.RSyntaxEditor;
import unalcol.gui.editor.simple.SimpleAWTEditor;
import unalcol.gui.editor.simple.SyntaxEditPanel;
import unalcol.gui.editor.simple.SyntaxStyle;
import unalcol.gui.io.FileFilter;
import unalcol.gui.log.AWTLog;
import unalcol.gui.log.LogPanel;
import unalcol.gui.render.Render;
import unalcol.util.FileResource;
import unalcol.i18n.I18N;
import unalcol.io.Tokenizer;
import unalcol.collection.keymap.HashMap;
import unalcol.collection.KeyMap;
import unalcol.vc.FrontEnd;

public class ProgrammingPanel  extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3339559167222155206L;

	protected static final String resources="resources/";
	protected static final String images="imgs/";
	protected static final String i18n="I18N/";
	
	String title;
	String fileName = null;
	String fileDir = ".";
	String fileDirStyle = ".";
	String fileDirMachine = ".";
	TitleComponent title_component;
	
	JSplitPane splitPane;
	protected Render drawPanel; 
	
	// Window area
	BorderLayout windowLayout = new BorderLayout();
	JPanel windowPanel = new JPanel();
	BorderLayout windowPaneLayout = new BorderLayout();
	
	// The program area
	AWTEditor program_editor;
	
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
	
	// The log area
	LogPanel logPanel = new LogPanel();
	AWTLog log;   
	
	// The command area
	JPanel jCommandBar = new JPanel();
	BorderLayout commandLayout = new BorderLayout();
	JLabel jCommandLabel = new JLabel();
	AWTEditor command_editor;
	JButton jCommandButton = new JButton();

	//Log command area
	BorderLayout logCommandLayout = new BorderLayout();
	JPanel logCommandArea = new JPanel();
	
	protected KeyMap<Integer, ?> tokens;
	protected FrontEnd frontend;
	
	public LogPanel getLogPanel(){ return logPanel; }

	public ProgrammingPanel(FrontEnd frontend, TitleComponent parent, Render drawPanel){ this(frontend,parent,drawPanel,null); }
	
	public ProgrammingPanel(FrontEnd frontend, TitleComponent parent, Render drawPanel, String styles){
		this.frontend = frontend;
		this.drawPanel = drawPanel;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
		this.setSize(new Dimension(width*4/5, height*4/5));
		build(parent, width, height, styles);
	}
	
	public static String i18n(String code){ return unalcol.i18n.I18N.get(code); }
	
	// FunPL Tokenizers
	public static KeyMap<Integer, String> awtEditorTokens(){
		HashMap<Integer, String> tokens = new HashMap<Integer,String>();
	    String[] tokens_style = { "undef", "regular", "comment", "symbol", "stitch", "regular", "reserved", "remnant" };
	    tokens.set(Integer.MIN_VALUE,tokens_style[0]);
	    tokens.set(FunConstants.VALUE,tokens_style[1]);
	    tokens.set(FunConstants.COMMENT,tokens_style[2]);
	    tokens.set(FunConstants.ASSIGN,tokens_style[3]);
	    tokens.set(FunConstants.OPEN,tokens_style[3]);
	    tokens.set(FunConstants.CLOSE,tokens_style[3]);
	    tokens.set(FunConstants.COMMA,tokens_style[3]);
	    String code = I18N.get(FunConstants.code);
	    for( int i=0; i<code.length(); i++ ) tokens.set(FunConstants.START_LINK_SYMBOLS+i,tokens_style[4]);
	    tokens.set(FunConstants.VARIABLE,tokens_style[5]);
	    tokens.set(FunConstants.PRIM_COMMAND,tokens_style[6]);
	    tokens.set(FunConstants.PRIM_VALUE,tokens_style[7]);
		return tokens;
	}
	
	public static KeyMap<Integer, Integer> rSyntaxEditorTokens(){
		KeyMap<Integer, Integer> converter = new HashMap<Integer, Integer>();
	    converter.set(Integer.MIN_VALUE,Token.ERROR_IDENTIFIER);
	    converter.set(FunConstants.VALUE,Token.IDENTIFIER);
	    converter.set(FunConstants.COMMENT,Token.COMMENT_EOL);
	    converter.set(FunConstants.ASSIGN,Token.OPERATOR);
	    converter.set(FunConstants.OPEN,Token.SEPARATOR);
	    converter.set(FunConstants.CLOSE,Token.SEPARATOR);
	    converter.set(FunConstants.COMMA,Token.SEPARATOR);
	    String code = I18N.get(FunConstants.code);
	    for( int i=0; i<code.length(); i++ ) converter.set(FunConstants.START_LINK_SYMBOLS+i,Token.OPERATOR);
	    converter.set(FunConstants.VARIABLE, Token.VARIABLE);
		converter.set(FunConstants.PRIM_COMMAND, Token.FUNCTION);
		converter.set(FunConstants.PRIM_VALUE, Token.LITERAL_NUMBER_DECIMAL_INT);
		return converter;
	}
	
	
	public void build( TitleComponent parent, int width, int height, String styles ){
		this.title_component = parent;
		try{
			if(styles!=null) tokens = awtEditorTokens();
			else tokens = rSyntaxEditorTokens();
			
			title =i18n(GUIFunConstants.TITLE);
			fileName = i18n(GUIFunConstants.NONAME);
			parent.setTitle(title + " [" + fileName + "]");
			this.setSize(new Dimension(width*4/5, height*4/5));
			this.setLayout(windowLayout);

			// Program area
			if( styles!=null ) program_editor = new SimpleAWTEditor(FunVCModel.PROGRAM);
			else program_editor = new RSyntaxEditor(FunVCModel.PROGRAM,1);
			//program_editor.setStyle(SyntaxStyle.get(styles));
			JTextComponent jProgram = program_editor.editArea();
			jProgram.setToolTipText("");
			jProgram.setText("");
			program_editor.scroll().setMaximumSize(new Dimension(261, 261));
			program_editor.scroll().setMinimumSize(new Dimension(261, 261));
			
			// Tool bar 
			initButton(jNewButton, "new.png", i18n(GUIFunConstants.NEW));
			jNewButton.addActionListener(new ProgrammingPanel_jNewButton_actionAdapter(this));
			initButton(jOpenButton, "open.png", i18n(GUIFunConstants.OPEN));
			jOpenButton.addActionListener(new ProgrammingPanel_jOpenButton_actionAdapter(this));
			initButton(jSaveButton, "save.png", i18n(GUIFunConstants.SAVE));
			jSaveButton.addActionListener(new ProgrammingPanel_jSaveButton_actionAdapter(this));
			initButton(jCompileButton, "compile.png", i18n(GUIFunConstants.COMPILE));
			jCompileButton.addActionListener(new ProgrammingPanel_jCompileButton_actionAdapter(this));
			initButton(jRemnantButton, "remnant.png", i18n(GUIFunConstants.VALUE));
			jRemnantButton.addActionListener(new ProgrammingPanel_jRemnantButton_actionAdapter(this));
			initButton(jPrimitiveButton, "tools.png", i18n(GUIFunConstants.PRIMITIVE));
			jPrimitiveButton.addActionListener(new ProgrammingPanel_jPrimitiveButton_actionAdapter(this));
			initButton(jMachineButton, "machine.png", i18n(GUIFunConstants.MACHINE));
			jMachineButton.addActionListener(new ProgrammingPanel_jMachineButton_actionAdapter(this));
			initButton(jStyleButton, "style.png", i18n(GUIFunConstants.STYLE));
			jStyleButton.addActionListener(new ProgrammingPanel_jStyleButton_actionAdapter(this));
			jToolBar.add(jNewButton);
			jToolBar.add(jOpenButton);
			jToolBar.add(jSaveButton);
			jToolBar.add(jCompileButton);
			jToolBar.add(jRemnantButton);
			jToolBar.add(jPrimitiveButton);
			jToolBar.add(jMachineButton);
			jToolBar.add(jStyleButton);

			// Command area
			jCommandBar.setLayout(commandLayout);
			jCommandBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			jCommandLabel.setText(i18n(GUIFunConstants.COMMAND));
			
			if( styles!=null ) command_editor = new SimpleAWTEditor(FunVCModel.COMMAND);
			else command_editor = new RSyntaxEditor(FunVCModel.COMMAND,0);
			if( styles!=null ) ((SimpleAWTEditor)command_editor).setStyle(SyntaxStyle.get(styles));
			
			JTextComponent jCommand = command_editor.editArea();
			jCommand.setToolTipText("");
			jCommand.setText("");
			
			initButton(jCommandButton, "quilt-run.png", i18n(GUIFunConstants.EXECUTE));
			jCommandButton.addActionListener(new ProgrammingPanel_jCommandButton_actionAdapter(this));
			jCommandBar.add(jCommandLabel, java.awt.BorderLayout.WEST);
			jCommandBar.add(command_editor.scroll(), java.awt.BorderLayout.CENTER);
			jCommandBar.add(jCommandButton, java.awt.BorderLayout.EAST);

			//Log area
			log = new AWTLog(FunVCModel.LOG, logPanel);
			logCommandArea.setLayout(logCommandLayout);	
			logCommandArea.add(logPanel, java.awt.BorderLayout.CENTER);
			logCommandArea.add(jCommandBar, java.awt.BorderLayout.NORTH);
			logCommandArea.setMinimumSize(new Dimension(width/5, height/5));
			logCommandArea.setPreferredSize(new Dimension(width/5, height/5));
			logPanel.setLanguage(i18n(GUIFunConstants.OUT), i18n(GUIFunConstants.ERROR));
			log.out(i18n(GUIFunConstants.AUTHOR));

			// Window area
			windowPanel.setLayout(windowPaneLayout);
			windowPanel.add(program_editor.scroll(), java.awt.BorderLayout.CENTER);
			windowPanel.add(jToolBar, java.awt.BorderLayout.NORTH);
			windowPanel.add(logCommandArea, java.awt.BorderLayout.SOUTH);
			
			//Create a split pane with the two scroll panes in it.
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			                           windowPanel, (JPanel)drawPanel);
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(width*2/5);

			//Provide minimum sizes for the two components in the split pane
			Dimension minimumSize = new Dimension(width/5, height/5);
			windowPanel.setMinimumSize(minimumSize);
			((JPanel)drawPanel).setMinimumSize(minimumSize);		
			this.add(splitPane, java.awt.BorderLayout.CENTER);
			((ProgrammingFrame)parent).setIconImage( FileResource.image("quilt.png"));
			
		}catch(Exception e){ e.printStackTrace();  }
	}
	
	protected void initButton( JButton button, String resource, String message ){
		button.setToolTipText(message);
		Image img = FileResource.image(resource);
		button.setIcon(new ImageIcon(img.getScaledInstance((button==jCommandButton)?60:30, 30, Image.SCALE_SMOOTH)));
		button.setToolTipText(message);
	}
	
	protected String readFile(String file, boolean addNL ){
		try {
			BufferedReader reader = new BufferedReader( new FileReader(file) );
			StringBuffer sb = new StringBuffer();
			String s = reader.readLine();
			while( s != null ){
				sb.append(s);
				s = reader.readLine();
				if( addNL ) sb.append('\n');
			}
			reader.close();
			return sb.toString();
		}catch (Exception e){ log.error(e.getMessage()); }
		return null;
	}
	
	protected String tmpName;
	protected String chooseFile(String TYPE, String path){
		FileFilter filter = new FileFilter( i18n(GUIFunConstants.FILE)+" (*"+TYPE+")" );
		filter.add(TYPE.substring(1));
		JFileChooser file = new JFileChooser( fileDirStyle );
		file.setFileFilter(filter);
		if( file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ){
			tmpName= file.getSelectedFile().getName();
			return file.getSelectedFile().getAbsolutePath();
		}	
		return null;
	}
	   
	public void jStyleButton_actionPerformed(ActionEvent actionEvent) {
		if( command_editor instanceof SimpleAWTEditor){
			String file = chooseFile( GUIFunConstants.FMS, fileDirStyle );
			if( file != null ){
				fileDirStyle = file;
				String styles = readFile(fileDirStyle,false);
				if( styles != null ){
					((SyntaxEditPanel)command_editor.editArea()).setStyle(SyntaxStyle.get(styles));
					((SyntaxEditPanel)program_editor.editArea()).setStyle(SyntaxStyle.get(styles));
				}	
			}
		}else{}
	}

	public void jMachineButton_actionPerformed(ActionEvent actionEvent) {
		String file = chooseFile( GUIFunConstants.FMC, fileDirMachine );
		if( file!=null ){
			fileDirMachine = file;
			String machine_txt = readFile(fileDirMachine, false);
			if(machine_txt!=null) setMachine(machine_txt);
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
		String file = chooseFile( GUIFunConstants.FMP, fileDir );
		if( file != null ){
			fileDir = file;
			fileName = tmpName;
			program_editor.editArea().setText(readFile(fileDir,true));
			title_component.setTitle(title + " [" + fileName + "]");
		}
	}

	public void jSaveButton_actionPerformed(ActionEvent actionEvent) {
		if( fileName.equals(i18n(GUIFunConstants.NONAME)) ){
			String QMP = GUIFunConstants.FMP;
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
				}catch( Exception e ){ e.printStackTrace(); }
			}
		}else{
			try{
				FileWriter writer = new FileWriter(fileDir);
				writer.write(program_editor.editArea().getText());
				writer.close();
			}catch( Exception e ){ e.printStackTrace(); }
		}	
	}
	
	protected FunToolbarController toolbar=null;
	
	protected FunToolbarController toolbar(){
		if( toolbar==null )	toolbar = (FunToolbarController)frontend.backend().component(FunVCModel.TOOLBAR);
		return toolbar;
	}

	protected FunEditorController editor(String id){ return (FunEditorController)frontend.backend().component(id); }

	public void jPrimitiveButton_actionPerformed(ActionEvent actionEvent){ toolbar().primitives(); }

	public void jRemnantButton_actionPerformed(ActionEvent actionEvent) { toolbar().values(); }
	
	public void jCompileButton_actionPerformed(ActionEvent actionEvent){ editor(FunVCModel.PROGRAM).text(program_editor.editArea().getText()); }
	
	public void jCommandButton_actionPerformed(ActionEvent actionEvent){  editor(FunVCModel.COMMAND).text(command_editor.editArea().getText());  }
	
	public void setMachine(){
		Tokenizer tokenizer = FunLanguage.tokenizer(FunController.machine());
		program_editor.setTokenizer(tokenizer, tokens);
		command_editor.setTokenizer(tokenizer, tokens);
	}
	
	public void setMachine( String machine_txt ){
		toolbar().machine(machine_txt);
		setMachine();
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