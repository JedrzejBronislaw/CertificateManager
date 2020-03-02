package certificateManager.ctrls;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MainWindowController implements Initializable{

	@FXML
	private BorderPane pane;
	
	
	public void initialize(URL location, ResourceBundle resources) {
		pane.setCenter(new Label("CertificateManager"));
	}

}
