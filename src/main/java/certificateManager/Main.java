package certificateManager;

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
		MyFXMLLoader<MainWindowController> loader = new MyFXMLLoader<MainWindowController>();
		NodeAndController<MainWindowController> nac = loader.create("MainWindow.fxml");
		
		primaryStage.setScene(new Scene((Pane)nac.getNode()));
		
		primaryStage.setTitle("Certificate Manager");
		primaryStage.setOnCloseRequest(e -> Platform.exit());
		
		primaryStage.show();
	}

}
