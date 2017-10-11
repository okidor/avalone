package avalone.socket;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import avalone.api.util.Point;

public class Server 
{
	ServerSocket socket;
	Socket wtfSocket;
	private int clientId;
	private HashMap<Integer,Point> playerPos;
	
	public Server()
	{
		clientId = -1;
		playerPos = new HashMap<Integer,Point>();
		try 
		{
			socket();
		}
		catch(EOFException eof)
		{
			System.out.println("a client disconnected");
		}
		catch(IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void socket() throws IOException
	{
		socket = new ServerSocket(25565,3);
		System.out.println("server listening on port 25565");
		while(true)
		{
			wtfSocket = socket.accept();
			System.out.println("new connection with " + wtfSocket.getInetAddress());
			clientId++;
			playerPos.put(clientId,new Point());
			new Thread(new ThreadedServerSocket(wtfSocket,clientId,playerPos)).start();
		}
	}
	
	public static void main(String[] args) 
	{
	    new Server();
	}
}
