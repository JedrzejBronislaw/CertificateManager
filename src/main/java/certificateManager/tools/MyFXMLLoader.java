package certificateManager.tools;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class MyFXMLLoader<T extends Initializable>{
	
	@RequiredArgsConstructor
	public static class NodeAndController<T>{

		@NonNull
		@Getter
		private Node node;

		@NonNull
		@Getter
		private T controller;
	}


	private static final String mainDir = "/";
	private static final String lang = "lang.labels";
	
	
	public NodeAndController<T> create(String fxmlFilePath) {
		FXMLLoader fxmlLoader = new FXMLLoader();

		fxmlLoader.setLocation(getClass().getResource(mainDir + fxmlFilePath));
		fxmlLoader.setResources(ResourceBundle.getBundle(lang));

    	Node node;
		try {
			node = fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
    	
		
		return new NodeAndController<T>(node, fxmlLoader.getController());
	}

}
