package sist2;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class Servidor {
	private static ServerSocket serverSocket;
	private static Socket no;
	private static Hashtable <String, String> hashTValue = new Hashtable<>();
	private static Hashtable <String, Timestamp> hashTTime = new Hashtable<>();
	private static Scanner scan = new Scanner(System.in);
	private static InetAddress ipC;
	private static InetAddress ipLead;
	private static int portC;
	private static int port1;
	private static int port2;
	private static int portLead;
	
	//thread para receber mensagem do client
	public static class TCPServer extends Thread {
		public void run() {
			try {
				//socket aceito
				InputStreamReader is = new InputStreamReader(no.getInputStream());
				BufferedReader reader = new BufferedReader(is);
				
				//mensagem recebida de Json para String
				Mensagem msgRec = new Gson().fromJson(reader.readLine(), Mensagem.class);
				no.close();
				
				//caso a mensagem seja PUT e não encaminhou p/ server líder
				if(msgRec.getType().equals("PUT") & msgRec.getStatus().equals("N/A") & portC != portLead) {
					System.out.println("Encaminhando PUT Key:" + msgRec.getKey() + " Value:" + msgRec.getValue());
					//encaminha mensagem recebida para o líder
					try(Socket lead = new Socket(ipLead, portLead)){
						String msgForward = new Gson().toJson(new Mensagem(msgRec.getKey(), msgRec.getValue(), "PUT", "N/A", ipC, portC, msgRec.getIpOrig(), msgRec.getPortOrig(), msgRec.getTimeStamp(), msgRec.getTPort()));
						
						OutputStream osL = lead.getOutputStream();
						DataOutputStream writerL = new DataOutputStream(osL);
						
						writerL.writeBytes(msgForward + "\n");
						
						lead.close();
					}
				}
				//caso a mensagem seja do tipo PUT e encaminhou p/ líder
				else if(msgRec.getType().equals("PUT") & msgRec.getStatus().equals("N/A") & portC == portLead) {
					System.out.println("Cliente: "+ "[" + "127.0.0.1" + " : " + msgRec.getTPort() + "]\n");
					System.out.println("PUT Key: " + msgRec.getKey() + " Value: " + msgRec.getValue() + "\n");
					
					//guarda informações recebidas na mensagem na hashTable
					hashTValue.put(msgRec.getKey(), msgRec.getValue());
					hashTTime.put(msgRec.getKey(), msgRec.getTimeStamp());
					
					//passando as informações de PUT para os outros servidores vizinhos
					try(Socket server1 = new Socket(ipC, port1)){
						OutputStream osS1 = server1.getOutputStream();
						DataOutputStream writerS1 = new DataOutputStream(osS1);
						String msgS1 = new Gson().toJson(new Mensagem(msgRec.getKey(), msgRec.getValue(), "REPLICATION", "N/A", ipC, portC, msgRec.getIpOrig(), msgRec.getPortDest(), msgRec.getTimeStamp(), msgRec.getTPort()));
						writerS1.writeBytes(msgS1 + "\n");
						server1.close();
					} catch (JsonSyntaxException e) {
						e.printStackTrace();
					}
				}
				//caso receba mensagem encaminhada do server líder
				else if(msgRec.getType().equals("REPLICATION") & msgRec.getPortDest() == portLead) {
					//adicionando as informações da mensagem na hashTable do servidor não líder
					hashTValue.put(msgRec.getKey(), msgRec.getValue());
					hashTTime.put(msgRec.getKey(), msgRec.getTimeStamp());
					
					System.out.println("REPLICATION Key: " + msgRec.getKey() + " Value: " + msgRec.getValue() + "\nTimestamp:" + msgRec.getTimeStamp() + "\n");
					
					try(Socket lead = new Socket(ipLead, portLead)){
						String msgLead = new Gson().toJson(new Mensagem(msgRec.getKey(), msgRec.getValue(), "REPLICATION", "REPLICATION_OK", ipC, portC, msgRec.getIpOrig(), msgRec.getPortOrig(), msgRec.getTimeStamp(), msgRec.getTPort()));
						
						OutputStream osL = lead.getOutputStream();
						DataOutputStream writerL = new DataOutputStream(osL);
						writerL.writeBytes(msgLead + "\n");
						lead.close();
					}
				}
				//enviando mensagem recebida para o servidor1 vizinho
				else if(msgRec.getStatus().equals("N/A") == false & msgRec.getType().equals("REPLICATION")) {
					if(msgRec.getPortDest() == port1 & msgRec.getStatus().equals("REPLICATION_OK")) {
						System.out.println("REPLICATION_OK SERVER 1");
						
						//atraso de 5s para retornar resposta ao server
						TCPServer.sleep(5000);
						
						try(Socket server2 = new Socket(ipC, port2)){
							OutputStream osS2 = server2.getOutputStream();
							DataOutputStream writerS2 = new DataOutputStream(osS2);
							
							String msgServer = new Gson().toJson(new Mensagem(msgRec.getKey(), msgRec.getValue(), "REPLICATION", "N/A", ipC, portC, msgRec.getIpOrig(), msgRec.getPortOrig(), msgRec.getTimeStamp(), msgRec.getTPort()));
							writerS2.writeBytes(msgServer + "\n");
							server2.close();
						} catch(JsonSyntaxException e) {
							e.printStackTrace();
						}
						
					}
					//caso o servidor1 vizinho responda com OK, enviar para o servidor2 vizinho
					else if(msgRec.getPortDest() == port2 & msgRec.getStatus().equals("REPLICATION_OK")) {
						System.out.println("REPLICATION_OK SERVER 2");
						
						System.out.println("Enviando PUT_OK ao Cliente: "+ "[" + "127.0.0.1" + " : " + msgRec.getTPort() + "]" + "\nKey: " + msgRec.getKey() + "\nTimeStamp do server: " + new Timestamp(System.currentTimeMillis()) + "\n");

						try(Socket clientR = new Socket("127.0.0.1", msgRec.getTPort())){
							OutputStream osR = clientR.getOutputStream();
							DataOutputStream writerR = new DataOutputStream(osR);
							
							String msgServer = new Gson().toJson(new Mensagem (msgRec.getKey(), msgRec.getValue(), "PUT_resp", "PUT_OK", ipC, portC, msgRec.getIpOrig(), msgRec.getPortOrig(), msgRec.getTimeStamp(), msgRec.getTPort()));
							writerR.writeBytes(msgServer + "\n");
							clientR.close();						
						} catch (JsonSyntaxException e ) {
							e.printStackTrace();
						}
					
					}
				}
				//caso mensagem recebida seja GET
				else if(msgRec.getType().equals("GET")) {
					System.out.println("Cliente: [" + "127.0.0.1" + " : " + msgRec.getTPort() + "] GET REQUEST" + "\nTimeStamp do cliente: " + msgRec.getTimeStamp() + "\n");
					
					//pega value da hashTable
					String Value = hashTValue.get(msgRec.getKey());
					
					//caso value não pertença à hashTable, retornar value como NULL
					if(Value == null) {
						try(Socket clientR = new Socket("127.0.0.1", msgRec.getTPort())){
							OutputStream osR = clientR.getOutputStream();
							DataOutputStream writerR = new DataOutputStream(osR);
							String returnGet = new Gson().toJson(new Mensagem(msgRec.getKey(), "NULL", "GET_resp", "GET_OK", ipC, portC, msgRec.getIpOrig(), msgRec.getPortOrig(), null, msgRec.getTPort()));
							writerR.writeBytes(returnGet + "\n");
							clientR.close();
						} catch (JsonSyntaxException e ) {
							e.printStackTrace();
						}
					}
					//caso valor buscado exista na hashTable
					else {
						System.out.println("GET_OK Value: " + Value + "\n");
						System.out.println("TimeStamp da key: "+ hashTTime.get(msgRec.getKey())+ "\n");
						System.out.println("TimeStamp do server: " + new Timestamp(System.currentTimeMillis()) + "\n");
						
						//retornar valor para o cliente que solicitou
						try(Socket clientR = new Socket("127.0.0.1", msgRec.getPortDest())) {
							OutputStream osR = clientR.getOutputStream();
							DataOutputStream writerR = new DataOutputStream(osR);
							String returnGet = new Gson().toJson(new Mensagem(msgRec.getKey(), Value, "GET_resp", "GET_OK", ipC, portC, msgRec.getIpOrig(), msgRec.getPortOrig(), hashTTime.get(msgRec.getKey()), msgRec.getTPort()));
							writerR.writeBytes(returnGet + "\n");
							clientR.close();
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						}
					}
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} 
	}

	public static void main(String[] args) throws Exception {		
		System.out.println("Insira o IP do servidor:");
		ipC = InetAddress.getByName(scan.next());
		
		System.out.println("Insira a porta do servidor:");
		portC = scan.nextInt();
		
		System.out.println("Insira o IP do líder:");
		ipLead = InetAddress.getByName(scan.next());
		
		System.out.println("Insira a porta do líder:");
		portLead = scan.nextInt();
		
		//definindo as portas default dos servidores, assim, só é preciso pedir uma porta e os as portas dos servers vizinhos são setadas abaixo
		if(portC == 10097) {
			port1 = 10098;
			port2 = 10099;
		}
		else if(portC == 10098) {
			port1 = 10097;
			port2 = 10099;
		}
		else {
			port1 = 10097;
			port2 = 10098;
		}
		
		//iniciando server
		serverSocket = new ServerSocket(portC);
		while(true) {
			no = serverSocket.accept();
			TCPServer servidor = new TCPServer();
			servidor.start();
		}		
	}
	
	
}
