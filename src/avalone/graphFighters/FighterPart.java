package avalone.graphFighters;

import java.util.ArrayList;
import java.util.HashMap;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Vector;
import avalone.physics.Solid;
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

public class FighterPart extends Solid
{
	public static HashMap<String, Vector> presetJoints;
	
	//private Point localPosLeftDown;
	//private Point localPosRightUp;
	private Fighter fighter;
	private FighterPart parent;
	private Point localCenter;
	private int sizeX;
	private int sizeY;
	public String color;
	public int rotation;
	public Vector rotationJoint;
	
	private ArrayList<Vector> joints;
	
	public FighterPart(Fighter fighter,int offsetX,int offsetY,int sizeX,int sizeY,String color)
	{
		super(1.0f,new Point(offsetX,offsetY),new Point(offsetX,offsetY + sizeY),new Point(offsetX + sizeX,offsetY + sizeY),new Point(offsetX + sizeX,offsetY));
		presetJointsInitialize();
		this.fighter = fighter;
		//localPosLeftDown = new Point(offsetX,offsetY);
		//localPosRightUp = new Point(offsetX + sizeX,offsetY + sizeY);
		localCenter = new Point(
				/*fighter.pos.x + */(vertices.get(0).x + vertices.get(2).x)/2,
				/*fighter.pos.y + */(vertices.get(0).y + vertices.get(2).y)/2);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.color = color;
		
		rotation = 0;
		joints = new ArrayList<Vector>();
		joints.add(presetJoints.get(Joints.center.toString()));
		rotationJoint = joints.get(0);
		System.out.println((vertices.get(0).x + vertices.get(2).x)/2);
		System.out.println("Static (" + vertices.get(0).x + " : " + vertices.get(0).y + ")");
	}
	
	//parent : FighterPart sur laquelle est attaché celle-ci.
	//origin : Joint (FPoint) du parent sur lequel est attaché ce FighterPart.
	//joints : ArrayList des joints dont dispose ce FighterPart.
	//localCenter : Center coordinates from the position of the Fighter.
	public FighterPart(FighterPart parent, Vector origin, Vector linkJoint, int sizeX, int sizeY, String color,
			ArrayList<Vector> newJoints)
	{
		/*super(1.0f,new Point(Float.floatToIntBits(parent.localCenter.x * (1 + origin.x)),Float.floatToIntBits(parent.localCenter.y * (1 + origin.y))),
				   new Point(Float.floatToIntBits(parent.localCenter.x * (1 + origin.x)),Float.floatToIntBits(parent.localCenter.y * (1 + origin.y)) + sizeY),
				   new Point(Float.floatToIntBits(parent.localCenter.x * (1 + origin.x)) + sizeX,Float.floatToIntBits(parent.localCenter.y * (1 + origin.y)) + sizeY),
				   new Point(Float.floatToIntBits(parent.localCenter.x * (1 + origin.x)) + sizeX,Float.floatToIntBits(parent.localCenter.y * (1 + origin.y))));
		*/
		/*super(1.0f,new Point(Math.round(parent.localCenter.x * (1 + origin.x)), Math.round(parent.localCenter.y * (1 + origin.y))),
				   new Point(Math.round(parent.localCenter.x * (1 + origin.x)), Math.round(parent.localCenter.y * (1 + origin.y)) + sizeY),
				   new Point(Math.round(parent.localCenter.x * (1 + origin.x)) + sizeX, Math.round(parent.localCenter.y * (1 + origin.y)) + sizeY),
				   new Point(Math.round(parent.localCenter.x * (1 + origin.x)) + sizeX, Math.round(parent.localCenter.y * (1 + origin.y))));
		*/
		super(1.0f, new Point(Math.round(parent.localCenter.x + (parent.sizeX/2) * origin.x + (sizeX/2) * (-1 - linkJoint.x)),
							  Math.round(parent.localCenter.y + (parent.sizeY/2) * origin.y + (sizeY/2) * (1 - linkJoint.y))),
				    new Point(Math.round(parent.localCenter.x + (parent.sizeX/2) * origin.x + (sizeX/2) * (-1 - linkJoint.x)),
							  Math.round(parent.localCenter.y + (parent.sizeY/2) * origin.y + (sizeY/2) * (-1 - linkJoint.y))),
				    new Point(Math.round(parent.localCenter.x + (parent.sizeX/2) * origin.x + (sizeX/2) * (1 - linkJoint.x)),
							  Math.round(parent.localCenter.y + (parent.sizeY/2) * origin.y + (sizeY/2) * (-1 - linkJoint.y))),
				    new Point(Math.round(parent.localCenter.x + (parent.sizeX/2) * origin.x + (sizeX/2) * (1 - linkJoint.x)),
							  Math.round(parent.localCenter.y + (parent.sizeY/2) * origin.y + (sizeY/2) * (1 - linkJoint.y))));
		
		presetJointsInitialize();
		this.fighter = parent.fighter;
		this.parent = parent;
		/*localPosLeftDown = new Point(
				Float.floatToIntBits(parent.localCenter.x * (1 + origin.x)),
				Float.floatToIntBits(parent.localCenter.y * (1 + origin.y)));
		localPosRightUp = new Point(
				localPosLeftDown.x + sizeX,
				localPosLeftDown.y + sizeY);*/
		localCenter = new Point(
				/*fighter.pos.x + */(vertices.get(0).x + vertices.get(2).x)/2,
				/*fighter.pos.y + */(vertices.get(0).y + vertices.get(2).y)/2);
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.color = color;
		
		rotation = 0;
		rotationJoint = linkJoint;
		joints = new ArrayList<Vector>();
		joints.add(linkJoint);
		for (Vector v : newJoints)
			joints.add(v);
		System.out.println(parent.localCenter.y);
		System.out.println("(" + origin.x + ":" + origin.y + ") > " +
				vertices.get(0).x + " - " + vertices.get(0).y);
		//System.out.println("Dynamic (" + vertices.get(0).x + " : " + vertices.get(0).y + ")");
	}
	
	public void draw()
	{
		AvaloneGLAPI.getInstance().beginRotate(new Point(
				Math.round(localCenter.x + (sizeX/2) * rotationJoint.x),
				Math.round(localCenter.y + (sizeY/2) * rotationJoint.y)), rotation);
		
		AvaloneGLAPI.getInstance().drawRect(Point.add(fighter.pos, vertices.get(0)), Point.add(fighter.pos, vertices.get(2)), color);
		AvaloneGLAPI.getInstance().endRotate();
	}
	
	public void move(int x,int y)
	{
		moveAllPoints(new Point(x,y));
	}
	
	public Vector GetPresetJoint(Joints preset)
	{
		if (presetJoints.containsKey(preset.toString()))
				return presetJoints.get(preset.toString());
		return null;
	}
	
	private static void presetJointsInitialize()
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
