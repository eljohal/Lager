package main.application;


import java.io.IOException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SceneOverviewController {
	
	public static Stage stag;
	public static Stage oriStag;
	public Scene scene;
	public Parent root;
	
	static VersionCheckerThread checkChanges = new VersionCheckerThread();
	
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
	
	@FXML
	Button carparts;
	@FXML
	Button settings;
	
	@FXML
	TextField filterField;
	
	Text ausgabeedit = new Text("Bitte wähle vorher ein Fahrzeug aus der Liste um es zu bearbeiten.");
	Text ausgabedelete = new Text("Bitte wähle vorher gewissenhaft welches Fahrzeug gelöscht werden soll!");
	
	static ObservableList<Car> cardata = FXCollections.observableArrayList();
	public static ObservableList<Car> getCarData(){
		cardata.clear();
		
		int cid = MySQLDatenbankConnection.getInt("SELECT `CarID` FROM `cardata` WHERE 1");
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(CarID) FROM `cardata` WHERE 1");
		for(int i = 129;i <= i_max; i++) {
			cid = MySQLDatenbankConnection.getInt("SELECT `CarID` FROM `cardata` WHERE `CarID` = '" + i + "'");
			String herst = MySQLDatenbankConnection.getString("SELECT `Hersteller` FROM `cardata` WHERE `CarID` = '" + i + "'");
			String modl = MySQLDatenbankConnection.getString("SELECT `Modell` FROM `cardata` WHERE `CarID` = " + i + "");
			if(cid != 0)
				cardata.add(new Car(cid,herst,modl));
		}				
		return cardata;
	}
	
	@FXML 
	void initialize() {
		checkChanges.sceneCar(this);
		startThread();
		fznm.setCellValueFactory(cellData -> cellData.getValue().getCarID());
		hersteller.setCellValueFactory(cellData -> cellData.getValue().getCarHersteller());
		modell.setCellValueFactory(cellData -> cellData.getValue().getCarModell());
		stag.setOnCloseRequest((WindowEvent event) -> {
            // Show a confirmation dialog
			
			checkChanges.pauseCarPartsChangesThread();
			if(checkChanges.carPartsChanges.isShutdown()) {
				System.out.print("Test");
				Platform.exit();
				System.exit(0);
			}
        });
		caroverview.setRowFactory( tv -> {
			   TableRow<Car> row = new TableRow<>();
			   row.setOnMouseClicked(e -> {
			      if (e.getClickCount() == 2 && (!row.isEmpty()) ) {

			  		checkChanges.pauseCarPartsChangesThread();
			  		if(checkChanges.carPartsChanges.isShutdown()) {
			    	  SceneCarPartsController.setCarID(getSelectedCarID(caroverview.getSelectionModel().getSelectedItem()));
			    	  stag = (Stage) carparts.getScene().getWindow();
			    	  SceneCarPartsController.getStage(stag);
			    	  try {
						root = FXMLLoader.load(getClass().getClassLoader().getResource("CarPartsOverview.fxml"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			    	  
			  		  stag.setScene(new Scene(root));
			          SceneCarPartsController.setCarID(getSelectedCarID(caroverview.getSelectionModel().getSelectedItem()));
			          cardata.clear();
			  		}
			      }
			   });
			   return row;
			});
		caroverview.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		setCarList();
		filter();
	}
	
	public void filter() {

		FilteredList<Car> filteredData = new FilteredList<>(cardata, b -> true);
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(car -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				String lowerCaseFilter = newValue.toLowerCase();
				
				if(car.getCarHersteller().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}else if (car.getCarID().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}else if (car.getCarModell().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true;
				}else {
					return false;
				}
			});
		});
		SortedList<Car> sortedData = new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(caroverview.comparatorProperty());
		caroverview.setItems(sortedData);
	}
	
	public TableView<Car> getTableView() {
		return caroverview;
	}
	
	public void startThread() {
		checkChanges.setInitCount("SELECT COUNT(*) FROM `cardata` WHERE 1");
		checkChanges.setCarPartController("SceneOverviewController");
		checkChanges.startCarPartsThread();
	}
	
	public void versionErrorChangesCount() {
		checkChanges.pauseCarPartsChangesThread();
		if(checkChanges.carPartsChanges.isShutdown()) {
			cardata.clear();
			caroverview.refresh();
			setCarList();
			startThread();
		}	
	}
	
	public void createCarTest() throws IOException {
		checkChanges.pauseCarPartsChangesThread();
		if(checkChanges.carPartsChanges.isShutdown()) {
			stag = (Stage) createcar.getScene().getWindow();
			SceneCreateCarController.getStage(stag);
			root = FXMLLoader.load(getClass().getClassLoader().getResource("CreateCar.fxml"));
			
			stag.setScene(new Scene(root));
			
	        cardata.clear();
		}
	}
	public void openCarParts() throws IOException {
		if(caroverview.getSelectionModel().getSelectedItem() != null ) {
			checkChanges.pauseCarPartsChangesThread();
			if(checkChanges.carPartsChanges.isShutdown()) {
				Car ident = caroverview.getSelectionModel().getSelectedItem();
				SceneCarPartsController.setCarID(getSelectedCarID(ident));
				stag = (Stage) carparts.getScene().getWindow();
				SceneCarPartsController.getStage(stag);
				root = FXMLLoader.load(getClass().getClassLoader().getResource("CarPartsOverview.fxml"));			
				stag.setScene(new Scene(root));
		        cardata.clear();
			}
		}else {
			textshow.getChildren().clear();
			ausgabeedit.setFill(Color.BLACK);
			textshow.getChildren().addAll(ausgabeedit);
		}
	}
	
	public void editCar(){
		if(caroverview.getSelectionModel().getSelectedItem() != null ) {
			Platform.runLater(() -> {
				try {
				checkChanges.pauseCarPartsChangesThread();
				if(checkChanges.carPartsChanges.isShutdown()) {
					Car ident = caroverview.getSelectionModel().getSelectedItem();
					int carid = ident.getCarID().get();
					SceneEditCarController.init(carid);
					stag = (Stage) createcar.getScene().getWindow();
					SceneEditCarController.getStage(stag);
					root = FXMLLoader.load(getClass().getClassLoader().getResource("EditCar.fxml"));
					stag.setScene(new Scene(root));
					cardata.clear();
				}
				}catch(IOException e) {
					e.printStackTrace();
				}
			});
		}else {
			textshow.getChildren().clear();
			ausgabeedit.setFill(Color.BLACK);
			textshow.getChildren().addAll(ausgabeedit);
			
		}
	}
	
	public static void closeStag() {
		stag.close();
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
	
	public void settings() throws IOException, ClassNotFoundException {
		checkChanges.pauseCarPartsChangesThread();
		if(checkChanges.carPartsChanges.isShutdown()) {
			ScenePersonalSettingsController.getData(User.id);
			ScenePersonalSettingsController.getStage(stag);
			stag = (Stage) settings.getScene().getWindow();
			root = FXMLLoader.load(getClass().getClassLoader().getResource("PersonalSettings.fxml"));
			stag.setScene(new Scene(root));
			cardata.clear();
		}
	} 
	
	
	public void init() {
		caroverview.refresh();
	}
	
	
	

	public void deleteCar() throws IOException {
		//Stage childStage = new Stage();
		if(caroverview.getSelectionModel().getSelectedItem() != null ) {
			checkChanges.pauseCarPartsChangesThread();
			if(checkChanges.carPartsChanges.isShutdown()) {
				SceneDeleteController.setIDValue(getSelectedCarID(caroverview.getSelectionModel().getSelectedItem()), caroverview.getSelectionModel().getSelectedItem().getCarHersteller().getValue(), caroverview.getSelectionModel().getSelectedItem().getCarModell().getValue());
				stag = (Stage) createcar.getScene().getWindow();
				SceneDeleteController.getStage(stag);
				root = FXMLLoader.load(getClass().getClassLoader().getResource("Delete.fxml"));
				stag.setScene(new Scene(root));
				cardata.clear();
			}
		}else {
			textshow.getChildren().clear();
			ausgabedelete.setFill(Color.BLACK);
			textshow.getChildren().addAll(ausgabedelete);			
		}
	}
	
	
	
}
