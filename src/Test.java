import java.sql.* ;

public class Test {

	public static void main(String[] args) {      
		try 
		{
			Class.forName("com.mysql.jdbc.Driver"); // Driver Loading

		//	String url = "jdbc:mysql://localhost/h-mog_army"; // project URL link
			String url = "jdbc:mysql://localhost/test1";
			String user = "root"; // user for MySQL_BD
			String passwd = ""; // password for MySQL_BD

			java.sql.Connection connect = DriverManager.getConnection(url, user, passwd); // Get connection        

			PreparedStatement statement = connect.prepareStatement(" INSERT INTO message (content) VALUES (?) ") ; // statement creation
			// String sql = ""  ; // String for SQL request 
			statement.setString(1, "salem") ;
			System.out.println(statement.executeUpdate()>0 ? "done" : "error") ;
			// ResultSet rs = statement.executeUpdate(sql) ;  // SQL request  ; rs => result of SQL request
			// ResultSetMetaData resultMeta = rs.getMetaData(); 
			//			while(rs.next())
			//			{
			//				for(int i = 1; i <= resultMeta.getColumnCount(); i++)
			//				{
			//					System.out.print(toString(rs.getObject(i))+" "); /* resultMeta.getColumnName(i).toUpperCase()  */ 
			//				}
			//				System.out.println();
			//		}
		} 
		catch (Exception e) 
		{ e.printStackTrace();}  


	}

	public static String toString(Object T)
	{
		if(T==null)
			return "NULL" ;
		else 
			return T.toString() ;
	}

}

