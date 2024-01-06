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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.util.TimeZone;
import java.util.prefs.Preferences;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import javax.swing.SwingConstants;
import java.awt.Component;

public class WizardNetworkTxt extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final String TXT_FILE = "network.txt";
	
	static final float UtcValidOffets[] = { -12.0f, -11.0f, -10.0f, -9.5f, -9.0f, -8.0f, -7.0f, -6.0f, -5.0f, -4.0f,
			-3.5f, -3.0f, -2.0f, -1.0f, 0.0f, 1.0f, 2.0f, 3.0f, 3.5f, 4.0f, 4.5f, 5.0f, 5.5f, 5.75f, 6.0f, 6.5f, 7.0f,
			8.0f, 8.75f, 9.0f, 9.5f, 10.0f, 10.5f, 11.0f, 12.0f, 12.75f, 13.0f, 14.0f };
	
	private Preferences prefs = Preferences.userRoot().node(getClass().getName());
	static final String LAST_USED_NTP_SERVER = "org.orangepi.dmx";
	
	RemoteConfig remoteConfig = null;
	OrangePi opi = null;
	String txt = null;
	
	private final JPanel contentPanel = new JPanel();
	
	private JButton btnCancel;
	private JButton btnSave;
	private JCheckBox chckbxEnableDHCP;
	private JLabel lblDhcpRetry;
	private JSpinner spinner;
	private JLabel lblStaticIp;
	private JLabel lblLocal;
	private JLabel lblNetmask;
	private JLabel lblNtpServerIp;
	private JLabel lblUtcOffset;
	private JComboBox<String> comboBoxUtcOffset;
	private JLabel lblHostname;
	private JTextField textFieldHostname;
	private JTextField textFieldNetmask;
	private JFormattedTextField formattedStaticIP1;
	private JFormattedTextField formattedStaticIP2;
	private JFormattedTextField formattedStaticIP3;
	private JFormattedTextField formattedStaticIP4;
	private JFormattedTextField formattedCdir;
	private JLabel lblsep;
	private JFormattedTextField formattedNtpServerIP1;
	private JFormattedTextField formattedNtpServerIP2;
	private JFormattedTextField formattedNtpServerIP3;
	private JFormattedTextField formattedNtpServerIP4;
	private JCheckBox chckbxEnableNtpServer;
	private JButton btnSetDefaults;
	private JLabel lblGateway;
	private JFormattedTextField formattedGatewayIP1;
	private JFormattedTextField formattedGatewayIP2;
	private JFormattedTextField formattedGatewayIP3;
	private JFormattedTextField formattedGatewayIP4;
	private JTextField textFieldSecondaryIP;
	private boolean hasSecondaryIP;

	public WizardNetworkTxt(String nodeId, OrangePi opi, RemoteConfig remoteConfig) {
		this.opi = opi;
		this.remoteConfig = remoteConfig;

		setTitle(nodeId);

		initComponents();
		createEvents();

		for (int i = 0; i < UtcValidOffets.length; i++) {
			comboBoxUtcOffset.addItem(formatUtcOffset(UtcValidOffets[i]));
		}

		load();
	}

	private void initComponents() {
		setBounds(100, 100, 346, 412);
		chckbxEnableDHCP = new JCheckBox("Enable DHCP");
		lblDhcpRetry = new JLabel("DHCP retry");	
		spinner = new JSpinner(new SpinnerNumberModel(0, 0, 5, 1));
		spinner.setToolTipText("0 is no retry");
		
		JLabel lblMinutes = new JLabel("Minutes");
		
		lblStaticIp = new JLabel("Static");
		lblStaticIp.setForeground(new Color(0, 0, 128));
		lblLocal = new JLabel("Primary IP");
		lblNetmask = new JLabel("Netmask");
		lblNtpServerIp = new JLabel("Server IP");
		lblUtcOffset = new JLabel("UTC offset");
		
		comboBoxUtcOffset = new JComboBox<String>();
		comboBoxUtcOffset.setSelectedItem(formatUtcOffset(0));
		comboBoxUtcOffset.setEnabled(false);
		
		lblHostname = new JLabel("Hostname");
		textFieldHostname = new JTextField();
		textFieldHostname.setColumns(10);
		
		textFieldNetmask = new JTextField();
		textFieldNetmask.setEditable(false);
		textFieldNetmask.setBackground(Color.LIGHT_GRAY);
		textFieldNetmask.setColumns(10);
		
		NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatterIP = new NumberFormatter(format);
		formatterIP.setValueClass(Integer.class);
		formatterIP.setMinimum(0);
		formatterIP.setMaximum(255);
		formatterIP.setAllowsInvalid(false);
		formatterIP.setCommitsOnValidEdit(true);
		
		formattedStaticIP1 = new JFormattedTextField(formatterIP);
		formattedStaticIP1.setColumns(2);
		
		formattedStaticIP2 = new JFormattedTextField(formatterIP);
		formattedStaticIP2.setColumns(2);
		
		formattedStaticIP3 = new JFormattedTextField(formatterIP);
		formattedStaticIP3.setColumns(2);
		
		formattedStaticIP4 = new JFormattedTextField(formatterIP);
		formattedStaticIP4.setColumns(2);
		
		NumberFormatter formatterCdir = new NumberFormatter(format);
		formatterCdir.setValueClass(Integer.class);
		formatterCdir.setMinimum(1);
		formatterCdir.setMaximum(32);
		formatterCdir.setAllowsInvalid(false);
		formatterCdir.setCommitsOnValidEdit(true);
		
		formattedCdir = new JFormattedTextField(formatterCdir);
		formattedCdir.setColumns(2);
		
		lblsep = new JLabel("/");
		
		formattedNtpServerIP1 = new JFormattedTextField(formatterIP);
		formattedNtpServerIP1.setEnabled(false);
		formattedNtpServerIP1.setColumns(2);
		
		formattedNtpServerIP2 = new JFormattedTextField(formatterIP);
		formattedNtpServerIP2.setEnabled(false);
		formattedNtpServerIP2.setColumns(2);
		
		formattedNtpServerIP3 = new JFormattedTextField(formatterIP);
		formattedNtpServerIP3.setEnabled(false);
		formattedNtpServerIP3.setColumns(2);
		
		formattedNtpServerIP4 = new JFormattedTextField(formatterIP);
		formattedNtpServerIP4.setEnabled(false);
		formattedNtpServerIP4.setColumns(2);
		
		chckbxEnableNtpServer = new JCheckBox("Enable NTP Client");
		chckbxEnableNtpServer.setEnabled(false);
				
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		lblGateway = new JLabel("Gateway");
		
		formattedGatewayIP1 = new JFormattedTextField(formatterIP);
		formattedGatewayIP1.setColumns(2);
		
		formattedGatewayIP2 = new JFormattedTextField(formatterIP);
		formattedGatewayIP2.setColumns(2);
		
		formattedGatewayIP3 = new JFormattedTextField(formatterIP);
		formattedGatewayIP3.setColumns(2);
		
		formattedGatewayIP4 = new JFormattedTextField(formatterIP);
		formattedGatewayIP4.setColumns(2);
		
		JLabel lblSecondaryIP = new JLabel("Seconday IP");
		
		textFieldSecondaryIP = new JTextField();
		textFieldSecondaryIP.setBackground(Color.LIGHT_GRAY);
		textFieldSecondaryIP.setEditable(false);
		textFieldSecondaryIP.setColumns(10);
		
		GroupLayout groupLayout = new GroupLayout(contentPanel);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblDhcpRetry)
								.addComponent(lblNetmask)
								.addComponent(lblHostname)
								.addComponent(lblLocal)
								.addComponent(lblNtpServerIp)
								.addComponent(lblGateway)
								.addComponent(lblStaticIp))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(textFieldHostname, 243, 243, 243)
									.addContainerGap())
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addGroup(groupLayout.createSequentialGroup()
												.addComponent(formattedStaticIP1, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(formattedStaticIP2, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(formattedStaticIP3, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(formattedStaticIP4, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
											.addGroup(groupLayout.createSequentialGroup()
												.addComponent(formattedGatewayIP1, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(formattedGatewayIP2, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(formattedGatewayIP3, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(formattedGatewayIP4, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
											.addGroup(groupLayout.createSequentialGroup()
												.addComponent(formattedNtpServerIP1, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(formattedNtpServerIP2, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(formattedNtpServerIP3, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(formattedNtpServerIP4, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)))
										.addGap(477)
										.addComponent(lblsep, GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(formattedCdir, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addGap(47))
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(textFieldNetmask, 144, 144, 144)
										.addContainerGap()))))
						.addComponent(lblSecondaryIP)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(81)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblMinutes))
								.addComponent(textFieldSecondaryIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(604))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(chckbxEnableDHCP)
							.addGap(702))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblUtcOffset)
							.addGap(12)
							.addComponent(comboBoxUtcOffset, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
							.addGap(631))))
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(chckbxEnableNtpServer)
					.addContainerGap(678, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSecondaryIP)
						.addComponent(textFieldSecondaryIP, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxEnableDHCP)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblDhcpRetry)
						.addComponent(spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblMinutes))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
									.addComponent(lblsep)
									.addComponent(formattedCdir, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblStaticIp)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblLocal))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(24)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(formattedStaticIP1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedStaticIP2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedStaticIP3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedStaticIP4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNetmask)
								.addComponent(textFieldNetmask, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(6)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(formattedGatewayIP1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedGatewayIP2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedGatewayIP3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedGatewayIP4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addComponent(lblGateway)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(32)
							.addComponent(chckbxEnableNtpServer))
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblHostname)
							.addComponent(textFieldHostname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(6)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(formattedNtpServerIP1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedNtpServerIP2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedNtpServerIP3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(formattedNtpServerIP4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addComponent(lblNtpServerIp)))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBoxUtcOffset, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addComponent(lblUtcOffset)))
					.addContainerGap(8, Short.MAX_VALUE))
		);
		groupLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] {formattedStaticIP1, formattedNtpServerIP1, formattedGatewayIP1});
		groupLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] {formattedStaticIP2, formattedNtpServerIP2, formattedGatewayIP2});
		groupLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] {formattedStaticIP3, formattedNtpServerIP3, formattedGatewayIP3});
		groupLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] {formattedStaticIP4, formattedNtpServerIP4, formattedGatewayIP4});
		
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

		chckbxEnableDHCP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		
		btnSetDefaults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				remoteConfig.setTextArea(opi.doDefaults(TXT_FILE));
				load();
			}
		});
		
		formattedCdir.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				textFieldNetmask.setText(convertCidrToString((int)formattedCdir.getValue()));
			}
		});
	}
	
	private String formatUtcOffset(float f) {
		if (f > 0) {
			return String.format("+%1.2f", f);
		}
		return String.format("%1.2f", f);
	}
	
	private int convertNetmaskToCIDR(InetAddress netmask) {
		byte[] netmaskBytes = netmask.getAddress();
		int cidr = 0;
		boolean zero = false;
		for (byte b : netmaskBytes) {
			int mask = 0x80;
			for (int i = 0; i < 8; i++) {
				int result = b & mask;
				if (result == 0) {
					zero = true;
				} else if (zero) {
					throw new IllegalArgumentException("Invalid netmask.");
				} else {
					cidr++;
				}
				mask >>>= 1;
			}
		}
		return cidr;
	}
	
	private String convertCidrToString(int cidrMask) {
		final long bits = 0xffffffff ^ (1 << 32 - cidrMask) - 1;
		return String.format("%d.%d.%d.%d", (bits & 0x0000000000ff000000L) >> 24, (bits & 0x0000000000ff0000) >> 16, (bits & 0x0000000000ff00) >> 8, bits & 0xff);
	}
	
	private void load() {
		if (opi != null) {
			txt = opi.getTxt(TXT_FILE);
			if (txt != null) {
				final String[] lines = txt.split("\n");
				for (int i = 0; i < lines.length; i++) {
					final String line = lines[i];
					
					if (line.contains("secondary_ip")) {
						textFieldSecondaryIP.setText(Properties.getString(line));
						hasSecondaryIP = true;
						continue;
					}
					
					// DHCP 
					
					if (line.contains("use_dhcp")) {
						chckbxEnableDHCP.setSelected(Properties.getBool(line));
						continue;
					}
					
					if (line.contains("dhcp_retry_time")) {
						spinner.setValue(Properties.getInt(line));
						continue;
					}
					
					// Static IP
					
					if (line.contains("ip_address")) {
						final String value = Properties.getString(line).replace('.', '\n');
						final String parts[] = value.split("\n");
						if (parts.length == 4) {
							formattedStaticIP1.setValue(Integer.parseInt(parts[0]));
							formattedStaticIP2.setValue(Integer.parseInt(parts[1]));
							formattedStaticIP3.setValue(Integer.parseInt(parts[2]));
							formattedStaticIP4.setValue(Integer.parseInt(parts[3]));
						}
					}
					
					if (line.contains("default_gateway")) {
						final String value = Properties.getString(line).replace('.', '\n');
						final String parts[] = value.split("\n");
						if (parts.length == 4) {
							formattedGatewayIP1.setValue(Integer.parseInt(parts[0]));
							formattedGatewayIP2.setValue(Integer.parseInt(parts[1]));
							formattedGatewayIP3.setValue(Integer.parseInt(parts[2]));
							formattedGatewayIP4.setValue(Integer.parseInt(parts[3]));
						}
					}
					
					if (line.contains("net_mask")) {
						textFieldNetmask.setText(Properties.getString(line));
						try {
							formattedCdir.setValue(convertNetmaskToCIDR(InetAddress.getByName(Properties.getString(line))));
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
						continue;	
					}
					
					if (line.contains("hostname")) {
						textFieldHostname.setText(Properties.getString(line));
						continue;
					}
					
					// NTP Server
					
					if (line.contains("ntp_server")) {
						chckbxEnableNtpServer.setEnabled(true);
						formattedNtpServerIP1.setEnabled(true);
						formattedNtpServerIP2.setEnabled(true);
						formattedNtpServerIP3.setEnabled(true);
						formattedNtpServerIP4.setEnabled(true);
						comboBoxUtcOffset.setEnabled(true);
						
						String value;
						
						if (line.startsWith("#")) {
							value = prefs.get(LAST_USED_NTP_SERVER, "0.0.0.0").replace('.', '\n');
							chckbxEnableNtpServer.setSelected(false);
						} else {
							value = Properties.getString(line).replace('.', '\n');
							chckbxEnableNtpServer.setSelected(true);
						}
						
						final String parts[] = value.split("\n");
						
						if (parts.length == 4) {
							try {
								formattedNtpServerIP1.setValue(Integer.parseInt(parts[0]));
								formattedNtpServerIP2.setValue(Integer.parseInt(parts[1]));
								formattedNtpServerIP3.setValue(Integer.parseInt(parts[2]));
								formattedNtpServerIP4.setValue(Integer.parseInt(parts[3]));	
							} catch (Exception e) {
							}
						}
					}
					
					if (line.contains("ntp_utc_offset")) {
						if (line.startsWith("#")) {
							TimeZone timeZone = TimeZone.getDefault();
							final float offsetUtc = (float) timeZone.getOffset(System.currentTimeMillis()) / 3600000;
							comboBoxUtcOffset.setSelectedItem(formatUtcOffset(offsetUtc));
						} else {
							comboBoxUtcOffset.setSelectedItem(formatUtcOffset(Float.parseFloat(Properties.getString(line))));
						}
						continue;
					}
				}
			}
			
			if (!hasSecondaryIP) {
				textFieldSecondaryIP.setForeground(Color.MAGENTA);
				textFieldSecondaryIP.setText("Update firmware");
			}
			
			if (!chckbxEnableNtpServer.isEnabled()) {
				comboBoxUtcOffset.setSelectedIndex(15);
			}
			
			update();
		}
	}
	
	private void update() {
		spinner.setEnabled(chckbxEnableDHCP.isSelected());
		//
		formattedStaticIP1.setEditable(!chckbxEnableDHCP.isSelected());
		formattedStaticIP2.setEditable(!chckbxEnableDHCP.isSelected());
		formattedStaticIP3.setEditable(!chckbxEnableDHCP.isSelected());
		formattedStaticIP4.setEditable(!chckbxEnableDHCP.isSelected());
		formattedCdir.setEditable(!chckbxEnableDHCP.isSelected());
		formattedGatewayIP1.setEditable(!chckbxEnableDHCP.isSelected());
		formattedGatewayIP2.setEditable(!chckbxEnableDHCP.isSelected());
		formattedGatewayIP3.setEditable(!chckbxEnableDHCP.isSelected());
		formattedGatewayIP4.setEditable(!chckbxEnableDHCP.isSelected());
	}	
	
	private void save() {
		if (opi != null) {
			StringBuffer txtFile = new StringBuffer("#" + TXT_FILE + "\n");
			
			final boolean enableDHCP  = chckbxEnableDHCP.isSelected();
			txtFile.append(String.format("use_dhcp=%d\n", enableDHCP ? 1 : 0));
						
			if (enableDHCP) {
				// DHCP
				txtFile.append(String.format("dhcp_retry_time=%d\n", spinner.getValue()));
			} else {
				// Static IP
				txtFile.append(String.format("ip_address=%d.%d.%d.%d\n", formattedStaticIP1.getValue(),formattedStaticIP2.getValue(),formattedStaticIP3.getValue(),formattedStaticIP4.getValue()));
				txtFile.append(String.format("net_mask=%s\n", textFieldNetmask.getText()));
				txtFile.append(String.format("default_gateway=%d.%d.%d.%d\n", formattedGatewayIP1.getValue(),formattedGatewayIP2.getValue(),formattedGatewayIP3.getValue(),formattedGatewayIP4.getValue()));
			}
			
			final String hostname = textFieldHostname.getText();
			
			if (hostname.length() > 63) {
				txtFile.append(String.format("hostname=%s\n", hostname.substring(0, 63)));
				textFieldHostname.setText(hostname.substring(0, 63));
			} else {
				txtFile.append(String.format("hostname=%s\n", hostname));
			}
			
			if (chckbxEnableNtpServer.isSelected()) {
				// NTP Server
				final String ip = String.format("%d.%d.%d.%d", formattedNtpServerIP1.getValue(), formattedNtpServerIP2.getValue(), formattedNtpServerIP3.getValue(), formattedNtpServerIP4.getValue());
				txtFile.append(String.format("ntp_server=%s\n", ip));
				prefs.put(LAST_USED_NTP_SERVER, ip);

				String utcOffset = comboBoxUtcOffset.getSelectedItem().toString().replace(',', '.');

				if (utcOffset.startsWith("+")) {
					utcOffset = utcOffset.substring(1);
				}

				txtFile.append(String.format("ntp_utc_offset=%s\n", utcOffset));
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
