package application;

import java.io.IOException;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SceneRegisterController {
	
	public Stage stag;
	public Scene scene;
	public Parent root;
	

	@FXML
	Button abbrechenButton;
	@FXML
	Button register;
	@FXML
	TextField usernameTextInput;
	@FXML
	TextField preNameTextInput;
	@FXML
	TextField surnameTextInput;
	@FXML
	TextField emailTextInput;
	@FXML
	TextField telefonTextInput;
	@FXML
	PasswordField password1Password;
	@FXML
	PasswordField password2Password;
	@FXML
	Label registerLabel;
	
	public void switchToLogin() throws IOException {
		root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stag = (Stage) abbrechenButton.getScene().getWindow();
		stag.setScene(new Scene(root));
	}
	
	public void register() throws IOException{
		String username = usernameTextInput.getText();
		String prename = preNameTextInput.getText();
		String surname = surnameTextInput.getText();
		String email = emailTextInput.getText();
		String telefon = telefonTextInput.getText();
		String password1 = password1Password.getText();
		String password2 = password2Password.getText();
		if(username.equals("")) {
			setLabel("Bitte (*) erforderliche Felder ausfüllen", registerLabel);
		}else if(password1.equals("")) {
			setLabel("Bitte (*) erforderliche Felder ausfüllen", registerLabel);
		}else if(!password1.equals(password2)) {
			setLabel("Passwords are unequal", registerLabel);
		}else {
			setLabel("", registerLabel);
			if(createUser(username, prename, surname, email, telefon, password1, "")) {
				switchToLogin();
			}
		}
	}
	
	public boolean createUser(String username, String preName, String lastName, String EMail, String Telefonnummer, String Password, String UserBildOrt) throws IOException{
		
		
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(`UserID`) FROM `userdata` WHERE 1");
		for(int i = 1;i <= i_max; i++) {
			String compare = MySQLDatenbankConnection.getString("SELECT `Username` FROM `userdata` WHERE `UserID`='" + i + "'");
			if(compare.equals(username)) {
				setLabel("Username ist schon vergeben", registerLabel);
				return false;
			}
		}
		i_max++;
		MySQLDatenbankConnection.update("INSERT INTO `userdata`(`UserID`, `Username`, `PreName`, `LastName`, `EMAil`, `Telefonnumber`, `Password`, `UserBildOrt`) VALUES ('" + i_max + "','" + username + "','" + preName + "','" + lastName + "','" + EMail + "','" + Telefonnummer + "','" + Password + "','" + UserBildOrt + "')");
		return true;
	}
	
	public static void setLabel(String Text, Label label) {
		label.setText(Text);
	}

}
