package application;


import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class SceneOverviewController {
	
	public Stage stag;
	public Scene scene;
	public Parent root;
	
	@FXML
	public TableView<Car> caroverview;
	
	@FXML
	TableColumn<Car, Number> fznm;
	
	@FXML
	TableColumn<Car, String> hersteller;
	
	@FXML
	TableColumn<Car, String> modell;
	
	@FXML 
	Button createcar;
	
	@FXML
	Button deletecar;
	
	@FXML
	TextFlow textshow;
	
	Text ausgabeedit = new Text("Bitte wähle vorher ein Fahrzeug aus der Liste um es zu bearbeiten.");
	Text ausgabedelete = new Text("Bitte wähle vorher das Fahrzeug gewissenhaft was gelscht werden soll!");
	
	public static ObservableList<Car> getCarData(){
		
		ObservableList<Car> cardata = FXCollections.observableArrayList();
		int cid = MySQLDatenbankConnection.getInt("SELECT `CarID` FROM `cardata` WHERE 1");
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(CarID) FROM `cardata` WHERE 1");
		for(int i = 1;i <= i_max; i++) {
			cid = MySQLDatenbankConnection.getInt("SELECT `CarID` FROM `cardata` WHERE `CarID` = '" + i + "'");
			String herst = MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `cardata` WHERE `CarID` = '" + i + "'");
			String modl = MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = " + i + "");
			cardata.add(new Car(cid,herst,modl));
		}				
		return cardata;
	}
	
	@FXML void initialize() {
		fznm.setCellValueFactory(cellData -> cellData.getValue().getCarID());
		hersteller.setCellValueFactory(cellData -> cellData.getValue().getCarHersteller());
		modell.setCellValueFactory(cellData -> cellData.getValue().getCarModell());
		
		setCarList();
	}
	
	
	public TableView<Car> getTableView() {
		return caroverview;
	}
	
	public void createCarTest() throws IOException {
		root = FXMLLoader.load(getClass().getResource("CreateCar.fxml"));
		stag = (Stage) createcar.getScene().getWindow();
		stag.setScene(new Scene(root));
		System.out.print(User.id);
	}
	
	public void editCar() throws IOException {
		if(caroverview.getSelectionModel().getSelectedItem() != null ) {
			Car ident = caroverview.getSelectionModel().getSelectedItem();
			root = FXMLLoader.load(getClass().getResource("EditCar.fxml"));
			stag = (Stage) createcar.getScene().getWindow();
			stag.setScene(new Scene(root));
			//function set all in next scene
			System.out.print(ident.getCarID().intValue());
		}else {
			textshow.getChildren().clear();
			ausgabeedit.setFill(Color.BLACK);
			textshow.getChildren().addAll(ausgabeedit);
			
		}
	}
	public void setCarList() {
		try {
			MySQLDatenbankConnection.connect("cardaten");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		caroverview.setItems(getCarData());
		
	}
	
	public int getSelectedCarID(Car delCar) {
		Car deleteCar = delCar;
		int id = deleteCar.getCarID().intValue();
		return id;
	}	
	
	
	public void init() {
		caroverview.refresh();
	}
	
	public void deleteCar() throws IOException {
		//Stage childStage = new Stage();
		if(caroverview.getSelectionModel().getSelectedItem() != null ) {
			SceneDeleteController.setIDValue(getSelectedCarID(caroverview.getSelectionModel().getSelectedItem()), caroverview.getSelectionModel().getSelectedItem().getCarHersteller().getValue(), caroverview.getSelectionModel().getSelectedItem().getCarModell().getValue());
			//SceneDeleteController.ausgabe.setText("Möchtest du das Fahrzeug (" + getSelectedCarID(caroverview.getSelectionModel().getSelectedItem()) + ") " + caroverview.getSelectionModel().getSelectedItem().getCarHersteller().toString() + " " + caroverview.getSelectionModel().getSelectedItem().getCarModell().toString() + " wirklich löschen?");
			root = FXMLLoader.load(getClass().getResource("Delete.fxml"));
			stag = (Stage) createcar.getScene().getWindow();
			stag.setScene(new Scene(root));
			
		}else {
			textshow.getChildren().clear();
			ausgabedelete.setFill(Color.BLACK);
			textshow.getChildren().addAll(ausgabedelete);
			
		}
	}
	
	
}
