package main.application;

import java.io.IOException;
import java.math.BigDecimal;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SceneDeleteCarPartController {
	private SceneCarPartsController sceneCarPartsController;
	static int id;
	static int carid;
	static String bezeichnung;
	static int index;
	public static Stage stag;
	public Scene scene;
	public Parent root;
	@FXML
	Button accept;
	@FXML
	Button abbrechen;
	@FXML
	TextFlow ausgabe;
	
	@FXML
	void initialize() {
		Text normal1 = new Text("Möchtest du das Fahrzeug (");
		Text fahrzeugid = new Text("" + id);
		Text normal2 = new Text(") ");
		Text fahrzeugmodl = new Text("" + bezeichnung);
		Text normal3 = new Text(" unwiderruflich löschen?");
		normal1.setFill(Color.BLACK);
		normal2.setFill(Color.BLACK);
		normal3.setFill(Color.BLACK);
		fahrzeugid.setFill(Color.RED);
		fahrzeugmodl.setFill(Color.RED);
		ausgabe.getChildren().addAll(normal1, fahrzeugid, normal2, fahrzeugmodl, normal3);
	}
	
	public void delete() throws IOException {
		double pre = MySQLDatenbankConnection.getDouble("SELECT `Preis` FROM `carpartsdata` WHERE `CarPartID` = '" + id + "' AND `CarID` = "+carid+"");
		SceneCarPartsController.substract = BigDecimal.valueOf(pre);
		MySQLDatenbankConnection.update("DELETE FROM `carpartsdata` WHERE `CarPartID` = '" + id + "' AND `CarID` = "+carid+"");
		sceneCarPartsController.removeCarPart(index);
		SceneCarPartsController.closeCheckWindow();
	}
	
	public void sceneGeneralSettings(SceneCarPartsController controller) {
		sceneCarPartsController = controller;
	}
	
	public void abbrchen() throws IOException {
		SceneCarPartsController.closeCheckWindow();
		sceneCarPartsController.startThread();
	}
	
	public static void setIDValue(int i, int x, String herst, int inde) {
		index = inde;
		id = i;
		carid = x;
		bezeichnung = herst;
	}
} 
