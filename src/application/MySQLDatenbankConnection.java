package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLDatenbankConnection {
	
	private static final String host = "localhost";
	private static final String port = "3306";
	//private static final String database = "userdaten";
	private static final String username = "root";
	private static final String password = "";
	
	private static Connection con;
	
	public static boolean isConnected() {
		return(con == null ? false : true);
	}
	
	public static void connect(String database) throws ClassNotFoundException {
		if(!isConnected()) {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try {
				con =  DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void disconnect() {
		if(isConnected()) {
			try {
				con.close();
				con = null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static boolean update(String qry) {
		try {			
			PreparedStatement ps = con.prepareStatement(qry);
			ps.execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return false;
	}
	
	public static String getString(String qry) {
		try {
			PreparedStatement ps = con.prepareStatement(qry);
			ps.execute();
			ResultSet st = ps.executeQuery();
			if (st.next()) {
			    return st.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return "Error";
	}
	
	public static int getInt(String qry) {
		try {
			PreparedStatement ps = con.prepareStatement(qry);
			ps.execute();
			ResultSet st = ps.executeQuery();
			if (st.next()) {
			    return st.getInt(1);
			}else {
				return 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return 10;
			
		}
	}
	
	
	
	
	public static char[] getCharArray(String qry) {
		
		try {
			PreparedStatement ps = con.prepareStatement(qry);
			ps.execute();
			ResultSet st = ps.executeQuery();
			if (st.next()) {
			    return (st.getString(2)).toCharArray();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		char[] JavaCharArray = {'E','R','R','O','R'};
		return JavaCharArray;
	}
	
	
	
	


}