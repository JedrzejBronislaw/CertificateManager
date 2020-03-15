package certificateManager;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import certificateManager.tools.Downloader;
import certificateManager.tools.SSLValidation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class CertificateUnitDownloader {

	@NonNull
	private String DIR;// = "D://certificate//";
	private static final String EXTENSION = "jpg";

	private final List<String> URLs;
	@Setter
	private String fileName;
	private String downloadFileName = "";
	
	private Downloader downloader = new Downloader();
	private boolean downloadOngoing = false;
	
	public String getLastDownloadFileName() {
		return downloadFileName;
	}
	
	public boolean download() {
		return download(null);
	}

	public boolean download(Consumer<List<Boolean>> after) {
		if (downloadOngoing) return false;
		downloadOngoing = true;

		Thread t = new Thread(() -> downloadProcess(after));
		t.setDaemon(true);
		t.start();
		return true;
	}

	private void downloadProcess(Consumer<List<Boolean>> after) {
		
		List<Boolean> success = new ArrayList<>();
		downloader.setDir(DIR);

		if (SSLValidation.turnOff()) {
			createDirIfNotExists();
			for(int i=0; i<URLs.size(); i++) {
				success.add(download(i));
				
			}
		}

		if (after != null)
			after.accept(success);
		
		downloadOngoing = false;
	}
	
	private String createFileName(int i) {
		return i + ".jpg";
	}

	private boolean download(int i) {
		return downloader.downloadFile(
				getFileURL(URLs.get(i)),
				createFileName(i+1));
	}

	private String getFileURL(String url) {
		return url.replace("full", "save");
	}

	private void createDirIfNotExists() {
		System.out.println(DIR);
		File directory = new File(DIR);
		directory.mkdirs();
	}

	private String generateFileName() {
		String baseName, finalName;

		baseName = (fileName != null || fileName.isEmpty()) ? fileName : generateTimeFileName();
		finalName = changeNameIfFileExist(baseName);

		return finalName;
	}

	private String changeNameIfFileExist(final String baseName) {
		File f;
		String finalName;
		int i = 2;
		
		finalName = baseName;

		f = new File(DIR + finalName + "." + EXTENSION);
		while (f.exists()) {
			finalName = baseName + " (" + (i++) + ")";
			f = new File(DIR + finalName + "." + EXTENSION);
		}
		
		return finalName;
	}

	private String generateTimeFileName() {
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		return "certificate_" + LocalDateTime.now().format(formatter).replace(":", "_");
	}


}
