package certificateManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

	public String getLastDownloadFileName() {
		return downloadFileName;
	}
	
	public void download() {
		download(null);
	}

	public void download(Consumer<List<Boolean>> after) {
//		setDownloadFileName();
		new Thread(() -> downloadProcess(after)).start();
	}

//	private void setDownloadFileName() {
//		downloadFileName = generateFileName() + "." + EXTENSION;
//	}

	private void downloadProcess(Consumer<List<Boolean>> after) {
		List<Boolean> success = new ArrayList<>();

		try {
			turnOffCertificateValidation();
		} catch (KeyManagementException | NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			success.add(false);
		}

		if (success.size() == 0) {
			createDirIfNotExists();
			URLs.forEach(url -> {
				
				success.add(downloadFile(url.replace("full", "save")));
			});
//			success = downloadFile(url);

		}

		if (after != null)
			after.accept(success);
	}

	private void createDirIfNotExists() {
		System.out.println(DIR);
		File directory = new File(DIR);
		directory.mkdirs();
	}

	int i = 1;
	private boolean downloadFile(String URL) {
		boolean success = true;
		downloadFileName = (i++) + ".jpg";//TODO
		System.out.println("Download: " + URL);
		System.out.println("To: " + downloadFileName);

		try (BufferedInputStream in = new BufferedInputStream(new URL(URL).openStream());
				FileOutputStream fileOutputStream = new FileOutputStream(DIR + File.separator + downloadFileName)) {

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

	private void turnOffCertificateValidation() throws KeyManagementException, NoSuchAlgorithmException {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}
}
