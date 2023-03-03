package tcpdemo;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
public class TCPServerConcorrente {
	
	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(9000);
		
		while(true) {
			System.out.println("Esperando conn");
			Socket no = serverSocket.accept(); //blocking
			System.out.println("Conn aceita");
			
			ThreadAtendimento thread = new ThreadAtendimento(no);
			thread.start();
		}
	}
}
