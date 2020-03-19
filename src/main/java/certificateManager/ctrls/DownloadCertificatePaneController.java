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

	@FXML
	private Button downloadButton;

	
	@Setter
	private Runnable downladButtonAction;

	
	private CertificateNamer namer = new CertificateNamer();


	private boolean controlsBlock = false;
	private boolean possibilityOfDownloading = false;
	
	public void setPossibilityOfDownloading(boolean possibility) {
		this.possibilityOfDownloading = possibility;
		refreshControls();
	}

	public String getCertificateName() {
		return nameField.getText().trim();
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
			downloadButton.setDisable(!possibilityOfDownloading|| controlsBlock);
			
			cType_birth.setDisable(controlsBlock);
			cType_marriage.setDisable(controlsBlock);
			cType_death.setDisable(controlsBlock);
			cType_other.setDisable(controlsBlock);

			recordField.setDisable(controlsBlock);
			nameField.setDisable(controlsBlock);
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		downloadButton.setOnAction(url -> {
			if (downladButtonAction != null)
				downladButtonAction.run();
		});
		
		recordField.textProperty().addListener((obrv, oldV, newV) -> refreshCertificateRecord());

		namer.setCertificateTypeNames(
				Internationalization.get("birth").toLowerCase(),
				Internationalization.get("marriage").toLowerCase(),
				Internationalization.get("death").toLowerCase());
		
		ChangeListener<? super Boolean> typeRefersher = (obrv, oldV, newV) -> refreshCertificateType();
		cType_birth.selectedProperty().addListener(typeRefersher);
		cType_marriage.selectedProperty().addListener(typeRefersher);
		cType_death.selectedProperty().addListener(typeRefersher);
		cType_other.selectedProperty().addListener(typeRefersher);
		

		refreshControls();
	}

	private void refreshCertificateType() {
		if(cType_birth.isSelected())
			namer.setType(CertificateType.Birth);
		if(cType_marriage.isSelected())
			namer.setType(CertificateType.Marriage);
		if(cType_death.isSelected())
			namer.setType(CertificateType.Death);
		if(cType_other.isSelected())
			namer.setType(CertificateType.Other);

		refreshCertificateName();
	}
	
	private void refreshCertificateRecord() {
		namer.setRecord(recordField.getText());
		refreshCertificateName();
	}

	private void refreshCertificateName() {
		nameField.setText(namer.generateName());
	}
	
	
}
