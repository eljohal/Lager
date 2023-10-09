package main.application; 

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CarParts {
	//Instanzvariablen
	private StringProperty artikelbezeichnung;
	private StringProperty herteller;
	private StringProperty zustand;
	private StringProperty oriTeilNR;
	private IntegerProperty artikelnummer;
	private DoubleProperty preis;
	//Konstruktor
	public CarParts(int carpartsid, String artikelbez, String zstd, String herst, String oriTeil, double pre) {
		this.artikelbezeichnung = new SimpleStringProperty(artikelbez);
		this.zustand = new SimpleStringProperty(zstd);
		this.herteller = new SimpleStringProperty(herst);
		this.artikelnummer = new SimpleIntegerProperty(carpartsid);
		this.oriTeilNR = new SimpleStringProperty(oriTeil);
		this.preis = new SimpleDoubleProperty(pre);
	}
	//Getter
	public StringProperty getCarPartsName() {
		return artikelbezeichnung;
	}
	
	public StringProperty getCarPartsZustand() {
		return zustand;
	}
	public StringProperty getCarPartsHersteller() {
		return herteller;
	}
	public StringProperty getCarPartsOriTeilNr() {
		return oriTeilNR;
	}
	
	public IntegerProperty getCarPartsID() {
		return artikelnummer;
	}
	public DoubleProperty getCarPartsPreis() {
		return preis;
	}
}
