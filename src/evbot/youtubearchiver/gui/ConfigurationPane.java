package evbot.youtubearchiver.gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConfigurationPane extends JPanel {
	
	public JTextField saveDirectory;
	private JButton saveDirectoryBrowse;
	
	public ConfigurationPane() {
		
		init();
		
	}
	
	private void init() {
		
		setLayout(new GridLayout(1, 0));
		saveDirectory = new JTextField(40);
		saveDirectoryBrowse = new JButton("...");
		
		ConfigurationField saveDirectoryConfigurationPane = 
				new ConfigurationField("Save to:", saveDirectory, saveDirectoryBrowse);
		
		add(saveDirectoryConfigurationPane);
		
	}
	
	private class ConfigurationField extends JPanel {
		
		public ConfigurationField(String label, JComponent... components) {
			
			setLayout(new FlowLayout(FlowLayout.TRAILING));
			
			add(new JLabel(label));
			for(JComponent c : components) {
				add(c);
			}
			
		}
		
	}
	
}
