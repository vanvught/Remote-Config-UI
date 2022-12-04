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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

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
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import javax.swing.JTextField;

public class WizardArtnetTxt extends JDialog {
	private static final long serialVersionUID = 1L;
	//
	private static final String TXT_FILE = "artnet.txt";
	private static final int SHORT_NAME_LENGTH = 18;
	private static final int LONG_NAME_LENGTH = 64;
	//
	String nodeId = null;
	OrangePi opi = null;
	RemoteConfig remoteConfig = null;

	private final JPanel contentPanel = new JPanel();
	private JButton btnCancel;
	private JButton btnSetDefaults;
	private JButton btnSave;
	private JFormattedTextField formattedTextFieldUniverseA;
	private JFormattedTextField formattedTextFieldUniverseB;
	private JFormattedTextField formattedTextFieldUniverseC;
	private JFormattedTextField formattedTextFieldUniverseD;
	private JComboBox<String> comboBoxDirectionPortA;
	private JComboBox<String> comboBoxDirectionPortB;
	private JComboBox<String> comboBoxDirectionPortC;
	private JComboBox<String> comboBoxDirectionPortD;
	private JComboBox<String> comboBoxMergePortA;
	private JComboBox<String> comboBoxMergePortB;
	private JComboBox<String> comboBoxMergePortC;
	private JComboBox<String> comboBoxMergePortD;
	private JComboBox<String> comboBoxProtocolPortA;
	private JComboBox<String> comboBoxProtocolPortB;
	private JComboBox<String> comboBoxProtocolPortC;
	private JComboBox<String> comboBoxProtocolPortD;
	private JCheckBox chckbxRdmEnablePortA;
	private JCheckBox chckbxRdmEnablePortB;
	private JCheckBox chckbxRdmEnablePortC;
	private JCheckBox chckbxRdmEnablePortD;
	
	private int artnetNet = 0;
	private int artnetSubnet = 0;
	
	private int artnetAdressPortA;
	private int artnetAdressPortB;
	private int artnetAdressPortC;
	private int artnetAdressPortD;
	
	private JCheckBox chckbxMapUniverse0;
	
	private JLabel lblDirection;
	
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
	private JLabel lblUniverse;
	
	private JCheckBox chckbxEnableRDM;
	private JLabel lblShortName;
	private JLabel lblLongName;
	private JTextField textFieldShortName;
	private JTextField textFieldLongName;
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
	
	public WizardArtnetTxt() {
		setTitle("Art-Net");
		initComponents();
		createEvents();

	}
	
	public WizardArtnetTxt(String nodeId, OrangePi opi, RemoteConfig remoteConfig) {
		this.nodeId = nodeId;
		this.opi = opi;
		this.remoteConfig = remoteConfig;

		setTitle(nodeId);

		initComponents();
		createEvents();

		load();
	}
	
	private void initComponents() {
		setBounds(100, 100, 671, 395);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		
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
		
		formattedTextFieldUniverseA = new JFormattedTextField(formatterUniverse);
		formattedTextFieldUniverseA.setColumns(6);
		formattedTextFieldUniverseB = new JFormattedTextField(formatterUniverse);
		formattedTextFieldUniverseB.setColumns(6);
		formattedTextFieldUniverseC = new JFormattedTextField(formatterUniverse);
		formattedTextFieldUniverseC.setColumns(6);
		formattedTextFieldUniverseD = new JFormattedTextField(formatterUniverse);
		formattedTextFieldUniverseD.setColumns(6);
		
		comboBoxDirectionPortA = new JComboBox<String> ();
		comboBoxDirectionPortA.setModel(new DefaultComboBoxModel<String>(new String[] {"Output", "Input", "Disable"}));
		comboBoxDirectionPortB = new JComboBox<String>();
		comboBoxDirectionPortB.setModel(new DefaultComboBoxModel<String>(new String[] {"Output", "Input", "Disable"}));
		comboBoxDirectionPortC = new JComboBox<String>();
		comboBoxDirectionPortC.setModel(new DefaultComboBoxModel<String>(new String[] {"Output", "Input", "Disable"}));
		comboBoxDirectionPortD = new JComboBox<String>();
		comboBoxDirectionPortD.setModel(new DefaultComboBoxModel<String>(new String[] {"Output", "Input", "Disable"}));
		
		comboBoxMergePortA = new JComboBox<String> ();
		comboBoxMergePortA.setModel(new DefaultComboBoxModel<String> (new String[] {"HTP", "LTP"}));
		comboBoxMergePortB = new JComboBox<String> ();
		comboBoxMergePortB.setModel(new DefaultComboBoxModel<String> (new String[] {"HTP", "LTP"}));
		comboBoxMergePortC = new JComboBox<String> ();
		comboBoxMergePortC.setModel(new DefaultComboBoxModel<String> (new String[] {"HTP", "LTP"}));
		comboBoxMergePortD = new JComboBox<String> ();
		comboBoxMergePortD.setModel(new DefaultComboBoxModel<String> (new String[] {"HTP", "LTP"}));
		
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
		
		JLabel lblMerge = new JLabel("Merge");
		lblMerge.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		JLabel lblProtocol = new JLabel("Protocol");
		lblProtocol.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		JLabel lblDestinationIP = new JLabel("Destination IP Address");
		lblDestinationIP.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		lblDirection = new JLabel("Direction");
		lblDirection.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		lblUniverse = new JLabel("Universe");
		
		chckbxEnableRDM = new JCheckBox("Enable RDM");
		
		lblShortName = new JLabel("Short name");
		
		lblLongName = new JLabel("Long name");
		
		textFieldShortName = new JTextField();
		textFieldShortName.setColumns(10);
		
		textFieldLongName = new JTextField();
		textFieldLongName.setColumns(10);
		
		JLabel lblFailsafe = new JLabel("Failsafe");
		
		comboBoxFailsafe = new JComboBox<String>();
		comboBoxFailsafe.setModel(new DefaultComboBoxModel<String>(new String[] {"Hold", "Zero", "Full", "Scene"}));
										
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
								.addComponent(formattedTextFieldUniverseA, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldUniverseB, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldUniverseC, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldUniverseD, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDirection)
								.addComponent(comboBoxDirectionPortD, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortC, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortB, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortA, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblMerge)
								.addComponent(comboBoxMergePortD, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortC, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortB, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortA, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblProtocol)
								.addComponent(comboBoxProtocolPortA, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxProtocolPortB, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxProtocolPortC, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxProtocolPortD, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDestinationIP)
								.addGroup(gl_contentPanel.createSequentialGroup()
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
										.addComponent(formattedIP4PortD, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(chckbxRdmEnablePortD, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxRdmEnablePortB, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxRdmEnablePortA, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxRdmEnablePortC, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)))
						.addComponent(chckbxEnableRDM)
						.addComponent(chckbxMapUniverse0)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblShortName)
								.addComponent(lblLongName))
							.addGap(18)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(textFieldShortName, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldLongName, GroupLayout.PREFERRED_SIZE, 531, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblFailsafe, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(comboBoxFailsafe, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(8)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblUniverse)
						.addComponent(lblDirection, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMerge)
						.addComponent(lblProtocol, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDestinationIP, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE))
					.addGap(12)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(formattedTextFieldUniverseA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblUniversePortA))
						.addComponent(comboBoxDirectionPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxMergePortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxProtocolPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedIP1PortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedIP2PortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedIP3PortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedIP4PortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(chckbxRdmEnablePortA))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(formattedTextFieldUniverseB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblUniversePortB))
						.addComponent(comboBoxDirectionPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxMergePortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxProtocolPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedIP1PortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedIP2PortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedIP3PortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedIP4PortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(chckbxRdmEnablePortB))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(formattedTextFieldUniverseC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblUniversePortC))
						.addComponent(comboBoxDirectionPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxMergePortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxProtocolPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedIP1PortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedIP2PortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedIP3PortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedIP4PortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(chckbxRdmEnablePortC))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(formattedTextFieldUniverseD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblUniversePortD))
						.addComponent(comboBoxDirectionPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxMergePortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
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
						.addComponent(textFieldLongName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblLongName))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFailsafe)
						.addComponent(comboBoxFailsafe, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
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
		
		formattedTextFieldUniverseA.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					formattedTextFieldUniverseA.commitEdit();
					artnetAdressPortA = setNetSubNet((int)formattedTextFieldUniverseA.getValue());
					update('A');
				} catch (ParseException e1) {
					formattedTextFieldUniverseA.setValue(getUniverseFromAddress(artnetAdressPortA));
				}
			}
		});
		
		formattedTextFieldUniverseB.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					formattedTextFieldUniverseB.commitEdit();
					artnetAdressPortB = setNetSubNet((int)formattedTextFieldUniverseB.getValue());
					update('B');
				} catch (ParseException e1) {
					formattedTextFieldUniverseB.setValue(getUniverseFromAddress(artnetAdressPortB));
				}
			}
		});
		
		formattedTextFieldUniverseC.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					formattedTextFieldUniverseC.commitEdit();
					artnetAdressPortC = setNetSubNet((int)formattedTextFieldUniverseC.getValue());
					update('C');
				} catch (ParseException e1) {
					formattedTextFieldUniverseC.setValue(getUniverseFromAddress(artnetAdressPortC));
				}
			}
		});
		
		formattedTextFieldUniverseD.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					formattedTextFieldUniverseD.commitEdit();
					artnetAdressPortD = setNetSubNet((int)formattedTextFieldUniverseD.getValue());
					update('D');
				} catch (ParseException e1) {
					formattedTextFieldUniverseD.setValue(getUniverseFromAddress(artnetAdressPortD));
				}
			}
		});
		
		comboBoxDirectionPortA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				handleComboBoxDirectionPort(comboBoxDirectionPortA, comboBoxMergePortA, comboBoxProtocolPortA,
						formattedIP1PortA, formattedIP2PortA, formattedIP3PortA, formattedIP4PortA, chckbxRdmEnablePortA);
			}
		});
		
		comboBoxDirectionPortB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleComboBoxDirectionPort(comboBoxDirectionPortB, comboBoxMergePortB, comboBoxProtocolPortB,
						formattedIP1PortB, formattedIP2PortB, formattedIP3PortB, formattedIP4PortB, chckbxRdmEnablePortB);
			}
		});
		
		comboBoxDirectionPortC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleComboBoxDirectionPort(comboBoxDirectionPortC, comboBoxMergePortC, comboBoxProtocolPortC,
						formattedIP1PortC, formattedIP2PortC, formattedIP3PortC, formattedIP4PortC, chckbxRdmEnablePortC);
			}
		});
		
		comboBoxDirectionPortD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleComboBoxDirectionPort(comboBoxDirectionPortD, comboBoxMergePortD, comboBoxProtocolPortD,
						formattedIP1PortD, formattedIP2PortD, formattedIP3PortD, formattedIP4PortD, chckbxRdmEnablePortD);
			}
		});
	}
	
	private void handleComboBoxDirectionPort(JComboBox<String> comboBoxDirection, JComboBox<String> merge,
			JComboBox<String> protocol, JFormattedTextField IP1, JFormattedTextField IP2, JFormattedTextField IP3,
			JFormattedTextField IP4, JCheckBox rdm) {
		final int selected = comboBoxDirection.getSelectedIndex();

		if (selected == 0) { // Output
			merge.setEnabled(true);
			protocol.setEnabled(true);
			rdm.setEnabled(true);
			//
			IP1.setEnabled(false);
			IP2.setEnabled(false);
			IP3.setEnabled(false);
			IP4.setEnabled(false);
			return;
		}
		if (selected == 1) { // Input
			merge.setEnabled(false);
			protocol.setEnabled(false);
			rdm.setEnabled(false);
			//
			IP1.setEnabled(true);
			IP2.setEnabled(true);
			IP3.setEnabled(true);
			IP4.setEnabled(true);
			return;
		}

		// Disabled

		merge.setEnabled(true);
		protocol.setEnabled(true);
		rdm.setEnabled(true);
		//
		IP1.setEnabled(true);
		IP2.setEnabled(true);
		IP3.setEnabled(true);
		IP4.setEnabled(true);
	}
	
	private void load() {
		if (opi != null) {
			final String txt = opi.getTxt(TXT_FILE);
			if (txt != null) {
				final String[] lines = txt.split("\n");
				for (int i = 0; i < lines.length; i++) {
					final String line = lines[i];
					//
					if (line.contains("failsafe")) {
						final String value = Properties.getString(line);
						if (value.equals("off")) {
							comboBoxFailsafe.setSelectedIndex(1);
						} else if (value.equals("on")) {
							comboBoxFailsafe.setSelectedIndex(2);
						} else if (value.equals("playback")) {
							comboBoxFailsafe.setSelectedIndex(3);
						} else {
							comboBoxFailsafe.setSelectedIndex(0);
						}
						continue;
					}
					//
					if (line.contains("short_name")) {
						textFieldShortName.setText(Properties.getString(line));
						continue;
					}
					if (line.contains("long_name")) {
						textFieldLongName.setText(Properties.getString(line));
						continue;
					}
					//
					if (line.contains("enable_rdm")) {
						chckbxEnableRDM.setSelected(Properties.getBool(line));
						continue;
					}
					//
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
					if ((line.contains("net") && (!line.contains("subnet")) && (!line.contains("artnet") && (!line.contains("network"))))) {
						artnetNet = Properties.getInt(line);
						System.out.println("artnetNet->" + artnetNet);
						continue;
					}
					if (line.contains("subnet")) {
						artnetSubnet = Properties.getInt(line);
						System.out.println("artnetSubnet->" + artnetSubnet);
						continue;
					}
					if (line.contains("universe_port_a")) {
						artnetAdressPortA = Properties.getInt(line);
						formattedTextFieldUniverseA.setValue(getUniverseFromAddress(artnetAdressPortA));
						continue;
					}
					if (line.contains("universe_port_b")) {
						artnetAdressPortB = Properties.getInt(line);
						formattedTextFieldUniverseB.setValue(getUniverseFromAddress(artnetAdressPortB));
						continue;
					}
					if (line.contains("universe_port_c")) {
						artnetAdressPortC = Properties.getInt(line);
						formattedTextFieldUniverseC.setValue(getUniverseFromAddress(artnetAdressPortC));
						continue;
					}
					if (line.contains("universe_port_d")) {
						artnetAdressPortD = Properties.getInt(line);
						formattedTextFieldUniverseD.setValue(getUniverseFromAddress(artnetAdressPortD));
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
	
	private int setNetSubNet(int universe) {
		artnetNet = (universe >> 8) & 0x7F;
		artnetSubnet  = (universe >> 4) & 0x0F;
		final int artnetAddress = universe & 0x0F;
		System.out.println("[" + universe + "] " + artnetNet + ":" + artnetSubnet + ":" + artnetAddress);
		return artnetAddress;
	}
	
	private int getUniverseFromAddress(int address) {
		int universe = (artnetNet & 0x7F) << 8;		// Net : Bits 14-8
		universe |= ((artnetSubnet & 0x0F) << 4);	// Sub-Net : Bits 7-4
		universe |= address & 0x0F;	
		
		System.out.println("" + artnetNet + ":" +  artnetSubnet + "->" + universe);
		
		return universe;
	}
		
	private String getComment(JComboBox<String> comboBox) {
		return comboBox.getSelectedItem().toString() == "Input" ? "" : "#";
	}
	
	private void update(char port) {
		if (port != 'A')
			formattedTextFieldUniverseA.setValue(getUniverseFromAddress(artnetAdressPortA));
		if (port != 'B')
			formattedTextFieldUniverseB.setValue(getUniverseFromAddress(artnetAdressPortB));
		if (port != 'C')
			formattedTextFieldUniverseC.setValue(getUniverseFromAddress(artnetAdressPortC));
		if (port != 'D')
			formattedTextFieldUniverseD.setValue(getUniverseFromAddress(artnetAdressPortD));
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
	
	private String getProtocol(JComboBox<String> comboBox) {
		if (comboBox.getSelectedItem().toString().toLowerCase().equals("sacn")) {
			return "sacn";
		}
		return "artnet";
	}
	
	private String getMergeMode(JComboBox<String> comboBox) {
		if (comboBox.getSelectedItem().toString().toLowerCase().equals("ltp")) {
			return "ltp";
		}
		return "htp";
	}
	
	private String getFailsafe() {
		if (comboBoxFailsafe.getSelectedItem().toString().toLowerCase().equals("zero")) {
			return "off";
		}
		
		if (comboBoxFailsafe.getSelectedItem().toString().toLowerCase().equals("full")) {
			return "on";
		}
		
		if (comboBoxFailsafe.getSelectedItem().toString().toLowerCase().equals("scene")) {
			return "playback";
		}
		
		return "hold";
	}
	
	private void save() {
		if (opi != null) {
			StringBuffer txtAppend = new StringBuffer();
			//
			txtAppend.append(String.format("failsafe=%s\n", getFailsafe()));
			//
			final String shortName = textFieldShortName.getText();	
			if (shortName.length() > SHORT_NAME_LENGTH) {
				txtAppend.append(String.format("short_name=%s\n", shortName.substring(0, SHORT_NAME_LENGTH - 1)));
				textFieldShortName.setText(shortName.substring(SHORT_NAME_LENGTH - 1));
			} else {
				txtAppend.append(String.format("short_name=%s\n", shortName));
			}
			
			final String longName = textFieldLongName.getText();	
			if (longName.length() > LONG_NAME_LENGTH) {
				txtAppend.append(String.format("long_name=%s\n", longName.substring(0, LONG_NAME_LENGTH - 1)));
				textFieldShortName.setText(longName.substring(LONG_NAME_LENGTH - 1));
			} else {
				txtAppend.append(String.format("long_name=%s\n", longName));
			}
			//			
			txtAppend.append(String.format("net=%d\n", artnetNet));
			txtAppend.append(String.format("subnet=%d\n", artnetSubnet));
			
			txtAppend.append(String.format("direction_port_a=%s\n", getDirection(comboBoxDirectionPortA)));
			txtAppend.append(String.format("direction_port_b=%s\n", getDirection(comboBoxDirectionPortB)));
			txtAppend.append(String.format("direction_port_c=%s\n", getDirection(comboBoxDirectionPortC)));
			txtAppend.append(String.format("direction_port_d=%s\n", getDirection(comboBoxDirectionPortD)));
			
			txtAppend.append(String.format("universe_port_a=%s\n", artnetAdressPortA));
			txtAppend.append(String.format("universe_port_b=%s\n", artnetAdressPortB));
			txtAppend.append(String.format("universe_port_c=%s\n", artnetAdressPortC));
			txtAppend.append(String.format("universe_port_d=%s\n", artnetAdressPortD));
			
			// Output
			txtAppend.append(String.format("protocol_port_a=%s\n", getProtocol(comboBoxProtocolPortA)));
			txtAppend.append(String.format("protocol_port_b=%s\n", getProtocol(comboBoxProtocolPortB)));
			txtAppend.append(String.format("protocol_port_c=%s\n", getProtocol(comboBoxProtocolPortC)));
			txtAppend.append(String.format("protocol_port_d=%s\n", getProtocol(comboBoxProtocolPortD)));

			txtAppend.append(String.format("merge_mode_port_a=%s\n", getMergeMode(comboBoxMergePortA)));
			txtAppend.append(String.format("merge_mode_port_b=%s\n", getMergeMode(comboBoxMergePortB)));
			txtAppend.append(String.format("merge_mode_port_c=%s\n", getMergeMode(comboBoxMergePortC)));
			txtAppend.append(String.format("merge_mode_port_d=%s\n", getMergeMode(comboBoxMergePortD)));
			
			txtAppend.append(String.format("enable_rdm=%d\n", chckbxEnableRDM.isSelected() ? 1 : 0));

			txtAppend.append(String.format("rdm_enable_port_a=%s\n", chckbxRdmEnablePortA.isSelected() ? 1 : 0));
			txtAppend.append(String.format("rdm_enable_port_b=%s\n", chckbxRdmEnablePortB.isSelected() ? 1 : 0));
			txtAppend.append(String.format("rdm_enable_port_c=%s\n", chckbxRdmEnablePortC.isSelected() ? 1 : 0));
			txtAppend.append(String.format("rdm_enable_port_d=%s\n", chckbxRdmEnablePortD.isSelected() ? 1 : 0));
				
			// Input
			txtAppend.append(String.format("%sdestination_ip_port_a=%d.%d.%d.%d\n", getComment(comboBoxDirectionPortA), formattedIP1PortA.getValue(),formattedIP2PortA.getValue(),formattedIP3PortA.getValue(),formattedIP4PortA.getValue()));
			txtAppend.append(String.format("%sdestination_ip_port_b=%d.%d.%d.%d\n", getComment(comboBoxDirectionPortB), formattedIP1PortB.getValue(),formattedIP2PortB.getValue(),formattedIP3PortB.getValue(),formattedIP4PortB.getValue()));
			txtAppend.append(String.format("%sdestination_ip_port_c=%d.%d.%d.%d\n", getComment(comboBoxDirectionPortC), formattedIP1PortC.getValue(),formattedIP2PortC.getValue(),formattedIP3PortC.getValue(),formattedIP4PortC.getValue()));
			txtAppend.append(String.format("%sdestination_ip_port_d=%d.%d.%d.%d\n", getComment(comboBoxDirectionPortD), formattedIP1PortD.getValue(),formattedIP2PortD.getValue(),formattedIP3PortD.getValue(),formattedIP4PortD.getValue()));
			
			//
			txtAppend.append(String.format("map_universe0=%d\n", chckbxMapUniverse0.isSelected() ? 1 : 0));
			
			String txt = Properties.removeComments(opi.getTxt(TXT_FILE));
			txt = txt.replaceAll("direction", "#");
			txt = txt.replaceAll("universe_port_", "#");
			txt = txt.replaceAll("destination_ip_port", "#");
			txt = txt.replaceAll("rdm_enable_port", "#");
			txt = txt.replaceAll("failsafe", "#");
					
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
