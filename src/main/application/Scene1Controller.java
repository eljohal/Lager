package main.application;

import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
public class Scene1Controller {
	public Stage stag;
	public Scene scene;
	public Parent root;
	static int id;
	@FXML
	Button registerButton;
	@FXML
	Button loginButton;
	@FXML
	TextField UserNameInput;
	@FXML
	PasswordField PasswordInput;
	@FXML
	Label loginLabel;
	User user = new User();
	static VersionCheckerThread initThread = new VersionCheckerThread();
	
	
	public static boolean Login(String UserName, String PassWd) {
		if(MySQLDatenbankConnection.isConnected()) {
			if(Objects.equals(MySQLDatenbankConnection.getInt("SELECT `UserID` FROM `userdata` WHERE `Username`='" + UserName + "'"),0)) {
				return false;
			}else if(Objects.equals(MySQLDatenbankConnection.getInt("SELECT `UserID` FROM `userdata` WHERE `Username`='" + UserName + "'"), MySQLDatenbankConnection.getInt("SELECT `UserID` FROM `userdata` WHERE `Password`='" +  PassWd + "' AND `Username` ='" + UserName +"'")) && !Objects.equals(MySQLDatenbankConnection.getInt("SELECT `UserID` FROM `userdata` WHERE `Password`='" +  PassWd + "' AND `Username` ='" + UserName +"'"), 0)) {		
				id = MySQLDatenbankConnection.getInt("SELECT `UserID` FROM `userdata` WHERE `Username`='" + UserName + "'");
				return true;
			}
		}
		return false;
	}
	
	
	public void switchToFahrzeugübersicht() throws IOException {
		String userName = UserNameInput.getText();
		String password = PasswordInput.getText();
		if (userName.equals("") || password.equals("")) {
			setLabel("Username or Password is empty. Please insert your Login credentials", loginLabel, "black");
		}else if(Login(userName, password)  && !Objects.equals(userName, "") && !Objects.equals(password, "")) {
			initswitchToFahrzeugübersicht();
			User.username = userName;
			User.id = id;
			switchToOverview();
		}else {
			setLabel("wrong Username or Password", loginLabel,"red");
		}
	}
	
	public void initswitchToFahrzeugübersicht() {
		initThread.startInitThread();
        PictureDirectory.setDir();
	}
	
	public static void setLabel(String Text, Label label, String color) {
		label.setStyle("-fx-text-fill: "+color+";");
		label.setText(Text);
	}
	
	public void switchToRegister() throws IOException {
		root = FXMLLoader.load(getClass().getClassLoader().getResource("Register.fxml"));
		stag = (Stage) registerButton.getScene().getWindow();		
		stag.setScene(new Scene(root));
	}
	
	public void switchToOverview() throws IOException {
        stag = (Stage) loginButton.getScene().getWindow();
        SceneOverviewController.stag = stag;
        root = FXMLLoader.load(getClass().getClassLoader().getResource("Overview.fxml"));
        stag.setScene(new Scene(root));
    }
        
}
