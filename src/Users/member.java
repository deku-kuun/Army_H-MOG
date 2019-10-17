package Users;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class member {
	private String militaryId, password, surName, firstName ;
	private String rankId ;
	private static HashMap <Integer,ArrayList<String>> members = new HashMap <Integer,ArrayList<String>> () ;
	public member (String militaryId, String password, String surName, String firstName, String rankId)
	{
		this.militaryId = militaryId.trim() ;
		this.password = password.trim() ;
		this.surName = surName.trim() ;
		this.firstName = firstName.trim() ;
		this.rankId = rankId ;
		addMemberIntoDatabase(this.militaryId,this.password,this.surName,this.firstName,this.rankId) ;
		LoadMemberFromDatabase() ;
	}	

	public static void LoadMemberFromDatabase()
	{
		int idMember ;
		try
		{
			ArrayList <String> f = new ArrayList <String>() ;
			Class.forName("com.mysql.jdbc.Driver");
			DatabaseConnection.connect 
			= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
			Statement statementLoad= 
					DatabaseConnection.connect.createStatement() ;
			ResultSet resultat = statementLoad.executeQuery("SELECT * FROM member") ;
			ResultSetMetaData resultMeta = resultat.getMetaData();
			while(resultat.next())
			{
				idMember = resultat.getInt("memberId") ;
				for(int i = 2; i <= resultMeta.getColumnCount(); i++)
				{
					f.add(resultat.getString(i)) ;
				}
				members.put(idMember, f) ;
				f = new ArrayList <String>() ;
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public static Boolean testIfRankIdExists(String rankCode)
	{
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

	public static Boolean testIfMemberExists(String militaryId)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			DatabaseConnection.connect 
			= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
			PreparedStatement statementTest= 
					DatabaseConnection.connect.prepareStatement("SELECT COUNT(*) AS nb FROM member WHERE militaryId LIKE ?") ;
			statementTest.setString(1, militaryId);
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

	public static void addMemberIntoDatabase(String militaryId, String password, String surName, String firstName, String rankCode)
	{
		if(!testIfMemberExists(militaryId) && testIfRankIdExists(rankCode))
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				DatabaseConnection.connect 
				= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
				PreparedStatement statementInsert = 
						DatabaseConnection.connect.prepareStatement(" INSERT INTO member(militaryId,password,surName,firstName,rankCode) VALUES (?,?,?,?,?) ") ;
				statementInsert.setString(1, militaryId);
				statementInsert.setString(2, password);
				statementInsert.setString(3, surName);
				statementInsert.setString(4, firstName);
				statementInsert.setString(5, rankCode);
				statementInsert.executeUpdate() ;
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void removeMemberFromDatabase(String militaryId)
	{
		if(testIfMemberExists(militaryId))
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				DatabaseConnection.connect 
				= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
				PreparedStatement statementDelete = DatabaseConnection.connect.prepareStatement("DELETE FROM member WHERE militaryId LIKE ?") ;
				statementDelete.setString(1, militaryId);
				statementDelete.executeUpdate();		
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void UpdateMember(String militaryId, String password, String email, String surName,
			String firstName, String picName)
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			DatabaseConnection.connect 
			= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
			PreparedStatement statementUpdate = DatabaseConnection.connect.prepareStatement("UPDATE member SET password=?,email=?,"
					+ "surName=?,firstName=?,picName=? WHERE militaryId LIKE ?") ;
			statementUpdate.setString(1, password) ;
			statementUpdate.setString(2, email) ;
			statementUpdate.setString(3, surName) ;
			statementUpdate.setString(4, firstName) ;
			statementUpdate.setString(5, picName) ;
			statementUpdate.setString(6, militaryId) ;
			statementUpdate.executeUpdate();		
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void promoteMember(String rankCode, String militaryId) 
	{ 
		if(testIfMemberExists(militaryId) && testIfRankIdExists(rankCode) )
		{
			try {
				Class.forName("com.mysql.jdbc.Driver");
				DatabaseConnection.connect 
				= DriverManager.getConnection(DatabaseConnection.url,DatabaseConnection.user, DatabaseConnection.password);
				PreparedStatement statementUpdate = DatabaseConnection.connect.prepareStatement("UPDATE member SET rankCode=? WHERE militaryId = ? ") ;
				statementUpdate.setString(1, rankCode) ;
				statementUpdate.setString(2, militaryId) ;
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) 
	{
		System.out.println(Affiche());
	}

	public static void addMemberToTypeSoldier(String militaryId, String typeSoldierCode)
	{
		
	}

	public static String Affiche()
	{
		String affichage = "" ;
		for (Entry<Integer, ArrayList<String>> e : members.entrySet()) 
		{
			affichage += e.getKey() + " => " ; 
			for(int i = 0 ; i<e.getValue().size()-1 ;i++)
			{
				affichage += e.getValue().get(i) + ", " ;
			}
			affichage += e.getValue().get(e.getValue().size()-1) + "\n" ;
		}
		return affichage ;
	}

}
