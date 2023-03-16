package application;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Car {
	private StringProperty modell;
	private StringProperty hersteller;
	private IntegerProperty carid;
	
	public Car(int carid, String herst, String modl) {
		this.carid = new SimpleIntegerProperty(carid);
		this.hersteller = new SimpleStringProperty(herst);
		this.modell = new SimpleStringProperty(modl); 
		
	}
	
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


