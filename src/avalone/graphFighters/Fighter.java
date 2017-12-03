package avalone.graphFighters;

import avalone.api.util.Point;
import avalone.physics.Falling;
import avalone.physics.Physics;

public abstract class Fighter implements Falling
{
	public Point pos;
	private int verticalSpeed;
	protected FighterPart head;
	protected FighterPart body;
	protected FighterPart leftArm;
	protected FighterPart rightArm;
	protected FighterPart leftLeg;
	protected FighterPart rightLeg;
	
	public Fighter(int x,int y)
	{
		pos = new Point(x,y);
		verticalSpeed = 0;
		head = new FighterPart(this,-5,0,10,10,"red");
		body = new FighterPart(this,-5,-20,10,20,"blue");
		leftArm = new FighterPart(this,-10,-20,5,20,"yellow");
		rightArm = new FighterPart(this,5,-20,5,20,"green");
		rightArm.rotation = 45;
		leftLeg = new FighterPart(this,-10,-40,7,20,"brown");
		rightLeg = new FighterPart(this,3,-40,7,20,"cyan");
	}
	
	public void draw()
	{
		head.draw();
		body.draw();
		leftArm.draw();
		rightArm.draw();
		rightArm.rotation += 1;
		leftLeg.draw();
		rightLeg.draw();
		rightArm.rotation += 1;
	}
	
	public abstract void input();
	
	/*public void collideWith(Solid solid)
	{
		if(Physics.hasCollided(leftLeg, solid))
		{
			verticalSpeed = 0;
		}
		if(Physics.hasCollided(rightLeg, solid))
		{
			verticalSpeed = 0;
		}
	}*/
	
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
