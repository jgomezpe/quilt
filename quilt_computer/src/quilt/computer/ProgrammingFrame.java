package quilt.computer;
import unalcol.gui.I18N.I18NManager;
import unalcol.gui.editor.ErrorManager;
import unalcol.gui.editor.Position;
import unalcol.gui.editor.SyntaxEditPanel;
import unalcol.gui.io.FileFilter;
import unalcol.gui.log.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.text.JTextComponent;

import quilt.util.Language;
import quilt.QuiltMachine;
import quilt.Remnant;
import quilt.operation.Command;
import quilt.operation.CommandCall;


//
//Quilt Sewer Machine 1.0 by Jonatan Gomez-Perdomo
//https://github.com/jgomezpe/quilt/tree/master/quilt/
//
/**
*
* ProgrammingFrame
* <P>GUI for the sewer machine.
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/quilt/src/quilt/gui/ProgrammingFrame.java" target="_blank">
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
public class ProgrammingFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7085997916395110108L;
	String title;
	String fileName = null;
	String fileDir = ".";
	String language="spanish";
	boolean asResource=false;
	
	// Window area
	BorderLayout windowLayout = new BorderLayout();
	JPanel windowPanel = new JPanel();
	BorderLayout windowPaneLayout = new BorderLayout();
	
	// The program area
	JScrollPane jProgramScrollPane1 = new JScrollPane();
	SyntaxEditPanel jProgram = new SyntaxEditPanel();
	
	// The tool bar
	JToolBar jToolBar = new JToolBar();
	JButton jOpenButton = new JButton();
	JButton jSaveButton = new JButton();
	JButton jCompileButton = new JButton();
	JButton jRemnantButton = new JButton();
	JButton jPrimitiveButton = new JButton();
	
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

	
	protected QuiltMachine machine;;
	
	public LogPanel getLogPanel(){ return log; }

	public ProgrammingFrame(String language, boolean asResource){
		this.language=language;
		this.asResource=asResource;
		System.out.println(i18n_file_name());
		try{
			String fileName = i18n_file_name();
			InputStream in;
			if( asResource ) in = this.getClass().getResourceAsStream(fileName);
			else in = new FileInputStream(fileName);
		
			machine = QuiltMachinePicker.get(4, new ErrorManager(in), image("remnant.png"));
			title = machine.message(Language.TITLE);
			jbInit();
		}catch(Exception e){ e.printStackTrace();  }
	}
	private void jbInit() throws Exception {
		this.setSize(new Dimension(600, 400));
		this.setTitle(title);
		this.getContentPane().setLayout(windowLayout);

		// Program area
		jProgram.setTokenizer(machine.parser(), tokens);
		jProgram.setStyle(styles);
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
		initButton(jOpenButton, "open.png", machine.message(Language.OPEN));
		jOpenButton.addActionListener(new QuiltMachineProgrammingFrame_jOpenButton_actionAdapter(this));
		initButton(jSaveButton, "save.png", machine.message(Language.SAVE));
		jSaveButton.addActionListener(new QuiltMachineProgrammingFrame_jSaveButton_actionAdapter(this));
		initButton(jCompileButton, "compile.png", machine.message(Language.COMPILE));
		jCompileButton.addActionListener(new QuiltMachineProgrammingFrame_jCompileButton_actionAdapter(this));
		initButton(jRemnantButton, "remnant.png", machine.message(Language.REMNANT));
		jRemnantButton.addActionListener(new QuiltMachineProgrammingFrame_jRemnantButton_actionAdapter(this));
		initButton(jPrimitiveButton, "tools.png", machine.message(Language.PRIMITIVE));
		jPrimitiveButton.addActionListener(new QuiltMachineProgrammingFrame_jPrimitiveButton_actionAdapter(this));
		jToolBar.add(jOpenButton);
		jToolBar.add(jSaveButton);
		jToolBar.add(jCompileButton);
		jToolBar.add(jRemnantButton);
		jToolBar.add(jPrimitiveButton);

		// Command area
		jCommandBar.setLayout(commandLayout);
		jCommandBar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		jCommandLabel.setText(machine.message(Language.COMMAND));
		jCommand.setTokenizer(machine.parser(), tokens);
		jCommand.setStyle(styles);
		jCommand.setToolTipText("");
		jCommand.setVerifyInputWhenFocusTarget(true);
		jCommand.setText("");
		
		initButton(jCommandButton, "quilt-run.png", machine.message(Language.EXECUTE));
		jCommandButton.addActionListener(new QuiltMachineProgrammingFrame_jCommandButton_actionAdapter(this));
		jCommandBar.add(jCommandLabel, java.awt.BorderLayout.WEST);
		jCommandBar.add(jCommand, java.awt.BorderLayout.CENTER);
		jCommandBar.add(jCommandButton, java.awt.BorderLayout.EAST);

		//Log area
		jLogPanel.setLayout(logLayout);	
		jLogPanel.add(log, java.awt.BorderLayout.CENTER);
		jLogPanel.add(jCommandBar, java.awt.BorderLayout.NORTH);
		log.setLanguage(machine.message(Language.OUT), machine.message(ErrorManager.ERROR));
		log.getOutArea().setText(machine.message(Language.AUTHOR));

		// Window area
		windowPanel.setLayout(windowPaneLayout);
		windowPanel.add(jProgramScrollPane1, java.awt.BorderLayout.CENTER);
		windowPanel.add(jToolBar, java.awt.BorderLayout.NORTH);
		windowPanel.add(jLogPanel, java.awt.BorderLayout.SOUTH);
		this.getContentPane().add(windowPanel, java.awt.BorderLayout.CENTER);
	
		// Closing the window
		this.addWindowListener( new WindowAdapter(){
			public void windowClosing( WindowEvent e ){
				System.exit(0);
			} } );
	}
	
	protected static final String resources="resources/";
	protected static final String images="imgs/";
	protected static final String i18n="I18N/";
	
	public Image image(String resource){
		System.out.println(asResource);
		try {
			if( asResource )
				return ImageIO.read(getClass().getResource("/"+images+resource));
			else{
				File f = new File(resources+images+resource);
				return ImageIO.read(f);
			}	
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			return null; 
		}		
	}
	
	public String i18n_file_name(){
		if( asResource ) return "/"+i18n+language+".txt";
		else return resources+i18n+language+".txt";
	}
	
	protected void initButton( JButton button, String resource, String message ){
		button.setToolTipText(message);
		try {
			Image img;
			if( asResource ) img = ImageIO.read(getClass().getResource("/"+images+resource));
			else{
				File f = new File(resources+images+resource);
				img = ImageIO.read(f);
			}
			button.setIcon(new ImageIcon(img.getScaledInstance((button==jCommandButton)?60:30, 30, Image.SCALE_SMOOTH)));
		} catch (Exception ex) {
			button.setText(message); 
		}		
		button.setToolTipText(message);
	}
	
    protected String[] tokens = { "undef", "regular", "comment", "symbol", "stitch", "regular", "reserved", "remnant" };
    protected String[] styles = {"[regular,[SansSerif,12]]", "[undef,["+color(Color.pink)+"]]","[comment,[italic,"+color(Color.gray)+"]]",
    							 "[symbol,["+color(Color.blue)+"]]","[stitch,["+color(Color.red)+"]]",
    							 "[reserved,[bold]]","[remnant,["+color(Color.orange)+"]]"};
    
	protected static String color( Color c ){
		return "["+c.getRed()+","+c.getGreen()+","+c.getBlue()+","+c.getAlpha()+"]";
	}
    
	public void jOpenButton_actionPerformed(ActionEvent actionEvent) {
		FileFilter filter = new FileFilter( machine.message(Language.FILE)+" (*.quilt)" );
		filter.add("quilt");
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
				this.setTitle(title + " [" + fileName + "]");
			}catch (Exception e){ e.printStackTrace(); }
		}
	}

	public void jSaveButton_actionPerformed(ActionEvent actionEvent) {
		FileFilter filter = new FileFilter(  machine.message(Language.FILE)+" (*.quilt)"  );
		filter.add("quilt");
		JFileChooser file = new JFileChooser( fileDir );
		file.setFileFilter(filter);
		if( file.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ){
			try{
				String fileExt = ".quilt";  
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
				this.setTitle(title + " [" + fileName + "]");
			}catch( Exception e ){ e.printStackTrace(); }
		}
	}

	public void jPrimitiveButton_actionPerformed(ActionEvent actionEvent) {
		Command[] commands = machine.primitives();
		StringBuilder sb = new StringBuilder();
		for( Command c:commands ){
			sb.append(c.toString(language));
			sb.append('\n');
		}
		this.log.getOutArea().setText(sb.toString());
		this.log.getErrorArea().setText("");
	}

	public void jRemnantButton_actionPerformed(ActionEvent actionEvent) {
		String[] remnants = machine.remnants();
		StringBuilder sb = new StringBuilder();
		for( String r:remnants ){
			sb.append(r);
			sb.append('\n');
		}
		this.log.getOutArea().setText(sb.toString());
		this.log.getErrorArea().setText("");
	}
	
	public void show_error_message( JTextComponent code_area, Exception e ){
		if( frame != null ) frame.setVisible(false);
		String msg = e.getMessage();	
		System.out.println(msg);
		int k = msg.indexOf(I18NManager.MSG_SEPARATOR);
		JOptionPane.showMessageDialog(this, machine.message(Language.ERRORS));
		this.log.getOutArea().setText(machine.message(Language.ERRORS));
		this.log.getErrorArea().setText(msg.substring(k+1));
		this.log.select(false);
		Position pos = new Position(msg.substring(0, k));
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

	public void jCompileButton_actionPerformed(ActionEvent actionEvent) {
		String program = jProgram.getText();
		try{
			machine.setProgram(program);
			this.log.getOutArea().setText(machine.message(Language.NO_ERRORS));
			this.log.getErrorArea().setText("");
			this.log.select(true);
		}catch(Exception e){
			show_error_message(jProgram, e);
		}
	}
	
	protected DrawFrame frame = null;
	public void jCommandButton_actionPerformed(ActionEvent actionEvent) {
		String program = jCommand.getText();
		CommandCall command=null;
		try{
			command = machine.command(program);
			command.setRow(-1);
		}catch(Exception e){
			show_error_message(jCommand, e);
		}
		if( command != null ){
			try{
				Remnant r = machine.execute(command);
			    //this.theLogPanel.getOutArea().setText(r.toString());
			    if( frame == null ) frame = new DrawFrame();
				frame.setVisible(true);
				frame.setRemnant(r);
				if(this.log.getOutArea().getText().contains(ErrorManager.ERROR) ) this.log.getOutArea().setText(machine.message(Language.NO_ERRORS));
				this.log.select(true);
				this.log.getErrorArea().setText("");
			}catch(Exception e){
				show_error_message(jProgram, e);
			}
		}	
	}
}


class QuiltMachineProgrammingFrame_jCompileButton_actionAdapter implements ActionListener {
	private ProgrammingFrame adaptee;
	QuiltMachineProgrammingFrame_jCompileButton_actionAdapter(ProgrammingFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jCompileButton_actionPerformed(actionEvent);
	}
}

class QuiltMachineProgrammingFrame_jRemnantButton_actionAdapter implements ActionListener {
	private ProgrammingFrame adaptee;
	QuiltMachineProgrammingFrame_jRemnantButton_actionAdapter(ProgrammingFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jRemnantButton_actionPerformed(actionEvent);
	}
}

class QuiltMachineProgrammingFrame_jPrimitiveButton_actionAdapter implements ActionListener {
	private ProgrammingFrame adaptee;
	QuiltMachineProgrammingFrame_jPrimitiveButton_actionAdapter(ProgrammingFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jPrimitiveButton_actionPerformed(actionEvent);
	}
}

class QuiltMachineProgrammingFrame_jCommandButton_actionAdapter implements ActionListener {
	private ProgrammingFrame adaptee;
	QuiltMachineProgrammingFrame_jCommandButton_actionAdapter(ProgrammingFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jCommandButton_actionPerformed(actionEvent);
	}
}


class QuiltMachineProgrammingFrame_jSaveButton_actionAdapter implements ActionListener {
	private ProgrammingFrame adaptee;
	QuiltMachineProgrammingFrame_jSaveButton_actionAdapter(ProgrammingFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jSaveButton_actionPerformed(actionEvent);
	}
}

class QuiltMachineProgrammingFrame_jOpenButton_actionAdapter implements ActionListener {
	private ProgrammingFrame adaptee;
	QuiltMachineProgrammingFrame_jOpenButton_actionAdapter(ProgrammingFrame adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent actionEvent) {
		adaptee.jOpenButton_actionPerformed(actionEvent);
	}
}