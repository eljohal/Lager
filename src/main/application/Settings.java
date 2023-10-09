package main.application;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Settings {
	private IntegerProperty id;
	private StringProperty value;
	
	public Settings(int id, String value) {
		this.id = new SimpleIntegerProperty(id);
		this.value = new SimpleStringProperty(value);
	}
	public IntegerProperty getSetID() {
		return id;
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
}
