package avalone.graphFighters;

import java.util.ArrayList;
import java.util.HashMap;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.FPoint;
import avalone.api.util.Point;

enum Joints
{
	Center,
	Up,
	Down,
	Left,
	Right,
	UpLeft,
	UpRight,
	DownLeft,
	DownRight,
	ShoulderLeft,
	ShoulderRight,
	LegLeft,
	LegRight
}

public class FighterPart 
{
	private static HashMap<String, FPoint> presetJoints;
	
	private Point localPosLeftDown;
	private Point localPosRightUp;
	private Fighter fighter;
	public String color;
	public int rotation;
	
	private ArrayList<FPoint> joints;
	
	
	public FighterPart(Fighter fighter,int offsetX,int offsetY,int sizeX,int sizeY,String color)
	{
		presetJointsInitialize();
		this.fighter = fighter;
		localPosLeftDown = new Point(offsetX,offsetY);
		localPosRightUp = new Point(offsetX + sizeX,offsetY + sizeY);
		this.color = color;
		
		rotation = 0;
		joints = new ArrayList<FPoint>();
	}
	
	public void draw()
	{
		AvaloneGLAPI.getInstance().beginRotate(new Point(
				((2 * fighter.pos.x) + localPosLeftDown.x + localPosRightUp.x)/2,
				((2 * fighter.pos.y) + localPosLeftDown.y + localPosRightUp.y)/2),
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
	
	public void presetJointsInitialize()
	{
		if (presetJoints == null)
		{
			presetJoints = new HashMap<String, FPoint>();
			presetJoints.put("center", new FPoint(0, 0));
			
			presetJoints.put("up", new FPoint(0, 1));
			presetJoints.put("down", new FPoint(0, -1));
			presetJoints.put("left", new FPoint(-1, 0));
			presetJoints.put("right", new FPoint(1, 0));
			presetJoints.put("upLeft", new FPoint(-1, 1));
			presetJoints.put("upRight", new FPoint(1, 1));
			presetJoints.put("downLeft", new FPoint(-1, -1));
			presetJoints.put("downRight", new FPoint(1, -1));
			
			presetJoints.put("shoulderLeft", new FPoint(-1, 0.8f));
			presetJoints.put("shoulderRight", new FPoint(1, 0.8f));
			presetJoints.put("legLeft", new FPoint(-0.6f, -1));
			presetJoints.put("legRight", new FPoint(0.6f, -1));
			
			presetJoints.put("middleUp", new FPoint(0, 0.8f));
			presetJoints.put("middleDown", new FPoint(1, -0.8f));
			presetJoints.put("middleLeft", new FPoint(-0.8f, 0));
			presetJoints.put("middleRight", new FPoint(0.8f, 0));
		}
	}
}
