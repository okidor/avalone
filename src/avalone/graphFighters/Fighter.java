package avalone.graphFighters;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import avalone.api.util.Point;
import avalone.api.util.Vector;
import avalone.physics.Falling;
import avalone.physics.Physics;
import avalone.physics.Solid;

public abstract class Fighter implements Falling
{
	public Point pos;
	private int verticalSpeed;
	protected FighterPart body;
	protected HashMap<String, FighterPart> parts;
	
	protected FighterPart leftLeg;
	/*
	protected FighterPart head;
	protected FighterPart leftArm;
	protected FighterPart rightArm;
	protected FighterPart rightLeg;
	
	public Fighter(int x,int y)
	{
		pos = new Point(x,y);
		verticalSpeed = 0;
		head = new FighterPart(this,-5,0,10,10,"red");
		body = new FighterPart(this,-5,-20,10,20,"blue");
		leftArm = new FighterPart(this,-10,-20,5,20,"yellow");
		rightArm = new FighterPart(this,5,-20,5,20,"green");
		//rightArm.rotation = 45;
		leftLeg = new FighterPart(this,-10,-40,7,20,"brown");
		rightLeg = new FighterPart(this,3,-40,7,20,"cyan");
	}*/
	public Fighter(int x, int y)
	{
		pos = new Point(x, y);
		verticalSpeed = 0;
		parts = new HashMap<String, FighterPart>();
		body = new FighterPart(this, -10, -40, 20, 40, "blue");
		
		//parts.put("head", new FighterPart(this,-5,0,10,10,"red"));
		//parts.put("leftArm", new FighterPart(this,-10,-20,5,20,"yellow"));
		//parts.put("rightArm", new FighterPart(this,5,-20,5,20,"green"));
		//rightArm.rotation = 45;
		//parts.put("leftLeg", new FighterPart(this,-10,-40,7,20,"brown"));
		//parts.put("rightLeg", new FighterPart(this,3,-40,7,20,"cyan"));
		
		parts.put("head", new FighterPart(body, FighterPart.presetJoints.get(Joints.up.toString()),
				FighterPart.presetJoints.get(Joints.down.toString()),
				10, 10, "red", new ArrayList<Vector>()));
		parts.put("leftArm", new FighterPart(body, FighterPart.presetJoints.get(Joints.shoulderLeft.toString()),
				FighterPart.presetJoints.get(Joints.middleUp.toString()),
				8, 20, "yellow", new ArrayList<Vector>()));
		parts.put("rightArm", new FighterPart(body, FighterPart.presetJoints.get(Joints.shoulderRight.toString()),
				FighterPart.presetJoints.get(Joints.middleUp.toString()),
				8, 20, "green", new ArrayList<Vector>()));
		parts.put("leftLeg", new FighterPart(body, FighterPart.presetJoints.get(Joints.legLeft.toString()),
				FighterPart.presetJoints.get(Joints.middleUp.toString()),
				8, 40, "cyan", new ArrayList<Vector>()));
		parts.put("rightLeg", new FighterPart(body, FighterPart.presetJoints.get(Joints.legRight.toString()),
				FighterPart.presetJoints.get(Joints.middleUp.toString()),
				8, 40, "brown", new ArrayList<Vector>()));
		parts.put("test", new FighterPart(body, FighterPart.presetJoints.get(Joints.center.toString()),
				FighterPart.presetJoints.get(Joints.center.toString()),
				10, 10, "white", new ArrayList<Vector>()));
		
		//leftLeg = new FighterPart(this,-20,-80,14,40,"red");
	}
	
	public void draw()
	{
		body.draw();
		//parts.get("rightLeg").rotation = 45;
		parts.get("leftArm").draw();
		parts.get("rightArm").draw();
		//parts.get("rightArm").rotation += 1;
		parts.get("leftLeg").draw();
		parts.get("rightLeg").draw();
		parts.get("head").draw();
		parts.get("test").draw();
		//leftLeg.draw();
	}
	
	public abstract void input();
	
	public void collideWith(Solid solid)
	{

		if(Physics.resolveCollisionGlobalLocal(body, pos, solid))
		{
			//System.out.println("collide body");
		}
		/*if(Physics.resolveCollisionGlobalLocal(leftLeg, pos, solid))
		{
			verticalSpeed = 0;
		}*/
		
		for (String part : parts.keySet())
		{
			if(Physics.resolveCollisionGlobalLocal(parts.get(part), pos, solid))
			{
				if (part == "leftLeg")
				{
					//System.out.println("collide left leg");
					verticalSpeed = 0;
				}
				if (part == "rightLeg")
				{
					//System.out.println("collide right leg");
					verticalSpeed = 0;
				}
				//else
					//System.out.println("collide " + part);
			}
		}
	}
	
	@Override
	public int getVerticalSpeed() 
	{
		return verticalSpeed;
	}

	@Override
	public void setVerticalSpeed(int value) 
	{
		verticalSpeed = value;
	}

	@Override
	public int getVerticalPos() 
	{
		return pos.y;
	}

	@Override
	public void setVerticalPos(int value) 
	{
		pos.y = value;
	}
}
