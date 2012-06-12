package evbot.youtubearchiver.download;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import evbot.youtubearchiver.Utils;
import evbot.youtubearchiver.config.Configuration;

public class VideoListFetcher {

	public static String[] getChannel(String name) {
		ArrayList<String> videoIDs = new ArrayList<String>();
		while(VideoQueue.threads >= Configuration.MAX_DOWNLOAD_THREADS) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		VideoQueue.threads++;
		int pageNumber = 1;
		while(true) {
			try {
				URL videoPageURL = new URL("http://www.youtube.com/user/" + name + "/videos?page=" + pageNumber);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				DownloadUtils.download(videoPageURL, out);
				String page = out.toString();
				if(!page.contains("yt-c3-grid-item\">")) {
					break;
				}
				String[] split1 = page.split("yt-c3-grid-item\">");
				for(int i = 1; i < split1.length; i++) {
					videoIDs.add(Utils.getBetween(split1[i], "watch\\?v=", "&amp;"));
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				break;
			}
			pageNumber++;
		}
		VideoQueue.threads--;
		
		return videoIDs.toArray(new String[videoIDs.size()]);
	}
	
	public static String[] getPlaylist(String id) {
		ArrayList<String> videoIDs = new ArrayList<String>();
		while(VideoQueue.threads >= Configuration.MAX_DOWNLOAD_THREADS) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		VideoQueue.threads++;
		int pageNumber = 1;
		while(true) {
			try {
				System.out.println(pageNumber);
				URL videoPageURL = new URL("http://www.youtube.com/playlist?list=" + id + "&page=" + pageNumber);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				DownloadUtils.download(videoPageURL, out);
				String page = out.toString();
				if(!page.contains("playlist-video-item")) {
					break;
				}
				String[] split1 = page.split("<li class=\"playlist-video-item");
				for(int i = 1; i < split1.length; i++) {
					videoIDs.add(Utils.getBetween(split1[i], "watch\\?v=", "&amp;"));
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				break;
			}
			pageNumber++;
		}
		VideoQueue.threads--;
		
		return videoIDs.toArray(new String[videoIDs.size()]);
	}
	
}
