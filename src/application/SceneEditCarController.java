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
	@FXML
	TextFlow herstell;
	//change all textflow to label
	@FXML
	TextFlow modl;
	@FXML
	TextFlow zulassg;
	@FXML
	TextFlow farbcd;
	@FXML
	TextFlow fahrgstellnmr;
	@FXML
	TextFlow hbr;
	@FXML
	TextFlow kmstand;
	@FXML
	TextFlow getrieb;
	@FXML
	TextFlow getriebco;
	@FXML
	TextFlow krftstf;
	@FXML
	TextFlow lst;
	@FXML
	TextFlow motrcd;
	@FXML
	TextFlow tuerz;
	
	@FXML
	public void initialize() {
		// TODO Auto-generated method stub
		herstell.getChildren().clear();
		modl.getChildren().clear();
		zulassg.getChildren().clear();
		Text zul = new Text(""+zulass);
		zul.getBaselineOffset();
		zulassg.getChildren().addAll(zul);
		farbcd.getChildren().clear();
		fahrgstellnmr.getChildren().clear();
		hbr.getChildren().clear();
		kmstand.getChildren().clear();
		getrieb.getChildren().clear();
		getriebco.getChildren().clear();
		krftstf.getChildren().clear();
		lst.getChildren().clear();
		motrcd.getChildren().clear();
		tuerz.getChildren().clear();
		kraftstoff.getItems().addAll(fuel);
		getriebe.getItems().addAll(getr);
		
	}
	
	
	public static void init(Car test) {
		carid = test.getCarID().intValue();
		LocalDate zulassung;
		zulassung = MySQLDatenbankConnection.getDate("SELECT `Erstzulassung` FROM `cardata` WHERE `CarID` = " + carid);
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.uuuu");
	    zulass = zulassung.format(formatters);
	}
	
	public void goBack() throws IOException {
		root = FXMLLoader.load(getClass().getResource("Overview.fxml"));
		stag = (Stage) abbrechen.getScene().getWindow();
		stag.setScene(new Scene(root));
	}
	
	public void addCar() {
		
	}
}
