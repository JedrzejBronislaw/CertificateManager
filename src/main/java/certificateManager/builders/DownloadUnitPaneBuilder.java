package certificateManager.builders;

import java.util.List;
import java.util.function.Supplier;

import certificateManager.CertificateUnitDownloader;
import certificateManager.WebParserSzukajwarchiwach;
import certificateManager.ctrls.DownloadUnitPaneController;
import certificateManager.tools.MyFXMLLoader;
import certificateManager.tools.MyFXMLLoader.NodeAndController;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.Setter;

public class DownloadUnitPaneBuilder {

	private static final String DOWNLOAD_UNIT_PANE_FXML = "DownloadUnitPane.fxml";
	private Pane pane;
	@Getter
	private DownloadUnitPaneController controller;

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
	
	public DownloadUnitPaneBuilder build() {
		MyFXMLLoader<DownloadUnitPaneController> loader = new MyFXMLLoader<>();
		NodeAndController<DownloadUnitPaneController> nac = loader.create(DOWNLOAD_UNIT_PANE_FXML);
		
		controller = nac.getController();
		pane = (Pane)nac.getNode();

		controller.setDownladUnitButtonAction(downloadWholeUnit(controller));
		
		return this;
	}

	private Runnable downloadWholeUnit(DownloadUnitPaneController controller) {
		
		
		return () -> {
			String url;
			String destDir;
			
			if (startDownload != null)
				startDownload.run();
			
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
				String destitationDir = destDir;
				String referenceCode = parser.getReferenceCode();
				String dir = destitationDir + referenceCode.replace("/", "-").replace(".", "_");
				
				downloader = new CertificateUnitDownloader(dir, certificateURLs);
				downloader.setProgressEvent(controller::setUnitDownloadProgress);
				downloader.download(b -> {
					
					if (finishDownload != null)
						finishDownload.run();
					
					controller.unblockControls();
					});
			});
			
			t.setDaemon(true);
			t.start();
			
		};
	}
}
