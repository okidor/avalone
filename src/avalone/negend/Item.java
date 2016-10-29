package avalone.negend;

import avalone.api.util.Point;
import avalone.negend.global.Var;

public class Item 
{
	public int id;
	public int subID;
	private EnumItem en;
	public int tier;
	public int level;
	public int damage;
	protected String texture;
	protected int type;
	private int angle;
	
	public Item(int id,int subID,EnumItem en,int tier,int level,String texture)
	{
		this.id = id;
		this.subID = subID;
		this.en = en;
		this.tier = tier;
		this.level = level;
		damage = level*tier;
		this.texture = texture;
		angle = 0;
		setType();
	}
	
	public void setType()
	{
		type = 0;
	}
	
	public boolean isPlacable()
	{
		return en.placable;
	}
	
	public boolean isMineral()
	{
		return en.isMineral;
	}
	
	public boolean isWeapon()
	{
		return en.isWeapon;
	}
	
	public boolean isStackable()
	{
		return en.stackable;
	}
	
	public String getTexture()
	{
		return texture;
	}
	
	public void draw(Point p)
	{
		p.moveCoords(7, 7);
		Var.rend.draw(p,p.clone(16),texture);
	}
	
	private boolean checkRotation(boolean usingItemInHand)
	{
		if(usingItemInHand)
		{
			if(angle < 70)
			{
				angle = angle + 2;
				//System.out.println(angle);
			}
			else
			{
				usingItemInHand = false;
			}
		}
		else
		{
			if(angle > 0)
			{
				angle = angle - 2;
				//System.out.println(angle);
			}
		}
		return usingItemInHand;
	}
	
	public boolean drawHand(Point posPlayer,int turned,boolean usingItemInHand)
	{
		if(turned == 0)
		{
			Point p = posPlayer.clone(-7,5);
			Var.rend.drawRotated(p.clone(16, 0),angle,p,p.clone(16),texture);
			usingItemInHand = checkRotation(usingItemInHand);
		}
		else if(turned == 1)
		{
			Point p = posPlayer.clone(-15,5);
			Var.rend.drawRotated(p.clone(16, 0),angle,p,p.clone(16),texture);
			usingItemInHand = checkRotation(usingItemInHand);
		}
		else if(turned == 2)
		{
			Point p = posPlayer.clone(7,5);
			Point p2 = p.clone(16);
			p.moveCoords(16, 0);
			p2.moveCoords(-16,0);
			Var.rend.drawRotated(p.clone(-16, 0),-angle,p,p2,texture);
			usingItemInHand = checkRotation(usingItemInHand);
		}
		return usingItemInHand;
	}
}
