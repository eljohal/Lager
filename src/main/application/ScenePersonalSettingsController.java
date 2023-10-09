package main.application;


import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class ScenePersonalSettingsController {
	public static Stage stag;
	static Stage checkWindow = new Stage();

	public static Stage oriStag;
	public Scene scene;
	public Parent root;
	
	@FXML
	Button generalSettings;
	@FXML 
	Button historie;
	@FXML
	Button logout;
	@FXML
	Button delete;
	@FXML
	Button changePassword;
	@FXML
	Button changeUser;
	@FXML
	Button abbrechen;
	Text userNameVergeben = new Text("Username ist schon vergeben");
	Text done = new Text("ƒnderungen wurde ¸bernommen");
	@FXML
	TextFlow textshow;
	@FXML
	TextField usernameTextInput;
	String uNPost;
	@FXML
	TextField preNameTextInput;
	String pNPost;
	@FXML
	TextField surnameTextInput;
	String sPost;
	@FXML
	TextField emailTextInput;
	String ePost;
	@FXML
	TextField telefonTextInput;
	String tPost;
	@FXML
	Label userNamePre;
	static String uNPre;
	@FXML
	Label PreNamePre;
	static String pNPre;
	@FXML
	Label surnamePre;
	static String sPre;
	@FXML
	Label emailPre;
	static String ePre;
	@FXML
	Label telephonPre;
	static String tPre;
	static String passwordPost = "";
	static int uID;
	
	public static void getData(int uID) throws ClassNotFoundException {
		
		uNPre = MySQLDatenbankConnection.getString("SELECT `Username` FROM `userdata` WHERE `UserID` = "+uID+"");
		pNPre = MySQLDatenbankConnection.getString("SELECT `PreName` FROM `userdata` WHERE `UserID` = "+uID+"");
		sPre = MySQLDatenbankConnection.getString("SELECT `LastName` FROM `userdata` WHERE `UserID` = "+uID+"");
		ePre = MySQLDatenbankConnection.getString("SELECT `EMail` FROM `userdata` WHERE `UserID` = "+uID+"");
		tPre = MySQLDatenbankConnection.getString("SELECT `Telefonnumber` FROM `userdata` WHERE `UserID` = "+uID+"");
	}
	
	@FXML 
	void initialize() throws IOException {
		setLabel(userNamePre, uNPre);
		setLabel(PreNamePre, pNPre);
		setLabel(surnamePre, sPre);
		setLabel(emailPre, ePre);
		setLabel(telephonPre, tPre);
		oriStag.setOnCloseRequest(event -> {
            event.consume(); // Verhindert das sofortige Schlieﬂen des Hauptfensters
            try {
            	goBack();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
	}
	
	public void setLabel(Label label, String string) {
		if(string.equals("")) {
			label.setText("");
		}else {
			label.setText(" "+string);
		}
	}
	public static void getStage(Stage stage) {
		oriStag = stage;
	}
	public void openGeneralSettings() throws IOException, ClassNotFoundException {
		
		stag = (Stage) generalSettings.getScene().getWindow();
		SceneGeneralSettingsController.getStage(stag);
		root = FXMLLoader.load(getClass().getClassLoader().getResource("GeneralSettings.fxml"));
		stag.setScene(new Scene(root));
	}
	public void goBack() throws IOException, ClassNotFoundException {
		
		stag = (Stage) abbrechen.getScene().getWindow();
		SceneOverviewController.stag = stag;
		root = FXMLLoader.load(getClass().getClassLoader().getResource("Overview.fxml"));
		stag.setScene(new Scene(root));
	}
	public void goToHistory() throws ClassNotFoundException, IOException{
		
		stag = (Stage) historie.getScene().getWindow();
		SceneHistorySettingsController.getStage(stag);
		root = FXMLLoader.load(getClass().getClassLoader().getResource("HistorieSettings.fxml"));
		stag.setScene(new Scene(root));
	}
	public void changePW() throws IOException {
		checkWindow.setScene(new Scene(FXMLLoader.load(getClass().getClassLoader().getResource("ChangePasswordSettings.fxml"))));
		showCheckWindow(checkWindow);
	}
	public void showCheckWindow(Stage stage) throws IOException {
		stage.show();
	}
	public static void closeCheckWindow() throws IOException {
		checkWindow.hide();
	}
	public void deleteInputs() {
		usernameTextInput.setText("");
		preNameTextInput.setText("");
		surnameTextInput.setText("");
		emailTextInput.setText("");
		telefonTextInput.setText("");
	}
	public void changeUD() throws ClassNotFoundException{
		if(usernameTextInput.getText().equals("")) {
			uNPost = uNPre;
		}else {
			uNPost = usernameTextInput.getText();
		}
		if(preNameTextInput.getText().equals("")) {
			pNPost = pNPre;
		}else {
			pNPost = preNameTextInput.getText();
		}if(surnameTextInput.getText().equals("")) {
			sPost = sPre;
		}else {
			sPost = surnameTextInput.getText();
		}if(emailTextInput.getText().equals("")) {
			ePost = ePre;
		}else {
			ePost = emailTextInput.getText();
		}if(telefonTextInput.getText().equals("")) {
			tPost = tPre;
		}else {
			tPost = telefonTextInput.getText();
		}if(passwordPost.equals("")) {
			passwordPost = MySQLDatenbankConnection.getString("SELECT `Password` FROM `userdata` WHERE `UserID` = '"+User.id+"'");
		}
		if(changeUser()) {
			MySQLDatenbankConnection.update("UPDATE `userdata` SET `Username`='"+uNPost+"',`UserBildOrt`='[value-3]',`Telefonnumber`='"+tPost+"',`PreName`='"+pNPost+"',`Password`='"+passwordPost+"',`LastName`='"+sPost+"',`EMail`='"+ePost+"' WHERE `UserID`='"+User.id+"'");
			getData(User.id);
			setLabel(userNamePre, uNPre);
			setLabel(PreNamePre, pNPre);
			setLabel(surnamePre, sPre);
			setLabel(emailPre, ePre);
			setLabel(telephonPre, tPre);
			usernameTextInput.setText("");
			preNameTextInput.setText("");
			textshow.getChildren().clear();
			surnameTextInput.setText("");
			emailTextInput.setText("");
			telefonTextInput.setText("");
			done.setFill(Color.BLACK);
			textshow.getChildren().addAll(done);
		}
		
	}
	public boolean changeUser() {
		if(!uNPost.equals(uNPre)) {
			int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(`UserID`) FROM `userdata` WHERE 1");
			for(int i = 1;i <= i_max; i++) {
				String compare = MySQLDatenbankConnection.getString("SELECT `Username` FROM `userdata` WHERE `UserID`='" + i + "'");
				if(compare.equals(uNPost)) {
					
					textshow.getChildren().clear();
					userNameVergeben.setFill(Color.RED);
					textshow.getChildren().addAll(userNameVergeben);
					return false;
				}
			}
			}
		return true;
	}
	
	public static void setNewPassword(String passwd) {
		passwordPost = passwd;
	}

}
