package certificateManager.builders;

import java.util.function.Consumer;

import certificateManager.ctrls.MainWindowController;
import certificateManager.lang.Languages;
import certificateManager.tools.MyFXMLLoader;
import certificateManager.tools.MyFXMLLoader.NodeAndController;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class MainWindowBuilder {

	private Pane pane;
	
	@Setter
	private Consumer<Languages> changeLanguage;
	
	public MainWindowBuilder build() {
		DownloadPaneBuilder downloadPaneBuilder = new DownloadPaneBuilder();
		MyFXMLLoader<MainWindowController> loader = new MyFXMLLoader<>();
		NodeAndController<MainWindowController> nac = loader.create("MainWindow.fxml");
		
		
		nac.getController().setContent(downloadPaneBuilder.build().get());
		
		if(changeLanguage != null)
			nac.getController().setChangeLanguage(changeLanguage);
		
		pane = (Pane)nac.getNode();
		return this;
	}

	public Pane get() {
		return pane;
	}
}
