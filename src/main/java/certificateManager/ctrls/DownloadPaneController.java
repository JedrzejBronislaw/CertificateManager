package certificateManager.ctrls;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;

import certificateManager.lang.Internationalization;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import lombok.Setter;

public class DownloadPaneController implements Initializable {

	private static final String INCORRECT_URL_COLOR = "FFA0A0";
	private static final String CORRECT_URL_COLOR = "FFFFFF";
	@FXML
	private VBox mainBox;
	@FXML
	private TabPane downloadPane;
	@FXML
	private TextField urlField;
	@FXML
	private TextField dirField;
	@FXML
	private Button selectDirButton;

	
	@Setter
	private Function<String, Boolean> urlValidator;
	@Setter
	private Consumer<Boolean> possibilityOfDownloadingEvent;

	
	private boolean correctURL = true;
	private boolean controlsBlock = false;

	
	public void addPane(Pane newPane) {
		mainBox.getChildren().add(newPane);
	}
	
	public void addDownloadOptionPane(String name, Pane pane) {
		Tab tab = new Tab(name, pane);
		tab.closableProperty().set(false);

		downloadPane.getTabs().add(tab);
	}

	public String getURL() {
		String url = urlField.getText();

		if (urlValidator != null && urlValidator.apply(url))
			return url;
		else
			return null;
	}
	
	public String getDestitationDir() {
		return dirField.getText();
	}
	

	public void blockControls(boolean block) {
		controlsBlock = block;
		refreshControls();
	}

	public void blockControls() {
		blockControls(true);
	}

	public void unblockControls() {
		blockControls(false);
	}

	private void refreshControls() {

		boolean blankURL = urlField.getText().isBlank();

		if (blankURL || correctURL)
			setUrlFieldColor(CORRECT_URL_COLOR);
		else
			setUrlFieldColor(INCORRECT_URL_COLOR);

		Platform.runLater(() -> {
			urlField.setDisable(controlsBlock);
			dirField.setDisable(controlsBlock);
			selectDirButton.setDisable(controlsBlock);
		});
	}
	
	private void setUrlFieldColor(String color) {
		CornerRadii radii = new CornerRadii(3);
		Insets insets = new Insets(1);

		BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf(color), radii, insets);

		Platform.runLater(() -> urlField.setBackground(new Background(backgroundFill)));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		urlField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (urlValidator != null)
				correctURL = urlValidator.apply(newValue);

			if(possibilityOfDownloadingEvent != null)
				possibilityOfDownloadingEvent.accept(correctURL);
			
			refreshControls();
		});
		
		selectDirButton.setOnAction(e -> {
			DirectoryChooser chooser = new DirectoryChooser();
			File dir;
			
			chooser.setTitle(Internationalization.get("select_dierctory"));
			dir = chooser.showDialog(null);
			
			if(dir != null && dir.exists() && dir.isDirectory())
				dirField.setText(dir.getAbsolutePath() + File.separator);
			
		});

		refreshControls();
	}

}
