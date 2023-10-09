package main.application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class ScenePasswordSettingsController {
	public Stage stag;
	public Scene scene;
	public Parent root;
	
	@FXML 
	Button changePassword;
	@FXML 
	Button close;
	@FXML
	PasswordField usernameTextInput;
	String password;
	@FXML
	PasswordField password1Password;
	@FXML
	PasswordField password2Password;
	@FXML
	TextFlow textshow;
	Text passwordEmpty = new Text("Bitte (*) erforderliche Felder ausfüllen");
	Text passwordNotEqual = new Text("Passwords are unequal");
	Text passwordWrong = new Text("falsches Passwort");
	
	public void goBack() throws IOException {
		ScenePersonalSettingsController.closeCheckWindow();
	}
	public void changePassword() throws ClassNotFoundException, IOException {
		password = MySQLDatenbankConnection.getString("SELECT `Password` FROM `userdata` WHERE `UserID` = '"+User.id+"'");
		if(usernameTextInput.getText().isEmpty() || password1Password.getText().isEmpty() || password2Password.getText().isEmpty()) {
			textshow.getChildren().clear();
			passwordEmpty.setFill(Color.RED);
			textshow.getChildren().addAll(passwordEmpty);
		}else if(!password1Password.getText().equals(password2Password.getText())) {
			textshow.getChildren().clear();
			passwordNotEqual.setFill(Color.RED);
			textshow.getChildren().addAll(passwordNotEqual);
		}else if(!password.equals(usernameTextInput.getText())) {
			textshow.getChildren().clear();
			passwordWrong.setFill(Color.RED);
			textshow.getChildren().addAll(passwordWrong);
		}else {
			ScenePersonalSettingsController.setNewPassword(password1Password.getText());
			ScenePersonalSettingsController.closeCheckWindow();
		}
		
		
	}
}
