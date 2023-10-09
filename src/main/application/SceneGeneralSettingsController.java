package main.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class SceneGeneralSettingsController {
	public static Stage stag;
	static Stage checkWindow = new Stage();
	public static Stage oriStag;
	public Scene scene;
	public Parent root;

	static VersionCheckerThread csv = new VersionCheckerThread();
	
	@FXML
	Button pic;
	@FXML
	Button settings;
	@FXML
	Button carManufactor;
	@FXML
	Button historie;
	@FXML
	Button carModell;
	@FXML
	Button carParts;
	@FXML
	Button carPartsManufactor;
	@FXML
	Button csvImportButton;
	@FXML
	Button abbrechen;
	@FXML
	Button possibleZustand;
	@FXML
	Button possiblePos;
	@FXML
	Button possibleProd;
	@FXML
	Button possibleFarbe;
	@FXML
	private AnchorPane anchorid;
	@FXML
	TextField picDirTextField;
	@FXML
	TextField csvTextField;
	int picDir = 0;
	String picDirName = "Bilderspeicherort";
	String dataPath = "";
	@FXML
	public void initialize() {
		oriStag.setOnCloseRequest(event -> {
            event.consume(); // Verhindert das sofortige Schlieﬂen des Hauptfensters
            try {
				goBack();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
		dataPath = MySQLDatenbankConnection.getString("SELECT `Verzeichnis` FROM `programdata` WHERE `ID` = "+picDir+"");
		if(dataPath.equals("Error")) {
			picDirTextField.setText("");
		}else {
			picDirTextField.setText(dataPath);
		}
		
	}
	public static void getStage(Stage stage) {
		oriStag = stage;
	}
	public void picDir() {
		final DirectoryChooser chooseFile = new DirectoryChooser();
		
		stag = (Stage) anchorid.getScene().getWindow();
		
		File file = chooseFile.showDialog(stag);
		
		if(file != null) {
			picDirTextField.setText(file.getAbsolutePath());
			new PictureDirectory(picDirTextField.getText());
			String stringWithBackslashes = addBackslashes(file.getAbsolutePath());
			MySQLDatenbankConnection.update("UPDATE `programdata` SET `Verzeichnis`='"+stringWithBackslashes+"' WHERE `ID`='"+picDir+"'");
			PictureDirectory.setDir();
		}
		
	}
	public static String addBackslashes(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (c == '\\') {
                result.append("\\\\"); // Add an extra backslash before each backslash
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
	public void csvImport() throws IOException, CsvException {
		final FileChooser chooseFile = new FileChooser();
		MySQLDatenbankConnection.update("DELETE FROM `csvcategories` WHERE 1");
		chooseFile.getExtensionFilters().addAll(new ExtensionFilter("CSV Files", "*.csv"));
		stag = (Stage) anchorid.getScene().getWindow();
		File file = chooseFile.showOpenDialog(stag);
		if(file != null) {
			csv.setFile(file);
			csv.startInit1Thread();
			csvTextField.setText(file.getAbsolutePath());
	    }
		
	}
	
	public static void addData(File file) throws FileNotFoundException, IOException, CsvException {
		try (CSVReader reader = new CSVReader(new FileReader(file))) {
            List<String[]> r = reader.readAll();
            List<String[]> foreignKeysTable = createForeignKeyTable(r);
            for (int i = 3; i < foreignKeysTable.size(); i++) {
                String[] row = foreignKeysTable.get(i);
                MySQLDatenbankConnection.update("INSERT INTO `csvcategories`(`CatID`, `L1`, `L2`, `L3`, `L4`, `L5`, `L6`, `CategorieNr`) VALUES (\""+row[0]+"\",\""+row[1]+"\",\""+row[2]+"\",\""+row[3]+"\",\""+row[4]+"\",\""+row[5]+"\",\""+row[6]+"\",\""+row[7]+"\")");
            }
        }
		
		if(Scene1Controller.initThread.initIsFinished()) {
			SceneCategorieSelectionController.getCateData();
		}
	}
	
	public static List<String[]> createForeignKeyTable(List<String[]> csvData) {
	    List<String[]> foreignKeysTable = new ArrayList<>();
	    String[] header = csvData.get(0);
	    String[] newHeader = new String[header.length + 1];
	    newHeader[0] = "Index";
	    System.arraycopy(header, 0, newHeader, 1, header.length);
	    foreignKeysTable.add(newHeader);

	    int[] lastNonEmptyIndexes = new int[header.length];
	    Arrays.fill(lastNonEmptyIndexes, -1);

	    for (int i = 1; i < csvData.size(); i++) {
	        String[] currentRow = csvData.get(i);
	        String[] newRow = new String[currentRow.length + 1];

	        newRow[0] = Integer.toString(i);

	        // Kopiere die Werte aus der aktuellen Zeile in die neue Zeile
	        for (int j = 0; j < currentRow.length; j++) {
	            newRow[j + 1] = currentRow[j];
	        }

	        // Aktualisiere die Verweise auf den Index
	        for (int j = 1; j < currentRow.length + 1; j++) {
	            if (!newRow[j].isEmpty()) {
	                lastNonEmptyIndexes[j - 1] = i;
	            } else {
	                newRow[j] = Integer.toString(lastNonEmptyIndexes[j - 1]);
	            }
	        }

	        // F¸ge die neue Zeile zur Tabelle hinzu
	        foreignKeysTable.add(newRow);
	    }

	    return foreignKeysTable;
	}

	public static List<String> getFirstValuesWithIndex(List<String[]> csvData, List<String[]> foreignKeysTable) {
	    List<String> firstValuesWithIndex = new ArrayList<>();

	    for (int i = 1; i < foreignKeysTable.size(); i++) {
	        String[] row = foreignKeysTable.get(i);
	        int index = Integer.parseInt(row[0]);

	        if (index >= 1 && index < csvData.size()) {
	            String[] csvRow = csvData.get(index);
	            String firstValue = "";
	            for (String value : csvRow) {
	                if (!value.isEmpty()) {
	                    firstValue = value;
	                    break;
	                }
	            }
	            firstValuesWithIndex.add(firstValue);
	        }
	    }

	    return firstValuesWithIndex;
	}
	
	public void goToSettings() throws ClassNotFoundException, IOException{
		ScenePersonalSettingsController.getData(User.id);
		stag = (Stage) settings.getScene().getWindow();
		ScenePersonalSettingsController.getStage(stag);
		root = FXMLLoader.load(getClass().getClassLoader().getResource("PersonalSettings.fxml"));	
		stag.setScene(new Scene(root));
	}
	public void goToHistory() throws ClassNotFoundException, IOException{
		stag = (Stage) historie.getScene().getWindow();
		SceneHistorySettingsController.getStage(stag);
		root = FXMLLoader.load(getClass().getClassLoader().getResource("HistorieSettings.fxml"));
		stag.setScene(new Scene(root));
	}
	public void getCarManufactor() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CarManufactor.fxml"));		
		Parent root = loader.load();
		SceneGeneralSettingsCarManufactorController sceneGeneralSettingsCarManufactorController = loader.getController();
		sceneGeneralSettingsCarManufactorController.setSceneGeneralSettingsController(this);
		checkWindow.setScene(new Scene(root));
	    checkWindow.show();
	}
	public static void closeCheckWindow() throws IOException {
		checkWindow.hide();
	}
	
	public void getCarModell() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CarModell.fxml"));		
		Parent root = loader.load();
		SceneGeneralSettingsCarModellController sceneGeneralSettingsCarModellController = loader.getController();
		sceneGeneralSettingsCarModellController.setSceneGeneralSettingsController(this);
		checkWindow.setScene(new Scene(root));
	    checkWindow.show();
	}
	public void getCarParts() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CarPart.fxml"));		
		Parent root = loader.load();
		SceneGeneralSettingsCarPartController sceneGeneralSettingsCarPartController = loader.getController();
		sceneGeneralSettingsCarPartController.setSceneGeneralSettingsController(this);
		checkWindow.setScene(new Scene(root));
	    checkWindow.show();
	}
	public void getCarPartsManufactor() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CarPartManufactor.fxml"));		
		Parent root = loader.load();
		SceneGeneralSettingsCarPartManufactorController sceneGeneralSettingsCarPartManufactorController = loader.getController();
		sceneGeneralSettingsCarPartManufactorController.setSceneGeneralSettingsController(this);
		checkWindow.setScene(new Scene(root));
	    checkWindow.show();
	}
	public void getPoPo() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CarPartPosition.fxml"));		
		Parent root = loader.load();
		SceneGeneralSettingsPostitionController sceneGeneralSettingsPostitionController = loader.getController();
		sceneGeneralSettingsPostitionController.setSceneGeneralSettingsController(this);
		checkWindow.setScene(new Scene(root));
	    checkWindow.show();
	}
	public void getZustand() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Zustand.fxml"));		
		Parent root = loader.load();
		SceneGeneralSettingsZustandController sceneGeneralSettingsZustandController = loader.getController();
		sceneGeneralSettingsZustandController.setSceneGeneralSettingsController(this);
		checkWindow.setScene(new Scene(root));
	    checkWindow.show();
	}
	public void getProdukt() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("CarPartProduktart.fxml"));		
		Parent root = loader.load();
		SceneGeneralSettingsProduktartController sceneGeneralSettingsZustandController = loader.getController();
		sceneGeneralSettingsZustandController.setSceneGeneralSettingsController(this);
		checkWindow.setScene(new Scene(root));
	    checkWindow.show();
	}
	public void getFarbe() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Farbe.fxml"));		
		Parent root = loader.load();
		SceneGeneralSettingsFarbeController sceneGeneralSettingsZustandController = loader.getController();
		sceneGeneralSettingsZustandController.setSceneGeneralSettingsController(this);
		checkWindow.setScene(new Scene(root));
	    checkWindow.show();
	}
	public void goBack() throws IOException {
		stag = (Stage) abbrechen.getScene().getWindow();
		SceneOverviewController.stag = stag;
		root = FXMLLoader.load(getClass().getClassLoader().getResource("Overview.fxml"));
		stag.setScene(new Scene(root));
		closeCheckWindow();
	}

}
