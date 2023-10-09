package main.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartController extends Application{
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			MySQLDatenbankConnection.connect("cardaten"); //if error und  so
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("Login.fxml"));
			Scene scene = new Scene(root,300,270);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
