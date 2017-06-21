package quilt.computer;
import unalcol.gui.io.FileFilter;
import unalcol.gui.log.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.JTextComponent;

import quilt.Language;
import quilt.Position;
import quilt.Remnant;
import quilt.basic.BasicQuiltMachine;
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
	FlowLayout flowLayout1 = new FlowLayout();
	ButtonGroup buttonGroup1 = new ButtonGroup();
	JCheckBox jCheckBox1 = new JCheckBox();
	JCheckBox jCheckBox2 = new JCheckBox();
	JCheckBox jCheckBox3 = new JCheckBox();
	JCheckBox jCheckBox4 = new JCheckBox();
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel jLogPanel = new JPanel();
	JScrollPane jScrollPane1 = new JScrollPane();
	JTextArea jProgram = new JTextArea();
	JToolBar jToolBar1 = new JToolBar();
	JPanel jPanel2 = new JPanel();
	BorderLayout borderLayout2 = new BorderLayout();
	JButton jOpenButton = new JButton();
	JButton jSaveButton = new JButton();
	JButton jCompileButton = new JButton();
	JButton jRemnantButton = new JButton();
	JButton jPrimitiveButton = new JButton();
	LogPanel theLogPanel = new LogPanel();
	BorderLayout borderLayout3 = new BorderLayout();
	
	// The command panel
	JToolBar jPanel3 = new JToolBar();
	JLabel jCommandLabel = new JLabel();
	FlowLayout flowLayout3 = new FlowLayout();
	JTextField jCommand = new JTextField();
	JButton jCommandButton = new JButton();

	
	protected BasicQuiltMachine machine;;
	
	public LogPanel getLogPanel(){ return theLogPanel; }

	public ProgrammingFrame(String language){
		machine = new BasicQuiltMachine(new Language(language));
		title = machine.message(Language.TITLE);
		try {
			jbInit();
		}catch(Exception e){ e.printStackTrace();  }
	}
	private void jbInit() throws Exception {
		this.setSize(new Dimension(600, 400));
		jProgram.setToolTipText("");
		jProgram.setVerifyInputWhenFocusTarget(true);
		jProgram.setText("");
		this.setTitle(title);
		this.getContentPane().setLayout(borderLayout1);
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollPane1.setAutoscrolls(true);
		jScrollPane1.setMaximumSize(new Dimension(261, 261));
		jScrollPane1.setMinimumSize(new Dimension(261, 261));
		jPanel2.setLayout(borderLayout2);
		jOpenButton.setToolTipText(machine.message(Language.OPEN));
		jOpenButton.setSelectedIcon(null);
		jOpenButton.setText(machine.message(Language.OPEN));
		jOpenButton.addActionListener(new
				QuiltMachineProgrammingFrame_jOpenButton_actionAdapter(this));
		jSaveButton.setText(machine.message(Language.SAVE));
		jSaveButton.addActionListener(new
				QuiltMachineProgrammingFrame_jSaveButton_actionAdapter(this));
		jCompileButton.setToolTipText(machine.message(Language.COMPILE));
		jCompileButton.setText(machine.message(Language.COMPILE));
		jCompileButton.addActionListener(new
				QuiltMachineProgrammingFrame_jCompileButton_actionAdapter(this));
		jRemnantButton.setToolTipText(machine.message(Language.REMNANT));
		jRemnantButton.setText(machine.message(Language.REMNANT));
		jRemnantButton.addActionListener(new
				QuiltMachineProgrammingFrame_jRemnantButton_actionAdapter(this));
		jPrimitiveButton.setToolTipText(machine.message(Language.PRIMITIVE));
		jPrimitiveButton.setText(machine.message(Language.PRIMITIVE));
		jPrimitiveButton.addActionListener(new
				QuiltMachineProgrammingFrame_jPrimitiveButton_actionAdapter(this));
		jLogPanel.setLayout(borderLayout3);
		jToolBar1.add(jOpenButton);
		jToolBar1.add(jSaveButton);
		jToolBar1.add(jCompileButton);
		jToolBar1.add(jRemnantButton);
		jToolBar1.add(jPrimitiveButton);
		jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);
		jScrollPane1.getViewport().add(jProgram, null);
		jPanel2.add(jToolBar1, java.awt.BorderLayout.NORTH);
		jPanel2.add(jLogPanel, java.awt.BorderLayout.SOUTH);
		this.getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
		this.theLogPanel.setLanguage(machine.message(Language.OUT), machine.message(Language.ERROR));
		jLogPanel.add(this.theLogPanel, java.awt.BorderLayout.CENTER);
		jCommandLabel.setText(machine.message(Language.COMMAND));
		jPanel3.add(jCommandLabel);
		jPanel3.add(jCommand);
		jCommandButton.setText(machine.message(Language.EXECUTE));
		jCommandButton.addActionListener(new
				QuiltMachineProgrammingFrame_jCommandButton_actionAdapter(this));
		jPanel3.add(jCommandButton);
		jLogPanel.add(jPanel3, java.awt.BorderLayout.NORTH);
		this.theLogPanel.getOutArea().setText(machine.message(Language.AUTHOR));
		// Closing the window
		this.addWindowListener( new WindowAdapter(){
			public void windowClosing( WindowEvent e ){
				System.exit(0);
			} } );
	}


	void jButton1_actionPerformed(ActionEvent e) {
		try{
			StringBuffer sb = new StringBuffer();
			int n = jProgram.getLineCount();
			for (int i = 0; i < n; i++) {
				int start = jProgram.getLineStartOffset(i);
				int end = jProgram.getLineEndOffset(i);
				String line;
				if( i == n - 1 ){
					line = jProgram.getText(start, end - start);
				}else{
					line = jProgram.getText(start, end - start - 1);
				}

				sb.append(line);
				sb.append(",");
			}
		}catch( Exception x ){ x.printStackTrace(); }
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
				jProgram.setText(sb.toString());
				reader.close();
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
			sb.append(c.toString(machine.language().current()));
			sb.append('\n');
		}
		this.theLogPanel.getOutArea().setText(sb.toString());
		this.theLogPanel.getErrorArea().setText("");
	}

	public void jRemnantButton_actionPerformed(ActionEvent actionEvent) {
		String[] remnants = machine.remnants();
		StringBuilder sb = new StringBuilder();
		for( String r:remnants ){
			sb.append(r);
			sb.append('\n');
		}
		this.theLogPanel.getOutArea().setText(sb.toString());
		this.theLogPanel.getErrorArea().setText("");
	}
	
	public void show_error_message( JTextComponent code_area, Exception e ){
		if( frame != null ) frame.setVisible(false);
		String msg = e.getMessage();	
		System.out.println(msg);
		int k = msg.indexOf(Language.MSG_SEPARATOR);
		JOptionPane.showMessageDialog(this, machine.message(Language.ERRORS));
		this.theLogPanel.getOutArea().setText(machine.message(Language.ERRORS));
		this.theLogPanel.getErrorArea().setText(msg.substring(k+1));
		this.theLogPanel.select(false);
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
			this.theLogPanel.getOutArea().setText(machine.message(Language.NO_ERRORS));
			this.theLogPanel.getErrorArea().setText("");
			this.theLogPanel.select(true);
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
				if(this.theLogPanel.getOutArea().getText().contains(Language.ERROR) ) this.theLogPanel.getOutArea().setText(machine.message(Language.NO_ERRORS));
				this.theLogPanel.select(true);
				this.theLogPanel.getErrorArea().setText("");
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