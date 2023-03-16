package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			MySQLDatenbankConnection.connect("userdaten"); //if error und  so
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Scene scene = new Scene(root,300,270);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	

	public static void main(String[] args) {
		launch(args);
	}
}
