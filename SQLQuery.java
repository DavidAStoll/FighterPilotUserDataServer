package Server;

import java.sql.SQLException;
import java.sql.Statement;

public class SQLQuery
{
	String iClientMessage;
	SQLServer iSQLServer;
	HQInteractiveServer iHqServer;
	
	SQLQuery(HQInteractiveServer aHqServer, String aClientMessage)
	{
		iHqServer = aHqServer;
		iSQLServer = aHqServer.iSQLServer;
		iClientMessage = aClientMessage;
		
	}
	
	//used for storing the phone's 
	public void OperatingSystemInsert(String[] aSplitMessage)
	{
		iHqServer.updateTotalQueries(1);
		String lQuery = "";
		Statement lStatement;
		//TableName UID OSVersion GameVersion Date Time
		//NumberOfDaysPlayed Int NumberOfTimesStarted Int
		try
		{
			lStatement = iSQLServer.iConnection.createStatement();
			// for mysql the select statement is different compare to other
			// database software
			// SELECT * FROM database.schema.table
			// query
			lQuery = "replace into hqsqldatabase.phoneoperatingsystem(PhoneID, OperatingSystem) values (\'" + 
					aSplitMessage[1] + "\', \'" + aSplitMessage[2] + "\')";
			lStatement.executeUpdate(lQuery);
			iHqServer.updateSuccessfulQueries(1);
		}
		catch(SQLException eSQLException)
		{
			iHqServer.updateFailedQueries(1);
			System.out.println("OperatingSystemInsert error on the following query:");
			System.out.println(lQuery);
			System.out.println(eSQLException.getLocalizedMessage());
		}
	}
	
	//generic insert query statement
	public int GenericInsert(String[] aSplitMessage)
	{
		iHqServer.updateTotalQueries(1);
		int lResult = 0;
		Statement lStatement;
		String lQuery = "";
		//TableName UID OSVersion GameVersion Date Time
		try
		{
			lStatement = iSQLServer.iConnection.createStatement();
			// for mysql the select statement is different compare to other
			// database software
			// SELECT * FROM database.schema.table
			// query
			lQuery = "insert into hqsqldatabase." + aSplitMessage[0].toLowerCase() + "(PhoneID, Version, Date, Time";
			for(int i = 6; i < aSplitMessage.length; i += 2)
			{
				if(i >= 6)
				{
					lQuery += ", ";
				}
				lQuery += aSplitMessage[i]; 
			}
			lQuery += ") values (\'" + aSplitMessage[1] + "\', \'" + aSplitMessage[3] + "\', \'" + aSplitMessage[4] + "\', \'" + aSplitMessage[5]+"\'";
			for(int i = 7; i < aSplitMessage.length; i += 2)
			{
				if(i >= 7)
				{
					lQuery += ", ";
				}
				lQuery += aSplitMessage[i];
			}
			lQuery += ")";
			lStatement.executeUpdate(lQuery);
			iHqServer.updateSuccessfulQueries(1);
			lResult = 1;
		}
		catch(SQLException eSQLException)
		{
			iHqServer.updateFailedQueries(1);
			System.out.println("GenericInsert error on the following query:");
			System.out.println(lQuery);
			System.out.println(eSQLException.getLocalizedMessage());
		}
		return lResult;
	}

	//specific for StartedInfo
	public void StartedInfoInsert(String[] aSplitMessage)
	{
		iHqServer.updateTotalQueries(1);
		String lQuery = "";
		Statement lStatement;
		//TableName UID OSVersion GameVersion Date Time
		//NumberOfDaysPlayed Int NumberOfTimesStarted Int
		try
		{
			lStatement = iSQLServer.iConnection.createStatement();
			// for mysql the select statement is different compare to other
			// database software
			// SELECT * FROM database.schema.table
			// query
			lQuery = "replace into hqsqldatabase." + aSplitMessage[0].toLowerCase() + " (PhoneID, Version, ";
			for(int i = 6; i < aSplitMessage.length; i+=2)
			{
				if(i > 6)
				{
					lQuery += ", ";
				}
				lQuery += aSplitMessage[i];		
			}
			lQuery += ") values (\'" + aSplitMessage[1] + "\', \'" + aSplitMessage[3] + "\', ";
			for(int i = 7; i < aSplitMessage.length; i+=2)
			{
				if(i > 7)
				{
					lQuery += ", ";
				}
				lQuery += aSplitMessage[i];
			}
			lQuery += ")";
			lStatement.executeUpdate(lQuery);
			iHqServer.updateSuccessfulQueries(1);
		}
		catch(SQLException eSQLException)
		{
			iHqServer.updateFailedQueries(1);
			System.out.println("StartedInfoInsert error on the following query:");
			System.out.println(lQuery);
			System.out.println(eSQLException.getLocalizedMessage());
		}
	}
	
	public void StartedInfoInsertDateTime(String[] aSplitMessage)
	{
		iHqServer.updateTotalQueries(1);
		String lQuery = "";
		Statement lStatement;
		//TableName UID OSVersion GameVersion Date Time
		//NumberOfDaysPlayed Int NumberOfTimesStarted Int
		try
		{
			lStatement = iSQLServer.iConnection.createStatement();
			// for mysql the select statement is different compare to other
			// database software
			// SELECT * FROM database.schema.table
			// query
			lQuery = "insert into hqsqldatabase." + aSplitMessage[0].toLowerCase() + "datetime (PhoneID, Version, Date, Time" + 
			") values (\'" + aSplitMessage[1] + "\', \'" + aSplitMessage[3] + "\', \'" + aSplitMessage[4] + "\', \'" + aSplitMessage[5] + "\')";
			lStatement.executeUpdate(lQuery);
			iHqServer.updateSuccessfulQueries(1);
		}
		catch(SQLException eSQLException)
		{
			iHqServer.updateFailedQueries(1);
			System.out.println("StartedInfoDateTime error on the following query:");
			System.out.println(lQuery);
			System.out.println(eSQLException.getLocalizedMessage());
		}
	}
	
	public void ChangeSongFunctionInsert(String[] aSplitMessage)
	{
		iHqServer.updateTotalQueries(1);
		String lQuery = "";
		Statement lStatement;
		// TableName UID OSVersion GameVersion Date Time
		// NumberOfDaysPlayed Int NumberOfTimesStarted Int
		try
		{
			lStatement = iSQLServer.iConnection.createStatement();
			// for mysql the select statement is different compare to other
			// database software
			// SELECT * FROM database.schema.table
			// query
			lQuery = "insert into hqsqldatabase." + aSplitMessage[0].toLowerCase() + " (PhoneID, Version, Date, Time, OpenedInFrontend, SongName) values (\'" + aSplitMessage[1] + "\'," +
					" \'" + aSplitMessage[3] + "\', \'" + aSplitMessage[4] + "\', \'" + aSplitMessage[5] + "\', "+ aSplitMessage[7] +", \'"+ aSplitMessage[9]+ "\')";
			lStatement.executeUpdate(lQuery);
			iHqServer.updateSuccessfulQueries(1);
		} catch (SQLException eSQLException)
		{
			iHqServer.updateFailedQueries(1);
			System.out.println("ChangeSongFunction error on the following query:");
			System.out.println(lQuery);
			System.out.println(eSQLException.getLocalizedMessage());
		}
	}
	
	public void LanguageSelectedInsert(String[] aSplitMessage)
	{
		iHqServer.updateTotalQueries(1);
		String lQuery = "";
		Statement lStatement;
		// TableName UID OSVersion GameVersion Date Time
		// NumberOfDaysPlayed Int NumberOfTimesStarted Int
		try
		{
			lStatement = iSQLServer.iConnection.createStatement();
			// for mysql the select statement is different compare to other
			// database software
			// SELECT * FROM database.schema.table
			// query
			lQuery = "insert into hqsqldatabase." + aSplitMessage[0].toLowerCase() + " (PhoneID, Version, Date, Time, Language) values (\'" + aSplitMessage[1] + "\'," +
					" \'" + aSplitMessage[3] + "\', \'" + aSplitMessage[4] + "\', \'" + aSplitMessage[5] + "\', \'"+ aSplitMessage[7]+ "\')";
			lStatement.executeUpdate(lQuery);
			iHqServer.updateSuccessfulQueries(1);
		} catch (SQLException eSQLException)
		{
			iHqServer.updateFailedQueries(1);
			System.out.println("LanguageSelected error on the following query:");
			System.out.println(lQuery);
			System.out.println(eSQLException.getLocalizedMessage());
		}
	}
	
	public void TutorialSectionInsert(String[] aSplitMessage)
	{
		iHqServer.updateTotalQueries(1);
		String lQuery = "";
		Statement lStatement;
		// TableName UID OSVersion GameVersion Date Time
		// NumberOfDaysPlayed Int NumberOfTimesStarted Int
		try
		{
			lStatement = iSQLServer.iConnection.createStatement();
			// for mysql the select statement is different compare to other
			// database software
			// SELECT * FROM database.schema.table
			// query
			lQuery = "insert into hqsqldatabase." + aSplitMessage[0].toLowerCase() + " (PhoneID, Version, Date, Time, SectionName, PrevSectionTimeInMin) values (\'" + aSplitMessage[1] + "\'," +
					" \'" + aSplitMessage[3] + "\', \'" + aSplitMessage[4] + "\', \'" + aSplitMessage[5] + "\', \'"+ aSplitMessage[7]+ "\', "+ aSplitMessage[9] + ")";
			lStatement.executeUpdate(lQuery);
			iHqServer.updateSuccessfulQueries(1);
		} catch (SQLException eSQLException)
		{
			iHqServer.updateFailedQueries(1);
			System.out.println("TutorialSection error on the following query:");
			System.out.println(lQuery);
			System.out.println(eSQLException.getLocalizedMessage());
		}
	}
	
	public void GenericQuery(String[] aSplitMessage)
	{
		if(aSplitMessage[0].equals("StartedInfo"))
		{
			OperatingSystemInsert(aSplitMessage);
			StartedInfoInsertDateTime(aSplitMessage);
			StartedInfoInsert(aSplitMessage);
		}
		else if(aSplitMessage[0].equals("ChangeSongFunction"))
		{
			OperatingSystemInsert(aSplitMessage);
			ChangeSongFunctionInsert(aSplitMessage);
		}
		else if(aSplitMessage[0].equals("LanguageSelected"))
		{
			OperatingSystemInsert(aSplitMessage);
			LanguageSelectedInsert(aSplitMessage);
		}
		else if(aSplitMessage[0].equals("TutorialSection"))
		{
			OperatingSystemInsert(aSplitMessage);
			TutorialSectionInsert(aSplitMessage);
		}
		else
		{
			OperatingSystemInsert(aSplitMessage);
			GenericInsert(aSplitMessage);
		}
	}

	public int ProcessQuery()
	{
		int lResult = 0;
		String lSplitMessage[] = iClientMessage.split(" ");
		GenericQuery(lSplitMessage);
		return lResult;
	}
}
