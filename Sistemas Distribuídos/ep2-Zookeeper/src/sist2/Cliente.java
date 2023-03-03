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
import java.util.Random;
import java.util.Scanner;
import com.google.gson.Gson;

public class Cliente {
	private static Scanner scan = new Scanner(System.in);
	
	private static int portC;
	private static int port1;
	private static int port2;
	private static int port3;
	private static int portRandom;
	private static String newMsg;
	private static InetAddress ipC;
	private static InetAddress ip1;
	private static InetAddress ip2;
	private static InetAddress ip3;
	private static InetAddress ipRandom;
	
	private static Hashtable <String, Timestamp> hashTTime = new Hashtable<>();
	private static ServerSocket receive;
	
	public static void randomServer() throws IOException {
		//random entre servers vizinhos
		Random gerador = new Random();
		int numRandom = gerador.nextInt(2); 
		
		if(numRandom == 1) {
			ipRandom = ip1;
			portRandom = port1;
		}
		else if (numRandom == 2) {
			ipRandom = ip2;
			portRandom = port2;
		}
		else {
			ipRandom = ip3;
			portRandom = port3;
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("Cliente:");
		
		System.out.println("Insira o seu IP:");
		ipC = InetAddress.getByName(scan.next());
		
		System.out.println("Insira a sua porta:");
		portC = scan.nextInt();
		
		System.out.println("Insira o ip do primeiro servidor:");
		ip1 = InetAddress.getByName(scan.next());
		
		System.out.println("Insira a porta do primeiro servidor:");
		port1 = scan.nextInt();
		
		System.out.println("Insira o ip do segundo servidor:");
		ip2 = InetAddress.getByName(scan.next());
		
		System.out.println("Insira a porta do segundo servidor:");
		port2 = scan.nextInt();
		
		System.out.println("Insira o ip do terceiro servidor:");
		ip3 = InetAddress.getByName(scan.next());
		
		System.out.println("Insira a porta do terceiro servidor:");
		port3 = scan.nextInt();
		
		receive = new ServerSocket(portC);
		
		//menu de opções
		while(true) {
			System.out.println("Digite 1 para PUT e 2 para GET:");
			int menu = scan.nextInt();
			
			//PUT
			if(menu == 1) {
				System.out.println("Digite a chave key:");
				String key = scan.next();
				
				System.out.println("Digite o value:");
				String value = scan.next();
				
				//escolhe server vizinho de forma aleatória
				randomServer();
				
				//envia mensagem para o servidor escolhido
				Mensagem msgPUT = new Mensagem(key, value, "PUT", "N/A", ipC, portC, ipC, portC, new Timestamp(System.currentTimeMillis()), portC);
				sendMessage(msgPUT);
				
				hashTTime.put(key, new Timestamp(System.currentTimeMillis()));
			}
			//GET
			else if(menu == 2) {
				System.out.println("Digite a chave key do valor procurado:");
				String key = scan.next();
				
				//escolhe server vizinho de forma aleatória
				randomServer();
				
				//envia mensagem para o servidor escolhido
				Mensagem msgGET = new Mensagem(key, "N/A", "GET", "N/A", ipC, portC, ipC, portC, new Timestamp(System.currentTimeMillis()), portC);
				sendMessage(msgGET);
			}
		}		
	}
	
	public static class TCPClient extends Thread{
		private static String mensagem;
		private static Mensagem resp;
		private Socket client;
		
		public TCPClient(String mensagem) {
			TCPClient.mensagem = mensagem;
		}
		
		public void run() {
			try { 
				//cria socket para enviar mensagem ao servidor vizinho escolhido
				client = new Socket(ipRandom, portRandom);
				OutputStream os = client.getOutputStream();
				DataOutputStream writer = new DataOutputStream(os);

				writer.writeBytes(mensagem + "\n");
				client.close();
				
				Socket no = receive.accept();
				
				InputStreamReader is = new InputStreamReader(no.getInputStream());
				BufferedReader reader = new BufferedReader(is);
				
				//resposta recebida
				resp = new Gson().fromJson(reader.readLine(), Mensagem.class);
				no.close();
				
				//caso resposta seja do tipo PUT
				if(resp.getType().equals("PUT_resp")) {
					System.out.print(resp.getStatus());
					System.out.println(" Key:" + resp.getKey()+ " Value:" + resp.getValue() + "\nRealizado no servidor: {" + resp.getIpDest()+ " : " + resp.getPortDest() + "}" + "\nTimeStamp da key no servidor: " + resp.getTimeStamp());
					hashTTime.put(resp.getKey(), resp.getTimeStamp());
				}
				//caso resposta seja do tipo GET
				else if(resp.getType().equals("GET_resp")) {
					boolean consis;
					
					//verifica caso a a key já está na hashTable
					if(hashTTime.get(resp.getKey()) == null) {
						consis = true;
					}
					else {
						if(resp.getTimeStamp() == null) {
							consis = false;
						}
						else if(resp.getTimeStamp().getTime() >= hashTTime.get(resp.getKey()).getTime()) {
							consis = true;
						}
						else {
							consis = false;
							hashTTime.put(resp.getKey(), resp.getTimeStamp());
						}
					}
					//caso GET_OK e value diferente de NULL
					if(resp.getStatus().equals("GET_OK") & resp.getValue().equals("NULL") == false & consis == true) {
						System.out.println("GET " +"Key:" + resp.getKey() + " Value:" + resp.getValue() + " obtido do servidor: {" + resp.getIpDest() + " : " + resp.getPortDest() + "}" + "\nTimeStamp do servidor: " + resp.getTimeStamp() + "\nMeu TimeStamp: " + new Timestamp(System.currentTimeMillis()));
						hashTTime.put(resp.getKey(), resp.getTimeStamp());
					}
					//caso GET_OK e value igual NULL
					else if(resp.getStatus().equals("GET_OK") & resp.getValue().equals("NULL") == true & consis == true) {
						System.out.println("GET " + resp.getKey() + " : " + resp.getValue());
					}
					//caso não encontre o value
					else if(resp.getStatus().equals("GET_OK") & consis == false) {
						System.out.println("TRY_OTHER_SERVER_OR_LATER \n");
						//tenta a cada 1s por 5s para dar o tempo de terminar o PUT se o processo tiver sido iniciado
						int cont = 5;
						while (cont == 0) {
							cont = cont-1;
							TCPClient.sleep(1000);
						}
						TCPClient.sleep(1000);
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void sendMessage(Mensagem msg) {
		//mensagem para Json
		newMsg = new Gson().toJson(msg);
		
		//cria thread para enviar mensagem
		TCPClient client = new TCPClient(newMsg);
		client.start();
	}
	
	
}


