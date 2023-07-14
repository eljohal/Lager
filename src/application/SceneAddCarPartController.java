package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SceneAddCarPartController {
	public Stage stag;
	public Scene scene;
	public Parent root;
	@FXML
	Label caid;
	DecimalFormat caridFormat = new DecimalFormat("000");
	@FXML
	Label modell;
	static int carid;
	@FXML
	private TextField teil;
	private AutoCompletionBinding<String> autoComplete;
	private Set<String> possibleSuggestions = new HashSet<>();
	@FXML
	private ComboBox<String> hersteller;
	private String[] herstelle = {"Bosch", "Volkswagen", "Porsche", "weiter..."};
	@FXML
	TextField oriTeilNr;
	@FXML
	TextArea bemerkung;
	@FXML
	private ComboBox<String> zust;
	private String[] zustand = {"neu", "gebraucht", "defekt", "weiter..."};
	@FXML
	TextField preis;
	@FXML
	private ComboBox<String> versandDE;
	private String[] versandD = {"S", "M", "L", "weiter..."};
	@FXML
	private ComboBox<String> versandEU;
	private String[] versandE = {"S", "M", "L", "weiter..."};
	@FXML
	TextField anzahl;
	@FXML
	TextField titel;
	@FXML 
	Button abbrechen;
	@FXML 
	Button add;
	@FXML 
	Button reload;
	@FXML
	TextFlow show;
	@FXML
	ImageView image;
	DecimalFormat filename = new DecimalFormat("000");
	static String Alphabet[] = {"","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	
	@FXML
	Button rightpic;
	private String modelle;
	String tite;
	String herst;
	String oriT;
	String zusta;
	String bemer;
	int kategorie;
	double pr;
	int meng;
	String vers;
	String versEU;
	int imagecount;
	
	Text nothingSelected = new Text("Bitte Titel über Reload Button erstellen");
	Text missingteil = new Text("Bitte vorher ein Teil auswählen");
	
	@FXML
	public void initialize() throws IOException {
		initTeile();
		autoComplete = TextFields.bindAutoCompletion(teil, possibleSuggestions);
		teil.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				ke.consume();
				switch (ke.getCode()) {
				case ENTER:
					autoCompletionLearnWord(teil.getText().trim());
					break;
				default:
					break;
				}
			}
		});
		
		hersteller.getItems().addAll(herstelle);
		zust.getItems().addAll(zustand);
		versandDE.getItems().addAll(versandD);
		versandEU.getItems().addAll(versandE);
		caid.setText("  Fz.Nr.: "+caridFormat.format(carid).toString());
		modelle = ""+MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `cardata` WHERE `CarID` = " + carid)+" "+MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = " + carid);
		modell.setText("  Fahrzeug: "+modelle);
		image(false);
	}
	
	public void initTeile() {
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(TID) FROM `möglicheteile` WHERE 1");
		for(int i = 0; i <= i_max; i++) {
			System.out.print("H"+MySQLDatenbankConnection.getString("SELECT `Part` FROM `möglicheteile` WHERE `TID` = '" + i + "'"));
			possibleSuggestions.add(MySQLDatenbankConnection.getString("SELECT `Part` FROM `möglicheteile` WHERE `TID` = '" + i + "'"));
		}
	}
	public void image(boolean direction) throws IOException {
		Path path = createTempDir();
		getFileID(path);
		if(getFileID(path) > 0) {
			if(direction) {
				imagecount++;
				if(imagecount < getFileID(path)) {
					Path imageFile = path.resolve(""+imagecount+".jpg");
					//image.setImage(new Image(imageFile.toString()));
					centerImage(new Image(imageFile.toString()));
				}else if (imagecount >= getFileID(path)) {
					imagecount = getFileID(path) - 1;
				}
			}else {
				imagecount--;
				if(imagecount > 0) {
					Path imageFile = path.resolve(""+imagecount+".jpg");
					//image.setImage(new Image(imageFile.toString()));
					centerImage(new Image(imageFile.toString()));
				}if(imagecount <= 0) {
					imagecount = 0;
					Path imageFile = path.resolve(""+imagecount+".jpg");
					//image.setImage(new Image(imageFile.toString()));
					centerImage(new Image(imageFile.toString()));
				}
		}
		}
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
	@FXML
	private void handleDragOver(DragEvent event) {
		if(event.getDragboard().hasFiles()) {
		event.acceptTransferModes(TransferMode.ANY);
		}
	}
	@FXML
	private void handleDrop(DragEvent event) throws IOException {
		Path path = createTempDir();
		List<File> files = event.getDragboard().getFiles();
		int x = getFileID(path);
		for(int i = 0; i < files.size(); i++) {
			Path copied = Paths.get(files.get(i).getAbsolutePath());
			Path newFile = Files.createFile(path.resolve(""+(i+x)+".jpg"));
			Files.copy(copied, newFile, StandardCopyOption.REPLACE_EXISTING);
		}
		image(false);
	}
	private void copyImageToDestination(int cpid) throws IOException {
		Path path = createDir(carid);
		Path pathe = createTempDir();
		int x = getFileID(pathe);
		for(int i = 0; i < x; i++) {
			Path copied = Paths.get(pathe.resolve(""+i+".jpg").toUri());
			Path newFile = Files.createFile(path.resolve(""+filename.format(cpid)+""+Alphabet[i]+".jpg"));
			Files.copy(copied, newFile, StandardCopyOption.REPLACE_EXISTING);
			Files.deleteIfExists(pathe.resolve(""+i+".jpg"));
		}
	}
	public Path createDir(int carid) throws IOException {
		Path path = Paths.get("C:\\Users\\jhaug\\Desktop\\TestBilder\\" + filename.format(carid));
		if(!Files.exists(path)) {
			Files.createDirectories(path);
			return path;
		}
		return path;
	}
	private int getFileID(Path path) {
		return new File(path.toString()).list().length;
	}
	
	public Path createTempDir() throws IOException {
		Path path = Paths.get("C:\\Users\\jhaug\\Desktop\\TestBilder\\Temp");
		if(!Files.exists(path)) {
			Files.createDirectories(path);
			return path;
		}
		return path;
	}
	
	private void autoCompletionLearnWord(String newWord) {
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(TID) FROM `möglicheteile` WHERE 1");
		System.out.print(""+i_max++);
		MySQLDatenbankConnection.update("INSERT INTO `möglicheteile`(`TID`, `Part`) VALUES ('"+ i_max++ +"','"+newWord+"')");
		if(autoComplete != null) {
			autoComplete.dispose();
		}
		initTeile();
		autoComplete = TextFields.bindAutoCompletion(teil, possibleSuggestions);
		
	}
	
	public static void setCarID(int cid) {
		carid = cid;		
	}
	
	public void reload () {
		if(teil.getText().equals("")) {
			show.getChildren().clear();
			show.getChildren().addAll(missingteil);
			
		}else if(oriTeilNr.getText().equals("") && teil.getText() != null) {
			show.getChildren().clear();
			titel.setText(""+teil.getText().toString()+" "+modelle);
		}else {
			show.getChildren().clear();
			titel.setText(""+teil.getText().toString()+" "+oriTeilNr.getText()+" "+modelle);
		}
		
	}
	
	public void addCarParts() throws IOException {
		if(titel.getText().equals("")) {
			show.getChildren().clear();
			show.getChildren().addAll(nothingSelected);
		}else {
			show.getChildren().clear();
			tite = titel.getText();
			oriT = oriTeilNr.getText();
			if(anzahl.getText().isEmpty()) {
				meng = 0;
			}else {
				meng = Integer.parseInt(anzahl.getText());
			}
			if(hersteller.getValue() == null) {
				herst = "0";
			}else {
				herst = hersteller.getValue();
			}
			if(zust.getValue() == null) {
				zusta = "0";
			}else {
				zusta = zust.getValue();
			}
			if(versandDE.getValue() == null) {
				vers = "0";
			}else {
				vers = versandDE.getValue();
			}
			if(versandEU.getValue() == null) {
				versEU = "0";
			}else {
				versEU = versandEU.getValue();
			}
			if(preis.getText().isEmpty()) {
				pr = 0;
			}else {
				pr = Double.parseDouble(preis.getText().replace(",", "."));
			}
			bemer = bemerkung.getText();
			int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(CarPartID) FROM `carpartsdata` WHERE `CarID` = '"+carid+"'");
			int y_max = MySQLDatenbankConnection.getInt("SELECT MAX(CPID) FROM `carpartsdata`");
			i_max++;
			y_max++;
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			MySQLDatenbankConnection.update("INSERT INTO `carpartsdata`(`CarPartID`, `Bezeichnung`, `Hersteller`, `OrignalTeilenummer`, `Zustand`, `Bemerkung`, `Kategorienummer`, `Preis`, `Menge`, `Versand`, `Label`, `CarID`, `versandEU`, `CPID`) VALUES ('"+i_max+"','"+tite+"','"+herst+"','"+oriT+"','"+zusta+"','"+bemer+"','"+0+"','"+pr+"','"+meng+"','"+vers+"','"+"TestLabel"+"','"+carid+"','"+versEU+"','"+y_max+"')");
			int x_max = MySQLDatenbankConnection.getInt("SELECT MAX(`ChangeID`) FROM `changehistorycarparts` WHERE 1");
			x_max++;
			MySQLDatenbankConnection.update("INSERT INTO `changehistorycarparts`(`CPID`, `UserID`, `TimeStamp`, `ChangeID`, `CarID`) VALUES ('"+y_max+"','"+User.id+"','"+timestamp+"','"+x_max+"','"+carid+"')");
			SceneCarPartsController.setCarID(carid);
			copyImageToDestination(i_max);
			root = FXMLLoader.load(getClass().getResource("CarPartsOverview.fxml"));
			stag = (Stage) abbrechen.getScene().getWindow();
			stag.setScene(new Scene(root));
		}
	}
	
	public void rightUp() throws IOException {
		image(true);
	}	
	
	public void leftDown() throws IOException {
		image(false);
	}
	
	public void goBack() throws IOException {
		show.getChildren().clear();
		SceneCarPartsController.setCarID(carid);
		root = FXMLLoader.load(getClass().getResource("CarPartsOverview.fxml"));
		stag = (Stage) abbrechen.getScene().getWindow();
		stag.setScene(new Scene(root));
	}
	
}
