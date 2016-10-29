package avalone.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import avalone.api.util.Point;

public class ThreadedServerSocket implements Runnable
{
	private Socket socket;
	private final int clientId;
	private ArrayList<Point> playerPos;
	
	public ThreadedServerSocket(Socket socket,int clientId,ArrayList<Point> playerPos)
	{
		this.socket = socket;
		this.clientId = clientId;
		this.playerPos = playerPos;
	}
	
	@Override
	public void run() 
	{
		try 
		{
			socket();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void socket() throws IOException
	{
		System.out.println("conection");
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
		boolean loop = true;
		syncClientId(out,in);
		System.out.println("start of reception");
		while(loop) 
		{
			loop = receiveClientPos(in);
			sendAllPos(out);
		}
		System.out.println("end of reception");
		socket.close();
	}
	
	private void sendAllPos(PrintWriter out)
	{
		for(int i = 0;i < playerPos.size();i++)
		{
			out.print(playerPos.get(i).x + " " + playerPos.get(i).y + " ");
		}
		out.println();
	}
	
	private boolean receiveClientPos(BufferedReader in) throws IOException
	{
		String reception;
		String[] pos;
		reception = in.readLine();
		//System.out.println("received: " + reception);
		if(reception.equals("stop"))
		{
			return false;
		}
		else
		{
			pos = reception.split(" ");
			playerPos.get(clientId).x = Integer.valueOf(pos[0]);
			playerPos.get(clientId).y = Integer.valueOf(pos[1]);
			return true;
		}
	}
	
	private void syncClientId(PrintWriter out,BufferedReader in) throws IOException
	{
		boolean hasConfirmed = false;int i = 0;String confirmReception;
		out.println(clientId);
		while(!hasConfirmed)
		{
			sleep(1000);
			confirmReception = in.readLine();
			if(confirmReception.equals("true"))
			{
				hasConfirmed = true;
			}
		}
	}
	
	public void sleep(int ms)
	{
		try 
		{
			Thread.currentThread().sleep(ms);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}

}
