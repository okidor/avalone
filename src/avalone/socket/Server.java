package avalone.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import avalone.api.util.Point;

public class Server 
{
	ServerSocket socket;
	Socket wtfSocket;
	private int clientId;
	private ArrayList<Point> playerPos;
	
	public Server()
	{
		clientId = -1;
		playerPos = new ArrayList<Point>();
		try 
		{
			socket();
		} 
		catch(IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void socket() throws IOException
	{
		socket = new ServerSocket(25565,3);
		while(true)
		{
			wtfSocket = socket.accept();
			clientId++;
			playerPos.add(new Point());
			new Thread(new ThreadedServerSocket(wtfSocket,clientId,playerPos)).start();
		}
	}
	
	public static void main(String[] args) 
	{
	    new Server();
	}
}
