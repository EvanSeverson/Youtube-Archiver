package evbot.youtubearchiver.download;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;

import evbot.youtubearchiver.config.Configuration;

public class VideoQueue{
	
	private static ArrayList<String> VIDEO_QUEUE = new ArrayList<String>();
	private static ArrayList<File> LOCATION_QUEUE = new ArrayList<File>();
	
	private static boolean DOWNLOADING = false;
	
	public static volatile int threads = 0;
	
	public static void addToQueue(String id, File location) {
		
		VIDEO_QUEUE.add(id);
		LOCATION_QUEUE.add(location);
		
		if(!DOWNLOADING) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					downloadCue();
					
				}
			}).start();
		}
		
	}
	
	private static void downloadCue()  {
		DOWNLOADING = true;
		while(!VIDEO_QUEUE.isEmpty()) {
			
			LOCATION_QUEUE.get(0).mkdirs();
			threads++;
			
			final String id = VIDEO_QUEUE.get(0);
			final File location = LOCATION_QUEUE.get(0);
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						VideoDownloader vDown = new VideoDownloader(id, location);
						vDown.download();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					threads--;
					
				}
			}).start();

			LOCATION_QUEUE.remove(0);
			VIDEO_QUEUE.remove(0);
			
			while(threads >= Configuration.MAX_DOWNLOAD_THREADS) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {}
			}
		}
		DOWNLOADING = false;
	}
	
}
