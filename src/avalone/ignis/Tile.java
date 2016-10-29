package avalone.ignis;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Point;

public class Tile 
{
	public Element elem;
	private boolean destroyed;
	private Point pos;
	private Point pos2;
	public int animateX;
	public int animateY;
	
	public Tile(int x, int y)
	{
		elem = Element.Empty;
		destroyed = false;
		pos = new Point(x,y);
		pos2 = pos.clone(75);
		animateX = 0;
		animateY = 0;
	}
	
	public boolean isDestroyed()
	{
		return destroyed;
	}
	
	public void destroy()
	{
		destroyed = true;
		elem = Element.Empty;
	}
	
	public void draw(AvaloneGLAPI glapi)
	{
		if(destroyed)
		{
			glapi.drawAlphaRect(pos, pos2, "black", 0.5f);
			glapi.clearFilter();
		}
		else if(elem != Element.Empty)
		{
			glapi.drawTexturedRect(pos.clone(animateX,animateY), pos2.clone(animateX,animateY), elem.texture);
		}
		if(animateX > 0)
		{
			animateX-=5;
		}
		else if(animateX < 0)
		{
			animateX+=5;
		}
		if(animateY > 0)
		{
			animateY-=5;
		}
		else if(animateY < 0)
		{
			animateY+=5;
		}
	}
	
	public Tile clone()
	{
		Tile newTile = new Tile(pos.x,pos.y);
		newTile.destroyed = destroyed;
		newTile.elem = elem;
		return newTile;
	}
}
