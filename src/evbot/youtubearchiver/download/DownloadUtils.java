package evbot.youtubearchiver.download;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class DownloadUtils {
	
	public static void download(URL url, OutputStream out) {
		try {
			BufferedInputStream in = new BufferedInputStream(url.openStream());
			byte[] buff = new byte[1024];
			int len;
			while((len = in.read(buff)) != -1) {
				out.write(buff, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
