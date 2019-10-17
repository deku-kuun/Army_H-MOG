package Lieux;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import Users.DatabaseConnection;

public class country 
{	
	private static HashMap <Integer,ArrayList<String>> countries = new HashMap <Integer,ArrayList<String>> () ;
	private String name, code;
	public country(String code, String name)
	{

		this.code = code.trim() ;
		this.name = name.trim() ;
		addCountryIntoDatabase(this.code,this.name) ;
		LoadCountryFromDatabase() ;
	}
	public static void LoadCountryFromDatabase()
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
			ResultSet resultat = statementLoad.executeQuery("SELECT * FROM country") ;
			ResultSetMetaData resultMeta = resultat.getMetaData();
			while(resultat.next())
			{
				idLoad = resultat.getInt("countryId") ;
				//	countries.put(codeLoad, nameLoad) ; 
				for(int i = 2; i <= resultMeta.getColumnCount(); i++)
				{

					f.add(resultat.getString(i)) ;
					//	System.out.println(resultat.getString("countryCode")) ;
					//	System.out.print(resultat.getObject(i)+" "); /* resultMeta.getColumnName(i).toUpperCase()  */ 
				}
				countries.put(idLoad, f) ;
				f = new ArrayList <String>() ;

			}

		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static Boolean testIfCountryExists(String countryCode){
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
	public static void addCountryIntoDatabase(String countryCode, String countryName)
	{
		if(!testIfCountryExists(countryCode))
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				DatabaseConnection.connect 
				= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
				// Statement statement = (DatabaseConnection.connect).createStatement() ;
				PreparedStatement statementInsert = 
						DatabaseConnection.connect.prepareStatement(" INSERT INTO country (countryCode,countryName) VALUES (?,?) ") ;
				statementInsert.setString(1, countryCode);
				statementInsert.setString(2, countryName);
				statementInsert.executeUpdate() ;
				//countries.put(countryCode,countryName) ;
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	public static void removeCountryFromDatabase(String countryCode)
	{
		if(testIfCountryExists(countryCode))
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				DatabaseConnection.connect 
				= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
				// Statement statement = (DatabaseConnection.connect).createStatement() ;
				PreparedStatement statementDelete = DatabaseConnection.connect.prepareStatement("DELETE FROM country WHERE countryCode LIKE ? ") ;
				statementDelete.setString(1, countryCode);
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
		//LoadCountryFromDatabase() ;
		// System.out.println(testIfCountryExists("Algérie")) ;
		//	country france = new country("FR", "France") ;
		LoadCountryFromDatabase() ;
		//removeCountryFromDatabase("FR") ;
		// System.out.println(testIfCountryExists("Algérie")) ;
		//
		//	System.out.println(countr);
		System.out.println(Affiche());


	}
	
	public static String Affiche()
	{
		String affichage = "" ;
		for (Entry<Integer, ArrayList<String>> e : countries.entrySet()) 
		{
			affichage += e.getKey() + " => " + e.getValue().get(0) + ", " +  e.getValue().get(1) + "\n" ; //	if(e.getKey() != null && e.getValue() != null)
		}
		return affichage ;
	}

}
