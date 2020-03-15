package certificateManager.builders;

import java.util.List;
import java.util.function.Consumer;

import certificateManager.CertificateDownloader;
import certificateManager.CertificateUnitDownloader;
import certificateManager.SourceWriter;
import certificateManager.WebParserSzukajwarchiwach;
import certificateManager.ctrls.DownloadPaneController;
import certificateManager.tools.MyFXMLLoader;
import certificateManager.tools.MyFXMLLoader.NodeAndController;
import javafx.scene.layout.Pane;

public class DownloadPaneBuilder {

	private static final String urlPrefix = "https://szukajwarchiwach.pl/";
	private static final String DOWNLOAD_PANE_FXML = "DownloadPane.fxml";
	private Pane pane;
	

	public Pane get() {
		return pane;
	}
	
	public DownloadPaneBuilder build() {
		MyFXMLLoader<DownloadPaneController> loader = new MyFXMLLoader<>();
		NodeAndController<DownloadPaneController> nac = loader.create(DOWNLOAD_PANE_FXML);
		DownloadPaneController controller = nac.getController();


		controller.setDownladButtonAction(downloadSingleCertificate(controller));
		controller.setDownladUnitButtonAction(downloadWholeUnit(controller));
		controller.setUrlValidator(url -> url.startsWith(urlPrefix));
		
		pane = (Pane)nac.getNode();
		
		return this;
	}

	private Consumer<String> downloadWholeUnit(DownloadPaneController controller) {
		return url -> {
			controller.blockControls();

			WebParserSzukajwarchiwach parser = new WebParserSzukajwarchiwach(url);
			List<String> certificateURLs;
			
			parser.parse();
			certificateURLs = parser.getAllScansInCatallog();
			
			
			//----
			CertificateUnitDownloader downloader;
			String destitationDir = controller.getDestitationDir();
			String referenceCode = parser.getReferenceCode();
			String dir = destitationDir + referenceCode.replace("/", "-").replace(".", "_");
			
			downloader = new CertificateUnitDownloader(dir, certificateURLs);
			downloader.download(b -> controller.unblockControls());
			
		};
	}

	private Consumer<String> downloadSingleCertificate(DownloadPaneController controller) {
		return url -> {
			controller.blockControls();

			CertificateDownloader downloader;
			WebParserSzukajwarchiwach parser = new WebParserSzukajwarchiwach(url);
			String destitationDir = controller.getDestitationDir();
			SourceWriter sourceWriter = new SourceWriter(destitationDir + "source.txt", parser);
			String certificateName = controller.getCertificateName();
			
			parser.parse();
			downloader = new CertificateDownloader(destitationDir,parser.getCertificateURL());
			downloader.setFileName(certificateName);
			downloader.download(b -> controller.unblockControls());

			sourceWriter.setUrl(url);
			sourceWriter.setCertificateName(downloader.getLastDownloadFileName());
			
			sourceWriter.write();
		};
	}
}
