package certificateManager.ctrls;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		downloadButton.setOnAction(url -> {
			if(downladButtonAction != null)
				downladButtonAction.accept(urlField.getText());
		});
		
	}
}
