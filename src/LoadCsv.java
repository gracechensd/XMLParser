import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

 
public class LoadCsv {
 
  public static void main(String[] argv) {  
	  
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
		return;
	}
 
	Connection connection = null;
 
	try {
		connection = DriverManager
		.getConnection("jdbc:mysql://localhost:3306/metadata","root", "");
 
	} catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
		return;
	}
 
	
	if (connection != null) {
		System.out.println("You made it, take control your database now!");
		execute();
	} else {
		System.out.println("Failed to make connection!");
	}
  }
  
  public static void execute() {
	  
  }
}