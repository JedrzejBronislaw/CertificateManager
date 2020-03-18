package certificateManager.builders;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import certificateManager.CertificateDownloader;
import certificateManager.CertificateUnitDownloader;
import certificateManager.SourceWriter;
import certificateManager.WebParserSzukajwarchiwach;
import certificateManager.ctrls.DownloadCertificatePaneController;
import certificateManager.ctrls.DownloadPaneController;
import certificateManager.tools.MyFXMLLoader;
import certificateManager.tools.MyFXMLLoader.NodeAndController;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class DownloadCertificatePaneBuilder {

//	private static final String urlPrefix = "https://szukajwarchiwach.pl/";
	private static final String DOWNLOAD_CERTIFICATE_PANE_FXML = "DownloadCertificatePane.fxml";
	private Pane pane;

	@Setter
	private Supplier<String> certificateURL;
	@Setter
	private Supplier<String> destitationDir;;
	

	public Pane get() {
		return pane;
	}
	
	public DownloadCertificatePaneBuilder build() {
		MyFXMLLoader<DownloadCertificatePaneController> loader = new MyFXMLLoader<>();
		NodeAndController<DownloadCertificatePaneController> nac = loader.create(DOWNLOAD_CERTIFICATE_PANE_FXML);
		DownloadCertificatePaneController controller = nac.getController();


		controller.setDownladButtonAction(downloadSingleCertificate(controller));
//		controller.setDownladUnitButtonAction(downloadWholeUnit(controller));
//		controller.setUrlValidator(url -> url.startsWith(urlPrefix));
		
		pane = (Pane)nac.getNode();
		
		return this;
	}

//	private Consumer<String> downloadWholeUnit(DownloadPaneController controller) {
//		return url -> {
//			controller.blockControls();
//			
//			Thread t = new Thread(() -> {
//				controller.setUnitDownloadProgress(-1);
//				WebParserSzukajwarchiwach parser = new WebParserSzukajwarchiwach(url);
//				List<String> certificateURLs;
//				parser.parse();
//				certificateURLs = parser.getAllScansInCatallog();
//
//			
//				//----
//				CertificateUnitDownloader downloader;
//				String destitationDir = controller.getDestitationDir();
//				String referenceCode = parser.getReferenceCode();
//				String dir = destitationDir + referenceCode.replace("/", "-").replace(".", "_");
//				
//				downloader = new CertificateUnitDownloader(dir, certificateURLs);
//				downloader.setProgressEvent(controller::setUnitDownloadProgress);
//				downloader.download(b -> controller.unblockControls());
//			});
//			
//			t.setDaemon(true);
//			t.start();
//			
//		};
//	}

	private Runnable downloadSingleCertificate(DownloadCertificatePaneController controller) {
		return () -> {
			String url;
			String destDir;
			
			controller.blockControls();

			if(certificateURL != null && destitationDir != null) {
				url = certificateURL.get();
				destDir = destitationDir.get();
			} else {
				controller.unblockControls();
				return;
			}

			Thread t = new Thread(() -> {
				CertificateDownloader downloader;
				WebParserSzukajwarchiwach parser = new WebParserSzukajwarchiwach(url);
				String destitationDir = destDir;//controller.getDestitationDir();
				SourceWriter sourceWriter = new SourceWriter(destitationDir + "source.txt", parser);
				String certificateName = controller.getCertificateName();
				
				parser.parse();
				downloader = new CertificateDownloader(destitationDir,parser.getCertificateURL());
				downloader.setFileName(certificateName);
				downloader.download(b -> controller.unblockControls());
	
				sourceWriter.setUrl(url);
				sourceWriter.setCertificateName(downloader.getLastDownloadFileName());
				
				sourceWriter.write();
			});

			t.setDaemon(true);
			t.start();
			
		};
	}
}
