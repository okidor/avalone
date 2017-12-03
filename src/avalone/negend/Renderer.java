package avalone.negend;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import avalone.api.lwjgl3.AvaloneGLAPI;
import avalone.api.util.Vector;
import avalone.api.util.Point;
import avalone.negend.global.Const;
import avalone.negend.global.Var;

public class Renderer 
{
	private AvaloneGLAPI glapi;
	private int offsetX;
	private int offsetY;
	
	public Renderer(AvaloneGLAPI glapi)
	{
		this.glapi = glapi;
		offsetX = 0;
		offsetY = 0;
		//customTextures();
		Var.rend = this;
	}
	
	/*public AvaloneGLAPI getAPI()
	{
		return glapi;
	}*/
	
	/*private void customTextures()
	{
		for(int i = 0;i < blocks.size();i++)
		{
			String[] s = blocks.get(i);
		}
	}*/
	
	public int[] getImageSize(String texture)
	{
		BufferedImage img = glapi.getImgFromName(texture);
		int[] size = new int[2];
		size[0] = img.getWidth();
		size[1] = img.getHeight();
		return size;
	}
	
	public Point getMousePos()
	{
		return glapi.getMouse().clone(0);
	}
	
	public Point getMouseRightSidePos()
	{
		return glapi.getMouse().clone(15,0);
	}
	
	public Point getGuiMousePos()
	{
		Point p = glapi.getGuiMouse();
		p.y = Const.tailleFenY - p.y;
		return p;
	}
	
	public int changeValueWithWheel(int val,int mul)
	{
		return glapi.changeValueWithWheel(val, mul);
	}
	
	public void unbindTexture()
	{
		glapi.unbindTexture();
	}
	
	public void clearFilter()
	{
		glapi.clearFilter();
	}
	
	public void setOffset(int offsetX,int offsetY)
	{
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	public void draw(Point p1,Point p2,Tile tile)
	{
		if(tile.subID >= 0)
		{
			draw(p1,p2,tile.getTexture());
		}
		else
		{
			int newSub = -tile.subID - 1 - tile.ore.id;
			int	oldSub = tile.subID;
			tile.subID = newSub;
			draw(p1,p2,tile.getTexture());
			tile.subID = oldSub;
			addMineral(tile);
		}
	}
	
	public void drawRotated(Point rotatingCentre,int angle,Point p1,Point p2,String texture)
	{
		if(angle != 0)
		{
			glapi.beginRotate(rotatingCentre,angle);
		}
		draw(p1,p2,texture);
		if(angle != 0)
		{
			glapi.endRotate();
		}
	}
	
	public void drawRotated(Point rotatingCentre,int angle,Point p1,Point p2,Tile tile)
	{
		glapi.beginRotate(rotatingCentre,angle);
		draw(p1,p2,tile);
		glapi.endRotate();
	}
	
	public void drawDebug(Point p1,Point p2,Tile tile,String color)
	{
		glapi.unbindTexture();
		glapi.drawRect(p1.clone(offsetX, offsetY),p2.clone(offsetX, offsetY),color);
		tile.debug = false;
		tile.debug2 = false;
		glapi.clearFilter();
	}
	
	public void drawRadiusDebug(WorldItem item)
	{
		glapi.unbindTexture();
		glapi.drawCircle(item.pos, item.getRadius()*Const.tailleCase, "white");
	}
	
	public void drawAnimated(Point p1,Point p2,Tile tile,float liquidHeight)
	{
		int[] size = getImageSize(tile.getTexture());
		if(size[0] > size[1])
		{
			float rapport = (float)size[1]/(float)size[0];
			int textureOffset = tile.getTextureOffset();
			if(textureOffset > size[0] - size[1])
			{
				int midX = p1.x + (size[0] - textureOffset);
				glapi.drawTexturedCoordRect(p1,new Point(midX,p2.y),new Vector(textureOffset/(float)size[0],0.0f),new Vector(1.0f,liquidHeight),tile.getTexture());
				glapi.drawTexturedCoordRect(new Point(midX,p1.y),p2,new Vector(0.0f,0.0f),new Vector((size[1] - (size[0] - textureOffset))/(float)size[0],liquidHeight),tile.getTexture());
			}
			else
			{
				glapi.drawTexturedCoordRect(p1,p2,new Vector((float)textureOffset/size[0],0.0f),new Vector(rapport + (float)textureOffset/size[0],liquidHeight),tile.getTexture());
			}
			if(textureOffset >= size[0])
			{
				tile.clearTextureOffset();
			}
		}
		else
		{
			glapi.drawTexturedCoordRect(p1,p2,new Vector(0.0f,0.0f),new Vector(1.0f,liquidHeight),tile.getTexture());
		}
	}
	
	public void drawLiquid(Point p1,Point p2,Tile tile)
	{
		//float f = (float)tile.subID/(float)tile.getFlowPotential();
		int pas = 16/tile.getFlowPotential();
		int base = 16%tile.getFlowPotential();
		for(int i = tile.subID;i < tile.getFlowPotential();i++)
		{
			base = base + pas;
		}
		Point p1_2 = p1.clone(offsetX, offsetY);
		Point p2_2 = p2.clone(offsetX, -(16 - base) + offsetY);
		
		drawAnimated(p1_2,p2_2,tile,(float)base/16);
	}
	
	public float[] chooseColor(int tierVal)
	{
		float[] color = new float[4];
		color[3] = 1.0f;
		if(tierVal <= 2180)
		{
			color[0] = (float)tierVal/2180.0f + 35.0f;
			color[1] = (float)tierVal/2180.0f + 35.0f;
			color[2] = (float)tierVal/2180.0f + 35.0f;
		}
		else if(tierVal <= 4360)
		{
			tierVal = tierVal - 2180;
			color[0] = 1.0f;
			color[1] = 1.0f;
			color[2] = 1.0f - (float)tierVal/2180.0f;
		}
		else if(tierVal <= 6540)
		{
			tierVal = tierVal - 4360;
			color[0] = 1.0f;
			color[1] = 1.0f - (float)tierVal/4360.0f;
			color[2] = 0;
		}
		else if(tierVal <= 8720)
		{
			tierVal = tierVal - 6540;
			color[0] = 1.0f;
			color[1] = 0.5f - (float)tierVal/4360.0f;
			color[2] = 0;
		}
		else if(tierVal <= 10900)
		{
			tierVal = tierVal - 8720;
			color[0] = 1.0f;
			color[1] = (float)tierVal/2907.0f;
			color[2] = (float)tierVal/2907.0f;
		}
		else if(tierVal <= 13080)
		{
			tierVal = tierVal - 10900;
			color[0] = 1.0f - (float)tierVal/8720.0f;
			color[1] = 0.75f - (float)tierVal/2907.0f;
			color[2] = 0.75f + (float)tierVal/8720.0f;
		}
		else if(tierVal <= 15260)
		{
			tierVal = tierVal - 13080;
			color[0] = 1.0f - (float)tierVal/8720.0f;
			color[1] = 0.75f - (float)tierVal/2907.0f;
			color[2] = 0.75f + (float)tierVal/8720.0f;
		}
		else if(tierVal <= 17440)
		{
			tierVal = tierVal - 15260;
			color[0] = 0.75f - (float)tierVal/2907.0f;
			color[1] = 0.0f;
			color[2] = 1.0f;
		}
		else if(tierVal <= 19620)
		{
			tierVal = tierVal - 17440;
			color[0] = 0.0f;
			color[1] = (float)tierVal/2180.0f;
			color[2] = 1.0f;
		}
		else if(tierVal <= 21800)
		{
			tierVal = tierVal - 19620;
			color[0] = 0.0f;
			color[1] = 1.0f;
			color[2] = 1.0f - (float)tierVal/2180.0f;
		}
		else if(tierVal <= 23980)
		{
			tierVal = tierVal - 21800;
			color[0] = (float)tierVal/4360.0f;
			color[1] = 1.0f - (float)tierVal/2907.0f;
			color[2] = 0.0f;
		}
		else if(tierVal <= 26160)
		{
			tierVal = tierVal - 23980;
			color[0] = 0.5f - (float)tierVal/4360.0f;
			color[1] = 0.25f - (float)tierVal/8720.0f;
			color[2] = 0.0f;
		}
		else
		{
			
		}
		return color;
	}
	
	public void drawTier(Point p1,Point p2,int tierVal)
	{
		float[] color = chooseColor(tierVal);
		glapi.setFilter(color);
		draw(p1, p2,"tier.png");
		glapi.clearFilter();
	}
	
	public void draw(Point p1,Point p2,String texture)
	{
		Point p1_2 = p1.clone(offsetX, offsetY);
		Point p2_2 = p2.clone(offsetX, offsetY);
		glapi.drawTexturedRect(p1_2,p2_2,texture);
	}
	
	public void drawText(Point p1,Point p2,String text,String c)
	{
		glapi.drawText(p1,p2,text,c);
	}
	
	public void drawText(Point p1,Point p2,int number,String c)
	{
		glapi.drawText(p1,p2,number,c);
	}
	
	public void drawTextGroup(ArrayList<Point> p1s,ArrayList<Point> p2s,String text,String c)
	{
		for(int i = 0;i < p1s.size();i++)
		{
			glapi.drawText(p1s.get(i),p2s.get(i),text,c);
		}
	}
	
	public void addMineral(Tile tile)
	{
		Point p1 = tile.coord.clone(0);
		drawMineral(p1,p1.clone(Const.tailleCase),tile.ore.color);
	}
	
	public void drawMineral(Point p1,Point p2,float[] color)
	{
		glapi.setFilter(color);
		draw(p1, p2,"minerai.png");
		glapi.clearFilter();
	}
	
	public void drawLife(Point posDownLeft,Point posUpRight,int health,int maxHealth)
	{
		float lifePercentage = (float)health / (float)maxHealth;
		float[] color = new float[4];
		if(lifePercentage > 0.75f)
		{
			color[0] = (1.0f - lifePercentage) * 4;
			color[1] = 1.0f;
		}
		else
		{
			color[0] = 1.0f;
			if(lifePercentage > 0.25f)
			{
				color[1] = (lifePercentage - 0.25f) * 2;
			}
			else
			{
				color[1] = 0.0f;
			}
		}
		color[2] = 0.0f;
		color[3] = 1.0f;
		
		glapi.unbindTexture();
		glapi.drawRectWithColors(posDownLeft, new Point(posDownLeft.x + (int)(lifePercentage*100),posUpRight.y),color);

		glapi.clearFilter();
		glapi.drawTexturedRect(posDownLeft, posUpRight,"health_container.png");
	}
	
	public void drawShadow(Point p,float shadow)
	{
		Point realP = p.clone(offsetX,offsetY);
		glapi.drawAlphaRect(realP, realP.clone(Const.tailleCase),"BLACK",shadow);
	}

	public void drawFilter(Point pos,Point pos2,String color)
	{
		glapi.unbindTexture();
		glapi.drawAlphaRect(pos, pos2,color,0.5f);
		glapi.clearFilter();
	}
	
	public void drawChunkLimit()
	{
		glapi.unbindTexture();
		glapi.drawAlphaRect(new Point(0,0),new Point(1,Const.tailleFenY),"red",0.5f);
		glapi.drawAlphaRect(new Point(Const.tailleFenX-1,0),new Point(Const.tailleFenX,Const.tailleFenY),"red",0.5f);
		glapi.drawAlphaRect(new Point(),new Point(Const.tailleFenX,1),"red",0.5f);
		glapi.drawAlphaRect(new Point(0,Const.tailleFenY-1),new Point(Const.tailleFenX,Const.tailleFenY),"red",0.5f);
		glapi.clearFilter();
	}
	
	public void centerView(Point pos)
	{
		int centerX = Const.tailleFenX/2;
		int centerY = Const.tailleFenY/2;
		glapi.setView(pos.x-centerX,pos.y-centerY);
	}
	
	public void addMarker()
	{
		
	}
	
	public void drawShadowGroup(ArrayList<Point> p1s,ArrayList<Point> p2s,int light)
	{
		for(int i = 0;i < p1s.size();i++)
		{
			p1s.set(i,p1s.get(i).clone(offsetX,offsetY));
			p2s.set(i,p2s.get(i).clone(offsetX,offsetY));
		}
		float shadow = 1-((float)light/(float)Const.maxLight);
		glapi.drawMultiAlphaRect(p1s, p2s, "BLACK", shadow);
	}
}
