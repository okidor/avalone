package avalone.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import avalone.api.util.Point;

public class ThreadedSocket extends Thread//implements Runnable
{
	private Point p;
	private Socket socket;
	private PrintWriter out;
	private boolean send;
	private int id;
	private ArrayList<Point> playerPos;
	
	public ThreadedSocket(Point p,ArrayList<Point> playerPos)
	{
		this.p = p;
		send = true;
		id = -1;
		this.playerPos = playerPos;
	}
	
	public void run() 
	{
		try 
		{
			//socket = new Socket(InetAddress.getLocalHost(),5000);
			socket = new Socket(InetAddress/*.getByName("78.203.12.33")*/.getLocalHost(),25565);
			out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
			waitId(in);
			syncPos(in);
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void waitId(BufferedReader in) throws IOException
	{
		boolean loop = true;
		while(loop)
		{
			String receive = in.readLine();
			try
			{
				id = Integer.valueOf(receive);
				System.out.println("id received = " + id);
			}
			catch(NumberFormatException ne)
			{
				System.err.println("expected a number but server sent an other object");
			}
			finally
			{
				loop = false;
			}
		}
		out.println("true");
	}
	
	private void syncPos(BufferedReader in) throws IOException
	{
		System.out.println("start of transmission");
		while(send)
		{
			out.println(p.x + " " + p.y);
			sleep(10);
			String tmp = in.readLine();
			String[] pos = tmp.split(" ");
			playerPos.clear();//si le client fait son get a ce moment, crash!!!
			for(int i = 0;i < pos.length;i = i + 2)
			{
				playerPos.add(new Point(Integer.valueOf(pos[i]), Integer.valueOf(pos[i+1])));
			}
		}
	}
	
	public void disconnect()
	{
		send = false;
		out.println("stop");
		System.out.println("end of transmission");
		sleep(500);
		try 
		{
			socket.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
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
