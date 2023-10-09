package main.application; 

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Categorie {
	//Instanzvariablen
	private IntegerProperty id;
	private StringProperty l1;
	private StringProperty l2;
	private StringProperty l3;
	private StringProperty l4;
	private StringProperty l5;
	private StringProperty l6;
	private IntegerProperty categorieNr;
	//Konstruktor
	public Categorie(int id, String l1, String l2, String l3, String l4, String l5, String l6, int catNr) {
		this.id = new SimpleIntegerProperty(id);
		this.l1 = new SimpleStringProperty(l1);
		this.l2 = new SimpleStringProperty(l2);
		this.l3 = new SimpleStringProperty(l3);
		this.l4 = new SimpleStringProperty(l4);
		this.l5 = new SimpleStringProperty(l5);
		this.l6 = new SimpleStringProperty(l6);
		this.categorieNr = new SimpleIntegerProperty(catNr);
	}
	//Getter und Setter
	public IntegerProperty getCatID() {
		return id;
	}
	public IntegerProperty getCatNr() {
		return categorieNr;
	}
	public StringProperty getl1() {
		return l1;
	}
	public StringProperty getl2() {
		return l2;
	}
	public StringProperty getl3() {
		return l3;
	}
	public StringProperty getl4() {
		return l4;
	}
	public StringProperty getl5() {
		return l5;
	}
	public StringProperty getl6() {
		return l6;
	}
	public void setCatID(int id) {
        this.id.set(id);
    }

    public void setCatNr(int catNr) {
        this.categorieNr.set(catNr);
    }

    public void setl1(String l1) {
        this.l1.set(l1);
    }

    public void setl2(String l2) {
        this.l2.set(l2);
    }

    public void setl3(String l3) {
        this.l3.set(l3);
    }

    public void setl4(String l4) {
        this.l4.set(l4);
    }

    public void setl5(String l5) {
        this.l5.set(l5);
    }

    public void setl6(String l6) {
        this.l6.set(l6);
    }
	
}
