package evbot.youtubearchiver.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ScriptInstaller implements Runnable{

	@Override
	public void run() {
		
		try {
			ServerSocket serverSocket = new ServerSocket(8841, 0, InetAddress.getByName("localhost"));
			Socket sock = serverSocket.accept();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			PrintStream out = new PrintStream(sock.getOutputStream());
			
			String line;
			in.readLine();
			while(in.ready()) {
				System.out.println(in.readLine());
			}
			
			out.print("HTTP/1.1 OK\r\n");
			
			BufferedReader scriptIn = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("YoutubeArchiver.user.js")));
			while((line = scriptIn.readLine()) != null) {
				out.println(line);
				System.out.println(line);
			}
			sock.close();
			serverSocket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
