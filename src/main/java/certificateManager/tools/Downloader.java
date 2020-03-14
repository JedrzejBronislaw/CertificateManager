package certificateManager.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Downloader {

	private String dir;
	
	public void setDir(String dir) {
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
		
		this.dir = dir;
	}
	
	public boolean downloadFile(String URL, String fileName) {
		boolean success = true;

		try (BufferedInputStream in = new BufferedInputStream(new URL(URL).openStream());
				FileOutputStream fileOutputStream = new FileOutputStream(dir + fileName)) {

			byte dataBuffer[] = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				fileOutputStream.write(dataBuffer, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}

		return success;
	}
}
