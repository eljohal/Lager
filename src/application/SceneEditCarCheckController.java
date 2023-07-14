package application;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SceneEditCarCheckController {
	
	public Stage stag;
	public Scene scene;
	public Parent root;
	
	@FXML 
	Button back;
	@FXML
	Button add;
	@FXML
	TextFlow carpre;
	@FXML
	TextFlow carpost;	
	
	static String zulass;
	static String herstell;
	static String model;
	static String farbcode;
	static String fahrgestellnmr;
	static int hubraum;
	static int kilometer;
	static int leistung;
	static int tuer;
	static String motorcode;
	static String kraft;
	static String getriebecode;
	static String getriebeart;
	
	static String zulass2;
	static String herstell2;
	static String model2;
	static String farbcode2;
	static String fahrgestellnmr2;
	static int hubraum2;
	static int kilometer2;
	static int leistung2;
	static int tuer2;
	static String motorcode2;
	static String kraft2;
	static String getriebecode2;
	static String getriebeart2;
	static int carid;
	
	Text zul = new Text(" Erstzulassung: "+zulass);
	Text zul2 = new Text(" Erstzulassung: "+zulass2);
	Text hert = new Text(" Hersteller: "+herstell);
	Text hert2 = new Text(" Hersteller: "+herstell2);
	Text mod = new Text(" Modell: "+model);
	Text mod2 = new Text(" Modell: "+model2);
	Text fc = new Text(" Farbcode: "+farbcode);
	Text fc2 = new Text(" Farbcode: "+farbcode2);
	Text fahr = new Text(" Fahrgestellnnummer: "+fahrgestellnmr);
	Text fahr2 = new Text(" Fahrgestellnnummer: "+fahrgestellnmr2);
	Text hub = new Text(" Hubraum: "+hubraum);
	Text hub2 = new Text(" Hubraum: "+hubraum2);
	Text kil = new Text(" Kilometerstand: "+kilometer);
	Text kil2 = new Text(" Kilometerstand: "+kilometer2);
	Text leist = new Text(" Leistung: "+leistung);
	Text leist2 = new Text(" Leistung: "+leistung2);
	Text tur = new Text(" Türanzahl: "+tuer);
	Text tur2 = new Text(" Türanzahl: "+tuer2);
	Text motor = new Text(" Motorcode: "+motorcode);
	Text motor2 = new Text(" Motorcode: "+motorcode2);
	Text kraf = new Text(" Kraftstoff: "+kraft);
	Text kraf2 = new Text(" Kraftstoff: "+kraft2);
	Text getrieb = new Text(" Getriebecode: "+getriebecode);
	Text getrieb2 = new Text(" Getriebecode: "+getriebecode2);
	Text getrieba = new Text(" Getriebeart: "+getriebeart);
	Text getrieba2 = new Text(" Getriebeart: "+getriebeart2);
	
	public static void init(String zulasse, String herstelle, String modele, String farbcodee, String fahrgestellnmre, int hubraume, int kilometere, int leistunge, int tuerz, String motorcodee, String krafte, String getriebecodee, String getriebearte, int cd) {
		zulass = zulasse;
		tuer = tuerz;
		getriebeart = getriebearte;
		getriebecode = getriebecodee;
		kraft = krafte;
		herstell = herstelle;
		motorcode = motorcodee;
		model = modele;
		farbcode = farbcodee;
		fahrgestellnmr = fahrgestellnmre;
		hubraum = hubraume;
		kilometer = kilometere;
		leistung = leistunge;
		carid = cd;
	}
	
	public static void initSecondCar(String zulasse, String herstelle, String modele, String farbcodee, String fahrgestellnmre, int hubraume, int kilometere, int leistunge, int tuerz, String motorcodee, String krafte, String getriebecodee, String getriebearte) {
		zulass2 = zulasse;
		tuer2 = tuerz;
		getriebeart2 = getriebearte;
		getriebecode2 = getriebecodee;
		kraft2 = krafte;
		herstell2 = herstelle;
		motorcode2 = motorcodee;
		model2 = modele;
		farbcode2 = farbcodee;
		fahrgestellnmr2 = fahrgestellnmre;
		hubraum2 = hubraume;
		kilometer2 = kilometere;
		leistung2 = leistunge;
	}
	
	@FXML
	public void initialize() {
		compareText(hert, hert2);
		compareText(mod, mod2);
		compareText(zul, zul2);
		compareText(fc, fc2);
		compareText(fahr, fahr2);
		compareText(hub, hub2);
		compareText(kil, kil2);
		compareText(leist, leist2);
		compareText(tur, tur2);
		compareText(motor, motor2);
		compareText(kraf, kraf2);
		compareText(getrieb, getrieb2);
		compareText(getrieba, getrieba2);
	}
	
	
	public void compareText(Text pre, Text post) {
		if(Objects.equals(pre.getText(), post.getText())) {
			pre.setFill(Color.BLACK);
			post.setFill(Color.BLACK);
		}else {
			pre.setFill(Color.RED);
			post.setFill(Color.GREEN);
		}
		carpre.getChildren().add(pre);
		System.out.print(pre);
		carpre.getChildren().add(new Text(System.lineSeparator()));
		carpost.getChildren().add(post);
		System.out.print(post);
		carpost.getChildren().add(new Text(System.lineSeparator()));
	}
	
	static Stage stage1;
	public void addCar() throws IOException {
		System.out.print(zulass2);
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.uuuu");
		
		LocalDate zulassung = LocalDate.parse(zulass2, formatters);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(`ChangeID`) FROM `changehistorycar` WHERE 1");
		i_max++;
		MySQLDatenbankConnection.update("INSERT INTO `changehistorycar`(`TimeStamp`, `ChangeID`, `CarID`, `UserID`) VALUES ('"+timestamp+"','"+i_max+"','"+carid+"','"+User.id+"')");
		MySQLDatenbankConnection.update("UPDATE `cardata` SET `Erstzulassung`='"+zulassung+"',`Fahrgestellnummer`='"+fahrgestellnmr2+"',`Farbcode`='"+farbcode2+"',`Getriebeart`='"+getriebeart2+"',`Getriebecode`='"+getriebecode2+"',`Hersteller`='"+herstell2+"',`Hubraum`='"+hubraum2+"',`Kilometerstand`='"+kilometer2+"',`Kraftstoff`='"+kraft2+"',`Leistung`='"+leistung2+"',`Modell`='"+model2+"',`Motorcode`='"+motorcode2+"',`Türenanzahl`='"+tuer2+"' WHERE `CarID`='"+carid+"'");
		root = FXMLLoader.load(getClass().getResource("Overview.fxml"));
		stage1 = (Stage) add.getScene().getWindow();
		stage1.setScene(new Scene(root));
		SceneOverviewController.closeStag();
		
	
	}
	
	public void goBack() throws IOException {
		SceneEditCarController.closeCheckWindow();
	}
	
	
	
	public static void initStage(Stage stage) {
		// TODO Auto-generated method stub
		stage1 = stage;
	}
}
