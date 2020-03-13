package certificateManager;

import java.util.List;

import certificateManager.ctrls.DownloadPaneController;
import certificateManager.ctrls.MainWindowController;
import certificateManager.lang.Internationalization;
import certificateManager.lang.Languages;
import certificateManager.tools.MyFXMLLoader;
import certificateManager.tools.MyFXMLLoader.NodeAndController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application{

	private Stage stage;
	
	public static void main(String[] args) {
		Main.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.stage = primaryStage;
		buildView();
	}

	private void buildView() {
		stage.setScene(buildScene());
		
		stage.setTitle("Certificate Manager");
		stage.setOnCloseRequest(e -> Platform.exit());
		
		stage.show();
	}
	
	private void changeLanguage(Languages language) {
		if(Internationalization.getCurrentLanguage() != language)
			stage.setScene(buildScene(language));
	}
	
	private Scene buildScene(Languages language) {
		Internationalization.setLanguage(language);
		return buildScene();
	}

	private Scene buildScene() {
		MyFXMLLoader<MainWindowController> loader = new MyFXMLLoader<>();
		NodeAndController<MainWindowController> nac = loader.create("MainWindow.fxml");
		
		MyFXMLLoader<DownloadPaneController> downloadLoader = new MyFXMLLoader<>();
		NodeAndController<DownloadPaneController> downloadNAC = downloadLoader.create("DownloadPane.fxml");
		
		nac.getController().setContent((Pane)downloadNAC.getNode());
		
		nac.getController().setChangeLanguage(this::changeLanguage);
		
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

		return new Scene((Pane)nac.getNode());
	}

}
