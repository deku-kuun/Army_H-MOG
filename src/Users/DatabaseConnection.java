package Lieux;
import java.sql.DriverManager;

public class DatabaseConnection 
{
	public static final String url = "jdbc:mysql://localhost/h-mog_army"; // project URL link
	public static final String user = "root" ; // user for MySQL_BD
	public static final String password = ""; // password for MySQL_BD
	public static java.sql.Connection connect = null ;
	{
	try 
	{
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection(url, user, password); // Get connection   
	} 
	catch (Exception e) { // TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
	
	

