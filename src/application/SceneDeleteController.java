package application;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SceneDeleteController {
	static int id;
	static String hersteller;
	static String modell;
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
		Text fahrzeugmodl = new Text(hersteller + " " + modell);
		Text normal3 = new Text(" unwiderruflich löschen?");
		normal1.setFill(Color.BLACK);
		normal2.setFill(Color.BLACK);
		normal3.setFill(Color.BLACK);
		fahrzeugid.setFill(Color.RED);
		fahrzeugmodl.setFill(Color.RED);
		ausgabe.getChildren().addAll(normal1, fahrzeugid, normal2, fahrzeugmodl, normal3);
		//ausgabe.setText("Möchtest du das Fahrzeug (" + id + ") " + hersteller + " " + modell + " wirklich löschen?");
	}
	
	public void delete() throws IOException {
		//TO DO Delete car in MYSQL
		//ausgabe.setText("Möchtest du das Fahrzeug (" + id + ") " + hersteller + " " + modell + " wirklich löschen?");
		MySQLDatenbankConnection.update("DELETE FROM `cardata` WHERE `CarID` = '" + id + "'");
		root = FXMLLoader.load(getClass().getResource("Overview.fxml"));
		stag = (Stage) accept.getScene().getWindow();
		stag.setScene(new Scene(root));
		
	}
	
	
	public void abbrchen() throws IOException {
		root = FXMLLoader.load(getClass().getResource("Overview.fxml"));
		stag = (Stage) accept.getScene().getWindow();
		stag.setScene(new Scene(root));
	}
	
	public static void setIDValue(int i, String herst, String modl) {
		id = i;
		hersteller = herst;
		modell = modl;
	}
}
