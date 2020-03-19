package certificateManager.ctrls;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import lombok.Setter;

public class DownloadUnitPaneController implements Initializable {


	@FXML
	private Button downloadUnitButton;
	@FXML
	private ProgressBar progressBar;


	@Setter
	private Runnable downladUnitButtonAction;	

	private boolean controlsBlock = false;
	private boolean possibilityOfDownloading = false;
	
	
	public void setPossibilityOfDownloading(boolean possibility) {
		this.possibilityOfDownloading = possibility;
		refreshControls();
	}

	public void setUnitDownloadProgress(float percentage) {
		Platform.runLater(() -> {
			float p = (percentage == 100) ? 0 : percentage/100;
			progressBar.setProgress(p);
		});
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
		Platform.runLater(() -> {
			downloadUnitButton.setDisable(!possibilityOfDownloading || controlsBlock);
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		downloadUnitButton.setOnAction(url -> {
			if (downladUnitButtonAction != null)
				downladUnitButtonAction.run();
		});
		
		refreshControls();	
	}
}
