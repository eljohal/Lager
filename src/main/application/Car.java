package main.application;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*use of javafx property for better handling of the data
 * 
 * Data Binding:
 * 			erleichterte Aktualisierung
 * Event Handling und Bequeme Verwendung in TableView und ListView
 * 
 * 
 * 
 * */

public class Car {
	//Instanzvariablen
	private StringProperty modell;
	private StringProperty hersteller;
	private IntegerProperty carid;
	
	//Konstruktor
	public Car(int carid, String herst, String modl) {
		this.carid = new SimpleIntegerProperty(carid);
		this.hersteller = new SimpleStringProperty(herst);
		this.modell = new SimpleStringProperty(modl); 
		
	}
	
	
	//Getter Methoden (Datenkapselung)
	public StringProperty getCarModell() {
		return modell;
	}
	
	public StringProperty getCarHersteller() {
		return hersteller;
	}
	
	public IntegerProperty getCarID() {
		return carid;
	}
}


