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

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

public class WizardNodeE131Txt extends JDialog {
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
			
	private JFormattedTextField formattedTextFieldPriorityPortA;
	private JFormattedTextField formattedTextFieldPriorityPortB;
	private JFormattedTextField formattedTextFieldPriorityPortC;
	private JFormattedTextField formattedTextFieldPriorityPortD;
	//

	public static void main(String[] args) {
		try {
			WizardNodeE131Txt dialog = new WizardNodeE131Txt();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public WizardNodeE131Txt() {
		setTitle("sACN E1.31");
		initComponents();
		createEvents();

	}
	
	public WizardNodeE131Txt(String nodeId, OrangePi opi, RemoteConfig remoteConfig) {
		this.nodeId = nodeId;
		this.opi = opi;
		this.remoteConfig = remoteConfig;

		setTitle(nodeId);

		initComponents();
		createEvents();

		load();
	}
	
	private void initComponents() {
		setBounds(100, 100, 306, 250);
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
				
		JLabel lblPriority = new JLabel("Priority");
		
		NumberFormatter formatterPriority = new NumberFormatter(format);
		formatterPriority.setValueClass(Integer.class);
		formatterPriority.setMinimum(1);
		formatterPriority.setMaximum(200);
		formatterPriority.setAllowsInvalid(false);
		formatterPriority.setCommitsOnValidEdit(true);
		
		formattedTextFieldPriorityPortA = new JFormattedTextField(formatterPriority);
		formattedTextFieldPriorityPortA.setText("100");
		formattedTextFieldPriorityPortA.setColumns(3);
		
		formattedTextFieldPriorityPortB = new JFormattedTextField(formatterPriority);
		formattedTextFieldPriorityPortB.setText("100");
		formattedTextFieldPriorityPortB.setColumns(3);
		
		formattedTextFieldPriorityPortC = new JFormattedTextField(formatterPriority);
		formattedTextFieldPriorityPortC.setText("100");
		formattedTextFieldPriorityPortC.setColumns(3);
		
		formattedTextFieldPriorityPortD = new JFormattedTextField(formatterPriority);
		formattedTextFieldPriorityPortD.setText("100");
		formattedTextFieldPriorityPortD.setColumns(3);
				
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblUniversePortA, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUniversePortC, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUniversePortD, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUniversePortB, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPriority)
						.addComponent(formattedTextFieldPriorityPortA, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedTextFieldPriorityPortC, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedTextFieldPriorityPortD, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
						.addComponent(formattedTextFieldPriorityPortB, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
					.addGap(14))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblPriority)
					.addPreferredGap(ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGap(34)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(formattedTextFieldPriorityPortB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUniversePortB)))
						.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblUniversePortA)
							.addComponent(formattedTextFieldPriorityPortA, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(7)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUniversePortC)
						.addComponent(formattedTextFieldPriorityPortC, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(7)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUniversePortD)
						.addComponent(formattedTextFieldPriorityPortD, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(11))
		);
		gl_contentPanel.linkSize(SwingConstants.HORIZONTAL, new Component[] {formattedTextFieldPriorityPortA, formattedTextFieldPriorityPortB, formattedTextFieldPriorityPortC, formattedTextFieldPriorityPortD});
		
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
			final String txt = opi.getTxt(TXT_FILE);
			if (txt != null) {
				final String[] lines = txt.split("\n");
				for (int i = 0; i < lines.length; i++) {
					final String line = lines[i];
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

	
	private void save() {
		if (opi != null) {
			StringBuffer txt = new StringBuffer(opi.getTxt(TXT_FILE));
			txt.append("\n");
			// Input
			txt.append(String.format("priority_port_a=%s\n", formattedTextFieldPriorityPortA.getValue()));
			txt.append(String.format("priority_port_b=%s\n", formattedTextFieldPriorityPortB.getValue()));
			txt.append(String.format("priority_port_c=%s\n", formattedTextFieldPriorityPortC.getValue()));
			txt.append(String.format("priority_port_d=%s\n", formattedTextFieldPriorityPortD.getValue()));
											
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
