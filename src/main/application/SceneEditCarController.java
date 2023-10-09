package main.application;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.*;

import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SceneEditCarController {

	private static final Logger logger = LogManager.getLogger(SceneEditCarController.class);
	public static Stage oriStag;
	public static Stage stag;
	static Stage checkWindow = new Stage();
	public static Scene scene;
	public Parent root;
	VersionCheckerThread versionEditCarCheck = new VersionCheckerThread();
	
	@FXML
	private ComboBox<String> kraftstoff;
	private String[] fuel = {"","Benzin", "Diesel", "Elektro", "Gas"};
	
	@FXML
	private ComboBox<String> getriebe;
	private String[] getr = {"","Automatik","Schaltung"};
	
	@FXML
	TextField capid;
	@FXML
	private ComboBox<BigDecimal> versionSelection;
	ArrayList<BigDecimal> te = new ArrayList<>();
	
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
	private AutoCompletionBinding<String> autoCompleteHersteller;
	private Set<String> possibleSuggestionsHersteller = new HashSet<>();
	@FXML
	TextField modell;
	private AutoCompletionBinding<String> autoCompleteModell;
	private Set<String> possibleSuggestionsModell = new HashSet<>();
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
	
	
	@FXML
	public void initialize() throws IOException {
		versionEditCarCheck.sceneEditCar(this);
		versionEditCarCheck.setEditVersion("SELECT `version` FROM `cardata` WHERE `CarID` = "+carid+"");
		versionEditCarCheck.setCarPartController("SceneEditCarController");
		versionEditCarCheck.startThread();
		initSuggestionsComplete("MID", "carmanufactor", "Manufactor", possibleSuggestionsHersteller, hersteller, autoCompleteHersteller);
		initSuggestionsComplete("CMID", "möglichecarmodells", "Modell", possibleSuggestionsModell, modell, autoCompleteModell);
		initVersionComboBox();
		initView();
		oriStag.setOnCloseRequest(event -> {
            event.consume(); // Verhindert das sofortige Schließen des Hauptfensters
            try {
				goBack();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
		autoCompleteHersteller = TextFields.bindAutoCompletion(hersteller, possibleSuggestionsHersteller);
		autoCompleteModell = TextFields.bindAutoCompletion(modell, possibleSuggestionsModell);
		versionSelection.setOnAction(event -> {
            BigDecimal selectedOption = versionSelection.getSelectionModel().getSelectedItem();
            initVersion(selectedOption);
            initView();
            //versionSelection.setText(selectedOption);
        });
		farbcode.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null) {
		    	farbcode.setText(newValue.toUpperCase());
		    }
		});
		motorcode.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null) {
		    	motorcode.setText(newValue.toUpperCase());
		    }
		});
		getriebecode.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null) {
		    	getriebecode.setText(newValue.toUpperCase());
		    }
		});
		fahrgestellnummer.textProperty().addListener((observable, oldValue, newValue) -> {
		    if (newValue != null) {
		    	fahrgestellnummer.setText(newValue.toUpperCase());
		    }
		});
	}
	public void initView() {
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
	public void initVersionComboBox(){
		te.clear();
		versionSelection.getItems().clear();
		BigDecimal startValue = new BigDecimal("0.00001");
		BigDecimal increment = new BigDecimal("0.00001");
		BigDecimal max = MySQLDatenbankConnection.getFloat("SELECT MAX(`version`) FROM `changehistorycar` WHERE `CarID` = "+carid+"");
			for(BigDecimal i = startValue; i.compareTo(max) <= 0 ;i = i.add(increment)) {
				te.add(i);
			}
		
		versionSelection.getItems().addAll(te);
		versionSelection.setCellFactory(param -> new ComboBoxListCell<>());	
	}
	
	public void initVersion(BigDecimal version) {
		LocalDate zulassung;
		zulassung = MySQLDatenbankConnection.getDate("SELECT `Erstzulassung` FROM `changehistorycar` WHERE `CarID` = " + carid + " AND `version` = "+version.toPlainString()+"");
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.uuuu");
	    zulass = zulassung.format(formatters);
	    herstell = MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `changehistorycar` WHERE `CarID` = " + carid + " AND `version` = "+version.toPlainString()+"");
	    model = MySQLDatenbankConnection.getString("SELECT `Modell` FROM `changehistorycar` WHERE `CarID` = " + carid + " AND `version` = "+version.toPlainString()+"");
	    farbcodee = MySQLDatenbankConnection.getString("SELECT `Farbcode` FROM `changehistorycar` WHERE `CarID` = " + carid + " AND `version` = "+version.toPlainString()+"");
	    fahrgestellnmr = MySQLDatenbankConnection.getString("SELECT `Fahrgestellnummer` FROM `changehistorycar` WHERE `CarID` = " + carid + " AND `version` = "+version.toPlainString()+"");
	    hubraume = MySQLDatenbankConnection.getInt("SELECT `Hubraum` FROM `changehistorycar` WHERE `CarID` = " + carid + " AND `version` = "+version.toPlainString()+"");
	    kilometere = MySQLDatenbankConnection.getInt("SELECT `Kilometerstand` FROM `changehistorycar` WHERE `CarID` = " + carid + " AND `version` = "+version.toPlainString()+"");
	    leistunge = MySQLDatenbankConnection.getInt("SELECT `Leistung` FROM `changehistorycar` WHERE `CarID` = " + carid + " AND `version` = "+version.toPlainString()+"");
	    motorcodee = MySQLDatenbankConnection.getString("SELECT `Motorcode` FROM `changehistorycar` WHERE `CarID` = " + carid + " AND `version` = "+version.toPlainString()+"");
	    getriebecodee = MySQLDatenbankConnection.getString("SELECT `Getriebecode` FROM `changehistorycar` WHERE `CarID` = " + carid + " AND `version` = "+version.toPlainString()+"");
	    getriebearte = MySQLDatenbankConnection.getString("SELECT `Getriebeart` FROM `changehistorycar` WHERE `CarID` = " + carid + " AND `version` = "+version.toPlainString()+"");
	    tuerz = MySQLDatenbankConnection.getInt("SELECT `Türanzahl` FROM `changehistorycar` WHERE `CarID` = " + carid + " AND `version` = "+version.toPlainString()+"");
	    krafte = MySQLDatenbankConnection.getString("SELECT `Kraftstoff` FROM `changehistorycar` WHERE `CarID` = " + carid + " AND `version` = "+version.toPlainString()+"");
	}
	public void versionError() {
		versionEditCarCheck.pauseThread();
			Platform.runLater(() -> {
				
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ChangedData.fxml"));		
					Parent root;
					root = loader.load();
					SceneVersionChangeController sceneSelectedCarPartController = loader.getController();
					sceneSelectedCarPartController.sceneEditCar(this);
					sceneSelectedCarPartController.setCarID(carid);
					sceneSelectedCarPartController.getStage(oriStag);
					sceneSelectedCarPartController.setText("SceneEditCarController");
					checkWindow.setScene(new Scene(root));
					checkWindow.show();
				}catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("Version Check new Window Error SceneEditCarController, versionError(): ", e);
					}
			});
		
	}
	
	public static void getStage(Stage stage) {
		oriStag = stage;
	}
	
	public static void init(int cari) {
		carid = cari;
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
		versionEditCarCheck.pauseThread();
		if(versionEditCarCheck.executor.isShutdown()) {
			stag = (Stage) abbrechen.getScene().getWindow();
			SceneOverviewController.stag = stag;
			root = FXMLLoader.load(getClass().getClassLoader().getResource("Overview.fxml"));
			stag.setScene(new Scene(root));
		}
	}
	
	public void addCar() throws IOException {
		if(erstzulassung.getValue() == null) {
			zulass2 = zulass;
		}else {
			LocalDate zulassung = erstzulassung.getValue(); 
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd.MM.uuuu");
			zulass2 = zulassung.format(formatters);
		}
		herstell2 = setEditedString(hersteller, herstell2, herstell);
		model2 = setEditedString(modell, model2, model);
		farbcodee2 = setEditedString(farbcode, farbcodee2, farbcodee);
		motorcodee2 = setEditedString(motorcode, motorcodee2, motorcodee);
		getriebecodee2 = setEditedString(getriebecode, getriebecodee2, getriebecodee);
		fahrgestellnmr2 = setEditedString(fahrgestellnummer, fahrgestellnmr2, fahrgestellnmr);
		hubraume2 = setEditedInteger(hubraum, hubraume2, hubraume2);
		kilometere2 = setEditedInteger(kilometer, kilometere2, kilometere);
		leistunge2 = setEditedInteger(leistung, leistunge2, leistunge);
		tuerz2 = setEditedInteger(tuercount, tuerz2, tuerz);
		getriebearte2 = setEditedStringComboBox(getriebe, getriebearte2, getriebearte);
		krafte2 = setEditedStringComboBox(kraftstoff, krafte2, krafte);
		SceneEditCarCheckController.init(zulass, herstell, model, farbcodee, fahrgestellnmr, hubraume, kilometere, leistunge, tuerz, motorcodee, krafte, getriebecodee, getriebearte, carid);
		SceneEditCarCheckController.initSecondCar(zulass2, herstell2, model2, farbcodee2, fahrgestellnmr2, hubraume2, kilometere2, leistunge2, tuerz2, motorcodee2, krafte2, getriebecodee2, getriebearte2);			
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("EditCarCheck.fxml"));	
		Parent root = loader.load();
		SceneEditCarCheckController sceneGeneralSettingsCarManufactorController = loader.getController();
		sceneGeneralSettingsCarManufactorController.sceneGeneralSettings(this);
		sceneGeneralSettingsCarManufactorController.getStage(oriStag);
		checkWindow.setScene(new Scene(root));
	    checkWindow.show();
	}
	
	public String setEditedString(TextField text, String edit, String ori) {
		if(text.getText().equals("-")) {
			return "";
		}else if(!text.getText().isBlank()) {
			return text.getText();
		}
		return ori;
	}
	
	public String setEditedStringComboBox(ComboBox<String> text, String edit, String ori) {
		if(text.getValue() != null) {
			return text.getValue();
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
	
	public void showCheckWindow(Stage stage) throws IOException {
		stage.show();
	}
	
	public void closeCheckWindow() throws IOException {
		checkWindow.hide();
	}
	
}
