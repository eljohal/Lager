package main.application;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MySQLDatenbankConnection {
	
	
	private static final Logger logger = LogManager.getLogger(MySQLDatenbankConnection.class);
	private static final String host = "localhost";
	private static final String port = "3306";
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
				logger.error("MySQLDatenbankConnection keine Verbindung, connect()", e);
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
				logger.error("MySQLDatenbankConnection kann sich nicht disconnecten, disconnect()", e);
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
			logger.error("MySQLDatenbankConnection update Database Error, update()", e);
			
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
	
	public static LocalDate getDate(String qry) {
		try {
			PreparedStatement ps = con.prepareStatement(qry);
			ps.execute();
			ResultSet st = ps.executeQuery();
			if (st.next()) {
				LocalDate date = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(st.getDate(1)));
			    return date;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return LocalDate.of(0000, 0, 0);
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
			return 0;
			
		}
	}
	
	public static float getRealFloat(String qry) {
		try {
			PreparedStatement ps = con.prepareStatement(qry);
			ps.execute();
			ResultSet st = ps.executeQuery();
			if (st.next()) {
				BigDecimal decimalValue = st.getBigDecimal(1);
			    return decimalValue.floatValue();
			}else {
				return 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return 0;
			
		}
	}
	
	public static BigDecimal getFloat(String qry) {
		try {
			PreparedStatement ps = con.prepareStatement(qry);
			ps.execute();
			ResultSet st = ps.executeQuery();
			if (st.next()) {
				BigDecimal decimalValue = st.getBigDecimal(1);
			    return decimalValue;
			}else {
				return new BigDecimal(0.0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return new BigDecimal(0.0);
			
		}
	}
	
	public static double getDouble(String qry) {
		try {
			PreparedStatement ps = con.prepareStatement(qry);
			ps.execute();
			ResultSet st = ps.executeQuery();
			if (st.next()) {
				return st.getDouble(1);
			    
			}else {
				return 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return 0;
			
		}
	}
	
	
	public static List<Integer> getIntList(String qry) {
		 List<Integer> charArrays = new ArrayList<>();

		try {
			PreparedStatement ps = con.prepareStatement(qry);
			ps.execute();
			ResultSet st = ps.executeQuery();
			while (st.next()) {
	            int intValue = st.getInt(1);
	            charArrays.add(intValue);
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		if (charArrays.isEmpty()) {
			charArrays.add(0);
		}
		return charArrays;
	}

	
	
	
	


}