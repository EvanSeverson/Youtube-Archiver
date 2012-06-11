package evbot.youtubearchiver.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.NumericShaper;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import evbot.youtubearchiver.YoutubeArchiver;
import evbot.youtubearchiver.config.Configuration;

public class ConfigurationPane extends JPanel implements ActionListener, KeyListener {
	
	public static final long serialVersionUID = 1L;
	
	public JTextField saveDirectoryTextField;
	public JButton saveDirectoryBrowseButton;
	public JFileChooser saveDirectoryFileChooser;
	public ConfigurationField saveDirectoryConfigurationField;
	
	public JTextField downloadSpeedLimitTextField;
	public JLabel downloadSpeedLimitLabel;
	public ConfigurationField downloadSpeedLimitConfigurationField;
	
	public JTextField maxThreadsTextField;
	public ConfigurationField maxThreadsConfigurationField;
	
	public JCheckBox zipArchivesCheckBox;
	public ConfigurationField zipArchivesConfigurationField;
	
	
	public JCheckBox timeStampCheckBox;
	public ConfigurationField timeStampConfigurationField;
	
	public JButton save;
	
	public ConfigurationPane() {
		
		init();
		
	}
	
	private void init() {
		
		setLayout(new GridLayout(0, 1));
		
		saveDirectoryTextField = new JTextField(40);
//		try {
//			saveDirectoryTextField.setText(
//					this.getClass().getProtectionDomain().getCodeSource()
//					.getLocation().toURI().getPath()
//					+ "Youtube-Archives" +  File.separator);
//		} catch (URISyntaxException e) {
//		}
		try {
			saveDirectoryTextField.setText(new File("Youtube-Archives").getCanonicalPath());
		} catch (IOException e) {
		}
		saveDirectoryBrowseButton = new JButton("...");
		saveDirectoryFileChooser = new JFileChooser();
		saveDirectoryBrowseButton.setActionCommand("browse");
		saveDirectoryBrowseButton.addActionListener(this);
		saveDirectoryFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		saveDirectoryFileChooser.addActionListener(this);
		saveDirectoryConfigurationField = 
				new ConfigurationField("Save to:", saveDirectoryTextField,
						saveDirectoryBrowseButton);
		
		downloadSpeedLimitTextField = new JTextField(10);
		downloadSpeedLimitTextField.setText("0");
		downloadSpeedLimitTextField.addKeyListener(this);
		downloadSpeedLimitLabel = new JLabel("KB/s");
		downloadSpeedLimitConfigurationField =
				new ConfigurationField("Download Speed Limit:", downloadSpeedLimitTextField, downloadSpeedLimitLabel);
		
		maxThreadsTextField = new JTextField(5);
		maxThreadsTextField.setText("5");
		maxThreadsTextField.addKeyListener(this);
		maxThreadsConfigurationField =
				new ConfigurationField("Max Downlaod Threads:", maxThreadsTextField);
		
		zipArchivesCheckBox = new JCheckBox();
		zipArchivesCheckBox.setSelected(true);
		zipArchivesConfigurationField =
				new ConfigurationField("Zip Archives:", zipArchivesCheckBox);
		
		timeStampCheckBox = new JCheckBox();
		timeStampCheckBox.setSelected(false);
		timeStampConfigurationField =
				new ConfigurationField("Timestamp video:", timeStampCheckBox);
		
		save = new JButton("Save");
		save.addActionListener(this);
		
		add(saveDirectoryConfigurationField);
		add(downloadSpeedLimitConfigurationField);
		add(maxThreadsConfigurationField);
		add(zipArchivesConfigurationField);
		add(timeStampConfigurationField);
//		add(save);
		
	}
	
	private class ConfigurationField extends JPanel {
		
		private static final long serialVersionUID = 1L;
		String label;

		public ConfigurationField(String label, Component... components) {
			
			this.label = label;
			
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
		
		public String getLabel() {
			return label;
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
		
		if(command.equals("Save")) {
			
			saveConfiguration();
		}
		
	}

	public void saveConfiguration() {
		if(!checkFields()) {
			return;
		}
		
		applyConfig();
		YoutubeArchiver.CONFIG_LOADER.saveConfig(
				saveDirectoryTextField.getText(),
				downloadSpeedLimitTextField.getText(),
				maxThreadsTextField.getText(),
				zipArchivesCheckBox.isSelected(),
				timeStampCheckBox.isSelected(),
				Configuration.TIMESTAMP_FORMAT.toPattern(),
				Configuration.SHOW_GUI);
	}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {
		if(!Character.isDigit(e.getKeyChar())) {
			e.consume();
		}
	}
	
	public void displayConfig() {
		
		try {
			saveDirectoryTextField.setText(Configuration.SAVE_DIR.getCanonicalPath());
		} catch (IOException e) {
		}
		
		downloadSpeedLimitTextField.setText(Configuration.DOWNLOAD_SPEED_LIMIT + "");
		maxThreadsTextField.setText(Configuration.MAX_DOWNLOAD_THREADS + "");
		zipArchivesCheckBox.setSelected(Configuration.ZIP_ARCHIVES);
		timeStampCheckBox.setSelected(Configuration.TIMESTAMP_VIDEOS);
		
	}
	
	public void applyConfig() {
		
		Configuration.SAVE_DIR = new File(saveDirectoryTextField.getText());
		try {
			Configuration.DOWNLOAD_SPEED_LIMIT = Integer.parseInt(downloadSpeedLimitTextField.getText());
		} catch(NumberFormatException e) {
			System.err.println("Downlaod speed limit not integer");
			e.printStackTrace();
		}
		try {
		Configuration.MAX_DOWNLOAD_THREADS = Integer.parseInt(maxThreadsTextField.getText());
		} catch(NumberFormatException e) {
			System.err.println("Max download threads not integer");
			e.printStackTrace();
		}
		Configuration.ZIP_ARCHIVES = zipArchivesCheckBox.isSelected();
		Configuration.TIMESTAMP_VIDEOS = timeStampCheckBox.isSelected();
		
	}
	
	public boolean checkFields() {
		try {
			new File(saveDirectoryTextField.getText()).getCanonicalPath();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Invalid parameter at: " + saveDirectoryConfigurationField.getLabel());
			return false;
		}
		
		try {
			Integer.parseInt(downloadSpeedLimitTextField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Invalid parameter at: " + downloadSpeedLimitConfigurationField.getLabel());
		}
		
		try {
			Integer.parseInt(maxThreadsTextField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Invalid parameter at: " + maxThreadsConfigurationField.getLabel());
		}
		
		return true;
	}
	
}
