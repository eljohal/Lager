package main.application;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.*;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javafx.application.Platform;
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
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
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

public class SceneRealEditCarPartController {
	private static final Logger logger = LogManager.getLogger(SceneRealEditCarPartController.class);
	public Stage stag;
	static Stage checkWindow = new Stage();
	public static Stage oriStag;
	public Scene scene;
	public static Scene oriScene;
	public Parent root;
	VersionCheckerThread versionCheckerThread = new VersionCheckerThread();
	
	@FXML
	TextField caid;
	DecimalFormat caridFormat = new DecimalFormat("000");
	@FXML
	TextField modell;
	@FXML
	TextField capid;
	static int carid;
	static int carpartid;
	@FXML
	private TextField teil;
	private AutoCompletionBinding<String> autoComplete;
	private Set<String> possibleSuggestions = new HashSet<>();
	@FXML
	private TextField eCat;
	@FXML
	private TextField hersteller;
	private AutoCompletionBinding<String> autoCompleteHersteller;
	private Set<String> possibleSuggestionsHersteller = new HashSet<>();
	@FXML
	private TextField zust;
	private AutoCompletionBinding<String> autoCompleteZustand;
	private Set<String> possibleSuggestionsZustand = new HashSet<>();
	
	@FXML
	TextField oEOri;
	@FXML
	TextField oE;
	String oEString;
	String orioEString;
	@FXML
	TextField eBProdeOri;
	@FXML
	TextField eBProde;
	List<String> suggestionListProd = new ArrayList<>();
	List<Integer> setSuggestionsListProd = new ArrayList<>();
	List<String> setSuggestionsFindProd = new ArrayList<>();
	ListView<String> suggestionListViewProd = new ListView<>();
	javafx.stage.Popup popupProd = new javafx.stage.Popup();
	String eBProdeString;
	String orieBProdeString;
	@FXML
	TextField eBPositionOri;
	@FXML
	TextField eBPosition;
	List<String> suggestionList = new ArrayList<>();
	List<Integer> setSuggestionsList = new ArrayList<>();
	List<String> setSuggestionsFind = new ArrayList<>();
	ListView<String> suggestionListView = new ListView<>();
	javafx.stage.Popup popup = new javafx.stage.Popup();
	String eBPositionString;
	String orieBPositionString;
	@FXML
	TextField ebFarbeOri;
	@FXML
	TextField ebFarbe;
	public AutoCompletionBinding<String> autoCompleteFarbe;
	private Set<String> possibleSuggestionsFarbe = new HashSet<>();
	String ebFarbeString;
	String oriebFarbeString;
	@FXML
	TextField eBFarbcodeOri;
	@FXML
	private TextField passend;
	String pass;
	@FXML
	private TextField passendOri;
	String oriPassend;
	@FXML
	TextField eBFarbcode;
	String eBFarbcodeString;
	String orieBFarbcodeString;
	@FXML
	TextField spgStrOri;
	double oriSpg;
	String oriSpgE;
	double oriStr;
	String oriStrE;
	@FXML
	TextField spg;
	double span;
	@FXML
	TextField str;
	double stro;
	@FXML
	private ComboBox<String> strE;
	private String[] strValue = {"","mA", "A", "kA"};
	String strEin;
	@FXML
	private ComboBox<String> spgE;
	private String[] spgValue = {"","mV", "V", "kV"};
	@FXML
	private ComboBox<BigDecimal> versionSelection;
	ArrayList<BigDecimal> te = new ArrayList<>();
	String spgEin;
	@FXML
	TextField oriTeilNr;
	
	@FXML
	TextArea bemerkungOri;
	String bemOri;
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
	TextField versandEUOri;
	String oriVEU;
	@FXML
	TextField anzahlOri;
	int oriA;
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
	String oriTeil;
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
	int cpid;
	int meng;
	String vers;
	String versEU;
	int imagecount;
	
	Text nothingSelected = new Text("Bitte Titel über Reload Button erstellen");
	Text missingteil = new Text("Bitte vorher ein Teil auswählen");
	Text loadingData = new Text("Bitte warten, eBay Kategorieliste wird noch geladen");
	
	
	
	public void initPopupSuggestions(Popup popup, ListView<String> suggestionsView, List<String> suggestionMarker, TextField textField, TextField source, TextField oriSource, Set<String> sourceHashset, String sourceID, String sourceDatabase, String connectionDatabase, String id, String database, String suggestions, List<Integer> idChecker, List<String> allPossibilitiesListString) {
		
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
		            }else if(!oriSource.getText().equals("")) {
		            	if(sourceHashset.contains(source.getText())) {
		            		int teileID = MySQLDatenbankConnection.getInt("SELECT `"+sourceID+"` FROM `"+sourceDatabase+"` WHERE `Part` = '"+oriSource.getText()+"'");
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
		versionCheckerThread.sceneGeneralSettings(this);
		versionCheckerThread.setEditVersion("SELECT `Version` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		versionCheckerThread.setCarPartController("SceneRealEditCarPartController");
		versionCheckerThread.startThread();
		initSuggestionsComplete("FID", "möglichefarbe", "Farbe", possibleSuggestionsFarbe, ebFarbe, autoCompleteFarbe);
		initSuggestionsComplete("TID", "möglicheteile", "Part", possibleSuggestions, teil, autoComplete);
		initSuggestionsComplete("MID", "möglichepartshersteller", "Manufactor", possibleSuggestionsHersteller, hersteller, autoCompleteHersteller);
		initSuggestionsComplete("ZID", "möglichezustand", "Zustand", possibleSuggestionsZustand, zust, autoCompleteZustand);
		initPopupSuggestions(popupProd, suggestionListViewProd, setSuggestionsFindProd, eBProde, teil, teilOri, possibleSuggestions, "TID", "möglicheteile", "conteileproduktart", "PRID", "möglicheproduktart", "Produktart", setSuggestionsListProd, suggestionListProd);
		initPopupSuggestions(popup, suggestionListView, setSuggestionsFind, eBPosition, teil, teilOri, possibleSuggestions, "TID", "möglicheteile", "conteilepositionen", "PID", "möglichepositionen", "Position", setSuggestionsList, suggestionList);
		initVersionComboBox();
		oriTeilNr.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null) {
		    	oriTeilNr.setText(newValue.toUpperCase());
		    }
		});
		versionSelection.setOnAction(event -> {
            BigDecimal selectedOption = versionSelection.getSelectionModel().getSelectedItem();
            initVersion(selectedOption);
            initView();
            //versionSelection.setText(selectedOption);
        });
		oE.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null) {
		    	oE.setText(newValue.toUpperCase());
		    }
		});
		init();
		titel.textProperty().addListener((observable, oldValue, newValue) -> {
		    // Calculate the new character count
		    int newCharacterCount = newValue.length();
		    characterCountLabel.setText(" "+newCharacterCount);

		    // Limit the text to 80 characters if it exceeds the limit
		    if (newCharacterCount > CHARACTER_LIMIT) {
		        titel.setText(newValue.substring(0, CHARACTER_LIMIT));
		    }
		});
		eBFarbcode.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null) {
		    	eBFarbcode.setText(newValue.toUpperCase());
		    }
		});
		versionSelection.getItems().addAll(te);
		versionSelection.setCellFactory(param -> new ComboBoxListCell<>());	
		initView();
		image(false);
	}
	public void initVersionComboBox(){
		cpid = MySQLDatenbankConnection.getInt("SELECT `CPID` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		BigDecimal startValue = new BigDecimal("0.00001");
		BigDecimal increment = new BigDecimal("0.00001");
		BigDecimal max = MySQLDatenbankConnection.getFloat("SELECT MAX(`Version`) FROM `changehistorycarparts` WHERE `CPID` = "+cpid+"");
			for(BigDecimal i = startValue; i.compareTo(max) <= 0 ;i = i.add(increment)) {
				te.add(i);
			}
	}
	public void initView() {
		oriTeilNrOri.setText(""+oriTNr);
		bemerkungOri.setText(bemOri);
		teilOri.setText(""+teile);
		titel.setText(oriTeil);
		herstellerOri.setText(""+oriHerst);
		zustOri.setText(""+oriZust);

		oEOri.setText(orioEString);
		eBProdeOri.setText(orieBProdeString);
		eBPositionOri.setText(orieBPositionString);
		ebFarbeOri.setText(oriebFarbeString);
		eBFarbcodeOri.setText(orieBFarbcodeString);
		spgStrOri.setText(doubleToString(oriSpg)+" "+oriSpgE+" "+doubleToString(oriStr)+" "+oriStrE);
		
		eCatOri.setText(""+Integer.toString(oriECat));
		preisOri.setText(""+doubleToString(oriPreis));
		versandDEOri.setText(""+oriVDE);
		versandEUOri.setText(""+oriVEU);
		anzahlOri.setText(""+Integer.toString(oriA));
		passendOri.setText(oriPassend);
		
		strE.getItems().clear();
		strE.getItems().addAll(strValue);
		spgE.getItems().clear();
		spgE.getItems().addAll(spgValue);
		versandDE.getItems().clear();
		versandDE.getItems().addAll(versandD);
		versandEU.getItems().clear();
		versandEU.getItems().addAll(versandE);
		
		caid.setText("Fz.Nr.: "+caridFormat.format(carid).toString());
		modelle = ""+MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `cardata` WHERE `CarID` = " + carid)+" "+MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = " + carid);
		modell.setText("Fahrzeug: "+modelle);
		capid.setText("Fahrzeugteil Nr.: "+carpartid);
		eCat.setText("");
		
	}
	public String doubleToString(double value) {
		return String.valueOf(value);
	}
	public void init() {
		cpid = MySQLDatenbankConnection.getInt("SELECT `CPID` FROM `carpartsdata` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+"");
		oriTNr = MySQLDatenbankConnection.getString("SELECT `OrignalTeilenummer` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		teile = MySQLDatenbankConnection.getString("SELECT `Teil` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		bemOri = MySQLDatenbankConnection.getString("SELECT `Bemerkung` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		oriTeil = MySQLDatenbankConnection.getString("SELECT `Bezeichnung` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		oriHerst = MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		oriZust = MySQLDatenbankConnection.getString("SELECT `Zustand` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		oriECat =  MySQLDatenbankConnection.getInt("SELECT `Kategorienummer` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		oriPreis = MySQLDatenbankConnection.getDouble("SELECT `Preis` FROM `carpartsdata` WHERE `CPID` = " + cpid);
		oriVDE = MySQLDatenbankConnection.getString("SELECT `Versand` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		oriVEU = MySQLDatenbankConnection.getString("SELECT `VersandEU` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		oriA =  MySQLDatenbankConnection.getInt("SELECT `Menge` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		orioEString = MySQLDatenbankConnection.getString("SELECT `OENumber` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		orieBProdeString = MySQLDatenbankConnection.getString("SELECT `eBayProduktart` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		orieBPositionString = MySQLDatenbankConnection.getString("SELECT `eBayPosition` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		oriebFarbeString = MySQLDatenbankConnection.getString("SELECT `eBayFarbe` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		orieBFarbcodeString = MySQLDatenbankConnection.getString("SELECT `eBayFarbecode` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		oriSpg = MySQLDatenbankConnection.getDouble("SELECT `Spannung` FROM `carpartsdata` WHERE `CPID` = " + cpid);
		oriSpgE = MySQLDatenbankConnection.getString("SELECT `SpannungEinheit` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		oriStr = MySQLDatenbankConnection.getDouble("SELECT `Stromstaerke` FROM `carpartsdata` WHERE `CPID` = " + cpid);
		oriPassend = MySQLDatenbankConnection.getString("SELECT `Passend` FROM `carpartsdata` WHERE `CPID` = "+cpid);
		oriStrE = MySQLDatenbankConnection.getString("SELECT `StromstaerkeEinheit` FROM `carpartsdata` WHERE `CPID` = "+cpid);
	}
	public void initVersion(BigDecimal version) {
		cpid = MySQLDatenbankConnection.getInt("SELECT `CPID` FROM `changehistorycarparts` WHERE `CarPartID` = "+carpartid+" AND `CarID` = "+carid+" AND `Version` = "+version.toPlainString()+"");
		oriTNr = MySQLDatenbankConnection.getString("SELECT `OrignalTeilenummer` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		teile = MySQLDatenbankConnection.getString("SELECT `Teil` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		bemOri = MySQLDatenbankConnection.getString("SELECT `Bemerkung` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		oriTeil = MySQLDatenbankConnection.getString("SELECT `Bezeichnung` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		oriHerst = MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		oriZust = MySQLDatenbankConnection.getString("SELECT `Zustand` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		oriECat =  MySQLDatenbankConnection.getInt("SELECT `Kategorienummer` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		oriPreis = MySQLDatenbankConnection.getDouble("SELECT `Preis` FROM `changehistorycarparts` WHERE `CPID` = " + cpid+" AND `Version` = "+version.toPlainString());
		oriVDE = MySQLDatenbankConnection.getString("SELECT `Versand` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		oriVEU = MySQLDatenbankConnection.getString("SELECT `VersandEU` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		oriA =  MySQLDatenbankConnection.getInt("SELECT `Menge` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		orioEString = MySQLDatenbankConnection.getString("SELECT `OENumber` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		orieBProdeString = MySQLDatenbankConnection.getString("SELECT `eBayProduktart` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		orieBPositionString = MySQLDatenbankConnection.getString("SELECT `eBayPosition` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		oriebFarbeString = MySQLDatenbankConnection.getString("SELECT `eBayFarbe` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		orieBFarbcodeString = MySQLDatenbankConnection.getString("SELECT `eBayFarbecode` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		oriSpg = MySQLDatenbankConnection.getDouble("SELECT `Spannung` FROM `changehistorycarparts` WHERE `CPID` = " + cpid+" AND `Version` = "+version.toPlainString());
		oriSpgE = MySQLDatenbankConnection.getString("SELECT `SpannungEinheit` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		oriStr = MySQLDatenbankConnection.getDouble("SELECT `Stromstaerke` FROM `changehistorycarparts` WHERE `CPID` = " + cpid+" AND `Version` = "+version.toPlainString());
		oriPassend = MySQLDatenbankConnection.getString("SELECT `Passend` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
		oriStrE = MySQLDatenbankConnection.getString("SELECT `StromstaerkeEinheit` FROM `changehistorycarparts` WHERE `CPID` = "+cpid+" AND `Version` = "+version.toPlainString());
	}
	public void versionError() {
			versionCheckerThread.pauseThread();
				Platform.runLater(() -> {
					
					try {
						FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ChangedData.fxml"));		
						Parent root;
						root = loader.load();
						SceneVersionChangeController sceneSelectedCarPartController = loader.getController();
						sceneSelectedCarPartController.sceneGeneralSettings(this);
						sceneSelectedCarPartController.setCarID(carid);
						sceneSelectedCarPartController.setCarPartID(carpartid);
						sceneSelectedCarPartController.getStage(oriStag);
						sceneSelectedCarPartController.setText("SceneRealEditCarPartController");
						checkWindow.setScene(new Scene(root));
						checkWindow.show();
					}catch (IOException e) {
						// TODO Auto-generated catch block
						logger.error("Version Check new Window Error SceneRealEditCarPartsController, versionError(): ", e);
						}
				});
			
	}
	public void setCatNr(int value){
		eCat.setText(Integer.toString(value));
	}
	public void initZustand() {
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(ZID) FROM `möglichezustand` WHERE 1");
		for(int i = 1; i <= i_max; i++) {
			possibleSuggestionsZustand.add(MySQLDatenbankConnection.getString("SELECT `Zustand` FROM `möglichezustand` WHERE `ZID` = '" + i + "'"));
		}
	}
	public void getStage(Stage stage) {
		oriStag = stage;
		oriStag.setOnCloseRequest(event -> {
            event.consume(); // Verhindert das sofortige Schließen des Hauptfensters
            try {
				goBack();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
	}	
	public void reload (){
		if(teil.getText().equals("") && !oriTeilNr.getText().isEmpty()) {	
				show.getChildren().clear();
				if(oriTeilNr.getText().equals("-"))
					titel.setText(""+teilOri.getText().toString()+" "+modelle);
				else
					titel.setText(""+teilOri.getText().toString()+" "+oriTeilNr.getText()+" "+modelle);			
		}else if(!teil.getText().isEmpty() && !oriTeilNr.getText().isEmpty()) {
			
			show.getChildren().clear();
			if(oriTeilNr.getText().equals("-"))
				titel.setText(""+teil.getText().toString()+" "+modelle);
			else
				titel.setText(""+teil.getText().toString()+" "+oriTeilNr.getText()+" "+modelle);
		
		}else if(!teil.getText().isEmpty() && oriTeilNr.getText().isEmpty()){
			
			if(oriTeilNrOri.getText().equals("")) {
				show.getChildren().clear();
				titel.setText(""+teil.getText().toString()+" "+modelle);
			}else {
				titel.setText(""+teil.getText().toString()+" "+oriTeilNrOri.getText()+" "+modelle);
			}
			
		}else if(teil.getText().isEmpty() && oriTeilNr.getText().isEmpty()) {
			
			if(oriTeilNrOri.getText().equals("")) {
				show.getChildren().clear();
				titel.setText(""+teilOri.getText().toString()+" "+modelle);
			}else {
				titel.setText(""+teilOri.getText().toString()+" "+oriTeilNrOri.getText()+" "+modelle);
			}
		}
		
	}
	public void getScene(Scene scene) {
		oriScene = scene;	
		oriScene.setOnKeyPressed(event -> {
		    if (event.isControlDown() && event.getCode() == KeyCode.C) {
		        // Strg+C wurde gedrückt
		        // Führen Sie hier die Aktion zum Kopieren aus
		        event.consume();
		    } else if (event.isShortcutDown() && event.getCode() == KeyCode.V) {
		        // Strg+V wurde gedrückt
		        // Führen Sie hier die Aktion zum Einfügen aus
		    	pasteImageFromClipboard();
		        event.consume();
		    }
		});
	}
	private void pasteImageFromClipboard() {
		Clipboard clipboard = Clipboard.getSystemClipboard();
		if (clipboard.hasUrl()) {
			Path path;
			try {
				path = createDir(carid);
				List<File> files = clipboard.getFiles();
				int x = countImages(carpartid);
				int z;
				for(int i = 0; i < files.size(); i++) {
					if(files.get(i).getAbsolutePath().toLowerCase().contains(".jpg") || files.get(i).getAbsolutePath().toLowerCase().contains(".png") || files.get(i).getAbsolutePath().toLowerCase().contains(".jpeg")) {
						z = x + i;
						Path copied = Paths.get(files.get(i).getAbsolutePath());
						Path newFile = Files.createFile(path.resolve(""+filename.format(carpartid)+""+Alphabet[z]+".jpg"));
						Files.copy(copied, newFile, StandardCopyOption.REPLACE_EXISTING);
					}
				}
				image(false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.warn("Paste Image Error SceneRealEditCarPartController Function pasteImageFromClipboard", e);
			}
	    }
	}
	public void image(boolean direction){
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
		}else {
			image.setImage(null); 
		}
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
	@FXML
	private void handleDragOver(DragEvent event) {
		if(event.getDragboard().hasFiles()) {
		event.acceptTransferModes(TransferMode.ANY);
		}
	}
	@FXML
	private void handleDrop(DragEvent event) throws IOException {
		Path path = createDir(carid);
		List<File> files = event.getDragboard().getFiles();
		int x = countImages(carpartid);
		int z;
		for(int i = 0; i < files.size(); i++) {
			if(files.get(i).getAbsolutePath().toLowerCase().contains(".jpg") || files.get(i).getAbsolutePath().toLowerCase().contains(".png") || files.get(i).getAbsolutePath().toLowerCase().contains(".jpeg")) {
				z = x + i;
				Path copied = Paths.get(files.get(i).getAbsolutePath());
				Path newFile = Files.createFile(path.resolve(""+filename.format(carpartid)+""+Alphabet[z]+".jpg"));
				Files.copy(copied, newFile, StandardCopyOption.REPLACE_EXISTING);
			}
		}
		image(false);
	}
	public Path createDir(int carid){
		try {
			Path path = Paths.get(PictureDirectory.getDir()+ "\\" + filename.format(carid));
	
			//Path path = Paths.get(PictureDirectory.getDir()+ "\\" + filename.format(carid));
			if(!Files.exists(path)) {
				Files.createDirectories(path);
				return path;
			}
			return path;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Creating Car Directory for Images SceneRealEditCarPartController Function createDir", e);
		}
		return null;
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
	public void rightUp(){
		image(true);
	}	
	
	public void leftDown(){
		image(false);
	}
	public static void initCarPart(int cp){
		carpartid = cp;
	}
	public void goBack() throws IOException {
		versionCheckerThread.pauseThread();
		if(versionCheckerThread.executor.isShutdown()) {
			checkWindow.close();
			show.getChildren().clear();
			SceneCarPartsController.setCarID(carid);
			root = FXMLLoader.load(getClass().getClassLoader().getResource("CarPartsOverview.fxml"));
			stag = (Stage) abbrechen.getScene().getWindow();
			stag.setScene(new Scene(root));
		}
	}
	public void getEbayCat() throws IOException{
		if(Scene1Controller.initThread.initIsFinished() && (SceneGeneralSettingsController.csv.init1IsFinished() || !SceneGeneralSettingsController.csv.getInit1Started())) {
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CategorieSelection.fxml"));
		    Parent root = loader.load();
		    SceneCategorieSelectionController sceneCategorieSelectionController = loader.getController();
		    sceneCategorieSelectionController.setSceneEditCarPartController(this);
		    checkWindow.setScene(new Scene(root));
		    checkWindow.show();
	    }else {
			show.getChildren().clear();
			show.getChildren().addAll(loadingData);
		}
	}
	String teiler;
	public void change() throws IOException {
		if(titel.getText().equals("")) {
			show.getChildren().clear();
			show.getChildren().addAll(nothingSelected);
		}else {
			tite = titel.getText();
			if(oriTeil.equals(tite)) {
				tite = oriTeil;
			}
			teiler = teile;
			if(!teil.getText().isBlank()) {
				teiler = teil.getText();
			}
			herst = setEditedString(hersteller, herst, oriHerst);
			oriT = setEditedString(oriTeilNr, oriT, oriTNr);
			zusta = setEditedString(zust, zusta, oriZust);
			kategorie = setEditedInteger(eCat, kategorie, oriECat);
			pr = setEditedDouble(preis, pr, oriPreis);
			eBFarbcodeString = setEditedString(eBFarbcode, eBFarbcodeString, orieBFarbcodeString);
			oEString = setEditedString(oE, oEString, orioEString);
			eBProdeString = setEditedString(eBProde, eBProdeString, orieBProdeString);
			eBPositionString = setEditedString(eBPosition, eBPositionString, orieBPositionString);
			ebFarbeString = setEditedString(ebFarbe, ebFarbeString, oriebFarbeString);
			span = setEditedDouble(spg, span, oriSpg);
			stro = setEditedDouble(str, stro, oriStr);
			strEin = setEditedStringComboBox(strE, strEin, oriStrE);
			spgEin = setEditedStringComboBox(spgE, spgEin, oriSpgE);
			pass = setEditedString(passend, pass, oriPassend);		
			vers = setEditedStringComboBox(versandDE, vers, oriVDE);
			versEU =  setEditedStringComboBox(versandEU, versEU, oriVEU);
			meng = setEditedInteger(anzahl, meng, oriA);
			bemer = setEditedStringTextArea(bemerkungOri, bemer, bemOri);
			//TODO AFTER
			SceneRealEditCarPartsCheckController.init(oriTeil, teile, oriHerst, oriTNr, oriZust, oriECat, oriPreis, oriVDE, oriVEU, oriA, bemOri, orioEString, orieBFarbcodeString, oriebFarbeString, orieBPositionString, orieBProdeString, oriStrE, oriSpgE, oriStr, oriSpg, oriPassend);
			SceneRealEditCarPartsCheckController.initSecond(tite, teiler, herst, oriT, zusta, kategorie, pr, vers, versEU, meng, bemer, oEString, eBFarbcodeString, ebFarbeString, eBPositionString, eBProdeString, strEin, spgEin, stro, span, pass);
			SceneRealEditCarPartsCheckController.setCarID(carid);
			SceneRealEditCarPartsCheckController.initCarPart(carpartid);
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("RealEditCarPartsCheck.fxml"));	
			Parent root = loader.load();
			SceneRealEditCarPartsCheckController sceneGeneralSettingsCarManufactorController = loader.getController();
			sceneGeneralSettingsCarManufactorController.sceneGeneralSettings(this);
			sceneGeneralSettingsCarManufactorController.getStage(oriStag);
			checkWindow.setScene(new Scene(root));
		    checkWindow.show();
		}
	}
	
	public String setEditedString(TextField text, String edit, String ori) {
		if(text.getText().equals("-")) {
			return "";
		}else if(!text.getText().isBlank()) {
			return text.getText();
		}
		return ori;
	}
	
	public String setEditedStringTextArea(TextArea text, String edit, String ori) {
		if(text.getText().equals("-")) {
			return "";
		}else if(!text.getText().equals(ori)) {
			return text.getText();
		}
		return ori;
	}
	
	public double setEditedDouble(TextField text, double edit, double ori) {
		if(text.getText().equals("-")) {
			return 0.0;
		}else if(!text.getText().isBlank()) {
			return Double.parseDouble(text.getText().replace(",", "."));
		}
		return ori;
	}
	
	public int setEditedInteger(TextField text, int edit, int ori) {
		if(text.getText().equals("-")) {
			return 0;
		}else if(!text.getText().isBlank()) {
			return Integer.parseInt(text.getText().replace(",", "."));
		}
		return ori;
	}
	
	public String setEditedStringComboBox(ComboBox<String> text, String edit, String ori) {
		if(text.getValue() != null) {
			return text.getValue();
		}
		return ori;
	}
	
	public void showCheckWindow(Stage stage) throws IOException {
		stage.show();
	}
	public void closeCheckWindow() throws IOException {
		checkWindow.hide();
	}
	public void delete() throws IOException {
		Path path = createDir(carid); //5
		int x = countImages(carpartid);
		Files.deleteIfExists(path.resolve(""+filename.format(carpartid)+""+Alphabet[imagecount]+".jpg")); //3
		int start = imagecount + 1;
		int before;
		for(int i = start; i < x; i++) {
			Path copied = Paths.get(path.resolve(""+filename.format(carpartid)+""+Alphabet[i]+".jpg").toUri());
			before = i - 1;
			Path newFile = Files.createFile(path.resolve(""+filename.format(carpartid)+""+Alphabet[before]+".jpg"));
			Files.copy(copied, newFile, StandardCopyOption.REPLACE_EXISTING);
			Files.deleteIfExists(path.resolve(""+filename.format(carpartid)+""+Alphabet[i]+".jpg"));
		}
		image(false);
	}
}
