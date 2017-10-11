package avalone.socket;

import java.util.ArrayList;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class Client 
{
	private AvaloneGLAPI glapi;
	private Point p;
	private ArrayList<Point> playerPos;
	
	public Client()
	{
		p = new Point();
		playerPos = new ArrayList<Point>();
		ThreadedSocket thread = new ThreadedSocket(p,playerPos);
		thread.start();
		glapi = new AvaloneGLAPI(1200,600,"multi");
		loop();
		thread.disconnect();
		try 
		{
			thread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	private void loop()
	{
		glapi.enableTextures();
	     while (glapi.windowShouldNotClose()) 
	     {
	    	 glapi.glLoopBegin();
	    	 
	    	 move();
	    	 //mettre un mutex sur playerPos
	    	 for(int i = 0;i < playerPos.size();i++)
	    	 {
	    		 glapi.drawRect(playerPos.get(i), playerPos.get(i).clone(20),"red");
	    	 }
	    	 //l'enlever ici
	    	 //glapi.drawRect(p, p.clone(20),"red");
	    	 
	    	 glapi.glLoopEnd(40);
	     }
	     glapi.destroyDisplay();
	}
	
	private void move()
	{
		//System.out.println(glapi.pressedKey());
		if(glapi.lastPressedKey() == 'q')
   	 	{
			p.x = p.x - 5;
   		 	if(p.x < 0)
   		 	{
   		 		p.x = 0;
   		 	}
   	 	}
		
		else if(glapi.lastPressedKey() == 'd')
   	 	{
			p.x = p.x + 5;
   		 	if(p.x > 1180)
   		 	{
   		 		p.x = 1180;
   		 	}
   	 	}
	}
	
	public static void main(String[] args) 
	{
	    new Client();
	}
}
