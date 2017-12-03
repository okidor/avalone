package avalone.graphFighters;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class ArenaObject
{
	public Point min;
	public Point max;
	String textureName;
		
	public ArenaObject(String line)
	{
		String[] infos = line.split(":");
	    min = new Point(Integer.parseInt(infos[0]),Integer.parseInt(infos[1]));
	    max = new Point(Integer.parseInt(infos[2]),Integer.parseInt(infos[3]));
	    textureName = infos[4];
	}
	
	public void draw()
	{
		AvaloneGLAPI.getInstance().drawTexturedRect(min, max, textureName);
	}
	
	public void move(int x,int y)
	{
		min.x = min.x + x;
		max.x = max.x + x;
		min.y = min.y + y;
		max.y = max.y + y;
	}
}
