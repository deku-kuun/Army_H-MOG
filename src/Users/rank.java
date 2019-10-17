package Users;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class rank {

	private static HashMap <Integer,ArrayList<String>> ranks = new HashMap <Integer,ArrayList<String>> () ;
	private String name, code;
	public rank(String name, String code)
	{
		this.code = code.trim() ;
		this.name = name.trim() ;
		addRankIntoDatabase(this.name,this.code) ;
		LoadRankFromDatabase() ;
	}
	public static void LoadRankFromDatabase()
	{
		int idRank;
		try
		{
			ArrayList <String> f = new ArrayList <String>() ;
			Class.forName("com.mysql.jdbc.Driver");
			DatabaseConnection.connect 
			= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
			Statement statementLoad= 
					DatabaseConnection.connect.createStatement() ;
			ResultSet resultat = statementLoad.executeQuery("SELECT * FROM rank") ;
			ResultSetMetaData resultMeta = resultat.getMetaData();
			while(resultat.next())
			{
				idRank = resultat.getInt("rankId") ;
				for(int i = 2; i <= resultMeta.getColumnCount(); i++)
				{
					f.add(resultat.getString(i)) ;
				}
				ranks.put(idRank, f) ;
				f = new ArrayList <String>() ;
			}

		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static Boolean testIfRankExists(String rankCode){
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			DatabaseConnection.connect 
			= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
			PreparedStatement statementTest= 
					DatabaseConnection.connect.prepareStatement("SELECT COUNT(*) AS nb FROM rank WHERE rankCode LIKE ?") ;
			statementTest.setString(1, rankCode);
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
	public static void addRankIntoDatabase(String rankName, String rankCode)
	{
		if(!testIfRankExists(rankCode))
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				DatabaseConnection.connect 
				= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
				PreparedStatement statementInsert = 
						DatabaseConnection.connect.prepareStatement(" INSERT INTO rank (rankname,rankCode) VALUES (?,?) ") ;
				statementInsert.setString(1, rankName);
				statementInsert.setString(2, rankCode);
				statementInsert.executeUpdate() ;
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	public static void removeCountryFromDatabase(String rankCode)
	{
		if(testIfRankExists(rankCode))
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				DatabaseConnection.connect 
				= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
				PreparedStatement statementDelete = DatabaseConnection.connect.prepareStatement("DELETE FROM rank WHERE rankCode = ? ") ;
				statementDelete.setString(1, rankCode);
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
		new rank("Général des armées","GA") ;
		new rank("Général","G") ;
		new rank("Général de corps d'armée","GCA") ;
		new rank("Général de division","GD") ;
		new rank("Général de brigade","GB") ;
		new rank("Colonel","Co") ;
		new rank("Lieutenant-Colonel","LC") ;
		new rank("Capitaine","Ca") ;
		new rank("Lieutenant","L") ;
		new rank("Soldat","S") ;	
		new rank("Administrateur","AD") ;
		System.out.println(Affiche());
	}

	public static String Affiche()
	{
		String affichage = "" ;
		for (Entry<Integer, ArrayList<String>> e : ranks.entrySet()) 
		{
			affichage += e.getKey() + " => " + e.getValue().get(0) + ", " +  e.getValue().get(1) + "\n" ; 
		}
		return affichage ;
	}

}
