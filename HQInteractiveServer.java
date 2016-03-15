package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HQInteractiveServer implements Runnable
{
	// instance variables
	ServerSocketChannel iServerSocketChannel;
	Socket iSocket;
	String iDateStarted;
	int iTotalNumberOfConnections;
	int iSuccessfulConnections;
	int iFailedConnections;
	int iTotalQueries;
	int iSuccessfulQueries;
	int iFailedQueries;
	SQLServer iSQLServer;
	
	Connection iConnection = null;

	HQInteractiveServer()
	{
		iTotalNumberOfConnections = 0;
		iSuccessfulConnections = 0;
		iFailedConnections = 0;
		
		//update the interface
		ServerUserInterface.UpdateTotalConnections(0);
		ServerUserInterface.UpdateSuccessfulConnections(0);
		ServerUserInterface.UpdateFailedConnections(0);
		
		//create Decoder functions
		Decoder.CreateCodeValues();
	}

	public void run()
	{
		try
		{
			// 1. creating a server socket
			iServerSocketChannel = ServerSocketChannel.open();
			iServerSocketChannel.socket().bind(new InetSocketAddress(5555));

			while (true)
			{
				// 2. Wait for connection
				System.out.println("");// new line
				System.out.println("Date: " + printCurrentDateTime());
				System.out.println("Waiting for connection");

				SocketChannel lNewSocketChannel = iServerSocketChannel.accept();
				if (lNewSocketChannel != null)
				{
					// create new dealer to handle client connection
					ClientDealer lClientDealer = new ClientDealer(lNewSocketChannel, this);
					new Thread(lClientDealer).start();
				}
			}
		}
		catch (ClosedByInterruptException aServerThreadInterrupted)
		{
			// wait for client to finish
		}
		catch (Exception aException)
		{
			aException.printStackTrace();
		}
		finally
		// will be called once connection with client is done or an error
		// occurred
		{
			// 4: Closing connection
			try
			{
				if(iServerSocketChannel != null)
					iServerSocketChannel.close();
			}
			catch (IOException aIOException)
			{
				aIOException.printStackTrace();
			}
		}
	}

	public static String printCurrentDateTime()
	{
		DateFormat lDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date lDate = new Date();
		String lCurrentDate = lDateFormat.format(lDate);
		return lCurrentDate;
	}
	
	public void startMySQLServer()
	{
		iSQLServer = new SQLServer();
		iSQLServer.startMySQLServer();
	}

	public void stopMySQLServer()
	{
		iSQLServer.stopMySQLServer();
	}

	public Connection getConnection()
	{
		return iConnection;
	}

	public String getDateStarted()
	{
		return iDateStarted;
	}

	public void setDateStarted(String aDateStarted)
	{
		iDateStarted = aDateStarted;
	}
	
	public void UpdateTotalNumberOfConnections(int aValue)
	{
		iTotalNumberOfConnections += aValue;
		ServerUserInterface.UpdateTotalConnections(iTotalNumberOfConnections);
	}
	
	public void UpdateSuccessfulConnection(int aValue)
	{
		iSuccessfulConnections += aValue;
		ServerUserInterface.UpdateSuccessfulConnections(iSuccessfulConnections);
	}
	
	public void UpdateFailedConnections(int aValue)
	{
		iFailedConnections += aValue;
		ServerUserInterface.UpdateFailedConnections(iFailedConnections);
	}
	
	public void updateTotalQueries(int aValue)
	{
		iTotalQueries += aValue;
		ServerUserInterface.UpdateTotalQueries(iTotalQueries);
	}
	
	public void updateSuccessfulQueries(int aValue)
	{
		iSuccessfulQueries += aValue;
		ServerUserInterface.UpdateSuccessfulQueries(iSuccessfulQueries);
	}
	
	public void updateFailedQueries(int aValue)
	{
		iFailedQueries += aValue;
		ServerUserInterface.UpdateFailedQueries(iFailedQueries);
	}
}