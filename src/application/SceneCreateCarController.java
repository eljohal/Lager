package application;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SceneCreateCarController implements Initializable{
	
	public Stage stag;
	public Scene scene;
	public Parent root;
	
	@FXML
	private ComboBox<String> kraftstoff;
	private String[] fuel = {"Benzin", "Diesel", "Elektro", "Gas"};
	
	@FXML
	private ComboBox<String> getriebe;
	private String[] getr = {"Automatik","Schaltung"};

	@FXML
	TextField hersteller;
	@FXML
	TextField modell;
	@FXML
	TextField farbcode;
	@FXML
	DatePicker erstzulassung;
	@FXML
	TextField fahrgestellnummer;
	@FXML
	TextField hubraum;
	@FXML
	TextField kilometer;
	@FXML
	TextField getriebecode;
	@FXML
	TextField leistung;
	@FXML
	TextField motorcode;
	@FXML
	TextField tuercount;
	@FXML
	Button add;
	@FXML
	TextFlow show;
	@FXML 
	Button abbrechen;
	//
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		kraftstoff.getItems().addAll(fuel);
		getriebe.getItems().addAll(getr);
		
	}
	
	
	
	public void goBack() throws IOException {
		root = FXMLLoader.load(getClass().getResource("Overview.fxml"));
		stag = (Stage) abbrechen.getScene().getWindow();
		stag.setScene(new Scene(root));
	}
	
	public void addCar() {
		show.getChildren().clear();
		String herst = hersteller.getText();
		String modl = modell.getText();
		String farbe = farbcode.getText();
		String fahrgenmr = fahrgestellnummer.getText();
		String getr = getriebe.getValue();
		String getrco = getriebecode.getText();
		int hub = Integer.parseInt(hubraum.getText());
		int kilo = Integer.parseInt(kilometer.getText());
		LocalDate zulassung = erstzulassung.getValue();
		String kraft = kraftstoff.getValue();
		int leist = Integer.parseInt(leistung.getText());
		String motor = motorcode.getText();
		int tuer = Integer.parseInt(tuercount.getText());
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(`CarID`) FROM `cardata` WHERE 1");
		i_max++;
		MySQLDatenbankConnection.update("INSERT INTO `cardata`(`CarID`, `Erstzulassung`, `Fahrgestellnummer`, `Farbcode`, `Getriebeart`, `Getriebecode`, `Hersteller`, `Hubraum`, `Kilometerstand`, `Kraftstoff`, `Leistung`, `Modell`, `Motorcode`, `URL`, `Türenanzahl`) VALUES ('" + i_max + "','" + zulassung + "','" + fahrgenmr + "','" + farbe + "','" + getr + "','" + getrco + "','" + herst + "','" + hub + "','" + kilo + "','" + kraft + "','" + leist + "','" + modl + "','" + motor + "','url','" + tuer + "')");
		Text normal1 = new Text("Fahrzeug (");
		Text fahrzeugid = new Text("" + i_max);
		Text normal2 = new Text(") ");
		Text fahrzeugmodl = new Text(herst + " " + modl);
		Text normal3 = new Text(" wurde hinzugefügt. Kehre mit Abbrechen zurück.");
		normal1.setFill(Color.BLACK);
		normal2.setFill(Color.BLACK);
		normal3.setFill(Color.BLACK);
		fahrzeugid.setFill(Color.GREEN);
		fahrzeugmodl.setFill(Color.GREEN);
		show.getChildren().addAll(normal1, fahrzeugid, normal2, fahrzeugmodl, normal3);
		
	}
}
