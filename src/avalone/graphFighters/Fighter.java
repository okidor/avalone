package avalone.graphFighters;

import avalone.api.util.Point;

public abstract class Fighter 
{
	public Point pos;
	protected FighterPart head;
	protected FighterPart body;
	protected FighterPart leftArm;
	protected FighterPart rightArm;
	protected FighterPart leftLeg;
	protected FighterPart rightLeg;
	
	public Fighter(int x,int y)
	{
		pos = new Point(x,y);
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
		leftLeg.draw();
		rightLeg.draw();
	}
	
	public abstract void input();
}
