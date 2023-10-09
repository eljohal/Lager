package main.application;

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
	public static Stage oriStag;
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
		oriStag.setOnCloseRequest(event -> {
            event.consume(); // Verhindert das sofortige Schließen des Hauptfensters
            try {
            	abbrchen();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
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
	}
	
	public void delete() throws IOException {
		MySQLDatenbankConnection.update("DELETE FROM `cardata` WHERE `CarID` = '" + id + "'");
		stag = (Stage) accept.getScene().getWindow();
		SceneOverviewController.stag = stag;
		root = FXMLLoader.load(getClass().getClassLoader().getResource("Overview.fxml"));
		stag.setScene(new Scene(root));
		
	}
	public static void getStage(Stage stage) {
		oriStag = stage;
	}
	
	public void abbrchen() throws IOException {
		stag = (Stage) accept.getScene().getWindow();
		SceneOverviewController.stag = stag;
		root = FXMLLoader.load(getClass().getClassLoader().getResource("Overview.fxml"));
		stag.setScene(new Scene(root));
	}
	
	public static void setIDValue(int i, String herst, String modl) {
		id = i;
		hersteller = herst;
		modell = modl;
	}
}
