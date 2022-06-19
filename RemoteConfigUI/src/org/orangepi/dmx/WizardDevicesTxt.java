/* Copyright (C) 2021 by Arjan van Vught mailto:info@orangepi-dmx.nl
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

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;

public class WizardDevicesTxt extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private static final long serialVersionUID = 1L;
	//
	OrangePi opi = null;
	RemoteConfig remoteConfig = null;
	//
	private JButton btnCancel;
	private JButton btnSave;
	private JRadioButton rdbtnRGB;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnRGBW;
	private int ledsPerPixel = 3;
	private int maxPorts = 8;
	private JLabel lblCount;
	private NumberFormatter formatterCount;
	private JFormattedTextField formattedTextFieldPixelCount;
	private JLabel lblStartUniverse;
	
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
		
		load();
	}
	
	private void InitComponents() {
		setBounds(100, 100, 305, 588);
	
		JLabel lblType = new JLabel("Type");
		
		rdbtnRGB = new JRadioButton("RGB");
		buttonGroup.add(rdbtnRGB);
		rdbtnRGB.setSelected(true);
		rdbtnRGBW = new JRadioButton("RGBW");
		buttonGroup.add(rdbtnRGBW);
		
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

		GroupLayout groupLayout = new GroupLayout(contentPanel);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblType)
										.addComponent(lblOutputs)
										.addComponent(lblCount))
									.addGap(24)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(rdbtnRGB)
										.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
											.addComponent(formattedTextFieldPixelCount, Alignment.LEADING)
											.addComponent(spinnerActiveOutput, Alignment.LEADING)
											.addComponent(formattedTextFieldGroupSize, Alignment.LEADING)))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblUniversesPerPort)
										.addComponent(rdbtnRGBW))
									.addPreferredGap(ComponentPlacement.RELATED, 54, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblStartUniverse)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(formattedTextFieldStartUniverse, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblProtocol)
									.addGap(12))))
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
							.addGap(69))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblGroupSize)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addContainerGap()
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblType)
								.addComponent(rdbtnRGB)
								.addComponent(rdbtnRGBW))
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
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
		
		rdbtnRGB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnRGB.isSelected()) {
					ledsPerPixel = 3;
					formatterCount.setMaximum(4 * 170);
					update();
				}
			}
		});
		
		rdbtnRGBW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnRGBW.isSelected()) {
					ledsPerPixel = 4;
					formatterCount.setMaximum(4 * 128);
					if ((int)formattedTextFieldPixelCount.getValue() > (4 * 128)) {
						formattedTextFieldPixelCount.setValue(4 * 128);
					}
					update();
				}
			}
		});
		
		spinnerActiveOutput.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if ((maxPorts == 8) && ((int) spinnerActiveOutput.getValue() > 8)) {
					spinnerActiveOutput.setValue((int)8);
				}
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
				remoteConfig.setTextArea(opi.doDefaults("devices.txt"));
				load();
			}
		});
	}
	
	private void load() {
		if (opi != null) {
			final String txt = opi.getTxt("devices.txt");
			if (txt != null) {
				final String[] lines = txt.split("\n");
				for (int i = 0; i < lines.length; i++) {
					final String line = lines[i];
					if (line.contains("led_type")) {
						if (line.toUpperCase().contains("SK6812W")) {
							rdbtnRGBW.setSelected(true);
							rdbtnRGB.setEnabled(false);
						} else {
							rdbtnRGB.setSelected(true);
							rdbtnRGBW.setEnabled(false);
						}
						continue;
					}
					if (line.contains("led_count")) {
						formattedTextFieldPixelCount.setValue(Properties.getInt(line));
						continue;
					}
					if (line.contains("active_out")) {
						spinnerActiveOutput.setValue(Properties.getInt(line));
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
				}
			}
		}
		
		update();
	}
	
	private void update() {
		final int count = (int) formattedTextFieldPixelCount.getValue();
		final int groupingCount = (int) formattedTextFieldGroupSize.getValue();
		
		if ((groupingCount == 0) || (groupingCount > count)){
			formattedTextFieldGroupSize.setValue(count);
		}
		
		final int groups = count / (int) formattedTextFieldGroupSize.getValue();
		int universeStart = (int) formattedTextFieldStartUniverse.getValue();
		int universes;
		
		final boolean isArtNet = getTitle().toLowerCase().contains("art");
		
		if (isArtNet) {
			universeStart = ArtNet.getStartUniverse(universeStart);
			universes = 4;
		} else {
			universes = 1 + (groups/ (512 / ledsPerPixel));
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
			if (isArtNet) 
				lblUniversePort2.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort2.setText(String.valueOf(universePrevious + universes));
		}
		
		if (outputs >= 3) {
			final int universePrevious = Integer.parseInt(lblUniversePort2.getText());
			if (isArtNet) 
				lblUniversePort3.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort3.setText(String.valueOf(universePrevious + universes));
		}
		
		if (outputs >= 4) {
			final int universePrevious = Integer.parseInt(lblUniversePort3.getText());
			if (isArtNet) 
				lblUniversePort4.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort4.setText(String.valueOf(universePrevious + universes));
		}
		
		if (outputs >= 5) {
			final int universePrevious = Integer.parseInt(lblUniversePort4.getText());
			if (isArtNet) 
				lblUniversePort5.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort5.setText(String.valueOf(universePrevious + universes));
		}
		
		if (outputs >= 6) {
			final int universePrevious = Integer.parseInt(lblUniversePort5.getText());
			if (isArtNet) 
				lblUniversePort6.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort6.setText(String.valueOf(universePrevious + universes));
		}
		
		if (outputs >= 7) {
			final int universePrevious = Integer.parseInt(lblUniversePort6.getText());
			if (isArtNet) 
				lblUniversePort7.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort7.setText(String.valueOf(universePrevious + universes));
		}
		
		if (outputs >= 8) {
			final int universePrevious = Integer.parseInt(lblUniversePort7.getText());
			if (isArtNet) 
				lblUniversePort8.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort8.setText(String.valueOf(universePrevious + universes));
		}
		
		if (outputs >= 9) {
			final int universePrevious = Integer.parseInt(lblUniversePort8.getText());
			if (isArtNet) 
				lblUniversePort9.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort9.setText(String.valueOf(universePrevious + universes));
		}
		
		if (outputs >= 10) {
			final int universePrevious = Integer.parseInt(lblUniversePort9.getText());
			if (isArtNet) 
				lblUniversePort10.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort10.setText(String.valueOf(universePrevious + universes));
		}
		
		if (outputs >= 11) {
			final int universePrevious = Integer.parseInt(lblUniversePort10.getText());
			if (isArtNet) 
				lblUniversePort11.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort11.setText(String.valueOf(universePrevious + universes));
		}
		
		if (outputs >= 12) {
			final int universePrevious = Integer.parseInt(lblUniversePort11.getText());
			if (isArtNet) 
				lblUniversePort12.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort12.setText(String.valueOf(universePrevious + universes));
		}
		
		if (outputs >= 13) {
			final int universePrevious = Integer.parseInt(lblUniversePort12.getText());
			if (isArtNet) 
				lblUniversePort13.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort13.setText(String.valueOf(universePrevious + universes));
		}
		
		if (outputs >= 14) {
			final int universePrevious = Integer.parseInt(lblUniversePort13.getText());
			if (isArtNet) 
				lblUniversePort14.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort14.setText(String.valueOf(universePrevious + universes));
		}
		
		if (outputs >= 15) {
			final int universePrevious = Integer.parseInt(lblUniversePort14.getText());
			if (isArtNet) 
				lblUniversePort15.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort15.setText(String.valueOf(universePrevious + universes));
		}
		
		if (outputs == 16) {
			final int universePrevious = Integer.parseInt(lblUniversePort15.getText());
			if (isArtNet) 
				lblUniversePort16.setText(String.valueOf(ArtNet.getStartUniverse(universePrevious + universes)));
			else 
				lblUniversePort16.setText(String.valueOf(universePrevious + universes));
		}
	}
	
	private void save() {		
		if (opi != null) {
			StringBuffer devicesTxtAppend = new StringBuffer();		
			devicesTxtAppend.append(String.format("led_count=%s\n", formattedTextFieldPixelCount.getText()));
			devicesTxtAppend.append(String.format("active_out=%d\n", (int) spinnerActiveOutput.getValue()));
			//
			devicesTxtAppend.append(String.format("start_uni_port_1=%s\n", lblUniversePort1.getText()));
			devicesTxtAppend.append(String.format("start_uni_port_2=%s\n", lblUniversePort2.getText()));
			devicesTxtAppend.append(String.format("start_uni_port_3=%s\n", lblUniversePort3.getText()));
			devicesTxtAppend.append(String.format("start_uni_port_4=%s\n", lblUniversePort4.getText()));
			devicesTxtAppend.append(String.format("start_uni_port_5=%s\n", lblUniversePort5.getText()));
			devicesTxtAppend.append(String.format("start_uni_port_6=%s\n", lblUniversePort6.getText()));
			devicesTxtAppend.append(String.format("start_uni_port_7=%s\n", lblUniversePort7.getText()));
			devicesTxtAppend.append(String.format("start_uni_port_8=%s\n", lblUniversePort8.getText()));
			//
			if ((int) spinnerActiveOutput.getValue() > 8) {
				devicesTxtAppend.append(String.format("start_uni_port_9=%s\n", lblUniversePort9.getText()));
				devicesTxtAppend.append(String.format("start_uni_port_10=%s\n", lblUniversePort10.getText()));
				devicesTxtAppend.append(String.format("start_uni_port_11=%s\n", lblUniversePort11.getText()));
				devicesTxtAppend.append(String.format("start_uni_port_12=%s\n", lblUniversePort12.getText()));
				devicesTxtAppend.append(String.format("start_uni_port_13=%s\n", lblUniversePort13.getText()));
				devicesTxtAppend.append(String.format("start_uni_port_14=%s\n", lblUniversePort14.getText()));
				devicesTxtAppend.append(String.format("start_uni_port_15=%s\n", lblUniversePort15.getText()));
				devicesTxtAppend.append(String.format("start_uni_port_16=%s\n", lblUniversePort16.getText()));
			}
			
			devicesTxtAppend.append(String.format("led_grouping=%s\n", (int) formattedTextFieldGroupSize.getValue() != 1 ? "1" : "0"));
			devicesTxtAppend.append(String.format("led_group_count=%d\n", (int) formattedTextFieldGroupSize.getValue()));
			
			String txt = opi.getTxt("devices.txt");
			
			txt = txt.replaceAll("start_uni_port_", "#");
			
			try {
				opi.doSave(txt + "\n" + devicesTxtAppend.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (remoteConfig != null) {
				remoteConfig.setTextArea(opi.getTxt("devices.txt"));
			}
			
			System.out.println(devicesTxtAppend.toString());
		}
	}
}
