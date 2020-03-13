package certificateManager.ctrls;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import certificateManager.lang.Languages;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class MainWindowController implements Initializable{

	@FXML
	private BorderPane pane;
	
	//menu
	@FXML
	private MenuItem englishMenu;
	@FXML
	private MenuItem polishMenu;
	
	@Setter
	private Consumer<Languages> changeLanguage;
	//menu END
	
	public void setContent(Pane content) {
		pane.setCenter(content);
	}
	
	public void initialize(URL location, ResourceBundle resources) {
		pane.setCenter(new Label("CertificateManager"));

		//menu
		englishMenu.setOnAction(e -> {
			if (changeLanguage != null)
				changeLanguage.accept(Languages.ENGLISH);
		});
		
		polishMenu.setOnAction(e -> {
			if (changeLanguage != null)
				changeLanguage.accept(Languages.POLISH);
		});
		//menu END
	}

}
