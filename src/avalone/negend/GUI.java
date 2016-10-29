package avalone.negend;

import java.util.ArrayList;

import avalone.api.util.Point;
import avalone.negend.global.Var;

public class GUI 
{
	private ArrayList<GUIComponent> components;
	private PlayerInfo infos;
	
	public GUI(PlayerInfo infos)
	{
		components = new ArrayList<GUIComponent>();
		this.infos = infos;
	}
	
	public void addComponent(GUIComponent gc)
	{
		components.add(gc);
	}
	
	public GUIComponent getComponentPointedByMouse(Point mousePoint)
	{
		for(int i = 0;i < components.size();i++)
		{
			if(components.get(i).isPointedByMouse(mousePoint))
			{
				return components.get(i);
			}
		}
		return null;
	}
	
	public void clickAction(boolean leftClick)
	{
		Point mousePoint = Var.rend.getGuiMousePos();
		System.out.println("gui click check: mousePoint = " + mousePoint.x + ", " + mousePoint.y);
		GUIComponent gc = getComponentPointedByMouse(mousePoint);
		System.out.println("gc = " + gc);
		if(gc != null)
		{
			gc.clickAction(mousePoint,infos,leftClick);
		}
		
	}
	
	public void draw()
	{
		for(int i = 0;i < components.size();i++)
		{
			components.get(i).draw(infos);
		}
	}
}
