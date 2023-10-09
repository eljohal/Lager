package main.application;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.logging.log4j.*;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SceneVersionChangeController {

	private static final Logger logger = LogManager.getLogger(SceneVersionChangeController.class);
	SceneRealEditCarPartController sceneCarPartsController;
	SceneEditCarController sceneEditCarController;
	public static Stage stag;
	public Scene scene;
	public Parent root;
	public static Stage oriStag;
	public int carid;
	String modelle;
	public int carpartid;
	String carPartName;
	String userName;
	String userMail;
	@FXML
	Button add;
	@FXML
	Button abbrechen;
	@FXML
	private TextField caid;
	@FXML
	private TextField modell;
	@FXML
	private TextField user;
	String controller;
	DecimalFormat caridFormat = new DecimalFormat("000");
	@FXML
	Label output;
	String status;
	
	@FXML
	public void initialize(){
		output.setText("Die Daten an denen du arbeitest wurden während deiner Bearbeitung von einem anderen Nutzer bearbeitet! Möchtest du die Änderungen übernehmen oder die Bearbeitung abbrechen?");	
	}
	public void sceneGeneralSettings(SceneRealEditCarPartController controller) {
		this.sceneCarPartsController = controller;
	}
	
	public void sceneEditCar(SceneEditCarController controller) {
		this.sceneEditCarController = controller;
	}
	
	public void setText(String status) {
		this.status = status;
		if(status.equals("SceneRealEditCarPartController")) {
			modelle = ""+MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `cardata` WHERE `CarID` = " + carid)+" "+MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = " + carid);
			caid.setText(caridFormat.format(carid).toString()+" "+modelle);
			carPartName = MySQLDatenbankConnection.getString("SELECT `Bezeichnung` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
			modell.setText(carpartid+" "+carPartName);
			int userid = getUserID();			
			userName = MySQLDatenbankConnection.getString("SELECT `Username` FROM `userdata` WHERE `UserID` = "+userid+"");
			userMail = MySQLDatenbankConnection.getString("SELECT `EMail` FROM `userdata` WHERE `UserID` = "+userid+"");
			user.setText("Die Daten wurden von "+userName+" geändert. E-Mail: "+userMail);
			
		}
		if(status.equals("SceneEditCarController")) {
			modelle = ""+MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `cardata` WHERE `CarID` = " + carid)+" "+MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = " + carid);
			caid.setText(caridFormat.format(carid).toString()+" "+modelle);
			modell.setText("Autodaten wurden geändert");
			int userid = getUserID();			
			userName = MySQLDatenbankConnection.getString("SELECT `Username` FROM `userdata` WHERE `UserID` = "+userid+"");
			userMail = MySQLDatenbankConnection.getString("SELECT `EMail` FROM `userdata` WHERE `UserID` = "+userid+"");
			user.setText("Die Daten wurden von "+userName+" geändert. E-Mail: "+userMail);
		}
	}
	
	public int getUserID() {
		if(status.equals("SceneRealEditCarPartController")) {
			BigDecimal version = MySQLDatenbankConnection.getFloat("SELECT `Version` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
			
			int id = MySQLDatenbankConnection.getInt("SELECT `UserID` FROM `changehistorycarparts` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+" AND `Version` = "+version.toPlainString()+"");
			
			return id;
		}else if(status.equals("SceneEditCarController")) {
			BigDecimal version = MySQLDatenbankConnection.getFloat("SELECT `version` FROM `cardata` WHERE `CarID` = "+carid+"");
			
			int id = MySQLDatenbankConnection.getInt("SELECT `UserID` FROM `changehistorycar` WHERE `CarID` = "+carid+" AND `version` = "+version.toPlainString()+"");
			
			return id;
		}else {
			return 0;
		}
	}
	
	public void getStage(Stage stage) {
		oriStag = stage;
	}
	public void addCarParts() {
		if(status.equals("SceneRealEditCarPartController")) {
			sceneCarPartsController.init();
			sceneCarPartsController.initView(); 
			try {
				sceneCarPartsController.closeCheckWindow();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("Version Decision Window Error SceneVersionChangeController Edit CarPart, addCarParts(): ", e);
			}
		}else if(status.equals("SceneEditCarController")) {
			SceneEditCarController.init(carid);
			sceneEditCarController.initView();
			sceneEditCarController.initVersionComboBox();
			try {
				sceneEditCarController.closeCheckWindow();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("Version Decision Window Error SceneVersionChangeController Edit Car, addCarParts(): ", e);
			}
		}
	}
	public void setCarID(int id) {
		carid = id;
	}
	
	public void setCarPartID(int id) {
		carpartid = id;
	}
	public void goBack() {
		if(sceneCarPartsController != null && status.equals("SceneRealEditCarPartController")) {
			try {
				sceneCarPartsController.getStage(oriStag);
				sceneCarPartsController.closeCheckWindow();
				sceneCarPartsController.goBack();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("Version Decision Window Error SceneVersionChangeController Edit CarPart, goBack(): ", e);
			}
		}else if(sceneEditCarController != null && status.equals("SceneEditCarController")) {
			try {
				SceneEditCarController.getStage(oriStag);
				sceneEditCarController.closeCheckWindow();
				sceneEditCarController.goBack();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("Version Decision Window Error SceneVersionChangeController Edit Car, goBack(): ", e);
			}
		}
	}
}
