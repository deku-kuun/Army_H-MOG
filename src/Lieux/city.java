package Lieux;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import Users.DatabaseConnection;

public class city {

	private String code, name ,Dcode ;
	private static HashMap <Integer,ArrayList<String>> cities = new HashMap <Integer,ArrayList<String>> () ;
	public city (String code, String name, String Ccode)
	{
		this.code = code.trim() ;
		this.name = name.trim() ;
		this.Dcode = Ccode.trim() ;
		addCityIntoDatabase(this.code,this.name,this.Dcode) ;
		LoadCityFromDatabase() ;
	}	
	
	public static void LoadCityFromDatabase()
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
			ResultSet resultat = statementLoad.executeQuery("SELECT * FROM city") ;
			ResultSetMetaData resultMeta = resultat.getMetaData();
			while(resultat.next())
			{
				idLoad = resultat.getInt("cityId") ;
				//	countries.put(codeLoad, nameLoad) ; 
				for(int i = 2; i <= resultMeta.getColumnCount(); i++)
				{

					f.add(resultat.getString(i)) ;
					//	System.out.println(resultat.getString("countryCode")) ;
					//	System.out.print(resultat.getObject(i)+" "); /* resultMeta.getColumnName(i).toUpperCase()  */ 
				}
				cities.put(idLoad, f) ;
				f = new ArrayList <String>() ;

			}

		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static Boolean testIfCodeDistrictExists(String districtCode)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			DatabaseConnection.connect 
			= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
			// Statement statement = (DatabaseConnection.connect).createStatement() ;
			PreparedStatement statementTest= 
					DatabaseConnection.connect.prepareStatement("SELECT COUNT(*) AS nb FROM district WHERE districtCode LIKE ?") ;
			statementTest.setString(1, districtCode);
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

	public static Boolean testIfCityExists(String codeInsee, String districtCode)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			DatabaseConnection.connect 
			= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
			// Statement statement = (DatabaseConnection.connect).createStatement() ;
			PreparedStatement statementTest= 
					DatabaseConnection.connect.prepareStatement("SELECT COUNT(*) AS nb FROM city WHERE codeInsee LIKE ? AND districtCode LIKE ?") ;
			statementTest.setString(1, codeInsee);
			statementTest.setString(2, districtCode);
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

	public static void addCityIntoDatabase(String codeInsee, String cityName, String districtCode)
	{
		if(!testIfCityExists(codeInsee,districtCode) && testIfCodeDistrictExists(districtCode))
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				DatabaseConnection.connect 
				= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
				// Statement statement = (DatabaseConnection.connect).createStatement() ;
				PreparedStatement statementInsert = 
						DatabaseConnection.connect.prepareStatement(" INSERT INTO city (codeInsee,cityName,districtCode) VALUES (?,?,?) ") ;
				statementInsert.setString(1, codeInsee);
				statementInsert.setString(2, cityName);
				statementInsert.setString(3, districtCode);
				statementInsert.executeUpdate() ;
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void removeCityFromDatabase(String codeInsee, String districtCode)
	{
		if(testIfCityExists(codeInsee, districtCode))
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				DatabaseConnection.connect 
				= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
				// Statement statement = (DatabaseConnection.connect).createStatement() ;
				PreparedStatement statementDelete = DatabaseConnection.connect.prepareStatement("DELETE FROM city WHERE codeInsee LIKE ? AND districtCode LIKE ? ") ;
				statementDelete.setString(1, codeInsee);
				statementDelete.setString(2, districtCode);
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
		new country("FR", "France");
		new district("24","Centre"," FR");
		new city("37261", " Tours", "25");
		System.out.println(Affiche());
	}
	
	
	
	public static String Affiche()
	{
		String affichage = "" ;
		for (Entry<Integer, ArrayList<String>> e : cities.entrySet()) 
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
