/* Copyright (C) 2021-2024 by Arjan van Vught mailto:info@gd32-dmx.org
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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import javax.swing.JCheckBox;

public class WizardDevicesTxt extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final String TXT_FILE = "devices.txt";

	private final JPanel contentPanel = new JPanel();
	
	OrangePi opi = null;
	RemoteConfig remoteConfig = null;
	String txt = null;
	
	private JButton btnCancel;
	private JButton btnSave;
	private int ledsPerPixel = 3;
	private int maxPorts = 1;
	private JLabel lblCount;
	private NumberFormatter formatterCount;
	private JFormattedTextField formattedTextFieldPixelCount;
	private JLabel lblStartUniverse;
	private boolean doDisableStartUniverse = false;

	private JLabel lblPort1;
	private JLabel lblPort2;
	private JLabel lblPort3;
	private JLabel lblPort4;
	private JLabel lblPort5;
	private JLabel lblPort6;
	private JLabel lblPort7;
	private JLabel lblPort8;
	private JLabel lblPort9;
	private JLabel lblPort10;
	private JLabel lblPort11;
	private JLabel lblPort12;
	private JLabel lblPort13;
	private JLabel lblPort14;
	private JLabel lblPort15;
	private JLabel lblPort16;
	
	private JLabel lblUniversePort1;
	private JLabel lblUniversePort2;
	private JLabel lblUniversePort3;
	private JLabel lblUniversePort4;
	private JLabel lblUniversePort5;
	private JLabel lblUniversePort6;
	private JLabel lblUniversePort7;
	private JLabel lblUniversePort8;
	private JLabel lblUniversePort9;
	private JLabel lblUniversePort10;
	private JLabel lblUniversePort11;
	private JLabel lblUniversePort12;
	private JLabel lblUniversePort13;
	private JLabel lblUniversePort14;
	private JLabel lblUniversePort15;
	private JLabel lblUniversePort16;
	
	private NumberFormatter formatterStartUniverse;
	private JFormattedTextField formattedTextFieldStartUniverse;
	private JSpinner spinnerActiveOutput;
	private JLabel lblUniversesPerPort;
	private JLabel lblProtocol;
	private JButton btnSetDefaults;
	private JFormattedTextField formattedTextFieldGroupSize;
	private JComboBox<String> comboBoxType;
	private JComboBox<String> comboBoxMap;
	private JComboBox<String> comboBoxTestPattern;
	private JCheckBox chckbxGammaCorrection;
	private JComboBox<String> comboBoxGammaValue;

	public WizardDevicesTxt(String nodeId, OrangePi opi, RemoteConfig remoteConfig) {
		this.opi = opi;
		this.remoteConfig = remoteConfig;
		
		setTitle(nodeId);
		
		InitComponents();
		CreateEvents();
		
		formatterStartUniverse.setMinimum(1);
		
		if (nodeId.toLowerCase().contains("art")) {
			lblProtocol.setText("Art-Net 4");
			formatterStartUniverse.setMinimum(0);
		}
		
		
		if (nodeId.toLowerCase().contains("ddp")) {
			lblProtocol.setText("DDP Display");
			doDisableStartUniverse = true;
		}
		
		if (nodeId.toLowerCase().contains("pusher")) {
			lblProtocol.setText("PixelPusher");
			doDisableStartUniverse = true;
		}
		
		if (doDisableStartUniverse) {
			formatterStartUniverse.setMinimum(-1);
			formattedTextFieldStartUniverse.setValue(-1);
			formattedTextFieldStartUniverse.setEnabled(false);
			formattedTextFieldStartUniverse.setVisible(false);
			lblStartUniverse.setText("");
		}
		
		load();
	}
	
	private void InitComponents() {
		setBounds(100, 100, 330, 660);
	
		JLabel lblType = new JLabel("Type / Mapping");
		
		lblCount = new JLabel("Pixel count");
		
		spinnerActiveOutput = new JSpinner(new SpinnerNumberModel(1, 1, 16, 1));

		JLabel lblOutputs = new JLabel("Outputs");
		
	    NumberFormat format = NumberFormat.getInstance();
	    formatterCount = new NumberFormatter(format);
	    formatterCount.setValueClass(Integer.class);
	    formatterCount.setMinimum(1);
	    formatterCount.setMaximum(4 * (512 / ledsPerPixel));
	    formatterCount.setAllowsInvalid(false);
	    formatterCount.setCommitsOnValidEdit(true);
		
		formattedTextFieldPixelCount = new JFormattedTextField(formatterCount);
		formattedTextFieldPixelCount.setText("170");
		
		lblStartUniverse = new JLabel("Start Universe");
		
		lblPort1 = new JLabel("Port 1 ");
		lblPort2 = new JLabel("Port 2 ");
		lblPort3 = new JLabel("Port 3 ");
		lblPort4 = new JLabel("Port 4 ");
		lblPort5 = new JLabel("Port 5 ");
		lblPort6 = new JLabel("Port 6 ");
		lblPort7 = new JLabel("Port 7 ");
		lblPort8 = new JLabel("Port 8 ");
		//
		lblPort9 =  new JLabel("Port 9 ");
		lblPort10 = new JLabel("Port 10");
		lblPort11 = new JLabel("Port 11");
		lblPort12 = new JLabel("Port 12");
		lblPort13 = new JLabel("Port 13");
		lblPort14 = new JLabel("Port 14");
		lblPort15 = new JLabel("Port 15");
		lblPort16 = new JLabel("Port 16");
		
		lblUniversePort1 = new JLabel("");
		lblUniversePort2 = new JLabel("");		
		lblUniversePort3 = new JLabel("");		
		lblUniversePort4 = new JLabel("");	
		lblUniversePort5 = new JLabel("");	
		lblUniversePort6 = new JLabel("");	
		lblUniversePort7 = new JLabel("");
		lblUniversePort8 = new JLabel("");
		//
		lblUniversePort9  = new JLabel("");
		lblUniversePort10 = new JLabel("");		
		lblUniversePort11 = new JLabel("");		
		lblUniversePort12 = new JLabel("");	
		lblUniversePort13 = new JLabel("");	
		lblUniversePort14 = new JLabel("");	
		lblUniversePort15 = new JLabel("");
		lblUniversePort16 = new JLabel("");
		
		formatterStartUniverse = new NumberFormatter(format);
		formatterStartUniverse.setValueClass(Integer.class);
		formatterStartUniverse.setMinimum(0);
		formatterStartUniverse.setMaximum(32767);
		formatterStartUniverse.setAllowsInvalid(false);
		formatterStartUniverse.setCommitsOnValidEdit(true);
		
		formattedTextFieldStartUniverse = new JFormattedTextField(formatterStartUniverse);
		formattedTextFieldStartUniverse.setText("1");
		
		JLabel lblGroupSize = new JLabel("Group size");
		
		formattedTextFieldGroupSize = new JFormattedTextField(formatterCount);
		formattedTextFieldGroupSize.setText("1");
		
		lblUniversesPerPort = new JLabel("");
		lblProtocol = new JLabel("sACN E1.31");
	
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		comboBoxType = new JComboBox<String> ();
		comboBoxType.setModel(new DefaultComboBoxModel<String>(new String[] {"WS2801", "WS2811", "WS2812", "WS2812B", "WS2813", "WS2815", "SK6812", "SK6812W", "UCS1903", "UCS2903", "CS8812", "APA102", "SK9822", "P9813"}));
		comboBoxType.setSelectedIndex(3);
		
		comboBoxMap = new JComboBox<String>();
		comboBoxMap.setModel(new DefaultComboBoxModel<String>(new String[] {"<default>", "RGB", "RBG", "GRB", "GBR", "BRG", "BGR"}));
		
		comboBoxTestPattern = new JComboBox<String> ();
		comboBoxTestPattern.setModel(new DefaultComboBoxModel<String>(new String[] {"None", "Rainbow cycle", "Theater chase", "Colour wipe", "Scanner", "Fade"}));
		
		JLabel lblTestPattern = new JLabel("Test pattern");
		
		chckbxGammaCorrection = new JCheckBox("Gamma correction");
		
		comboBoxGammaValue = new JComboBox<String> ();
		comboBoxGammaValue.setModel(new DefaultComboBoxModel<String>(new String[] {"<default>", "2.0", "2.1", "2.2", "2.3", "2.4", "2.5"}));

		GroupLayout groupLayout = new GroupLayout(contentPanel);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(22)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort1)
							.addGap(18)
							.addComponent(lblUniversePort1))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort2)
							.addGap(18)
							.addComponent(lblUniversePort2))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort3)
							.addGap(18)
							.addComponent(lblUniversePort3))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort4)
							.addGap(18)
							.addComponent(lblUniversePort4))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort5)
							.addGap(18)
							.addComponent(lblUniversePort5))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort6)
							.addGap(18)
							.addComponent(lblUniversePort6))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort7)
							.addGap(18)
							.addComponent(lblUniversePort7))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort8)
							.addGap(18)
							.addComponent(lblUniversePort8))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort9)
							.addGap(18)
							.addComponent(lblUniversePort9))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort10)
							.addGap(18)
							.addComponent(lblUniversePort10))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort11)
							.addGap(18)
							.addComponent(lblUniversePort11))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort12)
							.addGap(18)
							.addComponent(lblUniversePort12))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort13)
							.addGap(18)
							.addComponent(lblUniversePort13))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort14)
							.addGap(18)
							.addComponent(lblUniversePort14))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort15)
							.addGap(18)
							.addComponent(lblUniversePort15))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblPort16)
							.addGap(18)
							.addComponent(lblUniversePort16)))
					.addGap(166))
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblType)
								.addComponent(lblCount)
								.addComponent(lblGroupSize)
								.addComponent(lblOutputs)
								.addComponent(lblStartUniverse))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(spinnerActiveOutput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(formattedTextFieldGroupSize, 61, 61, 61)
										.addComponent(formattedTextFieldPixelCount, GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
										.addComponent(comboBoxType, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(lblUniversesPerPort)
											.addGap(80))
										.addComponent(comboBoxMap, Alignment.LEADING, 0, 96, Short.MAX_VALUE)))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(formattedTextFieldStartUniverse, 83, 83, 83)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblProtocol))))
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addComponent(chckbxGammaCorrection)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBoxGammaValue, 0, 170, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addComponent(lblTestPattern)
							.addGap(18)
							.addComponent(comboBoxTestPattern, 0, 229, Short.MAX_VALUE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblType)
						.addComponent(comboBoxType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxMap, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCount)
						.addComponent(formattedTextFieldPixelCount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUniversesPerPort))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblGroupSize)
						.addComponent(formattedTextFieldGroupSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOutputs)
						.addComponent(spinnerActiveOutput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStartUniverse)
						.addComponent(formattedTextFieldStartUniverse, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblProtocol))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort1)
						.addComponent(lblUniversePort1))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort2)
						.addComponent(lblUniversePort2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort3)
						.addComponent(lblUniversePort3))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort4)
						.addComponent(lblUniversePort4))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort5)
						.addComponent(lblUniversePort5))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort6)
						.addComponent(lblUniversePort6))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort7)
						.addComponent(lblUniversePort7))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort8)
						.addComponent(lblUniversePort8))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort9)
						.addComponent(lblUniversePort9))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort10)
						.addComponent(lblUniversePort10))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort11)
						.addComponent(lblUniversePort11))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort12)
						.addComponent(lblUniversePort12))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort13)
						.addComponent(lblUniversePort13))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort14)
						.addComponent(lblUniversePort14))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort15)
						.addComponent(lblUniversePort15))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort16)
						.addComponent(lblUniversePort16))
					.addPreferredGap(ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxGammaCorrection)
						.addComponent(comboBoxGammaValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTestPattern)
						.addComponent(comboBoxTestPattern, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
		);
		
		contentPanel.setLayout(groupLayout);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnSave = new JButton("Save");
				buttonPane.add(btnSave);
			}
			{
				btnSetDefaults = new JButton("Set defaults");
				buttonPane.add(btnSetDefaults);
			}
			{
				btnCancel = new JButton("Cancel");
				buttonPane.add(btnCancel);
				getRootPane().setDefaultButton(btnCancel);
			}
		}
	}

	private void CreateEvents() {
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		spinnerActiveOutput.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if ((int) spinnerActiveOutput.getValue() > maxPorts) {
					spinnerActiveOutput.setValue(maxPorts);
				}
				update();
			}
		});
		
		comboBoxType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				comboBoxMap.setSelectedIndex(0);
				update();
			}
		});
		
		formattedTextFieldPixelCount.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				update();
			}
		});
		
		formattedTextFieldGroupSize.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				update();
			}
		});
		
		formattedTextFieldStartUniverse.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				update();
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		
		btnSetDefaults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remoteConfig.setTextArea(TXT_FILE);
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
					
					if (line.contains("led_type")) {
						comboBoxType.setSelectedItem(Properties.getString(line).toUpperCase());
						continue;
					}
					
					if (line.contains("led_count")) {
						formattedTextFieldPixelCount.setValue(Properties.getInt(line));
						continue;
					}
					
					if (line.contains("led_rgb_mapping")) {
						comboBoxMap.setSelectedItem(Properties.getString(line).toUpperCase());
						continue;	
					}
					
					if (line.contains("active_out")) {
						spinnerActiveOutput.setValue(Properties.getInt(line));
						continue;
					}
					
					if (line.contains("start_uni_port_8")) {
						maxPorts = 8;
						continue;
					}
					
					if (line.contains("start_uni_port_16")) {
						maxPorts = 16;
						continue;
					}
					
					if (line.contains("start_uni_port_1")) {
						final int lastIndex = line.lastIndexOf("_1=");
						if (lastIndex != -1) {
							formattedTextFieldStartUniverse.setValue(Properties.getInt(line));
						}
						continue;
					}
					
					if (line.contains("gamma_correction")) {
						chckbxGammaCorrection.setSelected(Properties.getBool(line));
						continue;
					}
					
					if (line.contains("gamma_value")) {
						comboBoxGammaValue.setSelectedItem(Properties.getString(line).toLowerCase());
						continue;
					}
					
					if (line.contains("test_pattern")) {
						comboBoxTestPattern.setSelectedIndex(Properties.getInt(line));
						continue;
					}
				}
			}
		}
		
		update();
	}
	
	private void update() {
		final int count = (int) formattedTextFieldPixelCount.getValue();
		final int groupingCount = (int) formattedTextFieldGroupSize.getValue();
		
		if (comboBoxType.getSelectedItem().toString().equals("SK6812W")) {
			ledsPerPixel = 4;
		} else {
			ledsPerPixel = 3;
		}
		
		if ((groupingCount == 0) || (groupingCount > count)){
			formattedTextFieldGroupSize.setValue(count);
		}
		
		final int groups = count / (int) formattedTextFieldGroupSize.getValue();
		int universeStart = (int) formattedTextFieldStartUniverse.getValue();
		int universes;
		
		if (doDisableStartUniverse) {
			universeStart = groups;
			universes = universeStart;
		} else {
			final int max_leds = 512 / ledsPerPixel;
			universes = ((groups + (max_leds - 1)) / max_leds);
			System.out.println("max_leds=" + max_leds);
			System.out.println("ledsPerPixel=" + ledsPerPixel);
			System.out.println("groups=" + groups);
			System.out.println("universes=" + universes);
		}
		
		lblUniversesPerPort.setText("[" + String.valueOf(universes) + "]");
				
		lblUniversePort1.setText(String.valueOf(universeStart));
		lblUniversePort2.setText(""); 
		lblUniversePort3.setText(""); 	
		lblUniversePort4.setText("");
		lblUniversePort5.setText("");
		lblUniversePort6.setText("");
		lblUniversePort7.setText(""); 
		lblUniversePort8.setText("");
		//
		lblUniversePort9.setText(""); 
		lblUniversePort10.setText(""); 	
		lblUniversePort11.setText("");
		lblUniversePort12.setText("");
		lblUniversePort13.setText("");
		lblUniversePort14.setText(""); 
		lblUniversePort15.setText("");
		lblUniversePort16.setText("");
		
		final int outputs = (int) spinnerActiveOutput.getValue();
		
		if (outputs >= 2) {
			final int universePrevious = Integer.parseInt(lblUniversePort1.getText());
			lblUniversePort2.setText(String.valueOf(universePrevious + universes));
		}

		if (outputs >= 3) {
			final int universePrevious = Integer.parseInt(lblUniversePort2.getText());
			lblUniversePort3.setText(String.valueOf(universePrevious + universes));
		}

		if (outputs >= 4) {
			final int universePrevious = Integer.parseInt(lblUniversePort3.getText());
			lblUniversePort4.setText(String.valueOf(universePrevious + universes));
		}

		if (outputs >= 5) {
			final int universePrevious = Integer.parseInt(lblUniversePort4.getText());
			lblUniversePort5.setText(String.valueOf(universePrevious + universes));
		}

		if (outputs >= 6) {
			final int universePrevious = Integer.parseInt(lblUniversePort5.getText());
			lblUniversePort6.setText(String.valueOf(universePrevious + universes));
		}

		if (outputs >= 7) {
			final int universePrevious = Integer.parseInt(lblUniversePort6.getText());
			lblUniversePort7.setText(String.valueOf(universePrevious + universes));
		}

		if (outputs >= 8) {
			final int universePrevious = Integer.parseInt(lblUniversePort7.getText());
			lblUniversePort8.setText(String.valueOf(universePrevious + universes));
		}

		if (outputs >= 9) {
			final int universePrevious = Integer.parseInt(lblUniversePort8.getText());
			lblUniversePort9.setText(String.valueOf(universePrevious + universes));
		}

		if (outputs >= 10) {
			final int universePrevious = Integer.parseInt(lblUniversePort9.getText());
			lblUniversePort10.setText(String.valueOf(universePrevious + universes));
		}

		if (outputs >= 11) {
			final int universePrevious = Integer.parseInt(lblUniversePort10.getText());
			lblUniversePort11.setText(String.valueOf(universePrevious + universes));
		}

		if (outputs >= 12) {
			final int universePrevious = Integer.parseInt(lblUniversePort11.getText());
			lblUniversePort12.setText(String.valueOf(universePrevious + universes));
		}

		if (outputs >= 13) {
			final int universePrevious = Integer.parseInt(lblUniversePort12.getText());
			lblUniversePort13.setText(String.valueOf(universePrevious + universes));
		}

		if (outputs >= 14) {
			final int universePrevious = Integer.parseInt(lblUniversePort13.getText());
			lblUniversePort14.setText(String.valueOf(universePrevious + universes));
		}

		if (outputs >= 15) {
			final int universePrevious = Integer.parseInt(lblUniversePort14.getText());
			lblUniversePort15.setText(String.valueOf(universePrevious + universes));
		}

		if (outputs == 16) {
			final int universePrevious = Integer.parseInt(lblUniversePort15.getText());
			lblUniversePort16.setText(String.valueOf(universePrevious + universes));
		}
	}
	
	private void save() {		
		if (opi != null) {
			StringBuffer txtFile = new StringBuffer("#" + TXT_FILE + "\n");
			
			txtFile.append(String.format("led_count=%s\n", formattedTextFieldPixelCount.getText()));
			txtFile.append(String.format("led_type=%s\n", comboBoxType.getSelectedItem().toString()));
			
			final String mapping = comboBoxMap.getSelectedItem().toString();
			if (mapping.toLowerCase().equals("<default>")) {
				// Nothing
			} else {
				txtFile.append(String.format("led_rgb_mapping=%s\n", mapping));	
			}
			
			txtFile.append(String.format("active_out=%d\n", (int) spinnerActiveOutput.getValue()));
			
			if (!doDisableStartUniverse) {
				txtFile.append(String.format("start_uni_port_1=%s\n", lblUniversePort1.getText()));
				if ((int) spinnerActiveOutput.getValue() > 1) {
					txtFile.append(String.format("start_uni_port_2=%s\n", lblUniversePort2.getText()));
					txtFile.append(String.format("start_uni_port_3=%s\n", lblUniversePort3.getText()));
					txtFile.append(String.format("start_uni_port_4=%s\n", lblUniversePort4.getText()));
					txtFile.append(String.format("start_uni_port_5=%s\n", lblUniversePort5.getText()));
					txtFile.append(String.format("start_uni_port_6=%s\n", lblUniversePort6.getText()));
					txtFile.append(String.format("start_uni_port_7=%s\n", lblUniversePort7.getText()));
					txtFile.append(String.format("start_uni_port_8=%s\n", lblUniversePort8.getText()));
				}
				if ((int) spinnerActiveOutput.getValue() > 8) {
					txtFile.append(String.format("start_uni_port_9=%s\n", lblUniversePort9.getText()));
					txtFile.append(String.format("start_uni_port_10=%s\n", lblUniversePort10.getText()));
					txtFile.append(String.format("start_uni_port_11=%s\n", lblUniversePort11.getText()));
					txtFile.append(String.format("start_uni_port_12=%s\n", lblUniversePort12.getText()));
					txtFile.append(String.format("start_uni_port_13=%s\n", lblUniversePort13.getText()));
					txtFile.append(String.format("start_uni_port_14=%s\n", lblUniversePort14.getText()));
					txtFile.append(String.format("start_uni_port_15=%s\n", lblUniversePort15.getText()));
					txtFile.append(String.format("start_uni_port_16=%s\n", lblUniversePort16.getText()));
				}
			}
			
			txtFile.append(String.format("led_group_count=%d\n", (int) formattedTextFieldGroupSize.getValue()));
			
			if (chckbxGammaCorrection.isSelected()) {
				txtFile.append(String.format("gamma_correction=1\n"));
			}
			
			final String gammaValue = comboBoxGammaValue.getSelectedItem().toString();
			
			if (gammaValue.toLowerCase().equals("<default>")) {
				// Nothing
			} else {
				txtFile.append(String.format("gamma_value=%s\n", gammaValue));	
			}
			
			txtFile.append(String.format("test_pattern=%s\n", comboBoxTestPattern.getSelectedIndex()));
			
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
