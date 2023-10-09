package main.application;

import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SceneCategorieSelectionController {
	private SceneAddCarPartController sceneAddCarPartController;
	private SceneRealEditCarPartController sceneEditCarPartController;
	public static Stage stag;
	public Scene scene;
	public Parent root;
	@FXML
    AnchorPane ap;
	
	@FXML
	public TableView<Categorie> caroverview;
	@FXML
	TableColumn<Categorie, Number> id;
	@FXML
	TableColumn<Categorie, String> l1;
	@FXML
	TableColumn<Categorie, String> l2;
	@FXML
	TableColumn<Categorie, String> l3;
	@FXML
	TableColumn<Categorie, String> l4;
	@FXML
	TableColumn<Categorie, String> l5;
	@FXML
	TableColumn<Categorie, String> l6;
	@FXML
	TableColumn<Categorie, Number> nummer;
	@FXML
	Button selectCatButton;
	@FXML
	TextField filterField;
	
	public static ObservableList<Categorie> catdata = FXCollections.observableArrayList();
	public static ObservableList<Categorie> getCateData(){
		catdata.clear();		
		int i_max = MySQLDatenbankConnection.getInt("SELECT MAX(CatID) FROM `csvcategories` WHERE 1");
		for(int i = 3;i <= i_max; i++) {
			String l1 = MySQLDatenbankConnection.getString("SELECT `L1` FROM `csvcategories` WHERE `CatID` = "+i+"");
			String l2 = MySQLDatenbankConnection.getString("SELECT `L2` FROM `csvcategories` WHERE `CatID` = "+i+"");
			String l3 = MySQLDatenbankConnection.getString("SELECT `L3` FROM `csvcategories` WHERE `CatID` = "+i+"");
			String l4 = MySQLDatenbankConnection.getString("SELECT `L4` FROM `csvcategories` WHERE `CatID` = "+i+"");
			String l5 = MySQLDatenbankConnection.getString("SELECT `L5` FROM `csvcategories` WHERE `CatID` = "+i+"");
			String l6 = MySQLDatenbankConnection.getString("SELECT `L6` FROM `csvcategories` WHERE `CatID` = "+i+"");
			int nummer = MySQLDatenbankConnection.getInt("SELECT `CategorieNr` FROM `csvcategories` WHERE `CatID` = "+i+"");
			addToObservableListIfNotNumber(catdata, l1, l2, l3, l4, l5, l6, i, nummer);
		}
		return catdata;
	}
	
	private static void addToObservableListIfNotNumber(ObservableList<Categorie> list, String l1, String l2, String l3, String l4, String l5, String l6, int ind, int cat) {
        int index;
        int minIndex;
        int done = 0;
		String[] possible = new String[6];
        possible[0] = l1;
        possible[1] = l2;
        possible[2] = l3;
        possible[3] = l4;
        possible[4] = l5;
        possible[5] = l6;
        for(int i = 0; i < possible.length; i++ ) {
        	try {
        		done = i;
            	index = Integer.parseInt(possible[i]);
            	minIndex = index;
            	index = index - 3;
            	if (minIndex >= 3 && index <= catdata.size()) {
            		switch(i) {
            		case 0:
            			possible[i] = catdata.get(index).getl1().getValue().toString();
            			break;
            		case 1:
            			possible[i] = catdata.get(index).getl2().getValue().toString();
            			break;
            		case 2:
            			possible[i] = catdata.get(index).getl3().getValue().toString();
            			break;
            		case 3:
            			possible[i] = catdata.get(index).getl4().getValue().toString();
            			break;
            		case 4:
            			possible[i] = catdata.get(index).getl5().getValue().toString();
            			break;
            		case 5:
            			possible[i] = catdata.get(index).getl6().getValue().toString();
            			break;
            		}
            	}
            }catch(NumberFormatException e) {
            	done = done + 1;
            	for(int x = done; x < possible.length; x++) {
            		possible[x] = "";
            	}
            }
        	
        }
        list.add(new Categorie(ind,  possible[0],  possible[1],  possible[2],  possible[3],  possible[4],  possible[5], cat));
    }
	@FXML 
	void initialize() {
		id.setCellValueFactory(cellData -> cellData.getValue().getCatID());
		l1.setCellValueFactory(cellData -> cellData.getValue().getl1());
		l2.setCellValueFactory(cellData -> cellData.getValue().getl2());
		l3.setCellValueFactory(cellData -> cellData.getValue().getl3());
		l4.setCellValueFactory(cellData -> cellData.getValue().getl4());
		l5.setCellValueFactory(cellData -> cellData.getValue().getl5());
		l6.setCellValueFactory(cellData -> cellData.getValue().getl6());
		nummer.setCellValueFactory(cellData -> cellData.getValue().getCatNr());
		setCateList();
		filter();
		caroverview.setRowFactory( tv -> {
			   TableRow<Categorie> row = new TableRow<>();
			   row.setOnMouseClicked(e -> {
			      if (e.getClickCount() == 2 && (!row.isEmpty()) ) {
			    	  try {
						done();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			  		}
			   });
			   return row;
			});
	}
	public void setSceneAddCarPartController(SceneAddCarPartController controller) {
        this.sceneAddCarPartController = controller;
    }
	public void setSceneEditCarPartController(SceneRealEditCarPartController controller) {
        this.sceneEditCarPartController = controller;
    }
	
	public void filter() {
		FilteredList<Categorie> filteredData = new FilteredList<>(catdata, b -> true);
		
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(categorie -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

	            String lowerCaseFilter = newValue.toLowerCase();
	            boolean isMatch = false;

	            if (categorie.getl1().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	                isMatch = true;
	            } else if (categorie.getl2().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	                isMatch = true;
	            } else if (categorie.getl6().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	                isMatch = true;
	            } else if (categorie.getl3().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	                isMatch = true;
	            } else if (categorie.getl4().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	                isMatch = true;
	            } else if (categorie.getl5().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	                isMatch = true;
	            } else if (categorie.getCatID().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	            	isMatch = true;
	            } else if (categorie.getCatNr().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
	            	isMatch = true;
	            }

	            return isMatch;
	        });
	    });
		SortedList<Categorie> sortedData = new SortedList<>(filteredData);
		caroverview.setItems(sortedData);
	}
	
	public void setCateList() {
		try {
			MySQLDatenbankConnection.connect("cardaten");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		caroverview.setItems(catdata);
		
	}
	public void done() throws IOException {
		if(caroverview.getSelectionModel().getSelectedItem() != null) {
			Categorie ident = caroverview.getSelectionModel().getSelectedItem();
			int selectedCatNr = ident.getCatNr().intValue();
			if(sceneAddCarPartController != null) {
				sceneAddCarPartController.setCatNr(selectedCatNr);
				sceneAddCarPartController.closeCheckWindow();
			}else if(sceneEditCarPartController != null) {
				sceneEditCarPartController.setCatNr(selectedCatNr);
				sceneEditCarPartController.closeCheckWindow();
			}
			
		}
		
	}
	
}
