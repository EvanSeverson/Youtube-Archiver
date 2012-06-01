package evbot.youtubearchiver.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class ConfigurationPane extends JPanel implements ActionListener, ItemListener{
	
	public static final long serialVersionUID = 1L;
	
	public JTextField saveDirectoryTextField;
	public JButton saveDirectoryBrowseButton;
	public JFileChooser saveDirectoryFileChooser;
	public ConfigurationField saveDirectoryConfigurationPane;
	
	public JTextField downloadSpeedLimitTextField;
	public JLabel downloadSpeedLimitLabel;
	public ConfigurationField downloadSpeedLimitConfigurationField;
	
	public JTextField maxThreadsTextField;
	public ConfigurationField maxThreadsConfigurationField;
	
	public JCheckBox zipVideosCheckBox;
	public ConfigurationField zipVideosConfigurationField;
	
	
	public JCheckBox timeStampCheckBox;
	public ConfigurationField timeStampConfigurationField;
	
	public JButton save;
	
	public ConfigurationPane() {
		
		init();
		
	}
	
	private void init() {
		
		setLayout(new GridLayout(0, 1));
		
		saveDirectoryTextField = new JTextField(40);
		try {
			saveDirectoryTextField.setText(
					this.getClass().getProtectionDomain().getCodeSource()
					.getLocation().toURI().getPath()
					+ "Youtube-Archives" +  File.separator);
		} catch (URISyntaxException e) {
		}
		saveDirectoryBrowseButton = new JButton("...");
		saveDirectoryFileChooser = new JFileChooser();
		saveDirectoryBrowseButton.setActionCommand("browse");
		saveDirectoryBrowseButton.addActionListener(this);
		saveDirectoryFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		saveDirectoryFileChooser.addActionListener(this);
		saveDirectoryConfigurationPane = 
				new ConfigurationField("Save to:", saveDirectoryTextField,
						saveDirectoryBrowseButton);
		
		downloadSpeedLimitTextField = new JTextField(10);
		downloadSpeedLimitTextField.setText("0");
		downloadSpeedLimitLabel = new JLabel("KB/s");
		downloadSpeedLimitConfigurationField =
				new ConfigurationField("Download Speed Limit:", downloadSpeedLimitTextField, downloadSpeedLimitLabel);
		
		maxThreadsTextField = new JTextField(5);
		maxThreadsTextField.setText("15");
		maxThreadsConfigurationField =
				new ConfigurationField("Max Downlaod Threads:", maxThreadsTextField);
		
		zipVideosCheckBox = new JCheckBox();
		zipVideosCheckBox.setSelected(true);
		zipVideosConfigurationField =
				new ConfigurationField("Zip Videos:", zipVideosCheckBox);
		
		timeStampCheckBox = new JCheckBox();
		timeStampCheckBox.setSelected(false);
		timeStampCheckBox.addItemListener(this);
		timeStampConfigurationField =
				new ConfigurationField("Timestamp video:", timeStampCheckBox);
		
		save = new JButton("Save");
		save.addActionListener(this);
		
		add(saveDirectoryConfigurationPane);
		add(downloadSpeedLimitConfigurationField);
		add(maxThreadsConfigurationField);
		add(zipVideosConfigurationField);
		add(timeStampConfigurationField);
		add(save);
		
	}
	
	private class ConfigurationField extends JPanel {
		
		private static final long serialVersionUID = 1L;

		public ConfigurationField(String label, Component... components) {
			
			setLayout(new FlowLayout(FlowLayout.LEADING));
			add(new JLabel(label));
			
			for(Component c : components) {
				add(c);
			}
			
		}
		
		@Override
		public void setEnabled(boolean enabled) {
			for(Component c : getComponents()) {
				c.setEnabled(enabled);
			}
			super.setEnabled(enabled);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		System.out.println(command);
		
		if(command.equals("browse")) {
			
			int returnVal = saveDirectoryFileChooser.showOpenDialog(this);
			
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					
					saveDirectoryTextField.setText(saveDirectoryFileChooser.getSelectedFile().getCanonicalPath());
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		
	}
}
