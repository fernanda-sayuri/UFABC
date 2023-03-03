package tcpdemo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class TCPClient {
	public static void main(String[] args) throws Exception {
		//cria conexão
		Socket s = new Socket("127.0.0.1", 9000);
		
		//envia informações
		OutputStream os = s.getOutputStream();
		DataOutputStream writer = new DataOutputStream(os);
		
		//recebe info
		InputStreamReader is = new InputStreamReader(s.getInputStream());
		BufferedReader reader = new BufferedReader(is);
		
		//captura infos do teclado
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		String texto = inFromUser.readLine(); //blocking
		
		writer.writeBytes(texto+"\n");
	
		//recebe do servidor
		String response = reader.readLine(); //blocking
		System.out.println("DoServidor:"+response);
		
		s.close();
	}
}
