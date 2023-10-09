package main.application;

public class connectionProduktIDspezifischeID {
	//Objekt für sauberen Code :D und projezierung von Datenbanktabelle
	//Verbindung von Produktart ID und spezifische Autoteile ID (AutoID-AutoteileID)
	private int firstNumber;
    private int secondNumber;
    private int thirdNumber;

    public connectionProduktIDspezifischeID(int firstNumber, int secondNumber, int thirdNumber) {
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
        this.thirdNumber = thirdNumber;
    }

    public int getFirstNumber() {
        return firstNumber;
    }

    public int getSecondNumber() {
        return secondNumber;
    }

    public int getThirdNumber() {
        return thirdNumber;
    }
}
