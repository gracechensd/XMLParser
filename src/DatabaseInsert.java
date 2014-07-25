import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;


public class DatabaseInsert {
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/metadata";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";

	public static void main(String[] argv) {

		try {
			insertRecordIntoDbUserTable();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void insertRecordIntoDbUserTable() throws SQLException {

		Connection dbConnection = null;
		Statement statement = null;

		try {
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();
			
			ArrayList<String> list = new ArrayList<String>();
			list.add("demo.xml");
			list.add("keyword");
			String insertTableSQL = new String (generateRows(list));

			System.out.println(insertTableSQL);

			// execute insert SQL statement
			statement.executeUpdate(insertTableSQL, Statement.RETURN_GENERATED_KEYS);

			System.out.println("Records are inserted into Keywords table!");

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}

	private static Connection getDBConnection() {

		Connection dbConnection = null;

		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		try {
			dbConnection = DriverManager.getConnection(
					DB_CONNECTION, DB_USER,DB_PASSWORD);
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return dbConnection;
	}
	
	private static String generateRows(ArrayList<String> xmlList) {
		String name = new String(xmlList.get(0));
		String word = new String(xmlList.get(1));
		
		String row = new String("INSERT INTO Keywords"
				+ "(file_name, keyword) " + "VALUES"
				+ "("+name+", "+word+")");
		return row;
	}
	
}