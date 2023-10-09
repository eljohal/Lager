package main.application;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CategorieFind {
	//Instanzvariablen
	private IntegerProperty id;
	private StringProperty value;
	//Konstruktor
	public CategorieFind(int id, String value) {
		this.id = new SimpleIntegerProperty(id);
		this.value = new SimpleStringProperty(value);
	}
	//Getter
	public IntegerProperty getCatID() {
		return id;
	}
	public StringProperty getValue() {
		return value;
	}
}
