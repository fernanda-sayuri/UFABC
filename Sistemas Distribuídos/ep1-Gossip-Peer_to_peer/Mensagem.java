package teste;

import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Mensagem implements Serializable {
	private String msg;
	private String msgType;
	private int port;
	private String ip;
	private ArrayList<Integer> arrPeers;
	private int busca;
	
	public Mensagem(String msg, String msgType, int port, String ip, ArrayList<Integer> arrPeers, int busca) {
		this.msg = msg;
		this.msgType = msgType;
		this.port = port;
		this.ip = ip;
		this.arrPeers = arrPeers;
		this.busca = busca;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getMsgType() {
		return msgType;
	}
	public int getPort() {
		return port;
	}
	public int getBusca() {
		return busca;
	}
	public String getIp() {
		return ip;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	public ArrayList<Integer> getArrPeers() {
		return this.arrPeers = arrPeers;
	}
	
	

}
