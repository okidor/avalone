package avalone.penguins;

import java.util.ArrayList;

import avalone.api.util.Point;

public class Util 
{
	public static boolean isValidPath(Tile[][] tiles, Tile selectedTile, Tile oldSelectedTile)
	{
		if(selectedTile.y == oldSelectedTile.y)
		{
			if(selectedTile.x > oldSelectedTile.x)
			{
				for(int i = oldSelectedTile.x + 1;i <= selectedTile.x;i++)
				{
					if(tiles[i][selectedTile.y] == null || tiles[i][selectedTile.y].penguin > -1)
					{
						return false;
					}
				}
				return true;
			}
			else if(selectedTile.x < oldSelectedTile.x)
			{
				for(int i = selectedTile.x;i < oldSelectedTile.x;i++)
				{
					if(tiles[i][selectedTile.y] == null || tiles[i][selectedTile.y].penguin > -1)
					{
						return false;
					}
				}
				return true;
			}
			else
			{
				return false;
			}
		}
		//if diagonal constants are equal, we are on the same diagonal
		int oldc1 = oldSelectedTile.x - (8 - oldSelectedTile.y)/2;
		int oldc2 = oldSelectedTile.x - oldSelectedTile.y/2;
		int c1 = selectedTile.x - (8 - selectedTile.y)/2;
		int c2 = selectedTile.x - selectedTile.y/2;
		int selectedConst;
		int diag;
		int mul;
		if(c1 == oldc1)
		{
			selectedConst = c1;
			diag = 8;
			mul = -1;
		}
		else if(c2 == oldc2)
		{
			selectedConst = c2;
			diag = 0;
			mul = 1;
		}
		else
		{
			return false;
		}
		if(selectedTile.y > oldSelectedTile.y)
		{
			for(int i = oldSelectedTile.y + 1;i <= selectedTile.y;i++)
			{
				int tmpX = selectedConst + (diag + (mul * i))/2;
				/*System.out.println(selectedConst + " + (" + diag + " + (" + mul + " * " + i + "))/2");
				System.out.println("tmpX 1: " + tmpX);*/
				if(tiles[tmpX][i] == null || tiles[tmpX][i].penguin > -1)
				{
					return false;
				}
			}
			return true;
		}
		else if(selectedTile.y < oldSelectedTile.y)
		{
			for(int i = selectedTile.y;i < oldSelectedTile.y;i++)
			{
				int tmpX = selectedConst + (diag + (mul * i))/2;
				/*System.out.println(selectedConst + " + (" + diag + " + (" + mul + " * " + i + "))/2");
				System.out.println("tmpX 2: " + tmpX);*/
				if(tiles[tmpX][i] == null || tiles[tmpX][i].penguin > -1)
				{
					return false;
				}
			}
			return true;
		}
		else
		{
			System.out.println("error, should have entered first condition in the function");
		}
		return false;
	}
	
	public static Tile clickOnTile(Tile tiles[][], Point mouse)
	{
		//selectedTile = null;
		//Point mouse = glapi.getMouse();
		for(int i = 0;i < tiles.length;i++)
		{
			for(int j = 0;j < tiles[0].length;j++)
			{
				if(tiles[i][j] != null)
				{
					ArrayList<Point> pos = tiles[i][j].getPos();
					if(mouse.x < pos.get(0).x || mouse.x > pos.get(2).x)
					{
						continue;
					}
					else if(mouse.y < pos.get(4).y || mouse.y > pos.get(1).y)
					{
						continue;
					}
					else if(mouse.x < pos.get(1).x)		//equation of the type 'y = coeff * x + ord'
					{
						float coeff1 = getCoeff(pos.get(0), pos.get(1));
						float ord1 = pos.get(0).y - (coeff1 * pos.get(0).x); 
						
						int limitY = (int) (coeff1 * mouse.x + ord1);
						if(mouse.y > limitY)
						{
							continue;
						}
						
						float coeff4 = getCoeff(pos.get(4), pos.get(5));
						float ord4 = pos.get(4).y - (coeff4 * pos.get(4).x);
							
						limitY = (int) (coeff4 * mouse.x + ord4);
						if(mouse.y < limitY)
						{
							continue;
						}
					}
					else
					{
						float coeff2 = getCoeff(pos.get(1), pos.get(2));
						float ord2 = pos.get(1).y - (coeff2 * pos.get(1).x);
							
						int limitY = (int) (coeff2 * mouse.x + ord2);
						if(mouse.y > limitY)
						{
							continue;
						}
							
						float coeff3 = getCoeff(pos.get(3), pos.get(4));
						float ord3 = pos.get(3).y - (coeff3 * pos.get(3).x);
						
						limitY = (int) (coeff3 * mouse.x + ord3);
						if(mouse.y < limitY)
						{
							continue;
						}
					}
					//selectedTile = tiles[i][j];
					//return;
					return tiles[i][j];
				}
			}
		}
		return null;
	}
	
	public static float getCoeff(Point p1, Point p2)
	{
		return (float)(p2.y - p1.y)/(float)(p2.x - p1.x);
	}
}
