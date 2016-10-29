package avalone.negend;

import java.util.ArrayList;

import avalone.negend.global.Const;
import avalone.negend.global.Var;

public class WorldItem extends Entity
{
	private Item item;
	private int amount;
	private AliveEntity target;
	private int radius;
	private boolean shouldNotSpawn;
	private int time;
	
	public WorldItem(int posX, int posY, Chunk spawn,Item item,int amount)
	{
		this(posX,posY,spawn,item,amount,0);
	}
	
	public WorldItem(int posX, int posY, Chunk spawn,Item item,int amount,int time) 
	{
		super(posX + (Const.tailleCase - Const.tailleItem)/2, posY/* + (Const.tailleCase - Const.tailleItem)/2*/, spawn);
		this.item = item;
		this.amount = amount;
		radius = 3;
		tailleX = Const.tailleItem;
		tailleY = Const.tailleItem;
		vitY = 8;
		shouldNotSpawn = false;
		this.time = time;
		if(time < 0)
		{
			time = 0;
		}
		setAroundFromChunk(spawn);
		changeItem();
	}
	
	public int getRadius()
	{
		return radius;
	}

	public void movements() 
	{
		if(target == null)
		{
			//System.out.println("target is null");
			return;
		}
		if(shouldNotSpawn)
		{
			currentChunk.itemList.remove(this);
			return;
		}
		if(time > 0)
		{
			time--;
		}
		else
		{
			if(pos.x + tailleX >= target.pos.x && pos.x <= target.pos.x + target.tailleX)
			{
				if(pos.y + tailleY >= target.pos.y && pos.y <= target.pos.y + target.tailleY)
				{
					boolean added = target.inv.addItem(item,amount);
					if(added)
					{
						boolean tmp = currentChunk.itemList.remove(this);
						if(!tmp)
						{
							Const.debug("(WorldItem:movements):element not in list !!");
							boolean test;
							for(int i = 0;i < 9;i++)
							{
								test = cAround[i].itemList.remove(this);
								if(test)
								{
									Const.debug("(WorldItem:movements):element removed from neighbors");
								}
							}
						}
					}
					return;
				}
			}
			else if(distance(pos,target.pos) < radius * Const.tailleCase)
			{
				goTowards(target);
			}
			else
			{
				repulse();
				Physic.unstuck(this);
			}
		}
	}
	
	private void repulse()
	{
		for(int i = 0;i < currentChunk.itemList.size();i++)
		{
			WorldItem item = currentChunk.itemList.get(i);
			if(item != this && item.pos.x == pos.x && item.pos.y == pos.y)
			{
				pos.x = pos.x + 2;
				Physic.checkCollisionFromRight(this);
				item.pos.x = item.pos.x - 2;
				Physic.checkCollisionFromLeft(this);
			}
		}
	}
	
	private void goTowards(Entity ent)
	{
		int distX = pos.x - ent.pos.x;
		if(distX < 0)
		{
			distX = -distX;
		}
		int spdX = radius*tailleCase - distX;
		if(spdX > 5)
		{
			spdX = 5;
		}
		
		int distY = pos.y - ent.pos.y;
		if(distY < 0)
		{
			distY = -distY;
		}
		int spdY = radius*tailleCase - distY;
		if(spdY > 5)
		{
			spdY = 5;
		}
		distX = pos.x - ent.pos.x;
		if(distX > 0)
		{
			spdX = -spdX;
		}
		pos.x = pos.x + spdX;
		distY = pos.y - ent.pos.y;
		if(distY > 0)
		{
			spdY = -spdY;
		}
		pos.y = pos.y + spdY;
	}
	
	public void changeItem()
	{
		switch(item.id)
		{
			case 1:
				item.id = 2;
				item.subID = 0;
				item.texture = Block.getBlock(2).getTexture(0);
				break;
			case 5:
				item.id = 0;
				item.subID = 0;
				item.texture = Block.getBlock(0).getTexture(0);
				shouldNotSpawn = true;
			case 10:
				item.id = 2;
				item.subID = 0;
				item.texture = Block.getBlock(2).getTexture(0);
				break;
			default:
				break;
		}
	}
	
	public void draw()
	{
		Var.rend.draw(pos,pos.clone(Const.tailleItem),item.texture);
	}
	
	public void setTarget(AliveEntity ent)
	{
		target = ent;
	}
	
	public void setTargetToNearest(ArrayList<AliveEntity> entities)
	{
		if(!entities.isEmpty())
		{
			double mindist = distance(pos,entities.get(0).pos);
			int indice = 0;
			for(int i = 1; i < entities.size();i++)
			{
				if(mindist > distance(pos,entities.get(i).pos))
				{
					mindist = distance(pos,entities.get(i).pos);
					indice = i;
				}
			}
			setTarget(entities.get(indice));
		}
	}

}
