package certificateManager.builders;

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

		controller.setUrlValidator(url -> url.startsWith(urlPrefix));
		
		pane = (Pane)nac.getNode();
		
		DownloadCertificatePaneBuilder downloadCertificatePaneBuilder = new DownloadCertificatePaneBuilder();
		DownloadUnitPaneBuilder downloadUnitPaneBuilder = new DownloadUnitPaneBuilder();

		downloadCertificatePaneBuilder.setCertificateURL(controller::getURL);
		downloadCertificatePaneBuilder.setDestitationDir(controller::getDestitationDir);
		downloadUnitPaneBuilder.setCertificateURL(controller::getURL);
		downloadUnitPaneBuilder.setDestitationDir(controller::getDestitationDir);

		Runnable blockControls = () -> {
			controller.blockControls();
			downloadCertificatePaneBuilder.getController().blockControls();
			downloadUnitPaneBuilder.getController().blockControls();
		};
		Runnable unblockControls = () -> {
			controller.unblockControls();
			downloadCertificatePaneBuilder.getController().unblockControls();
			downloadUnitPaneBuilder.getController().unblockControls();
		};
		downloadCertificatePaneBuilder.setStartDownload(blockControls);
		downloadCertificatePaneBuilder.setFinishDownload(unblockControls);
		downloadUnitPaneBuilder.setStartDownload(blockControls);
		downloadUnitPaneBuilder.setFinishDownload(unblockControls);
		
		controller.setPossibilityOfDownloadingEvent(possibility -> {
			downloadCertificatePaneBuilder.getController().setPossibilityOfDownloading(possibility);
			downloadUnitPaneBuilder.getController().setPossibilityOfDownloading(possibility);
		});
		
		
		Pane downloadCertificatePane = downloadCertificatePaneBuilder.build().get();
		Pane downloadUnitPane = downloadUnitPaneBuilder.build().get();

		controller.addPane(downloadCertificatePane);
		controller.addPane(downloadUnitPane);
		
		return this;
	}

}
