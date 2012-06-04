package evbot.youtubearchiver;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import javax.swing.SwingUtilities;

import evbot.youtubearchiver.config.ConfigLoader;
import evbot.youtubearchiver.gui.MainFrame;

public class YoutubeArchiver {
	
	public static ConfigLoader configLoader;
	
	public static MainFrame mainFrame;


	public static void main(String[] args) {
		
		System.out.println("Starting Youtube-Archiver by Evan");
		
		try {
			PrintStream err = new PrintStream("errors.txt");
			System.setErr(err);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		configLoader = new ConfigLoader("config.ini");
		mainFrame = new MainFrame();
		
		if(!configLoader.loadConfig()) {
			
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					
					mainFrame.setVisible(true);
					
				}
			});
		}
		
	}

}
