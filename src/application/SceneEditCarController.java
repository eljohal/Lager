package application;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SceneEditCarController {
	
	public static Stage stag;
	static Stage checkWindow = new Stage();
	public static Scene scene;
	public Parent root;
	
	@FXML
	private ComboBox<String> kraftstoff;
	private String[] fuel = {"Benzin", "Diesel", "Elektro", "Gas"};
	
	@FXML
	private ComboBox<String> getriebe;
	private String[] getr = {"Automatik","Schaltung"};
	
	static int carid;
	static String zulass;
	static String zulass2;
	static String herstell;
	static String herstell2;
	static String model;
	static String model2;
	static String farbcodee;
	static String farbcodee2;
	static String fahrgestellnmr;
	static String fahrgestellnmr2;
	static int hubraume;
	static int hubraume2;
	static int kilometere;
	static int kilometere2;
	static int leistunge;
	static int leistunge2;
	static int tuerz;
	static int tuerz2;
	static String motorcodee;
	static String motorcodee2;
	static String krafte;
	static String krafte2;
	static String getriebecodee;
	static String getriebecodee2;
	static String getriebearte;
	static String getriebearte2;
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
	public void initialize() throws IOException {
		//checkWindow.initModality(Modality.APPLICATION_MODAL);
		// TODO Auto-generated method stub
		herstelle.setText(" "+herstell);
		modle.setText(" "+model);
		farbcde.setText(" "+farbcodee);
		zulassge.setText(" "+zulass);
		kraftstoff.getItems().addAll(fuel);
		getriebe.getItems().addAll(getr);
		fahrgstellnmre.setText(" "+fahrgestellnmr);
		hbre.setText(" "+Integer.toString(hubraume));
		kmstande.setText(" "+Integer.toString(kilometere));
		getriebee.setText(" "+getriebearte);
		getriebcoe.setText(" "+getriebecodee);
		lste.setText(" "+Integer.toString(leistunge));
		krftstfe.setText(" "+krafte);
		motrcde.setText(" "+motorcodee);
		tuerze.setText(" "+Integer.toString(tuerz));		
	}
	
	
	public static void init(Car test) {
		carid = test.getCarID().intValue();
		LocalDate zulassung;
		zulassung = MySQLDatenbankConnection.getDate("SELECT `Erstzulassung` FROM `cardata` WHERE `CarID` = " + carid);
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.uuuu");
	    zulass = zulassung.format(formatters);
	    herstell = MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `cardata` WHERE `CarID` = " + carid);
	    model = MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = " + carid);
	    farbcodee = MySQLDatenbankConnection.getString("SELECT `Farbcode` FROM `cardata` WHERE `CarID` = " + carid);
	    fahrgestellnmr = MySQLDatenbankConnection.getString("SELECT `Fahrgestellnummer` FROM `cardata` WHERE `CarID` = " + carid);
	    hubraume = MySQLDatenbankConnection.getInt("SELECT `Hubraum` FROM `cardata` WHERE `CarID` = " + carid);
	    kilometere = MySQLDatenbankConnection.getInt("SELECT `Kilometerstand` FROM `cardata` WHERE `CarID` = " + carid);
	    leistunge = MySQLDatenbankConnection.getInt("SELECT `Leistung` FROM `cardata` WHERE `CarID` = " + carid);
	    motorcodee = MySQLDatenbankConnection.getString("SELECT `Motorcode` FROM `cardata` WHERE `CarID` = " + carid);
	    getriebecodee = MySQLDatenbankConnection.getString("SELECT `Getriebecode` FROM `cardata` WHERE `CarID` = " + carid);
	    getriebearte = MySQLDatenbankConnection.getString("SELECT `Getriebeart` FROM `cardata` WHERE `CarID` = " + carid);
	    tuerz = MySQLDatenbankConnection.getInt("SELECT `Türanzahl` FROM `cardata` WHERE `CarID` = " + carid);
	    krafte = MySQLDatenbankConnection.getString("SELECT `Kraftstoff` FROM `cardata` WHERE `CarID` = " + carid);
	}
	
	public void goBack() throws IOException {
		root = FXMLLoader.load(getClass().getResource("Overview.fxml"));
		stag = (Stage) abbrechen.getScene().getWindow();
		stag.setScene(new Scene(root));
	}
	
	public void compare() {
		
	}
	
	public void addCar() throws IOException {
		if(erstzulassung.getValue() == null) {
			zulass2 = zulass;
		}else {
			LocalDate zulassung = erstzulassung.getValue(); 
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.uuuu");
			zulass2 = zulassung.format(formatters);
		}
		if(hersteller.getText().isBlank()) {
			herstell2 = herstell;
		}else {
			herstell2 = hersteller.getText();
		}
		if(modell.getText().isBlank()) {
			model2 = model;
		}else {
			model2 = modell.getText();
		}
		if(farbcode.getText().isBlank()) {
			farbcodee2 = farbcodee;
		}else {
			farbcodee2 = farbcode.getText();
		}
		if(motorcode.getText().isBlank()) {
			motorcodee2 = motorcodee;
		}else {
			motorcodee2 = motorcode.getText();
		}
		if(getriebecode.getText().isBlank()) {
			getriebecodee2 = getriebecodee;
		}else {
			getriebecodee2 = getriebecode.getText();
		}
		if(fahrgestellnummer.getText().isBlank()) {
			fahrgestellnmr2 = fahrgestellnmr;
		}else {
			fahrgestellnmr2 = fahrgestellnummer.getText();
		}
		if(hubraum.getText().isBlank()) {
			hubraume2 = hubraume;
		}else {
			hubraume2 = Integer.parseInt(hubraum.getText());
		}
		if(kilometer.getText().isBlank()) {
			kilometere2 = kilometere;
		}else {
			kilometere2 = Integer.parseInt(kilometer.getText());
		}
		if(leistung.getText().isBlank()) {
			leistunge2 = leistunge;
		}else {
			leistunge2 = Integer.parseInt(leistung.getText());
		}
		if(tuercount.getText().isBlank()) {
			tuerz2 = tuerz;
		}else {
			tuerz2 = Integer.parseInt(tuercount.getText());
		}
		if(getriebe.getValue() == null) {
			getriebearte2 = getriebearte;
		}else {
			getriebearte2 = getriebe.getValue();
		}
		if(kraftstoff.getValue() == null) {
			krafte2 = krafte;
		}else {
			krafte2 = kraftstoff.getValue();
		}
		SceneEditCarCheckController.init(zulass, herstell, model, farbcodee, fahrgestellnmr, hubraume, kilometere, leistunge, tuerz, motorcodee, krafte, getriebecodee, getriebearte, carid);
		SceneEditCarCheckController.initSecondCar(zulass2, herstell2, model2, farbcodee2, fahrgestellnmr2, hubraume2, kilometere2, leistunge2, tuerz2, motorcodee2, krafte2, getriebecodee2, getriebearte2);			
		SceneEditCarCheckController.initStage(checkWindow);
		checkWindow.setScene(new Scene(FXMLLoader.load(getClass().getResource("EditCarCheck.fxml"))));
		compare(); //maybe um später Felder auch wieder zu entleeren, aber dafür muss in SceneCreateCar Anpassungen gemacht werden, damit es allgemein gestattet ist
		showCheckWindow(checkWindow);
	}
	
	public void showCheckWindow(Stage stage) throws IOException {
		stage.show();
	}
	
	public static void closeCheckWindow() throws IOException {
		checkWindow.hide();
	}
	
}
