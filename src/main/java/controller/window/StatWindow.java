package controller.window;

import java.io.IOException;

import controller.StatController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class StatWindow {

	private FXMLLoader loader;
	private Stage statiViewStage;
	private Parent root;
	private StatController statViewController;
	
	public StatWindow(){
		initWindow();
	}
	
	private void initWindow(){
		statiViewStage = new Stage();
		try {
			loader = new FXMLLoader(getClass().getResource("/view/dialog/StatView.fxml"));
			
			root = (Parent) loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add("application/style.css");
			statiViewStage.setScene(scene);
			statiViewStage.setTitle("Wykres");
			statiViewStage.setResizable(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		statViewController = (StatController)loader.getController();
	}
	
	public StatController getController(){
		return statViewController;
	}
	
	public void show(){
		statiViewStage.show();
	}
	
}
