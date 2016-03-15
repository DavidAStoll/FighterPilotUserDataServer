package Server;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.plaf.SliderUI;

public class ClientDealer implements Runnable {

	// constants
	final String DISCONNECT_MESSAGE = "SOS";
	final String CLIENT_MESSAGE_DELIMITER = "|";
	final int MAX_MESSAGE_SIZE_IN_BYTES = 300 * 4; // 1200 bytes, which is 300 characters
	final int SLEEP_TIME_IF_NO_DATA_RECEIVED_IN_MILLI_SECS = 500; //0.5 of a second
	final int MAX_NUMBER_OF_NO_DATA_RECEIVED_UNTIL_MESSAGE_GETS_DROPPED = 10;
	final int MAX_NUMBER_OF_FAILED_MESSAGES_UNTIL_DISCONNECT = 10;

	SocketChannel iSocketChannel;
	HQInteractiveServer iHqServer;
	ByteBuffer iReadBuffer;
	byte[] iResetBuffer = new byte [MAX_MESSAGE_SIZE_IN_BYTES];

	ClientDealer(SocketChannel aServerSocketChannel,
			HQInteractiveServer aHqServer) {
		iSocketChannel = aServerSocketChannel;
		iHqServer = aHqServer;
		iReadBuffer = ByteBuffer.allocateDirect(MAX_MESSAGE_SIZE_IN_BYTES);
		for(int lIndex = 0; lIndex < MAX_MESSAGE_SIZE_IN_BYTES; lIndex++)
		{
			iResetBuffer[lIndex] = 0;
		}
	}

	public static String printCurrentDateTime() {
		DateFormat lDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date lDate = new Date();
		String lCurrentDate = lDateFormat.format(lDate);
		return lCurrentDate;
	}

	public void run() 
	{
		try {
			//got a connection
			int lMessageFailedCounter = 0;
			Socket lSocket = iSocketChannel.socket();
			lSocket.setReceiveBufferSize(MAX_MESSAGE_SIZE_IN_BYTES * 200); //at least buffer up to 200 messages
			System.out.println("Date: " + printCurrentDateTime());
			System.out.println("Connection received from " + lSocket.getInetAddress().getHostAddress());

			// update connection stats
			iHqServer.UpdateTotalNumberOfConnections(1);

			while(true)
			{
				// read next message from socket
				String lMessageFromClient = ReadMessageFromSocketChannel(iSocketChannel);
				
				if (lMessageFromClient == null) 
				{
					//some problem occurred or last message
					lMessageFailedCounter++;
					System.out.println("client>" + "Message is NULL!");
					if(lMessageFailedCounter == MAX_NUMBER_OF_FAILED_MESSAGES_UNTIL_DISCONNECT)
					{
						break; //need to break here since client might have shut down, otherwise server will keep connection alive forever
					}

				}
				else if(lMessageFromClient.equals(DISCONNECT_MESSAGE))
				{
					break;//disconnect message, close connection
				}
				else 
				{
					// print out client message
					// add code here to put into database
					SQLQuery lSQLQuery = new SQLQuery(iHqServer, lMessageFromClient);
					lSQLQuery.ProcessQuery();
					System.out.println("client>" + lMessageFromClient);
				}
			} 

			// all done, close Connection
			System.out.println("Connection closed");
			iSocketChannel.close();
			iHqServer.UpdateSuccessfulConnection(1);
		} 
		catch (ClosedChannelException aClosedException) 
		{
			//happens when sockets gets closed too early
			aClosedException.printStackTrace();
			iHqServer.UpdateFailedConnections(1);
		}
		catch (Exception aException) 
		{
			aException.printStackTrace();
			iHqServer.UpdateFailedConnections(1);
		}
	}

	public String ReadMessageFromSocketChannel(SocketChannel aSocketChannel) {
		try {

			iReadBuffer.clear();
			int lTotalBytesAlreadyRead = 0;
			int lNumberOfNoDataReceived = 0;
			
			while(lTotalBytesAlreadyRead != MAX_MESSAGE_SIZE_IN_BYTES)
			{
				int lBytesRead = 0;
				aSocketChannel.configureBlocking(false); //non blocking, in case no new data comes in
				lBytesRead = aSocketChannel.read(iReadBuffer);
				lTotalBytesAlreadyRead += lBytesRead;
				
				if(lBytesRead == -1)
				{
					return null; //client close connection
				}
				else if(lBytesRead == 0)
				{
					if(lNumberOfNoDataReceived == MAX_NUMBER_OF_NO_DATA_RECEIVED_UNTIL_MESSAGE_GETS_DROPPED)
					{
						return null;
					}
					
					//nothing received, maybe connection got interrupted
					lNumberOfNoDataReceived++;
					Thread.sleep(SLEEP_TIME_IF_NO_DATA_RECEIVED_IN_MILLI_SECS);
				}
				
				if(lTotalBytesAlreadyRead > MAX_MESSAGE_SIZE_IN_BYTES)
				{
					//in theory should never happen, but put it in just in case to avoid infinite loop
					return null;
				}
			}

			aSocketChannel.configureBlocking(true); //want it to block again
			iReadBuffer.flip(); // shrinks buffer to message size and resets
			// extract the message from ByteBuffer
			String lResult = Decoder.DecodeMessage(iReadBuffer.asIntBuffer());
			//System.out.println("Debug>> " + lResult);
			
			//reset buffer
			iReadBuffer.put(iResetBuffer);
			
			
			int lDelimiterStartPosition = lResult.indexOf(CLIENT_MESSAGE_DELIMITER);
			if(lDelimiterStartPosition == -1)
			{
				System.out.println("Error!!! Message Corrupted!");
				System.out.println(lResult);
				return null; //start delimiter not present
			}
			int lDelimiterEndPosition = lResult.indexOf(CLIENT_MESSAGE_DELIMITER, lDelimiterStartPosition + 1);
			if (lDelimiterEndPosition == -1) 
			{
				System.out.println("Error!!! Message Corrupted!");
				System.out.println(lResult);
				return null; //end delimiter not in message, message seems to be corrupted
			} else 
			{
				lResult = lResult.substring(lDelimiterStartPosition + 1, lDelimiterEndPosition); //don't include delimiter
			}
			return lResult;
		} catch (Exception aException) {
			aException.printStackTrace();
			return null;
		}
	}
}
