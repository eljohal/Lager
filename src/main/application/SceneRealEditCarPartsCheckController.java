package main.application;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SceneRealEditCarPartsCheckController {
	public Stage stag;

	public static Stage oriStag;
	public Scene scene;
	public Parent root;
	SceneRealEditCarPartController sceneCarPartsController;

	static int carid;
	static int carpartid;
	@FXML 
	Button back;
	@FXML
	Button add;
	@FXML
	TextFlow carpre;
	@FXML
	TextFlow carpost;	

	
	static String titel;
	static String teil;
	static String hersteller;
	static String oriTeileNr;
	static String zustand;
	static int eCat;
	static double preis;
	static String versandDE;
	static int menge;
	static String bemerkung;
	static String bemerkungOWN;
	static String passend;
	
	static String oeS;
	static String ebfarbCodeS;
	static String ebfarbeS;
	static String ebPosS;
	static String ebProdS;
	static String strES;
	static String spgES;
	static double strS;
	static double spgS;
	
	static String titel2;
	static String teil2;
	static String hersteller2;
	static String oriTeileNr2;
	static String zustand2;
	static int eCat2;
	static double preis2;
	static String versandDE2;
	static int menge2;
	static String bemerkung2;
	static String bemerkungOWN2;
	static String passend2;
	
	static String oeS2;
	static String ebfarbCodeS2;
	static String ebfarbeS2;
	static String ebPosS2;
	static String ebProdS2;
	static String strES2;
	static String spgES2;
	static double strS2;
	static double spgS2;
	
	String changes = "";
	
	Text oeT = new Text(" OE-Nummer: "+oeS);
	Text oeT2 = new Text(" OE-Nummer: "+oeS2);
	Text ebfarbCodeT = new Text(" Farbcode: "+ebfarbCodeS);
	Text ebfarbCodeT2 = new Text(" Farbcode: "+ebfarbCodeS2);
	Text ebfarbeT = new Text(" Farbe: "+ebfarbeS);
	Text ebfarbeT2 = new Text(" Farbe: "+ebfarbeS2);
	Text ebPosT = new Text(" Position: "+ebPosS);
	Text ebPosT2 = new Text(" Position: "+ebPosS2);
	Text ebProdT = new Text(" Produktart: "+ebProdS);
	Text ebProdT2 = new Text(" Produktart: "+ebProdS2);
	Text strET = new Text(" Stromeinheit: "+strES);
	Text strET2 = new Text(" Stromeinheit: "+strES2);
	Text spgET = new Text(" Spannungseinheit: "+spgES);
	Text spgET2 = new Text(" Spannungseinheit: "+spgES2);
	Text strT = new Text(" Stromstärke: "+strS);
	Text strT2 = new Text(" Stromstärke: "+strS2);
	Text spgT = new Text(" Spannung: "+spgS);
	Text spgT2 = new Text(" Spannung: "+spgS2);
	Text pass = new Text(" passend für: "+passend);
	Text pass2 = new Text(" passend für: "+passend2);
	Text bemerkOWN2 = new Text(" eigene Bemerkung: "+bemerkungOWN2);
	Text bemerkOWN = new Text(" eigene Bemerkung: "+bemerkungOWN);
	Text tit = new Text(" Titel: "+titel);
	Text tit2 = new Text(" Titel: "+titel2);
	Text tei = new Text(" Teil: "+teil);
	Text tei2 = new Text(" Teil: "+teil2);
	Text herstelle = new Text(" Hersteller: "+hersteller);
	Text herstelle2 = new Text(" Hersteller: "+hersteller2);
	Text oriTeileN = new Text(" Teilenummer: "+oriTeileNr);
	Text oriTeileN2 = new Text(" Teilenummer: "+oriTeileNr2);
	Text zustan = new Text(" Zustand: "+zustand);
	Text zustan2 = new Text(" Zustand: "+zustand2);
	Text eCa = new Text(" eBay Kategorie: "+eCat);
	Text eCa2 = new Text(" eBay Kategorie: "+eCat2);
	Text prei = new Text(" Preis: "+preis);
	Text prei2 = new Text(" Preis: "+preis2);
	Text versandD = new Text(" Versand: "+versandDE);
	Text versandD2 = new Text(" Versand: "+versandDE2);
	Text meng = new Text(" Menge: "+menge);
	Text meng2 = new Text(" Menge: "+menge2);
	Text bemerkun = new Text(" Bemerkung: "+bemerkung);
	Text bemerkun2 = new Text(" Bemerkung: "+bemerkung2);
	
	public static void init(String tite,String teile,String herste,String oriT,String zust,int eC, double pre, String vD, int men, String bem, String oe, String ebfarbCode, String ebfarbe, String ebPos, String ebProd, String strE, String spgE, double str, double spg, String passe, String beme) {
		titel = tite;
		teil = teile;
		hersteller = herste;
		oriTeileNr = oriT;
		zustand = zust;
		eCat = eC;
		preis = pre;
		versandDE = vD;
		menge = men;
		bemerkung = bem;
		
		oeS = oe;
		ebfarbCodeS = ebfarbCode;
		ebfarbeS = ebfarbe;
		ebPosS = ebPos;
		ebProdS = ebProd;
		strES = strE;
		spgES = spgE;
		strS = str;
		spgS = spg;
		passend = passe;
		bemerkungOWN = beme;
	}
	public static void initSecond(String tite,String teile,String herste,String oriT,String zust,int eC, double pre, String vD, int men, String bem, String oe, String ebfarbCode, String ebfarbe, String ebPos, String ebProd, String strE, String spgE, double str, double spg, String passe, String beme) {
		titel2 = tite;
		teil2 = teile;
		hersteller2 = herste;
		oriTeileNr2 = oriT;
		zustand2 = zust;
		eCat2 = eC;
		preis2 = pre;
		versandDE2 = vD;
		menge2 = men;
		bemerkung2 = bem;
		
		oeS2 = oe;
		ebfarbCodeS2 = ebfarbCode;
		ebfarbeS2 = ebfarbe;
		ebPosS2 = ebPos;
		ebProdS2 = ebProd;
		strES2 = strE;
		spgES2 = spgE;
		strS2 = str;
		spgS2 = spg;
		passend2 = passe;
		bemerkungOWN2 = beme;
	}
	
	public void initialize() {
		compareText(tit, tit2);
		compareText(tei, tei2);
		compareText(herstelle, herstelle2);
		compareText(oriTeileN, oriTeileN2);
		compareText(zustan, zustan2);
		compareText(eCa, eCa2);
		compareText(prei, prei2);
		compareText(versandD, versandD2);
		compareText(meng, meng2);
		compareText(bemerkun, bemerkun2);
		

		compareText(oeT, oeT2);
		compareText(ebfarbCodeT, ebfarbCodeT2);
		compareText(ebfarbeT, ebfarbeT2);
		compareText(ebPosT, ebPosT2);
		compareText(ebProdT, ebProdT2);
		compareText(spgT, spgT2);
		compareText(spgET, spgET2);
		compareText(strT, strT2);
		compareText(strET, strET2);
		compareText(pass, pass2);
		compareText(bemerkOWN, bemerkOWN2);
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
		carpre.getChildren().add(new Text(System.lineSeparator()));
		carpost.getChildren().add(post);
		carpost.getChildren().add(new Text(System.lineSeparator()));
	}
	public static void setCarID(int cid) {
		carid = cid;		
	}
	public static void initCarPart(int cp){
		carpartid = cp;
	}
	public void sceneGeneralSettings(SceneRealEditCarPartController controller) {
		sceneCarPartsController = controller;
	}
	public void getStage(Stage stage) {
		oriStag = stage;
	}
	static Stage stage1;
	public void addCar() throws IOException {
		//DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.uuuu");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(`ChangeID`) FROM `changehistorycarparts` WHERE 1");
		i_max++;
		int cpid = MySQLDatenbankConnection.getInt("SELECT `CPID` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		BigDecimal version = MySQLDatenbankConnection.getFloat("SELECT `Version` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		/*Das Problem, das Sie beschreiben, ist auf die Art und Weise zurückzuführen, wie Gleitkommazahlen in Java intern dargestellt werden. Java verwendet den IEEE 754-Standard zur Darstellung von Gleitkommazahlen im Binärformat. Aufgrund von Rundungsfehlern und begrenzter Genauigkeit kann es zu solchen Ungenauigkeiten kommen.*/
		sceneCarPartsController.versionCheckerThread.pauseThread();
		version = version.add(new BigDecimal("0.00001"));
		changes = "edit";
		MySQLDatenbankConnection.update("INSERT INTO `changehistorycarparts`(`CPID`, `UserID`, `TimeStamp`, `ChangeID`, `CarID`, `Changed`, `CarPartID`, `Bezeichnung`, `Hersteller`, `OrignalTeilenummer`, `Zustand`, `Bemerkung`, `Kategorienummer`, `Preis`, `Menge`, `Versand`, `eBayProduktart`, `versandEU`, `Teil`, `OENumber`, `eBayPosition`, `eBayFarbe`, `eBayFarbecode`, `Spannung`, `SpannungEinheit`, `Stromstaerke`, `StromstaerkeEinheit`, `Passend`, `Version`) VALUES ('"+cpid+"','"+User.id+"','"+timestamp+"','"+i_max+"','"+carid+"','"+changes+"','"+carpartid+"','"+titel2+"','"+hersteller2+"','"+oriTeileNr2+"','"+zustand2+"','"+bemerkung2+"','"+eCat2+"','"+preis2+"','"+menge2+"','"+versandDE2+"','"+ebProdS2+"','"+bemerkungOWN2+"','"+teil2+"','"+oeS2+"','"+ebPosS2+"','"+ebfarbeS2+"', '"+ebfarbCodeS2+"','"+spgS2+"','"+spgES2+"','"+strS2+"','"+strES2+"','"+passend2+"','"+version+"')");
		MySQLDatenbankConnection.update("UPDATE `carpartsdata` SET `Bezeichnung`='"+titel2+"',`Hersteller`='"+hersteller2+"',`OrignalTeilenummer`='"+oriTeileNr2+"',`Zustand`='"+zustand2+"',`Bemerkung`='"+bemerkung2+"',`Kategorienummer`='"+eCat2+"',`Preis`='"+preis2+"',`Menge`='"+menge2+"',`Versand`='"+versandDE2+"',`eBayProduktart`='"+ebProdS2+"',`versandEU`='"+bemerkungOWN2+"',`Teil`='"+teil2+"',`OENumber`='"+oeS2+"',`eBayPosition`='"+ebPosS2+"',`eBayFarbe`='"+ebfarbeS2+"',`eBayFarbecode`='"+ebfarbCodeS2+"',`Spannung`='"+spgS2+"',`SpannungEinheit`='"+spgES2+"',`Stromstaerke`='"+strS2+"',`StromstaerkeEinheit`='"+strES2+"',`Passend`='"+passend2+"',`Version`='"+version+"' WHERE `CPID` = "+cpid+"");
		SceneCarPartsController.editInCarPartData(new CarParts(carpartid, cpid, titel2, zustand2, hersteller2, oriTeileNr2, preis2,versandDE2,menge2));
		sceneCarPartsController.getStage(oriStag);
		sceneCarPartsController.goBack();
	}
	public void goBack() throws IOException {
		sceneCarPartsController.getStage(oriStag);
		sceneCarPartsController.closeCheckWindow();
	}
}
