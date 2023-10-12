package main.application;

import java.math.BigDecimal;

import javafx.scene.control.ListCell;

/* Vererbung (Ableitung) aus ListCell Klasse
 * Erweiterung (Override) von updateItem aus Cell um eigene Versionierungsvorlage von 0,00001 sauber anzuzeigen mithilfe von BigDecimal
 * 
 * 
 * */


public class ComboBoxListCell<T> extends ListCell<T> {

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
        } else {
            setText(doubleToString(item));
        }
    }

    private String doubleToString(T item) {
        if (item instanceof BigDecimal) {
        	BigDecimal value = (BigDecimal) item;
            return String.format("%.5f", value); // Formatieren von Double-Wert 
        }
        return "";
    }
}