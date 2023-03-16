package application;

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
import javafx.scene.layout.AnchorPane;
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
	
	
	public static boolean Login(String UserName, String PassWd) {
		if(MySQLDatenbankConnection.isConnected()) {
		if(Objects.equals(MySQLDatenbankConnection.getInt("SELECT `UserID` FROM `userdata` WHERE `Username`='" + UserName + "'"),0)) {
			return false;
		}
		if(Objects.equals(MySQLDatenbankConnection.getInt("SELECT `UserID` FROM `userdata` WHERE `Username`='" + UserName + "'"), MySQLDatenbankConnection.getInt("SELECT `UserID` FROM `userdata` WHERE `Password`='" +  PassWd + "' AND `Username` ='" + UserName +"'")) && !Objects.equals(MySQLDatenbankConnection.getInt("SELECT `UserID` FROM `userdata` WHERE `Username`='" + UserName + "' AND `Passwort`='" + PassWd + "'"), 0)) {		
			id = MySQLDatenbankConnection.getInt("SELECT `UserID` FROM `userdata` WHERE `Username`='" + UserName + "'");
			return true;
		}}
		return false;
	}
	
	
	public void switchToFahrzeugübersicht() throws IOException {
		String userName = UserNameInput.getText();
		String password = PasswordInput.getText();
		if (userName.equals("") || password.equals("")) {
			setLabel("Username or Password is empty. Please insert your Login credentials", loginLabel);
		}else if(Login(userName, password)  && !Objects.equals(userName, "") && !Objects.equals(password, "")) {
			User.username = userName;
			User.id = id;
			switchToOverview();
		}else {
			setLabel("wrong Username or Password", loginLabel);
		}
	}
	
	public static void setLabel(String Text, Label label) {
		label.setText(Text);
	}
	
	public void switchToRegister() throws IOException {
		
		root = FXMLLoader.load(getClass().getResource("Register.fxml"));
		stag = (Stage) registerButton.getScene().getWindow();
		stag.setScene(new Scene(root));
	}
	
	public void switchToOverview() throws IOException {
		// Load person overview.
		//FXMLLoader loader = new FXMLLoader();
		//loader.setLocation(getClass().getResource("Overview.fxml"));
		//AnchorPane personOverview = (AnchorPane) loader.load();
		MySQLDatenbankConnection.disconnect();
		root = FXMLLoader.load(getClass().getResource("Overview.fxml"));
		
		// Give the controller access to the main app.
		//SceneOverviewController controller = loader.getController();
		//controller.setCarList(carlist);
		stag = (Stage) loginButton.getScene().getWindow();
		stag.setScene(new Scene(root));
		
	}
}
