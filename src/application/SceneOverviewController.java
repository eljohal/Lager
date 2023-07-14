package application;


import java.io.IOException;


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

public class SceneOverviewController {
	
	public static Stage stag;
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
	
	@FXML
	Button carparts;
	
	@FXML
	TextField filterField;
	
	Text ausgabeedit = new Text("Bitte wähle vorher ein Fahrzeug aus der Liste um es zu bearbeiten.");
	Text ausgabedelete = new Text("Bitte wähle vorher das Fahrzeug gewissenhaft was gelöscht werden soll!");
	
	static ObservableList<Car> cardata = FXCollections.observableArrayList();
	public static ObservableList<Car> getCarData(){
		
		
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
		caroverview.setRowFactory( tv -> {
			   TableRow<Car> row = new TableRow<>();
			   row.setOnMouseClicked(e -> {
			      if (e.getClickCount() == 2 && (!row.isEmpty()) ) {
			    	  SceneCarPartsController.setCarID(getSelectedCarID(caroverview.getSelectionModel().getSelectedItem()));
			          
			    	  try {
						root = FXMLLoader.load(getClass().getResource("CarPartsOverview.fxml"));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

			    	  stag = (Stage) carparts.getScene().getWindow();
			  		  stag.setScene(new Scene(root));
			          SceneCarPartsController.setCarID(getSelectedCarID(caroverview.getSelectionModel().getSelectedItem()));
			          cardata.clear();
			      }
			   });
			   return row;
			});
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
		caroverview.setItems(sortedData);
	}
	
	public TableView<Car> getTableView() {
		return caroverview;
	}
	
	public void createCarTest() throws IOException {
		root = FXMLLoader.load(getClass().getResource("CreateCar.fxml"));
		stag = (Stage) createcar.getScene().getWindow();
		stag.setScene(new Scene(root));
        cardata.clear();
	}
	
	public void openCarParts() throws IOException {
		if(caroverview.getSelectionModel().getSelectedItem() != null ) {
			Car ident = caroverview.getSelectionModel().getSelectedItem();
			SceneCarPartsController.setCarID(getSelectedCarID(ident));
			root = FXMLLoader.load(getClass().getResource("CarPartsOverview.fxml"));
			stag = (Stage) carparts.getScene().getWindow();
			stag.setScene(new Scene(root));
	        cardata.clear();
		}else {
			textshow.getChildren().clear();
			ausgabeedit.setFill(Color.BLACK);
			textshow.getChildren().addAll(ausgabeedit);
		}
	}
	
	public void editCar() throws IOException {
		if(caroverview.getSelectionModel().getSelectedItem() != null ) {
			Car ident = caroverview.getSelectionModel().getSelectedItem();
			SceneEditCarController.init(ident);
			root = FXMLLoader.load(getClass().getResource("EditCar.fxml"));
			stag = (Stage) createcar.getScene().getWindow();
			stag.setScene(new Scene(root));
			cardata.clear();
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
	
	
	public void init() {
		caroverview.refresh();
	}
	
	
	
	/* don`t need to remove cars
	public void deleteCar() throws IOException {
		//Stage childStage = new Stage();
		if(caroverview.getSelectionModel().getSelectedItem() != null ) {
			SceneDeleteController.setIDValue(getSelectedCarID(caroverview.getSelectionModel().getSelectedItem()), caroverview.getSelectionModel().getSelectedItem().getCarHersteller().getValue(), caroverview.getSelectionModel().getSelectedItem().getCarModell().getValue());
			//SceneDeleteController.ausgabe.setText("Möchtest du das Fahrzeug (" + getSelectedCarID(caroverview.getSelectionModel().getSelectedItem()) + ") " + caroverview.getSelectionModel().getSelectedItem().getCarHersteller().toString() + " " + caroverview.getSelectionModel().getSelectedItem().getCarModell().toString() + " wirklich löschen?");
			root = FXMLLoader.load(getClass().getResource("Delete.fxml"));
			stag = (Stage) createcar.getScene().getWindow();
			stag.setScene(new Scene(root));
			cardata.clear();
			
		}else {
			textshow.getChildren().clear();
			ausgabedelete.setFill(Color.BLACK);
			textshow.getChildren().addAll(ausgabedelete);
			
		}
	}
	*/
	
	
}
