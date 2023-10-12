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
	private StringProperty versand;
	private IntegerProperty artikelnummer;
	private IntegerProperty cpid;
	private DoubleProperty preis;
	private IntegerProperty menge;
	//Konstruktor
	public CarParts(int carpartsid, int cpid, String artikelbez, String zstd, String herst, String oriTeil, double pre, String versand,int menge) {
		this.artikelbezeichnung = new SimpleStringProperty(artikelbez);
		this.zustand = new SimpleStringProperty(zstd);
		this.herteller = new SimpleStringProperty(herst);
		this.artikelnummer = new SimpleIntegerProperty(carpartsid);
		this.cpid = new SimpleIntegerProperty(cpid);
		this.oriTeilNR = new SimpleStringProperty(oriTeil);
		this.preis = new SimpleDoubleProperty(pre);
		this.versand = new SimpleStringProperty(versand);
		this.menge = new SimpleIntegerProperty(menge);
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
	
	public StringProperty getCarPartsVersand() {
		return versand;
	}
	
	public IntegerProperty getCarPartsID() {
		return artikelnummer;
	}
	
	public IntegerProperty getCarPartsMenge() {
		return menge;
	}
	
	public IntegerProperty getCarPartsCPID() {
		return cpid;
	}
	
	public DoubleProperty getCarPartsPreis() {
		return preis;
	}
}
