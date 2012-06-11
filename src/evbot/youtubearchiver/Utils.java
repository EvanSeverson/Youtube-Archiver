package evbot.youtubearchiver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils {

	public static void deleteFile(File file) {
		
		if(!file.isDirectory()) {
			file.delete();
			return;
		}
		
		for(File f : file.listFiles()) {
			deleteFile(f);
		}
		file.delete();
		
	}

	public static void zipDirectory(File mainDir, File directory, ZipOutputStream zout) throws IOException {
		
		if(!directory.isDirectory()) return;
		for(File f : directory.listFiles()) {
			if(f.isDirectory()) {
				zout.putNextEntry(new ZipEntry(f.getName() + "/"));
				zipDirectory(mainDir, f, zout);
				continue;
			}
			zout.putNextEntry(new ZipEntry(mainDir.toURI().relativize(f.toURI()).getPath()));
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
			byte[] buff = new byte[1024];
			int len;
			while((len = in.read(buff)) != -1) {
				zout.write(buff, 0, len);
			}
		}
		
	}

	public static String getBetween(String text, String beg, String end) {
		try{
			text = text.split(beg, 2)[1];
		} catch (Exception e) {
		}
		try {
			text = text.split(end)[0];
		}catch (Exception e) {
		}
		
		return text;
	}

	public static String unescapeHTML(String s) {
		String ret = s;
		for(int i = 0; i < s.length(); i++) {
			try {
				if(s.charAt(i) == '&') {
					if(s.charAt(i + 1) == '#') {
						if(s.charAt(i + 4) == ';') {
							byte val = Byte.parseByte(s.substring(i + 2, i + 4));
							String ss = new String(new byte[]{val});
							ret = ret.replace("&#" + val + ";", ss);
						}
					}
				}
			}catch(Exception e) {
				
			}
		}
		return ret;
	}

	public static String removeWhiteSpace(String str) {
		while(str.charAt(0) == ' ' || str.charAt(0) == '\r' ||str.charAt(0) == '\n' || str.charAt(0) == '\t'){
			str = str.substring(1);
		}
		while(str.charAt(str.length() - 1) == ' ' || str.charAt(str.length() - 1) == '\r' ||str.charAt(str.length() - 1) == '\n' || str.charAt(str.length() - 1) == '\t'){
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}
	
	public static String toLegalFileName(String name) {
		String retName = "";
		for(char c : name.toCharArray()) {
			try {
				if(c == File.separatorChar) {
					throw new Exception();
				}
				new File(c + "").getCanonicalPath();
				retName += c;
			} catch (Exception e) {
				retName += "[" + (byte) c + "]";
			}
		}
		
		return retName;
	}

}
