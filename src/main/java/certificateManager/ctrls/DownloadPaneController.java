package certificateManager.ctrls;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DownloadPaneController implements Initializable{

	@FXML
	private TextField urlField;
	
	@FXML
	private Button downloadButton;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		downloadButton.setOnAction(e -> System.out.println(urlField.getText()));
	}
}
