package avalone.graphFighters;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;
import avalone.physics.Solid;

public class ArenaObject extends Solid
{
	String textureName;
		
	public ArenaObject(String textureName,Point... points)
	{
		super(999999999,points);
	    this.textureName = textureName;
	}
	
	public void draw()
	{
		AvaloneGLAPI.getInstance().drawTexturedRect(vertices.get(0), vertices.get(2), textureName);
	}
	
	public void move(int x,int y)
	{
		vertices.get(0).x = vertices.get(0).x + x;
		vertices.get(2).x = vertices.get(2).x + x;
		vertices.get(0).y = vertices.get(0).y + y;
		vertices.get(2).y = vertices.get(2).y + y;
	}
}
