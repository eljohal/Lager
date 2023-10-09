package main.application;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CarPartPostion {
	//Instanzvariablen
	private IntegerProperty id;
	private StringProperty value;
	private BooleanProperty selected;
	private IntegerProperty vID;
	//Konstruktor
	public CarPartPostion(int id, String value, boolean selected, int vID) {
		this.id = new SimpleIntegerProperty(id);
		this.value = new SimpleStringProperty(value);
		this.selected = new SimpleBooleanProperty(selected);
		this.vID = new SimpleIntegerProperty(vID);
	}
	//Getter und Setter Methoden
	public IntegerProperty getSetID() {
		return id;
	}
	public IntegerProperty getSetVID() {
		return vID;
	}
	public StringProperty getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = new SimpleStringProperty(value);
	}
	public void setSetID(int id) {
		this.id = new SimpleIntegerProperty(id);
	}
	public void setSetVID(int vID) {
		this.vID = new SimpleIntegerProperty(vID);
	}
	public void setSelected(boolean selected) {
		this.selected = new SimpleBooleanProperty(selected);
	}
	public BooleanProperty getSelected() {
		return selected;
	}
}
