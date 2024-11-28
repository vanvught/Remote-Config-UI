/* Copyright (C) 2024 by Arjan van Vught mailto:info@gd32-dmx.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.dmx.gd32;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

public class WizardShowTxt extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final String TXT_FILE = "show.txt";
	
	OrangePi opi = null;
	RemoteConfig remoteConfig = null;
	String txt = null;
	
	private final JPanel contentPanel = new JPanel();
	
	private JButton btnSetDefaults;
	private JButton btnSave;
	private JButton btnCancel;
	
	private JFormattedTextField formattedTextFieldShow;
	private JCheckBox chckbxAutoStart;
	private JCheckBox chckbxLoop;
	private JLabel lblDmxMaster;
	private JSpinner spinner;
	private JLabel lblOscDisabled;
	private JFormattedTextField formattedTextFieldOscPortOutgoing;
	private JFormattedTextField formattedTextFieldOscPortIncoming;
	
	private boolean OscEnabled = false;
	private boolean DmxMasterEnabled = false;
	
	public static void main(String[] args) {
		try {
			WizardShowTxt dialog = new WizardShowTxt();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public WizardShowTxt() {
		setTitle("Show player");
		initComponents();
		createEvents();
	}
	
	public WizardShowTxt(String nodeId, OrangePi opi, RemoteConfig remoteConfig) {
		this.opi = opi;
		this.remoteConfig = remoteConfig;

		setTitle(nodeId);

		initComponents();
		createEvents();

		load();
	}
	
	private void initComponents() {
		setBounds(100, 100, 320, 258);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.WEST);
		
		JLabel lblShow = new JLabel("Show");
		
		NumberFormat format = NumberFormat.getInstance();
		format.setGroupingUsed(false);
		format.setMinimumIntegerDigits(0);
		
		NumberFormatter formatterShow = new NumberFormatter(format);
		formatterShow.setValueClass(Integer.class);
		formatterShow.setMinimum(0);
		formatterShow.setMaximum(99);
		formatterShow.setAllowsInvalid(false);
		formatterShow.setCommitsOnValidEdit(true);
		
		formattedTextFieldShow = new JFormattedTextField(formatterShow);
		chckbxAutoStart = new JCheckBox("Auto start");
		chckbxLoop = new JCheckBox("Loop");
		lblDmxMaster = new JLabel("DMX Master");
		
	    SpinnerModel value = new SpinnerNumberModel(255, 0, 255, 1);
		spinner = new JSpinner(value);
		spinner.setEnabled(false);
		
		JLabel lblNewLabel = new JLabel("(Pixel output only)");
		
		JLabel lblOSC = new JLabel("OSC");
		JLabel lblOscPortIncoming = new JLabel("Port Incoming");
		JLabel lblOscPortOutgoing = new JLabel("Port Outgoing");
		
		NumberFormatter formatterOsc = new NumberFormatter(format);
		formatterOsc.setValueClass(Integer.class);
		formatterOsc.setMinimum(1024);
		formatterOsc.setMaximum(65535);
		formatterOsc.setAllowsInvalid(false);
		formatterOsc.setCommitsOnValidEdit(true);
		
		formattedTextFieldOscPortIncoming = new JFormattedTextField(formatterOsc);
		formattedTextFieldOscPortOutgoing = new JFormattedTextField(formatterOsc);
		
		lblOscDisabled = new JLabel("Disabled");

		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblShow)
								.addComponent(lblDmxMaster)
								.addComponent(lblOSC))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblNewLabel))
								.addComponent(lblOscDisabled)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(formattedTextFieldShow, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(chckbxAutoStart)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(chckbxLoop))))
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
							.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
								.addComponent(lblOscPortOutgoing)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(formattedTextFieldOscPortOutgoing, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_contentPanel.createSequentialGroup()
								.addGap(4)
								.addComponent(lblOscPortIncoming)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(formattedTextFieldOscPortIncoming, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblShow)
							.addComponent(formattedTextFieldShow, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(chckbxAutoStart)
						.addComponent(chckbxLoop))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDmxMaster)
						.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOSC)
						.addComponent(lblOscDisabled))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblOscPortIncoming)
							.addComponent(formattedTextFieldOscPortIncoming, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(formattedTextFieldOscPortOutgoing, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblOscPortOutgoing)))
					.addContainerGap(37, Short.MAX_VALUE))
		);
		
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnSave = new JButton("Save");
				buttonPane.add(btnSave);
			}
			{
				btnSetDefaults = new JButton("Set default");
				buttonPane.add(btnSetDefaults);
			}
			{
				btnCancel = new JButton("Cancel");
				buttonPane.add(btnCancel);
			}
			getRootPane().setDefaultButton(btnCancel);
		}
	}
	
	private void createEvents() {
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		
		btnSetDefaults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remoteConfig.setTextArea(opi.doDefaults(TXT_FILE));
				load();
			}
		});
	}
	
	private void load() {
		if (opi != null) {		
			txt = opi.getTxt(TXT_FILE);
			System.out.println(">|" + txt + "|<");
			if (txt != null) {
				final String[] lines = txt.split("\n");
				for (int i = 0; i < lines.length; i++) {
					final String line = lines[i];
					
					if (line.contains("show")) {
						formattedTextFieldShow.setValue(Properties.getInt(line));
						continue;
					}
					
					if (line.contains("auto_start")) {
						chckbxAutoStart.setSelected(Properties.getBool(line));
						continue;
					}
					
					if (line.contains("loop")) {
						chckbxLoop.setSelected(Properties.getBool(line));
						continue;
					}
					
					if (line.contains("dmx_master")) {
						spinner.setValue(Properties.getInt(line));
						DmxMasterEnabled = true;
						continue;
					}
					
					if (line.contains("incoming_port")) {
						formattedTextFieldOscPortIncoming.setValue(Properties.getInt(line));
						OscEnabled = true;
						continue;
					}
					
					if (line.contains("outgoing_port")) {
						formattedTextFieldOscPortOutgoing.setValue(Properties.getInt(line));
						continue;
					}
				}
			}
		}
		
		if (OscEnabled) {
			lblOscDisabled.setText("");
		}
		
		spinner.setEnabled(DmxMasterEnabled);
	}
	
	private void save() {
		if (opi != null) {
			StringBuffer txtFile = new StringBuffer("#" + TXT_FILE + "\n");
			
			txtFile.append(String.format("show=%s\n", formattedTextFieldShow.getText()));
			txtFile.append(String.format("auto_start=%d\n", chckbxAutoStart.isSelected() ? 1 : 0));
			txtFile.append(String.format("loop=%d\n", chckbxLoop.isSelected() ? 1 : 0));
			
			txtFile.append(String.format("dmx_master=%d\n", spinner.getValue()));
			
			if (OscEnabled) {
				txtFile.append(String.format("incoming_port=%s\n", formattedTextFieldOscPortIncoming.getText()));
				txtFile.append(String.format("outgoing_port=%s\n", formattedTextFieldOscPortOutgoing.getText()));
			}
			
			try {
				opi.doSave(txtFile.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}

			load();

			if (remoteConfig != null) {
				remoteConfig.setTextArea(this.txt);
			}
		}
	}
}
