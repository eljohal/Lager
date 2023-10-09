package main.application;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SceneCreateCarController {
	public Stage stag;
	public static Stage oriStag;
	public Scene scene;
	public Parent root;
	
	@FXML
	private ComboBox<String> kraftstoff;
	private String[] fuel = {"","Benzin", "Diesel", "Elektro", "Gas"};
	
	@FXML
	private ComboBox<String> getriebe;
	private String[] getr = {"","Automatik","Schaltung"};

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
	public void initialize() {
		initSuggestionsComplete("MID", "carmanufactor", "Manufactor", possibleSuggestionsHersteller, hersteller, autoCompleteHersteller);
		initSuggestionsComplete("CMID", "möglichecarmodells", "Modell", possibleSuggestionsModell, modell, autoCompleteModell);
		oriStag.setOnCloseRequest(event -> {
            event.consume(); // Verhindert das sofortige Schließen des Hauptfensters
            try {
				goBack();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		kraftstoff.getItems().addAll(fuel);
		getriebe.getItems().addAll(getr);
		
	}	
	
	public static void getStage(Stage stage) {
		oriStag = stage;
	}
	
	public void goBack() throws IOException {
		stag = (Stage) abbrechen.getScene().getWindow();
		SceneOverviewController.stag = stag;
		root = FXMLLoader.load(getClass().getClassLoader().getResource("Overview.fxml"));	
		stag.setScene(new Scene(root));
	}
	
	public void addCar() {
		if (hersteller.getText().equals("")) {
			show.getChildren().clear();
			Text nothingSelected = new Text("erforderliche Felder sind leer");
			nothingSelected.setFill(Color.RED);
			show.getChildren().addAll(nothingSelected);
		}else {
			show.getChildren().clear();
			String herst = hersteller.getText();
			String modl = modell.getText();
			String farbe = farbcode.getText();
			String fahrgenmr = fahrgestellnummer.getText();
			String getr = getriebe.getValue();
			String getrco = getriebecode.getText();
			int hub = 0;
			if(!hubraum.getText().isEmpty()) {
				 hub = Integer.parseInt(hubraum.getText());
			}
			int kilo = 0; 
			if(!kilometer.getText().isBlank()) {	
				kilo = Integer.parseInt(kilometer.getText());
			}
			LocalDate zulassung = LocalDate.of(1997, 8, 24);
			if(erstzulassung.getValue() != null)
				zulassung = erstzulassung.getValue();
			String kraft = kraftstoff.getValue();
			int leist = 0;
			if(!leistung.getText().isEmpty())
				leist = Integer.parseInt(leistung.getText());
			String motor = motorcode.getText();
			int tuer = 0;
			if(!tuercount.getText().isEmpty())
				tuer = Integer.parseInt(tuercount.getText());
			int i_max = MySQLDatenbankConnection.getInt("SELECT MIN(d1.CarID)+1 FROM `cardata` d1 LEFT JOIN `cardata` d2 ON d1.CarID + 1 = d2.CarID WHERE d2.CarID is NULL");
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			MySQLDatenbankConnection.update("INSERT INTO `cardata`(`CarID`, `Erstzulassung`, `Fahrgestellnummer`, `Farbcode`, `Getriebeart`, `Getriebecode`, `Hersteller`, `Hubraum`, `Kilometerstand`, `Kraftstoff`, `Leistung`, `Modell`, `Motorcode`, `Türenanzahl`, `version`) VALUES ('" + i_max + "','" + zulassung + "','" + fahrgenmr + "','" + farbe + "','" + getr + "','" + getrco + "','" + herst + "','" + hub + "','" + kilo + "','" + kraft + "','" + leist + "','" + modl + "','" + motor + "','" + tuer + "','0.00001')");
			int x_max = MySQLDatenbankConnection.getInt("SELECT MAX(`ChangeID`) FROM `changehistorycar` WHERE 1");
			x_max++;
			String changes = "neu";
			MySQLDatenbankConnection.update("INSERT INTO `changehistorycar`(`TimeStamp`, `ChangeID`, `CarID`, `UserID`, `Changed`, `Erstzulassung`, `Fahrgestellnummer`, `Farbcode`, `Getriebeart`, `Getriebecode`, `Hersteller`, `Hubraum`, `Kilometerstand`, `Kraftstoff`, `Leistung`, `Modell`, `Motorcode`, `Türrenanzahl`, `version`) VALUES ('"+timestamp+"','"+x_max+"','"+i_max+"','"+User.id+"','"+changes+"','" + zulassung + "','" + fahrgenmr + "','" + farbe + "','" + getr + "','" + getrco + "','" + herst + "','" + hub + "','" + kilo + "','" + kraft + "','" + leist + "','" + modl + "','" + motor + "','" + tuer + "','0.00001')");
			Text normal1 = new Text("Fahrzeug (");
			Text fahrzeugid = new Text("" + i_max);
			Text normal2 = new Text(") ");
			Text fahrzeugmodl = new Text(herst + " " + modl);
			Text normal3 = new Text(" wurde hinzugefügt. Kehre mit Abbrechen zurück.");
			normal1.setFill(Color.BLACK);
			normal2.setFill(Color.BLACK);
			normal3.setFill(Color.BLACK);
			fahrzeugid.setFill(Color.GREEN);
			fahrzeugmodl.setFill(Color.GREEN);
			show.getChildren().addAll(normal1, fahrzeugid, normal2, fahrzeugmodl, normal3);
		}
	}
}
