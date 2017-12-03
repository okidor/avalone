package avalone.graphFighters;

import java.util.ArrayList;
import java.util.HashMap;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Vector;
import avalone.api.util.Point;

enum Joints
{
	center,
	up,
	down,
	left,
	right,
	upLeft,
	upRight,
	downLeft,
	downRight,
	shoulderLeft,
	shoulderRight,
	legLeft,
	legRight,
	middleUp,
	middleDown,
	middleLeft,
	middleRight
}

public class FighterPart 
{
	private static HashMap<String, Vector> presetJoints;
	
	private Point localPosLeftDown;
	private Point localPosRightUp;
	private Point localCenter;
	private Fighter fighter;
	private FighterPart parent;
	public String color;
	public int rotation;
	
	private ArrayList<Vector> joints;
	
	
	public FighterPart(Fighter fighter,int offsetX,int offsetY,int sizeX,int sizeY,String color)
	{
		presetJointsInitialize();
		this.fighter = fighter;
		localPosLeftDown = new Point(offsetX,offsetY);
		localPosRightUp = new Point(offsetX + sizeX,offsetY + sizeY);
		localCenter = new Point(
				((2 * fighter.pos.x) + localPosLeftDown.x + localPosRightUp.x)/2,
				((2 * fighter.pos.y) + localPosLeftDown.y + localPosRightUp.y)/2);
		this.color = color;
		
		rotation = 0;
		joints = new ArrayList<Vector>();
	}
	
	public FighterPart(FighterPart parent, String Color, Vector origin, int sizeX, int sizeY,
			ArrayList<Vector> joints)
	{
		presetJointsInitialize();
		this.fighter = parent.fighter;
		this.parent = parent;
		localPosLeftDown = new Point(
				Float.floatToIntBits(parent.localCenter.x * (1 + origin.x)),
				Float.floatToIntBits(parent.localCenter.y * (1 + origin.y)));
		localPosRightUp = new Point(
				localPosLeftDown.x + sizeX,
				localPosRightUp.y + sizeY);
		this.color = color;
		
		rotation = 0;
		this.joints = joints;
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
	
	public Vector GetPresetJoint(Joints preset)
	{
		if (presetJoints.containsKey(preset.toString()))
				return presetJoints.get(preset.toString());
		return null;
	}
	
	public void presetJointsInitialize()
	{
		if (presetJoints == null)
		{
			presetJoints = new HashMap<String, Vector>();
			presetJoints.put("center", new Vector(0, 0));
			
			presetJoints.put("up", new Vector(0, 1));
			presetJoints.put("down", new Vector(0, -1));
			presetJoints.put("left", new Vector(-1, 0));
			presetJoints.put("right", new Vector(1, 0));
			presetJoints.put("upLeft", new Vector(-1, 1));
			presetJoints.put("upRight", new Vector(1, 1));
			presetJoints.put("downLeft", new Vector(-1, -1));
			presetJoints.put("downRight", new Vector(1, -1));
			
			presetJoints.put("shoulderLeft", new Vector(-1, 0.8f));
			presetJoints.put("shoulderRight", new Vector(1, 0.8f));
			presetJoints.put("legLeft", new Vector(-0.6f, -1));
			presetJoints.put("legRight", new Vector(0.6f, -1));
			
			presetJoints.put("middleUp", new Vector(0, 0.8f));
			presetJoints.put("middleDown", new Vector(1, -0.8f));
			presetJoints.put("middleLeft", new Vector(-0.8f, 0));
			presetJoints.put("middleRight", new Vector(0.8f, 0));
		}
	}
}
