package sist2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Mensagem {
	private String key;
	private String type;
	private String value;
	private String status;
	private InetAddress ipDest;
	private int portDest;
	private InetAddress ipOrig;
	private int portOrig;
	private Timestamp timeStamp;
	private int TPort;

	
	public Mensagem(String key, String value, String type, String status, InetAddress ipDest, int portDest, InetAddress ipOrig, int portOrig, Timestamp timeStamp, int TPort) {
		this.key = key;
		this.type = type;
		this.value = value;
		this.status = status;
		this.portDest = portDest;
		this.ipDest = ipDest;
		this.timeStamp = timeStamp;
		this.TPort = TPort;
	}
	
	public String getKey() {
		return key;
	}
	public String getType() {
		return type;
	}
	public String getValue() {
		return value;
	}
	public String getStatus() {
		return status;
	}
	public InetAddress getIpDest() {
		return ipDest;
	}
	public int getPortDest() {
		return portDest;
	}
	public InetAddress getIpOrig() {
		return ipOrig;
	}
	public int getPortOrig() {
		return portOrig;
	}
	public Timestamp getTimeStamp() {
		return timeStamp;
	}
	public int getTPort() {
		return TPort;
	}
	
	
}

