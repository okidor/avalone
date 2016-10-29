package avalone.negend;

import avalone.api.util.Point;
import avalone.negend.global.Const;
import avalone.negend.global.Var;

import java.lang.Math;
import java.util.ArrayList;

public class Mob extends AliveEntity
{
	private int chaseRadius;
	private Player target;
	private int targetOffsetX;
	public ArrayList<Player> playerList;
	protected int attackDamage;
	
	public Mob(int posX,int posY,Chunk spawn)
	{
		super(posX,posY,spawn);
		inv = new Inventory(this,8);
		chaseRadius = 30;
		playerList = new ArrayList<Player>();
		nbJump = Const.totalJump;
		tailleX = Const.tailleCase;
		tailleY = Const.tailleCase*2;
		maxHealth = 50;
		health = 50;
		attackDamage = 10;
		initInventoryRandom();
	}
	
	public Mob(int posX,int posY,Chunk spawn,Player play)
	{
		this(posX,posY,spawn);
		playerList.add(play);
	}
	
	public Mob(int posX,int posY,Chunk spawn,ArrayList<Player> list)
	{
		this(posX,posY,spawn);
		playerList = list;
	}
	
	public void AddPlayer(Player play)
	{
		playerList.add(play);
	}
	
	public void initInventoryRandom()
	{
		int i = tierDropped();
		inv.addItem(new WeaponItem(0,i,1,"epee.png"), 1);
		//inv.addItem(new ItemBlock(Block.dirt.blockID,0), 100);
		//inv.addItem(new ItemBlock(Block.water.blockID,0), 100);
	}
	
	public void onDeath()
	{
		currentChunk.mobList.remove(this);
	}
	
	public void setTarget()
	{
		for(int i = 0;i < playerList.size();i++)
		{
			if(verifPlayer(playerList.get(i)))
			{
				target = playerList.get(i);
				return;
			}
		}
		target = null;
	}
	
	private boolean verifPlayer(Player play)
	{
		if(currentChunk == play.currentChunk)
		{
			targetOffsetX = 0;
			return verifDistance(play,play.pos);
		}
		else if(currentChunk.pos.x == play.currentChunk.pos.x-1)
		{
			Point realPos = play.pos.clone(Const.tailleChunkX*tailleCase,0);
			targetOffsetX = 1200;
			return verifDistance(play,realPos);
		}
		else if(currentChunk.pos.x == play.currentChunk.pos.x+1)
		{
			Point realPos = play.pos.clone(-Const.tailleChunkX*tailleCase,0);
			targetOffsetX = -1200;
			return verifDistance(play,realPos);
		}
		return false;
	}
	
	private boolean verifDistance(Player play,Point playerPos)
	{
		if(distance(playerPos,pos) <= chaseRadius*tailleCase)
		{
			target = play;
			return true;
		}
		else
		{
			if(target == play)
			{
				target = null;
			}
			return false;
		}
	}
	
	public void draw()
	{
		Var.rend.draw(pos, pos.clone(tailleX,tailleY), "mob" + turned + ".png");
		if(damaged)
		{
			Var.rend.drawFilter(pos, pos.clone(tailleX,tailleY),"red");
		}
	}
	
	public void movements()
	{
		turned = 1;
		setTarget();
		if(target != null)
		{
			if(pos.x + tailleX >= target.pos.x && pos.x <= target.pos.x + target.tailleX)
			{
				if(pos.y + tailleY >= target.pos.y && pos.y <= target.pos.y + target.tailleY)
				{
					if(pos.x > target.pos.x)
					{
						target.takeDamage(target, attackDamage, -10);
					}
					else
					{
						target.takeDamage(target, attackDamage, 10);
					}
					if(target.health <= 0)
					{
						target = null;
						return;
					}
				}
			}
			if(target.pos.x+targetOffsetX < pos.x)
			{
				if(leftOfFeet().getBlockSolidity().equals("solid"))
				{
					jump();
				}
				pos.x = pos.x - 2;
				turned = 0;
				Physic.checkCollisionFromLeft(this);
			}
			else if(target.pos.x+targetOffsetX > pos.x)
			{
				if(rightOfFeet().getBlockSolidity().equals("solid"))
				{
					jump();
				}
				pos.x = pos.x + 2;
				turned = 2;
				Physic.checkCollisionFromRight(this);
			}
		}
	}
	
	public int tierDropped()
	{
		double d = 0;
		double random = Var.rand.nextDouble();
		for(int i = 0;i <= 26165;i++)
		{
			d = d + (1/7700.0) * Math.pow(1.001,i/8.0);
			if(d > random)
			{
				return i;
			}
		}
		return 26165;
	}
	
	public int tierTestDropped(double random)
	{
		double d = 0;
		for(int i = 0;i <= 26165;i++)
		{
			d = d + (1/7700.0) * Math.pow(1.001,i/8.0);
			if(d > random)
			{
				return i;
			}
		}
		return 26165;
	}
	
	public void changeChunk(Chunk newC,int n)
	{
		currentChunk.mobList.remove(this);
		super.changeChunk(newC,n);
		currentChunk.mobList.add(this);
	}

	@Override
	public int chooseInventorySize() 
	{
		return 8;
	}
}
