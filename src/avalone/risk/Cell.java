package avalone.risk;

import java.util.ArrayList;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class Cell 
{
	public ArrayList<Point> border;
	private String color;
	
	public Cell(Point[][] mapPoints,int x,int y,String color)
	{
		border = new ArrayList<Point>();
		border.add(mapPoints[x][y]);
		border.add(mapPoints[x-1][y]);
		border.add(mapPoints[x-1][y-1]);
		border.add(mapPoints[x][y-1]);
		this.color = color;
	}
	
	public void fuse(Cell otherCell)
	{
		int k = 0;
		for(int i = 0;i < border.size();i++)
		{
			for(int j = 0;j < otherCell.border.size();j++)
			{
				if(border.get(i).x == otherCell.border.get(j).x && border.get(i).y == otherCell.border.get(j).y)
				{
					otherCell.border.remove(j);
					k++;
				}
			}
		}
		System.out.println("deleted " + k + " cells");
		if(k != 0)
		{
			for(int i = 0;i < otherCell.border.size();i++)
			{
				border.add(otherCell.border.get(i));
			}
		}
	}
	
	public void drawBorder()
	{
		AvaloneGLAPI.getInstance().drawConvex(border, color);
	}
}
