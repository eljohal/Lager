package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SceneCarPartsController {

	public Stage stag;
	public Scene scene;
	public Parent root;
	
	@FXML
	public TableView<CarParts> caroverview;
	@FXML
	TableColumn<CarParts, Number> carpartsid;
	@FXML
	TableColumn<CarParts, String> artikelname;
	@FXML
	TableColumn<CarParts, String> hersteller;
	@FXML
	TableColumn<CarParts, String> oriT;
	@FXML
	TableColumn<CarParts, String> zustand;
	@FXML
	TableColumn<CarParts, Number> preis;
	@FXML
	ImageView image;
	
	@FXML
	Label caid;
	@FXML
	Label kilometerstan;
	@FXML
	Label model;
	@FXML
	Label zulassung;
	@FXML
	Label herstelle;
	@FXML
	Label farbcode;
	@FXML
	Label hubraum;
	@FXML
	Label fahrgestellnummer;
	
	static int carid;
	static String zulass;
	int imagecount;
	
	@FXML 
	Button abbrechen;
	@FXML 
	Button export;
	@FXML 
	Button label;
	@FXML 
	Button change;
	@FXML 
	Button add;
	@FXML
	Button rightpic;
	DecimalFormat filename = new DecimalFormat("000");
	static String Alphabet[] = {"","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	int carpartid = 1;
	
	
	public void image(boolean direction) throws IOException {
		Path path = createDir(carid);
		if(countImages(carpartid) > 0) {
			if(direction) {
				imagecount++;
				if(imagecount < countImages(carpartid)) {
					Path imageFile = path.resolve(""+filename.format(carpartid)+""+Alphabet[imagecount]+".jpg");
					//image.setImage(new Image(imageFile.toString()));
					centerImage(new Image(imageFile.toString()));
				}else if (imagecount >= countImages(carpartid)) {
					imagecount = countImages(carpartid) - 1;
				}
			}else {
				imagecount--;
				if(imagecount > 0) {
					Path imageFile = path.resolve(""+filename.format(carpartid)+""+Alphabet[imagecount]+".jpg");
					//image.setImage(new Image(imageFile.toString()));
					centerImage(new Image(imageFile.toString()));
				}if(imagecount <= 0) {
					imagecount = 0;
					Path imageFile = path.resolve(""+filename.format(carpartid)+""+Alphabet[imagecount]+".jpg");
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
	
	public int countImages(int cpid) {
		File folder = new File("C:\\Users\\jhaug\\Desktop\\TestBilder\\" + filename.format(carid));
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
	
	public Path createDir(int carid) throws IOException {
		Path path = Paths.get("C:\\Users\\jhaug\\Desktop\\TestBilder\\" + filename.format(carid));
		if(!Files.exists(path)) {
			Files.createDirectories(path);
			System.out.print("Hello2");
			return path;
		}
		return path;
	}
	

	
	public static ObservableList<CarParts> getCarData(){
		
		ObservableList<CarParts> carpartdata = FXCollections.observableArrayList();
		int carpartid;
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(CarPartID) FROM `carpartsdata` WHERE `CarID` = '"+carid+"'");
		for(int i = 1;i <= i_max; i++) {
			carpartid = MySQLDatenbankConnection.getInt("SELECT `CarPartID` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
			String bezeich = MySQLDatenbankConnection.getString("SELECT `Bezeichnung` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
			String herst = MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
			String zstd = MySQLDatenbankConnection.getString("SELECT `Zustand` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
			String oriT = MySQLDatenbankConnection.getString("SELECT `OrignalTeilenummer` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
			double pre = MySQLDatenbankConnection.getDouble("SELECT `Preis` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
			if(carpartid != 0) {
				carpartdata.add(new CarParts(carpartid,bezeich,zstd,herst,oriT,pre));
			}
		}				
		return carpartdata;
	}
	
	@FXML void initialize() throws IOException {
		imagecount = 1;
		carpartsid.setCellValueFactory(cellData -> cellData.getValue().getCarPartsID());
		artikelname.setCellValueFactory(cellData -> cellData.getValue().getCarPartsName());
		hersteller.setCellValueFactory(cellData -> cellData.getValue().getCarPartsHersteller());
		oriT.setCellValueFactory(cellData -> cellData.getValue().getCarPartsOriTeilNr());
		zustand.setCellValueFactory(cellData -> cellData.getValue().getCarPartsZustand());
		preis.setCellValueFactory(cellData -> cellData.getValue().getCarPartsPreis());
		caroverview.setRowFactory( tv -> {
			   TableRow<CarParts> row = new TableRow<>();
			   row.setOnMouseClicked(e -> {
			      if (e.getClickCount() == 2 && (!row.isEmpty()) ) {
			    	  try {
						root = FXMLLoader.load(getClass().getResource("CarPartsOverview.fxml"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			  		  
			  		  stag.setScene(new Scene(root));
			                             
			      }
			   });
			   return row;
			});
		caroverview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if(newSelection != null) {
				carpartid = newSelection.getCarPartsID().intValue();
				image.setImage(null);
			}else {
				carpartid = 1;
			}
			try {
				image(false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		setCarPartsList();
		image(false);
		kilometerstan.setText("  Kilometerstand:  "+Integer.toString(MySQLDatenbankConnection.getInt("SELECT `Kilometerstand` FROM `cardata` WHERE `CarID` = " + carid)));
		caid.setText("  Fz.Nr.: "+carid);
		herstelle.setText("  Hersteller: "+MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `cardata` WHERE `CarID` = " + carid));
		model.setText("  Modell: "+MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = " + carid));
		farbcode.setText("  Farbcode: "+MySQLDatenbankConnection.getString("SELECT `Farbcode` FROM `cardata` WHERE `CarID` = " + carid));
		fahrgestellnummer.setText("  Fahrgstlnummer: "+MySQLDatenbankConnection.getString("SELECT `Fahrgestellnummer` FROM `cardata` WHERE `CarID` = " + carid));
		hubraum.setText("  Hubraum: "+Integer.toString(MySQLDatenbankConnection.getInt("SELECT `Hubraum` FROM `cardata` WHERE `CarID` = " + carid)));
		LocalDate zulassun;
		zulassun = MySQLDatenbankConnection.getDate("SELECT `Erstzulassung` FROM `cardata` WHERE `CarID` = " + carid);
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.uuuu");
		zulass = zulassun.format(formatters);
		zulassung.setText("  Zulassung: "+zulass);
		
		
	}
	
	
	
	public void setCarPartsList() {
		try {
			MySQLDatenbankConnection.connect("cardaten");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		caroverview.setItems(getCarData());
		
	}
	
	public void rightUp() throws IOException {
		image(true);
	}	
	
	public void leftDown() throws IOException {
		image(false);
	}
	
	public int getCarID() {
		return carid;
	}
	
	public static void setCarID(int cid) {
		carid = cid;
		
	}
	
	public void goBack() throws IOException {
		root = FXMLLoader.load(getClass().getResource("Overview.fxml"));
		stag = (Stage) abbrechen.getScene().getWindow();
		stag.setScene(new Scene(root));
	}
	
	public void addTeil() throws IOException {
		SceneAddCarPartController.setCarID(carid);
		root = FXMLLoader.load(getClass().getResource("EditCarParts.fxml"));
		stag = (Stage) add.getScene().getWindow();
		stag.setScene(new Scene(root));
	}
}
