package avalone.negend;

import java.util.ArrayList;

import avalone.api.util.Point;
import avalone.negend.global.Const;

public class GUIComponent
{
	private Point localPosDownLeft;
	private Point localPosUpRight;
	private String name;
	protected ArrayList<Behavior> behaviors;
	
	public GUIComponent(Point localPosDownLeft,Point localPosUpRight,String name)
	{
		this.localPosDownLeft = localPosDownLeft;
		this.localPosUpRight = localPosUpRight;
		this.name = name;
		behaviors = new ArrayList<Behavior>();
	}
	
	public void addBehavior(Behavior behav)
	{
		behaviors.add(behav);
	}
	
	public boolean isPointedByMouse(Point mousePoint)
	{
		System.out.println("component: " + name);
		System.out.println("localPosDownLeft = " + localPosDownLeft.x + ", " + localPosDownLeft.y);
		System.out.println("localPosUpRight = " + localPosUpRight.x + ", " + localPosUpRight.y);
		if(mousePoint.x >= localPosDownLeft.x && mousePoint.x <= localPosUpRight.x)
		{
			if(mousePoint.y >= localPosDownLeft.y && mousePoint.y <= localPosUpRight.y)
			{
				return true;
			}
		}
		return false;
	}
	
	public void clickAction(Point mousePoint, PlayerInfo infos,boolean leftClick)
	{
		System.out.println("click on " + name);
		Point p = new Point(Const.tailleFenX/2,Const.tailleFenY/2);
		for(int i = 0;i < behaviors.size();i++)
		{
			behaviors.get(i).reactionToClick(Point.sub(Point.add(infos.pos, localPosDownLeft),p),Point.sub(Point.add(infos.pos, localPosUpRight),p),mousePoint,name,leftClick);
		}
	}
	
	public void draw(PlayerInfo infos)
	{
		Point p = new Point(Const.tailleFenX/2,Const.tailleFenY/2);
		for(int i = 0;i < behaviors.size();i++)
		{
			behaviors.get(i).determineDraw(Point.sub(Point.add(infos.pos, localPosDownLeft),p),Point.sub(Point.add(infos.pos, localPosUpRight),p),name,infos);
		}
	}
	
	public void drawName()
	{
		
	}
}
