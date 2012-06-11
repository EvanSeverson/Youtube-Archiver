package evbot.youtubearchiver;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.SwingUtilities;

import evbot.youtubearchiver.config.ConfigLoader;
import evbot.youtubearchiver.config.Configuration;
import evbot.youtubearchiver.gui.MainFrame;
import evbot.youtubearchiver.httpserver.ConnectionHandler;

public class YoutubeArchiver {
	
	public static ConfigLoader CONFIG_LOADER;
	
	public static MainFrame MAIN_FRAME;


	public static void main(String[] args) {
		
		System.out.println("Starting Youtube-Archiver by Evan");
		
//		try {
//			PrintStream err = new PrintStream(new FileOutputStream("errors.txt", true));
//			System.setErr(err);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		
		CONFIG_LOADER = new ConfigLoader("config.ini");
		MAIN_FRAME = new MainFrame();
		
		if(!CONFIG_LOADER.loadConfig() || Configuration.SHOW_GUI) {
			
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					
					MAIN_FRAME.setVisible(true);
					
				}
			});
		} else {
			startServer();
		}
		
	}
	
	public static void startServer() {
		new ConnectionHandler().start();
		
	}

}
