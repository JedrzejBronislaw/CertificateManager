package certificateManager;

import certificateManager.builders.MainWindowBuilder;
import certificateManager.lang.Internationalization;
import certificateManager.lang.Languages;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
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
		MainWindowBuilder mainWindowBuilder = new MainWindowBuilder();
		
		mainWindowBuilder.setChangeLanguage(this::changeLanguage);

		return new Scene(mainWindowBuilder.build().get());
	}
	
}
