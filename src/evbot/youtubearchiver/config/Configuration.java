package evbot.youtubearchiver.config;

import java.io.File;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;

public class Configuration {
	
	public static File SAVE_DIR = getCurrentDirectory();
	
	public static int DOWNLOAD_SPEED_LIMIT;
	
	public static int MAX_DOWNLOAD_THREADS;
	
	public static boolean ZIP_ARCHIVES;
	
	public static boolean TIMESTAMP_VIDEOS;
	
	public static SimpleDateFormat TIMESTAMP_FORMAT;
	
	static {
		
		DOWNLOAD_SPEED_LIMIT = 0;
		MAX_DOWNLOAD_THREADS = 15;
		ZIP_ARCHIVES = true;
		TIMESTAMP_VIDEOS = false;
		TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
		
	}
	
	private static File getCurrentDirectory() {
//		try {
//			return new File(Runtime.getRuntime().getClass().getProtectionDomain().getCodeSource()
//					.getLocation().toURI().getPath()
//					+ "Youtube-Archives" +  File.separator);
//		} catch (URISyntaxException e) {
//		}
//		return new File("");
		return new File("Youtube-Archives");
	}
	
}
