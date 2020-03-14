package certificateManager;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import certificateManager.tools.Downloader;
import certificateManager.tools.SSLValidation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class CertificateDownloader {

	@NonNull
	private String DIR;// = "D://certificate//";
	private static final String EXTENSION = "jpg";

	private final String url;
	@Setter
	private String fileName;
	private String downloadFileName = "";

	public String getLastDownloadFileName() {
		return downloadFileName;
	}
	
	public void download() {
		download(null);
	}

	public void download(Consumer<Boolean> after) {
		setDownloadFileName();
		new Thread(() -> downloadProcess(after)).start();
	}

	private void setDownloadFileName() {
		downloadFileName = generateFileName() + "." + EXTENSION;
	}

	private void downloadProcess(Consumer<Boolean> after) {
		boolean success;
		Downloader downloader = new Downloader();
		downloader.setDir(DIR);

		if (SSLValidation.turnOff()) {
			createDirIfNotExists();
			success = downloader.downloadFile(url, downloadFileName);
		} else
			success = false;

		if (after != null)
			after.accept(success);
	}

	private void createDirIfNotExists() {
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
