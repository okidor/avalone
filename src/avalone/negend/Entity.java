package avalone.negend;

import java.util.HashMap;

import avalone.api.util.CustomIndex2DList;
import avalone.api.util.Point;
import avalone.negend.global.Const;

public abstract class Entity 
{
	public Point pos,oldPos;
	public int tailleX; public int tailleY;
	public int vitX;
	public int vitY;
	public int nbJump;
	public int layer;
	protected int turned;
	public int tailleCase;
	public Chunk currentChunk; public Chunk currentChunkHead;
	protected Chunk[] cAround;
	//public boolean updateChunk;
	protected HashMap<Entity,Integer> tagTable;
	//private boolean destroy;
	private CustomIndex2DList<Chunk> cl;
	
	public boolean debug;
	
	protected Entity(int posX,int posY,Chunk spawn)
	{
		pos = new Point(posX,posY);
		oldPos = new Point(posX,posY);
		cAround = new Chunk[9];
		vitY = 0;
		vitX = 0;
		//updateChunk = false;
		tailleCase = Const.tailleCase;
		currentChunk = spawn;currentChunkHead = spawn;
		layer = 0;
		turned = 1;
		tagTable = new HashMap<Entity,Integer>();
		//destroy = false;
		/*System.out.println(spawn);
		System.out.println(spawn.map);*/
		cl = spawn.map.getCustomList();
		debug = false;
	}
	
	public void getChunkAround()
	{
		cAround[0] = cl.get(currentChunk.pos.x - 1, currentChunk.pos.y - 1);
		cAround[1] = cl.get(currentChunk.pos.x, currentChunk.pos.y - 1);
		cAround[2] = cl.get(currentChunk.pos.x + 1, currentChunk.pos.y - 1);
		cAround[3] = cl.get(currentChunk.pos.x - 1, currentChunk.pos.y);
		cAround[4] = currentChunk;
		cAround[5] = cl.get(currentChunk.pos.x + 1, currentChunk.pos.y);
		cAround[6] = cl.get(currentChunk.pos.x - 1, currentChunk.pos.y + 1);
		cAround[7] = cl.get(currentChunk.pos.x, currentChunk.pos.y + 1);
		cAround[8] = cl.get(currentChunk.pos.x + 1, currentChunk.pos.y + 1);
	}
	
	public void setAroundFromChunk(Chunk chunk)
	{
		cAround[0] = chunk.chunkBuffer[0];
		cAround[1] = chunk.chunkBuffer[1];
		cAround[2] = chunk.chunkBuffer[2];
		cAround[3] = chunk.chunkBuffer[3];
		cAround[4] = chunk.chunkBuffer[4];
		cAround[5] = chunk.chunkBuffer[5];
		cAround[6] = chunk.chunkBuffer[6];
		cAround[7] = chunk.chunkBuffer[7];
		cAround[8] = chunk.chunkBuffer[8];
	}
	
	/*public void destroy()
	{
		destroy = true;
	}
	
	public boolean isDestroyed()
	{
		return destroy;
	}*/
	
	protected Tile baseCase(int posX,int posY)
	{
		if(posY < 0)//comble l'erreur d'approximation des nb negatifs
		{
			posY = posY - 16;
		}
		posY = posY/tailleCase;
		if(posX < 0)
		{
			posX = posX - 16;// a verif
		}
		posX = posX/tailleCase;
		int chunkNumber = -1;
		if(posX >= 0)
		{
			if(posX < Const.tailleChunkX)
			{
				chunkNumber = 1;
			}
			else
			{
				chunkNumber = 2;
				posX = 0;
			}
		}
		else
		{
			chunkNumber = 0;
			posX = Const.tailleChunkX - 1;
		}
		
		if(posY >= 0)
		{
			if(posY < Const.tailleChunkY)
			{
				chunkNumber = chunkNumber + 3;
			}
			else
			{
				posY = 0;
			}
		}
		else
		{
			chunkNumber = chunkNumber + 6;
			posY = Const.tailleChunkY - 1;
		}
		//Const.debug("(Entity:baseCase):" + "chunkNumber = " + chunkNumber + ", posX = " + posX + ", posY = " + posY);
		if(debug)
		{
			Const.debug("(Entity:baseCase):" + "chunk = " + cAround[chunkNumber].pos.x + ", " + cAround[chunkNumber].pos.y);
		}
		debug = false;
		currentChunk.map.setChunkAround(this);
		return cAround[chunkNumber].cases[posX][posY];
	}
	
	public Tile headLeft()
	{
		/*Tile tmp = baseCase(pos.x,(pos.y+tailleY-1));
		tmp.debug = true;
		return tmp;*/
		return baseCase(pos.x,(pos.y+tailleY-1));
	}
	
	public Tile headRight()
	{
		//return baseCase((pos.x+tailleX-1)/tailleCase,pos.y/tailleCase + 1);
		return baseCase((pos.x+tailleX-1),(pos.y+tailleY-1));
	}
	
	public Tile overHeadLeft()
	{
		//return baseCase(pos.x/tailleCase,pos.y/tailleCase + 2);
		return baseCase(pos.x,(pos.y+tailleY));
	}
	
	public Tile overHeadRight()
	{
		//return baseCase((pos.x+tailleX-1)/tailleCase,pos.y/tailleCase + 2);
		return baseCase((pos.x+tailleX-1),(pos.y+tailleY));
	}
	
	public Tile currentCaseLeft()
	{
		/*if(debug)
		{
			Const.debug("(Entity:currentCaseLeft):" + "pos.x/tailleCase = " + pos.x/tailleCase);
		}
		Tile tmp = baseCase(pos.x,pos.y);
		tmp.debug2 = true;
		return tmp;*/
		return baseCase(pos.x,pos.y);
	}
	
	public Tile currentCaseRight()
	{
		/*if(debug)
		{
			Const.debug("(Entity:currentCaseRight):" + "(pos.x+tailleX-1)/tailleCase = " + (pos.x+tailleX-1)/tailleCase);
		}
		Tile tmp = baseCase((pos.x+tailleX-1),pos.y/tailleCase);
		tmp.debug = true;
		return tmp;*/
		return baseCase((pos.x+tailleX-1),pos.y);
	}
	
	public Tile underFeetLeft()
	{
		return baseCase(pos.x,pos.y - 16);
	}
	
	public Tile underFeetRight()
	{
		return baseCase((pos.x+tailleX-1),pos.y - 16);
	}
	
	public Tile leftOfFeet()
	{
		return baseCase(pos.x - 16,pos.y);
	}
	
	public Tile rightOfFeet()
	{
		return baseCase((pos.x+tailleX-1) + 16,pos.y);
	}
	
	public Tile leftOfHead()
	{
		return baseCase(pos.x - 16,pos.y + 16);
	}
	
	public Tile rightOfHead()
	{
		return baseCase((pos.x+tailleX-1) + 16,pos.y + 16);
	}
	
	public abstract void movements();
	
	public static double distance(Point p1,Point p2)
	{
		return Math.sqrt( (p1.x-p2.x) * (p1.x-p2.x) + (p1.y-p2.y) * (p1.y-p2.y));
	}
	
	public void changeChunk(Chunk newC,int n)
	{
		currentChunk = newC;
		//updateChunk = true;
		currentChunk.map.setChunkAround(this);
		if(n == 1)
		{
			//pos.x = (Const.tailleChunkX-1)*tailleCase;
			pos.x = pos.x + Const.tailleChunkX * tailleCase;
		}
		else if(n == 2)
		{
			//pos.x = 0;
			pos.x = pos.x - Const.tailleChunkX * tailleCase;
		}
		else if(n == 3)
		{
			//System.out.println("warning chunkY changed (-1)");
			//pos.y = (Const.tailleChunkY-1)*tailleCase;
			pos.y = pos.y + Const.tailleChunkY * tailleCase;
		}
		else if(n == 4)
		{
			//System.out.println("warning chunkY changed (+1)");
			//pos.y = 0;
			pos.y = pos.y - Const.tailleChunkY * tailleCase;
		}
		else if(n == -1)
		{
			
		}
		else
		{
			System.out.println("warning, illegal chunk change");
		}
		oldPos.setCoords(pos.x,pos.y);
	}
	
	/*public Chunk getChunkAround(int index)
	{
		if(index >= 0 && index < 9)
		{
			return cAround[index];
		}
		else
		{
			return null;
		}
	}*/
}
