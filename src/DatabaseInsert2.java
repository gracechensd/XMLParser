import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DatabaseInsert2 {
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/metadata";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";
	private static final String SQL_INSERT = "INSERT INTO Keywords"
			+ "(file_name, keyword) " + "VALUES"
			+ "(?, ?)";

	public static void main(String[] argv) {

		try {
			insertRecordIntoDbUserTable();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	private static void insertRecordIntoDbUserTable() throws SQLException {

		Connection dbConnection = null;
		PreparedStatement statement = null;

		try {
			dbConnection = getDBConnection();
			statement = dbConnection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);

			ArrayList<String> xmlKeywords = new ArrayList<String>();
			xmlKeywords.add("demo.xml");
			xmlKeywords.add("keyword1");
			xmlKeywords.add("keyword2");
			xmlKeywords.add("keyword3");
			 for (int i = 1; i < xmlKeywords.size(); i++) {
		            statement.setString(1, xmlKeywords.get(0));
		            statement.setString(2, xmlKeywords.get(i));
		            statement.addBatch();
		            if ((i + 1) % 1000 == 0) {
		                statement.executeBatch(); // Execute every 1000 items.
		            }
		        }
		        statement.executeBatch();

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
	
}