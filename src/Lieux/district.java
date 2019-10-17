package Lieux;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import Users.DatabaseConnection;

public class district {

	private String code, name ,Ccode ;
	private static HashMap <Integer,ArrayList<String>> districts = new HashMap <Integer,ArrayList<String>> () ;
	public district (String code, String name, String Ccode)
	{
		this.code = code.trim() ;
		this.name = name.trim() ;
		this.Ccode = Ccode.trim() ;
		addDistrictIntoDatabase(this.code,this.name,this.Ccode) ;
		LoadDistrictFromDatabase() ;
	}	
	
	public static void LoadDistrictFromDatabase()
	{
		int idLoad ;
		try
		{
			ArrayList <String> f = new ArrayList <String>() ;
			Class.forName("com.mysql.jdbc.Driver");
			DatabaseConnection.connect 
			= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
			// Statement statement = (DatabaseConnection.connect).createStatement() ;
			Statement statementLoad= 
			DatabaseConnection.connect.createStatement() ;
			ResultSet resultat = statementLoad.executeQuery("SELECT * FROM district") ;
			ResultSetMetaData resultMeta = resultat.getMetaData();
			while(resultat.next())
			{
				idLoad = resultat.getInt("districtId") ;
				//	countries.put(codeLoad, nameLoad) ; 
				for(int i = 2; i <= resultMeta.getColumnCount(); i++)
				{

					f.add(resultat.getString(i)) ;
					//	System.out.println(resultat.getString("countryCode")) ;
					//	System.out.print(resultat.getObject(i)+" "); /* resultMeta.getColumnName(i).toUpperCase()  */ 
				}
				districts.put(idLoad, f) ;
				f = new ArrayList <String>() ;

			}

		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}


	}

	public static Boolean testIfCodeCountryExists(String countryCode)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			DatabaseConnection.connect 
			= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
			// Statement statement = (DatabaseConnection.connect).createStatement() ;
			PreparedStatement statementTest= 
					DatabaseConnection.connect.prepareStatement("SELECT COUNT(*) AS nb FROM country WHERE countryCode LIKE ?") ;
			statementTest.setString(1, countryCode);
			ResultSet resultat = statementTest.executeQuery() ;
			resultat.next() ;
			if(resultat.getInt("nb")!=0)
				return true ;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return null ;
		}
		return false;
	}

	public static Boolean testIfDistrictExists(String districtCode, String countryCode)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			DatabaseConnection.connect 
			= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
			// Statement statement = (DatabaseConnection.connect).createStatement() ;
			PreparedStatement statementTest= 
					DatabaseConnection.connect.prepareStatement("SELECT COUNT(*) AS nb FROM district WHERE districtCode LIKE ? AND countryCode LIKE ?") ;
			statementTest.setString(1, districtCode);
			statementTest.setString(2, countryCode);
			ResultSet resultat = statementTest.executeQuery() ;
			resultat.next() ;
			if(resultat.getInt("nb")!=0)
				return true ;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return null ;
		}
		return false;
	}

	public static void addDistrictIntoDatabase(String districtCode, String districtName, String countryCode)
	{
		if(!testIfDistrictExists(districtCode,countryCode) && testIfCodeCountryExists(countryCode))
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				DatabaseConnection.connect 
				= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
				// Statement statement = (DatabaseConnection.connect).createStatement() ;
				PreparedStatement statementInsert = 
						DatabaseConnection.connect.prepareStatement(" INSERT INTO district (districtCode,districtName,countryCode) VALUES (?,?,?) ") ;
				statementInsert.setString(1, districtCode);
				statementInsert.setString(2, districtName);
				statementInsert.setString(3, countryCode);
				statementInsert.executeUpdate() ;
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void removeDistrictFromDatabase(String districtCode, String countryCode )
	{
		if(testIfDistrictExists(districtCode, countryCode))
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				DatabaseConnection.connect 
				= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
				// Statement statement = (DatabaseConnection.connect).createStatement() ;
				PreparedStatement statementDelete = DatabaseConnection.connect.prepareStatement("DELETE FROM district WHERE districtCode LIKE ? AND countryCode LIKE ? ") ;
				statementDelete.setString(1, districtCode);
				statementDelete.setString(2, countryCode);
				statementDelete.executeUpdate();		
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) 
	{
	//	district centre = new district("24","Centre"," DZ") ;
	//	country france = new country("FR", "France") ;
		removeDistrictFromDatabase("24", "DZ") ;
		removeDistrictFromDatabase("24", "FR") ;
		LoadDistrictFromDatabase() ;
		System.out.println(Affiche());
	}
	
	
	
	public static String Affiche()
	{
		String affichage = "" ;
		for (Entry<Integer, ArrayList<String>> e : districts.entrySet()) 
		{
			affichage += e.getKey() + " => " ; //	if(e.getKey() != null && e.getValue() != null)
			for(int i = 0 ; i<e.getValue().size()-1 ;i++)
			{
				affichage += e.getValue().get(i) + ", " ;
			}
			affichage += e.getValue().get(e.getValue().size()-1) + "\n" ;
		}
		return affichage ;
	}

}
