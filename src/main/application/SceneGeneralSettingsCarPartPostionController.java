package main.application;


import java.io.IOException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import main.application.BooleanCheckBoxTableCell.CheckBoxChangeListener;

public class SceneGeneralSettingsCarPartPostionController implements CheckBoxChangeListener{

	private SceneGeneralSettingsCarPartController sceneGeneralSettingsController;
	public static Stage stag;
	public Scene scene;
	public Parent root;
	@FXML
	public TableView<CarPartPostion> caroverview;
	@FXML
	TextField filterField;
	@FXML
	Label carPartShow;
	@FXML
	TableColumn<CarPartPostion, String> hersteller;
	@FXML
	TableColumn<CarPartPostion, Boolean>set;
	@FXML
	Button deletecar;
	@FXML
	Button createcar;
	int z = 0;
	String value; 
	static int carPartID;
	static String carPartValue;
	int i_max;
	boolean newValue = false;
	ObservableList<CarPartPostion> carPartPosition = FXCollections.observableArrayList(new ArrayList<>());
	ObservableList<CarPartPostion> updateCarPartPosition = FXCollections.observableArrayList(new ArrayList<>());
	ObservableList<Settings> carManufactor = FXCollections.observableArrayList(new ArrayList<>());
	
	public ObservableList<CarPartPostion> getCarData(){
		
		int id;
		int exist;
		int vid;
		i_max = MySQLDatenbankConnection.getInt("SELECT MAX(PID) FROM `möglichepositionen`");
		carPartPosition.clear();
		updateCarPartPosition.clear();
		for(int i = 1;i <= i_max; i++) {
			id = MySQLDatenbankConnection.getInt("SELECT `PID` FROM `möglichepositionen` WHERE `PID` = " + i + "");
			String value = MySQLDatenbankConnection.getString("SELECT `Position` FROM `möglichepositionen` WHERE `PID` = '" + i + "'");
			exist = MySQLDatenbankConnection.getInt("SELECT COUNT(*) as c FROM `conteilepositionen` WHERE `TID` = '"+carPartID+"' AND PID = '"+id+"'");
			if(id != 0) {
				if(exist > 0) {
					vid = MySQLDatenbankConnection.getInt("SELECT `VID` FROM `conteilepositionen` WHERE `PID` = "+id+" AND `TID` = "+carPartID+" ");
					carPartPosition.add(new CarPartPostion(id, value, true,  vid));
					updateCarPartPosition.add(new CarPartPostion(id, value, true,  vid));
				}
				else {
					carPartPosition.add(new CarPartPostion(id, value, false, 0));				
					updateCarPartPosition.add(new CarPartPostion(id, value, false,  0));
				}
			}	
		}
		return carPartPosition;
	}
	public static void setCarPart(int id) {
		carPartID = id;
		carPartValue = MySQLDatenbankConnection.getString("SELECT `Part` FROM `möglicheteile` WHERE `TID` = "+carPartID+"");
	}
	@Override
    public void onCheckBoxChanged(int index, boolean newValue) {
        // Spezifische Verarbeitung für Checkbox-Änderungen in dieser Controller-Klasse
		CarPartPostion carPart = (CarPartPostion) carPartPosition.get(index);
        // Verarbeiten Sie die Änderung der Checkbox hier
        if (newValue && carPart.getSetVID().get() != 0 && carPart.getSetVID().get() != -1) {
            // Checkbox wurde aktiviert und VID ist nicht null
            updateCarPartPosition.set(index, new CarPartPostion(index,updateCarPartPosition.get(index).getValue().get(),newValue,updateCarPartPosition.get(index).getSetVID().get()));
        } else if (newValue && carPart.getSetVID().get() == 0) {
            // Checkbox wurde aktiviert, aber VID ist null
            updateCarPartPosition.set(index, new CarPartPostion(index,updateCarPartPosition.get(index).getValue().get(),newValue,-1));
        } else if (!newValue && carPart.getSetVID().get() != 0) {
            // Checkbox wurde deaktiviert und VID ist nicht null
            updateCarPartPosition.set(index, new CarPartPostion(index,updateCarPartPosition.get(index).getValue().get(),false,updateCarPartPosition.get(index).getSetVID().get()));
        }
    }
    @FXML
    void initialize() {
        set.setCellFactory(col -> new BooleanCheckBoxTableCell<>(this));
        set.setCellValueFactory(cellData -> cellData.getValue().getSelected());
        hersteller.setCellValueFactory(cellData -> cellData.getValue().getValue());
        hersteller.setCellFactory(TextFieldTableCell.forTableColumn());
        carPartShow.setText(" Fahrzeugteil: "+carPartValue);       
        setSettingsList();
        caroverview.setEditable(true);
		filter();
    }
    public void filter() {
		FilteredList<CarPartPostion> filteredData = new FilteredList<>(carPartPosition, b -> true);
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(car -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				String lowerCaseFilter = newValue.toLowerCase();
				
				if(car.getValue().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}else {
					return false;
				}
			});
		});
		SortedList<CarPartPostion> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(caroverview.comparatorProperty());
		caroverview.setItems(sortedData);
	}
    public void setSettingsList() {
    	try {
			MySQLDatenbankConnection.connect("cardaten");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		caroverview.setItems(getCarData());
    }
    
    public void createCarTest() throws IOException {
    	for(int i = 0;i < updateCarPartPosition.size(); i++) {
    		
    		if(updateCarPartPosition.get(i).getSetVID().get() == -1 && updateCarPartPosition.get(i).getSelected().get()) {
    			int max = MySQLDatenbankConnection.getInt("SELECT MIN(d1.VID)+1 FROM `conteilepositionen` d1 LEFT JOIN `conteilepositionen` d2 ON d1.VID + 1 = d2.VID WHERE d2.VID is NULL");
    			int x = MySQLDatenbankConnection.getInt("SELECT `PID` FROM `möglichepositionen` WHERE `Position` = '"+updateCarPartPosition.get(i).getValue().get()+"'");
    			MySQLDatenbankConnection.update("INSERT INTO `conteilepositionen`(`TID`, `PID`, `VID`) VALUES ('"+carPartID+"','"+x+"','"+max+"')");
    		}else if((updateCarPartPosition.get(i).getSetVID().get() != -1 && updateCarPartPosition.get(i).getSetVID().get() != 0) && !updateCarPartPosition.get(i).getSelected().get()) {
    			MySQLDatenbankConnection.update("DELETE FROM `conteilepositionen` WHERE `VID` = '"+updateCarPartPosition.get(i).getSetVID().get()+"'");
    			
    		}
    	}
    	SceneGeneralSettingsCarPartController.closeCheckWindow();
    }

	public SceneGeneralSettingsCarPartController getSceneGeneralSettingsController() {
		return sceneGeneralSettingsController;
	}

	public void setSceneGeneralSettingsController(SceneGeneralSettingsCarPartController sceneGeneralSettingsController) {
		this.sceneGeneralSettingsController = sceneGeneralSettingsController;
	}
}
