/* Copyright (C) 2019-2024 by Arjan van Vught mailto:info@gd32-dmx.org
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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class OrangePi {
	private static final int BUFFERSIZE = 1024;
	private static final int PORT = 0x2905;
	
	private static final String ENV_TXT = "env.txt";
	private static final String RCONFIG_TXT = "rconfig.txt";
	private static final String DISPLAY_TXT = "display.txt";
	private static final String NETWORK_TXT = "network.txt";
											//  0			  1				2			  3			 4			    5					6		    7		8	   9              10		  11				 12
	private static final String[] NODE_NAMES = {"Art-Net",    "sACN E1.31", "OSC Server", "LTC",     "OSC Client",  "RDMNet LLRP Only", "Showfile", "MIDI", "DDP", "PixelPusher", "Node",     "Bootloader TFTP", "RDM Responder"};
	private static final String[] NODE_TXT   = {"artnet.txt", "e131.txt",   "osc.txt",    "ltc.txt", "oscclnt.txt", "",                 "show.txt", "",     "",    "",            "node.txt", "",                "rdm_device.txt"};
	private static final String[] OUTPUT_TXT = {"params.txt", "devices.txt", "mon.txt", "serial.txt", "rgbpanel.txt", "", "pca9685.txt"};
	private static final String LDISPLAY_TXT = "ldisplay.txt";
	private static final String TCNET_TXT = "tcnet.txt";
	private static final String GPS_TXT = "gps.txt";
	private static final String ETC_TXT = "etc.txt";
	private static final String MOTORS_TXT[] = {"motor0.txt", "motor1.txt", "motor2.txt", "motor3.txt", "motor4.txt", "motor5.txt", "motor6.txt", "motor7.txt" }; 
	private static final String RDM_DEVICE_TXT = "rdm_device.txt";
	private static final String SENSORS_TXT = "sensors.txt";
	private static final String SUBDEV_TXT = "subdev.txt";
	private static final String SPARKFUN_TXT = "sparkfun.txt";
	private static final String SHOW_TXT = "show.txt";
	
	private static final String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
	
	private DatagramSocket socketUDP;
	
	private Boolean isValid = false;
	
	private InetAddress address;
	
	private String nodeId = "";
	private String nodeDisplayName = "";

	private String txtEnv = null;
	private String txtRemoteConfig = null;
	private String txtDisplay = null;
	private String txtNetwork = null;
	private String txtNode = null;
	private String txtOutput = null;
	private String txtLtcDisplay = null;
	private String txtTCNet = null;
	private String txtGPS = null;
	private String txtETC = null;
	private String txtShow = null;
	private String txtSparkFun = null;
	private String txtMotors[] = {null, null, null, null, null, null, null, null};
	private String txtRDM_DEVICE = null;
	private String txtSENSORS = null;
	private String txtSUBDEV = null;

	private String sbLtcDisplay = null;
	private String sbTCNet = null;
	private String sbGPS = null;
	private String sbETC = null;
	private String sbSparkFun = null;
	private String sbMotors[] = {null, null, null, null, null, null, null, null};
	private String sbRDM_DEVICE = null;
	private String sbSENSORS = null;
	private String sbSUBDEV = null;
	
	public OrangePi(String arg, DatagramSocket socketReceive) {
		super();
		this.socketUDP = socketReceive;
		
		System.out.println("arg [" + arg + "]");
	
		String[] listValues = arg.split(",");
		
		if (listValues.length >= 4) {
			isValid = isValidNode(listValues[1]);	
			
			System.out.println(isValid + " node=[" + listValues[1] + "] output=[" + listValues[2] + "]");
			
			if (isValid) {
				String[] outputName = listValues[2].split("\n");
				
				if (outputName[0].equals("DMX") || outputName[0].equals("RDM")) {
					txtOutput = OUTPUT_TXT[0];
					txtShow = SHOW_TXT;
				} else if ((outputName[0].equals("Pixel")) && (!txtNode.contains("ddp"))){
					txtOutput = OUTPUT_TXT[1];
					txtShow = SHOW_TXT;
				} else if (outputName[0].equals("Monitor")) {
					txtOutput = OUTPUT_TXT[2];
				} else if (outputName[0].equals("TimeCode")) {
					txtLtcDisplay = LDISPLAY_TXT;
					txtTCNet = TCNET_TXT;
					txtGPS = GPS_TXT;
					txtETC = ETC_TXT;
				} else if (outputName[0].equals("OSC")) {
				} else if (outputName[0].equals("Config")) {
					//
				} else if (outputName[0].equals("Stepper")) {
					txtOutput = OUTPUT_TXT[1];
					txtSparkFun = SPARKFUN_TXT;
					for (int i = 0; i < txtMotors.length; i++) {
						txtMotors[i] = MOTORS_TXT[i];
					}
					txtRDM_DEVICE = RDM_DEVICE_TXT;
				} else if (outputName[0].equals("Player")) {
					// 
				} else if (outputName[0].equals("Art-Net")) {
					//
				} else if (outputName[0].equals("Serial")) {
					txtOutput = OUTPUT_TXT[4];
				} else if (outputName[0].equals("RGB Panel")) {
					txtOutput = OUTPUT_TXT[5];	
				} else if (outputName[0].equals("PWM")) {
					txtOutput = OUTPUT_TXT[6];	
				} else if (outputName[0].equals("Pixel")) {
					//
				} else {
					isValid = false;
				}
			}
			
			if (isValid) {
				if (!listValues[1].toLowerCase().contains("bootloader")) {
					txtEnv = ENV_TXT;
					txtRemoteConfig = RCONFIG_TXT;
					txtNetwork = NETWORK_TXT;
				}
					
				if (listValues[0].matches(ipv4Pattern)) {
					try {
						System.out.println("=> " + listValues[0]);
						address = InetAddress.getByName(listValues[0]);
					} catch (UnknownHostException e) {
						isValid = false;
						e.printStackTrace();
					}
				} else {
					isValid = false;
				}
			}
			
			if (isValid) {
				nodeId = listValues[0] + " " + listValues[1] + " " + listValues[2]  + " " + (listValues[3].equals("0") ? "" : listValues[3]);
				if (listValues.length == 5) {
					nodeDisplayName = listValues[4];
				} else {
					nodeDisplayName = "";
				}
				
				System.out.println("{" + nodeId + "} {" + nodeDisplayName + "}");
			} else {
				System.out.println("Invalid respone");
			}
		}
	}
	
	public String getTxt(String txt) {
		if (isEnvTxt(txt)) {
			return doGet(txt);
		} else if (isRemoteConfigTxt(txt)) {
			return doGet(txt);
		} else if (isDisplayTxt(txt)) {
			return doGet(txt);
		} else if (isNetworkTxt(txt)) {
			return doGet(txt);
		} else if (isNodeTxt(txt)) {
			return doGet(txt);
		} else if (isOutputTxt(txt)) {
			return doGet(txt);
		} else if (isLtcDisplayTxt(txt)) {
			if (sbLtcDisplay == null) {
				sbLtcDisplay = doGet(txt);
			}
			return sbLtcDisplay.toString();
		} else if (isTCNetTxt(txt)) {
			if (sbTCNet == null) {
				sbTCNet = doGet(txt);
			}
			return sbTCNet.toString();
		} else if (isGPSTxt(txt)) {
			if (sbGPS == null) {
				sbGPS = doGet(txt);
			}
			return sbGPS.toString();
		} else if (isETCTxt(txt)) {
			if (sbETC == null) {
				sbETC = doGet(txt);
			}
			return sbETC.toString();
		} else if (isMotorTxt(txt)) {
			int nIndex = txt.charAt(5) - '0';
			if (sbMotors[nIndex] == null) {
				sbMotors[nIndex] = doGet(txt);
			}
			return sbMotors[nIndex].toString();
		} else if (isSparkFunTxt(txt)) {
			if (sbSparkFun == null) {
				sbSparkFun = doGet(txt);
			}
			return sbSparkFun.toString();
		} else if (isRdmDeviceTxt(txt)) {
			if (sbRDM_DEVICE == null) {
				sbRDM_DEVICE = doGet(txt);
			}
			return sbRDM_DEVICE.toString();
		} else if (isRdmSensorsTxt(txt)) {
			if (sbSENSORS == null) {
				sbSENSORS = doGet(txt);
			}
			return sbSENSORS.toString();
		} else if (isRdmSubdevTxt(txt)) {
			if (sbSUBDEV == null) {
				sbSUBDEV = doGet(txt);
			}
			return sbSUBDEV.toString();
		} 

		return null;
	}
	
	public String doDefaults(String txt) {
		try {
			doSave("#" + txt + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getTxt(txt);
	}
	
	private void sendUdpPacket(byte[]  buffer) {
		System.out.println("sendUdpPacket " + address + ":" + PORT + " [" + new String(buffer) + "]");
		
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);
		
		try {
			socketUDP.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	private String doGet(String message) {
		final String p = new String("?get#" + message);

		byte[] bufferSendReceive = p.getBytes();
		
		sendUdpPacket(bufferSendReceive);
			
		bufferSendReceive = new byte[BUFFERSIZE];
		DatagramPacket packetReceive = new DatagramPacket(bufferSendReceive, bufferSendReceive.length);
		
		try {
			while (true) {
				socketUDP.receive(packetReceive);
				final String received = new String(packetReceive.getData()).trim();
				System.out.println("Message received [" + received + "]");
				if (received.toLowerCase().contains("error")) {
					return "Not implemented.";
				}
				return received;
			}
		} catch (SocketTimeoutException e) {
			System.out.println("Timeout reached!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new String("#" + message + "\n");
	}
	
	public Boolean doSave(String data) throws IOException {
		System.out.println("doSave [" + data + "]");
		
		if (!data.startsWith("#")) {
			System.err.println("Does not start with #");
			return false;
		}
		
		int nIndex = data.indexOf(".txt");
		
		if (nIndex < 0) {
			System.err.println("Does not start with #????.txt");
			return false;
		}
		
		String txt = data.substring(1, nIndex + 4);
		
		Boolean bDoSave = false;

		if (isEnvTxt(txt)) {
			bDoSave = true;
		} else if (isRemoteConfigTxt(txt)) {
			bDoSave = true;
		} else if (isDisplayTxt(txt)) {
			bDoSave = true;
		} else if (isNetworkTxt(txt)) {
			bDoSave = true;
		} else if (isOutputTxt(txt)) {
			bDoSave = true;
		} else if (isNodeTxt(txt)) {
			bDoSave = true;
		} else if (isLtcDisplayTxt(txt)) {
			sbLtcDisplay = null;
			bDoSave = true;
		} else if (isTCNetTxt(txt)) {
			sbTCNet = null;
			bDoSave = true;
		} else if (isGPSTxt(txt)) {
			sbGPS = null;
			bDoSave = true;
		} else if (isETCTxt(txt)) {
			sbETC = null;
			bDoSave = true;
		} else if (isMotorTxt(txt)) {
			int nMotorIndex = txt.charAt(5) - '0';
			sbMotors[nMotorIndex] = null;
			bDoSave = true;
		} else if (isSparkFunTxt(txt)) {
			sbSparkFun = null;
			bDoSave = true;
		} else if (isRdmDeviceTxt(txt)) {
			sbRDM_DEVICE = null;
			bDoSave = true;
		} else if (isRdmSensorsTxt(txt)) {
			sbSENSORS = null;
			bDoSave = true;
		} else if (isRdmSubdevTxt(txt)) {
			sbSUBDEV = null;
			bDoSave = true;
		}
		
		if (bDoSave) {
			byte[] buffer = data.trim().getBytes();
			sendUdpPacket(buffer);
		}
		
		return bDoSave;
	}
	
	public void doReboot() throws IOException {
		String p = new String("?reboot##");
		System.out.println(address + ":" + PORT + " " + p);

		byte[] buffer = p.getBytes();
		
		sendUdpPacket(buffer);
	}
	
	public void doFactory() throws IOException {
		String p = new String("?factory##");
		System.out.println(address + ":" + PORT + " " + p);

		byte[] buffer = p.getBytes();
		
		sendUdpPacket(buffer);	
	}
	
	public void doSetDisplay(Boolean bOnOff) {
		String p = new String("!display#");
		if (bOnOff) {
			p = p + '1';
		} else {
			p = p + '0';
		}
		
		System.out.println(address + ":" + PORT + " " + p);
		
		byte[] buffer = p.getBytes();
		
		sendUdpPacket(buffer);
	}
	
	private String doRequest(String p) {
		System.out.println("doRequest " + address + ":" + PORT + " " + p);

		byte[] bufferSendReceive = p.getBytes();
		
		sendUdpPacket(bufferSendReceive);
		
		bufferSendReceive = new byte[BUFFERSIZE];
		DatagramPacket packetReceive = new DatagramPacket(bufferSendReceive, bufferSendReceive.length);
					
		try {
			while (true) {
				socketUDP.receive(packetReceive);
				final String received = new String(packetReceive.getData()).trim();
				System.out.println("Message received [" + received + "]");
				return received;
			}
		} catch (SocketTimeoutException e) {
			System.out.println("Timeout reached!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new String("#ERROR - time out");
	}
	
	public String doGetDisplay() {
		return doRequest("?display#");
	}
	
	public String doUptime() {
		return doRequest("?uptime#");

	}
	
	public String doVersion() {
		return doRequest("?version#");

	}
	
	public String doGetTFTP() {
		return doRequest("?tftp#");
	}
	
	public void doSetTFTP(Boolean bOnOff) {
		String p = new String("!tftp#");
		if (bOnOff) {
			p = p + '1';
		} else {
			p = p + '0';
		}
		
		byte[] buffer = p.getBytes();
		
		sendUdpPacket(buffer);
	}
			
	private Boolean isValidNode(String nodeName) {
		for (int i = 0; i < NODE_NAMES.length; i++) {
			if (nodeName.equals(NODE_NAMES[i])) {
				txtNode = NODE_TXT[i];
				if ((i == 0) || (i == 1) || (i == 6) || (i == 8) || (i == 9) || (i == 10) || (i == 12)) {
					txtDisplay = DISPLAY_TXT;
				}
				if (i == 12) {
					txtSENSORS = SENSORS_TXT;
					txtSUBDEV = SUBDEV_TXT;
				}
				return true;
			}
		}
		return false;
	}

	private Boolean isEnvTxt(String config) {
		if (config.equals(ENV_TXT)) {
			return true;
		}
		return false;
	}
	
	private Boolean isRemoteConfigTxt(String config) {
		if (config.equals(RCONFIG_TXT)) {
			return true;
		}
		return false;
	}
	
	private Boolean isDisplayTxt(String config) {
		if (config.equals(DISPLAY_TXT)) {
			return true;
		}
		return false;
	}
	
	private Boolean isNetworkTxt(String network) {
		if (network.equals(NETWORK_TXT)) {
			return true;
		}
		return false;
	}
	
	private Boolean isNodeTxt(String type) {
		for (int i = 0; i < NODE_TXT.length; i++) {
			if (type.equals(NODE_TXT[i])) {
				return true;
			}
		}
		return false;
	}	
	
	private Boolean isOutputTxt(String mode) {
		for (int i = 0; i < OUTPUT_TXT.length; i++) {
			if (mode.equals(OUTPUT_TXT[i])) {
				return true;
			}
		}
		return false;
	}
	
	private Boolean isLtcDisplayTxt(String ldisplay) {
		if (ldisplay.equals(LDISPLAY_TXT)) {
			return true;
		}
		return false;
	}
	
	private Boolean isTCNetTxt(String tcnet) {
		if (tcnet.equals(TCNET_TXT)) {
			return true;
		}
		return false;
	}
	
	private Boolean isGPSTxt(String gps) {
		if (gps.equals(GPS_TXT)) {
			return true;
		}
		return false;
	}
	
	private Boolean isETCTxt(String etc) {
		if (etc.equals(ETC_TXT)) {
			return true;
		}
		return false;
	}
	
	private Boolean isSparkFunTxt(String s) {
		if (s.equals(SPARKFUN_TXT)) {
			return true;
		}
		return false;
	}
	
	private Boolean isMotorTxt(String s) {
		for (int i = 0; i < MOTORS_TXT.length; i++) {
			if (s.equals(MOTORS_TXT[i])) {
				return true;
			}
		}
		return false;
	}
	
	private Boolean isRdmDeviceTxt(String s) {
		if (s.equals(RDM_DEVICE_TXT)) {
			return true;
		}
		return false;
	}
	
	private Boolean isRdmSensorsTxt(String s) {
		if (s.equals(SENSORS_TXT)) {
			return true;
		}
		return false;		
	}

	private Boolean isRdmSubdevTxt(String s) {
		if (s.equals(SUBDEV_TXT)) {
			return true;
		}
		return false;		
	}
	
	public Boolean getIsValid() {
		return isValid;
	}
	
	public String getNodeId() {
		return nodeId;
	}
	
	public String getNodeType() {
		if (txtNode.trim().length() == 0) {
			return null;
		}
		return txtNode;
	}

	public String getNodeOutput() {
		return txtOutput;
	}
	
	public String getDisplayName() {
		return nodeDisplayName;
	}
	
	public String getEnv() {
		return txtEnv;
	}
	
	public String getRemoteConfig() {
		return txtRemoteConfig;
	}
	
	public String getDisplay() {
		return txtDisplay;
	}
		
	public String getNetwork() {
		return txtNetwork;
	}
	
	public String getLtcDisplay() {
		return txtLtcDisplay;
	}
	
	public String getTCNet() {
		return txtTCNet;
	}
	
	public String getGPS() {
		return txtGPS;
	}
	
	public String getETC() {
		return txtETC;
	}
	
	public String getSparkFun() {
		return txtSparkFun;
	}
	
	public String getMotor(int MotorIndex) {
		if (MotorIndex >= txtMotors.length) {
			return null;
		}
		return txtMotors[MotorIndex];
	}
	
	public String getRdmDevice() {
		return txtRDM_DEVICE;
	}
	
	public String getRdmSensors() {
		return txtSENSORS;
	}
	
	public String getRdmSubDev() {
		return txtSUBDEV;
	}
	
	public String getNodeShow() {
		return txtShow;
	}
	
	public InetAddress getAddress() {
		return address;
	}
		
	@Override
	public String toString() {
		if (nodeDisplayName.length() != 0) {
			return nodeDisplayName;
		}
		
		return nodeId;
	}
}
