/* Copyright (C) 2021-2023 by Arjan van Vught mailto:info@gd32-dmx.org
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
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import javax.swing.SwingConstants;
import java.awt.Component;

public class WizardArtnetTxt extends JDialog {
	private static final long serialVersionUID = 1L;	
	private static final String TXT_FILE = "artnet.txt";
	
	String nodeId = null;
	OrangePi opi = null;
	RemoteConfig remoteConfig = null;
	String txt = null;

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
	
	private JComboBox<String> comboBoxOutputStylePortA;
	private JComboBox<String> comboBoxOutputStylePortB;
	private JComboBox<String> comboBoxOutputStylePortC;
	private JComboBox<String> comboBoxOutputStylePortD;
	
	private JComboBox<String> comboBoxProtocolPortA;
	private JComboBox<String> comboBoxProtocolPortB;
	private JComboBox<String> comboBoxProtocolPortC;
	private JComboBox<String> comboBoxProtocolPortD;
	
	private JCheckBox chckbxRdmEnablePortA;
	private JCheckBox chckbxRdmEnablePortB;
	private JCheckBox chckbxRdmEnablePortC;
	private JCheckBox chckbxRdmEnablePortD;
	
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
	
	private JFormattedTextField formattedTextFieldsACNPriorityPortD;
	private JFormattedTextField formattedTextFieldsACNPriorityPortC;
	private JFormattedTextField formattedTextFieldsACNPriorityPortB;
	private JFormattedTextField formattedTextFieldsACNPriorityPortA;
	
	private JLabel lblUniversePortA;
	private JLabel lblUniversePortB;
	private JLabel lblUniversePortC;
	private JLabel lblUniversePortD;
	private JLabel lblUniverse;
	
	private JCheckBox chckbxEnableRDM;
	private JLabel lblLongName;
	private JTextField textFieldLongName;
	private JComboBox<String> comboBoxFailsafe;
	
	private boolean hasRDM = false;
	private boolean hasPriority = false;
	
	private boolean hasUniverseA = false;
	private boolean hasUniverseB = false;
	private boolean hasUniverseC = false;
	private boolean hasUniverseD = false;
	
	private boolean hasOutputStyleA = false;
	private boolean hasOutputStyleB = false;
	private boolean hasOutputStyleC = false;
	private boolean hasOutputStyleD = false;
	
	private boolean hasDestinationIpA = false;
	private boolean hasDestinationIpB = false;
	private boolean hasDestinationIpC = false;
	private boolean hasDestinationIpD = false;
	
	private String ipAddressA = "";
	private String ipAddressB = "";
	private String ipAddressC = "";
	private String ipAddressD = "";
	
	private JTextField textFieldLabelPortA;
	private JTextField textFieldLabelPortB;
	private JTextField textFieldLabelPortC;
	private JTextField textFieldLabelPortD;
	
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
		setBounds(100, 100, 1112, 370);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		NumberFormat format = NumberFormat.getInstance();
		format.setGroupingUsed(false);
		format.setMinimumIntegerDigits(0);
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
		
		comboBoxOutputStylePortA = new JComboBox<String>();
		comboBoxOutputStylePortA.setEnabled(false);
		comboBoxOutputStylePortA.setModel(new DefaultComboBoxModel<String> (new String[] {"Delta", "Constant"}));
		comboBoxOutputStylePortA.setSelectedIndex(1);
		comboBoxOutputStylePortB = new JComboBox<String>();
		comboBoxOutputStylePortB.setEnabled(false);
		comboBoxOutputStylePortB.setModel(new DefaultComboBoxModel<String> (new String[] {"Delta", "Constant"}));
		comboBoxOutputStylePortB.setSelectedIndex(1);
		comboBoxOutputStylePortC = new JComboBox<String>();
		comboBoxOutputStylePortC.setEnabled(false);
		comboBoxOutputStylePortC.setModel(new DefaultComboBoxModel<String> (new String[] {"Delta", "Constant"}));
		comboBoxOutputStylePortC.setSelectedIndex(1);
		comboBoxOutputStylePortD = new JComboBox<String>();
		comboBoxOutputStylePortD.setEnabled(false);
		comboBoxOutputStylePortD.setModel(new DefaultComboBoxModel<String> (new String[] {"Delta", "Constant"}));
		comboBoxOutputStylePortD.setSelectedIndex(1);
				
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
		
		lblLongName = new JLabel("Long name");
		
		textFieldLongName = new JTextField();
		textFieldLongName.setColumns(10);
		
		JLabel lblFailsafe = new JLabel("Failsafe");
		
		comboBoxFailsafe = new JComboBox<String>();
		comboBoxFailsafe.setModel(new DefaultComboBoxModel<String>(new String[] {"Hold", "Zero", "Full", "Scene"}));
		
		JLabel lblOutputStyle = new JLabel("Output Style");
		lblOutputStyle.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		
		NumberFormatter formatterPriority = new NumberFormatter(format);
		formatterPriority.setValueClass(Integer.class);
		formatterPriority.setMinimum(1);
		formatterPriority.setMaximum(200);
		formatterPriority.setAllowsInvalid(false);
		formatterPriority.setCommitsOnValidEdit(true);
		
		JLabel lblLabel = new JLabel("Label");
		
		textFieldLabelPortA = new JTextField();
		textFieldLabelPortA.setColumns(10);
		
		textFieldLabelPortB = new JTextField();
		textFieldLabelPortB.setColumns(10);
		
		textFieldLabelPortC = new JTextField();
		textFieldLabelPortC.setColumns(10);
		
		textFieldLabelPortD = new JTextField();
		textFieldLabelPortD.setColumns(10);
		
		JLabel lblPrioriy = new JLabel("Priority");
		
		formattedTextFieldsACNPriorityPortA = new JFormattedTextField(formatterPriority);
		formattedTextFieldsACNPriorityPortA.setText("100");
		
		formattedTextFieldsACNPriorityPortB = new JFormattedTextField(formatterPriority);
		formattedTextFieldsACNPriorityPortB.setText("100");
		
		formattedTextFieldsACNPriorityPortC = new JFormattedTextField(formatterPriority);
		formattedTextFieldsACNPriorityPortC.setText("100");
		
		formattedTextFieldsACNPriorityPortD = new JFormattedTextField(formatterPriority);
		formattedTextFieldsACNPriorityPortD.setText("100");
														
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUniversePortD, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniversePortA, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniversePortB, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniversePortC, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(formattedTextFieldUniverseC, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldUniverseB, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldUniverseA, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldUniverseD, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniverse))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBoxDirectionPortD, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortC, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortB, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxDirectionPortA, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBoxMergePortD, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortC, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortB, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxMergePortA, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblMerge))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBoxProtocolPortD, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxProtocolPortC, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxProtocolPortB, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxProtocolPortA, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblProtocol))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(comboBoxOutputStylePortD, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxOutputStylePortC, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxOutputStylePortB, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxOutputStylePortA, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblOutputStyle))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDestinationIP)
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(formattedIP1PortA, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(formattedIP2PortA, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(formattedIP3PortA, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(formattedIP4PortA, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(formattedIP1PortB, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(formattedIP2PortB, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(formattedIP3PortB, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(formattedIP4PortB, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(formattedIP1PortC, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(formattedIP2PortC, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(formattedIP3PortC, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(formattedIP4PortC, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPanel.createSequentialGroup()
									.addComponent(formattedIP1PortD, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(formattedIP2PortD, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(formattedIP3PortD, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(formattedIP4PortD, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(formattedTextFieldsACNPriorityPortD, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldsACNPriorityPortC, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldsACNPriorityPortB, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldsACNPriorityPortA, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPrioriy))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(chckbxRdmEnablePortD, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxRdmEnablePortC, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxRdmEnablePortB, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxRdmEnablePortA, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(textFieldLabelPortD, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldLabelPortC, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldLabelPortB, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblLabel)
								.addComponent(textFieldLabelPortA, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblFailsafe, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBoxFailsafe, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(lblLongName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textFieldLongName, GroupLayout.PREFERRED_SIZE, 532, GroupLayout.PREFERRED_SIZE))
						.addComponent(chckbxEnableRDM)
						.addComponent(chckbxMapUniverse0)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(140)
							.addComponent(lblDirection)))
					.addGap(10))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblUniverse)
							.addComponent(lblDirection)
							.addComponent(lblMerge)
							.addComponent(lblProtocol)
							.addComponent(lblOutputStyle)
							.addComponent(lblDestinationIP))
						.addComponent(lblLabel)
						.addComponent(lblPrioriy))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
							.addComponent(lblUniversePortA)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(formattedIP1PortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP2PortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP3PortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP4PortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxRdmEnablePortA)
								.addComponent(formattedTextFieldsACNPriorityPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldLabelPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(formattedTextFieldUniverseA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxDirectionPortA, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxMergePortA, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxProtocolPortA, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxOutputStylePortA, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
							.addComponent(lblUniversePortB)
							.addComponent(comboBoxDirectionPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(comboBoxMergePortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(comboBoxProtocolPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(comboBoxOutputStylePortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(formattedIP1PortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP2PortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP3PortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP4PortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxRdmEnablePortB)
								.addComponent(textFieldLabelPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldsACNPriorityPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(formattedTextFieldUniverseB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(1)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
							.addComponent(lblUniversePortC)
							.addComponent(comboBoxDirectionPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(comboBoxMergePortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(comboBoxProtocolPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(comboBoxOutputStylePortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(formattedIP1PortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP2PortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP3PortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP4PortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxRdmEnablePortC)
								.addComponent(textFieldLabelPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldsACNPriorityPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(formattedTextFieldUniverseC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(1)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
							.addComponent(lblUniversePortD)
							.addComponent(comboBoxDirectionPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(comboBoxMergePortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(comboBoxProtocolPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(comboBoxOutputStylePortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(formattedIP1PortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP2PortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP3PortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedIP4PortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxRdmEnablePortD)
								.addComponent(formattedTextFieldsACNPriorityPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldLabelPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(formattedTextFieldUniverseD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(chckbxEnableRDM)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxMapUniverse0)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLongName)
						.addComponent(textFieldLongName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFailsafe)
						.addComponent(comboBoxFailsafe, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_contentPanel.linkSize(SwingConstants.VERTICAL, new Component[] {lblUniversePortA, formattedTextFieldUniverseA, comboBoxDirectionPortA, comboBoxMergePortA, comboBoxProtocolPortA, comboBoxOutputStylePortA, chckbxRdmEnablePortA, formattedIP1PortA, formattedIP2PortA, formattedIP3PortA, formattedIP4PortA, textFieldLabelPortA, formattedTextFieldsACNPriorityPortA});
		gl_contentPanel.linkSize(SwingConstants.VERTICAL, new Component[] {lblUniversePortB, formattedTextFieldUniverseB, comboBoxDirectionPortB, comboBoxMergePortB, comboBoxProtocolPortB, comboBoxOutputStylePortB, chckbxRdmEnablePortB, formattedIP1PortB, formattedIP2PortB, formattedIP3PortB, formattedIP4PortB, textFieldLabelPortB, formattedTextFieldsACNPriorityPortB});
		gl_contentPanel.linkSize(SwingConstants.VERTICAL, new Component[] {lblUniversePortC, formattedTextFieldUniverseC, comboBoxDirectionPortC, comboBoxMergePortC, comboBoxProtocolPortC, comboBoxOutputStylePortC, chckbxRdmEnablePortC, formattedIP1PortC, formattedIP2PortC, formattedIP3PortC, formattedIP4PortC, textFieldLabelPortC, formattedTextFieldsACNPriorityPortC});
		gl_contentPanel.linkSize(SwingConstants.VERTICAL, new Component[] {lblUniversePortD, formattedTextFieldUniverseD, comboBoxDirectionPortD, comboBoxMergePortD, comboBoxProtocolPortD, comboBoxOutputStylePortD, chckbxRdmEnablePortD, formattedIP1PortD, formattedIP2PortD, formattedIP3PortD, formattedIP4PortD, textFieldLabelPortD, formattedTextFieldsACNPriorityPortD});

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
					eventUniverse('A');
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
					eventUniverse('B');
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
					eventUniverse('C');
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
					eventUniverse('D');
				} catch (ParseException e1) {
					formattedTextFieldUniverseD.setValue(getUniverseFromAddress(artnetAdressPortD));
				}
			}
		});
		
		comboBoxDirectionPortA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				handleComboBoxDirectionPort(comboBoxDirectionPortA, comboBoxMergePortA, comboBoxOutputStylePortA, hasOutputStyleA, comboBoxProtocolPortA,formattedTextFieldsACNPriorityPortA,chckbxRdmEnablePortA);
				updateDestinationIp('A');
			}
		});
		
		comboBoxDirectionPortB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleComboBoxDirectionPort(comboBoxDirectionPortB, comboBoxMergePortB, comboBoxOutputStylePortB, hasOutputStyleB, comboBoxProtocolPortB,formattedTextFieldsACNPriorityPortB,chckbxRdmEnablePortB);
				updateDestinationIp('B');
			}
		});
		
		comboBoxDirectionPortC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleComboBoxDirectionPort(comboBoxDirectionPortC, comboBoxMergePortC, comboBoxOutputStylePortC, hasOutputStyleC, comboBoxProtocolPortC,formattedTextFieldsACNPriorityPortC, chckbxRdmEnablePortC);
				updateDestinationIp('C');
			}
		});
		
		comboBoxDirectionPortD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleComboBoxDirectionPort(comboBoxDirectionPortD, comboBoxMergePortD, comboBoxOutputStylePortD, hasOutputStyleD, comboBoxProtocolPortD,formattedTextFieldsACNPriorityPortD, chckbxRdmEnablePortD);
				updateDestinationIp('D');
			}
		});
		
		comboBoxProtocolPortA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleComboBoxDirectionPort(comboBoxDirectionPortA, comboBoxMergePortA, comboBoxOutputStylePortA, hasOutputStyleA, comboBoxProtocolPortA,formattedTextFieldsACNPriorityPortA, chckbxRdmEnablePortA);
				updateDestinationIp('A');
			}
		});
		
		comboBoxProtocolPortB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleComboBoxDirectionPort(comboBoxDirectionPortB, comboBoxMergePortB, comboBoxOutputStylePortB, hasOutputStyleB, comboBoxProtocolPortB,formattedTextFieldsACNPriorityPortB, chckbxRdmEnablePortB);
				updateDestinationIp('B');
			}
		});
		
		comboBoxProtocolPortC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleComboBoxDirectionPort(comboBoxDirectionPortC, comboBoxMergePortC, comboBoxOutputStylePortC, hasOutputStyleC, comboBoxProtocolPortC,formattedTextFieldsACNPriorityPortC, chckbxRdmEnablePortC);
				updateDestinationIp('C');
			}
		});
		
		comboBoxProtocolPortD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleComboBoxDirectionPort(comboBoxDirectionPortD, comboBoxMergePortD, comboBoxOutputStylePortD, hasOutputStyleD, comboBoxProtocolPortD,formattedTextFieldsACNPriorityPortD,chckbxRdmEnablePortD);
				updateDestinationIp('D');
			}
		});
	}
	
	
	private void handleComboBoxDirectionPort(JComboBox<String> comboBoxDirection, JComboBox<String> merge,
			JComboBox<String> outputStyle, Boolean hasOutputStyle, JComboBox<String> protocol, JFormattedTextField priority, JCheckBox rdm) {

		final int selected = comboBoxDirection.getSelectedIndex();
		
		if (selected == 0) { // Output
			merge.setEnabled(true);
			outputStyle.setEnabled(hasOutputStyle);
			priority.setEnabled(false);
			rdm.setEnabled(hasRDM);
			return;
		}

		if (selected == 1) { // Input
			merge.setEnabled(false);
			outputStyle.setEnabled(false);
			priority.setEnabled(protocol.getSelectedIndex() == 1);
			rdm.setEnabled(false);
			return;
		}

		// Disabled
		merge.setEnabled(true);
		outputStyle.setEnabled(hasOutputStyle);
		priority.setEnabled(true);
		rdm.setEnabled(hasRDM);
	}
	
	private void load() {
		if (opi != null) {
			txt = opi.getTxt(TXT_FILE);
			System.out.println(">|" + txt + "|<");
			if (txt != null) {
				final String[] lines = txt.split("\n");
				for (int i = 0; i < lines.length; i++) {
					final String line = lines[i];
					System.out.println("[" + line + "]");
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
			
					if (line.contains("long_name")) {
						textFieldLongName.setText(Properties.getString(line));
						continue;
					}
					
					if (line.contains("enable_rdm")) {
						chckbxEnableRDM.setSelected(Properties.getBool(line));
						hasRDM = true;
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
					if (line.contains("universe_port_a")) {
						artnetAdressPortA = Properties.getInt(line);
						formattedTextFieldUniverseA.setValue(getUniverseFromAddress(artnetAdressPortA));
						hasUniverseA = true;
						continue;
					}
					if (line.contains("universe_port_b")) {
						artnetAdressPortB = Properties.getInt(line);
						formattedTextFieldUniverseB.setValue(getUniverseFromAddress(artnetAdressPortB));
						hasUniverseB = true;
						continue;
					}
					if (line.contains("universe_port_c")) {
						artnetAdressPortC = Properties.getInt(line);
						formattedTextFieldUniverseC.setValue(getUniverseFromAddress(artnetAdressPortC));
						hasUniverseC = true;
						continue;
					}
					if (line.contains("universe_port_d")) {
						artnetAdressPortD = Properties.getInt(line);
						formattedTextFieldUniverseD.setValue(getUniverseFromAddress(artnetAdressPortD));
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
					//
					if (line.contains("output_style_a")) {
						comboBoxOutputStylePortA.setSelectedIndex(Properties.getString(line).equals("constant") ? 1 : 0);
						hasOutputStyleA = true;
						continue;
					}
					if (line.contains("output_style_b")) {
						comboBoxOutputStylePortB.setSelectedIndex(Properties.getString(line).equals("constant") ? 1 : 0);
						hasOutputStyleB = true;
						continue;
					}
					if (line.contains("output_style_c")) {
						comboBoxOutputStylePortC.setSelectedIndex(Properties.getString(line).equals("constant") ? 1 : 0);
						hasOutputStyleC = true;
						continue;
					}
					if (line.contains("output_style_d")) {
						comboBoxOutputStylePortD.setSelectedIndex(Properties.getString(line).equals("constant") ? 1 : 0);
						hasOutputStyleD = true;
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
						ipAddressA = Properties.getString(line);
						hasDestinationIpA = setIpaddress(ipAddressA, formattedIP1PortA, formattedIP2PortA, formattedIP3PortA, formattedIP4PortA);
						continue;
					}
					if (line.contains("destination_ip_port_b")) {
						ipAddressB = Properties.getString(line);
						hasDestinationIpB = setIpaddress(ipAddressB, formattedIP1PortB, formattedIP2PortB, formattedIP3PortB, formattedIP4PortB);
						continue;
					}
					if (line.contains("destination_ip_port_c")) {
						ipAddressC = Properties.getString(line);
						hasDestinationIpC = setIpaddress(ipAddressC, formattedIP1PortC, formattedIP2PortC, formattedIP3PortC, formattedIP4PortC);
						continue;
					}
					if (line.contains("destination_ip_port_d")) {
						ipAddressD = Properties.getString(line);
						hasDestinationIpD = setIpaddress(ipAddressD, formattedIP1PortD, formattedIP2PortD, formattedIP3PortD, formattedIP4PortD);
						continue;
					}
					
					if (line.contains("priority_port_a")) {
						formattedTextFieldsACNPriorityPortA.setText(Properties.getString(line));
						hasPriority = true;
					}
					
					if (line.contains("priority_port_b")) {
						formattedTextFieldsACNPriorityPortB.setText(Properties.getString(line));
					}
					
					if (line.contains("priority_port_c")) {
						formattedTextFieldsACNPriorityPortC.setText(Properties.getString(line));
					}
					
					if (line.contains("priority_port_d")) {
						formattedTextFieldsACNPriorityPortD.setText(Properties.getString(line));
					}
					
					if (line.contains("map_universe0")) {
						chckbxMapUniverse0.setSelected(Properties.getBool(line));
						continue;
					}
				}
			}
						
			handleComboBoxDirectionPort(comboBoxDirectionPortA, comboBoxMergePortA, comboBoxOutputStylePortA, hasOutputStyleA, comboBoxProtocolPortA,formattedTextFieldsACNPriorityPortA, chckbxRdmEnablePortA);
			handleComboBoxDirectionPort(comboBoxDirectionPortB, comboBoxMergePortB, comboBoxOutputStylePortB, hasOutputStyleB, comboBoxProtocolPortB,formattedTextFieldsACNPriorityPortB, chckbxRdmEnablePortB);
			handleComboBoxDirectionPort(comboBoxDirectionPortC, comboBoxMergePortC, comboBoxOutputStylePortC, hasOutputStyleC, comboBoxProtocolPortC,formattedTextFieldsACNPriorityPortC, chckbxRdmEnablePortC);
			handleComboBoxDirectionPort(comboBoxDirectionPortD, comboBoxMergePortD, comboBoxOutputStylePortD, hasOutputStyleD, comboBoxProtocolPortD,formattedTextFieldsACNPriorityPortD, chckbxRdmEnablePortD);
		
			if (hasUniverseA) {
				updateDestinationIp('A');
			} else {
				formattedTextFieldUniverseA.setEnabled(false);
				comboBoxMergePortA.setEnabled(false);
				comboBoxProtocolPortA.setEnabled(false);
				comboBoxDirectionPortA.setEnabled(false);
				formattedTextFieldsACNPriorityPortA.setEnabled(false);
			}

			if (hasUniverseB) {
				updateDestinationIp('B');
			} else {
				formattedTextFieldUniverseB.setEnabled(false);
				comboBoxMergePortB.setEnabled(false);
				comboBoxProtocolPortB.setEnabled(false);
				comboBoxDirectionPortB.setEnabled(false);
				formattedTextFieldsACNPriorityPortB.setEnabled(false);
			}

			if (hasUniverseC) {
				updateDestinationIp('C');
			} else {
				formattedTextFieldUniverseC.setEnabled(false);
				comboBoxMergePortC.setEnabled(false);
				comboBoxProtocolPortC.setEnabled(false);
				comboBoxDirectionPortC.setEnabled(false);
				formattedTextFieldsACNPriorityPortC.setEnabled(false);
			}

			if (hasUniverseD) {
				updateDestinationIp('D');
			}  else {
				formattedTextFieldUniverseD.setEnabled(false);
				comboBoxMergePortD.setEnabled(false);
				comboBoxProtocolPortD.setEnabled(false);
				comboBoxDirectionPortD.setEnabled(false);
				formattedTextFieldsACNPriorityPortD.setEnabled(false);
			}
			
			if (!hasOutputStyleA) {
				comboBoxOutputStylePortA.setSelectedIndex(1);
			}
			
			if (!hasOutputStyleB) {
				comboBoxOutputStylePortB.setSelectedIndex(1);
			}
			
			if (!hasOutputStyleC) {
				comboBoxOutputStylePortC.setSelectedIndex(1);
			}
			
			if (!hasOutputStyleD) {
				comboBoxOutputStylePortD.setSelectedIndex(1);
			}

			chckbxEnableRDM.setEnabled(hasRDM);
			chckbxRdmEnablePortA.setEnabled(hasRDM);
			chckbxRdmEnablePortB.setEnabled(hasRDM);
			chckbxRdmEnablePortC.setEnabled(hasRDM);
			chckbxRdmEnablePortD.setEnabled(hasRDM);
		}
	}
	
	private boolean setIpaddress(String ipAdress, JFormattedTextField IP1, JFormattedTextField IP2, JFormattedTextField IP3, JFormattedTextField IP4) {
		final String parts[] = ipAdress.replace('.', '\n').split("\n");
		
		if (parts.length == 4) {
			IP1.setValue(Integer.parseInt(parts[0]));
			IP2.setValue(Integer.parseInt(parts[1]));
			IP3.setValue(Integer.parseInt(parts[2]));
			IP4.setValue(Integer.parseInt(parts[3]));
			return true;
		}
		return false;
	}
	
	private String getIpaddress(JFormattedTextField IP1, JFormattedTextField IP2, JFormattedTextField IP3, JFormattedTextField IP4) {
		return IP1.getText() + "." + IP2.getText() + "." + IP3.getText() + "." + IP4.getText();
	}
	
	private int setNetSubNet(int universe) {
		return universe;
	}
	
	private int getUniverseFromAddress(int address) {
		return address;
	}
		
	private String getComment(JComboBox<String> comboBox) {
		return comboBox.getSelectedItem().toString() == "Input" ? "" : "#";
	}
	
	private void updateDestinationIp(char port) {		
		if ((port == 'A') && hasDestinationIpA) {						// Port A
			if ((comboBoxDirectionPortA.getSelectedIndex() == 1) || (comboBoxDirectionPortA.getSelectedIndex() == 2)) { 		// Input or Disabled
				if (comboBoxProtocolPortA.getSelectedIndex() == 0) {	// Art-Net
					setIpaddress(ipAddressA, formattedIP1PortA, formattedIP2PortA, formattedIP3PortA, formattedIP4PortA);
					formattedIP1PortA.setEnabled(true);
					formattedIP2PortA.setEnabled(true);
					formattedIP3PortA.setEnabled(true);
					formattedIP4PortA.setEnabled(true);
				} else {												// sACN
					formattedIP1PortA.setEnabled(false);
					formattedIP2PortA.setEnabled(false);
					formattedIP3PortA.setEnabled(false);
					formattedIP4PortA.setEnabled(false);
					final int universe = getUniverseFromAddress(artnetAdressPortA);
					formattedIP1PortA.setText("239");
					formattedIP2PortA.setText("255");
					formattedIP3PortA.setText(String.valueOf((universe >> 8) & 0xFF));
					formattedIP4PortA.setText(String.valueOf(universe & 0xFF));
				}
				return;
			} else {
				formattedIP1PortA.setEnabled(false);
				formattedIP2PortA.setEnabled(false);
				formattedIP3PortA.setEnabled(false);
				formattedIP4PortA.setEnabled(false);
			}
			return;
		}
		
		if ((port == 'B') && hasDestinationIpB) {
			if ((comboBoxDirectionPortB.getSelectedIndex() == 1) || (comboBoxDirectionPortB.getSelectedIndex() == 2)) { 		// Input or Disabled
				if (comboBoxProtocolPortB.getSelectedIndex() == 0) {	// Art-Net
					setIpaddress(ipAddressB, formattedIP1PortB, formattedIP2PortB, formattedIP3PortB, formattedIP4PortB);
					formattedIP1PortB.setEnabled(true);
					formattedIP2PortB.setEnabled(true);
					formattedIP3PortB.setEnabled(true);
					formattedIP4PortB.setEnabled(true);
				} else {												// sACN
					formattedIP1PortB.setEnabled(false);
					formattedIP2PortB.setEnabled(false);
					formattedIP3PortB.setEnabled(false);
					formattedIP4PortB.setEnabled(false);
					final int universe = getUniverseFromAddress(artnetAdressPortB);
					formattedIP1PortB.setText("239");
					formattedIP2PortB.setText("255");
					formattedIP3PortB.setText(String.valueOf((universe >> 8) & 0xFF));
					formattedIP4PortB.setText(String.valueOf(universe & 0xFF));
				}
				return;
			} else {
				formattedIP1PortB.setEnabled(false);
				formattedIP2PortB.setEnabled(false);
				formattedIP3PortB.setEnabled(false);
				formattedIP4PortB.setEnabled(false);	
			}
			return;
		}
		
		if ((port == 'C') && hasDestinationIpC) {
			if ((comboBoxDirectionPortC.getSelectedIndex() == 1) || (comboBoxDirectionPortC.getSelectedIndex() == 2)) { 		// Input or Disabled
				if (comboBoxProtocolPortC.getSelectedIndex() == 0) {	// Art-Net
					setIpaddress(ipAddressC, formattedIP1PortC, formattedIP2PortC, formattedIP3PortC, formattedIP4PortC);
					formattedIP1PortC.setEnabled(true);
					formattedIP2PortC.setEnabled(true);
					formattedIP3PortC.setEnabled(true);
					formattedIP4PortC.setEnabled(true);
				} else {												// sACN
					formattedIP1PortC.setEnabled(false);
					formattedIP2PortC.setEnabled(false);
					formattedIP3PortC.setEnabled(false);
					formattedIP4PortC.setEnabled(false);
					final int universe = getUniverseFromAddress(artnetAdressPortC);
					formattedIP1PortC.setText("239");
					formattedIP2PortC.setText("255");
					formattedIP3PortC.setText(String.valueOf((universe >> 8) & 0xFF));
					formattedIP4PortC.setText(String.valueOf(universe & 0xFF));
				}
				return;
			} else {
				formattedIP1PortC.setEnabled(false);
				formattedIP2PortC.setEnabled(false);
				formattedIP3PortC.setEnabled(false);
				formattedIP4PortC.setEnabled(false);
			}
			return;
		}
		
		if ((port == 'D') && hasDestinationIpD) {
			if ((comboBoxDirectionPortD.getSelectedIndex() == 1) || (comboBoxDirectionPortD.getSelectedIndex() == 2)) { 		// Input or Disabled
				if (comboBoxProtocolPortD.getSelectedIndex() == 0) {	// Art-Net
					setIpaddress(ipAddressD, formattedIP1PortD, formattedIP2PortD, formattedIP3PortD, formattedIP4PortD);
					formattedIP1PortD.setEnabled(true);
					formattedIP2PortD.setEnabled(true);
					formattedIP3PortD.setEnabled(true);
					formattedIP4PortD.setEnabled(true);
				} else {												// sACN
					formattedIP1PortD.setEnabled(false);
					formattedIP2PortD.setEnabled(false);
					formattedIP3PortD.setEnabled(false);
					formattedIP4PortD.setEnabled(false);
					final int universe = getUniverseFromAddress(artnetAdressPortD);
					formattedIP1PortD.setText("239");
					formattedIP2PortD.setText("255");
					formattedIP3PortD.setText(String.valueOf((universe >> 8) & 0xFF));
					formattedIP4PortD.setText(String.valueOf(universe & 0xFF));
				}
				return;
			} else {
				formattedIP1PortD.setEnabled(false);
				formattedIP2PortD.setEnabled(false);
				formattedIP3PortD.setEnabled(false);
				formattedIP4PortD.setEnabled(false);
			}
			return;
		}
	}
	
	private void eventUniverse(char port) {
		updateDestinationIp(port);
		
		if (port != 'A') {
			formattedTextFieldUniverseA.setValue(getUniverseFromAddress(artnetAdressPortA));
		}
		if (port != 'B') {
			formattedTextFieldUniverseB.setValue(getUniverseFromAddress(artnetAdressPortB));
		}
		if (port != 'C') {
			formattedTextFieldUniverseC.setValue(getUniverseFromAddress(artnetAdressPortC));
		}
		if (port != 'D') {
			formattedTextFieldUniverseD.setValue(getUniverseFromAddress(artnetAdressPortD));
		}
	}
		
	private String getProtocol(JComboBox<String> comboBox) {
		if (comboBox.getSelectedItem().toString().toLowerCase().equals("sacn")) {
			return "sacn";
		}
		return "artnet";
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
			StringBuffer txtAppend = new StringBuffer("#" + TXT_FILE + "\n");

			txtAppend.append(String.format("failsafe=%s\n", getFailsafe()));
			txtAppend.append(String.format("map_universe0=%d\n", chckbxMapUniverse0.isSelected() ? 1 : 0));
			
			if (hasRDM) {
				txtAppend.append(String.format("enable_rdm=%d\n", chckbxEnableRDM.isSelected() ? 1 : 0));
			}

			txtAppend.append(String.format("long_name=%.63s\n", textFieldLongName.getText()));
					
			if (hasUniverseA) {
				txtAppend.append(String.format("universe_port_a=%s\n", artnetAdressPortA));
				txtAppend.append(String.format("label_port_a=%.17s\n", textFieldLabelPortA.getText()));

				if (comboBoxDirectionPortA.isEnabled()) {
					txtAppend.append(String.format("direction_port_a=%s\n", LightSet.getDirection(comboBoxDirectionPortA)));
				}
				
				txtAppend.append(String.format("protocol_port_a=%s\n", getProtocol(comboBoxProtocolPortA)));
				txtAppend.append(String.format("merge_mode_port_a=%s\n", LightSet.getMergeMode(comboBoxMergePortA)));
				
				if (hasOutputStyleA) {
					txtAppend.append(String.format("output_style_a=%s\n", LightSet.getOutputStyle(comboBoxOutputStylePortA)));
				}
				
				if (formattedIP1PortA.isEnabled() && (getComment(comboBoxDirectionPortA).trim().length() == 0)) {
					txtAppend.append(String.format("destination_ip_port_a=%d.%d.%d.%d\n",formattedIP1PortA.getValue(),formattedIP2PortA.getValue(),formattedIP3PortA.getValue(),formattedIP4PortA.getValue()));
					ipAddressA = getIpaddress(formattedIP1PortA, formattedIP2PortA, formattedIP3PortA, formattedIP4PortA);
				}
				
				if (hasRDM) {
					txtAppend.append(String.format("rdm_enable_port_a=%s\n", chckbxRdmEnablePortA.isSelected() ? 1 : 0));
				}
				
				if (hasPriority) {
					txtAppend.append(String.format("priority_port_a=%s\n", formattedTextFieldsACNPriorityPortA.getText()));
				}
			}	
			
			if (hasUniverseB) {
				txtAppend.append(String.format("universe_port_b=%s\n", artnetAdressPortB));
				txtAppend.append(String.format("label_port_b=%.17s\n", textFieldLabelPortB.getText()));
				
				if (comboBoxDirectionPortB.isEnabled()) {
					txtAppend.append(String.format("direction_port_b=%s\n", LightSet.getDirection(comboBoxDirectionPortB)));
				}
				
				txtAppend.append(String.format("protocol_port_b=%s\n", getProtocol(comboBoxProtocolPortB)));
				txtAppend.append(String.format("merge_mode_port_b=%s\n", LightSet.getMergeMode(comboBoxMergePortB)));
				
				if (hasOutputStyleB) {
					txtAppend.append(String.format("output_style_b=%s\n", LightSet.getOutputStyle(comboBoxOutputStylePortB)));
				}
				
				if (formattedIP1PortB.isEnabled() && (getComment(comboBoxDirectionPortB).trim().length() == 0)) {
					txtAppend.append(String.format("destination_ip_port_b=%d.%d.%d.%d\n", formattedIP1PortB.getValue(),formattedIP2PortB.getValue(),formattedIP3PortB.getValue(),formattedIP4PortB.getValue()));
					ipAddressB = getIpaddress(formattedIP1PortA, formattedIP2PortB, formattedIP3PortB, formattedIP4PortB);
				}
				
				if (hasRDM) {
					txtAppend.append(String.format("rdm_enable_port_b=%s\n", chckbxRdmEnablePortB.isSelected() ? 1 : 0));
				}
				
				if (hasPriority) {
					txtAppend.append(String.format("priority_port_b=%s\n", formattedTextFieldsACNPriorityPortB.getText()));
				}
			}
			
			if (hasUniverseC) {
				txtAppend.append(String.format("universe_port_c=%s\n", artnetAdressPortC));
				txtAppend.append(String.format("label_port_c=%.17s\n", textFieldLabelPortC.getText()));
				
				if (comboBoxDirectionPortC.isEnabled()) {
					txtAppend.append(String.format("direction_port_c=%s\n", LightSet.getDirection(comboBoxDirectionPortC)));
				}
				
				txtAppend.append(String.format("protocol_port_c=%s\n", getProtocol(comboBoxProtocolPortC)));
				txtAppend.append(String.format("merge_mode_port_c=%s\n", LightSet.getMergeMode(comboBoxMergePortC)));
				
				if (hasOutputStyleC) {
					txtAppend.append(String.format("output_style_c=%s\n", LightSet.getOutputStyle(comboBoxOutputStylePortC)));
				}
				
				if (formattedIP1PortC.isEnabled() && (getComment(comboBoxDirectionPortC).trim().length() == 0)) {
					txtAppend.append(String.format("destination_ip_port_c=%d.%d.%d.%d\n", formattedIP1PortC.getValue(),formattedIP2PortC.getValue(),formattedIP3PortC.getValue(),formattedIP4PortC.getValue()));
					ipAddressC = getIpaddress(formattedIP1PortC, formattedIP2PortC, formattedIP3PortC, formattedIP4PortC);
				}
				
				
				if (hasRDM) {
					txtAppend.append(String.format("rdm_enable_port_c=%s\n", chckbxRdmEnablePortC.isSelected() ? 1 : 0));
				}
				
				if (hasPriority) {
					txtAppend.append(String.format("priority_port_c=%s\n", formattedTextFieldsACNPriorityPortC.getText()));
				}
			}
			
			if (hasUniverseD) {
				txtAppend.append(String.format("universe_port_d=%s\n", artnetAdressPortD));
				txtAppend.append(String.format("label_port_d=%.17s\n", textFieldLabelPortD.getText()));
				
				if (comboBoxDirectionPortD.isEnabled()) {
					txtAppend.append(String.format("direction_port_d=%s\n", LightSet.getDirection(comboBoxDirectionPortD)));
				}
				
				txtAppend.append(String.format("protocol_port_d=%s\n", getProtocol(comboBoxProtocolPortD)));
				txtAppend.append(String.format("merge_mode_port_d=%s\n", LightSet.getMergeMode(comboBoxMergePortD)));
				
				if (hasOutputStyleD) {
					txtAppend.append(String.format("output_style_d=%s\n", LightSet.getOutputStyle(comboBoxOutputStylePortD)));
				}
				
				if (formattedIP1PortD.isEnabled() && (getComment(comboBoxDirectionPortD).trim().length() == 0)) {
					txtAppend.append(String.format("destination_ip_port_d=%d.%d.%d.%d\n", formattedIP1PortD.getValue(),formattedIP2PortD.getValue(),formattedIP3PortD.getValue(),formattedIP4PortD.getValue()));
					ipAddressD = getIpaddress(formattedIP1PortD, formattedIP2PortD, formattedIP3PortD, formattedIP4PortD);
				}
				
				if (hasRDM) {
					txtAppend.append(String.format("rdm_enable_port_d=%s\n", chckbxRdmEnablePortD.isSelected() ? 1 : 0));
				}
				
				if (hasPriority) {
					txtAppend.append(String.format("priority_port_d=%s\n", formattedTextFieldsACNPriorityPortD.getText()));
				}
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
