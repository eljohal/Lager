package main.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.opencsv.exceptions.CsvException;

public class VersionCheckerThread{
	
	SceneRealEditCarPartController sceneCarPartsController;
	SceneEditCarController sceneEditCarController;
	SceneCarPartsController sceneCarPartController;
	SceneOverviewController sceneCarController;
	float currentVersion; //Version die aktuell in DB liegt
	float editVersion; //Version die bearbeitet wird
	int initCount;
	int possibleChangedCount;
	String controller;
	String status;
	int carPartID;
	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	ScheduledExecutorService init = Executors.newScheduledThreadPool(1);
	ScheduledExecutorService init1 = Executors.newScheduledThreadPool(1);
	boolean inti1Successfully = false;
	ScheduledExecutorService carPartsChanges = Executors.newScheduledThreadPool(1);
	File file;
	boolean shutdown = false;
	
	public void setEditVersion(String status) {
		this.status = status;
		editVersion = MySQLDatenbankConnection.getRealFloat(status);
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public void setCurrentVersion(String status) {
		currentVersion = MySQLDatenbankConnection.getRealFloat(status);
	}
	
	public void setInitCount(String status) {
		this.status = status;
		initCount = MySQLDatenbankConnection.getInt(status);
	}
	
	public void setPossibleChangedCount(String status) {
		this.status = status;
		possibleChangedCount = MySQLDatenbankConnection.getInt(status);
	}
	
	public void sceneGeneralSettings(SceneRealEditCarPartController controller) {
		sceneCarPartsController = controller;
	}
	
	public void sceneEditCar(SceneEditCarController controller) {
		sceneEditCarController = controller;
	}
	
	public void sceneCar(SceneOverviewController controller) {
		sceneCarController = controller;
	}
	
	public void sceneCarParts(SceneCarPartsController controller) {
		sceneCarPartController = controller;
	}
	
	public void setStatusString(String status) {
		this.status = status;
	}
	
    public void pauseThread() {
        executor.shutdownNow(); // Beenden Sie den Executor vorübergehend
    }

    public void startThread() {
        executor = Executors.newScheduledThreadPool(1); // Erstellen Sie einen neuen Executor
        start(); // Starten Sie den Executor erneut
    }
    
    public void startInitThread() {
    	init = Executors.newScheduledThreadPool(1);
    	startInit();
    }
    
    public void startInit1Thread() {
    	init1 = Executors.newScheduledThreadPool(1);
    	startInit1();
    }
    
    public boolean getInit1Started() {
    	return inti1Successfully;
    }
    
    public void startCarPartsThread() {
    	carPartsChanges = Executors.newScheduledThreadPool(1);
    	startCarPartsChanges();
    }
    
    public void pauseCarPartsChangesThread() {
        carPartsChanges.shutdownNow(); // Beenden Sie den Executor vorübergehend
    }
    
    public void startInit() {
    	init.execute(myTask);
    }
    
    public void startInit1() {
    	init1.execute(myTask1);
    	inti1Successfully = true;
    }
	
    Runnable myTask = () -> {
        // Hier können Sie Ihre Funktion ausführen
        SceneCategorieSelectionController.getCateData();
        init.shutdown();
    };
    
    Runnable myTask1 = () -> {
        // Hier können Sie Ihre Funktion ausführen
    	try {
			SceneGeneralSettingsController.addData(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        init1.shutdown();
    	inti1Successfully = false;
    };
    
    public boolean initIsFinished() {
    	return init.isShutdown();
    }
    
    public boolean init1IsFinished() {
    	return init1.isShutdown();
    }
    
	public void startCarPartsChanges() {
	    // Führen Sie die Aufgabe alle 5 Sekunden aus (passen Sie das Intervall nach Bedarf an)
		shutdown = false;
		carPartsChanges.scheduleAtFixedRate(() -> {
	    		setPossibleChangedCount(status);
				if(initCount != possibleChangedCount) {
					if(controller.equals("SceneCarPartsController")) {
						//stopThread();
						sceneCarPartController.versionErrorChangesCount();
					}else if(controller.equals("SceneOverviewController")) {
						sceneCarController.versionErrorChangesCount();
					}
			}
			
			
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	public void start() {
	    // Führen Sie die Aufgabe alle 5 Sekunden aus (passen Sie das Intervall nach Bedarf an)
		shutdown = false;
		executor.scheduleAtFixedRate(() -> {
	    		setCurrentVersion(status);
				if(currentVersion != editVersion) {
					if(controller.equals("SceneRealEditCarPartController")) {
						
						sceneCarPartsController.versionError();
					}else if(controller.equals("SceneEditCarController")) {
						sceneEditCarController.versionError();
					}
			}
			
			 
		}, 0, 1, TimeUnit.SECONDS);
	}
	
	public void setCarPartController(String controller) {
		this.controller = controller;
	}
	
	public void setCarPartID(int carPartID) {
		this.carPartID = carPartID;
	}
	
}
