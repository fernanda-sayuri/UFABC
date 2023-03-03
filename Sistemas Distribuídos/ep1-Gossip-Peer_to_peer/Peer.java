package teste;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

import teste.Peer.UDPServer;

public class Peer extends Thread{
	public static byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }

    public static Object convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }
    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }
	static class UDPServer extends Thread {
		private int numberPort = 0;
		private String pathFolder = "";
		private ArrayList<Integer> arrPeers; 
		
		public UDPServer(int numberPort, String pathFolder, ArrayList<Integer> arrPeers ) {
			this.numberPort = numberPort;
			this.pathFolder = pathFolder;
			this.arrPeers = arrPeers;
		}
		
		@Override
		public void run() {
			try {
				DatagramSocket serverSocket = new DatagramSocket(numberPort);
				
				while(true) {
					byte[] recBuffer = new byte[1024];
					DatagramPacket recPkt = new DatagramPacket(recBuffer, recBuffer.length);
					//System.out.println("esperando alguma mensagem");
					
					//recebendo alguma mensagem
					serverSocket.receive(recPkt); //blocking
					Mensagem msgDecoded = (Mensagem) convertFromBytes(recPkt.getData());
					
					int portaa = msgDecoded.getPort();
					
					//verificando se a mensagem é do tipo search ou response
					if(msgDecoded.getMsgType().equals("SEARCH")) {
						ArrayList<Path> arrFiles = getFiles(pathFolder);
						
						ArrayList<String> arrStrFiles = new ArrayList<String>();
						
						arrFiles.forEach((Path p) -> {
							arrStrFiles.add(p.toString());
						});
						
						//se contém o arquivo procurado, cria uma nova mensagem com o tipo response e retorna ao peer inicial
						if(arrStrFiles.contains(msgDecoded.getMsg())) {
							System.out.println("tenho "+ msgDecoded.getMsg());
							
							Mensagem newMsg = new Mensagem(msgDecoded.getMsg(), "RESPONSE", numberPort, msgDecoded.getIp(), msgDecoded.getArrPeers(), 0);
							sendMessage(msgDecoded.getIp(), msgDecoded.getPort(), newMsg);
						}
						//se não contém a mensagem, cria uma nova mensagem e pergunta de forma aleatória para os peers vizinhos
						else {
							//tratamento para busca de arquivos de não existem
							if(msgDecoded.getBusca() < 10) {
								Mensagem newMsg = new Mensagem(msgDecoded.getMsg(), "SEARCH", msgDecoded.getPort(), msgDecoded.getIp(), msgDecoded.getArrPeers(), msgDecoded.getBusca()+1);
								
								Random gerador = new Random();
								int numRandom = gerador.nextInt(arrPeers.size()); //random entre peers	
								
								int numberPortPeerNeighboor = arrPeers.get(numRandom);
								System.out.println("não tenho "+ msgDecoded.getMsg()+", vou encaminhar para o peer"+ recPkt.getAddress().getHostAddress()+":"+numberPortPeerNeighboor);
								
								sendMessage(recPkt.getAddress().getHostAddress(), numberPortPeerNeighboor, newMsg);
							}
							else {
								Mensagem newMsg = new Mensagem(msgDecoded.getMsg(), "RESPONSEN", numberPort, msgDecoded.getIp(), msgDecoded.getArrPeers(), 0);
								sendMessage(msgDecoded.getIp(), msgDecoded.getPort(), newMsg);
							}							
						}
						
					}
					
					if(msgDecoded.getMsgType().equals("RESPONSE")) {
						System.out.println("Peer com o arquivo procurado: "+msgDecoded.getIp()+":"+msgDecoded.getPort()+" " +msgDecoded.getMsg());
					}
					if(msgDecoded.getMsgType().equals("RESPONSEN")) {
						System.out.println("Arquivo não encontrado ");
					}
					
				}
			} catch (Exception e) {
				
			}			
		}
	}
	public static void sendMessage(String ip, int numberPort, Mensagem msg) throws IOException {
		DatagramSocket clientSocket = new DatagramSocket();
		
		InetAddress IPAdress = InetAddress.getByName(ip);
		
		byte[] sendData = new byte[1024];
		sendData = convertToBytes(msg);
		
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAdress, numberPort);
		
		clientSocket.send(sendPacket);
		//System.out.println("mensagem enviada para servidor");
	}
	
	public static void main(String[] args) throws Exception {		
		Inicializa();
	}
	
	public static void Inicializa() throws Exception{
		System.out.println("Insira o IP::");
		Scanner leitor1 = new Scanner(System.in);
		String ip = leitor1.nextLine();
		InetAddress ipAddress = InetAddress.getByName(ip);
		
		System.out.println("Insira a porta do Peer:");
		Scanner leitor2 = new Scanner(System.in);
		String portPeer = leitor2.nextLine();
		
		System.out.println("Insira a pasta dos arquivos:");
		Scanner leitor3 = new Scanner(System.in);
		String pathFolder = leitor3.nextLine();
		
		System.out.println("Insira o IP de outro Peer:");
		Scanner leitor4 = new Scanner(System.in);
		String ip2 = leitor4.nextLine();
		InetAddress ipAddress2 = InetAddress.getByName(ip2);
		
		System.out.println("Insira a porta de outro Peer:");
		Scanner leitor5 = new Scanner(System.in);
		String portP1 = leitor5.nextLine();
		
		System.out.println("Insira o IP de mais um outro Peer:");
		Scanner leitor6 = new Scanner(System.in);
		String ip3 = leitor6.nextLine();
		InetAddress ipAddress3 = InetAddress.getByName(ip3);
		
		System.out.println("Insira a porta de mais um outro Peer:");
		Scanner leitor7 = new Scanner(System.in);
		String portP2 = leitor7.nextLine();
		
		//cria array com peers vizinhos
		ArrayList<Integer> arrPeers = new ArrayList();
		
		int portNB1 = Integer.parseInt(portP1);
		int portNB2 = Integer.parseInt(portP2);
		arrPeers.add(portNB1);
		arrPeers.add(portNB2);
		
		int port = Integer.parseInt(portPeer);
		Monitoramento(pathFolder, ip, port);
		
		//iniciar Server
		new UDPServer(port, pathFolder, arrPeers).start();
	
		ArrayList<String> arrSearched = new ArrayList();
		while(true) {
			System.out.println(" ");
			System.out.println("Insira o arquivo que deseja buscar:");
			Scanner leitor8 = new Scanner(System.in);
			String arquivo = leitor8.nextLine();
			
			if(arrSearched.contains(arquivo)) {
				System.out.println("requisição já processada para "+ arquivo);
			}
			else {
				arrSearched.add(arquivo);
				
				Random gerador = new Random();
				int numRandom = gerador.nextInt(arrPeers.size()-1); //random entre peers
				
				int numberPortPeerNeighboor = arrPeers.get(numRandom);
				
				Mensagem msg = new Mensagem(arquivo, "SEARCH", port, ip, arrPeers, 0);
				sendMessage(ip, numberPortPeerNeighboor, msg);
			}
			
		}
		
	}
	public static ArrayList<Path> getFiles(String pathFolder) {
		ArrayList<Path> arrFiles = new ArrayList();
		
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(pathFolder))) {
		    for (Path file: stream) {
		        arrFiles.add(file.getFileName());
		    }
		} catch (IOException | DirectoryIteratorException ex) {
		    System.err.println(ex);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arrFiles;
	}
	public static void Monitoramento(String pathFolder, String ip, int portPeer) throws Exception{
		//verifica arquivos na pasta
		ArrayList<Path> arrFiles = getFiles(pathFolder);	
		System.out.print("arquivos da pasta: ");
		for (int i = 0; i < arrFiles.size(); i++) {
			System.out.print(arrFiles.get(i) + " ");

		}
		
		new Thread(){
			public void run(){
				try {
					sleep(30000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				while(true){
					try {
						arrFiles.clear();
						try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(pathFolder))) {
						    for (Path file: stream) {
						        arrFiles.add(file.getFileName());
						    }
						} catch (IOException | DirectoryIteratorException ex) {
						    System.err.println(ex);
						}
						System.out.print("sou peer: "+ip+":"+portPeer + " ");
						for (int i = 0; i < arrFiles.size(); i++) {
							System.out.print(arrFiles.get(i) + " ");

						}
						System.out.println(" ");
						sleep(30000);//Para por 30s
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
	}
}
