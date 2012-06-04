package evbot.youtubearchiver.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import evbot.youtubearchiver.httpserver.ScriptInstaller;

public class MainFrame extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane;
	private ConfigurationPane configurationPane;
	
	private JButton ok;
	private JButton cancel;
	private JButton installScript;
	
	public MainFrame() {
		
		setTitle("Youtube-Archiver");
		init();
		
	}
	
	private void init() {
		
//		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e){
			System.err.println("Unable to set theme");
			e.printStackTrace();
		}
		
		tabbedPane = new JTabbedPane();
		configurationPane = new ConfigurationPane();
		
		tabbedPane.addTab("Configuration", configurationPane);
		
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		installScript = new JButton("Install Script");
		
		ok.addActionListener(this);
		cancel.addActionListener(this);
		installScript.addActionListener(this);
		
		JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
		controls.add(ok);
		controls.add(cancel);
		if(Desktop.isDesktopSupported())
			controls.add(installScript);
		
		add(tabbedPane, BorderLayout.CENTER);
		add(controls, BorderLayout.SOUTH);
		
		pack();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String command = e.getActionCommand();
		
		System.out.println(command);
		
		if (command.equals("Install Script")) {
			try {
				new Thread(new ScriptInstaller()).start();
				Desktop.getDesktop().browse(new URI("http://localhost:8841/YoutubeArchiver.user.js"));
				
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
}
