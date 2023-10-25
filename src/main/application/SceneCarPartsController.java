package main.application;

import java.awt.Color; //watch out, use full qualifier for colors
import javafx.scene.paint.Color; //watch out, use full qualifier for colors
import java.awt.Font;
import javafx.scene.control.TableCell;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.logging.log4j.*;
import javax.print.PrintService;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.exceptions.CsvException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SceneCarPartsController {

	
	private static final Logger logger = LogManager.getLogger(SceneCarPartsController.class);
	public Stage stag;
	public static Stage oriStag;
	public Scene scene;
	public Parent root;
	
	static VersionCheckerThread checkChanges = new VersionCheckerThread();

	static Stage checkWindow = new Stage();
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
	TableColumn<CarParts, String> versandTableColumn;
	@FXML
	TableColumn<CarParts, Number> anzahlTableColumn;
	@FXML
	ImageView image;
	@FXML
	TextFlow textshow;
	@FXML
	TextField caid;
	String herstellerCar;
	String modellCar;
	@FXML
	TextField kilometerstan;
	@FXML
	TextField model;
	@FXML
	TextField zulassung;
	@FXML
	TextField herstelle;
	@FXML
	TextField farbcode;
	@FXML
	TextField hubraum;
	@FXML
	TextField fahrgestellnummer;
	@FXML
	TextField gesamtPreisTextField;
	@FXML
	TextArea ownBemerkung;
	String bemer;
	static BigDecimal gesamtPreis;
	static BigDecimal substract = new BigDecimal("0");
	static BigDecimal mengeRemove;
	static String search;
	LocalDate zulassun;
	static int carid;
	static int caridOLD;
	static String zulass;
	int imagecount;
	@FXML
	TextField filterField;
	@FXML 
	Button abbrechen;
	@FXML 
	Button exportCSVP;
	@FXML 
	Button exportCSVM;
	@FXML
	Button carpart;
	@FXML 
	Button label;
	@FXML 
	Button change;
	@FXML 
	Button add;
	@FXML 
	Button duplicate;
	@FXML 
	Button delete;
	@FXML
	Button rightpic;
	DecimalFormat filename = new DecimalFormat("000");
	static String Alphabet[] = {"","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	int carpartid = MySQLDatenbankConnection.getInt("SELECT MAX(CarPartID) FROM `carpartsdata` WHERE `CarID` = '"+carid+"'");
	Text ausgabeedit = new Text("Bitte wähle vorher ein Fahrzeugteil aus der Liste um es zu duplizieren.");
	Image images;
	public void image(boolean direction) throws IOException {
		Path path = createDir(carid);
		Platform.runLater(() -> {
			if(countImages(carpartid) > 0) {
				if(direction) {
					imagecount++;
					if(imagecount < countImages(carpartid)) {
						Path imageFile = path.resolve(""+filename.format(carpartid)+""+Alphabet[imagecount]+".jpg");
						image.setImage(null);
						images = new Image(imageFile.toString(), 260, 235, true, false);
						image.setImage(images);
					}else if (imagecount >= countImages(carpartid)) {
						imagecount = countImages(carpartid) - 1;
					}
				}else {
					imagecount--;
					if(imagecount > 0) {
						Path imageFile = path.resolve(""+filename.format(carpartid)+""+Alphabet[imagecount]+".jpg");
						image.setImage(null);
						images = new Image(imageFile.toString(), 260, 235, true, false);
						image.setImage(images);
					}if(imagecount <= 0) {
						imagecount = 0;
						Path imageFile = path.resolve(""+filename.format(carpartid)+""+Alphabet[imagecount]+".jpg");
						image.setImage(null);
						images = new Image(imageFile.toString(), 260, 235, true, false);
						image.setImage(images);
						}
				}
			}else {
				image.setImage(null);
			}
		});
	}

	public int countImages(int cpid) {
		File folder = new File(PictureDirectory.getDir()+ "\\" + filename.format(carid)); //needs to be changed
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
		
		Path path = Paths.get(PictureDirectory.getDir()+ "\\" + filename.format(carid));
		if(!Files.exists(path)) {
			Files.createDirectories(path);
			return path;
		}
		return path;
	}
	
	public static BigDecimal setGesamtPreis(BigDecimal preis, BigDecimal menge) {
		gesamtPreis = gesamtPreis.add(preis.multiply(menge));
		return gesamtPreis;
	}
	public static BigDecimal substractGesamtPreis(BigDecimal preis, BigDecimal menge) {
		gesamtPreis = gesamtPreis.subtract(preis.multiply(menge));
		return gesamtPreis;
	}
	
	private void setPreis() {
		gesamtPreisTextField.setText("Preis (gesamt): " + gesamtPreis + " €");
	}
	
	static ObservableList<CarParts> carpartdata = FXCollections.observableArrayList();
	public ObservableList<CarParts> getCarData(){
		if(carid != caridOLD) {
			carpartdata.clear();
			gesamtPreis = new BigDecimal("0");
		}
		
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(CarPartID) FROM `carpartsdata` WHERE `CarID` = '"+carid+"'");
		if(carpartdata.size() != i_max) {
			for(int i = 1;i <= i_max; i++) {
				int carpartid = MySQLDatenbankConnection.getInt("SELECT `CPID` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
				boolean isCarPartIdInList = carpartdata.stream()
					    .anyMatch(carPart -> carPart.getCarPartsCPID().get() == carpartid);
				if(!isCarPartIdInList) {
					String bezeich = MySQLDatenbankConnection.getString("SELECT `Bezeichnung` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
					String herst = MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
					String zstd = MySQLDatenbankConnection.getString("SELECT `Zustand` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
					String oriT = MySQLDatenbankConnection.getString("SELECT `OrignalTeilenummer` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
					double pre = MySQLDatenbankConnection.getDouble("SELECT `Preis` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
					String versand = MySQLDatenbankConnection.getString("SELECT `Versand` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
					int menge = MySQLDatenbankConnection.getInt("SELECT `Menge` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
					if(carpartid != 0) {
						carpartdata.add(new CarParts(i,carpartid,bezeich,zstd,herst,oriT,pre,versand,menge));
						setGesamtPreis(BigDecimal.valueOf(pre),BigDecimal.valueOf(menge));
					}
				}
			}
		}
		return carpartdata;
	}
	public static void editInCarPartData(CarParts carpart) {
		for (int i = 0; i < carpartdata.size(); i++) {
		    CarParts carPart = carpartdata.get(i);
		    if (carPart.getCarPartsCPID().get() == carpart.getCarPartsCPID().get()) {
		        if (!carPart.equals(carpart)) {
		        	substractGesamtPreis(BigDecimal.valueOf(carPart.getCarPartsPreis().get()),BigDecimal.valueOf(carPart.getCarPartsMenge().get()));
		        	carpartdata.set(i, carpart);
		        	setGesamtPreis(BigDecimal.valueOf(carpart.getCarPartsPreis().get()),BigDecimal.valueOf(carpart.getCarPartsMenge().get()));
		        	break;
		        }
		    }
		}
		
	}
	private void copyToClipboard(String text) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(text);
        clipboard.setContent(content);
    }
	
	public void initTableViewSortingDescending() {
		carpartsid.setSortType(TableColumn.SortType.DESCENDING);
		caroverview.getSortOrder().add(carpartsid);
		caroverview.setSortPolicy(param -> true);
	}
	
	@FXML 
	void initialize() throws IOException {
		checkChanges.sceneCarParts(this);
		startThread();
		imagecount = 1;
		carpartsid.setCellValueFactory(cellData -> cellData.getValue().getCarPartsID());
		artikelname.setCellValueFactory(cellData -> cellData.getValue().getCarPartsName());
		hersteller.setCellValueFactory(cellData -> cellData.getValue().getCarPartsHersteller());
		oriT.setCellValueFactory(cellData -> cellData.getValue().getCarPartsOriTeilNr());
		zustand.setCellValueFactory(cellData -> cellData.getValue().getCarPartsZustand());
		preis.setCellValueFactory(cellData -> cellData.getValue().getCarPartsPreis());
		preis.setCellFactory(column -> {
			return new TableCell<CarParts, Number>() { 
		        @Override
		        protected void updateItem(Number item, boolean empty) {
		            super.updateItem(item, empty);

		            if (empty || item == null) {
		                setText(null);
		                setStyle(""); 
		            } else {
		                setText(item.toString());
		                if (item.doubleValue() == 0.0) {
		                    setTextFill(javafx.scene.paint.Color.RED);
		                } else {
		                    setTextFill(javafx.scene.paint.Color.BLACK);
		                }
		            }
		        }
		    };
		});
		versandTableColumn.setCellValueFactory(cellData -> cellData.getValue().getCarPartsVersand());
		versandTableColumn.setCellFactory(column -> {
			return new TableCell<CarParts, String>() { 
		        @Override
		        protected void updateItem(String item, boolean empty) {
		            super.updateItem(item, empty);

		            if (empty || item == null) {
		                setText(null);
		                setStyle(""); 
		            } else {
		                
		                if (item.toString().isEmpty()) {
		                	setText("leer");
		                	setTextFill(javafx.scene.paint.Color.RED);
		                } else {
		                	setText(item.toString());
		                	setTextFill(javafx.scene.paint.Color.BLACK);
		                }
		            }
		        }
		    };
		});
		anzahlTableColumn.setCellValueFactory(cellData -> cellData.getValue().getCarPartsMenge());
		anzahlTableColumn.setCellFactory(column -> {
			return new TableCell<CarParts, Number>() { // Verwenden Sie den gleichen generischen Typ
		        @Override
		        protected void updateItem(Number item, boolean empty) {
		            super.updateItem(item, empty);

		            if (empty || item == null) {
		                setText(null);
		                setStyle(""); // Zurücksetzen der benutzerdefinierten Stile
		            } else {
		                setText(item.toString());
		                if (item.intValue() == 0) {
		                    // Ändern Sie die Schriftfarbe auf Rot, wenn der Wert 0 ist
		                    setTextFill(javafx.scene.paint.Color.RED);
		                } else {
		                    // Andernfalls die Standard-Schriftfarbe verwenden
		                    setTextFill(javafx.scene.paint.Color.BLACK);
		                }
		            }
		        }
		    };
		});
		oriStag.setOnCloseRequest(event -> {
            event.consume(); // Verhindert das sofortige Schließen des Hauptfensters
            try {
				goBack();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
		ContextMenu contextMenu = new ContextMenu();
        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setOnAction(event -> {
            TableColumn<CarParts, ?> selectedColumn = caroverview.getFocusModel().getFocusedCell().getTableColumn();
            String selectedText = selectedColumn.getCellData(caroverview.getSelectionModel().getSelectedItem()).toString();
            copyToClipboard(selectedText);
        });
        contextMenu.getItems().add(copyItem);

        caroverview.setContextMenu(contextMenu);
		caroverview.setRowFactory( tv -> {
			   TableRow<CarParts> row = new TableRow<>();
			   row.setOnMouseClicked(e -> {
			      if (e.getClickCount() == 2 && (!row.isEmpty()) ) {
			    	  
			    	  try {
						selectedCarParts();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}}
			   });
			   return row;
			});
		caroverview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			Platform.runLater(() -> {
				if(newSelection != null) {
					carpartid = newSelection.getCarPartsID().intValue();
					image.setImage(null);
					setBemerkung(ownBemerkung);
				}else {
					carpartid = MySQLDatenbankConnection.getInt("SELECT MAX(CarPartID) FROM `carpartsdata` WHERE `CarID` = '"+carid+"'");
				}
				try {
					image(false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		});
		setCarPartsList();
		image(false);
		kilometerstan.setText("Kilometerstand:  "+Integer.toString(MySQLDatenbankConnection.getInt("SELECT `Kilometerstand` FROM `cardata` WHERE `CarID` = " + carid)));
		caid.setText("Fz.Nr.: "+carid);
		herstellerCar = MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `cardata` WHERE `CarID` = " + carid);
		herstelle.setText("Hersteller: "+herstellerCar);
		modellCar = MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = " + carid);
		model.setText("Modell: "+modellCar);
		farbcode.setText("Farbcode: "+MySQLDatenbankConnection.getString("SELECT `Farbcode` FROM `cardata` WHERE `CarID` = " + carid));
		fahrgestellnummer.setText("Fahrgestellnr.: "+MySQLDatenbankConnection.getString("SELECT `Fahrgestellnummer` FROM `cardata` WHERE `CarID` = " + carid));
		hubraum.setText("Hubraum: "+Integer.toString(MySQLDatenbankConnection.getInt("SELECT `Hubraum` FROM `cardata` WHERE `CarID` = " + carid)));
		zulassun = MySQLDatenbankConnection.getDate("SELECT `Erstzulassung` FROM `cardata` WHERE `CarID` = " + carid);
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.uuuu");
		zulass = zulassun.format(formatters);
		zulassung.setText("Zulassung: "+zulass);
		//setBemerkung(ownBemerkung);
		initTableViewSortingDescending();
		setPreis();
		filter();
	}
	
	private void setBemerkung(TextArea out) {
		bemer = MySQLDatenbankConnection.getString("SELECT `versandEU` FROM `carpartsdata` WHERE `CarPartID` = '" + carpartid + "' AND `CarID` = "+carid+"");
		if(bemer == null) {
			bemer = "";
		}
		out.setText(bemer);
	}
	
	public void versionErrorChangesCount() {
		checkChanges.pauseCarPartsChangesThread();
		if(checkChanges.carPartsChanges.isShutdown()) {
			//carpartdata.clear();
			Platform.runLater(() -> {
				getCarData();
				caroverview.setItems(carpartdata);
				carpartsid.setSortType(TableColumn.SortType.DESCENDING);
				caroverview.getSortOrder().add(carpartsid);
				caroverview.setSortPolicy(param -> true);
				filter();
				setPreis();
				startThread();
			});
		}	
	}
	
	public static void getStage(Stage stage) {
		oriStag = stage;
	}
	
	public void setCarPartsList() {
		caroverview.setItems(getCarData());
		
	}
	DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
	public void exportCSV() {
		String csvFilePath = "C:\\Program Files (x86)\\MyJavaApp\\CSV Export\\"+filename.format(carid)+"-"+herstellerCar+"-"+modellCar+"_Produktmerkmale.csv";
		try (FileWriter fileWriter = new FileWriter(csvFilePath);
	             CSVWriter csvWriter = (CSVWriter) new CSVWriterBuilder(fileWriter)
	                .withSeparator(';')
	                .withEscapeChar('\\')
	                .withQuoteChar('"')
	                .withLineEnd("\n")
	                .build()) {


	            // Datenzeilen erstellen und zur CSV-Datei hinzufügen
	            String[] header = {"ID", "Bezeichnung","Hersteller","Zustand","Bemerkung","Ori.Teilenr.","OE-Nr.","Preis","Versand (D)","Versand (EU)","passend fuer","Auto-Hersteller","spez. ID"};
	            // CSV-Header schreiben (optional)
	            csvWriter.writeNext(header);
	            int carpartid;
	    		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(CarPartID) FROM `carpartsdata` WHERE `CarID` = '"+carid+"'");
	    		for(int i = 1;i <= i_max; i++) {
	    			carpartid = MySQLDatenbankConnection.getInt("SELECT `CarPartID` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String bezeich = MySQLDatenbankConnection.getString("SELECT `Bezeichnung` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String herst = MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String zstd = MySQLDatenbankConnection.getString("SELECT `Zustand` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String oriT = MySQLDatenbankConnection.getString("SELECT `OrignalTeilenummer` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String bemerk = MySQLDatenbankConnection.getString("SELECT `Bemerkung` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String oe = MySQLDatenbankConnection.getString("SELECT `OENumber` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			double pre = MySQLDatenbankConnection.getDouble("SELECT `Preis` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String preis = decimalFormat.format(pre);
	    			String pass = MySQLDatenbankConnection.getString("SELECT `Passend` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String vD = MySQLDatenbankConnection.getString("SELECT `Versand` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String vE = MySQLDatenbankConnection.getString("SELECT `versandEU` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			if(carpartid != 0) {
	    				String[] row = {""+carpartid+"",bezeich,herst,zstd,bemerk,oriT,oe,preis,vD,vE,pass,herstellerCar,""+filename.format(carid)+"-"+filename.format(carpartid)+""};
	    				 csvWriter.writeNext(row, false);
	    			}
	    		}

	            System.out.println("CSV-Datei wurde erfolgreich erstellt: " + csvFilePath);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	@FXML
	private void handleDragOver(DragEvent event) {
		if(event.getDragboard().hasFiles()) {
			event.acceptTransferModes(TransferMode.ANY);
		}
	}
	
	@FXML
	private void handleDrop(DragEvent event){
		List<File> files = event.getDragboard().getFiles();
		for(int i = 0; i < files.size(); i++) {
			try (CSVReader reader = new CSVReaderBuilder(new FileReader(files.get(i)))
				.withCSVParser(new CSVParserBuilder()
		                .withSeparator(';')
		                .withQuoteChar('"')
		                .build())
		        .build()) {
		            String[] nextRecord;
		            while ((nextRecord = reader.readNext()) != null) {
		                // Hier gehen wir davon aus, dass die erste Spalte ein Integer ist
		                // und die zweite Spalte ein String, der durch "-" getrennt ist
		            	if(nextRecord[0].contains("-")) {
			                String[] stringParts = nextRecord[0].split("-");
				            int firstPart = Integer.parseInt(stringParts[0]);
				            int secondPart = Integer.parseInt(stringParts[1]);
				            int intValue = Integer.parseInt(nextRecord[1]);
				            if(firstPart == carid) {
				               	int cpid = MySQLDatenbankConnection.getInt("SELECT `CPID` FROM `carpartsdata` WHERE `CarPartID` = "+secondPart+" AND `CarID` = "+firstPart+"");
				                MySQLDatenbankConnection.update("UPDATE `carpartsdata` SET `ProduktIDAfterbuy`= '"+intValue+"' WHERE `CPID` = '"+cpid+"'");
				           }
			            }
		            }
			}catch (IOException | CsvException e) {
				logger.error("CSV Import zur Verknüpfung ProduktID und spezifische-ID (CarID-CarPartID), handleDrop() over Button exportCSVM", e);
	        }
		}
	}
	
	public void filter() {

		FilteredList<CarParts> filteredData = new FilteredList<>(carpartdata, b -> true);
		filterField.setText(search);
		filteredList(filterField, filteredData, search);
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredList(filterField, filteredData, newValue);
			search = newValue;
		});
		SortedList<CarParts> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(caroverview.comparatorProperty());
		caroverview.setItems(sortedData);
	}
	
	public void filteredList(TextField textfield, FilteredList<CarParts> filteredList, String search) {
		filteredList.setPredicate(carPart -> {
			if (search == null || textfield.getText().isEmpty()) {
				return true;
			}
			
			if(carPart.getCarPartsID().toString().toLowerCase().indexOf(search.toLowerCase()) != -1) {
				return true;
			}else if (carPart.getCarPartsName().toString().toLowerCase().indexOf(search.toLowerCase()) != -1) {
				return true;
			}else {
				return false;
			}
		});
	}
	
	String[] namesEbayMerkmale = {"Hersteller", "Herstellernummer", "OE/OEM Referenznummer(n)", "Produktart", "Einbauposition", "Farbe", "Farbcode", "Spannung", "Stromstärke"};
	
	public void exportProduktMerkmaleCSV() {
		String csvFilePath = "C:\\Program Files (x86)\\MyJavaApp\\CSV Export\\"+filename.format(carid)+"-"+herstellerCar+"-"+modellCar+"_eBayProduktMerkmale.csv";
		try (FileWriter fileWriter = new FileWriter(csvFilePath);
	             CSVWriter csvWriter = (CSVWriter) new CSVWriterBuilder(fileWriter)
	                .withSeparator(';')
	                .withEscapeChar('\\')
	                .withQuoteChar('"')
	                .withLineEnd("\n")
	                .build()) {


	            // Datenzeilen erstellen und zur CSV-Datei hinzufügen
	            String[] header = {"ProduktID","CategorieID","Merkmale","Menge","0"};
	            // CSV-Header schreiben (optional)
	            csvWriter.writeNext(header);
	            int carpartid;
	            String spannung = "";
	            String strom = "";
	    		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(CarPartID) FROM `carpartsdata` WHERE `CarID` = '"+carid+"'");
	    		for(int i = 1;i <= i_max; i++) {
	    			carpartid = MySQLDatenbankConnection.getInt("SELECT `CarPartID` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			int prodID = MySQLDatenbankConnection.getInt("SELECT `ProduktIDAfterbuy` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			int categorie = MySQLDatenbankConnection.getInt("SELECT `Kategorienummer` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			int menge = MySQLDatenbankConnection.getInt("SELECT `Menge` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String eBProd = MySQLDatenbankConnection.getString("SELECT `eBayProduktart` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String herst = MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String oe = MySQLDatenbankConnection.getString("SELECT `OENumber` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String oriT = MySQLDatenbankConnection.getString("SELECT `OrignalTeilenummer` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String eBPos = MySQLDatenbankConnection.getString("SELECT `eBayPosition` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String eBFarb = MySQLDatenbankConnection.getString("SELECT `eBayFarbe` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			String eBFarbcode = MySQLDatenbankConnection.getString("SELECT `eBayFarbecode` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			double eBSPGValue = MySQLDatenbankConnection.getDouble("SELECT `Spannung` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			double eBSTRValue = MySQLDatenbankConnection.getDouble("SELECT `Stromstaerke` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");	
	    			if(eBSPGValue != 0.0) {
	    				spannung = ""+formatDouble(eBSPGValue)+" "+MySQLDatenbankConnection.getString("SELECT `SpannungEinheit` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");   					
	    			}
	    			if(eBSTRValue != 0.0) {
	    				strom = ""+formatDouble(eBSTRValue)+" "+MySQLDatenbankConnection.getString("SELECT `StromstaerkeEinheit` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			}
	    			
	    			String merkmale = setProdMerkCSV(namesEbayMerkmale[0],herst)+setHerstellerOEMerkmaleCSV(namesEbayMerkmale[1],oriT)+setHerstellerOEMerkmaleCSV(namesEbayMerkmale[2],oe)+setProdMerkCSV(namesEbayMerkmale[3],eBProd)+setProdMerkCSV(namesEbayMerkmale[4],eBPos)+setProdMerkCSV(namesEbayMerkmale[5],eBFarb)+setProdMerkCSV(namesEbayMerkmale[6],eBFarbcode)+setProdMerkCSV(namesEbayMerkmale[7],spannung)+setProdMerkCSV(namesEbayMerkmale[8],strom);
	    			
	    			
	    			
	    			if(carpartid != 0) {	    			
	    				String[] row = {""+prodID,""+categorie,"<ItemSpecifics>"+merkmale+"</ItemSpecifics>",""+menge,"0"};
	    				csvWriter.writeNext(row, false);
	    			}
	    		}
	            System.out.println("CSV-Datei wurde erfolgreich erstellt: " + csvFilePath);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		csvFilePath = "C:\\Program Files (x86)\\MyJavaApp\\CSV Export\\"+filename.format(carid)+"-"+herstellerCar+"-"+modellCar+"_Bilder.csv";
		try (FileWriter fileWriter = new FileWriter(csvFilePath);
	             CSVWriter csvWriter = (CSVWriter) new CSVWriterBuilder(fileWriter)
	                .withSeparator(';')
	                .withEscapeChar('\\')
	                .withQuoteChar('"')
	                .withLineEnd("\n")
	                .build()) {


	            // Datenzeilen erstellen und zur CSV-Datei hinzufügen
	            String[] header = {"Bildtyp","URL","ProduktID","Bildnummer","0"};
	            // CSV-Header schreiben (optional)
	            csvWriter.writeNext(header);
	            int carpartid;
	            String URL_Stamm = "https://www.autohof-haselbach.de/";
	    		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(CarPartID) FROM `carpartsdata` WHERE `CarID` = '"+carid+"'");
	    		for(int i = 1;i <= i_max; i++) {
	    			carpartid = MySQLDatenbankConnection.getInt("SELECT `CPID` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			int carpartidReal = MySQLDatenbankConnection.getInt("SELECT `CarPartID` FROM `carpartsdata` WHERE `CarPartID` = '" + i + "' AND `CarID` = "+carid+"");
	    			int prodID = MySQLDatenbankConnection.getInt("SELECT `ProduktIDAfterbuy` FROM `carpartsdata` WHERE `CPID` = '"+carpartid+"'");
	    			if(carpartidReal != 0) {
		    			for(int x = 0; x < countImages(i); x++) {
		    				int z = x + 1;
		    				String URL = URL_Stamm+""+filename.format(carid)+"/"+filename.format(i)+Alphabet[x]+".jpg";
		    				//TODO check if File is auf Server
		    				String[] row = {"0",URL,""+prodID,""+z,"0"};
		    				csvWriter.writeNext(row, false);
		    			}			
	    			}
	    		}
	            System.out.println("CSV-Datei wurde erfolgreich erstellt: " + csvFilePath);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
	}
	
	public static Number formatDouble(double number) {
	    if (number == (int) number) {
	        return (int) number;
	    } else {
	        return number;
	    }
	}
	
	public String setProdMerkCSV(String name, String value) {
		if(!value.isBlank())
		{
			return "<NameValueList><Name><![CDATA["+name+"]]></Name><Value><![CDATA["+value+"]]></Value></NameValueList>";
		}
		return "";
	}
	
	public String setHerstellerOEMerkmaleCSV(String name, String value) {
		String output = "";
		if(!value.isBlank())
		{
			output = "<NameValueList><Name><![CDATA["+name+"]]></Name>";
			if(value.contains("/")) {
				String[] stringParts = value.split("/");
				for(int i = 0; i < stringParts.length; i++) {
					output = output + "<Value><![CDATA["+stringParts[i]+"]]></Value>";
				}
			}else {
				output = output + "<Value><![CDATA["+value+"]]></Value>";
			}
			output = output + "</NameValueList>";
			return output;
		}
		return "";
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
		checkChanges.pauseCarPartsChangesThread();
		if(checkChanges.carPartsChanges.isShutdown()) {
			image.setImage(null);
			search = "";
			closeCheckWindow();
			caridOLD = carid;
			root = FXMLLoader.load(getClass().getClassLoader().getResource("Overview.fxml"));
			stag = (Stage) abbrechen.getScene().getWindow();
			stag.setScene(new Scene(root));
		}
	}
	
	public void selectedCarParts() throws IOException{
		SceneSelectedCarPartController.setCarID(carid);
		CarParts ident = caroverview.getSelectionModel().getSelectedItem();
    	SceneSelectedCarPartController.initCarPart(ident.getCarPartsID().getValue().intValue());
    	FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("SelectedCarPart.fxml"));		
		Parent root = loader.load();
		SceneSelectedCarPartController sceneSelectedCarPartController = loader.getController();
		sceneSelectedCarPartController.sceneGeneralSettings(this);
		checkWindow.setScene(new Scene(root));
	    checkWindow.show();
	}
	
	public static void closeCheckWindow() throws IOException {
		checkWindow.hide();
	}
	
	public void editCarPart() throws IOException {	
		if(caroverview.getSelectionModel().getSelectedItem() != null ) {
			checkChanges.pauseCarPartsChangesThread();
			if(checkChanges.carPartsChanges.isShutdown()) {
				image.setImage(null);
				caridOLD = carid;
				SceneRealEditCarPartController.setCarID(carid);
				stag = (Stage) abbrechen.getScene().getWindow();
				CarParts ident = caroverview.getSelectionModel().getSelectedItem();
				SceneRealEditCarPartController.initCarPart(ident.getCarPartsID().getValue().intValue());
				FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("RealEditCarParts.fxml"));
				root = loader.load();
				SceneRealEditCarPartController test = loader.getController();
				scene = new Scene(root);			
				test.getScene(scene);
				test.getStage(stag);
				stag.setScene(scene);
			}
		}
	}
	
	public void duplicateCarPart() {
		textshow.getChildren().clear();
		checkChanges.pauseCarPartsChangesThread();
		if(checkChanges.carPartsChanges.isShutdown()) {
			if(caroverview.getSelectionModel().getSelectedItem() != null ) {
				int cpid = MySQLDatenbankConnection.getInt("SELECT `CPID` FROM `carpartsdata` WHERE `CarPartID` = "+caroverview.getSelectionModel().getSelectedItem().getCarPartsID().get()+" AND `CarID` = "+carid+"");
				int i_max = MySQLDatenbankConnection.getInt("SELECT MIN(CarPartID) FROM `carpartsdata` WHERE CarID = "+carid+"");
				if(i_max != 1 || i_max == 0) {
					i_max = 1;
				}else {
					i_max = MySQLDatenbankConnection.getInt("SELECT MIN(t1.CarPartID) + 1 AS NextAvailableCarPartID FROM `carpartsdata` t1 LEFT JOIN `carpartsdata` t2 ON t1.CarPartID + 1 = t2.CarPartID AND t1.CarID = t2.CarID WHERE t2.CarPartID IS NULL AND t1.CarID = "+carid+"");
				}
				BigDecimal version = MySQLDatenbankConnection.getFloat("SELECT `Version` FROM `carpartsdata` WHERE `CPID` = "+cpid+" ");
				int y_max = MySQLDatenbankConnection.getInt("SELECT MIN(d1.CPID)+1 FROM `carpartsdata` d1 LEFT JOIN `carpartsdata` d2 ON d1.CPID + 1 = d2.CPID WHERE d2.CPID is NULL");
				MySQLDatenbankConnection.update("INSERT INTO `carpartsdata`(`CarPartID`, `Bezeichnung`, `Hersteller`, `OrignalTeilenummer`, `Zustand`, `Bemerkung`, `Kategorienummer`, `Preis`, `Menge`, `Versand`, `eBayProduktart`, `CarID`, `versandEU`, `CPID`, `Teil`, `OENumber`, `eBayPosition`, `eBayFarbe`, `eBayFarbecode`,  `Spannung`, `SpannungEinheit`, `Stromstaerke`, `StromstaerkeEinheit`, `Passend`, `Version`) SELECT '"+i_max+"', CONCAT('DUPLIKAT' , ' ',`Bezeichnung`), `Hersteller`, `OrignalTeilenummer`, `Zustand`, `Bemerkung`, `Kategorienummer`, `Preis`, `Menge`, `Versand`, `eBayProduktart`, `CarID`, `versandEU`, '"+y_max+"', `Teil`, `OENumber`, `eBayPosition`, `eBayFarbe`, `eBayFarbecode`,  `Spannung`, `SpannungEinheit`, `Stromstaerke`, `StromstaerkeEinheit`, `Passend`, '0.00001' FROM `carpartsdata` WHERE `CPID` = "+cpid+"");
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				int x_max = MySQLDatenbankConnection.getInt("SELECT MAX(`ChangeID`) FROM `changehistorycarparts` WHERE 1");
				x_max++;
	
				String changes = "neu";
				MySQLDatenbankConnection.update("INSERT INTO `changehistorycarparts`(`CPID`, `UserID`, `TimeStamp`, `ChangeID`, `CarID`, `Changed`, `CarPartID`, `Bezeichnung`, `Hersteller`, `OrignalTeilenummer`, `Zustand`, `Bemerkung`, `Kategorienummer`, `Preis`, `Menge`, `Versand`, `eBayProduktart`, `versandEU`, `Teil`, `OENumber`, `eBayPosition`, `eBayFarbe`, `eBayFarbecode`, `Spannung`, `SpannungEinheit`, `Stromstaerke`, `StromstaerkeEinheit`, `Passend`, `Version`) SELECT '"+y_max+"', '"+User.id+"', '"+timestamp+"', '"+x_max+"', `CarID`, '"+changes+"', '"+i_max+"', CONCAT('DUPLIKAT' , ' ',`Bezeichnung`), `Hersteller`, `OrignalTeilenummer`, `Zustand`, `Bemerkung`, `Kategorienummer`, `Preis`, `Menge`, `Versand`, `eBayProduktart`, `versandEU`, `Teil`, `OENumber`, `eBayPosition`, `eBayFarbe`, `eBayFarbecode`, `Spannung`, `SpannungEinheit`, `Stromstaerke`, `StromstaerkeEinheit`, `Passend`, '0.00001' FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString()+"");
				String bez = MySQLDatenbankConnection.getString("SELECT `Bezeichnung` FROM `carpartsdata` WHERE `CPID` = "+y_max+"");;
				String zst = MySQLDatenbankConnection.getString("SELECT `Zustand` FROM `carpartsdata` WHERE `CPID` = "+y_max+"");;
				String herts = MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `carpartsdata` WHERE `CPID` = "+y_max+"");;
				String oriTe = MySQLDatenbankConnection.getString("SELECT `OrignalTeilenummer` FROM `carpartsdata` WHERE `CPID` = "+y_max+"");
				double p = MySQLDatenbankConnection.getDouble("SELECT `Preis` FROM `carpartsdata` WHERE `CPID` ="+y_max+"");
				String vers = MySQLDatenbankConnection.getString("SELECT `Versand` FROM `carpartsdata` WHERE `CPID` ="+y_max+"");
				int meng = MySQLDatenbankConnection.getInt("SELECT `Menge` FROM `carpartsdata` WHERE `CPID` ="+y_max+"");
				setGesamtPreis(BigDecimal.valueOf(p),BigDecimal.valueOf(meng));
				setPreis();
				carpartdata.add(new CarParts(i_max, y_max,bez,zst,herts,oriTe,p,vers,meng));
				startThread();
			}else {
				textshow.getChildren().clear();
				textshow.getChildren().addAll(ausgabeedit);
			}
		}
	}
	
	public void deleteCarPart() throws IOException {	
		textshow.getChildren().clear();
			if(caroverview.getSelectionModel().getSelectedItem() != null ) {
				checkChanges.pauseCarPartsChangesThread();
				if(checkChanges.carPartsChanges.isShutdown()) {
					SceneDeleteCarPartController.setIDValue(caroverview.getSelectionModel().getSelectedItem().getCarPartsID().get(), carid, caroverview.getSelectionModel().getSelectedItem().getCarPartsName().get(), carpartdata.indexOf(caroverview.getSelectionModel().getSelectedItem()));
					FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("DeleteCarPart.fxml"));
					Parent root = loader.load();
					SceneDeleteCarPartController sceneDeleteCarPartController = loader.getController();
					sceneDeleteCarPartController.sceneGeneralSettings(this);
					checkWindow.setScene(new Scene(root));
					checkWindow.show();
				}
			}else {
				textshow.getChildren().clear();
				textshow.getChildren().addAll(ausgabeedit);
			}
	}
	
	public void startThread() {
		checkChanges.setInitCount("SELECT COUNT(*) FROM `carpartsdata` WHERE CarID = "+carid+"");
		checkChanges.setCarPartController("SceneCarPartsController");
		checkChanges.startCarPartsThread();
	}
	
	public void removeCarPart(int index) throws IOException {
		if(checkChanges.carPartsChanges.isShutdown()) {
			substractGesamtPreis(substract,mengeRemove);
			substract = BigDecimal.valueOf(0);
			setPreis();
			int id = carpartdata.get(index).getCarPartsID().get();
			File dir = new File(PictureDirectory.getDir()+ "\\" + filename.format(carid));
			File[] files = dir.listFiles();
	        if (files != null) {
	            for (File file : files) {
	                if (file.isFile() && file.getName().contains(filename.format(id))) {
	                    // Check if the file name contains the search string
	                    file.delete();
	                }
	            }
	        }
			carpartdata.remove(index);
			startThread();
		}
	}
	
	public void addTeil(){
		checkChanges.pauseCarPartsChangesThread();
		if(checkChanges.carPartsChanges.isShutdown()) {
			Platform.runLater(() -> {
				try {
					image.setImage(null);
					caridOLD = carid;
					SceneAddCarPartController.setCarID(carid);
					stag = (Stage) add.getScene().getWindow();
					FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("EditCarParts.fxml"));		
					root = loader.load();
					SceneAddCarPartController test = loader.getController();
					scene = new Scene(root);
					test.getScene(scene);
					test.getStage(stag);
					stag.setScene(scene);
				}catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("Wechsel zu AddCarPart von SceneCarPartsController, addTeil()", e);
				}
			});
		}
	}
	public void print() throws IOException {
		if(caroverview.getSelectionModel().getSelectedItem() != null ) {
			NewCoder128 test = new NewCoder128();
			CarParts ident = caroverview.getSelectionModel().getSelectedItem();
			int id = ident.getCarPartsID().intValue();
			String teil = MySQLDatenbankConnection.getString("SELECT `Teil` FROM `carpartsdata` WHERE `CarPartID` = "+id+" AND `CarID` = "+carid+"");
			printLabel(test.StringToBarcode(filename.format(carid)+"-"+filename.format(id)), filename.format(carid)+"-"+filename.format(id), teil+" "+ herstellerCar + " " +modellCar, "", "", "", "", "", "", "", "", "");
		}
	}
	public static final String PRINTERNAME = "DYMO LabelWriter 450";
    /**
     * true if you want a menu to select the printer
     */
    public final boolean PRINTMENU = false;
    public String printThis[] = new String[11];
    static PrinterJob printerJob = PrinterJob.getPrinterJob();
    static PageFormat pageFormat = printerJob.defaultPage();
    static Paper paper = new Paper();

    public void printLabel(final String text0, final String text1, final String text2, final String text3, final String text4, final String text5, final String text6, final String text7,
            final String text8, final String text9, final String text10, final String text11) {


        final double widthPaper = (1.125 * 72);
        final double heightPaper = (3.5 * 72);

        paper.setSize(widthPaper, heightPaper);
        paper.setImageableArea(0, 0, widthPaper, heightPaper);

        pageFormat.setPaper(paper);

        pageFormat.setOrientation(PageFormat.LANDSCAPE);


        PrintService[] printService = PrinterJob.lookupPrintServices();

        for (int i = 0; i < printService.length; i++) {

            if (printService[i].getName().contains(PRINTERNAME)) {
                try {
                    printerJob.setPrintService(printService[i]);
                    printerJob.setPrintable(new Printable() {
                        @Override
                        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
                                throws PrinterException {
                            if (pageIndex < getPageNumbers()) {
                                Graphics2D g = (Graphics2D) graphics;
                                
                            	g.setColor(java.awt.Color.BLACK);
                            	g.translate(20, 10);

                                String value = "";

                                int x = 5;
                                int y = 5;

                                // label under barcode
                                g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 25));
                                value = text1;
                                g.drawString("" + value, x + 110, y + 30);

                                // Contract Number
                                g.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), 10));
                                value = text2;
                                g.drawString("" + value, x, y);

                                // Code
                                g.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), 7));
                                value = text3;
                                g.drawString("" + value, x, y + 27);

                                // Customer ID
                                g.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), 7));
                                value = text4;
                                g.drawString("" + value, x, y + 34);


                                // Item Code
                                g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 7));
                                value = text5;
                                g.drawString("" + value, x + 35, y + 20);

                                // Code Descr
                                g.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), 7));
                                value = text6;
                                g.drawString("" + value, x + 35, y + 27);

                                // Customer Name
                                g.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), 7));
                                value = text7;
                                g.drawString("" + value, x + 35, y + 34);



                                // AD Code
                                g.setFont(new Font(g.getFont().getFontName(), Font.BOLD, 7));
                                value = text8;
                                g.drawString("" + value, x + 155, y + 20);

                                // Edition
                                g.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), 7));
                                value = text9;
                                g.drawString("" + value, x + 155, y + 27);




                                // date
                                g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 7));
//                                value = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).format(new Date());
                                value = text10;
                                g.drawString(value, x, y + 40);

                                // Customer Account Number
                                g.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), 7));
                                value = text11;
                                g.drawString("" + value, x + 35, y + 42);


                                InputStream ttf = null;
								try {
									ttf = new BufferedInputStream(Files.newInputStream(Paths.get("C:\\Program Files (x86)\\MyJavaApp\\InstallJavaApp\\code128.ttf")));
									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
                                Font font = null;
                                try {
                                    font = Font.createFont(Font.TRUETYPE_FONT, ttf);
                                   
                                } catch (FontFormatException | IOException ex) {
                                	ex.printStackTrace();
                                }

                                g.setFont(font);

                                g.setFont(new Font(g.getFont().getFontName(), g.getFont().getStyle(), 24));
                                value = text0;
                                g.drawString(value, x, y + 40);
                                                                
                                return PAGE_EXISTS;
                            } else {
                                return NO_SUCH_PAGE;
                            }
                        }
                    }, pageFormat); // The 2nd param is necessary for printing into a label width a right landscape format.
                    printerJob.print();
                } catch (PrinterException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public int getPageNumbers() {
        return 1;
    }
}
