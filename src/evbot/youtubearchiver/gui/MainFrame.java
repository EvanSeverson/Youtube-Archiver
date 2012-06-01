package evbot.youtubearchiver.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class MainFrame extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane;
	private ConfigurationPane configurationPane;
	
	private JButton ok;
	private JButton cancel;
	
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
			//Unable to set window theme
		}
		
		tabbedPane = new JTabbedPane();
		configurationPane = new ConfigurationPane();
		
		tabbedPane.addTab("Configuration", configurationPane);
		
		ok = new JButton("OK");
		cancel = new JButton("Cancel");
		
		ok.addActionListener(this);
		cancel.addActionListener(this);
		
		JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
		controls.add(ok);
		controls.add(cancel);
		
		add(tabbedPane, BorderLayout.CENTER);
		add(controls, BorderLayout.SOUTH);
		
		pack();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		System.out.println(e.getActionCommand());
		
	}
	
}
