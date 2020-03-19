package certificateManager.builders;

import java.util.function.Supplier;

import certificateManager.CertificateDownloader;
import certificateManager.SourceWriter;
import certificateManager.WebParserSzukajwarchiwach;
import certificateManager.ctrls.DownloadCertificatePaneController;
import certificateManager.tools.MyFXMLLoader;
import certificateManager.tools.MyFXMLLoader.NodeAndController;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

public class DownloadCertificatePaneBuilder {

	private static final String SourceFileName = "source.txt";
	private static final String DOWNLOAD_CERTIFICATE_PANE_FXML = "DownloadCertificatePane.fxml";
	private Pane pane;
	@Getter
	private DownloadCertificatePaneController controller;
	
	@Setter
	private Supplier<String> certificateURL;
	@Setter
	private Supplier<String> destitationDir;

	@Setter
	private Runnable startDownload;
	@Setter
	private Runnable finishDownload;

	public Pane get() {
		return pane;
	}
	
	
	public DownloadCertificatePaneBuilder build() {
		MyFXMLLoader<DownloadCertificatePaneController> loader = new MyFXMLLoader<>();
		NodeAndController<DownloadCertificatePaneController> nac = loader.create(DOWNLOAD_CERTIFICATE_PANE_FXML);
		
		controller = nac.getController();
		pane = (Pane)nac.getNode();

		controller.setDownladButtonAction(downloadSingleCertificate(controller));
		
		
		return this;
	}

	private Runnable downloadSingleCertificate(DownloadCertificatePaneController controller) {
		DownloadCertificatePaneBuilder builder = this;
		
		return () -> {
			String url;
			String destDir;
			
			beforeDownload();

			if(certificateURL != null && destitationDir != null) {
				url = certificateURL.get();
				destDir = destitationDir.get();
			} else {
				builder.afterDownload(false);
				return;
			}

			Thread t = new Thread(() -> {
				CertificateDownloader downloader;
				WebParserSzukajwarchiwach parser = new WebParserSzukajwarchiwach(url);
				String destitationDir = destDir;
				SourceWriter sourceWriter = new SourceWriter(destitationDir + SourceFileName, parser);
				String certificateName = controller.getCertificateName();
				
				parser.parse();
				downloader = new CertificateDownloader(destitationDir,parser.getCertificateURL());
				downloader.setFileName(certificateName);
				downloader.download(builder::afterDownload);
	
				sourceWriter.setUrl(url);
				sourceWriter.setCertificateName(downloader.getLastDownloadFileName());
				
				sourceWriter.write();
			});

			t.setDaemon(true);
			t.start();
			
		};
	}

	void beforeDownload() {
		if (startDownload != null)
			startDownload.run();
		
		controller.blockControls();
	}
	void afterDownload(boolean successes) {
		if (finishDownload != null)
			finishDownload.run();
		
		controller.unblockControls();
	}
}
