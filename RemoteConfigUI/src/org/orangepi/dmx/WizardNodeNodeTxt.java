/* Copyright (C) 2022 by Arjan van Vught mailto:info@orangepi-dmx.nl
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

public class WizardNodeNodeTxt extends JDialog {
	private static final String TXT_FILE = "node.txt";
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
	private JComboBox<String> comboBoxPersonality;
	
	public static void main(String[] args) {
		try {
			WizardNodeNodeTxt dialog = new WizardNodeNodeTxt();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public WizardNodeNodeTxt() {
		setTitle("Node");
		initComponents();
		createEvents();

	}
	
	public WizardNodeNodeTxt(String nodeId, OrangePi opi, RemoteConfig remoteConfig) {
		this.nodeId = nodeId;
		this.opi = opi;
		this.remoteConfig = remoteConfig;

		setTitle(nodeId);

		initComponents();
		createEvents();

		load();
	}
	
	private void initComponents() {
		setBounds(100, 100, 355, 282);
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
		formattedTextFieldPortA.setText("1");
		formattedTextFieldPortA.setColumns(6);
		formattedTextFieldPortB = new JFormattedTextField(formatterUniverse);
		formattedTextFieldPortB.setText("2");
		formattedTextFieldPortB.setColumns(6);
		formattedTextFieldPortC = new JFormattedTextField(formatterUniverse);
		formattedTextFieldPortC.setText("3");
		formattedTextFieldPortC.setColumns(6);
		formattedTextFieldPortD = new JFormattedTextField(formatterUniverse);
		formattedTextFieldPortD.setText("4");
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
		
		NumberFormatter formatterPriority = new NumberFormatter(format);
		formatterPriority.setValueClass(Integer.class);
		formatterPriority.setMinimum(1);
		formatterPriority.setMaximum(200);
		formatterPriority.setAllowsInvalid(false);
		formatterPriority.setCommitsOnValidEdit(true);
		
		JLabel lblUniverse = new JLabel("Universe");
		JLabel lblMerge = new JLabel("Merge");
		JLabel lblDirection = new JLabel("Direction");
		JLabel lblPersonality = new JLabel("Personality");
		
		comboBoxPersonality = new JComboBox<String>();
		comboBoxPersonality.setModel(new DefaultComboBoxModel<String>(new String[] {"Art-Net 4", "sACN E1.31"}));
						
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(6)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUniversePortA, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniversePortB, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniversePortC, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniversePortD, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(formattedTextFieldPortD, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPortC, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPortB, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldPortA, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniverse))
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDirection)
								.addComponent(comboBoxDirectionPortC, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortD, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortB, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortA, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblMerge)
								.addComponent(comboBoxMergePortB, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortA, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortC, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortD, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblPersonality)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBoxPersonality, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(15, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap(10, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPersonality)
						.addComponent(comboBoxPersonality, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUniverse)
						.addComponent(lblMerge)
						.addComponent(lblDirection))
					.addGap(8)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(34)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblUniversePortB)
								.addComponent(formattedTextFieldPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(formattedTextFieldPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblUniversePortA)
							.addComponent(comboBoxDirectionPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(comboBoxMergePortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(7)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(formattedTextFieldPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUniversePortC)
						.addComponent(comboBoxDirectionPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxMergePortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(7)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(formattedTextFieldPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUniversePortD)
						.addComponent(comboBoxDirectionPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxMergePortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(11))
		);
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
			}
		});
		
		comboBoxDirectionPortB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Boolean isSelected = (comboBoxDirectionPortB.getSelectedIndex() == 1);
				comboBoxMergePortB.setEnabled(!isSelected);
			}
		});
		
		comboBoxDirectionPortC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Boolean isSelected = (comboBoxDirectionPortC.getSelectedIndex() == 1);
				comboBoxMergePortC.setEnabled(!isSelected);
			}
		});
		
		comboBoxDirectionPortD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Boolean isSelected = (comboBoxDirectionPortD.getSelectedIndex() == 1);
				comboBoxMergePortD.setEnabled(!isSelected);
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
				}
			}
		}
	}
	
	private String getPersonality(JComboBox<String> comboBox) {
		System.out.println(comboBox.getSelectedItem().toString().toLowerCase());
		if (comboBox.getSelectedItem().toString().toLowerCase().equals("sacn e1.31")) {
			return "sacn";
		}
		return "artnet";
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
			StringBuffer txt = new StringBuffer(opi.getTxt(TXT_FILE));
			txt.append("\n");
			
			txt.append(String.format("personality=%s\n", getPersonality(comboBoxPersonality)));
			
			txt.append(String.format("universe_port_a=%s\n", formattedTextFieldPortA.getValue()));
			txt.append(String.format("universe_port_b=%s\n", formattedTextFieldPortB.getValue()));
			txt.append(String.format("universe_port_c=%s\n", formattedTextFieldPortC.getValue()));
			txt.append(String.format("universe_port_d=%s\n", formattedTextFieldPortD.getValue()));
		
			txt.append(String.format("direction_port_a=%s\n", getDirection(comboBoxDirectionPortA)));
			txt.append(String.format("direction_port_b=%s\n", getDirection(comboBoxDirectionPortB)));
			txt.append(String.format("direction_port_c=%s\n", getDirection(comboBoxDirectionPortC)));
			txt.append(String.format("direction_port_d=%s\n", getDirection(comboBoxDirectionPortD)));
											
			txt.append(String.format("merge_mode_port_a=%s\n", getMergeMode(comboBoxMergePortA)));
			txt.append(String.format("merge_mode_port_b=%s\n", getMergeMode(comboBoxMergePortB)));
			txt.append(String.format("merge_mode_port_c=%s\n", getMergeMode(comboBoxMergePortC)));
			txt.append(String.format("merge_mode_port_d=%s\n", getMergeMode(comboBoxMergePortD)));
								
			try {
				opi.doSave(txt.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (remoteConfig != null) {
				remoteConfig.setTextArea(opi.getTxt(TXT_FILE));
			}
		}
	}
}
