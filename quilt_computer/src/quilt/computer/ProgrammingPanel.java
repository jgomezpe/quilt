package quilt.computer;

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
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;
import javax.swing.text.JTextComponent;

import fun_pl.FunLanguage;
import fun_pl.semantic.FunCommandCall;
import fun_pl.semantic.FunProgram;
import fun_pl.util.FunConstants;
import quilt.Quilt;
import quilt.QuiltMachine;
import quilt.factory.QuiltMachineInstance;
import quilt.util.QuiltConstants;
import unalcol.gui.editor.SyntaxEditPanel;
import unalcol.gui.editor.SyntaxStyle;
import unalcol.gui.io.FileFilter;
import unalcol.gui.log.LogPanel;
import unalcol.util.ObjectParser;
import unalcol.io.Position2D;
import unalcol.io.Tokenizer;
import unalcol.language.LanguageException;
import unalcol.types.collection.Collection;
import unalcol.types.collection.keymap.HTKeyMap;
import quilt.util.Util;

/**
*
* ProgrammingPanel
* <P>GUI of the Quilt Programming environment.
*
* <P>
* <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt_computer/src/quilt/computer/ProgrammingPanel.java" target="_blank">
* Source code </A> is available.
*
* <h3>License</h3>
*
* Copyright (c) 2017 by Jonatan Gomez-Perdomo. <br>
* All rights reserved. <br>
*
* <p>Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
* <ul>
* <li> Redistributions of source code must retain the above copyright notice,
* this list of conditions and the following disclaimer.
* <li> Redistributions in binary form must reproduce the above copyright notice,
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
* <li> Neither the name of the copyright owners, their employers, nor the
* names of its contributors may be used to endorse or promote products
* derived from this software without specific prior written permission.
* </ul>
* <p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNERS OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE.
*
*
*
* @author <A HREF="http://disi.unal.edu.co/profesores/jgomezpe"> Jonatan Gomez-Perdomo </A>
* (E-mail: <A HREF="mailto:jgomezpe@unal.edu.co">jgomezpe@unal.edu.co</A> )
* @version 1.0
*/
public class ProgrammingPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3339559167222155206L;

	String title;
	String fileName = null;
	String fileDir = ".";
	String fileDirStyle = ".";
	String fileDirMachine = ".";
	TitleComponent title_component;
	
	JSplitPane splitPane;
	protected DrawPanel drawPanel = new DrawPanel();
	
	// Window area
	BorderLayout windowLayout = new BorderLayout();
	JPanel windowPanel = new JPanel();
	BorderLayout windowPaneLayout = new BorderLayout();
	
	// The program area
	JScrollPane jProgramScrollPane1 = new JScrollPane();
	SyntaxEditPanel jProgram = new SyntaxEditPanel();
	
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
	JPanel jLogPanel = new JPanel();
	LogPanel log = new LogPanel();
	BorderLayout logLayout = new BorderLayout();
	
	// The command area
	JPanel jCommandBar = new JPanel();
	BorderLayout commandLayout = new BorderLayout();
	JLabel jCommandLabel = new JLabel();
	SyntaxEditPanel jCommand = new SyntaxEditPanel();
	JButton jCommandButton = new JButton();

	protected QuiltMachine machine;
	protected FunLanguage quiltLang;
	protected QuiltMachineInstance qm= new QuiltMachineInstance();
	protected HTKeyMap<Integer, String> tokens = new HTKeyMap<Integer,String>();
	
	public LogPanel getLogPanel(){ return log; }

	public ProgrammingPanel(TitleComponent parent, String machine_txt, String styles){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
		this.setSize(new Dimension(width*4/5, height*4/5));

		// @TODO: Load Constants i18n
		qm.register(ImageRemnantInstance.IMAGE, ImageRemnant.class.getName(), new ImageRemnantInstance());
		
		QuiltMachine machine=null;
		try{ machine = qm.load(ObjectParser.parse(machine_txt)); }catch(Exception e){ e.printStackTrace(); }
		build(parent, width, height, styles, machine);
	}
	
	public ProgrammingPanel( TitleComponent parent, int width, int height, String styles, QuiltMachine machine ){
		build( parent, width, height, styles, machine );
	}
	
	public static String i18n(String code){ return unalcol.util.I18N.get(code); }
	
	public void build( TitleComponent parent, int width, int height, String styles, QuiltMachine machine ){
		this.setMachine(machine);
		this.title_component = parent;
		try{
		    String[] tokens_style = { "undef", "regular", "comment", "symbol", "stitch", "regular", "reserved", "remnant" };
		    tokens.set(Integer.MIN_VALUE,tokens_style[0]);
		    tokens.set(FunConstants.VALUE,tokens_style[1]);
		    tokens.set(FunConstants.COMMENT,tokens_style[2]);
		    tokens.set(FunConstants.ASSIGN,tokens_style[3]);
		    tokens.set(FunConstants.OPEN,tokens_style[3]);
		    tokens.set(FunConstants.CLOSE,tokens_style[3]);
		    tokens.set(FunConstants.COMMA,tokens_style[3]);
		    tokens.set(FunConstants.START_LINK_SYMBOLS,tokens_style[4]);
		    tokens.set(FunConstants.VARIABLE,tokens_style[5]);
		    tokens.set(FunConstants.PRIM_COMMAND,tokens_style[6]);
		    tokens.set(FunConstants.PRIM_VALUE,tokens_style[7]);

			title =i18n(QuiltConstants.TITLE);
			fileName = i18n(QuiltConstants.NONAME);
			parent.setTitle(title + " [" + fileName + "]");
			this.setSize(new Dimension(width*4/5, height*4/5));
			this.setLayout(windowLayout);

			// Program area
			jProgram.setStyle(SyntaxStyle.get(styles));
			jProgram.setToolTipText("");
			jProgram.setVerifyInputWhenFocusTarget(true);
			jProgram.setText("");
			jProgramScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jProgramScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jProgramScrollPane1.setAutoscrolls(true);
			jProgramScrollPane1.setMaximumSize(new Dimension(261, 261));
			jProgramScrollPane1.setMinimumSize(new Dimension(261, 261));
			jProgramScrollPane1.getViewport().add(jProgram, null);
			
			
			// Tool bar 
			initButton(jNewButton, "new.png", i18n(QuiltConstants.NEW));
			jNewButton.addActionListener(new ProgrammingPanel_jNewButton_actionAdapter(this));
			initButton(jOpenButton, "open.png", i18n(QuiltConstants.OPEN));
			jOpenButton.addActionListener(new ProgrammingPanel_jOpenButton_actionAdapter(this));
			initButton(jSaveButton, "save.png", i18n(QuiltConstants.SAVE));
			jSaveButton.addActionListener(new ProgrammingPanel_jSaveButton_actionAdapter(this));
			initButton(jCompileButton, "compile.png", i18n(QuiltConstants.COMPILE));
			jCompileButton.addActionListener(new ProgrammingPanel_jCompileButton_actionAdapter(this));
			initButton(jRemnantButton, "remnant.png", i18n(QuiltConstants.REMNANT));
			jRemnantButton.addActionListener(new ProgrammingPanel_jRemnantButton_actionAdapter(this));
			initButton(jPrimitiveButton, "tools.png", i18n(QuiltConstants.PRIMITIVE));
			jPrimitiveButton.addActionListener(new ProgrammingPanel_jPrimitiveButton_actionAdapter(this));
			initButton(jMachineButton, "machine.png", i18n(QuiltConstants.MACHINE));
			jMachineButton.addActionListener(new ProgrammingPanel_jMachineButton_actionAdapter(this));
			initButton(jStyleButton, "style.png", i18n(QuiltConstants.STYLE));
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
			jCommandLabel.setText(i18n(QuiltConstants.COMMAND));
			jCommand.setStyle(SyntaxStyle.get(styles));
			jCommand.setToolTipText("");
			jCommand.setVerifyInputWhenFocusTarget(true);
			jCommand.setText("");
			
			initButton(jCommandButton, "quilt-run.png", i18n(QuiltConstants.EXECUTE));
			jCommandButton.addActionListener(new ProgrammingPanel_jCommandButton_actionAdapter(this));
			jCommandBar.add(jCommandLabel, java.awt.BorderLayout.WEST);
			jCommandBar.add(jCommand, java.awt.BorderLayout.CENTER);
			jCommandBar.add(jCommandButton, java.awt.BorderLayout.EAST);

			//Log area
			jLogPanel.setLayout(logLayout);	
			jLogPanel.add(log, java.awt.BorderLayout.CENTER);
			jLogPanel.add(jCommandBar, java.awt.BorderLayout.NORTH);
			jLogPanel.setMinimumSize(new Dimension(width/5, height/5));
			jLogPanel.setPreferredSize(new Dimension(width/5, height/5));
			log.setLanguage(i18n(QuiltConstants.OUT), i18n(QuiltConstants.ERROR));
			log.getOutArea().setText(i18n(QuiltConstants.AUTHOR));

			// Window area
			windowPanel.setLayout(windowPaneLayout);
			windowPanel.add(jProgramScrollPane1, java.awt.BorderLayout.CENTER);
			windowPanel.add(jToolBar, java.awt.BorderLayout.NORTH);
			windowPanel.add(jLogPanel, java.awt.BorderLayout.SOUTH);
			
			//Create a split pane with the two scroll panes in it.
			splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
			                           windowPanel, drawPanel);
			splitPane.setOneTouchExpandable(true);
			splitPane.setDividerLocation(width*2/5);

			//Provide minimum sizes for the two components in the split pane
			Dimension minimumSize = new Dimension(width/5, height/5);
			windowPanel.setMinimumSize(minimumSize);
			drawPanel.setMinimumSize(minimumSize);		
			this.add(splitPane, java.awt.BorderLayout.CENTER);
			((ProgrammingFrame)parent).setIconImage( Util.image("quilt.png"));
			
		}catch(Exception e){ e.printStackTrace();  }
	}
	
	protected static final String resources="resources/";
	protected static final String images="imgs/";
	protected static final String i18n="I18N/";
	
	protected void initButton( JButton button, String resource, String message ){
		button.setToolTipText(message);
		Image img = Util.image(resource);
		button.setIcon(new ImageIcon(img.getScaledInstance((button==jCommandButton)?60:30, 30, Image.SCALE_SMOOTH)));
		button.setToolTipText(message);
	}
	   
	public void jStyleButton_actionPerformed(ActionEvent actionEvent) {
		String QMS = Util.QMS;
		FileFilter filter = new FileFilter( i18n(QuiltConstants.FILE)+" (*"+QMS+")" );
		filter.add(QMS.substring(1));
		JFileChooser file = new JFileChooser( fileDirStyle );
		file.setFileFilter(filter);
		if( file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ){
			try {
				fileDirStyle = file.getSelectedFile().getAbsolutePath();
				BufferedReader reader = new BufferedReader( new FileReader(fileDirStyle) );
				StringBuffer sb = new StringBuffer();
				String s = reader.readLine();
				while( s != null ){
					sb.append(s);
					s = reader.readLine();
				}
				reader.close();
				String styles = sb.toString();
				jProgram.setStyle(SyntaxStyle.get(styles));
				jCommand.setStyle(SyntaxStyle.get(styles));				
			}catch (Exception e){ e.printStackTrace(); }
		}
	}

	public void jMachineButton_actionPerformed(ActionEvent actionEvent) {
		String QMC = QuiltConstants.QMC;
		FileFilter filter = new FileFilter( i18n(QuiltConstants.FILE)+" (*"+QMC+")" );
		filter.add(QMC.substring(1));
		JFileChooser file = new JFileChooser( fileDirMachine );
		file.setFileFilter(filter);
		if( file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ){
			try {
				fileDirMachine = file.getSelectedFile().getAbsolutePath();
				BufferedReader reader = new BufferedReader( new FileReader(fileDirMachine) );
				StringBuffer sb = new StringBuffer();
				String s = reader.readLine();
				while( s != null ){
					sb.append(s);
					s = reader.readLine();
				}
				reader.close();
				machine = qm.load(ObjectParser.parse(sb.toString()));
				setMachine(machine);
			}catch (Exception e){ e.printStackTrace(); }
		}
	}

	public void jNewButton_actionPerformed(ActionEvent actionEvent) {
		fileName = i18n(QuiltConstants.NONAME);
		if( (jProgram.getText().length()>0 || jCommand.getText().length()>0) && JOptionPane.showConfirmDialog(this, i18n(QuiltConstants.CLEAN)) == JOptionPane.YES_OPTION ){
			jProgram.setText("");
			jCommand.setText("");
		}
		title_component.setTitle(title + " [" + fileName + "]");
	}
	
	public void jOpenButton_actionPerformed(ActionEvent actionEvent) {
		String QMP = QuiltConstants.QMP;
		FileFilter filter = new FileFilter( i18n(QuiltConstants.FILE)+" (*"+QMP+")" );
		filter.add(QMP.substring(1));
		JFileChooser file = new JFileChooser( fileDir );
		file.setFileFilter(filter);
		if( file.showOpenDialog(this) == JFileChooser.APPROVE_OPTION ){
			try {
				fileDir = file.getSelectedFile().getAbsolutePath();
				fileName = file.getSelectedFile().getName();
				BufferedReader reader = new BufferedReader( new FileReader(fileDir) );
				StringBuffer sb = new StringBuffer();
				String s = reader.readLine();
				while( s != null ){
					sb.append(s);
					sb.append('\n');
					s = reader.readLine();
				}
				reader.close();
				jProgram.setText(sb.toString());
				title_component.setTitle(title + " [" + fileName + "]");
			}catch (Exception e){ e.printStackTrace(); }
		}
	}

	public void jSaveButton_actionPerformed(ActionEvent actionEvent) {
		if( fileName.equals(i18n(QuiltConstants.NONAME)) ){
			String QMP = QuiltConstants.QMP;
			FileFilter filter = new FileFilter( i18n(QuiltConstants.FILE)+" (*"+QMP+")" );
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
					writer.write(jProgram.getText());
					writer.close();
					title_component.setTitle(title + " [" + fileName + "]");
				}catch( Exception e ){ e.printStackTrace(); }
			}
		}else{
			try{
				FileWriter writer = new FileWriter(fileDir);
				writer.write(jProgram.getText());
				writer.close();
			}catch( Exception e ){ e.printStackTrace(); }
		}	
	}

	public void jPrimitiveButton_actionPerformed(ActionEvent actionEvent) {
		Collection<String> commands = machine.primitives();
		StringBuilder sb = new StringBuilder();
		for( String c:commands ){
			try{
				sb.append(machine.primitive(c).toString());
				sb.append('\n');
			}catch(Exception e){}
		}
		this.log.getOutArea().setText(sb.toString());
		this.log.getErrorArea().setText("");
	}

	public void jRemnantButton_actionPerformed(ActionEvent actionEvent) {
		Collection<String> remnants = machine.values();
		StringBuilder sb = new StringBuilder();
		for( String r:remnants ){
			sb.append(r);
			sb.append('\n');
		}
		this.log.getOutArea().setText(sb.toString());
		this.log.getErrorArea().setText("");
	}
	
	public void show_error_message( JTextComponent code_area, LanguageException e ){
		if( frame != null ) frame.setVisible(false);
		String msg = e.getMessage();	
		int k = msg.indexOf('=');
		JOptionPane.showMessageDialog(this, i18n(QuiltConstants.ERRORS));
		this.log.getOutArea().setText(i18n(QuiltConstants.ERRORS));
		this.log.getErrorArea().setText(msg.substring(k+1));
		this.log.select(false);
		if( e.position() instanceof Position2D ){
			Position2D pos = (Position2D)e.position();
			if( pos.row()==-1 ){
				code_area=jCommand;
				pos.setRow(0);
			}
			String str = code_area.getText();
			int caret = 0;
			int i=0;
			while( i<pos.row() ){
				while(str.charAt(caret)!='\n') caret++;
				caret++;
				i++;
			}
			code_area.setCaretPosition(caret+pos.column());
			code_area.requestFocusInWindow();
		}	
	}

	public void jCompileButton_actionPerformed(ActionEvent actionEvent) {
		String program = jProgram.getText();
		try{
			machine.clear();
			FunProgram prog = (FunProgram)quiltLang.process( program, true );
			machine.setProgram(prog);
			this.log.getOutArea().setText(i18n(QuiltConstants.NO_ERRORS));
			this.log.getErrorArea().setText("");
			this.log.select(true);
		}catch(LanguageException e){
			show_error_message(jProgram, e);
		}
	}
	
	protected DrawFrame frame = null;
	public void jCommandButton_actionPerformed(ActionEvent actionEvent) {
		String program = jCommand.getText();
		FunCommandCall command=null;
		try{
			command = (FunCommandCall)quiltLang.process( program, false);
			command.setRow(-1);
		}catch(LanguageException e){
			show_error_message(jCommand, e);
		}
		if( command != null ){
			try{
				Quilt r = (Quilt)command.execute( new HTKeyMap<String, Object>() );
				drawPanel.set(r);
				if(this.log.getOutArea().getText().contains(QuiltConstants.ERROR) ) this.log.getOutArea().setText(i18n(QuiltConstants.NO_ERRORS));
				this.log.select(true);
				this.log.getErrorArea().setText("");
			}catch(LanguageException e){
				System.out.println(e.getMessage());
				show_error_message(jProgram, e);
			}
		}	
	}
	
	public void setMachine( QuiltMachine machine ){
		this.machine = machine;
		try{ this.quiltLang = new FunLanguage(machine); }catch(Exception e){}
		Tokenizer tokenizer = FunLanguage.tokenizer(machine);
		jProgram.setTokenizer(tokenizer, tokens);
		jCommand.setTokenizer(tokenizer, tokens);
	}
}


class ProgrammingPanel_jCompileButton_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_jCompileButton_actionAdapter(ProgrammingPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jCompileButton_actionPerformed(actionEvent);
	}
}

class ProgrammingPanel_jRemnantButton_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	ProgrammingPanel_jRemnantButton_actionAdapter(ProgrammingPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jRemnantButton_actionPerformed(actionEvent);
	}
}

class ProgrammingPanel_jPrimitiveButton_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	ProgrammingPanel_jPrimitiveButton_actionAdapter(ProgrammingPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jPrimitiveButton_actionPerformed(actionEvent);
	}
}

class ProgrammingPanel_jCommandButton_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	ProgrammingPanel_jCommandButton_actionAdapter(ProgrammingPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jCommandButton_actionPerformed(actionEvent);
	}
}


class ProgrammingPanel_jSaveButton_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_jSaveButton_actionAdapter(ProgrammingPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jSaveButton_actionPerformed(actionEvent);
	}
}

class ProgrammingPanel_jNewButton_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_jNewButton_actionAdapter(ProgrammingPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jNewButton_actionPerformed(actionEvent);
	}	
}

class ProgrammingPanel_jOpenButton_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_jOpenButton_actionAdapter(ProgrammingPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jOpenButton_actionPerformed(actionEvent);
	}	
}

class ProgrammingPanel_jMachineButton_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_jMachineButton_actionAdapter(ProgrammingPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jMachineButton_actionPerformed(actionEvent);
	}	
}

class ProgrammingPanel_jStyleButton_actionAdapter implements ActionListener {
	private ProgrammingPanel adaptee;
	
	ProgrammingPanel_jStyleButton_actionAdapter(ProgrammingPanel adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jStyleButton_actionPerformed(actionEvent);
	}	
}