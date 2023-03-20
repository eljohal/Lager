package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SceneEditCarController {
	
	public Stage stag;
	public Scene scene;
	public Parent root;
	
	@FXML
	private ComboBox<String> kraftstoff;
	private String[] fuel = {"Benzin", "Diesel", "Elektro", "Gas"};
	
	@FXML
	private ComboBox<String> getriebe;
	private String[] getr = {"Automatik","Schaltung"};
	
	static int carid;
	static String zulass;
	static String herstell;
	static String model;
	static String farbcodee;
	static String fahrgestellnmr;
	static int hubraume;
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
	//change all textflow to label
	@FXML
	Label herstelle;
	@FXML
	Label modle;
	@FXML
	Label zulassge;
	@FXML
	Label farbcde;
	@FXML
	Label fahrgstellnmre;
	@FXML
	Label hbre;
	@FXML
	Label kmstande;
	@FXML
	Label getriebee;
	@FXML
	Label getriebcoe;
	@FXML
	Label krftstfe;
	@FXML
	Label lste;
	@FXML
	Label motrcde;
	@FXML
	Label tuerze;
	
	@FXML
	public void initialize() {
		// TODO Auto-generated method stub
		herstelle.setText(" "+herstell);
		modle.setText(" "+model);
		farbcde.setText(" "+farbcodee);
		zulassge.setText(" "+zulass);
		kraftstoff.getItems().addAll(fuel);
		getriebe.getItems().addAll(getr);
		fahrgstellnmre.setText(" "+fahrgestellnmr);
		hbre.setText(" "+Integer.toString(hubraume));
	}
	
	
	public static void init(Car test) {
		carid = test.getCarID().intValue();
		LocalDate zulassung;
		zulassung = MySQLDatenbankConnection.getDate("SELECT `Erstzulassung` FROM `cardata` WHERE `CarID` = " + carid);
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.uuuu");
	    zulass = zulassung.format(formatters);
	    
	    herstell = MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `cardata` WHERE `CarID` = " + carid);
	    model = MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = " + carid);
	    farbcodee = MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = " + carid);
	    fahrgestellnmr = MySQLDatenbankConnection.getString("SELECT `Fahrgestellnummer` FROM `cardata` WHERE `CarID` = " + carid);
	    hubraume = MySQLDatenbankConnection.getInt("SELECT `Hubraum` FROM `cardata` WHERE `CarID` = " + carid);
	}
	
	public void goBack() throws IOException {
		root = FXMLLoader.load(getClass().getResource("Overview.fxml"));
		stag = (Stage) abbrechen.getScene().getWindow();
		stag.setScene(new Scene(root));
	}
	
	public void addCar() {
		
	}
}
