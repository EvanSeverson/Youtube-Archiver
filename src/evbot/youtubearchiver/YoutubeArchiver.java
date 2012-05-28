package evbot.youtubearchiver;

import javax.swing.SwingUtilities;

import evbot.youtubearchiver.gui.MainFrame;

public class YoutubeArchiver {


	public static void main(String[] args) {
		
		System.out.println("Starting Youtube-Archiver by Evan");
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				
				MainFrame mainFrame = new MainFrame();
				mainFrame.setVisible(true);
				
			}
		});
		
	}

}
