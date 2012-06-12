package evbot.youtubearchiver.httpserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import evbot.youtubearchiver.config.Configuration;
import evbot.youtubearchiver.download.Playlist;
import evbot.youtubearchiver.download.VideoListFetcher;
import evbot.youtubearchiver.download.VideoQueue;

public class ConnectionHandler {
	
	private ServerSocket serverSocket;
	public Thread serverThread;
	
	public ConnectionHandler() {
		try {
			
			serverSocket = new ServerSocket(8840, 0, InetAddress.getByName("localhost"));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start() {
		
		serverThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while(true) {
					try {
						
						ClientHandler clientHandler = new ClientHandler(serverSocket.accept());
						new Thread(clientHandler).start();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		
		serverThread.start();
		
	}
	
	public void stop() {
		serverThread = null;
	}
	
	private class ClientHandler implements Runnable {
		
		Socket socket;
		
		public ClientHandler(Socket s) {
			socket = s;
		}
		
		@Override
		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintStream out = new PrintStream(socket.getOutputStream());
				
				StringTokenizer token = new StringTokenizer(in.readLine());
				String method = token.nextToken();
				String address = token.nextToken().substring(1);
				
				while(in.ready()) {
					in.readLine();
				}
				
				if(!method.equals("GET")) {
					socket.close();
					return;
				}
				
				if(address.startsWith("video/")) {
					sendCloseTab(out);
					String id = address.substring(address.indexOf('/') + 1);
					VideoQueue.addToQueue(id, new File(Configuration.SAVE_DIR, "Videos"));
				} else if (address.startsWith("channel/")) {
					sendCloseTab(out);
					String channelName = address.substring(address.indexOf("/") + 1);
					String[] ids = VideoListFetcher.getChannel(channelName);
					for(String id : ids) {
						VideoQueue.addToQueue(id, new File(Configuration.SAVE_DIR, "Channels/" + channelName));
					}
				} else if(address.startsWith("playlist/")) {
					sendCloseTab(out);
					String playlistID = address.substring(address.indexOf("/") + 1);
					Playlist pl = new Playlist(playlistID);
					String[] ids = VideoListFetcher.getPlaylist(playlistID);
					for(String id : ids) {
						VideoQueue.addToQueue(id, new File(Configuration.SAVE_DIR, "Playlists/" + pl.getName() + "[" + pl.getAuthor() + "][" + playlistID + "]"));
					}
				} else {
					sendError(out);
				}
				
				
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
			}
//			 StringTokenizer token = new StringTokenizer();
			
		}
		
	}
	
	private void sendCloseTab(PrintStream out) {
		
		out.print("HTTP/1.1 200 OK\r\n\r\n");
		out.println("<html><body onload='window.close()'><h1>Great job!</h1></body></html>");
		out.close();
		
		
	}
	
	private void sendError(PrintStream out) {

		out.print("HTTP/1.1 404 Not Found\r\n\r\n");
		out.println("<html><body><h1>What am i supposed to do with this link?</h1></body></html>");
		out.close();
		
	}
	
}
