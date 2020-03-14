package certificateManager.builders;

import java.util.List;

import certificateManager.CertificateDownloader;
import certificateManager.CertificateUnitDownloader;
import certificateManager.SourceWriter;
import certificateManager.WebParserSzukajwarchiwach;
import certificateManager.ctrls.DownloadPaneController;
import certificateManager.tools.MyFXMLLoader;
import certificateManager.tools.MyFXMLLoader.NodeAndController;
import javafx.scene.layout.Pane;

public class DownloadPaneBuilder {

	private Pane pane;
	

	public Pane get() {
		return pane;
	}
	
	public DownloadPaneBuilder build() {
		MyFXMLLoader<DownloadPaneController> downloadLoader = new MyFXMLLoader<>();
		NodeAndController<DownloadPaneController> downloadNAC = downloadLoader.create("DownloadPane.fxml");
		
		downloadNAC.getController().setDownladButtonAction(url -> {
			downloadNAC.getController().blockControls();

			CertificateDownloader downloader;
			WebParserSzukajwarchiwach parser = new WebParserSzukajwarchiwach(url);
			String destitationDir = downloadNAC.getController().getDestitationDir();
			SourceWriter sourceWriter = new SourceWriter(destitationDir + "source.txt", parser);
			String certificateName = downloadNAC.getController().getCertificateName();
			
			parser.parse();
			downloader = new CertificateDownloader(destitationDir,parser.getCertificateURL());
			downloader.setFileName(certificateName);
			downloader.download(b -> downloadNAC.getController().unblockControls());

			sourceWriter.setUrl(url);
			sourceWriter.setCertificateName(downloader.getLastDownloadFileName());
			
			sourceWriter.write();
		});

		downloadNAC.getController().setDownladUnitButtonAction(url -> {
			WebParserSzukajwarchiwach parser = new WebParserSzukajwarchiwach(url);
			parser.parse();
			List<String> certificates = parser.getAllScansInCatallog();
			
			System.out.println("Certificates: " + certificates.size());
			certificates.forEach(c -> System.out.println(":: " + c));
			
			
			//----
			CertificateUnitDownloader downloader;
			String destitationDir = downloadNAC.getController().getDestitationDir();
			String referenceCode = parser.getReferenceCode().replace("/", "-").replace(".", "_");
			
			downloader = new CertificateUnitDownloader(destitationDir + referenceCode, certificates);
//			downloader.setFileName(certificateName);
			downloader.download(b -> downloadNAC.getController().unblockControls());
			
			
		});
		
		downloadNAC.getController().setUrlValidator(url -> url.startsWith("https://szukajwarchiwach.pl/"));
		
		pane = (Pane)downloadNAC.getNode();
		
		return this;
	}
}
