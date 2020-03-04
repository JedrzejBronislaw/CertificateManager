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
import java.util.function.Consumer;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CertificateDownloader {

	private static final String DIR = "D://certificate//";

	private final String url;


	public void download() {
		download(null);
	}
	
	public void download(Consumer<Boolean> after) {
		new Thread(() -> downloadProcess(after)).start();;
	}

	private void downloadProcess(Consumer<Boolean> after) {
		boolean success = true;

		try {
			turnOffCertificateValidation();
		} catch (KeyManagementException | NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			success = false;
		}

		if (success) {
			createDirIfNotExists();
			success = downloadFile(url);

		}

		if (after != null)
			after.accept(success);
	}

	private void createDirIfNotExists() {
		File directory = new File(DIR);
		directory.mkdirs();
	}

	private boolean downloadFile(String URL) {
		boolean success = true;
		
		try (BufferedInputStream in = new BufferedInputStream(new URL(URL).openStream());
				FileOutputStream fileOutputStream = new FileOutputStream(DIR + generateFileName())) {

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
		DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		return "certificate_" + LocalDateTime.now().format(formatter).replace(":", "_") + ".jpg";
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
