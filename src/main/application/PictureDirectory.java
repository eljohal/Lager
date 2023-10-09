package main.application;

public class PictureDirectory {
	private static String dir;
	private static int picDir = 0;
	
	PictureDirectory (String dir) {
		PictureDirectory.dir = dir;
	}
	
	public static String getDir() {
		return dir;
	}
	
	public static void setDir() {
		dir = MySQLDatenbankConnection.getString("SELECT `Verzeichnis` FROM `programdata` WHERE `ID` = "+picDir+"");
	}
	
}
