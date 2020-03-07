package certificateManager;

import certificateManager.ctrls.DownloadPaneController;
import certificateManager.ctrls.MainWindowController;
import certificateManager.tools.MyFXMLLoader;
import certificateManager.tools.MyFXMLLoader.NodeAndController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {
		Main.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		MyFXMLLoader<MainWindowController> loader = new MyFXMLLoader<>();
		NodeAndController<MainWindowController> nac = loader.create("MainWindow.fxml");
		
		MyFXMLLoader<DownloadPaneController> downloadLoader = new MyFXMLLoader<>();
		NodeAndController<DownloadPaneController> downloadNAC = downloadLoader.create("DownloadPane.fxml");
		
		nac.getController().setContent((Pane)downloadNAC.getNode());
		
		downloadNAC.getController().setDownladButtonAction(url -> {
			downloadNAC.getController().blockControls();

			WebParserSzukajwarchiwach parser = new WebParserSzukajwarchiwach(url);
			CertificateDownloader downloader;
			SourceWriter sourceWriter = new SourceWriter("D://certificate//source.txt", parser);
			String certificateName = downloadNAC.getController().getCertificateName();
			
			parser.parse();
			downloader = new CertificateDownloader(parser.getCertificateURL());
			sourceWriter.setUrl(url);
			sourceWriter.setCertificateName(certificateName);
			
			downloader.setFileName(certificateName);
			downloader.download(b -> downloadNAC.getController().unblockControls());
			
			sourceWriter.write();
		});
		
		downloadNAC.getController().setUrlValidator(url -> url.startsWith("https://szukajwarchiwach.pl/"));
		
		primaryStage.setScene(new Scene((Pane)nac.getNode()));
		
		primaryStage.setTitle("Certificate Manager");
		primaryStage.setOnCloseRequest(e -> Platform.exit());
		
		primaryStage.show();
	}

}
