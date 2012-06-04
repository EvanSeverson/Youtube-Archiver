package evbot.youtubearchiver.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.security.auth.callback.ConfirmationCallback;
import javax.swing.plaf.SliderUI;

public class ConfigLoader {
	
	private File configFile;
	
	public ConfigLoader(String filename) {
		
		configFile = new File(filename);
		
	}
	
	public boolean loadConfig() {
		
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(configFile));
			HashMap<String, String> configMap = new HashMap<String, String>();
			String line;
			while((line = in.readLine()) != null) {
				
				if(line.startsWith(";")) {
					continue;
				}
				line = line.split(";")[0];
				
				if(!line.contains("=")) {
					continue;
				}
				
				String[] split = line.split("=", 2);
				if(split.length == 1) {
					continue;
				}
				configMap.put(split[0], split[1]);
			}
			
			
			boolean returnval = true;
			
			if(configMap.containsKey("save-direcotry")) {
				File saveDirectory = new File(configMap.get("save-directory"));
				Configuration.SAVE_DIR = saveDirectory;
			} else {
				returnval = false;
			}
			
			if(configMap.containsKey("download-limit")) {
				try {
					
					int downloadLimit = Integer.parseInt(configMap.get("download-limit"));
					
					Configuration.DOWNLOAD_SPEED_LIMIT = downloadLimit;
					
				} catch (NumberFormatException e) {
					returnval = false;
				}
			} else {
				returnval = false;
			}
			
			if(configMap.containsKey("download-threads")) {
				try {
					
					int downloadThreads = Integer.parseInt(configMap.get("download-threads"));
					
					Configuration.MAX_DOWNLOAD_THREADS = downloadThreads;
					
				} catch (NumberFormatException e) {
					returnval = false;
				}
			} else {
				returnval = false;
			}
			
			if(configMap.containsKey("zip-archives")) {
				String zipArchives = configMap.get("zip-archives");
				if(zipArchives.equalsIgnoreCase("true")) {
					Configuration.TIMESTAMP_VIDEOS = true;
				} else if(zipArchives.equalsIgnoreCase("false")) {
					Configuration.TIMESTAMP_VIDEOS = false;
				} else {
					returnval = false;
				}
			} else {
				returnval = false;
			}
			
			if(configMap.containsKey("timestamp-videos")) {
				String timestamp = configMap.get("timestamp-videos");
				if(timestamp.equalsIgnoreCase("true")) {
					Configuration.TIMESTAMP_VIDEOS = true;
				} else if(timestamp.equalsIgnoreCase("false")) {
					Configuration.TIMESTAMP_VIDEOS = false;
				} else {
					returnval = false;
				}
			} else {
				returnval = false;
			}
			
			if(configMap.containsKey("timestamp-format")) {
				Configuration.TIMESTAMP_FORMAT = new SimpleDateFormat(configMap.get("timestamp-format"));
			} else {
				returnval = false;
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("No configuration file found, using default settings.");
		} catch (IOException e) {
		}
		return false;
		
	}
	
	public void saveConfig(String saveDir, String downloadLimit, String downloadThreads, boolean zipArchives, boolean timestampVideos, String timestampFormat) {
		
		try {
			PrintStream out = new PrintStream(configFile);
			out.println("save-directory=" + saveDir);
			out.println("download-limit=" + downloadLimit);
			out.println("download-threads=" + downloadThreads);
			out.println("zip-archives=" + zipArchives);
			out.println("timestamp-videos=" + timestampVideos);
			out.println("timestampFormat=" + timestampFormat);
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println("Unable to esablish config file print stream");
			e.printStackTrace();
		}
		
		
		
	}

}
