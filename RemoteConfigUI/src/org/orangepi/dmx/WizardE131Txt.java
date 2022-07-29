/* Copyright (C) 2021-2022 by Arjan van Vught mailto:info@orangepi-dmx.nl
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
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import javax.swing.SwingConstants;
import java.awt.Component;

public class WizardE131Txt extends JDialog {
	private static final String TXT_FILE = "e131.txt";
	//
	private static final long serialVersionUID = 1L;
	String nodeId = null;
	OrangePi opi = null;
	RemoteConfig remoteConfig = null;

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
	
	private JFormattedTextField formattedTextFieldPriorityPortA;
	private JFormattedTextField formattedTextFieldPriorityPortB;
	private JFormattedTextField formattedTextFieldPriorityPortC;
	private JFormattedTextField formattedTextFieldPriorityPortD;
	private JLabel lblUniverse;
	private JLabel lblDirection;
	private JLabel lblMerge;
	//

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
		setBounds(100, 100, 403, 230);
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
		
		JLabel lblPriority = new JLabel("Priority");
		
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

		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		
		gl_contentPanel.setHorizontalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
						.addGap(6)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUniversePortA, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniversePortB, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniversePortC, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniversePortD, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(formattedTextFieldPortA, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPortB, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPortC, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPortD, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniverse))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBoxDirectionPortA, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortB, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortC, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortD, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDirection))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBoxMergePortA, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortB, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortC, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortD, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblMerge))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(formattedTextFieldPriorityPortA, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPriorityPortB, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPriorityPortC, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPriorityPortD, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPriority))
						.addContainerGap(17, Short.MAX_VALUE))
				);
		
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblDirection)
										.addComponent(lblMerge)
										.addComponent(lblUniverse))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(formattedTextFieldPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(lblPriority)
									.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_contentPanel.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.RELATED)
											.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
												.addComponent(comboBoxDirectionPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(comboBoxMergePortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
										.addGroup(gl_contentPanel.createSequentialGroup()
											.addGap(6)
											.addComponent(formattedTextFieldPriorityPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
							.addPreferredGap(ComponentPlacement.RELATED))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblUniversePortA)
							.addGap(12)))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(comboBoxDirectionPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(comboBoxMergePortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(5)
							.addComponent(lblUniversePortB))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(formattedTextFieldPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(formattedTextFieldPriorityPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboBoxMergePortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(11)
							.addComponent(lblUniversePortC))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(6)
							.addComponent(formattedTextFieldPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(6)
							.addComponent(formattedTextFieldPriorityPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboBoxMergePortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(11)
							.addComponent(lblUniversePortD))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(6)
							.addComponent(formattedTextFieldPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(6)
							.addComponent(formattedTextFieldPriorityPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(75))
		);
		gl_contentPanel.linkSize(SwingConstants.VERTICAL, new Component[] {formattedTextFieldPriorityPortA, formattedTextFieldPriorityPortB, formattedTextFieldPriorityPortC, formattedTextFieldPriorityPortD});
		gl_contentPanel.linkSize(SwingConstants.VERTICAL, new Component[] {comboBoxMergePortA, comboBoxMergePortB, comboBoxMergePortC, comboBoxMergePortD});
		gl_contentPanel.linkSize(SwingConstants.VERTICAL, new Component[] {comboBoxDirectionPortA, comboBoxDirectionPortB, comboBoxDirectionPortC, comboBoxDirectionPortD});
		gl_contentPanel.linkSize(SwingConstants.VERTICAL, new Component[] {formattedTextFieldPortA, formattedTextFieldPortB, formattedTextFieldPortC, formattedTextFieldPortD});
		gl_contentPanel.linkSize(SwingConstants.HORIZONTAL, new Component[] {formattedTextFieldPortA, formattedTextFieldPortB, formattedTextFieldPortC, formattedTextFieldPortD});
		gl_contentPanel.linkSize(SwingConstants.HORIZONTAL, new Component[] {lblUniversePortA, lblUniversePortB, lblUniversePortC, lblUniversePortD});

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
				final Boolean isSelected = (comboBoxDirectionPortA.getSelectedIndex() == 1);
				comboBoxMergePortA.setEnabled(!isSelected);
				formattedTextFieldPriorityPortA.setEditable(isSelected);
				formattedTextFieldPriorityPortA.setEnabled(isSelected);
			}
		});
		
		comboBoxDirectionPortB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Boolean isSelected = (comboBoxDirectionPortB.getSelectedIndex() == 1);
				comboBoxMergePortB.setEnabled(!isSelected);
				formattedTextFieldPriorityPortB.setEditable(isSelected);
				formattedTextFieldPriorityPortB.setEnabled(isSelected);
			}
		});
		
		comboBoxDirectionPortC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Boolean isSelected = (comboBoxDirectionPortC.getSelectedIndex() == 1);
				comboBoxMergePortC.setEnabled(!isSelected);
				formattedTextFieldPriorityPortC.setEditable(isSelected);
				formattedTextFieldPriorityPortC.setEnabled(isSelected);
			}
		});
		
		comboBoxDirectionPortD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Boolean isSelected = (comboBoxDirectionPortD.getSelectedIndex() == 1);
				comboBoxMergePortD.setEnabled(!isSelected);
				formattedTextFieldPriorityPortD.setEditable(isSelected);
				formattedTextFieldPriorityPortD.setEnabled(isSelected);
			}
		});
	}
	
	private void load() {
		if (opi != null) {
			final String txt = opi.getTxt(TXT_FILE);
			if (txt != null) {
				final String[] lines = txt.split("\n");
				for (int i = 0; i < lines.length; i++) {
					final String line = lines[i];
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
						continue;
					}
					if (line.contains("universe_port_b")) {
						formattedTextFieldPortB.setValue(Properties.getInt(line));
						continue;
					}
					if (line.contains("universe_port_c")) {
						formattedTextFieldPortC.setValue(Properties.getInt(line));
						continue;
					}
					if (line.contains("universe_port_d")) {
						formattedTextFieldPortD.setValue(Properties.getInt(line));
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
				}
			}
		}
	}
		
	private String getDirection(JComboBox<String> comboBox) {
		if (comboBox.getSelectedItem().toString().toLowerCase().equals("input")) {
			return "input";
		}
		
		if (comboBox.getSelectedItem().toString().toLowerCase().equals("disable")) {
			return "disable";
		}
		
		return "output";
	}
	
	private String getMergeMode(JComboBox<String> comboBox) {
		if (comboBox.getSelectedItem().toString().toLowerCase().equals("ltp")) {
			return "ltp";
		}
		return "htp";
	}
	
	private void save() {
		if (opi != null) {
			StringBuffer txtAppend = new StringBuffer();
			
			txtAppend.append(String.format("direction_port_a=%s\n", getDirection(comboBoxDirectionPortA)));
			txtAppend.append(String.format("direction_port_b=%s\n", getDirection(comboBoxDirectionPortB)));
			txtAppend.append(String.format("direction_port_c=%s\n", getDirection(comboBoxDirectionPortC)));
			txtAppend.append(String.format("direction_port_d=%s\n", getDirection(comboBoxDirectionPortD)));
									
			txtAppend.append(String.format("universe_port_a=%s\n", formattedTextFieldPortA.getValue()));
			txtAppend.append(String.format("universe_port_b=%s\n", formattedTextFieldPortB.getValue()));
			txtAppend.append(String.format("universe_port_c=%s\n", formattedTextFieldPortC.getValue()));
			txtAppend.append(String.format("universe_port_d=%s\n", formattedTextFieldPortD.getValue()));
			
			txtAppend.append(String.format("merge_mode_port_a=%s\n", getMergeMode(comboBoxMergePortA)));
			txtAppend.append(String.format("merge_mode_port_b=%s\n", getMergeMode(comboBoxMergePortB)));
			txtAppend.append(String.format("merge_mode_port_c=%s\n", getMergeMode(comboBoxMergePortC)));
			txtAppend.append(String.format("merge_mode_port_d=%s\n", getMergeMode(comboBoxMergePortD)));
			
			txtAppend.append(String.format("priority_port_a=%s\n", formattedTextFieldPriorityPortA.getValue()));
			txtAppend.append(String.format("priority_port_b=%s\n", formattedTextFieldPriorityPortB.getValue()));
			txtAppend.append(String.format("priority_port_c=%s\n", formattedTextFieldPriorityPortC.getValue()));
			txtAppend.append(String.format("priority_port_d=%s\n", formattedTextFieldPriorityPortD.getValue()));
			
			txtAppend.append(String.format("priority=%s\n", formattedTextFieldPriorityPortA.getValue()));
					
			String txt = Properties.removeComments(opi.getTxt(TXT_FILE));
			txt = txt.replaceAll("direction", "#direction");
			txt = txt.replaceAll("universe_port_", "#universe_port_");
					
			try {
				opi.doSave(txt + "\n" + txtAppend.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (remoteConfig != null) {
				remoteConfig.setTextArea(opi.getTxt(TXT_FILE));
			}
		}
	}
}
