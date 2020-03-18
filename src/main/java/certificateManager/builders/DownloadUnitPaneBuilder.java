package certificateManager.builders;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import certificateManager.CertificateDownloader;
import certificateManager.CertificateUnitDownloader;
import certificateManager.SourceWriter;
import certificateManager.WebParserSzukajwarchiwach;
import certificateManager.ctrls.DownloadPaneController;
import certificateManager.ctrls.DownloadUnitPaneController;
import certificateManager.tools.MyFXMLLoader;
import certificateManager.tools.MyFXMLLoader.NodeAndController;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class DownloadUnitPaneBuilder {

//	private static final String urlPrefix = "https://szukajwarchiwach.pl/";
	private static final String DOWNLOAD_UNIT_PANE_FXML = "DownloadUnitPane.fxml";
	private Pane pane;

	@Setter
	private Supplier<String> certificateURL;
	@Setter
	private Supplier<String> destitationDir;;
	
	
	public Pane get() {
		return pane;
	}
	
	public DownloadUnitPaneBuilder build() {
		MyFXMLLoader<DownloadUnitPaneController> loader = new MyFXMLLoader<>();
		NodeAndController<DownloadUnitPaneController> nac = loader.create(DOWNLOAD_UNIT_PANE_FXML);
		DownloadUnitPaneController controller = nac.getController();


//		controller.setDownladButtonAction(downloadSingleCertificate(controller));
		controller.setDownladUnitButtonAction(downloadWholeUnit(controller));
//		controller.setUrlValidator(url -> url.startsWith(urlPrefix));
		
		pane = (Pane)nac.getNode();
		
		return this;
	}

	private Runnable downloadWholeUnit(DownloadUnitPaneController controller) {
		
		
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
				controller.setUnitDownloadProgress(-1);
				WebParserSzukajwarchiwach parser = new WebParserSzukajwarchiwach(url);
				List<String> certificateURLs;
				parser.parse();
				certificateURLs = parser.getAllScansInCatallog();

			
				//----
				CertificateUnitDownloader downloader;
				String destitationDir = destDir;//controller.getDestitationDir();
				String referenceCode = parser.getReferenceCode();
				String dir = destitationDir + referenceCode.replace("/", "-").replace(".", "_");
				
				downloader = new CertificateUnitDownloader(dir, certificateURLs);
				downloader.setProgressEvent(controller::setUnitDownloadProgress);
				downloader.download(b -> controller.unblockControls());
			});
			
			t.setDaemon(true);
			t.start();
			
		};
	}
//
//	private Consumer<String> downloadSingleCertificate(DownloadPaneController controller) {
//		return url -> {
//			controller.blockControls();
//
//			Thread t = new Thread(() -> {
//				CertificateDownloader downloader;
//				WebParserSzukajwarchiwach parser = new WebParserSzukajwarchiwach(url);
//				String destitationDir = controller.getDestitationDir();
//				SourceWriter sourceWriter = new SourceWriter(destitationDir + "source.txt", parser);
//				String certificateName = controller.getCertificateName();
//				
//				parser.parse();
//				downloader = new CertificateDownloader(destitationDir,parser.getCertificateURL());
//				downloader.setFileName(certificateName);
//				downloader.download(b -> controller.unblockControls());
//	
//				sourceWriter.setUrl(url);
//				sourceWriter.setCertificateName(downloader.getLastDownloadFileName());
//				
//				sourceWriter.write();
//			});
//
//			t.setDaemon(true);
//			t.start();
//			
//		};
//	}
}
