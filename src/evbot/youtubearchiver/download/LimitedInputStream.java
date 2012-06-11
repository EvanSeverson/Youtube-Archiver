package evbot.youtubearchiver.download;

import java.io.IOException;
import java.io.InputStream;

import evbot.youtubearchiver.config.Configuration;

public class LimitedInputStream extends InputStream{
	
	private InputStream in;
	private long last;
	
	private static volatile int tokens = 0;
	private static boolean emptying = false;
	
	public LimitedInputStream(InputStream in) {
		this.in = in;
	}

	@Override
	public int read() throws IOException {
		if(Configuration.DOWNLOAD_SPEED_LIMIT == 0) {
			return in.read();
		}
		
		while(tokens == Configuration.DOWNLOAD_SPEED_LIMIT) {
			if(!emptying) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						emptying = true;
						tokenRefil();
						
					}
				}).start();
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		tokens++;
		return in.read();
		
	}
	
	private static void tokenRefil() {
		while(tokens < Configuration.DOWNLOAD_SPEED_LIMIT) {
			tokens -= Configuration.DOWNLOAD_SPEED_LIMIT * 128;
			
			try {
				Thread.sleep(125);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		emptying = false;
	}
	
}
