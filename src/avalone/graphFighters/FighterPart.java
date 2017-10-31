package avalone.graphFighters;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class FighterPart 
{
	private Point localPosLeftDown;
	private Point localPosRightUp;
	private Fighter fighter;
	public String color;
	public int rotation;
	
	public FighterPart(Fighter fighter,int offsetX,int offsetY,int sizeX,int sizeY,String color)
	{
		this.fighter = fighter;
		localPosLeftDown = new Point(offsetX,offsetY);
		localPosRightUp = new Point(offsetX + sizeX,offsetY + sizeY);
		this.color = color;
		rotation = 0;
	}
	
	public void draw()
	{
		AvaloneGLAPI.getInstance().beginRotate(new Point(
				(localPosLeftDown.x + localPosRightUp.x)/2,
				(localPosLeftDown.y + localPosRightUp.y)/2),
				rotation);
		
		AvaloneGLAPI.getInstance().drawRect(Point.add(fighter.pos, localPosLeftDown), Point.add(fighter.pos, localPosRightUp), color);
		AvaloneGLAPI.getInstance().endRotate();
	}
	
	public void move(int x,int y)
	{
		localPosLeftDown.x = localPosLeftDown.x + x;
		localPosLeftDown.y = localPosLeftDown.y + y;
		localPosRightUp.x = localPosRightUp.x + x;
		localPosRightUp.y = localPosRightUp.y + y;
	}
}
