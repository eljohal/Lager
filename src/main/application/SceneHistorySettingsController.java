package main.application;

import java.io.IOException;
import java.math.BigDecimal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SceneHistorySettingsController {
	public static Stage stag;
	public static Stage oriStag;
	public Scene scene;
	public Parent root;
	@FXML
	Button generalSettings;
	@FXML 
	Button historie;
	@FXML 
	Button settings;
	@FXML
	Button logout;
	@FXML
	Button abbrechen;
	@FXML
	TextFlow history;
	Text outputCar;
	Text car = new Text(" ƒnderungen an Autos: ");
	Text carPart = new Text(" ƒnderungen an Autoteilen: ");
	Text cars = new Text(" Auto: ");
	Text carse = new Text(" Autoteile: ");
	Text date = new Text(" Zeitpunkt: ");
	Text changes = new Text(" ƒnderung: ");
	@FXML
	public void initialize() { 
		setCarHistory();
		setCarPartHistory();
		oriStag.setOnCloseRequest(event -> {
            event.consume(); // Verhindert das sofortige Schlieﬂen des Hauptfensters
            try {
				goBack();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
	}
	
	public void setCarHistory() {
		car.setFill(Color.GREEN);
		history.getChildren().add(car);
		history.getChildren().add(new Text(System.lineSeparator()));
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(`ChangeID`) FROM `changehistorycar` WHERE `UserID` = "+User.id+"");;
		int z;
		int carid;
		String care;
		String dat;
		String chang;
		BigDecimal version;
		for(int i = 1; i <= i_max; i++) {
			z = MySQLDatenbankConnection.getInt("SELECT COUNT(*) as c FROM `changehistorycar` WHERE `ChangeID` = "+i+" AND `UserID` = "+User.id+"");
			if(z>0) {
				carid = MySQLDatenbankConnection.getInt("SELECT `CarID` FROM `changehistorycar` WHERE `ChangeID` = "+i+"");
				care = carid+" "+MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `cardata` WHERE `CarID` = "+carid+"")+" "+MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = "+carid+"");
				dat = MySQLDatenbankConnection.getString("SELECT `TimeStamp` FROM `changehistorycar` WHERE `ChangeID` = "+i+"");
				chang = MySQLDatenbankConnection.getString("SELECT `Changed` FROM `changehistorycar` WHERE `ChangeID` = "+i+"");
				version = MySQLDatenbankConnection.getFloat("SELECT `version` FROM `changehistorycar` WHERE `ChangeID` = "+i+"");
				String textToAdd = cars.getText() + care + date.getText() + dat + changes.getText() + chang + " Version: " + String.format("%.5f", version) + System.lineSeparator();
				history.getChildren().add(new Text(textToAdd));
			}
		}
	}
	
	public void setCarPartHistory() {
		carPart.setFill(Color.GREEN);
		history.getChildren().add(carPart);
		history.getChildren().add(new Text(System.lineSeparator()));
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(`ChangeID`) FROM `changehistorycarparts` WHERE `UserID` = "+User.id+"");;
		int z;
		int carid;
		int carpartid;
		int realcarpartid;
		String care;
		String dat;
		String chang;
		BigDecimal version;
		for(int i = 1; i <= i_max; i++) {
			z = MySQLDatenbankConnection.getInt("SELECT COUNT(*) as c FROM `changehistorycarparts` WHERE `ChangeID` = "+i+" AND `UserID` = "+User.id+"");
			if(z>0) {
				carpartid = MySQLDatenbankConnection.getInt("SELECT `CPID` FROM `changehistorycarparts` WHERE `ChangeID` = "+i+"");
				carid = MySQLDatenbankConnection.getInt("SELECT `CarID` FROM `carpartsdata` WHERE `CPID` = "+carpartid+"");
				realcarpartid = MySQLDatenbankConnection.getInt("SELECT `CarPartID` FROM `carpartsdata` WHERE `CPID` = "+carpartid+"");
				care = carid+" "+realcarpartid+" "+MySQLDatenbankConnection.getString("SELECT `Bezeichnung` FROM `carpartsdata` WHERE `CPID` = "+carpartid+"")+" "+MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `cardata` WHERE `CarID` = "+carid+"")+" "+MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = "+carid+"");
				dat = MySQLDatenbankConnection.getString("SELECT `TimeStamp` FROM `changehistorycarparts` WHERE `ChangeID` = "+i+"");
				chang = MySQLDatenbankConnection.getString("SELECT `Changed` FROM `changehistorycarparts` WHERE `ChangeID` = "+i+"");
				version = MySQLDatenbankConnection.getFloat("SELECT `Version` FROM `changehistorycarparts` WHERE `ChangeID` = "+i+"");
				String textToAdd = carse.getText() + care + date.getText() + dat + changes.getText() + chang + " Version: " + String.format("%.5f", version) + System.lineSeparator();
				history.getChildren().add(new Text(textToAdd));
			}
		}
	}
	public static void getStage(Stage stage) {
		oriStag = stage;
	}
	public void goBack() throws IOException {
		stag = (Stage) abbrechen.getScene().getWindow();
		SceneOverviewController.stag = stag;
		root = FXMLLoader.load(getClass().getClassLoader().getResource("Overview.fxml"));
		stag.setScene(new Scene(root));
	}
	public void goToSettings() throws ClassNotFoundException, IOException{
		ScenePersonalSettingsController.getData(User.id);
		stag = (Stage) generalSettings.getScene().getWindow();
		ScenePersonalSettingsController.getStage(stag);
		root = FXMLLoader.load(getClass().getClassLoader().getResource("PersonalSettings.fxml"));
		stag.setScene(new Scene(root));
	}
	public void openGeneralSettings() throws IOException, ClassNotFoundException {
		stag = (Stage) settings.getScene().getWindow();
		SceneGeneralSettingsController.getStage(stag);
		root = FXMLLoader.load(getClass().getClassLoader().getResource("GeneralSettings.fxml"));
		stag.setScene(new Scene(root));
	}
}
