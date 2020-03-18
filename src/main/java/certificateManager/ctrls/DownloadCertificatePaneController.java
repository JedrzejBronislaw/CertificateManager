package certificateManager.ctrls;

import java.net.URL;
import java.util.ResourceBundle;

import certificateManager.CertificateNamer;
import certificateManager.CertificateNamer.CertificateType;
import certificateManager.lang.Internationalization;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;

public class DownloadCertificatePaneController implements Initializable {

	
	//naming
	@FXML
	private RadioButton cType_birth;
	@FXML
	private RadioButton cType_marriage;
	@FXML
	private RadioButton cType_death;
	@FXML
	private RadioButton cType_other;

	@FXML
	private TextArea recordField;
	@FXML
	private TextField nameField;

	private CertificateNamer namer = new CertificateNamer();
	//naming END
	
	//dir
//	@FXML
//	private TextField dirField;
//	@FXML
//	private Button selectDirButton;
	//dir END
	
	//unit
//	@FXML
//	private Button downloadUnitButton;
//	
//	@FXML
//	private ProgressBar progressBar;
	//unit END
	
//	@FXML
//	private TextField urlField;

	@FXML
	private Button downloadButton;

	@Setter
	private Runnable downladButtonAction;


//	@Setter
//	private Supplier<String> CertificateURL;
	
	//unit
//	@Setter
//	private Consumer<String> downladUnitButtonAction;	
	//unit END
	
//	@Setter
//	private Function<String, Boolean> urlValidator;

	private boolean correctURL = true;
	private boolean controlsBlock = false;


	//naming
	public String getCertificateName() {
		return nameField.getText().trim();
	}
	//naming END
	
	//dir
//	public String getDestitationDir() {
//		return dirField.getText();
//	}
	//dir END
	
//	public void setUnitDownloadProgress(float percentage) {
//		Platform.runLater(() -> {
//			float p = (percentage == 100) ? 0 : percentage/100;
//			progressBar.setProgress(p);
//		});
//	}
	
//	private void setUrlFieldColor(String color) {
//		CornerRadii radii = new CornerRadii(3);
//		Insets insets = new Insets(1);
//
//		BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf(color), radii, insets);
//
//		Platform.runLater(() -> urlField.setBackground(new Background(backgroundFill)));
//	}

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

//		boolean blankURL = urlField.getText().isBlank();

//		if (blankURL || correctURL)
//			setUrlFieldColor("FFFFFF");
//		else
//			setUrlFieldColor("FFA0A0");

		Platform.runLater(() -> {
//			urlField.setDisable(controlsBlock);
//			downloadButton.setDisable(blankURL || !correctURL || controlsBlock);
//			downloadUnitButton.setDisable(blankURL || !correctURL || controlsBlock);
			
			//naming
			cType_birth.setDisable(controlsBlock);
			cType_marriage.setDisable(controlsBlock);
			cType_death.setDisable(controlsBlock);
			cType_other.setDisable(controlsBlock);

			recordField.setDisable(controlsBlock);
			nameField.setDisable(controlsBlock);
			//naming END
			
			//dir
//			dirField.setDisable(controlsBlock);
//			selectDirButton.setDisable(controlsBlock);
			//dir END
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		downloadButton.setOnAction(url -> {
			if (downladButtonAction != null)
				downladButtonAction.run();
		});
		
		//unit
//		downloadUnitButton.setOnAction(url -> {
//			if (downladUnitButtonAction != null)
//				downladUnitButtonAction.accept(urlField.getText());
//		});
		//unit END

//		urlField.textProperty().addListener((observable, oldValue, newValue) -> {
//			if (urlValidator != null)
//				correctURL = urlValidator.apply(newValue);
//
//			refreshControls();
//		});

		refreshControls();
		

		//naming
		namer.setCertificateTypeNames(
				Internationalization.get("birth").toLowerCase(),
				Internationalization.get("marriage").toLowerCase(),
				Internationalization.get("death").toLowerCase());
		
		
		recordField.textProperty().addListener((obrv, oldV, newV) -> refreshCertificateName());
		
		ChangeListener<? super Boolean> typeRefersher = (obrv, oldV, newV) -> refreshCertificateType();
		cType_birth.selectedProperty().addListener(typeRefersher);
		cType_marriage.selectedProperty().addListener(typeRefersher);
		cType_death.selectedProperty().addListener(typeRefersher);
		cType_other.selectedProperty().addListener(typeRefersher);
		//naming END
		
		//dir
//		selectDirButton.setOnAction(e -> {
//			DirectoryChooser chooser = new DirectoryChooser();
//			File dir;
//			
//			chooser.setTitle("Select directory...");
//			dir = chooser.showDialog(null);
//			
//			if(dir != null && dir.exists() && dir.isDirectory())
//				dirField.setText(dir.getAbsolutePath() + File.separator);
//			
//		});
		//dir END
	}

	//naming
	private void refreshCertificateType() {
		if(cType_birth.isSelected())
			namer.setType(CertificateType.Birth);
		if(cType_marriage.isSelected())
			namer.setType(CertificateType.Marriage);
		if(cType_death.isSelected())
			namer.setType(CertificateType.Death);
		if(cType_other.isSelected())
			namer.setType(CertificateType.Other);

		nameField.setText(namer.generateName());
	}
	
	private void refreshCertificateName() {
		namer.setRecord(recordField.getText());
		nameField.setText(namer.generateName());
	}
	//naming END
}
