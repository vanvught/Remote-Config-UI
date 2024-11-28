/* Copyright (C) 2019-2023 by Arjan van Vught mailto:info@gd32-dmx.org
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
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.prefs.Preferences;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class NetworkInterfaces extends JDialog {
	private static final long serialVersionUID = 8437167617114467625L;
	private final JPanel contentPanel = new JPanel();
	
	private static final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
	
	private JButton okButton;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField txtFieldIf0;
	private JTextField txtFieldIf1;
	private JTextField txtFieldIf2;
	private JTextField txtFieldIf3;
	private JTextField txtFieldIf4;
	private JRadioButton rdbtnIf0;
	private JRadioButton rdbtnIf1;
	private JRadioButton rdbtnIf2;
	private JRadioButton rdbtnIf3;
	private JRadioButton rdbtnIf4;
	
	private Preferences prefs = Preferences.userRoot().node(getClass().getName());
	static final String LAST_INTERFACE_NAME = "org.orangepi.dmx";
	
	private RemoteConfig remoteConfig;;
	private TreeMap<String, InterfaceAddress> treeMap = null;
	private InterfaceAddress interfaceAddress;

	public static void main(String[] args) {
		try {
			NetworkInterfaces dialog = new NetworkInterfaces();
			dialog.Show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Show() {
		setVisible(true);
	}
	
	public InterfaceAddress getInterfaceAddress() {
		return interfaceAddress;
	}
	
	public NetworkInterfaces() {
		InitComponents();
		CreateEvents();
		
		getNetworkInterfaceList();
	}
	
	public NetworkInterfaces(RemoteConfig remoteConfig) {
		this.remoteConfig = remoteConfig;
		
		InitComponents();
		CreateEvents();
		
		getNetworkInterfaceList();
	}

	private void InitComponents() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("Network Interfaces");
		setBounds(100, 100, 380, 235);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		rdbtnIf0 = new JRadioButton("");
		rdbtnIf0.setSelected(false);
		rdbtnIf0.setVisible(false);
		buttonGroup.add(rdbtnIf0);
		
		rdbtnIf1 = new JRadioButton("");
		rdbtnIf1.setEnabled(false);
		rdbtnIf1.setVisible(false);
		buttonGroup.add(rdbtnIf1);
		
		rdbtnIf2 = new JRadioButton("");
		rdbtnIf2.setEnabled(false);
		rdbtnIf2.setVisible(false);
		buttonGroup.add(rdbtnIf2);
		
		rdbtnIf3 = new JRadioButton("");
		rdbtnIf3.setEnabled(false);
		rdbtnIf3.setVisible(false);
		buttonGroup.add(rdbtnIf3);
		
		rdbtnIf4 = new JRadioButton("");
		rdbtnIf4.setEnabled(false);
		rdbtnIf4.setVisible(false);
		buttonGroup.add(rdbtnIf4);
		
		txtFieldIf0 = new JTextField();
		txtFieldIf0.setBackground(Color.LIGHT_GRAY);
		txtFieldIf0.setEditable(false);
		txtFieldIf0.setColumns(10);
		
		txtFieldIf1 = new JTextField();
		txtFieldIf1.setBackground(Color.LIGHT_GRAY);
		txtFieldIf1.setEditable(false);
		txtFieldIf1.setColumns(10);
		
		txtFieldIf2 = new JTextField();
		txtFieldIf2.setBackground(Color.LIGHT_GRAY);
		txtFieldIf2.setEditable(false);
		txtFieldIf2.setColumns(10);
		
		txtFieldIf3 = new JTextField();
		txtFieldIf3.setBackground(Color.LIGHT_GRAY);
		txtFieldIf3.setEditable(false);
		txtFieldIf3.setColumns(10);
		
		txtFieldIf4 = new JTextField();
		txtFieldIf4.setBackground(Color.LIGHT_GRAY);
		txtFieldIf4.setEditable(false);
		txtFieldIf4.setColumns(10);
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(rdbtnIf0, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
						.addComponent(rdbtnIf1, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
						.addComponent(rdbtnIf2, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
						.addComponent(rdbtnIf3, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
						.addComponent(rdbtnIf4, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(txtFieldIf0, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
						.addComponent(txtFieldIf1, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
						.addComponent(txtFieldIf2, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
						.addComponent(txtFieldIf3, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
						.addComponent(txtFieldIf4, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnIf0)
						.addComponent(txtFieldIf0, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnIf1)
						.addComponent(txtFieldIf1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnIf2)
						.addComponent(txtFieldIf2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnIf3)
						.addComponent(txtFieldIf3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(rdbtnIf4)
						.addComponent(txtFieldIf4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				
			}
			
			getRootPane().setDefaultButton(okButton);
			
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addContainerGap(289, Short.MAX_VALUE)
						.addComponent(okButton))
			);
			
			gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addComponent(okButton))
			);
			buttonPane.setLayout(gl_buttonPane);
		}
	}

	private void CreateEvents() {		
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(treeMap);
				
				if (rdbtnIf0.isSelected()) {
					interfaceAddress = treeMap.get(rdbtnIf0.getText());
					prefs.put(LAST_INTERFACE_NAME, rdbtnIf0.getText());
				} else if (rdbtnIf1.isSelected()) {
					interfaceAddress = treeMap.get(rdbtnIf1.getText());
					prefs.put(LAST_INTERFACE_NAME, rdbtnIf1.getText());
				} else if (rdbtnIf2.isSelected()) {
					interfaceAddress = treeMap.get(rdbtnIf2.getText());
					prefs.put(LAST_INTERFACE_NAME, rdbtnIf2.getText());
				} else if (rdbtnIf3.isSelected()) {
					interfaceAddress = treeMap.get(rdbtnIf3.getText());
					prefs.put(LAST_INTERFACE_NAME, rdbtnIf3.getText());
				} else if (rdbtnIf4.isSelected()) {
					interfaceAddress = treeMap.get(rdbtnIf4.getText());
					prefs.put(LAST_INTERFACE_NAME, rdbtnIf4.getText());
				}
				
				if (remoteConfig != null) {
					remoteConfig.setTitle(interfaceAddress.getAddress());
					remoteConfig.setInterfaceAddress(interfaceAddress);
					remoteConfig.constructTree();
				}
						
				Component component = (Component) e.getSource();
				JDialog dialog = (JDialog) SwingUtilities.getRoot(component);
				dialog.dispose();
			}
		});
	}

	private void SetButtonText(JRadioButton button, JTextField textField, String displayName, InetAddress ip) throws SocketException {
		button.setEnabled(true);
		button.setVisible(true);
		button.setText(displayName);
		textField.setText(ip.getHostAddress());
		
		if (prefs.get(LAST_INTERFACE_NAME, "").equals(button.getText())) {
			button.setSelected(true);
		}
	}

	int SetButton(int nButton, String displayName, InterfaceAddress interfaceAddress) throws SocketException {
		InetAddress inetAddress = interfaceAddress.getAddress();
		
		if (inetAddress.getHostAddress().matches(ipv4Pattern)) {
			switch (nButton) {
			case 0:
				SetButtonText(rdbtnIf0, txtFieldIf0, displayName, inetAddress);
				break;
			case 1:
				SetButtonText(rdbtnIf1, txtFieldIf1, displayName, inetAddress);
				break;
			case 2:
				SetButtonText(rdbtnIf2, txtFieldIf2, displayName, inetAddress);
				break;
			case 3:
				SetButtonText(rdbtnIf3, txtFieldIf3, displayName, inetAddress);
				break;
			case 4:
				SetButtonText(rdbtnIf4, txtFieldIf4, displayName, inetAddress);
				break;
			default:
				break;
			}

			nButton = nButton + 1;
		}

		return nButton;
	}

	private void getNetworkInterfaceList() {
		treeMap = new TreeMap<String, InterfaceAddress>();
		
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			System.out.println(interfaces.toString());
			
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				System.out.println(networkInterface.getDisplayName());
				
				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue;
				}

				for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
					InetAddress broadcastAddress = interfaceAddress.getBroadcast();
					
					if (broadcastAddress != null) {
						treeMap.put(networkInterface.getDisplayName(), interfaceAddress);
						
						if (prefs.get(LAST_INTERFACE_NAME, "").equals("")) {
							this.interfaceAddress = interfaceAddress;
							continue;
						}
						
						if (prefs.get(LAST_INTERFACE_NAME, "").equals(networkInterface.getDisplayName())) {
							this.interfaceAddress = interfaceAddress;
						}
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		if (this.interfaceAddress == null) {
			if (!treeMap.isEmpty()) {
				Entry<String, InterfaceAddress> entry = treeMap.firstEntry();
				this.interfaceAddress = entry.getValue();	
			}
		}
		
		if (!treeMap.isEmpty()) {
			int nButtonIndex = 0;

			Set<Entry<String, InterfaceAddress>> entries = treeMap.entrySet();

			for (Entry<String, InterfaceAddress> entry : entries) {
				try {
					if ((nButtonIndex = SetButton(nButtonIndex, entry.getKey(), entry.getValue())) > 4) {
						break;
					}
				} catch (SocketException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
