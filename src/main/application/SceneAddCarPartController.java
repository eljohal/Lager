package main.application; 

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class SceneAddCarPartController {
	public Stage stag;
	static Stage checkWindow = new Stage();
	public static Stage oriStag;
	public Scene scene;
	public static Scene oriScene;
	public Parent root;
	private static final Logger logger = LogManager.getLogger(SceneAddCarPartController.class);
	
	@FXML
	TextField caid;
	DecimalFormat caridFormat = new DecimalFormat("000");
	@FXML
	TextField modell;
	static int carid;
	@FXML
	private TextField teil;
	private AutoCompletionBinding<String> autoComplete;
	private Set<String> possibleSuggestions = new HashSet<>();
	@FXML
	private TextField eCat;
	@FXML
	private TextField eBayProdukt;
	List<String> suggestionListProd = new ArrayList<>();
	List<Integer> setSuggestionsListProd = new ArrayList<>();
	List<String> setSuggestionsFindProd = new ArrayList<>();
	ListView<String> suggestionListViewProd = new ListView<>();
	Popup popupProd = new javafx.stage.Popup();
	@FXML
	private TextField passtAuch;
	@FXML
	private TextField eBayFarbe;
	public AutoCompletionBinding<String> autoCompleteFarbe;
	private Set<String> possibleSuggestionsFarbe = new HashSet<>();
	@FXML
	private TextField eBayFarbecode;
	String eBFarbC;
	@FXML
	private TextField eBayPosition;
	List<String> suggestionList = new ArrayList<>();
	List<Integer> setSuggestionsList = new ArrayList<>();
	List<String> setSuggestionsFind = new ArrayList<>();
	ListView<String> suggestionListView = new ListView<>();
	Popup popup = new javafx.stage.Popup();
	@FXML
	private TextField hersteller;
	public AutoCompletionBinding<String> autoCompleteHersteller;
	private Set<String> possibleSuggestionsHersteller = new HashSet<>();
	@FXML
	TextField oriTeilNr;
	@FXML
	TextArea bemerkung;
	@FXML
	private TextField zust;
	private AutoCompletionBinding<String> autoCompleteZustand;
	private Set<String> possibleSuggestionsZustand = new HashSet<>();
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
	private final int CHARACTER_LIMIT = 80;
	@FXML
	Label characterCountLabel;
	@FXML
	TextField oriOENummer;
	@FXML
	TextField eBaySpannung;
	@FXML
	private ComboBox<String> eBaySpannungEinheit;
	private String[] spg = {"","mV", "V", "kV"};
	@FXML
	TextField eBayStrom;
	@FXML
	private ComboBox<String> ebayStromEinheit;
	private String[] str = {"","mA", "A", "kA"};
	@FXML 
	Button abbrechen;
	@FXML 
	Button add;
	@FXML 
	Button reload;
	@FXML
	Button getEbayCatButton;
	@FXML
	TextFlow show;
	@FXML
	ImageView image;
	DecimalFormat filename = new DecimalFormat("000");
	static String Alphabet[] = {"","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
	String changes;
	@FXML
	Button rightpic;
	private String modelle;
	String teile;
	String tite;
	String herst;
	String oriT;
	String oEN;
	String zusta;
	String bemer;
	String eBProd;
	String eBFarb;
	String eBPos;
	double eBSpg;
	double eBStr;
	String spgE;
	String strE;
	int kategorie;
	double pr;
	int meng;
	String vers;
	String passA;
	String versEU;
	int imagecount;
	
	Text nothingSelected = new Text("Bitte Titel über Reload Button erstellen");
	Text missingteil = new Text("Bitte vorher ein Teil auswählen");
	Text loadingData = new Text("Bitte warten, eBay Kategorieliste wird noch geladen");
	Text nothingSelectedKategorie = new Text("Bitte EBay Kategorie auswählen");
	Text nothingSelectedMenge = new Text("Bitte Menge angeben");
	Text nothingSelectedPassendFuer = new Text("Bitte Feld \"passend für\" ausfüllen");
	
	private void pasteImageFromClipboard() {
		Clipboard clipboard = Clipboard.getSystemClipboard();
		if (clipboard.hasUrl()) {
			Path path;
			try {
				path = createTempDir();
			
		        List<File> urlList = clipboard.getFiles();
		        int x = getFileID(path);
		        for(int i = 0; i < urlList.size(); i++) {
		        	if(urlList.get(i).getAbsolutePath().toLowerCase().contains(".jpg") || urlList.get(i).getAbsolutePath().toLowerCase().contains(".png") || urlList.get(i).getAbsolutePath().toLowerCase().contains(".jpeg")) {
						Path copied = Paths.get(urlList.get(i).getAbsolutePath());
						Path newFile = Files.createFile(path.resolve(""+(i+x)+".jpg"));
						Files.copy(copied, newFile, StandardCopyOption.REPLACE_EXISTING);
		        	}
				}
				image(false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.warn("Paste Image Warning SceneAddCarPartController Function pasteImageFromClipboard", e);
			}
	    }
	}
	
	
	public void initPopupSuggestions(Popup popup, ListView<String> suggestionsView, List<String> suggestionMarker, TextField textField, TextField source, Set<String> sourceHashset, String sourceID, String sourceDatabase, String connectionDatabase, String id, String database, String suggestions, List<Integer> idChecker, List<String> allPossibilitiesListString) {
		
		textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
		    if(newValue) {
		    	double x = textField.localToScreen(textField.getBoundsInLocal()).getMaxX();
		        double y = textField.localToScreen(textField.getBoundsInLocal()).getMaxY();
		        initListPossibility(id, database, suggestions, idChecker, suggestionMarker, allPossibilitiesListString, popup, suggestionsView);
		        double offsetX = 10; // Horizontaler Offset
		        double offsetY = 10; // Vertikaler Offset
		        popup.show(textField, x - offsetX, y - offsetY);
		    }else {
		    	popup.hide();
		    }
		});
		source.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if (!newValue) {
		            // Das Textfeld hat den Fokus verloren
		            if(!source.getText().equals("")) {
		            	if(sourceHashset.contains(source.getText())) {
		            		int teileID = MySQLDatenbankConnection.getInt("SELECT `"+sourceID+"` FROM `"+sourceDatabase+"` WHERE `Part` = '"+source.getText()+"'");
		            		allPossibilitiesListString.clear();
		            		suggestionMarker.clear();
		            		suggestionsView.getItems().clear();
		                	popup.getContent().remove(suggestionsView);
		                	initListPossibility(id, database, suggestions, initCheckListsPopupSuggestions(id, connectionDatabase, sourceID, teileID), suggestionMarker, allPossibilitiesListString, popup, suggestionsView);
		            	}
		            }else {
		            	initListPossibility(id, database, suggestions, idChecker, suggestionMarker, allPossibilitiesListString, popup, suggestionsView);
		            }
		        }
		    }
		});
		suggestionsView.setCellFactory(lv -> {
            ListCell<String> cell = new ListCell<>() {
            	
		        @Override
		        protected void updateItem(String item, boolean empty) {
		            super.updateItem(item, empty);
		            if (item != null && stringEqualsAny(item, suggestionMarker)) {
		                // Hintergrundfarbe für Elemente die in suggestionsMarker
		                setStyle("-fx-background-color: lightblue;");
		            } else {
		                // Standard-Hintergrundfarbe für andere Elemente
		                setStyle("");
		            }
	
		            setText(item);
		        }
            };
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty() &&  event.getClickCount() == 2) {
                    String selectedItem = cell.getItem();
                    textField.setText(selectedItem);
                    popup.hide();
                }
            });
            return cell;
		});
		textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
		    @Override
		    public void handle(KeyEvent ke) {
		        // aktuellen Text aus dem eBayPosition-Textfeld
		    	if (ke.getCode() == KeyCode.UP || ke.getCode() == KeyCode.DOWN || ke.getCode() == KeyCode.ENTER) {
		            ke.consume();
		        }else if(ke.getCode() == KeyCode.TAB){
		        	ke.consume();
		        }else {
		        	String input = textField.getText().toLowerCase();
			        // Leeren bestehende Liste
			        suggestionsView.getItems().clear();
			        //Vorschläge hinzufügen, die zur Eingabe passen
			        for (String suggestion : allPossibilitiesListString) {
			        	if (suggestion.toLowerCase().contains(input)) {
			        		suggestionsView.getItems().add(suggestion);
			        	}
			        }
			        // Überprüfen, ob Vorschläge vorhanden sind und das Popup anzeigen oder ausblenden
			        if (!suggestionsView.getItems().isEmpty()) {
			        	double x = textField.localToScreen(textField.getBoundsInLocal()).getMaxX();
			        	double y = textField.localToScreen(textField.getBoundsInLocal()).getMaxY();
			        	double offsetX = 10; // Horizontaler Offset
			        	double offsetY = 10; // Vertikaler Offset
			        	popup.show(textField, x - offsetX, y - offsetY);
			        }else {
			        	popup.hide();
			        }	        	
		        }
		    }
		});
		suggestionsView.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				switch (ke.getCode()) {
				case TAB:
					popup.hide();
				case ENTER:
					if (!suggestionsView.getSelectionModel().isEmpty()) {
		                String selectedValue = suggestionsView.getSelectionModel().getSelectedItem();
		                textField.setText(selectedValue);
					}
					popup.hide();
					break;
				default:
					break;
				}
			}
		});
	}
	public void initListPossibility(String id, String database, String suggestions, List<Integer> idChecker, List<String> containsInIdCheckerListString, List<String> allPossibilitiesListString, Popup popup, ListView<String> suggestionsView) {
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX("+id+") FROM `"+database+"` WHERE 1");
		suggestionsView.getItems().clear();
		allPossibilitiesListString.clear();
		popup.getContent().remove(suggestionsView);
		for(int i = 1; i <= i_max; i++) {
			int x = MySQLDatenbankConnection.getInt("SELECT `"+id+"` FROM `"+database+"` WHERE `"+id+"` = '" + i + "'");
			String content = MySQLDatenbankConnection.getString("SELECT `"+suggestions+"` FROM `"+database+"` WHERE `"+id+"` = '" + x + "'");
			if(intEqualsAny(i, idChecker))
				containsInIdCheckerListString.add(content);
			if(x != 0)
				allPossibilitiesListString.add(content);
		}
		suggestionsView.getItems().addAll(allPossibilitiesListString);
		popup.getContent().add(suggestionsView);
	}
	public List<Integer> initCheckListsPopupSuggestions(String id, String database, String sourceID, int sourceIDValue) {
		return MySQLDatenbankConnection.getIntList("SELECT `"+id+"` FROM `"+database+"` WHERE `"+sourceID+"` = "+sourceIDValue);
	}
	public void initSuggestionsComplete(String id, String database, String suggestions, Set<String> hashset, TextField textfield, AutoCompletionBinding<String> autoCompleteBinding) {
		initSuggestions(id, database, suggestions, hashset);
		initAutoCompletionBinding(autoCompleteBinding, textfield, hashset);
		textfield.addEventHandler(KeyEvent.ANY, event -> {
				if(event.getCode() == KeyCode.ALT){ //STRG+ALT to add new propersitions
					if(event.isControlDown()) {
						event.consume();
						autoCompletionLearnWord(id, database, suggestions, hashset, textfield, textfield.getText().trim(), autoCompleteBinding);
					}
				}
		});
		
	}
	private void autoCompletionLearnWord(String id, String database, String suggestions, Set<String> hashset, TextField textfield, String newWord, AutoCompletionBinding<String> autoCompleteBinding) {
		if(!hashset.contains(newWord))	{
			int i_max = MySQLDatenbankConnection.getInt("SELECT MIN(d1."+id+")+1 FROM `"+database+"` d1 LEFT JOIN `"+database+"` d2 ON d1."+id+" + 1 = d2."+id+" WHERE d2."+id+" is NULL");
			MySQLDatenbankConnection.update("INSERT INTO `"+database+"`(`"+id+"`, `"+suggestions+"`) VALUES ('"+ i_max +"','"+newWord+"')");
			if(autoCompleteBinding != null) {
				autoCompleteBinding.dispose();
			}
			initSuggestions(id, database, suggestions, hashset);
			autoCompleteBinding = TextFields.bindAutoCompletion(textfield, hashset);
		}
	}
	private void initSuggestions(String id, String database, String suggestions, Set<String> hashset) {
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX("+id+") FROM `"+database+"` WHERE 1");
		for(int i = 1; i <= i_max; i++) {
			String content = MySQLDatenbankConnection.getString("SELECT `"+suggestions+"` FROM `"+database+"` WHERE `"+id+"` = '" + i + "'");
			if(content != null)
				hashset.add(content);
		}
	}
	private void initAutoCompletionBinding(AutoCompletionBinding<String> autoCompleteBinding, TextField textfield, Set<String> hashset) {
		autoCompleteBinding = TextFields.bindAutoCompletion(textfield, hashset);
		//autoCompleteBinding.getOnAutoCompleted().handle((AutoCompletionEvent<String>) eventHandler);
	}
	public boolean stringEqualsAny(String target, List<String> stringList) {
	    for (String str : stringList) {
	        if (target.equals(str)) {
	            return true; // The target string is equal to at least one string in the list
	        }
	    }
	    return false; // The target string is not equal to any string in the list
	}
	public boolean intEqualsAny(int target, List<Integer> stringList) {
	    for (int str : stringList) {
	        if (target == str) {
	            return true; // The target int is equal to at least one int in the list
	        }
	    }
	    return false; // The target int is not equal to any int in the list
	}
	@FXML
	public void initialize(){
		initSuggestionsComplete("FID", "möglichefarbe", "Farbe", possibleSuggestionsFarbe, eBayFarbe, autoCompleteFarbe);
		initSuggestionsComplete("TID", "möglicheteile", "Part", possibleSuggestions, teil, autoComplete);
		initSuggestionsComplete("MID", "möglichepartshersteller", "Manufactor", possibleSuggestionsHersteller, hersteller, autoCompleteHersteller);
		initSuggestionsComplete("ZID", "möglichezustand", "Zustand", possibleSuggestionsZustand, zust, autoCompleteZustand);
		initPopupSuggestions(popupProd, suggestionListViewProd, setSuggestionsFindProd, eBayProdukt, teil, possibleSuggestions, "TID", "möglicheteile", "conteileproduktart", "PRID", "möglicheproduktart", "Produktart", setSuggestionsListProd, suggestionListProd);
		initPopupSuggestions(popup, suggestionListView, setSuggestionsFind, eBayPosition, teil, possibleSuggestions, "TID", "möglicheteile", "conteilepositionen", "PID", "möglichepositionen", "Position", setSuggestionsList, suggestionList);
		oriTeilNr.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null) {
		    	oriTeilNr.setText(newValue.toUpperCase());
		    }
		});
		oriOENummer.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null) {
		    	oriOENummer.setText(newValue.toUpperCase());
		    }
		});
		eBayFarbecode.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null) {
		    	eBayFarbecode.setText(newValue.toUpperCase());
		    }
		});
		titel.textProperty().addListener((observable, oldValue, newValue) -> {
		    // Calculate the new character count
		    int newCharacterCount = newValue.length();
		    characterCountLabel.setText(" "+newCharacterCount);

		    // Limit the text to 80 characters if it exceeds the limit
		    if (newCharacterCount > CHARACTER_LIMIT) {
		        titel.setText(newValue.substring(0, CHARACTER_LIMIT));
		    }
		});
		eBFarbC = MySQLDatenbankConnection.getString("SELECT `Farbcode` FROM `cardata` WHERE `CarID` = "+carid+"");
		if(!eBFarbC.equals("")) {
			eBayFarbecode.setText(eBFarbC);
		}
		zust.setText("gut");
		versandDE.getItems().addAll(versandD);
		eBaySpannungEinheit.getItems().addAll(spg);
		ebayStromEinheit.getItems().addAll(str);
		versandEU.getItems().addAll(versandE);
		caid.setText("Fz.Nr.: "+caridFormat.format(carid).toString());
		modelle = ""+MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `cardata` WHERE `CarID` = " + carid)+" "+MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = " + carid);
		modell.setText("Fahrzeug: "+modelle);
		eCat.setText("");
		image(false);
	}
	public void getStage(Stage stage) {
		oriStag = stage;
		oriStag.setOnCloseRequest(event -> {
            event.consume(); // Verhindert das sofortige Schließen des Hauptfensters
            goBack();
        });
	}
	public void getScene(Scene scene) {
		oriScene = scene;	
		oriScene.setOnKeyPressed(event -> {
		    if (event.isShortcutDown() && event.getCode() == KeyCode.V) {
		        // Strg+V wurde gedrückt
		    	pasteImageFromClipboard();
		        event.consume();
		    }
		});
	}
	public void setCatNr(int value){	
		eCat.setText(Integer.toString(value));
	}	
	public void image(boolean direction){
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
		}else {
			image.setImage(null); 
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
	private void handleDrop(DragEvent event){
		try {
			Path path = createTempDir();
			List<File> files = event.getDragboard().getFiles();
			int x = getFileID(path);
			for(int i = 0; i < files.size(); i++) {
				if(files.get(i).getAbsolutePath().toLowerCase().contains(".jpg") || files.get(i).getAbsolutePath().toLowerCase().contains(".png") || files.get(i).getAbsolutePath().toLowerCase().contains(".jpeg")) {
					Path copied = Paths.get(files.get(i).getAbsolutePath());
					Path newFile = Files.createFile(path.resolve(""+(i+x)+".jpg"));
					Files.copy(copied, newFile, StandardCopyOption.REPLACE_EXISTING);
				}
			}
			image(false);
		}catch(IOException e) {
			logger.warn("Dragevent Drop Warning SceneAddCarPartController Function handleDrop", e);
		}
	}
	private void copyImageToDestination(int cpid){
		Path path;
		try {
			path = createDir(carid);
			Path pathe = createTempDir();
			int x = getFileID(pathe);
			for(int i = 0; i < x; i++) {
				Path copied = Paths.get(pathe.resolve(""+i+".jpg").toUri());
				Path newFile = Files.createFile(path.resolve(""+filename.format(cpid)+""+Alphabet[i]+".jpg"));
				Files.copy(copied, newFile, StandardCopyOption.REPLACE_EXISTING);
				Files.deleteIfExists(pathe.resolve(""+i+".jpg"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.warn("Copy Image from TEMP Directory to CAR Directory Warning SceneAddCarPartController Function copyImageToDestination", e);
		}
	}
	private void deleteImagesInTemp(){
		Path pathe;
		try {
			pathe = createTempDir();
			int x = getFileID(pathe);
			for(int i = 0; i < x; i++) {
				Files.deleteIfExists(pathe.resolve(""+i+".jpg"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.warn("Delete Images in TEMP Directory Warning SceneAddCarPartController Function deleteImagesInTemp", e);
		}
	}
	public Path createDir(int carid){
		try {
			Path path = Paths.get(PictureDirectory.getDir()+ "\\" + filename.format(carid));
			if(!Files.exists(path)) {
				Files.createDirectories(path);
			} 
			return path;
		}
		catch (IOException e) {
				// TODO Auto-generated catch block
			logger.warn("Creating Car Directory for Images Warning SceneAddCarPartController Function createDir", e);
		}
		return null;
	}
	private int getFileID(Path path) {
		return new File(path.toString()).list().length;
	}	
	public Path createTempDir(){
		try {
			Path path = Paths.get(PictureDirectory.getDir()+ "\\Temp");
			if(!Files.exists(path)) {
				Files.createDirectories(path);
				return path;
			}
			return path;
		}catch(IOException e){
			logger.warn("Creating Temp Directory for Images Warning SceneAddCarPartController Function createTempDir", e);
		}
		return null;
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
			int newCharacterCount = titel.getLength();
			characterCountLabel.setText(" "+newCharacterCount);
		}else {
			show.getChildren().clear();
			titel.setText(""+teil.getText().toString()+" "+oriTeilNr.getText()+" "+modelle);
			int newCharacterCount = titel.getLength();
			characterCountLabel.setText(" "+newCharacterCount);
		}
		
	}
	
	public void addCarParts(){
		if(titel.getText().equals("")) {
			show.getChildren().clear();
			show.getChildren().addAll(nothingSelected);
		}else if(passtAuch.getText().equals("")){
			show.getChildren().clear();
			show.getChildren().addAll(nothingSelectedPassendFuer);
		}else if(eCat.getText().equals("")){
			show.getChildren().clear();
			show.getChildren().addAll(nothingSelectedKategorie);
		}else if(anzahl.getText().equals("")){
			show.getChildren().clear();
			show.getChildren().addAll(nothingSelectedMenge);
		}else {
			show.getChildren().clear();
			teile = teil.getText();
			tite = titel.getText();
			oriT = oriTeilNr.getText();
			passA = passtAuch.getText();
			kategorie = Integer.parseInt(eCat.getText());
			meng = setInteger(anzahl);
			oEN = setString(oriOENummer);
			herst = setString(hersteller);
			eBProd = setString(eBayProdukt);
			eBFarb = setString(eBayFarbe);
			eBPos = setString(eBayPosition);
			eBSpg = setDouble(eBaySpannung);
			eBStr = setDouble(eBayStrom);
			spgE = setStringComboBox(eBaySpannungEinheit);
			strE = setStringComboBox(ebayStromEinheit);
			eBFarbC = setString(eBayFarbecode);
			zusta = setString(zust);
			vers = setStringComboBox(versandDE);
			versEU = setStringComboBox(versandEU);
			pr = setDouble(preis);
			bemer = bemerkung.getText();
			int i_max = MySQLDatenbankConnection.getInt("SELECT MIN(CarPartID) FROM `carpartsdata` WHERE CarID = "+carid+"");
			if(i_max != 1 || i_max == 0) {
				i_max = 1;
			}else {
				i_max = MySQLDatenbankConnection.getInt("SELECT MIN(t1.CarPartID) + 1 AS NextAvailableCarPartID FROM `carpartsdata` t1 LEFT JOIN `carpartsdata` t2 ON t1.CarPartID + 1 = t2.CarPartID AND t1.CarID = t2.CarID WHERE t2.CarPartID IS NULL AND t1.CarID = "+carid+"");
			}
			int y_max = MySQLDatenbankConnection.getInt("SELECT MIN(d1.CPID)+1 FROM `carpartsdata` d1 LEFT JOIN `carpartsdata` d2 ON d1.CPID + 1 = d2.CPID WHERE d2.CPID is NULL");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			MySQLDatenbankConnection.update("INSERT INTO `carpartsdata`(`CarPartID`, `Bezeichnung`, `Hersteller`, `OrignalTeilenummer`, `Zustand`, `Bemerkung`, `Kategorienummer`, `Preis`, `Menge`, `Versand`, `eBayProduktart`, `CarID`, `versandEU`, `CPID`, `Teil`, `OENumber`, `eBayPosition`, `eBayFarbe`, `eBayFarbecode`, `Spannung`, `SpannungEinheit`, `Stromstaerke`, `StromstaerkeEinheit`, `Passend`, `Version`) VALUES ('"+i_max+"','"+tite+"','"+herst+"','"+oriT+"','"+zusta+"','"+bemer+"','"+kategorie+"','"+pr+"','"+meng+"','"+vers+"','"+eBProd+"','"+carid+"','"+versEU+"','"+y_max+"','"+teile+"','"+oEN+"','"+eBPos+"','"+eBFarb+"', '"+eBFarbC+"','"+eBSpg+"','"+spgE+"','"+eBStr+"','"+strE+"','"+passA+"','0.00001')");
			int x_max = MySQLDatenbankConnection.getInt("SELECT MAX(`ChangeID`) FROM `changehistorycarparts` WHERE 1");
			x_max++;
			changes = "neu";
			MySQLDatenbankConnection.update("INSERT INTO `changehistorycarparts`(`CPID`, `UserID`, `TimeStamp`, `ChangeID`, `CarID`, `Changed`, `CarPartID`, `Bezeichnung`, `Hersteller`, `OrignalTeilenummer`, `Zustand`, `Bemerkung`, `Kategorienummer`, `Preis`, `Menge`, `Versand`, `eBayProduktart`, `versandEU`, `Teil`, `OENumber`, `eBayPosition`, `eBayFarbe`, `eBayFarbecode`, `Spannung`, `SpannungEinheit`, `Stromstaerke`, `StromstaerkeEinheit`, `Passend`, `Version`) VALUES ('"+y_max+"','"+User.id+"','"+timestamp+"','"+x_max+"','"+carid+"','"+changes+"','"+i_max+"','"+tite+"','"+herst+"','"+oriT+"','"+zusta+"','"+bemer+"','"+kategorie+"','"+pr+"','"+meng+"','"+vers+"','"+eBProd+"','"+versEU+"','"+teile+"','"+oEN+"','"+eBPos+"','"+eBFarb+"', '"+eBFarbC+"','"+eBSpg+"','"+spgE+"','"+eBStr+"','"+strE+"','"+passA+"','0.00001')");
			SceneCarPartsController.setCarID(carid);
			copyImageToDestination(i_max);
			try {
				root = FXMLLoader.load(getClass().getClassLoader().getResource("CarPartsOverview.fxml"));
				stag = (Stage) abbrechen.getScene().getWindow();
				stag.setScene(new Scene(root));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("Can't open SceneCarPartsController from SceneAddCarPartController Function addCarParts", e);
			}
		}
	}
	
	public String setString(TextField text) {
		if(text.getText().equals("")){
			return "";
		}else {
			return text.getText();
		}
	}
	public Double setDouble(TextField text) {
		if(text.getText().equals("")){
			return 0.0;
		}else {
			return Double.parseDouble(text.getText());
		}
	}
	public int setInteger(TextField text) {
		if(text.getText().equals("")){
			return 0;
		}else {
			return Integer.parseInt(text.getText());
		}
	}
	public String setStringComboBox(ComboBox<String> text) {
		if(text.getValue() == null){
			return "";
		}else {
			return text.getValue();
		}
	}
	
	public void rightUp(){
		image(true);
		
	}	
	
	public void leftDown(){
		image(false);
	}
	
	public void goBack(){
		try {
			show.getChildren().clear();
			deleteImagesInTemp();
			SceneCarPartsController.setCarID(carid);
			root = FXMLLoader.load(getClass().getClassLoader().getResource("CarPartsOverview.fxml"));
			stag = (Stage) abbrechen.getScene().getWindow();
			stag.setScene(new Scene(root));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Can't open SceneCarPartsController from SceneAddCarPartController Function goBack", e);
		}
	}
	
	public void getEbayCat(){
		try {
			if(Scene1Controller.initThread.initIsFinished() && (SceneGeneralSettingsController.csv.init1IsFinished() || !SceneGeneralSettingsController.csv.getInit1Started())) {
				FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CategorieSelection.fxml"));
			    Parent root = loader.load();
			    SceneCategorieSelectionController sceneCategorieSelectionController = loader.getController();		    
			    // Hier wird die Referenz auf SceneAddCarPartController gesetzt 
			    sceneCategorieSelectionController.setSceneAddCarPartController(this);
			    checkWindow.setScene(new Scene(root));
			    checkWindow.show();
			}else {
				show.getChildren().clear();
				show.getChildren().addAll(loadingData);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Can't open SceneCategorieSelectionController from SceneAddCarPartController Function getEbayCat", e);
		}
	}
	
	public void closeCheckWindow() {
		checkWindow.hide();
	}
	public void delete(){
		try {
			Path path = createTempDir(); 
			int x = getFileID(path);
			Files.deleteIfExists(path.resolve(""+imagecount+".jpg")); 
			int start = imagecount + 1;
			int before;
			for(int i = start; i < x; i++) {
				Path copied = Paths.get(path.resolve(""+i+".jpg").toUri());
				before = i - 1;
				Path newFile = Files.createFile(path.resolve(""+before+".jpg"));
				Files.copy(copied, newFile, StandardCopyOption.REPLACE_EXISTING);
				Files.deleteIfExists(path.resolve(""+i+".jpg"));
			}
			image(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.warn("Can't delete Images from DELETE Button SceneAddCarPartController Function delete", e);
		}
	}
}
