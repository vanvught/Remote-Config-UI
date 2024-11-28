/* Copyright (C) 2022 by Arjan van Vught mailto:info@gd32-dmx.org
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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

public class WizardNodeArtnetTxt extends JDialog {
	private static final long serialVersionUID = 1L;

	private static final String TXT_FILE = "artnet.txt";
	private static final int SHORT_NAME_LENGTH = 18;
	private static final int LONG_NAME_LENGTH = 64;

	String nodeId = null;
	OrangePi opi = null;
	RemoteConfig remoteConfig = null;
	String txt = null;

	private final JPanel contentPanel = new JPanel();
	private JButton btnCancel;
	private JButton btnSetDefaults;
	private JButton btnSave;
	
	private JComboBox<String>  comboBoxProtocolPortA;
	private JComboBox<String>  comboBoxProtocolPortB;
	private JComboBox<String>  comboBoxProtocolPortC;
	private JComboBox<String>  comboBoxProtocolPortD;
	
	private JCheckBox chckbxRdmEnablePortD;
	private JCheckBox chckbxRdmEnablePortC;
	private JCheckBox chckbxRdmEnablePortB;
	private JCheckBox chckbxRdmEnablePortA;
	
	private JCheckBox chckbxMapUniverse0;
		
	private JFormattedTextField formattedIP1PortA;
	private JFormattedTextField formattedIP2PortA;
	private JFormattedTextField formattedIP3PortA;
	private JFormattedTextField formattedIP4PortA;
	
	private JFormattedTextField formattedIP1PortB;
	private JFormattedTextField formattedIP2PortB;
	private JFormattedTextField formattedIP3PortB;
	private JFormattedTextField formattedIP4PortB;
	
	private JFormattedTextField formattedIP1PortC;
	private JFormattedTextField formattedIP2PortC;
	private JFormattedTextField formattedIP3PortC;
	private JFormattedTextField formattedIP4PortC;
	
	private JFormattedTextField formattedIP1PortD;
	private JFormattedTextField formattedIP2PortD;
	private JFormattedTextField formattedIP3PortD;
	private JFormattedTextField formattedIP4PortD;
	
	private JLabel lblUniversePortA;
	private JLabel lblUniversePortB;
	private JLabel lblUniversePortC;
	private JLabel lblUniversePortD;
	
	private JCheckBox chckbxEnableRDM;
	private JLabel lblShortName;
	private JLabel lblLongName;
	private JTextField textFieldShortName;
	private JTextField textFieldLongName;
	
	public static void main(String[] args) {
		try {
			WizardNodeArtnetTxt dialog = new WizardNodeArtnetTxt();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public WizardNodeArtnetTxt() {
		setTitle("Art-Net 4");
		initComponents();
		createEvents();

	}
	
	public WizardNodeArtnetTxt(String nodeId, OrangePi opi, RemoteConfig remoteConfig) {
		this.nodeId = nodeId;
		this.opi = opi;
		this.remoteConfig = remoteConfig;

		setTitle(nodeId);

		initComponents();
		createEvents();

		load();
	}
	
	private void initComponents() {
		setBounds(100, 100, 636, 356);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.WEST);
		
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatterUniverse = new NumberFormatter(format);
		formatterUniverse.setValueClass(Integer.class);
		formatterUniverse.setMinimum(0);
		formatterUniverse.setMaximum(32768);
		formatterUniverse.setAllowsInvalid(false);
		formatterUniverse.setCommitsOnValidEdit(true);
		
		lblUniversePortA = new JLabel("Port A");
		lblUniversePortB = new JLabel("Port B");
		lblUniversePortC = new JLabel("Port C");
		lblUniversePortD = new JLabel("Port D");
								
		comboBoxProtocolPortA = new JComboBox<String> ();
		comboBoxProtocolPortA.setModel(new DefaultComboBoxModel<String> (new String[] {"Art-Net", "sACN"}));
		comboBoxProtocolPortB = new JComboBox<String> ();
		comboBoxProtocolPortB.setModel(new DefaultComboBoxModel<String> (new String[] {"Art-Net", "sACN"}));
		comboBoxProtocolPortC = new JComboBox<String> ();
		comboBoxProtocolPortC.setModel(new DefaultComboBoxModel<String> (new String[] {"Art-Net", "sACN"}));		
		comboBoxProtocolPortD = new JComboBox<String> ();
		comboBoxProtocolPortD.setModel(new DefaultComboBoxModel<String> (new String[] {"Art-Net", "sACN"}));
				
		chckbxRdmEnablePortA = new JCheckBox("RDM");
		chckbxRdmEnablePortB = new JCheckBox("RDM");
		chckbxRdmEnablePortC = new JCheckBox("RDM");
		chckbxRdmEnablePortD = new JCheckBox("RDM");
		
		chckbxMapUniverse0 = new JCheckBox("Art-Net 4 Map Universe 0");
		
		NumberFormatter formatterIP = new NumberFormatter(format);
		formatterIP.setValueClass(Integer.class);
		formatterIP.setMinimum(0);
		formatterIP.setMaximum(255);
		formatterIP.setAllowsInvalid(false);
		formatterIP.setCommitsOnValidEdit(true);
		
		formattedIP1PortA = new JFormattedTextField(formatterIP);
		formattedIP1PortA.setColumns(2);
		formattedIP2PortA = new JFormattedTextField(formatterIP);
		formattedIP2PortA.setColumns(2);
		formattedIP3PortA = new JFormattedTextField(formatterIP);
		formattedIP3PortA.setColumns(2);
		formattedIP4PortA = new JFormattedTextField(formatterIP);
		formattedIP4PortA.setColumns(2);
		
		formattedIP1PortB = new JFormattedTextField(formatterIP);
		formattedIP1PortB.setColumns(2);
		formattedIP2PortB = new JFormattedTextField(formatterIP);
		formattedIP2PortB.setColumns(2);
		formattedIP3PortB = new JFormattedTextField(formatterIP);
		formattedIP3PortB.setColumns(2);
		formattedIP4PortB = new JFormattedTextField(formatterIP);
		formattedIP4PortB.setColumns(2);
		
		formattedIP1PortC = new JFormattedTextField(formatterIP);
		formattedIP1PortC.setColumns(2);		
		formattedIP2PortC = new JFormattedTextField(formatterIP);
		formattedIP2PortC.setColumns(2);
		formattedIP3PortC = new JFormattedTextField(formatterIP);
		formattedIP3PortC.setColumns(2);
		formattedIP4PortC = new JFormattedTextField(formatterIP);
		formattedIP4PortC.setColumns(2);
		
		formattedIP1PortD = new JFormattedTextField(formatterIP);
		formattedIP1PortD.setColumns(2);
		formattedIP2PortD = new JFormattedTextField(formatterIP);
		formattedIP2PortD.setColumns(2);
		formattedIP3PortD = new JFormattedTextField(formatterIP);
		formattedIP3PortD.setColumns(2);
		formattedIP4PortD = new JFormattedTextField(formatterIP);
		formattedIP4PortD.setColumns(2);
				
		JLabel lblProtocol = new JLabel("Protocol");
		lblProtocol.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		JLabel lblDestinationIP = new JLabel("Destination IP Address");
		lblDestinationIP.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		chckbxEnableRDM = new JCheckBox("Enable RDM");
		chckbxEnableRDM.setToolTipText("Globally");
		
		lblShortName = new JLabel("Short name");
		
		lblLongName = new JLabel("Long name");
		
		textFieldShortName = new JTextField();
		textFieldShortName.setColumns(10);
		
		textFieldLongName = new JTextField();
		textFieldLongName.setColumns(10);
												
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		
		gl_contentPanel.setHorizontalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPanel.createSequentialGroup()
										.addContainerGap()
										.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(lblLongName)
												.addComponent(lblShortName))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(textFieldShortName, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
												.addComponent(textFieldLongName, GroupLayout.PREFERRED_SIZE, 531, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_contentPanel.createSequentialGroup()
										.addGap(171)
										.addComponent(lblDestinationIP, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPanel.createSequentialGroup()
										.addContainerGap()
										.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(lblUniversePortA, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblUniversePortB, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblUniversePortC, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblUniversePortD, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(comboBoxProtocolPortA, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
												.addComponent(comboBoxProtocolPortB, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
												.addComponent(comboBoxProtocolPortC, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
												.addComponent(comboBoxProtocolPortD, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(formattedIP1PortA, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addComponent(formattedIP1PortB, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addComponent(formattedIP1PortC, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addComponent(formattedIP1PortD, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(formattedIP2PortA, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addComponent(formattedIP2PortB, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addComponent(formattedIP2PortC, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addComponent(formattedIP2PortD, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(formattedIP3PortA, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addComponent(formattedIP3PortB, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addComponent(formattedIP3PortC, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addComponent(formattedIP3PortD, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(formattedIP4PortA, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addComponent(formattedIP4PortB, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addComponent(formattedIP4PortC, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addComponent(formattedIP4PortD, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(chckbxRdmEnablePortA, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
												.addComponent(chckbxRdmEnablePortB, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
												.addComponent(chckbxRdmEnablePortC, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
												.addComponent(chckbxRdmEnablePortD, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_contentPanel.createSequentialGroup()
										.addGap(60)
										.addComponent(lblProtocol, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE))
								.addComponent(chckbxMapUniverse0)
								.addComponent(chckbxEnableRDM))
						.addContainerGap(12, Short.MAX_VALUE))
				);
		
		gl_contentPanel.setVerticalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
						.addContainerGap(7, Short.MAX_VALUE)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblProtocol, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDestinationIP, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblUniversePortA)
								.addComponent(comboBoxProtocolPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP1PortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP2PortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP3PortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP4PortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxRdmEnablePortA))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblUniversePortB)
								.addComponent(comboBoxProtocolPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP1PortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP2PortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP3PortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP4PortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxRdmEnablePortB))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblUniversePortC)
								.addComponent(comboBoxProtocolPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP1PortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP2PortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP3PortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP4PortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxRdmEnablePortC))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblUniversePortD)
								.addComponent(comboBoxProtocolPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP1PortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP2PortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP3PortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP4PortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxRdmEnablePortD))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(chckbxEnableRDM)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(chckbxMapUniverse0)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblShortName)
								.addComponent(textFieldShortName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblLongName)
								.addComponent(textFieldLongName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
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
			if (txt != null) {
				final String[] lines = txt.split("\n");
				for (int i = 0; i < lines.length; i++) {
					final String line = lines[i];
					//
					if (line.contains("short_name")) {
						textFieldShortName.setText(Properties.getString(line));
						continue;
					}
					if (line.contains("long_name")) {
						textFieldLongName.setText(Properties.getString(line));
						continue;
					}
					// Output
					if (line.contains("enable_rdm")) {
						chckbxEnableRDM.setSelected(Properties.getBool(line));
						continue;
					}
					//
					if (line.contains("protocol_port_a")) {
						comboBoxProtocolPortA.setSelectedIndex(Properties.getString(line).equals("sacn") ? 1 : 0);
						continue;
					}
					if (line.contains("protocol_port_b")) {
						comboBoxProtocolPortB.setSelectedIndex(Properties.getString(line).equals("sacn") ? 1 : 0);
						continue;
					}
					if (line.contains("protocol_port_c")) {
						comboBoxProtocolPortC.setSelectedIndex(Properties.getString(line).equals("sacn") ? 1 : 0);
						continue;
					}
					if (line.contains("protocol_port_d")) {
						comboBoxProtocolPortD.setSelectedIndex(Properties.getString(line).equals("sacn") ? 1 : 0);
						continue;
					}
					//
					if (line.contains("rdm_enable_port_a")) {
						chckbxRdmEnablePortA.setSelected(Properties.getBool(line));
						continue;
					}
					if (line.contains("rdm_enable_port_b")) {
						chckbxRdmEnablePortB.setSelected(Properties.getBool(line));
						continue;	
					}
					if (line.contains("rdm_enable_port_c")) {
						chckbxRdmEnablePortC.setSelected(Properties.getBool(line));
						continue;	
					}
					if (line.contains("rdm_enable_port_d")) {
						chckbxRdmEnablePortD.setSelected(Properties.getBool(line));
						continue;
					}
					// Input
					if (line.contains("destination_ip_port_a")) {
						final String value = Properties.getString(line).replace('.', '\n');
						final String parts[] = value.split("\n");
						if (parts.length == 4) {
							formattedIP1PortA.setValue(Integer.parseInt(parts[0]));
							formattedIP2PortA.setValue(Integer.parseInt(parts[1]));
							formattedIP3PortA.setValue(Integer.parseInt(parts[2]));
							formattedIP4PortA.setValue(Integer.parseInt(parts[3]));
						}
					}
					if (line.contains("destination_ip_port_b")) {
						final String value = Properties.getString(line).replace('.', '\n');
						final String parts[] = value.split("\n");
						if (parts.length == 4) {
							formattedIP1PortB.setValue(Integer.parseInt(parts[0]));
							formattedIP2PortB.setValue(Integer.parseInt(parts[1]));
							formattedIP3PortB.setValue(Integer.parseInt(parts[2]));
							formattedIP4PortB.setValue(Integer.parseInt(parts[3]));
						}
					}
					if (line.contains("destination_ip_port_c")) {
						final String value = Properties.getString(line).replace('.', '\n');
						final String parts[] = value.split("\n");
						if (parts.length == 4) {
							formattedIP1PortC.setValue(Integer.parseInt(parts[0]));
							formattedIP2PortC.setValue(Integer.parseInt(parts[1]));
							formattedIP3PortC.setValue(Integer.parseInt(parts[2]));
							formattedIP4PortC.setValue(Integer.parseInt(parts[3]));
						}
					}
					if (line.contains("destination_ip_port_d")) {
						final String value = Properties.getString(line).replace('.', '\n');
						final String parts[] = value.split("\n");
						if (parts.length == 4) {
							formattedIP1PortD.setValue(Integer.parseInt(parts[0]));
							formattedIP2PortD.setValue(Integer.parseInt(parts[1]));
							formattedIP3PortD.setValue(Integer.parseInt(parts[2]));
							formattedIP4PortD.setValue(Integer.parseInt(parts[3]));
						}
					}
					//
					if (line.contains("map_universe0")) {
						chckbxMapUniverse0.setSelected(Properties.getBool(line));
						continue;
					}
				}
			}
		}
	}
					
	private String getProtocol(JComboBox<String> comboBox) {
		if (comboBox.getSelectedItem().toString().toLowerCase().equals("sacn")) {
			return "sacn";
		}
		return "artnet";
	}
		
	private void save() {
		if (opi != null) {
			StringBuffer txtFile = new StringBuffer("#" + TXT_FILE + "\n");
			//
			final String shortName = textFieldShortName.getText();	
			if (shortName.length() > SHORT_NAME_LENGTH) {
				txtFile.append(String.format("short_name=%s\n", shortName.substring(0, SHORT_NAME_LENGTH - 1)));
				textFieldShortName.setText(shortName.substring(SHORT_NAME_LENGTH - 1));
			} else {
				txtFile.append(String.format("short_name=%s\n", shortName));
			}
			
			final String longName = textFieldLongName.getText();	
			if (longName.length() > LONG_NAME_LENGTH) {
				txtFile.append(String.format("long_name=%s\n", longName.substring(0, LONG_NAME_LENGTH - 1)));
				textFieldShortName.setText(longName.substring(LONG_NAME_LENGTH - 1));
			} else {
				txtFile.append(String.format("long_name=%s\n", longName));
			}
			// Output
			txtFile.append(String.format("enable_rdm=%d\n", chckbxEnableRDM.isSelected() ? 1 : 0));
			
			txtFile.append(String.format("protocol_port_a=%s\n", getProtocol(comboBoxProtocolPortA)));
			txtFile.append(String.format("protocol_port_b=%s\n", getProtocol(comboBoxProtocolPortB)));
			txtFile.append(String.format("protocol_port_c=%s\n", getProtocol(comboBoxProtocolPortC)));
			txtFile.append(String.format("protocol_port_d=%s\n", getProtocol(comboBoxProtocolPortD)));

			txtFile.append(String.format("rdm_enable_port_a=%s\n", chckbxRdmEnablePortA.isSelected() ? 1 : 0));
			txtFile.append(String.format("rdm_enable_port_b=%s\n", chckbxRdmEnablePortB.isSelected() ? 1 : 0));
			txtFile.append(String.format("rdm_enable_port_c=%s\n", chckbxRdmEnablePortC.isSelected() ? 1 : 0));
			txtFile.append(String.format("rdm_enable_port_d=%s\n", chckbxRdmEnablePortD.isSelected() ? 1 : 0));	
			// Input
			txtFile.append(String.format("destination_ip_port_a=%d.%d.%d.%d\n", formattedIP1PortA.getValue(),formattedIP2PortA.getValue(),formattedIP3PortA.getValue(),formattedIP4PortA.getValue()));
			txtFile.append(String.format("destination_ip_port_b=%d.%d.%d.%d\n", formattedIP1PortB.getValue(),formattedIP2PortB.getValue(),formattedIP3PortB.getValue(),formattedIP4PortB.getValue()));
			txtFile.append(String.format("destination_ip_port_c=%d.%d.%d.%d\n", formattedIP1PortC.getValue(),formattedIP2PortC.getValue(),formattedIP3PortC.getValue(),formattedIP4PortC.getValue()));
			txtFile.append(String.format("destination_ip_port_d=%d.%d.%d.%d\n", formattedIP1PortD.getValue(),formattedIP2PortD.getValue(),formattedIP3PortD.getValue(),formattedIP4PortD.getValue()));		
			// Art-Net 4
			txtFile.append(String.format("map_universe0=%d\n", chckbxMapUniverse0.isSelected() ? 1 : 0));
						
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
