package certificateManager.ctrls;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Paint;
import lombok.Getter;
import lombok.Setter;

public class DownloadPaneController implements Initializable{

	@FXML
	private TextField urlField;
	
	@FXML
	private Button downloadButton;
	
	@Setter
	private Consumer<String> downladButtonAction;
	
	@Setter
	private Function<String, Boolean> urlValidator;

	@Getter
	private BooleanProperty correctURL = new SimpleBooleanProperty(false);
	
	private void setUrlFieldColor(String color) {
		CornerRadii radii = new CornerRadii(3);
		Insets insets = new Insets(1);

		BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf(color), radii, insets);
		
		Platform.runLater(() -> urlField.setBackground(new Background(backgroundFill)));
	}

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		downloadButton.setOnAction(url -> {
			if(downladButtonAction != null)
				downladButtonAction.accept(urlField.getText());
		});
		
		urlField.textProperty().addListener((observable, oldValue, newValue) -> {
			
			if(urlValidator != null)
				correctURL.setValue(urlValidator.apply(newValue));
				
			
			if(newValue.isEmpty() || correctURL.getValue())
				setUrlFieldColor("FFFFFF");
			else
				setUrlFieldColor("FFA0A0");
	
		});
		
		setUrlFieldColor("FFFFFF");
		downloadButton.disableProperty().bind(correctURL.not());		
		
	}
}
