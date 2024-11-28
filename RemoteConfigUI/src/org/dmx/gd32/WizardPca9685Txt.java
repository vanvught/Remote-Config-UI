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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.awt.Color;

public class WizardPca9685Txt extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final String TXT_FILE = "pca9685.txt";
	
	private static final int SERVO_LEFT_DEFAULT_US = 1000;
	private static final int SERVO_CENTER_DEFAULT_US = 1500;
	private static final int SERVO_RIGHT_DEFAULT_US = 2000;
	
	OrangePi opi = null;
	RemoteConfig remoteConfig = null;
	String txt = null;
	
	private final JPanel contentPanel = new JPanel();
	
	private JLabel lblChannels;
	private JLabel lblDMXStartAddress;
	private JLabel lblFootprint;
	private JLabel lblLed;
	private JLabel lblPwmFrequency;
	private JLabel lblLeftus;
	private JLabel lblCenterus;
	private JLabel lblRightUs;
	
	private JButton btnSetDefaults;
	private JButton btnSave;
	private JButton btnCancel;
	
	private JComboBox<String> comboBoxMode;

	private JCheckBox chckbxUse8bit;
	private JCheckBox chckbxOutputInvert;
	private JCheckBox chckbxOutputOpendrain;
	
	private JTextField textFieldFootprint;

	private JFormattedTextField formattedTextFieldChannels;
	private JFormattedTextField formattedTextFieldPwmFrequency;
	private JFormattedTextField formattedTextFieldDmxStartAddress;
	
	private JFormattedTextField formattedTextFieldCenterus;
	private JFormattedTextField formattedTextFieldLeftus;
	private JFormattedTextField formattedTextFieldRightus;
	
	private NumberFormatter formatterServoLeft;
	private NumberFormatter formatterServoCenter;
	private NumberFormatter formatterServoRight;

	public static void main(String[] args) {
		try {
			WizardPca9685Txt dialog = new WizardPca9685Txt();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public WizardPca9685Txt() {
		setTitle("PWM");
		initComponents();
		createEvents();
	}
	
	public WizardPca9685Txt(String nodeId, OrangePi opi, RemoteConfig remoteConfig) {
		this.opi = opi;
		this.remoteConfig = remoteConfig;

		setTitle(nodeId);

		initComponents();
		createEvents();

		load();
	}
	
	private void initComponents() {
		setBounds(100, 100, 470, 360);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblMode = new JLabel("Mode");
		
		comboBoxMode = new JComboBox<String>();
		comboBoxMode.setModel(new DefaultComboBoxModel<String> (new String[] {"Led", "Servo"}));
		comboBoxMode.setSelectedIndex(0);
		lblChannels = new JLabel("Channels");
		
		NumberFormat format = NumberFormat.getInstance();
		format.setGroupingUsed(false);
		format.setMinimumIntegerDigits(0);
		
		NumberFormatter formatterChannels = new NumberFormatter(format);
		formatterChannels.setValueClass(Integer.class);
		formatterChannels.setMinimum(1);
		formatterChannels.setMaximum(512);
		formatterChannels.setAllowsInvalid(false);
		formatterChannels.setCommitsOnValidEdit(true);
		
		formattedTextFieldChannels = new JFormattedTextField(formatterChannels);

		chckbxUse8bit = new JCheckBox("Use 8-bit");

		lblPwmFrequency = new JLabel("PWM Frequency");
		
		NumberFormatter formatterPwmFrequency = new NumberFormatter(format);
		formatterPwmFrequency.setValueClass(Integer.class);
		formatterPwmFrequency.setMinimum(24);
		formatterPwmFrequency.setMaximum(1526);
		formatterPwmFrequency.setAllowsInvalid(false);
		formatterPwmFrequency.setCommitsOnValidEdit(true);
		
		formattedTextFieldPwmFrequency = new JFormattedTextField(formatterPwmFrequency);
		
		chckbxOutputInvert = new JCheckBox("Output Invert");
		chckbxOutputOpendrain = new JCheckBox("Output open-drain");
		
		lblLeftus = new JLabel("Left μs");
		lblCenterus = new JLabel("Center μs");
		lblRightUs = new JLabel("Right μs");
		
		JSeparator separatorServo = new JSeparator();
		
		JLabel lblServo = new JLabel("Servo");
		
		JSeparator separator_Led = new JSeparator();
		
		lblLed = new JLabel("Led");
		
		lblDMXStartAddress = new JLabel("DMX Start address");
		
		formattedTextFieldDmxStartAddress = new JFormattedTextField();
		formattedTextFieldDmxStartAddress.setColumns(4);
		
		formatterServoLeft = new NumberFormatter(format);
		formatterServoLeft.setValueClass(Integer.class);
		formatterServoLeft.setAllowsInvalid(false);
		formatterServoLeft.setCommitsOnValidEdit(true);
		
		formatterServoCenter = new NumberFormatter(format);
		formatterServoCenter.setValueClass(Integer.class);
		formatterServoCenter.setAllowsInvalid(false);
		formatterServoCenter.setCommitsOnValidEdit(true);
		
		formatterServoRight = new NumberFormatter(format);
		formatterServoRight.setValueClass(Integer.class);
		formatterServoRight.setAllowsInvalid(false);
		formatterServoRight.setCommitsOnValidEdit(true);
		
		formattedTextFieldLeftus = new JFormattedTextField(formatterServoLeft);
		formattedTextFieldLeftus.setValue(SERVO_LEFT_DEFAULT_US);
		formattedTextFieldCenterus = new JFormattedTextField(formatterServoCenter);
		formattedTextFieldCenterus.setValue(SERVO_CENTER_DEFAULT_US);
		formattedTextFieldRightus = new JFormattedTextField(formatterServoRight);
		formattedTextFieldRightus.setValue(SERVO_RIGHT_DEFAULT_US);
		
		lblFootprint = new JLabel("Foot print");
		
		textFieldFootprint = new JTextField();
		textFieldFootprint.setBackground(new Color(192, 192, 192));
		textFieldFootprint.setEditable(false);
		textFieldFootprint.setColumns(4);
	
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblRightUs)
								.addComponent(lblCenterus)
								.addComponent(lblLeftus))
							.addGap(10)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(formattedTextFieldRightus, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldCenterus, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedTextFieldLeftus, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)))
						.addComponent(lblLed)
						.addComponent(lblServo)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(separatorServo, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblPwmFrequency)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(formattedTextFieldPwmFrequency, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxOutputInvert)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxOutputOpendrain))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(separator_Led, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblDMXStartAddress)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(formattedTextFieldDmxStartAddress, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblFootprint)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textFieldFootprint, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblMode)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBoxMode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblChannels)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(formattedTextFieldChannels, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxUse8bit)))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(7)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(4)
							.addComponent(lblMode))
						.addComponent(comboBoxMode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(4)
							.addComponent(lblChannels))
						.addComponent(formattedTextFieldChannels, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(2)
							.addComponent(chckbxUse8bit)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDMXStartAddress)
						.addComponent(formattedTextFieldDmxStartAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblFootprint)
						.addComponent(textFieldFootprint, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(10)
					.addComponent(separator_Led, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblLed)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(formattedTextFieldPwmFrequency, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(2)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(chckbxOutputInvert)
								.addComponent(chckbxOutputOpendrain)))
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(4)
							.addComponent(lblPwmFrequency)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separatorServo, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblServo)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLeftus)
						.addComponent(formattedTextFieldLeftus, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCenterus)
						.addComponent(formattedTextFieldCenterus, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblRightUs)
						.addComponent(formattedTextFieldRightus, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(25))
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
		
		comboBoxMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setMode(comboBoxMode.getSelectedIndex() == 1);
			}
		});
		
		formattedTextFieldLeftus.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					final int leftus = Integer.parseInt(formattedTextFieldLeftus.getText());
					final int centerus = Integer.parseInt(formattedTextFieldCenterus.getText());
					final int rightus = Integer.parseInt(formattedTextFieldRightus.getText());
					if (!((leftus < rightus) && (leftus < centerus))) {
						formattedTextFieldLeftus.setValue(centerus);
					}
				} catch (Exception e2) {
				}				
			}
		});
		
		formattedTextFieldCenterus.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					final int leftus = Integer.parseInt(formattedTextFieldLeftus.getText());
					final int centerus = Integer.parseInt(formattedTextFieldCenterus.getText());
					final int rightus = Integer.parseInt(formattedTextFieldRightus.getText());
					if (!((centerus < rightus) && (leftus < centerus))) {
						formattedTextFieldCenterus.setValue(leftus);
					}
				} catch (Exception e2) {
				}				
			}
		});
		
		formattedTextFieldRightus.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					final int leftus = Integer.parseInt(formattedTextFieldLeftus.getText());
					final int centerus = Integer.parseInt(formattedTextFieldCenterus.getText());
					final int rightus = Integer.parseInt(formattedTextFieldRightus.getText());
					if (!((leftus < rightus) && (centerus < rightus))) {
						formattedTextFieldRightus.setValue(centerus);
					}
				} catch (Exception e2) {
				}				
			}
		});
		
		formattedTextFieldChannels.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				calulateFootprint();
			}
		});
		
		chckbxUse8bit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calulateFootprint();
			}
		});
		
		
	}
	
	private void setMode(boolean isServo) {
		if (isServo) {
			formattedTextFieldPwmFrequency.setEnabled(false);
			chckbxOutputInvert.setEnabled(false);
			chckbxOutputOpendrain.setEnabled(false);
			formattedTextFieldLeftus.setEnabled(true);
			formattedTextFieldCenterus.setEnabled(true);
			formattedTextFieldRightus.setEnabled(true);
		} else {
			formattedTextFieldPwmFrequency.setEnabled(true);
			chckbxOutputInvert.setEnabled(true);
			chckbxOutputOpendrain.setEnabled(true);
			formattedTextFieldLeftus.setEnabled(false);
			formattedTextFieldCenterus.setEnabled(false);
			formattedTextFieldRightus.setEnabled(false);
		}
	}
	
	private void calulateFootprint() {
		if (chckbxUse8bit.isSelected()) {
			textFieldFootprint.setText(formattedTextFieldChannels.getText());
		} else {
			try {
				int channels = Integer.parseInt(formattedTextFieldChannels.getText());
				if (channels > 256) {
					channels = 256;
					formattedTextFieldChannels.setText("256");
				}
				final int footprint = channels * 2;
				textFieldFootprint.setText(String.valueOf(footprint));
			} catch (Exception e) {
			}
		}
	}
	
	private void load() {
		if (opi != null) {
			comboBoxMode.setSelectedIndex(0);
			
			txt = opi.getTxt(TXT_FILE);
			System.out.println(">|" + txt + "|<");
			if (txt != null) {
				final String[] lines = txt.split("\n");
				for (int i = 0; i < lines.length; i++) {
					final String line = lines[i];
					
					if ((!line.startsWith("#")) && (line.contains("mode"))) {
						if (Properties.getString(line).equals("servo")) {
							System.out.println("Servo");
							comboBoxMode.setSelectedIndex(1);
						}
						continue;
					}
					
					if (line.contains("channel_count")) {
						formattedTextFieldChannels.setValue(Properties.getInt(line));
						continue;
					}
					
					if (line.contains("dmx_start_address")) {
						formattedTextFieldDmxStartAddress.setValue(Properties.getInt(line));
						continue;
					}
					
					if (line.contains("use_8bit")) {
						chckbxUse8bit.setSelected(Properties.getBool(line));
						continue;
					}
					
					// Led
					
					if (line.contains("led_pwm_frequency")) {
						formattedTextFieldPwmFrequency.setValue(Properties.getInt(line));
						continue;
					}
					
					if (line.contains("led_output_invert")) {
						chckbxOutputInvert.setSelected(Properties.getBool(line));
						continue;
					}
					
					if (line.contains("led_output_opendrain")) {
						chckbxOutputOpendrain.setSelected(Properties.getBool(line));
						continue;
					}
					
					// Servo
					
					if (line.contains("servo_left_us")) {
						formattedTextFieldLeftus.setValue(Properties.getInt(line));
						continue;
					}
					
					if (line.contains("servo_center_us")) {
						formattedTextFieldCenterus.setValue(Properties.getInt(line));
						continue;
					}
					
					if (line.contains("servo_right_us")) {
						formattedTextFieldRightus.setValue(Properties.getInt(line));
						continue;
					}
				}
			}
		}
		
		setMode(comboBoxMode.getSelectedIndex() == 1);
		calulateFootprint();
	}
	
	private void save() {
		if (opi != null) {
			StringBuffer txtFile = new StringBuffer("#" + TXT_FILE + "\n");
			
			final int modeIndex = comboBoxMode.getSelectedIndex();
			
			txtFile.append(String.format("mode=%s\n", modeIndex == 0 ? "led" : "servo"));
			txtFile.append(String.format("channel_count=%s\n", formattedTextFieldChannels.getText()));
			txtFile.append(String.format("dmx_start_address=%s\n", formattedTextFieldDmxStartAddress.getText()));
			txtFile.append(String.format("use_8bit=%d\n", chckbxUse8bit.isSelected() ? 1 : 0));
			
			if (modeIndex == 0) {
				txtFile.append(String.format("led_pwm_frequency=%d\n", formattedTextFieldPwmFrequency.getValue()));
				txtFile.append(String.format("led_output_invert=%d\n", chckbxOutputInvert.isSelected() ? 1 : 0));
				txtFile.append(String.format("led_output_opendrain=%d\n", chckbxOutputOpendrain.isSelected() ? 1 : 0));
			} else {
				txtFile.append(String.format("servo_left_us=%d\n" , formattedTextFieldLeftus.getValue()));
				txtFile.append(String.format("servo_center_us=%d\n" ,formattedTextFieldCenterus.getValue()));
				txtFile.append(String.format("servo_right_us=%d\n" ,formattedTextFieldRightus.getValue()));
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
