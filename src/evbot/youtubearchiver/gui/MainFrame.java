package evbot.youtubearchiver.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainFrame extends JFrame{
	
	private JTabbedPane tabbedPane;
	public ConfigurationPane configurationPane;
	
	public MainFrame() {
		
		init();
		
	}
	
	private void init() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){
			//Unable to set window theme
		}
		
		tabbedPane = new JTabbedPane();
		configurationPane = new ConfigurationPane();
		
		tabbedPane.addTab("Configuration", configurationPane);
		add(tabbedPane);
		pack();
		
	}
	
}
