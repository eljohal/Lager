package main.application;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class SceneGeneralSettingsCarPartManufactorController{
	private SceneGeneralSettingsController sceneGeneralSettingsController;
	public static Stage stag;
	public Scene scene;
	public Parent root;
	@FXML	
	public TableView<Settings> caroverview;
	@FXML
	TextField filterField;
	@FXML
	TableColumn<Settings, String> hersteller;
	@FXML
	Button deletecar;
	@FXML
	Button createcar;
	int z = 0;
	String value;
	int i_max;
	boolean newValue = false;
	ObservableList<Settings> carManufactor = FXCollections.observableArrayList(new ArrayList<>());
	private Set<String> possibleSuggestionsModell = new HashSet<>();
	public ObservableList<Settings> getCarData(){
		
		int id;
		z = 0;
		i_max = MySQLDatenbankConnection.getInt("SELECT MAX(MID) FROM `möglichepartshersteller`");

		carManufactor.add(new Settings(0, ""));
		for(int i = 1;i <= i_max; i++) {
			id = MySQLDatenbankConnection.getInt("SELECT `MID` FROM `möglichepartshersteller` WHERE `MID` = '" + i + "'");
			String value = MySQLDatenbankConnection.getString("SELECT `Manufactor` FROM `möglichepartshersteller` WHERE `MID` = '" + i + "'");
			if(id != 0 || i == 0) {
				carManufactor.add(new Settings(id, value));
				possibleSuggestionsModell.add(value);
			}
		}
		return carManufactor;
	}
	
    @FXML
    void initialize() {
        hersteller.setCellValueFactory(cellData -> cellData.getValue().getValue());
        hersteller.setCellFactory(TextFieldTableCell.forTableColumn());
        setSettingsList();
        hersteller.setOnEditCommit(event -> {
        	z=event.getTablePosition().getRow();
        	int pos = carManufactor.indexOf(event.getRowValue());
        	int y = carManufactor.get(pos).getSetID().get();
        	int max;
        	value = event.getNewValue();
        	String oldValue = event.getOldValue();
            if(value.equals("")) {
            	MySQLDatenbankConnection.update("DELETE FROM `möglichepartshersteller` WHERE `MID` = '"+y+"'");
            	carManufactor.remove(z);
            	possibleSuggestionsModell.remove(oldValue);
            }else if(!value.equals(oldValue)){
            	
            	if(pos == 0) {
            		if(!possibleSuggestionsModell.contains(value)) {
	           			max = MySQLDatenbankConnection.getInt("SELECT MIN(d1.MID)+1 FROM `möglichepartshersteller` d1 LEFT JOIN `möglichepartshersteller` d2 ON d1.MID + 1 = d2.MID WHERE d2.MID is NULL");
	           			
	            			MySQLDatenbankConnection.update("INSERT INTO `möglichepartshersteller`(`MID`, `Manufactor`) VALUES ('"+max+"','"+value+"')");
	               		carManufactor.add(max, new Settings(max, value));
	               		carManufactor.set(max, new Settings(max, value));
	           			possibleSuggestionsModell.add(value);
            		}
            	}else {
            			if(!possibleSuggestionsModell.contains(value)) {
	           			MySQLDatenbankConnection.update("UPDATE `möglichepartshersteller` SET `Manufactor`='"+value+"' WHERE `MID`='"+y+"'");
	           			carManufactor.set(pos, new Settings(pos, value));
	           			possibleSuggestionsModell.remove(oldValue);
	            			possibleSuggestionsModell.add(value);
            		}
            	}
            }
        });
        caroverview.setEditable(true);
		filter();
    }
    public void filter() {

		FilteredList<Settings> filteredData = new FilteredList<>(carManufactor, b -> true);
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
		SortedList<Settings> sortedData = new SortedList<>(filteredData);
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
    	SceneGeneralSettingsController.closeCheckWindow();
    }
    public SceneGeneralSettingsController getSceneGeneralSettingsController() {
		return sceneGeneralSettingsController;
	}

	public void setSceneGeneralSettingsController(SceneGeneralSettingsController sceneGeneralSettingsController) {
		this.sceneGeneralSettingsController = sceneGeneralSettingsController;
	}
}
