package quilt.gui;
import unalcol.gui.io.FileFilter;
import unalcol.gui.log.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import quilt.Remnant;
import quilt.basic.BasicQuiltMachine;


//
//Unalcol Service structure Pack 1.0 by Jonatan Gomez-Perdomo
//https://github.com/jgomezpe/unalcol/tree/master/services/
//
/**
*
* ProgrammingFrame
* <P>GUI for the sewer machine.
*
* <P>
* <A HREF="https://github.com/jgomezpe/unalcol/blob/master/services/src/unalcol/clone/Clone.java" target="_blank">
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
	String title = "Ambiente para Maquina de Coser";
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
	JTextArea jTextArea1 = new JTextArea();
	JToolBar jToolBar1 = new JToolBar();
	JPanel jPanel2 = new JPanel();
	BorderLayout borderLayout2 = new BorderLayout();
	JButton jOpenButton = new JButton();
	JButton jSaveButton = new JButton();
	JButton jCompileButton = new JButton();
	LogPanel theLogPanel = new LogPanel();
	BorderLayout borderLayout3 = new BorderLayout();
	
	// The command panel
	JToolBar jPanel3 = new JToolBar();
	JLabel jCommandLabel = new JLabel("Comando:");
	FlowLayout flowLayout3 = new FlowLayout();
	JTextField jCommand = new JTextField();
	JButton jCommandButton = new JButton();

	
	protected BasicQuiltMachine machine=new BasicQuiltMachine();
	
	public LogPanel getLogPanel(){ return theLogPanel; }

	public ProgrammingFrame(){
		try {
			jbInit();
		}catch(Exception e){ e.printStackTrace();  }
	}
	private void jbInit() throws Exception {
		this.setSize(new Dimension(600, 400));
		jTextArea1.setToolTipText("");
		jTextArea1.setVerifyInputWhenFocusTarget(true);
		jTextArea1.setText("");
		this.setTitle(title);
		this.getContentPane().setLayout(borderLayout1);
		jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jScrollPane1.setAutoscrolls(true);
		jScrollPane1.setMaximumSize(new Dimension(261, 261));
		jScrollPane1.setMinimumSize(new Dimension(261, 261));
		jPanel2.setLayout(borderLayout2);
		jOpenButton.setToolTipText("Abrir");
		jOpenButton.setSelectedIcon(null);
		jOpenButton.setText("Abrir");
		jOpenButton.addActionListener(new
				QuiltMachineProgrammingFrame_jOpenButton_actionAdapter(this));
		jSaveButton.setText("Guardar");
		jSaveButton.addActionListener(new
				QuiltMachineProgrammingFrame_jSaveButton_actionAdapter(this));
		jCompileButton.setToolTipText("Compilar");
		jCompileButton.setText("Compilar");
		jCompileButton.addActionListener(new
				QuiltMachineProgrammingFrame_jCompileButton_actionAdapter(this));
		jLogPanel.setLayout(borderLayout3);
		jToolBar1.add(jOpenButton);
		jToolBar1.add(jSaveButton);
		jToolBar1.add(jCompileButton);
		jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);
		jScrollPane1.getViewport().add(jTextArea1, null);
		jPanel2.add(jToolBar1, java.awt.BorderLayout.NORTH);
		jPanel2.add(jLogPanel, java.awt.BorderLayout.SOUTH);
		this.getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
		jLogPanel.add(this.theLogPanel, java.awt.BorderLayout.CENTER);
		jPanel3.add(jCommandLabel);
		jPanel3.add(jCommand);
		jCommandButton.setText("Ejecutar");
		jCommandButton.addActionListener(new
				QuiltMachineProgrammingFrame_jCommandButton_actionAdapter(this));
		jPanel3.add(jCommandButton);
		jLogPanel.add(jPanel3, java.awt.BorderLayout.NORTH);
		// Closing the window
		this.addWindowListener( new WindowAdapter(){
			public void windowClosing( WindowEvent e ){
				System.exit(0);
			} } );
	}


	void jButton1_actionPerformed(ActionEvent e) {
		try{
			StringBuffer sb = new StringBuffer();
			int n = jTextArea1.getLineCount();
			for (int i = 0; i < n; i++) {
				int start = jTextArea1.getLineStartOffset(i);
				int end = jTextArea1.getLineEndOffset(i);
				String line;
				if( i == n - 1 ){
					line = jTextArea1.getText(start, end - start);
				}else{
					line = jTextArea1.getText(start, end - start - 1);
				}

				sb.append(line);
				sb.append(",");
			}
		}catch( Exception x ){ x.printStackTrace(); }
	}

	public void jOpenButton_actionPerformed(ActionEvent actionEvent) {
		FileFilter filter = new FileFilter( "Archivos Maquina Coser (*.quilt)" );
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
				jTextArea1.setText(sb.toString());
				reader.close();
				this.setTitle(title + " [" + fileName + "]");
			}catch (Exception e){ e.printStackTrace(); }
		}
	}

	public void jSaveButton_actionPerformed(ActionEvent actionEvent) {
		FileFilter filter = new FileFilter(  "Archivos Maquina Coser (*.quilt)"  );
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
				writer.write(jTextArea1.getText());
				writer.close();
				this.setTitle(title + " [" + fileName + "]");
			}catch( Exception e ){ e.printStackTrace(); }
		}
	}

	public void jCompileButton_actionPerformed(ActionEvent actionEvent) {
		String program = jTextArea1.getText();
		try{
			machine.setProgram(program);
			this.theLogPanel.getOutArea().setText("Sin errores de compilación...!!!!");
			this.theLogPanel.getErrorArea().setText("");
		}catch(Exception e){
			JOptionPane.showMessageDialog(this, "Problemas!!! Se presentaron problemas al compilar.\nLa descripción de los errores esta en el Tab error");
			this.theLogPanel.getOutArea().setText("Problemas!!! Se presentaron problemas al compilar.\nLa descripción de los errores esta en el Tab error");
			this.theLogPanel.getErrorArea().setText(e.getMessage());
		}
	}

	protected DrawFrame frame = null;
	public void jCommandButton_actionPerformed(ActionEvent actionEvent) {
		String program = jCommand.getText();
		try{
			Remnant r = machine.execute(program);
		    this.theLogPanel.getOutArea().setText(r.toString());
		    if( frame == null ) frame = new DrawFrame();
			frame.setVisible(true);
			frame.setRemnant(r);
	   	    this.theLogPanel.getOutArea().setText("Sin errores de ejecución...!!!!");
	        this.theLogPanel.getErrorArea().setText("");
		}catch(Exception e){
		      JOptionPane.showMessageDialog(this, "Problemas!!! Se presentaron problemas al ejecutar.\nLa descripción de los errores esta en el Tab error");
		      this.theLogPanel.getOutArea().setText("Problemas!!! Se presentaron problemas al ejecutar.\nLa descripción de los errores esta en el Tab error");
		      this.theLogPanel.getErrorArea().setText(e.getMessage());
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