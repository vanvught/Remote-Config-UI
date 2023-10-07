/* Copyright (C) 2021-2022 by Arjan van Vught mailto:info@gd32-dmx.org
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

package org.orangepi.dmx;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import javax.swing.JTextField;

public class WizardE131Txt extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final String TXT_FILE = "e131.txt";
	
	String nodeId = null;
	OrangePi opi = null;
	RemoteConfig remoteConfig = null;
	String txt = null;

	private final JPanel contentPanel = new JPanel();
	private JButton btnCancel;
	private JButton btnSetDefaults;
	private JButton btnSave;
	
	private JFormattedTextField formattedTextFieldPortA;
	private JFormattedTextField formattedTextFieldPortB;
	private JFormattedTextField formattedTextFieldPortC;
	private JFormattedTextField formattedTextFieldPortD;
	
	private JComboBox<String>  comboBoxMergePortA;
	private JComboBox<String>  comboBoxMergePortB;
	private JComboBox<String>  comboBoxMergePortC;
	private JComboBox<String>  comboBoxMergePortD;
		
	private JComboBox<String> comboBoxDirectionPortA;
	private JComboBox<String> comboBoxDirectionPortB;
	private JComboBox<String> comboBoxDirectionPortC;
	private JComboBox<String> comboBoxDirectionPortD;
	
	private JComboBox<String> comboBoxOutputStyleA;
	private JComboBox<String> comboBoxOutputStyleB;
	private JComboBox<String> comboBoxOutputStyleC;
	private JComboBox<String> comboBoxOutputStyleD;
	
	private JFormattedTextField formattedTextFieldPriorityPortA;
	private JFormattedTextField formattedTextFieldPriorityPortB;
	private JFormattedTextField formattedTextFieldPriorityPortC;
	private JFormattedTextField formattedTextFieldPriorityPortD;
	
	private JLabel lblUniverse;
	private JLabel lblDirection;
	private JLabel lblMerge;
	private JLabel lblPriority;
	
	private boolean hasUniverseA = false;
	private boolean hasUniverseB = false;
	private boolean hasUniverseC = false;
	private boolean hasUniverseD = false;
	
	private boolean hasOutputStyleA = false;
	private boolean hasOutputStyleB = false;
	private boolean hasOutputStyleC = false;
	private boolean hasOutputStyleD = false;
	
	private JTextField textFieldLabelPortA;
	private JTextField textFieldLabelPortB;
	private JTextField textFieldLabelPortC;
	private JTextField textFieldLabelPortD;
	private JComboBox<String> comboBoxFailsafe;

	public static void main(String[] args) {
		try {
			WizardArtnetTxt dialog = new WizardArtnetTxt();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public WizardE131Txt() {
		setTitle("sACN E1.31");
		initComponents();
		createEvents();

	}
	
	public WizardE131Txt(String nodeId, OrangePi opi, RemoteConfig remoteConfig) {
		this.nodeId = nodeId;
		this.opi = opi;
		this.remoteConfig = remoteConfig;

		setTitle(nodeId);

		initComponents();
		createEvents();

		load();
	}
	
	private void initComponents() {
		setBounds(100, 100, 760, 263);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatterUniverse = new NumberFormatter(format);
		formatterUniverse.setValueClass(Integer.class);
		formatterUniverse.setMinimum(0);
		formatterUniverse.setMaximum(32768);
		formatterUniverse.setAllowsInvalid(false);
		formatterUniverse.setCommitsOnValidEdit(true);
		
		JLabel lblUniversePortA = new JLabel("Port A");
		JLabel lblUniversePortB = new JLabel("Port B");
		JLabel lblUniversePortC = new JLabel("Port C");
		JLabel lblUniversePortD = new JLabel("Port D");
		
		formattedTextFieldPortA = new JFormattedTextField(formatterUniverse);
		formattedTextFieldPortA.setColumns(6);
		formattedTextFieldPortB = new JFormattedTextField(formatterUniverse);
		formattedTextFieldPortB.setColumns(6);
		formattedTextFieldPortC = new JFormattedTextField(formatterUniverse);
		formattedTextFieldPortC.setColumns(6);
		formattedTextFieldPortD = new JFormattedTextField(formatterUniverse);
		formattedTextFieldPortD.setColumns(6);
		
		comboBoxMergePortA = new JComboBox<String> ();
		comboBoxMergePortA.setModel(new DefaultComboBoxModel<String> (new String[] {"HTP", "LTP"}));
		comboBoxMergePortB = new JComboBox<String> ();
		comboBoxMergePortB.setModel(new DefaultComboBoxModel<String> (new String[] {"HTP", "LTP"}));
		comboBoxMergePortC = new JComboBox<String> ();
		comboBoxMergePortC.setModel(new DefaultComboBoxModel<String> (new String[] {"HTP", "LTP"}));
		comboBoxMergePortD = new JComboBox<String> ();
		comboBoxMergePortD.setModel(new DefaultComboBoxModel<String> (new String[] {"HTP", "LTP"}));
				
		comboBoxDirectionPortA = new JComboBox<String>();
		comboBoxDirectionPortA.setModel(new DefaultComboBoxModel<String>(new String[] {"Output", "Input", "Disable"}));
		
		comboBoxDirectionPortB = new JComboBox<String>();
		comboBoxDirectionPortB.setModel(new DefaultComboBoxModel<String>(new String[] {"Output", "Input", "Disable"}));
		
		comboBoxDirectionPortC = new JComboBox<String>();
		comboBoxDirectionPortC.setModel(new DefaultComboBoxModel<String>(new String[] {"Output", "Input", "Disable"}));
		
		comboBoxDirectionPortD = new JComboBox<String>();
		comboBoxDirectionPortD.setModel(new DefaultComboBoxModel<String>(new String[] {"Output", "Input", "Disable"}));
		
		JLabel lblOutputStyle = new JLabel("OutputSyle");
		
		NumberFormatter formatterPriority = new NumberFormatter(format);
		formatterPriority.setValueClass(Integer.class);
		formatterPriority.setMinimum(1);
		formatterPriority.setMaximum(200);
		formatterPriority.setAllowsInvalid(false);
		formatterPriority.setCommitsOnValidEdit(true);
		
		formattedTextFieldPriorityPortA = new JFormattedTextField(formatterPriority);
		formattedTextFieldPriorityPortA.setEditable(false);
		formattedTextFieldPriorityPortA.setText("100");
		formattedTextFieldPriorityPortA.setColumns(3);
		
		formattedTextFieldPriorityPortB = new JFormattedTextField(formatterPriority);
		formattedTextFieldPriorityPortB.setText("100");
		formattedTextFieldPriorityPortB.setEditable(false);
		formattedTextFieldPriorityPortB.setColumns(3);
		
		formattedTextFieldPriorityPortC = new JFormattedTextField(formatterPriority);
		formattedTextFieldPriorityPortC.setText("100");
		formattedTextFieldPriorityPortC.setEditable(false);
		formattedTextFieldPriorityPortC.setColumns(3);
		
		formattedTextFieldPriorityPortD = new JFormattedTextField(formatterPriority);
		formattedTextFieldPriorityPortD.setText("100");
		formattedTextFieldPriorityPortD.setEditable(false);
		formattedTextFieldPriorityPortD.setColumns(3);
		
		lblUniverse = new JLabel("Universe");
		
		lblDirection = new JLabel("Direction");
		
		lblMerge = new JLabel("Merge");
		
		lblPriority = new JLabel("Priority");
		
		comboBoxOutputStyleA = new JComboBox<String>(new String[] {"Delta", "Constant"});
		comboBoxOutputStyleA.setSelectedIndex(1);
		comboBoxOutputStyleA.setEnabled(false);
		comboBoxOutputStyleB = new JComboBox<String>(new String[] {"Delta", "Constant"});
		comboBoxOutputStyleB.setSelectedIndex(1);
		comboBoxOutputStyleB.setEnabled(false);		
		comboBoxOutputStyleC = new JComboBox<String>(new String[] {"Delta", "Constant"});
		comboBoxOutputStyleC.setSelectedIndex(1);
		comboBoxOutputStyleC.setEnabled(false);
		comboBoxOutputStyleD = new JComboBox<String>(new String[] {"Delta", "Constant"});
		comboBoxOutputStyleD.setSelectedIndex(1);
		comboBoxOutputStyleD.setEnabled(false);
		
		JLabel lblLabel = new JLabel("Label");
		
		textFieldLabelPortA = new JTextField();
		textFieldLabelPortA.setColumns(10);
		
		textFieldLabelPortB = new JTextField();
		textFieldLabelPortB.setColumns(10);
		
		textFieldLabelPortC = new JTextField();
		textFieldLabelPortC.setColumns(10);
		
		textFieldLabelPortD = new JTextField();
		textFieldLabelPortD.setColumns(10);
		
		JLabel lblFailSafe = new JLabel("Failsafe");
		
		comboBoxFailsafe = new JComboBox<String>();
		comboBoxFailsafe.setModel(new DefaultComboBoxModel<String>(new String[] {"Hold", "Zero", "Full"}));
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUniversePortD, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniversePortC, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniversePortB, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniversePortA, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUniverse)
								.addComponent(formattedTextFieldPortD, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPortC, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPortB, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPortA, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBoxDirectionPortD, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortC, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortB, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortA, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDirection))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblOutputStyle)
								.addComponent(comboBoxOutputStyleA, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxOutputStyleB, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxOutputStyleC, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxOutputStyleD, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblMerge)
								.addComponent(comboBoxMergePortA, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortB, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortC, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortD, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPriority)
								.addComponent(formattedTextFieldPriorityPortA, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPriorityPortB, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPriorityPortC, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPriorityPortD, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblLabel)
								.addComponent(textFieldLabelPortA, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldLabelPortB, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldLabelPortC, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldLabelPortD, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblFailSafe)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBoxFailsafe, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDirection)
						.addComponent(lblOutputStyle)
						.addComponent(lblMerge)
						.addComponent(lblLabel)
						.addComponent(lblPriority)
						.addComponent(lblUniverse))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(formattedTextFieldPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUniversePortA)
						.addComponent(comboBoxDirectionPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxOutputStyleA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxMergePortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedTextFieldPriorityPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldLabelPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(formattedTextFieldPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUniversePortB)
						.addComponent(comboBoxDirectionPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxOutputStyleB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxMergePortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedTextFieldPriorityPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldLabelPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUniversePortC)
						.addComponent(textFieldLabelPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedTextFieldPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedTextFieldPriorityPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxMergePortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxDirectionPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxOutputStyleC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUniversePortD)
						.addComponent(formattedTextFieldPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxDirectionPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxOutputStyleD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxMergePortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedTextFieldPriorityPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textFieldLabelPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFailSafe)
						.addComponent(comboBoxFailsafe, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(46, Short.MAX_VALUE))
		);
		gl_contentPanel.linkSize(SwingConstants.VERTICAL, new Component[] {lblUniversePortD, formattedTextFieldPortD, comboBoxMergePortD, comboBoxDirectionPortD, formattedTextFieldPriorityPortD, textFieldLabelPortD});
		gl_contentPanel.linkSize(SwingConstants.VERTICAL, new Component[] {lblUniversePortC, formattedTextFieldPortC, comboBoxMergePortC, comboBoxDirectionPortC, formattedTextFieldPriorityPortC, textFieldLabelPortC});
		gl_contentPanel.linkSize(SwingConstants.VERTICAL, new Component[] {lblUniversePortB, formattedTextFieldPortB, comboBoxMergePortB, comboBoxDirectionPortB, formattedTextFieldPriorityPortB, textFieldLabelPortB});
		gl_contentPanel.linkSize(SwingConstants.VERTICAL, new Component[] {lblUniversePortA, formattedTextFieldPortA, comboBoxMergePortA, comboBoxDirectionPortA, formattedTextFieldPriorityPortA, textFieldLabelPortA});
	
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
				
		comboBoxDirectionPortA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Boolean isInput = (comboBoxDirectionPortA.getSelectedIndex() == 1);
				comboBoxMergePortA.setEnabled(!isInput);
				formattedTextFieldPriorityPortA.setEnabled(isInput);
				comboBoxOutputStyleA.setEnabled(hasOutputStyleA && !isInput);
			}
		});
		
		comboBoxDirectionPortB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Boolean isInput = (comboBoxDirectionPortB.getSelectedIndex() == 1);
				comboBoxMergePortB.setEnabled(!isInput);
				formattedTextFieldPriorityPortB.setEnabled(isInput);
				comboBoxOutputStyleB.setEnabled(hasOutputStyleB && !isInput);
			}
		});
		
		comboBoxDirectionPortC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Boolean isInput = (comboBoxDirectionPortC.getSelectedIndex() == 1);
				comboBoxMergePortC.setEnabled(!isInput);
				formattedTextFieldPriorityPortC.setEnabled(isInput);
				comboBoxOutputStyleC.setEnabled(hasOutputStyleC && !isInput);
			}
		});
		
		comboBoxDirectionPortD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Boolean isInput = (comboBoxDirectionPortD.getSelectedIndex() == 1);
				comboBoxMergePortD.setEnabled(!isInput);
				formattedTextFieldPriorityPortD.setEnabled(isInput);
				comboBoxOutputStyleD.setEnabled(hasOutputStyleD && !isInput);
			}
		});
	}
	
	private void load() {
		if (opi != null) {
			txt = opi.getTxt(TXT_FILE);
			if (txt != null) {
				final String[] lines = txt.split("\n");
				for (int i = 0; i < lines.length; i++) {
					final String line = lines[i];
					
					if (line.contains("failsafe")) {
						final String value = Properties.getString(line);
						if (value.equals("off")) {
							comboBoxFailsafe.setSelectedIndex(1);
						} else if (value.equals("on")) {
							comboBoxFailsafe.setSelectedIndex(2);
						} else {
							comboBoxFailsafe.setSelectedIndex(0);
						}
						continue;
					}
					
					if (line.contains("label_port_a")) {
						textFieldLabelPortA.setText(Properties.getString(line));
						continue;
					}
					
					if (line.contains("label_port_b")) {
						textFieldLabelPortB.setText(Properties.getString(line));
						continue;
					}
					
					if (line.contains("label_port_c")) {
						textFieldLabelPortC.setText(Properties.getString(line));
						continue;
					}
					
					if (line.contains("label_port_d")) {
						textFieldLabelPortD.setText(Properties.getString(line));
						continue;
					}
					
					if (line.contains("direction_port_a")) {
						if (Properties.getString(line).equals("input")) {
							comboBoxDirectionPortA.setSelectedIndex(1);
						} else if (Properties.getString(line).equals("disable")) {
							comboBoxDirectionPortA.setSelectedIndex(2);
						} else {
							comboBoxDirectionPortA.setSelectedIndex(0);
						}
						continue;
					}
					if (line.contains("direction_port_b")) {
						if (Properties.getString(line).equals("input")) {
							comboBoxDirectionPortB.setSelectedIndex(1);
						} else if (Properties.getString(line).equals("disable")) {
							comboBoxDirectionPortB.setSelectedIndex(2);
						} else {
							comboBoxDirectionPortB.setSelectedIndex(0);
						}
						continue;
					}
					if (line.contains("direction_port_c")) {
						if (Properties.getString(line).equals("input")) {
							comboBoxDirectionPortC.setSelectedIndex(1);
						} else if (Properties.getString(line).equals("disable")) {
							comboBoxDirectionPortC.setSelectedIndex(2);
						} else {
							comboBoxDirectionPortC.setSelectedIndex(0);
						}
						continue;
					}
					if (line.contains("direction_port_d")) {
						if (Properties.getString(line).equals("input")) {
							comboBoxDirectionPortD.setSelectedIndex(1);
						} else if (Properties.getString(line).equals("disable")) {
							comboBoxDirectionPortD.setSelectedIndex(2);
						} else {
							comboBoxDirectionPortD.setSelectedIndex(0);
						}
						continue;
					}
					//
					if (line.contains("universe_port_a")) {
						formattedTextFieldPortA.setValue(Properties.getInt(line));
						hasUniverseA = true;
						continue;
					}
					if (line.contains("universe_port_b")) {
						formattedTextFieldPortB.setValue(Properties.getInt(line));
						hasUniverseB = true;
						continue;
					}
					if (line.contains("universe_port_c")) {
						formattedTextFieldPortC.setValue(Properties.getInt(line));
						hasUniverseC = true;
						continue;
					}
					if (line.contains("universe_port_d")) {
						formattedTextFieldPortD.setValue(Properties.getInt(line));
						hasUniverseD = true;
						continue;
					}
					//
					if (line.contains("merge_mode_port_a")) {
						comboBoxMergePortA.setSelectedIndex(Properties.getString(line).equals("ltp") ? 1 : 0);
						continue;
					}
					if (line.contains("merge_mode_port_b")) {
						comboBoxMergePortB.setSelectedIndex(Properties.getString(line).equals("ltp") ? 1 : 0);
						continue;
					}
					if (line.contains("merge_mode_port_c")) {
						comboBoxMergePortC.setSelectedIndex(Properties.getString(line).equals("ltp") ? 1 : 0);
						continue;
					}
					if (line.contains("merge_mode_port_d")) {
						comboBoxMergePortD.setSelectedIndex(Properties.getString(line).equals("ltp") ? 1 : 0);
						continue;
					}
					// Input
					if (line.contains("priority_port_a")) {
						formattedTextFieldPriorityPortA.setValue(Properties.getInt(line));
						continue;
					}
					if (line.contains("priority_port_b")) {
						formattedTextFieldPriorityPortB.setValue(Properties.getInt(line));
						continue;
					}
					if (line.contains("priority_port_c")) {
						formattedTextFieldPriorityPortC.setValue(Properties.getInt(line));
						continue;
					}
					if (line.contains("priority_port_d")) {
						formattedTextFieldPriorityPortD.setValue(Properties.getInt(line));
						continue;
					}
					//
					if (line.contains("output_style_a")) {
						comboBoxOutputStyleA.setSelectedIndex(Properties.getString(line).equals("constant") ? 1 : 0);
						comboBoxOutputStyleA.setEnabled(true);
						hasOutputStyleA = true;
						continue;
					}
					if (line.contains("output_style_b")) {
						comboBoxOutputStyleB.setSelectedIndex(Properties.getString(line).equals("constant") ? 1 : 0);
						comboBoxOutputStyleB.setEnabled(true);
						hasOutputStyleB = true;
						continue;
					}
					if (line.contains("output_style_c")) {
						comboBoxOutputStyleC.setSelectedIndex(Properties.getString(line).equals("constant") ? 1 : 0);
						comboBoxOutputStyleC.setEnabled(true);
						hasOutputStyleC = true;
						continue;
					}
					if (line.contains("output_style_d")) {
						comboBoxOutputStyleD.setSelectedIndex(Properties.getString(line).equals("constant") ? 1 : 0);
						comboBoxOutputStyleD.setEnabled(true);
						hasOutputStyleD = true;
						continue;
					}
				}
			}
			
			if (!hasUniverseA) {
				formattedTextFieldPortA.setEnabled(false);
				comboBoxDirectionPortA.setEnabled(false);
				comboBoxMergePortA.setEnabled(false);
				formattedTextFieldPriorityPortA.setEnabled(false);
				comboBoxOutputStyleA.setEnabled(false);
			}

			if (!hasUniverseB) {
				formattedTextFieldPortB.setEnabled(false);
				comboBoxDirectionPortB.setEnabled(false);
				comboBoxMergePortB.setEnabled(false);
				formattedTextFieldPriorityPortB.setEnabled(false);
				comboBoxOutputStyleB.setEnabled(false);
			}

			if (!hasUniverseC) {
				formattedTextFieldPortC.setEnabled(false);
				comboBoxDirectionPortC.setEnabled(false);
				comboBoxMergePortC.setEnabled(false);
				formattedTextFieldPriorityPortC.setEnabled(false);
				comboBoxOutputStyleC.setEnabled(false);
			}

			if (!hasUniverseD) {
				formattedTextFieldPortD.setEnabled(false);
				comboBoxDirectionPortD.setEnabled(false);
				comboBoxMergePortD.setEnabled(false);
				formattedTextFieldPriorityPortD.setEnabled(false);
				comboBoxOutputStyleD.setEnabled(false);
			}
		}
	}
	
	private String getFailsafe() {
		if (comboBoxFailsafe.getSelectedItem().toString().toLowerCase().equals("zero")) {
			return "off";
		}
		
		if (comboBoxFailsafe.getSelectedItem().toString().toLowerCase().equals("full")) {
			return "on";
		}
		
		return "hold";
	}
	
	private void save() {
		if (opi != null) {
			StringBuffer txtAppend = new StringBuffer("#" + TXT_FILE + "\n");
			
			txtAppend.append(String.format("failsafe=%s\n", getFailsafe()));

			if (hasUniverseA) {
				txtAppend.append(String.format("universe_port_a=%s\n", formattedTextFieldPortA.getValue()));	
				txtAppend.append(String.format("direction_port_a=%s\n", LightSet.getDirection(comboBoxDirectionPortA)));
				txtAppend.append(String.format("merge_mode_port_a=%s\n", LightSet.getMergeMode(comboBoxMergePortA)));
				txtAppend.append(String.format("priority_port_a=%s\n", formattedTextFieldPriorityPortA.getValue()));
				if (hasOutputStyleA) {
					txtAppend.append(String.format("output_style_a=%s\n", LightSet.getOutputStyle(comboBoxOutputStyleA)));
				}
				txtAppend.append(String.format("label_port_a=%.17s\n", textFieldLabelPortA.getText()));
			}

			if (hasUniverseB) {
				txtAppend.append(String.format("universe_port_b=%s\n", formattedTextFieldPortB.getValue()));
				txtAppend.append(String.format("direction_port_b=%s\n", LightSet.getDirection(comboBoxDirectionPortB)));
				txtAppend.append(String.format("merge_mode_port_b=%s\n", LightSet.getMergeMode(comboBoxMergePortB)));
				txtAppend.append(String.format("priority_port_b=%s\n", formattedTextFieldPriorityPortB.getValue()));
				if (hasOutputStyleB) {
					txtAppend.append(String.format("output_style_b=%s\n", LightSet.getOutputStyle(comboBoxOutputStyleB)));
				}
				txtAppend.append(String.format("label_port_b=%.17s\n", textFieldLabelPortB.getText()));
			}

			if (hasUniverseC) {
				txtAppend.append(String.format("universe_port_c=%s\n", formattedTextFieldPortC.getValue()));
				txtAppend.append(String.format("direction_port_c=%s\n", LightSet.getDirection(comboBoxDirectionPortC)));
				txtAppend.append(String.format("merge_mode_port_c=%s\n", LightSet.getMergeMode(comboBoxMergePortC)));
				txtAppend.append(String.format("priority_port_c=%s\n", formattedTextFieldPriorityPortC.getValue()));
				if (hasOutputStyleC) {
					txtAppend.append(String.format("output_style_c=%s\n", LightSet.getOutputStyle(comboBoxOutputStyleC)));
				}
				txtAppend.append(String.format("label_port_c=%.17s\n", textFieldLabelPortC.getText()));
			}

			if (hasUniverseD) {
				txtAppend.append(String.format("universe_port_d=%s\n", formattedTextFieldPortD.getValue()));
				txtAppend.append(String.format("direction_port_d=%s\n", LightSet.getDirection(comboBoxDirectionPortD)));
				txtAppend.append(String.format("merge_mode_port_d=%s\n", LightSet.getMergeMode(comboBoxMergePortD)));
				txtAppend.append(String.format("priority_port_d=%s\n", formattedTextFieldPriorityPortD.getValue()));
				if (hasOutputStyleD) {
					txtAppend.append(String.format("output_style_d=%s\n", LightSet.getOutputStyle(comboBoxOutputStyleD)));
				}
				txtAppend.append(String.format("label_port_d=%.17s\n", textFieldLabelPortD.getText()));
			}
											
			try {
				opi.doSave(txtAppend.toString());
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
