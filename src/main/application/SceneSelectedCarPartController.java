package main.application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SceneSelectedCarPartController {
	public Stage stag;
	public Scene scene;
	public Parent root;
	SceneCarPartsController sceneCarPartsController;
	@FXML
	TextField caid;
	DecimalFormat caridFormat = new DecimalFormat("000");
	@FXML
	TextField modell;
	static int carid;
	static int carpartid;
	private String modelle;
	
	
	@FXML
	TextField oriOENummer;
	String oeNummer;
	@FXML
	TextField eBayProdukt;
	String eBProd;
	@FXML
	TextField eBayPosition;
	String eBPos;
	@FXML
	TextField eBayFarbecode;
	String eBFarbc;
	@FXML
	TextField eBayFarbe;
	String eBFarb;
	@FXML
	TextField spgStrOri;
	String spgStr;
	
	
	@FXML
	TextField teilOri;
	String teile;
	@FXML
	TextField herstellerOri;
	String oriHerst;
	@FXML
	TextField oriTeilNrOri;
	String oriTNr;
	
	@FXML
	TextField zustOri;
	String oriZust;
	@FXML
	TextField eCatOri;
	int oriECat;
	@FXML
	TextField preisOri;
	double oriPreis;
	@FXML
	TextField versandDEOri;
	String oriVDE;
	@FXML
	TextField anzahlOri;
	int oriA;
	@FXML
	TextField titel;
	String oriTeil;
	@FXML
	TextField passendOri;
	String passendString;
	@FXML
	TextArea bemerkungOri;
	String bemOri;
	

	@FXML 
	Button abbrechen;
	@FXML
	TextFlow show;
	@FXML
	ImageView image;
	int imagecount;
	DecimalFormat filename = new DecimalFormat("000");
	static String Alphabet[] = {"","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	
	@FXML
	public void initialize() throws IOException {
		caid.setText("Fz.Nr.: "+caridFormat.format(carid).toString());
		modelle = ""+MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `cardata` WHERE `CarID` = " + carid)+" "+MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = " + carid);
		modell.setText("Fahrzeug: "+modelle);
		init();
		oriTeilNrOri.setText("orig. TeilNr.: "+oriTNr);
		bemerkungOri.setText(bemOri);
		teilOri.setText("Teil: "+teile);
		titel.setText(oriTeil);
		herstellerOri.setText("Hersteller: "+oriHerst);
		zustOri.setText("Zustand: "+oriZust);
		eCatOri.setText("eBay-Kat.: "+Integer.toString(oriECat));
		preisOri.setText(""+Double.toString(oriPreis)+" €");
		versandDEOri.setText("Versand: "+oriVDE);
		anzahlOri.setText("Anzahl: "+Integer.toString(oriA));
		
		oriOENummer.setText("OE-Nr.: "+oeNummer);
		eBayProdukt.setText("eBay-Prod.: "+eBProd);
		eBayPosition.setText("eBay-Pos.: "+eBPos);
		eBayFarbecode.setText("eBay-Farbc.: "+eBFarbc);
		eBayFarbe.setText("ebay-Farbe: "+eBFarb);
		spgStrOri.setText("Spg., Str.: "+spgStr);
		passendOri.setText("passend für: "+passendString);
		image(false);
	}
	
	public void init() {
		oriTNr = MySQLDatenbankConnection.getString("SELECT `OrignalTeilenummer` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		teile = MySQLDatenbankConnection.getString("SELECT `Teil` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		bemOri = MySQLDatenbankConnection.getString("SELECT `Bemerkung` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		oriTeil = MySQLDatenbankConnection.getString("SELECT `Bezeichnung` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		oriHerst = MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		oriZust = MySQLDatenbankConnection.getString("SELECT `Zustand` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		oriECat =  MySQLDatenbankConnection.getInt("SELECT `Kategorienummer` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		oriPreis = MySQLDatenbankConnection.getDouble("SELECT `Preis` FROM `carpartsdata` WHERE `CarPartID` = '" + carpartid + "' AND `CarID` = "+carid+"");
		oriVDE = MySQLDatenbankConnection.getString("SELECT `Versand` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");		
		oeNummer = MySQLDatenbankConnection.getString("SELECT `OENumber` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		eBProd = MySQLDatenbankConnection.getString("SELECT `eBayProduktart` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		eBPos = MySQLDatenbankConnection.getString("SELECT `eBayPosition` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		eBFarb = MySQLDatenbankConnection.getString("SELECT `eBayFarbe` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		eBFarbc = MySQLDatenbankConnection.getString("SELECT `eBayFarbecode` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		double oriSpg = MySQLDatenbankConnection.getDouble("SELECT `Spannung` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		String oriSpgE = MySQLDatenbankConnection.getString("SELECT `SpannungEinheit` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		double oriStr = MySQLDatenbankConnection.getDouble("SELECT `Stromstaerke` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		String oriStrE = MySQLDatenbankConnection.getString("SELECT `StromstaerkeEinheit` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		
		spgStr = doubleToString(oriSpg)+" "+oriSpgE+", "+doubleToString(oriStr)+" "+oriStrE;
		passendString = MySQLDatenbankConnection.getString("SELECT `Passend` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		oriA =  MySQLDatenbankConnection.getInt("SELECT `Menge` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
	}
	
	public String doubleToString(double value) {
		return String.valueOf(value);
	}
	
	public void image(boolean direction) throws IOException {
		Path path = createDir(carid);
		if(countImages(carpartid) > 0) {
			if(direction) {
				imagecount++;
				if(imagecount < countImages(carpartid)) {
					Path imageFile = path.resolve(""+filename.format(carpartid)+""+Alphabet[imagecount]+".jpg");
					centerImage(new Image(imageFile.toString()));
				}else if (imagecount >= countImages(carpartid)) {
					imagecount = countImages(carpartid) - 1;
				}
			}else {
				imagecount--;
				if(imagecount > 0) {
					Path imageFile = path.resolve(""+filename.format(carpartid)+""+Alphabet[imagecount]+".jpg");
					centerImage(new Image(imageFile.toString()));
				}if(imagecount <= 0) {
					imagecount = 0;
					Path imageFile = path.resolve(""+filename.format(carpartid)+""+Alphabet[imagecount]+".jpg");
					centerImage(new Image(imageFile.toString()));
				}
		}
		}
	}
	
	public void sceneGeneralSettings(SceneCarPartsController controller) {
		this.sceneCarPartsController = controller;
	}
	
	public static void setCarID(int cid) {
		carid = cid;		
	}
	
	public void centerImage(Image img) {
		if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = image.getFitWidth() / img.getWidth();
            double ratioY = image.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            image.setX((image.getFitWidth() - w) / 2);
            image.setY((image.getFitHeight() - h) / 2);
            image.setImage(img);

        }
	}
	public Path createDir(int carid) throws IOException {
		Path path = Paths.get(PictureDirectory.getDir()+ "\\" + filename.format(carid));
		if(!Files.exists(path)) {
			Files.createDirectories(path);
			return path;
		}
		return path;
	}
	public int countImages(int cpid) {
		File folder = new File(PictureDirectory.getDir()+ "\\" + filename.format(carid));
		File[] files = folder.listFiles();
		int count = 0;
		if(files != null) {
			for(File file : files) {
				if(file.isFile() && file.getName().contains(filename.format(cpid))) {
					count++;
				}
			}
			return count;
		}else {
			return 0;
		}
	}
	public void rightUp() throws IOException {
		image(true);
	}	
	
	public void leftDown() throws IOException {
		image(false);
	}
	public static void initCarPart(int cp){
		carpartid = cp;
	}
	public void goBack() throws IOException {
		show.getChildren().clear();
		SceneCarPartsController.setCarID(carid);
		SceneCarPartsController.closeCheckWindow();
	}
}
