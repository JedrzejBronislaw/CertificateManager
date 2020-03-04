package certificateManager.ctrls;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.Setter;

public class DownloadPaneController implements Initializable{

	@FXML
	private TextField urlField;
	
	@FXML
	private Button downloadButton;
	
	@Setter
	private Consumer<String> downladButtonAction;
	
	public void blockControls(boolean block) {
		Platform.runLater(() -> {
			urlField.setDisable(block);
			downloadButton.setDisable(block);
		});
	}
	
	public void blockControls() {
		blockControls(true);
	}
	public void unblockControls() {
		blockControls(false);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		downloadButton.setOnAction(url -> {
			if(downladButtonAction != null)
				downladButtonAction.accept(urlField.getText());
		});
		
	}
}
