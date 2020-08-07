package com.aliensdecoder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;

import com.aliensdecoder.syntaxedit.SyntaxEditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import quilt.factory.QuiltMachineInstance;
import quilt.operation.Command;
import quilt.operation.CommandCall;
import quilt.util.Language;
import quilt.util.Util;
import unalcol.gui.I18N.I18NManager;
import unalcol.gui.editor.ErrorManager;
import unalcol.gui.editor.Position;
import unalcol.gui.editor.SyntaxStyle;

/**
 *
 * MainActivity
 * <P>Programming Environment.
 *
 * <P>
 * <A HREF="https://github.com/jgomezpe/quilt/tree/master/quilt_android/src/main/java/com/aliensdecoder/AndroidDrawer.java" target="_blank">
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
public class MainActivity extends Activity {
	private SyntaxEditText program_input, command_input;
	private Button newBtn, load, save, compile, remnant, commands, machine, style, run;
	private Context context;
	private TextView fileText, logText;
	private String fileName;
	protected String[] tokens = { "undef", "regular", "comment", "symbol", "stitch", "regular", "reserved", "remnant" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this.getApplicationContext();

		// Quilt machine
		Store.load_language(getResources().getString(R.string.language));
		String conf_file = "default.qmc";
		String machine_txt = Util.config(conf_file);
		Store.load(machine_txt);
		conf_file = "default.qms";
		SyntaxStyle[] styles = SyntaxStyle.get(Util.config(conf_file));

		// Program Area
		program_input = new SyntaxEditText(findViewById(R.id.programInp));
		program_input.input().setMovementMethod(new ScrollingMovementMethod());
		program_input.setTokenizer(Store.machine.parser(), tokens);
		program_input.setStyle(styles);

		// Command Area
		command_input = new SyntaxEditText(findViewById(R.id.commandInp));
		command_input.setTokenizer(Store.machine.parser(), tokens);
		command_input.setStyle(styles);

		// FileInfo Area
		fileName = Store.manager.get(Language.NONAME);
		fileText = (TextView) findViewById(R.id.fileText);
		fileText.setText(fileName);

		// Log Area
		logText = (TextView) findViewById(R.id.logText);
		logText.setText(Store.manager.get(Language.AUTHOR));

		// Tool bar
		// new button
		newBtn = (Button) findViewById(R.id.newBtn);
		newBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which){
							case DialogInterface.BUTTON_POSITIVE:
								program_input.setText("");
								command_input.setText("");
								break;

							case DialogInterface.BUTTON_NEGATIVE:
								break;
						}
						fileName = Store.manager.get(Language.NONAME);
						fileText.setText(fileName);
					}
				};

				if(program_input.getText().length()>0 || command_input.getText().length()>0) {
					AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
					builder.setMessage(Store.machine.message(Language.CLEAN)).setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
							.setNegativeButton(getResources().getString(R.string.no), dialogClickListener).show();
				}else{
					fileName = Store.manager.get(Language.NONAME);
					fileText.setText(fileName);
				}
			}
		});

		// load button
		load = (Button) findViewById(R.id.openBtn);
		load.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FileChooser fc = new FileChooser(MainActivity.this, true);
				fc.setExtension(".qmp");
				fc.setFileListener(new FileChooser.FileSelectedListener() {
					@Override
					public void fileSelected(final File file) {
						try{
							fileName = file.getAbsolutePath();
							fileText.setText(fileName);
							BufferedReader reader = new BufferedReader(new FileReader(file));
							StringBuffer sb = new StringBuffer();
							String s = reader.readLine();
							while (s != null) {
								sb.append(s);
								sb.append('\n');
								s = reader.readLine();
							}
							reader.close();
							program_input.setText(sb.toString());
						}catch(Exception e){
							Toast tmsg = Toast.makeText(getBaseContext(), e.getMessage(),
									Toast.LENGTH_LONG);
							tmsg.show();
						}
					}
				});
				fc.showDialog();
			}
		});

		// save button
		save = (Button) findViewById(R.id.saveBtn);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (fileName.equals(Store.machine.message(Language.NONAME))) {
					FileChooser fc = new FileChooser(MainActivity.this, false);
					fc.setExtension(".qmp");
					fc.setFileListener(new FileChooser.FileSelectedListener() {
						@Override
						public void fileSelected(final File file) {
							fileName = file.getAbsolutePath();
							fileText.setText(fileName);
							save();
						}
					});
					fc.showDialog();
				}else{
					save();
				}
			}
		});

		// compile button
		compile = (Button) findViewById(R.id.compileBtn);
		compile.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Store.machine.setProgram(program_input.getText());
					logText.setText(Store.machine.language().get(Language.NO_ERRORS));
				} catch (Exception e) {
					show_error_message(program_input, e);
				}
			}
		});

		// remnant button
		remnant = (Button) findViewById(R.id.remnantBtn);
		remnant.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String[] remnants = Store.machine.remnants();
				StringBuilder sb = new StringBuilder();
				for( String r:remnants ){
					sb.append(r);
					sb.append('\n');
				}
				logText.setText(sb.toString());
			}
		});

		// commands button
		commands = (Button) findViewById(R.id.functionBtn);
		commands.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Command[] commands = Store.machine.primitives();
				StringBuilder sb = new StringBuilder();
				for( Command c:commands ){
					sb.append(c.toString(Store.machine.language()));
					sb.append('\n');
				}
				logText.setText(sb.toString());
			}
		});

		// config button
		machine = (Button) findViewById(R.id.machineBtn);
		machine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FileChooser fc = new FileChooser(MainActivity.this, true);
				fc.setExtension(".qmc");
				fc.setFileListener(new FileChooser.FileSelectedListener() {
					@Override
					public void fileSelected(final File file) {
						try{
							BufferedReader reader = new BufferedReader(new FileReader(file));
							StringBuffer sb = new StringBuffer();
							String s = reader.readLine();
							while (s != null) {
								sb.append(s);
								s = reader.readLine();
							}
							reader.close();
							QuiltMachineInstance qm = new QuiltMachineInstanceForAndroid(Store.machine.language());
							Store.load(sb.toString());
							program_input.setTokenizer(Store.machine.parser(), tokens);
							command_input.setTokenizer(Store.machine.parser(), tokens);
						}catch(Exception e){
							Toast tmsg = Toast.makeText(getBaseContext(), e.getMessage(),
									Toast.LENGTH_LONG);
							tmsg.show();
						}
					}
				});
				fc.showDialog();
			}
		});

		// config button
		style = (Button) findViewById(R.id.styleBtn);
		style.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FileChooser fc = new FileChooser(MainActivity.this, true);
				fc.setExtension(".qms");
				fc.setFileListener(new FileChooser.FileSelectedListener() {
					@Override
					public void fileSelected(final File file) {
						try{
							BufferedReader reader = new BufferedReader(new FileReader(file));
							StringBuffer sb = new StringBuffer();
							String s = reader.readLine();
							while (s != null) {
								sb.append(s);
								s = reader.readLine();
							}
							reader.close();
							String styles = sb.toString();
							program_input.setStyle(SyntaxStyle.get(styles));
							command_input.setStyle(SyntaxStyle.get(styles));
						}catch(Exception e){
							Toast tmsg = Toast.makeText(getBaseContext(), e.getMessage(),
									Toast.LENGTH_LONG);
							tmsg.show();
						}
					}
				});
				fc.showDialog();
			}
		});

		// Run button
		run = (Button) findViewById(R.id.runBtn);
		run.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String program = command_input.getText();
				CommandCall command = null;
				try {
					command = Store.machine.command(program);
					command.setRow(-1);
				} catch (Exception e) {
					show_error_message(command_input, e);
				}
				if (command != null) {
					try {
						Store.remnant = Store.machine.execute(command);
						Intent myIntent = new Intent(context, QuiltScreen.class);
						startActivity(myIntent);
						if (logText.getText().toString().contains(ErrorManager.ERROR))
							logText.setText(Store.machine.message(Language.NO_ERRORS));
					} catch (Exception e) {
						show_error_message(program_input, e);
					}
				}
			}
		});


//check for permission
/*		if(ContextCompat.checkSelfPermission(this,
				Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
			//ask for permission
			ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1234);
		} */

	}

	public void save(){
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.write(program_input.getText());
			writer.close();
		} catch (Exception e) {
			Toast tmsg = Toast.makeText(getBaseContext(), e.getMessage(),
					Toast.LENGTH_LONG);
			tmsg.show();
		}
	}

	public void show_error_message(SyntaxEditText code_area, Exception e) {
		String msg = e.getMessage();
		int k = msg.indexOf(I18NManager.MSG_SEPARATOR);
		Position pos = new Position(msg.substring(0, k));
		if (pos.row() == -1) {
			code_area = command_input;
			pos.setRow(0);
		}
		code_area.locate(pos.row(), pos.column());
		logText.setText(msg.substring(k + 1));
		Toast tmsg = Toast.makeText(getBaseContext(), Store.machine.message(Language.ERRORS),
				Toast.LENGTH_LONG);
		tmsg.show();
	}
}
