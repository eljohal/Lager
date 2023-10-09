package main.application;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;

/*Klasse, mit welcher in Anwendung in bestimmter Klasse Boolean Checkboxes in einer Zelle von einer TableView mit einem Listener genutzt werden können
 * Example:
 * 	
 * use Class:
 * 	class CLASSNAME implements CheckBoxChangeListener
 * 
 * 	Verwendung von CheckBoxChangeListener als Interface um Änderungen der Checkbox Zelle klassenspezifisch zu bearbeiten
 * 
 * 		@Override
 * 		_ _ onCkeckBoxChanged(int index, boolean value){
 * 		handle hier die Änderungen in der Checkbox Zelle
 * 		}
 * 
 * 		tableview.setCellFactory(col -> new BooleanCheckBoxTableCell<>(this)
 * 		this is a reference to the actual instance of the class who uses this class, to get the Listener from that class
 * 
 * */

public class BooleanCheckBoxTableCell<S, T> extends TableCell<S, Boolean> {

    private final CheckBox checkBox;
    private Boolean previousValue;
    private final CheckBoxChangeListener listener;
    
    public interface CheckBoxChangeListener {
        void onCheckBoxChanged(int index, boolean newValue);
    }
    
    public BooleanCheckBoxTableCell(CheckBoxChangeListener listener) {
        checkBox = new CheckBox();
        setGraphic(checkBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        previousValue = null;
        this.listener = listener;
    }
    
    
    
    @Override
    protected void updateItem(Boolean item, boolean empty) {
    	//extend use of function updateItem from superclass TableCell, but clearly
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            checkBox.setSelected(item);
            setGraphic(checkBox);
            // Hier wird der Listener für die Checkbox erstellt
            checkBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            	if (!empty && !newValue.equals(previousValue)) {
                    // Objekt der aktuellen Zeile
            		listener.onCheckBoxChanged(getIndex(), newValue);
                }
            });
        }
    }
}