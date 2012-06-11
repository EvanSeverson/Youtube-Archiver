package evbot.youtubearchiver.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import evbot.youtubearchiver.Utils;
import evbot.youtubearchiver.VideoFormat;
import evbot.youtubearchiver.config.Configuration;

public class VideoDownloader {
	
	private String id;
	private File location;
	private File directory;
	
	private boolean zip;
	
	private byte htmlPage[];
	private String htmlPageS;
	private String title;
	private String date;
	VideoFormat format;
	
	public VideoDownloader(String id, File directory) throws MalformedURLException {
		
		this.id = id;
		this.directory = directory;
		zip = Configuration.ZIP_ARCHIVES;
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DownloadUtils.download(new URL("http://www.youtube.com/watch?v=" + id), bout);
		
		htmlPage = bout.toByteArray();
		try {
			htmlPageS = bout.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			htmlPageS = new String(htmlPage);
			e.printStackTrace();
		}
		
//		htmlPageS = unescapeHTML(htmlPageS);
		title = Utils.removeWhiteSpace(Utils.getBetween(Utils.unescapeHTML(htmlPageS), "<title>", "- YouTube"));
		title = Utils.toLegalFileName(title);
		date = "";
		if(Configuration.TIMESTAMP_VIDEOS) {
			date = Configuration.TIMESTAMP_FORMAT.format(Calendar.getInstance().getTime());
		}
		this.location = new File(directory, (date != "" ? "[" + date + "]" : "") + getTitle());
	}

	public void download() {
		try {
			downloadFolder();
			if(zip) {
				ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(new File(directory, (date != "" ? "[" + date + "]" : "") + getTitle() + ".zip") )));
				Utils.zipDirectory(location, location, zout);
				zout.close();
				Utils.deleteFile(location);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not download video: " + getTitle());
		}
		
	}
	
	public void downloadFolder() throws IOException {
		
		location.mkdirs();
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(location, title + ".html")));
			out.write(htmlPage);
			out.close();
			
			File commentsDir = new File(location, "comments");
			commentsDir.mkdir();
			byte[] commentsPage;
			int i = 1;
			while((commentsPage = downloadCommentsPage(i)) != null) {
				
				out = new BufferedOutputStream(new FileOutputStream(new File(location, "comments" + File.separator + "p" + i + ".html")));
				out.write(commentsPage);
				out.close();
				
				i++;
			}
			try {
				getVdeoURL();
				File video = new File(location, title + "." + format.getFormat());
				out = new BufferedOutputStream(new FileOutputStream(video));
				downloadVideo(out);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	
	public void downloadVideo(OutputStream out) {
		
		URL url = getVdeoURL();
		if(url == null) {
			return;
		}
		DownloadUtils.download(url, out);
		
	}
	
	public byte[] downloadCommentsPage(int page) throws IOException {
		
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DownloadUtils.download(new URL(
				"http://www.youtube.com/all_comments?v=" + id + "&page=" + page)
				, bout);
		
		byte[] bytes = bout.toByteArray();
		String str = bout.toString();
		
		if(Utils.getBetween(str, "<title>", "</title>").contains("YouTube - Broadcast Yourself.") && !Utils.getBetween(str, "<div class=\"yt-alert-message\">", "</div>").contains("Video has no comments.")) {
			return bytes;
		}
		
		return null;
		
	}
	
	public URL getVdeoURL() {
		
		try {
			String urlString = URLDecoder.
					decode(URLDecoder.decode(Utils.getBetween(htmlPageS, "url_encoded_fmt_stream_map=url%3D",";"), "UTF-8"), "UTF-8");
			
			String[] urls = urlString.split(",url=");
			
			for (String s : urls) {
				for(VideoFormat f : VideoFormat.values()) {
					if((f.getID() + "").equals(Utils.getBetween(s, "itag=", "&"))) {
						format = f;
						return new URL(s.substring(0, s.indexOf("&q")));
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			
		} catch (MalformedURLException e) {
			System.out.println("Unable to resolve url");
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] getHTMLPage() {
		return htmlPage;
	}
	
	public String getTitle() {
		return title + "[" + id + "]";
	}
	
}